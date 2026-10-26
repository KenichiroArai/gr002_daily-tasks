package kmg.gr.gr002.dailytasks.carryover.application.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverItemModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSourceModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailySummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueRefModel;
import kmg.gr.gr002.dailytasks.carryover.application.service.CarryoverAggregateService;
import kmg.gr.gr002.dailytasks.carryover.application.service.CarryoverCollectService;
import kmg.gr.gr002.dailytasks.carryover.application.types.MinutesSourceTypes;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.logic.MessageLogic;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg.Gr002LogMsgTypes;
import kmg.gr.gr002.dailytasks.carryover.presentation.model.CarryoverPropertiesModel;
import kmg.gr.gr002.dailytasks.carryover.presentation.service.CarryoverService;

/**
 * 持ち越しの収集・集計を統合するサービスの実装<br>
 * <p>
 * presentation 層のサービスを実装する。収集と集計は役割ごとのサービスへ分け、単位ごとに {@link #collect()} と {@link #aggregate()} から決まった順で呼ぶ。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@Service
@Scope("prototype")
public class CarryoverServiceImpl implements CarryoverService {

    /**
     * ロガー
     *
     * @since 0.1.0
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CarryoverServiceImpl.class);

    /**
     * 残り時間の取得元ごとの行数を返す<br>
     *
     * @since 0.1.0
     *
     * @param issues
     *               解析結果
     * @param source
     *               残り時間の取得元
     *
     * @return 行数
     */
    private static long countBySource(final Collection<CarryoverIssueModel> issues, final MinutesSourceTypes source) {

        final long result = issues.stream().flatMap(issue -> {

            final List<CarryoverItemModel> items = issue.getItems();
            return items.stream();

        }).filter(item -> item.getMinutesSource() == source).count();
        return result;

    }

    /**
     * 参照を単位に分ける<br>
     * <p>
     * 日付がある参照は日付順に batchSize 件ずつ。日付がない参照は最後の 1 単位。batchSize が 1 未満のときは全件を 1 単位にする。参照がないときは空の 1 単位にする。
     * </p>
     *
     * @since 0.1.0
     *
     * @param refs
     *                 本文を持たない参照
     * @param batchSize
     *                 単位（Issue の件数）。1 未満のときは全件を 1 単位にする
     *
     * @return 単位の一覧
     */
    @SuppressWarnings("null")
    private static List<List<DailyTaskIssueRefModel>> split(final List<DailyTaskIssueRefModel> refs,
        final int batchSize) {

        final List<List<DailyTaskIssueRefModel>> result = new ArrayList<>();
        final List<DailyTaskIssueRefModel>       dated
            = refs.stream().filter(ref -> ref.getDate() != null)
                .sorted(Comparator.comparing(DailyTaskIssueRefModel::getDate)
                    .thenComparingInt(DailyTaskIssueRefModel::getNumber))
                .toList();
        final List<DailyTaskIssueRefModel>       undated
            = refs.stream().filter(ref -> ref.getDate() == null).toList();

        /* 全件を 1 単位にするか */
        if (batchSize < 1) {

            // 全件を 1 単位にする場合

            // 日付順のあとに、日付がない参照を続ける
            final List<DailyTaskIssueRefModel> all = new ArrayList<>(dated);
            all.addAll(undated);
            result.add(all);
            return result;

        }

        final int datedCount = dated.size();
        int       index      = 0;

        /* 日付のある参照が残っているか */
        boolean remaining = index < datedCount;

        while (remaining) {

            // 日付のある参照が残っている場合

            // 単位の件数だけ取り出す
            final int end = Math.min(index + batchSize, datedCount);
            result.add(new ArrayList<>(dated.subList(index, end)));
            index = end;

            /* 日付のある参照が残っているか */
            remaining = index < datedCount;

        }

        /* 日付がない参照があるか */
        final boolean hasUndated = !undated.isEmpty();

        if (hasUndated) {

            // 日付がない参照がある場合

            // 最後の単位にする
            result.add(new ArrayList<>(undated));

        }

        /* 参照がないか */
        final boolean empty = result.isEmpty();

        if (empty) {

            // 参照がない場合

            // 空の集計を 1 回書くための単位にする
            result.add(new ArrayList<>());

        }

        return result;

    }

    /**
     * 持ち越しの集計
     *
     * @since 0.1.0
     */
    private final CarryoverAggregateService carryoverAggregateService;

    /**
     * 持ち越しの収集
     *
     * @since 0.1.0
     */
    private final CarryoverCollectService carryoverCollectService;

    /**
     * メッセージの取得
     *
     * @since 0.1.0
     */
    private final MessageLogic messageLogic;

    /**
     * この単位の集計を状態へ足したか
     *
     * @since 0.1.0
     */
    private boolean batchAggregated;

    /**
     * 今の単位の番号（0 始まり）
     *
     * @since 0.1.0
     */
    private int batchIndex;

    /**
     * この単位の解析結果
     *
     * @since 0.1.0
     */
    private List<CarryoverIssueModel> batchIssues;

    /**
     * 収集と集計を繰り返す単位（Issue の件数）
     *
     * @since 0.1.0
     */
    private int batchSize;

    /**
     * 単位に分けた参照
     *
     * @since 0.1.0
     */
    private List<List<DailyTaskIssueRefModel>> batches;

    /**
     * 標準時間で補完した行数の累計
     *
     * @since 0.1.0
     */
    private long defaultCount;

    /**
     * 全件モードか
     *
     * @since 0.1.0
     */
    private boolean full;

    /**
     * 索引を作ったか
     *
     * @since 0.1.0
     */
    private boolean opened;

    /**
     * 取得したリモートの Issue の件数
     *
     * @since 0.1.0
     */
    private int remoteCount;

    /**
     * 解析して保存した Issue の件数の累計
     *
     * @since 0.1.0
     */
    private int savedCount;

    /**
     * 時間を決められなかった行数の累計
     *
     * @since 0.1.0
     */
    private long unknownCount;

    /**
     * コンストラクタ<br>
     * <p>
     * 解析結果は空にする。
     * </p>
     *
     * @since 0.1.0
     *
     * @param carryoverAggregateService
     *                                  持ち越しの集計
     * @param carryoverCollectService
     *                                  持ち越しの収集
     * @param messageLogic
     *                                  メッセージの取得
     */
    public CarryoverServiceImpl(final CarryoverAggregateService carryoverAggregateService,
        final CarryoverCollectService carryoverCollectService, final MessageLogic messageLogic) {

        this.carryoverAggregateService = carryoverAggregateService;
        this.carryoverCollectService = carryoverCollectService;
        this.messageLogic = messageLogic;
        this.batchIssues = List.of();
        this.batches = List.of();

    }

    /**
     * 直前の {@link #collect()} の結果を集計の状態へ足し、その時点の summary.json を上書きする<br>
     * <p>
     * {@link #collect()} のあとで呼ぶ。結果のログも出す。
     * </p>
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    @Override
    public void aggregate() throws IOException {

        /* この単位をまだ集計へ足していないか */
        if (!this.batchAggregated) {

            // この単位をまだ集計へ足していない場合

            // 日付順のまま足し、次の単位へ進める
            this.carryoverAggregateService.append(this.batchIssues);
            this.batchIndex++;
            this.batchAggregated = true;
            this.batchIssues = List.of();

        }

        final CarryoverSummaryModel summary = this.carryoverAggregateService.summary();
        this.carryoverAggregateService.saveSummary(summary);
        this.logResult(summary);

    }

    /**
     * 次の単位の Issue を取得して解析し、Issue ごとの JSON を出力する<br>
     * <p>
     * {@link #hasNext()} が true のあとで呼ぶ。差分モードでは、Issue 番号が大きい順の最新の指定件数（設定の
     * recentCount）を除き、保存済みの JSON と更新日時が同じ Issue は解析しない。集計は {@link #aggregate()} で行う。
     * </p>
     *
     * @since 0.1.0
     *
     * @return この単位で解析して保存した Issue の件数
     *
     * @throws IOException
     *                     取得、読み込みまたは書き込みに失敗した場合
     */
    @Override
    public int collect() throws IOException {

        int result = 0;

        /* 索引をまだ作っていないか */
        if (!this.opened) {

            // 索引をまだ作っていない場合

            // 索引を作り、単位に分ける
            this.open();

        }

        /* 単位が残っているか */
        final int     batchCount = this.batches.size();
        final boolean finished   = this.batchIndex >= batchCount;

        if (finished) {

            // 単位が残っていない場合

            // 解析せずに 0 を返す
            this.batchIssues = List.of();
            return result;

        }

        final List<DailyTaskIssueRefModel> batch = this.batches.get(this.batchIndex);
        result = this.carryoverCollectService.collectBatch(batch);
        final List<CarryoverIssueModel> collected = this.carryoverCollectService.getBatchIssues();
        this.batchIssues = collected;
        this.savedCount = this.savedCount + result;
        this.defaultCount = this.defaultCount
            + CarryoverServiceImpl.countBySource(collected, MinutesSourceTypes.DEFAULT);
        this.unknownCount = this.unknownCount
            + CarryoverServiceImpl.countBySource(collected, MinutesSourceTypes.UNKNOWN);
        this.batchAggregated = false;
        return result;

    }

    /**
     * 次の単位があるかを返す<br>
     * <p>
     * 最初の呼び出しで、本文を持たない索引を作り、設定の batchSize 件ずつに分ける。
     * </p>
     *
     * @since 0.1.0
     *
     * @return true：次の単位がある、false：単位が残っていない
     *
     * @throws IOException
     *                     取得または読み込みに失敗した場合
     */
    @Override
    public boolean hasNext() throws IOException {

        boolean result = false;

        /* 索引をまだ作っていないか */
        if (!this.opened) {

            // 索引をまだ作っていない場合

            // 索引を作り、単位に分ける
            this.open();

        }

        final int batchCount = this.batches.size();
        result = this.batchIndex < batchCount;
        return result;

    }

    /**
     * 収集の設定とモードを受け取り、フィールドに保持する<br>
     * <p>
     * 設定はデータの取得元と保存先に変換し、収集と集計のサービスへ渡す。
     * </p>
     *
     * @since 0.1.0
     *
     * @param properties
     *                   持ち越しの収集の設定
     * @param full
     *                   true：全件モード（すべての Issue を解析し直す）、false：差分モード
     */
    @SuppressWarnings("hiding")
    @Override
    public void initialize(final CarryoverPropertiesModel properties, final boolean full) {

        final CarryoverSourceModel source = new CarryoverSourceModel(properties);
        this.full = full;
        this.batchSize = properties.getBatchSize();
        this.opened = false;
        this.batchIndex = 0;
        this.batchAggregated = false;
        this.batchIssues = List.of();
        this.batches = List.of();
        this.savedCount = 0;
        this.remoteCount = 0;
        this.defaultCount = 0;
        this.unknownCount = 0;
        this.carryoverCollectService.initialize(source, properties.getRecentCount(), full);
        this.carryoverAggregateService.initialize(source.getDataDir());

    }

    /**
     * 本文を持たない索引を作り、単位に分ける<br>
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     取得または読み込みに失敗した場合
     */
    private void open() throws IOException {

        final List<DailyTaskIssueRefModel> refs = this.carryoverCollectService.fetchRefs();
        this.remoteCount = refs.size();
        this.batches = CarryoverServiceImpl.split(refs, this.batchSize);
        this.opened = true;

    }

    /**
     * 収集と集計の結果をログに出す<br>
     *
     * @since 0.1.0
     *
     * @param summary
     *                画面用の集計
     */
    private void logResult(final CarryoverSummaryModel summary) {

        final String                  collectMessage = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12000);
        final String                  modeName       = this.modeName();
        final List<DailySummaryModel> days           = summary.getDays();
        final int                     dayCount       = days.size();
        CarryoverServiceImpl.LOGGER.info(collectMessage, modeName, this.remoteCount, this.savedCount, dayCount);

        final String minutesSourceMessage = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12001);
        CarryoverServiceImpl.LOGGER.info(minutesSourceMessage, this.defaultCount, this.unknownCount);

    }

    /**
     * ログに出すモードの名前を返す<br>
     *
     * @since 0.1.0
     *
     * @return モードの名前（全件 / 差分）
     */
    private String modeName() {

        String result = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12012);

        /* 全件モードか */
        if (!this.full) {

            // 全件モードではない場合

            // 差分の名前を返す
            return result;

        }

        result = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12011);
        return result;

    }

}
