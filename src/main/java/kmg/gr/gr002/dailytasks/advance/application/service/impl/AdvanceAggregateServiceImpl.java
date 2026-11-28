package kmg.gr.gr002.dailytasks.advance.application.service.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import kmg.gr.gr002.dailytasks.advance.application.logic.AdvanceAggregateLogic;
import kmg.gr.gr002.dailytasks.advance.application.logic.AdvanceDataLogic;
import kmg.gr.gr002.dailytasks.advance.application.service.AdvanceAggregateService;
import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverDataLogic;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverAggregationModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssuesModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;

/**
 * 先行の集計サービスの実装<br>
 * <p>
 * 解析結果から日別の集計を作り、画面用の集計を advance/summary.json に保存する。Issue の取得と解析は行わない。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.2.0
 *
 * @version 0.2.0
 */
@Service
@Scope("prototype")
public class AdvanceAggregateServiceImpl implements AdvanceAggregateService {

    /**
     * 単位をまたいで持ち越す集計の状態
     *
     * @since 0.2.0
     */
    private CarryoverAggregationModel aggregation;

    /**
     * 日別の集計
     *
     * @since 0.2.0
     */
    private final AdvanceAggregateLogic advanceAggregateLogic;

    /**
     * 画面用の集計の保存
     *
     * @since 0.2.0
     */
    private final AdvanceDataLogic advanceDataLogic;

    /**
     * Issue ごとの解析結果の読み込み（持ち越しと共有）
     *
     * @since 0.2.0
     */
    private final CarryoverDataLogic carryoverDataLogic;

    /**
     * コンストラクタ<br>
     *
     * @since 0.2.0
     *
     * @param advanceAggregateLogic
     *                              日別の集計
     * @param advanceDataLogic
     *                              画面用の集計の保存
     * @param carryoverDataLogic
     *                              Issue ごとの解析結果の読み込み
     */
    public AdvanceAggregateServiceImpl(final AdvanceAggregateLogic advanceAggregateLogic,
        final AdvanceDataLogic advanceDataLogic, final CarryoverDataLogic carryoverDataLogic) {

        this.advanceAggregateLogic = advanceAggregateLogic;
        this.advanceDataLogic = advanceDataLogic;
        this.carryoverDataLogic = carryoverDataLogic;
        this.aggregation = new CarryoverAggregationModel();

    }

    /**
     * 解析結果から日別の集計を作る<br>
     * <p>
     * 保存はしない。
     * </p>
     *
     * @since 0.2.0
     *
     * @param issues
     *               持ち越しの解析結果
     *
     * @return 画面用の集計
     */
    @Override
    public CarryoverSummaryModel aggregate(final Collection<CarryoverIssueModel> issues) {

        final CarryoverSummaryModel result = this.advanceAggregateLogic.aggregate(issues);
        return result;

    }

    /**
     * 解析結果を、実行中の集計の状態へ日付順のまま足す<br>
     *
     * @since 0.2.0
     *
     * @param issues
     *               この単位の解析結果（日付順）
     */
    @Override
    public void append(final List<CarryoverIssueModel> issues) {

        for (final CarryoverIssueModel issue : issues) {

            this.advanceAggregateLogic.add(this.aggregation, issue);

        }

    }

    /**
     * 実行中の集計の状態をクリアする<br>
     *
     * @since 0.2.0
     */
    @Override
    public void clear() {

        this.aggregation = new CarryoverAggregationModel();

    }

    /**
     * 集計の保存先を受け取り、フィールドに保持する<br>
     *
     * @since 0.2.0
     *
     * @param dataDir
     *                保存先のディレクトリ（例: docs/data）
     */
    @Override
    public void initialize(final Path dataDir) {

        this.advanceDataLogic.initialize(dataDir);
        this.carryoverDataLogic.initialize(dataDir);

    }

    /**
     * 保存済みの Issue ごとの解析結果をすべて読み込み、先行の画面用集計を作り直す<br>
     *
     * @since 0.2.0
     *
     * @return 画面用の集計
     *
     * @throws IOException
     *                     読み込みに失敗した場合
     */
    @Override
    public CarryoverSummaryModel rebuildFromStored() throws IOException {

        final CarryoverIssuesModel  stored = this.carryoverDataLogic.loadIssues();
        final CarryoverSummaryModel result = this.advanceAggregateLogic.aggregate(stored.values());
        return result;

    }

    /**
     * ここまでの集計の状態から画面用の集計を作り、保存する<br>
     *
     * @since 0.2.0
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    @Override
    public void saveCurrentSummary() throws IOException {

        final CarryoverSummaryModel summary = this.advanceAggregateLogic.toSummary(this.aggregation);
        this.saveSummary(summary);

    }

    /**
     * 画面用の集計を保存する<br>
     *
     * @since 0.2.0
     *
     * @param summary
     *                画面用の集計
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    @Override
    public void saveSummary(final CarryoverSummaryModel summary) throws IOException {

        this.advanceDataLogic.saveSummary(summary);

    }

}
