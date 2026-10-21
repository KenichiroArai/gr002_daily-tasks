package kmg.gr.gr002.dailytasks.carryover.application.service.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverAggregateLogic;
import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverDataLogic;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.service.CarryoverAggregateService;

/**
 * 持ち越しの集計サービスの実装<br>
 * <p>
 * 解析結果から日別の集計を作り、画面用の集計を保存する。Issue の取得と解析は行わない。
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
public class CarryoverAggregateServiceImpl implements CarryoverAggregateService {

    /**
     * 日別の集計
     *
     * @since 0.1.0
     */
    @Autowired
    private CarryoverAggregateLogic carryoverAggregateLogic;

    /**
     * 画面用の集計の保存
     *
     * @since 0.1.0
     */
    @Autowired
    private CarryoverDataLogic carryoverDataLogic;

    /**
     * 解析結果から日別の集計を作る<br>
     * <p>
     * 保存はしない。
     * </p>
     *
     * @since 0.1.0
     *
     * @param issues
     *               持ち越しの解析結果
     *
     * @return 画面用の集計
     */
    @Override
    public CarryoverSummaryModel aggregate(final Collection<CarryoverIssueModel> issues) {

        final CarryoverSummaryModel result = this.carryoverAggregateLogic.aggregate(issues);
        return result;

    }

    /**
     * 集計の保存先を受け取り、フィールドに保持する<br>
     *
     * @since 0.1.0
     *
     * @param dataDir
     *                保存先のディレクトリ（例: docs/data）
     */
    @Override
    public void initialize(final Path dataDir) {

        this.carryoverDataLogic.initialize(dataDir);

    }

    /**
     * 画面用の集計を保存する<br>
     *
     * @since 0.1.0
     *
     * @param summary
     *                画面用の集計
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    @Override
    public void saveSummary(final CarryoverSummaryModel summary) throws IOException {

        this.carryoverDataLogic.saveSummary(summary);

    }

}
