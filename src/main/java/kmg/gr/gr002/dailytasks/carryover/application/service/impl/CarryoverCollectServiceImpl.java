package kmg.gr.gr002.dailytasks.carryover.application.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverDataLogic;
import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverParseLogic;
import kmg.gr.gr002.dailytasks.carryover.application.logic.DailyTaskIssueLogic;
import kmg.gr.gr002.dailytasks.carryover.application.logic.DefaultMinutesLogic;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSourceModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueModel;
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

        final int result = issues.stream().map(DailyTaskIssueModel::getNumber).sorted(Comparator.reverseOrder())
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
     * 全件モードか
     *
     * @since 0.1.0
     */
    private boolean full;

    /**
     * 保存済みと今回解析した Issue
     *
     * @since 0.1.0
     */
    private Map<Integer, CarryoverIssueModel> issues;

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
        this.issues = new TreeMap<>();
        this.pendingSaves = new ArrayList<>();
        this.remoteIssues = List.of();

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

            final Integer number = remoteIssue.getNumber();

            if (!this.needsUpdate(this.issues.get(number), remoteIssue, this.recentFrom)) {

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
        final DefaultMinutesModel defaultMinutes = this.defaultMinutesLogic.loadDefaultMinutes();
        this.carryoverParseLogic.initialize(defaultMinutes);

        /* 保存済みの解析結果の読み込み */
        final Map<Integer, CarryoverIssueModel> stored = this.carryoverDataLogic.loadIssues();
        this.issues = new TreeMap<>(stored);
        this.pendingSaves.clear();

        /* Issue の取得 */
        this.remoteIssues = this.dailyTaskIssueLogic.fetchAllIssues();
        this.recentFrom = CarryoverCollectServiceImpl.recentFrom(this.remoteIssues, this.recentCount);

    }

    /**
     * 保存済みと今回解析した Issue を返す<br>
     *
     * @since 0.1.0
     *
     * @return Issue 番号と解析結果の対応。未取得の場合は空
     */
    @Override
    public Map<Integer, CarryoverIssueModel> getIssues() {

        final Map<Integer, CarryoverIssueModel> result = this.issues;
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
     * @param remoteIssue
     *                    取得した Issue
     * @param recentFrom
     *                    更新日時に関係なく解析し直す Issue 番号の下限
     *
     * @return true：解析し直す、false：保存済みの解析結果を使う
     */
    @SuppressWarnings("hiding")
    private boolean needsUpdate(final CarryoverIssueModel stored, final DailyTaskIssueModel remoteIssue,
        final int recentFrom) {

        boolean result = true;

        if (this.full) {

            return result;

        }

        if (stored == null) {

            return result;

        }

        if (remoteIssue.getNumber() >= recentFrom) {

            return result;

        }

        result = !remoteIssue.getUpdatedAt().equals(stored.getUpdatedAt());
        return result;

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
