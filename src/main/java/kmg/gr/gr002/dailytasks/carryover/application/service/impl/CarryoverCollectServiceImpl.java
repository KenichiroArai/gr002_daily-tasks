package kmg.gr.gr002.dailytasks.carryover.application.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private CarryoverDataLogic carryoverDataLogic;

    /**
     * Issue 本文の解析
     *
     * @since 0.1.0
     */
    @Autowired
    private CarryoverParseLogic carryoverParseLogic;

    /**
     * 日々のタスク Issue の取得
     *
     * @since 0.1.0
     */
    @Autowired
    private DailyTaskIssueLogic dailyTaskIssueLogic;

    /**
     * 項目ごとの標準時間の読み込み
     *
     * @since 0.1.0
     */
    @Autowired
    private DefaultMinutesLogic defaultMinutesLogic;

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
     */
    public CarryoverCollectServiceImpl() {

        this.issues = new TreeMap<>();
        this.pendingSaves = new ArrayList<>();
        this.remoteIssues = List.of();

    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public Map<Integer, CarryoverIssueModel> getIssues() {

        final Map<Integer, CarryoverIssueModel> result = this.issues;
        return result;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getRemoteCount() {

        final int result = this.remoteIssues.size();
        return result;

    }

    /**
     * {@inheritDoc}
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
     * {@inheritDoc}
     */
    @Override
    public void saveIssues() throws IOException {

        for (final CarryoverIssueModel parsed : this.pendingSaves) {

            this.carryoverDataLogic.saveIssue(parsed);

        }

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

}
