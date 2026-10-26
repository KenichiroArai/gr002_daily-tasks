package kmg.gr.gr002.dailytasks.carryover.application.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverDataLogic;
import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverParseLogic;
import kmg.gr.gr002.dailytasks.carryover.application.logic.DailyTaskIssueLogic;
import kmg.gr.gr002.dailytasks.carryover.application.logic.DefaultMinutesLogic;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssuesModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSourceModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueRefModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DefaultMinutesModel;
import kmg.gr.gr002.dailytasks.carryover.application.service.CarryoverCollectService;

/**
 * 持ち越しの収集サービスの実装<br>
 * <p>
 * Issue の取得、差分判定、解析、解析結果の保存を順に行う。集計は行わない。
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
public class CarryoverCollectServiceImpl implements CarryoverCollectService {

    /**
     * Issue 番号が大きい順に指定件数を選んだときの、最小の Issue 番号を返す<br>
     *
     * @since 0.1.0
     *
     * @param issues
     *               取得した Issue
     * @param count
     *               件数
     *
     * @return 最小の Issue 番号。Issue がない場合は 0
     */
    private static int recentFrom(final List<DailyTaskIssueModel> issues, final int count) {

        @SuppressWarnings("null")
        final int result = issues.stream().map(DailyTaskIssueModel::getNumber).sorted(Comparator.reverseOrder())
            .limit(count).min(Comparator.naturalOrder()).orElse(0);
        return result;

    }

    /**
     * Issue 番号が大きい順に指定件数を選んだときの、最小の Issue 番号を返す<br>
     *
     * @since 0.1.0
     *
     * @param issues
     *               取得した参照
     * @param count
     *               件数
     *
     * @return 最小の Issue 番号。Issue がない場合は 0
     */
    private static int recentFromRefs(final List<DailyTaskIssueRefModel> issues, final int count) {

        @SuppressWarnings("null")
        final int result = issues.stream().map(DailyTaskIssueRefModel::getNumber).sorted(Comparator.reverseOrder())
            .limit(count).min(Comparator.naturalOrder()).orElse(0);
        return result;

    }

    /**
     * 持ち越しの解析結果の読み書き
     *
     * @since 0.1.0
     */
    private final CarryoverDataLogic carryoverDataLogic;

    /**
     * Issue 本文の解析
     *
     * @since 0.1.0
     */
    private final CarryoverParseLogic carryoverParseLogic;

    /**
     * 日々のタスク Issue の取得
     *
     * @since 0.1.0
     */
    private final DailyTaskIssueLogic dailyTaskIssueLogic;

    /**
     * 項目ごとの標準時間の読み込み
     *
     * @since 0.1.0
     */
    private final DefaultMinutesLogic defaultMinutesLogic;

    /**
     * 直前の単位で扱った解析結果
     *
     * @since 0.1.0
     */
    private List<CarryoverIssueModel> batchIssues;

    /**
     * 全件モードか
     *
     * @since 0.1.0
     */
    private boolean full;

    /**
     * 標準時間を解析ロジックへ渡したか
     *
     * @since 0.1.0
     */
    private boolean minutesReady;

    /**
     * 保存済みと今回解析した Issue
     *
     * @since 0.1.0
     */
    private CarryoverIssuesModel issues;

    /**
     * 今回解析し、まだ保存していない Issue
     *
     * @since 0.1.0
     */
    private final List<CarryoverIssueModel> pendingSaves;

    /**
     * 差分モードで更新日時に関係なく解析し直す最新の Issue の件数
     *
     * @since 0.1.0
     */
    private int recentCount;

    /**
     * 更新日時に関係なく解析し直す Issue 番号の下限
     *
     * @since 0.1.0
     */
    private int recentFrom;

    /**
     * 取得したリモートの Issue
     *
     * @since 0.1.0
     */
    private List<DailyTaskIssueModel> remoteIssues;

    /**
     * コンストラクタ<br>
     * <p>
     * 解析結果は空にする。
     * </p>
     *
     * @since 0.1.0
     *
     * @param carryoverDataLogic
     *                            持ち越しの解析結果の読み書き
     * @param carryoverParseLogic
     *                            Issue 本文の解析
     * @param dailyTaskIssueLogic
     *                            日々のタスク Issue の取得
     * @param defaultMinutesLogic
     *                            項目ごとの標準時間の読み込み
     */
    public CarryoverCollectServiceImpl(final CarryoverDataLogic carryoverDataLogic,
        final CarryoverParseLogic carryoverParseLogic, final DailyTaskIssueLogic dailyTaskIssueLogic,
        final DefaultMinutesLogic defaultMinutesLogic) {

        this.carryoverDataLogic = carryoverDataLogic;
        this.carryoverParseLogic = carryoverParseLogic;
        this.dailyTaskIssueLogic = dailyTaskIssueLogic;
        this.defaultMinutesLogic = defaultMinutesLogic;
        this.issues = new CarryoverIssuesModel();
        this.pendingSaves = new ArrayList<>();
        this.remoteIssues = List.of();
        this.batchIssues = List.of();
        this.minutesReady = false;

    }

    /**
     * 差分判定して解析する<br>
     * <p>
     * {@link #fetch()} のあとで呼ぶ。保存はしない。解析の途中で失敗したときは、今回解析した Issue はまだ保存されていない。
     * </p>
     *
     * @since 0.1.0
     *
     * @return 解析した Issue の件数
     */
    @Override
    public int analyze() {

        int result = 0;
        this.pendingSaves.clear();

        for (final DailyTaskIssueModel remoteIssue : this.remoteIssues) {

            final int number = remoteIssue.getNumber();

            /* 解析し直す必要があるか */
            final CarryoverIssueModel stored      = this.issues.find(number);
            final String              updatedAt   = remoteIssue.getUpdatedAt();
            final boolean             needsUpdate = this.needsUpdate(stored, number, updatedAt, this.recentFrom);

            if (!needsUpdate) {

                // 解析し直す必要がない場合

                // 次の Issue へ進む
                continue;

            }

            final CarryoverIssueModel parsed = this.carryoverParseLogic.parse(remoteIssue);
            this.pendingSaves.add(parsed);
            this.issues.put(number, parsed);
            result++;

        }

        return result;

    }

    /**
     * 渡された参照だけを差分判定して解析し、保存する<br>
     * <p>
     * {@link #fetchRefs()} のあとで呼ぶ。戻り値の解析結果は {@link #getBatchIssues()} でも返す。
     * </p>
     *
     * @since 0.1.0
     *
     * @param batch
     *              この単位の参照（日付順）
     *
     * @return 解析して保存した Issue の件数
     *
     * @throws IOException
     *                     取得、読み込みまたは書き込みに失敗した場合
     */
    @Override
    public int collectBatch(final List<DailyTaskIssueRefModel> batch) throws IOException {

        int result = 0;
        this.prepareMinutes();
        final List<CarryoverIssueModel> collected = new ArrayList<>();

        for (final DailyTaskIssueRefModel ref : batch) {

            final int                 number    = ref.getNumber();
            final CarryoverIssueModel stored    = this.carryoverDataLogic.loadIssue(number);
            final String              updatedAt = ref.getUpdatedAt();

            /* 解析し直す必要があるか */
            final boolean needsUpdate = this.needsUpdate(stored, number, updatedAt, this.recentFrom);

            if (!needsUpdate) {

                // 解析し直す必要がない場合

                // 保存済みの解析結果をこの単位の結果にする
                collected.add(stored);
                continue;

            }

            final DailyTaskIssueModel remoteIssue = this.dailyTaskIssueLogic.fetchIssue(number);
            final CarryoverIssueModel parsed      = this.carryoverParseLogic.parse(remoteIssue);
            this.carryoverDataLogic.saveIssue(parsed);
            collected.add(parsed);
            result++;

        }

        this.batchIssues = collected;
        return result;

    }

    /**
     * 標準時間、保存済みの解析結果、リモートの Issue を取得する<br>
     * <p>
     * {@link #initialize(CarryoverSourceModel, int, boolean)} のあとで呼ぶ。標準時間は解析ロジックへ渡す。
     * </p>
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     読み込みまたは取得に失敗した場合
     */
    @Override
    public void fetch() throws IOException {

        /* 標準時間の読み込み */
        this.prepareMinutes();

        /* 保存済みの解析結果の読み込み */
        final CarryoverIssuesModel stored = this.carryoverDataLogic.loadIssues();
        this.issues = stored.copy();
        this.pendingSaves.clear();

        /* Issue の取得 */
        this.remoteIssues = this.dailyTaskIssueLogic.fetchAllIssues();
        this.recentFrom = CarryoverCollectServiceImpl.recentFrom(this.remoteIssues, this.recentCount);

    }

    /**
     * 本文を持たない参照を取得し、タイトルから日付を付ける<br>
     * <p>
     * 標準時間の読み込みは、最初の呼び出しで 1 回行う。差分判定の下限もここで決める。
     * </p>
     *
     * @since 0.1.0
     *
     * @return 日々のタスク Issue の参照（作成順）
     *
     * @throws IOException
     *                     読み込みまたは取得に失敗した場合
     */
    @Override
    public List<DailyTaskIssueRefModel> fetchRefs() throws IOException {

        final List<DailyTaskIssueRefModel> result = new ArrayList<>();
        this.prepareMinutes();
        final List<DailyTaskIssueRefModel> fetched = this.dailyTaskIssueLogic.fetchIssueRefs();

        for (final DailyTaskIssueRefModel fetchedRef : fetched) {

            final String date = this.carryoverParseLogic.parseTitleDate(fetchedRef.getTitle());
            result.add(new DailyTaskIssueRefModel(fetchedRef.getNumber(), fetchedRef.getTitle(), fetchedRef.getState(),
                fetchedRef.getUpdatedAt(), date));

        }

        this.recentFrom = CarryoverCollectServiceImpl.recentFromRefs(result, this.recentCount);
        return result;

    }

    /**
     * 直前の {@link #collectBatch(List)} で扱った解析結果を返す<br>
     *
     * @since 0.1.0
     *
     * @return その単位の解析結果。未収集の場合は空
     */
    @Override
    public List<CarryoverIssueModel> getBatchIssues() {

        final List<CarryoverIssueModel> result = this.batchIssues;
        return result;

    }

    /**
     * 保存済みと今回解析した Issue を返す<br>
     *
     * @since 0.1.0
     *
     * @return Issue 番号と解析結果の対応。未取得の場合は空
     */
    @Override
    public CarryoverIssuesModel getIssues() {

        final CarryoverIssuesModel result = this.issues;
        return result;

    }

    /**
     * 取得したリモートの Issue の件数を返す<br>
     *
     * @since 0.1.0
     *
     * @return 件数。未取得の場合は 0
     */
    @Override
    public int getRemoteCount() {

        final int result = this.remoteIssues.size();
        return result;

    }

    /**
     * 収集の取得元と差分判定の条件を受け取り、フィールドに保持する<br>
     * <p>
     * Issue の取得、標準時間の読み込み、解析結果の読み書きは、それぞれが必要とする値で初期化する。
     * </p>
     *
     * @since 0.1.0
     *
     * @param source
     *                    データの取得元と保存先
     * @param recentCount
     *                    差分モードで更新日時に関係なく解析し直す最新の Issue の件数
     * @param full
     *                    true：全件モード（すべての Issue を解析し直す）、false：差分モード
     */
    @SuppressWarnings("hiding")
    @Override
    public void initialize(final CarryoverSourceModel source, final int recentCount, final boolean full) {

        this.recentCount = recentCount;
        this.full = full;
        this.dailyTaskIssueLogic.initialize(source.getRepository(), source.getToken());
        this.defaultMinutesLogic.initialize(source.getDefaultMinutesFile());
        this.carryoverDataLogic.initialize(source.getDataDir());

    }

    /**
     * Issue を解析し直す必要があるかを返す<br>
     *
     * @since 0.1.0
     *
     * @param stored
     *                    保存済みの解析結果。未保存の場合は null
     * @param issueNumber
     *                    Issue 番号
     * @param updatedAt
     *                    取得した更新日時
     * @param recentFrom
     *                    更新日時に関係なく解析し直す Issue 番号の下限
     *
     * @return true：解析し直す、false：保存済みの解析結果を使う
     */
    @SuppressWarnings("hiding")
    private boolean needsUpdate(final CarryoverIssueModel stored, final int issueNumber, final String updatedAt,
        final int recentFrom) {

        boolean result = true;

        /* 全件モードか */
        if (this.full) {

            // 全件モードの場合

            // 解析し直す
            return result;

        }

        /* 保存済みの解析結果がないか */
        if (stored == null) {

            // 保存済みの解析結果がない場合

            // 解析し直す
            return result;

        }

        /* 直近の Issue か */
        if (issueNumber >= recentFrom) {

            // 直近の Issue の場合

            // 更新日時に関係なく解析し直す
            return result;

        }

        final String storedUpdatedAt = stored.getUpdatedAt();
        result = !updatedAt.equals(storedUpdatedAt);
        return result;

    }

    /**
     * 標準時間を読み込み、解析ロジックへ渡す<br>
     * <p>
     * 最初の呼び出しだけ読む。
     * </p>
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     読み込みに失敗した場合
     */
    private void prepareMinutes() throws IOException {

        /* 標準時間を読み込み済みか */
        if (this.minutesReady) {

            // 標準時間を読み込み済みの場合

            // 読み込み直さない
            return;

        }

        final DefaultMinutesModel defaultMinutes = this.defaultMinutesLogic.loadDefaultMinutes();
        this.carryoverParseLogic.initialize(defaultMinutes);
        this.minutesReady = true;

    }

    /**
     * 今回解析した Issue を保存する<br>
     * <p>
     * {@link #analyze()} のあとで呼ぶ。
     * </p>
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    @Override
    public void saveIssues() throws IOException {

        for (final CarryoverIssueModel parsed : this.pendingSaves) {

            this.carryoverDataLogic.saveIssue(parsed);

        }

    }

}
