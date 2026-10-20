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
     * {@inheritDoc}
     */
    @Override
    public CarryoverSummaryModel aggregate(final Collection<CarryoverIssueModel> issues) {

        final CarryoverSummaryModel result = this.carryoverAggregateLogic.aggregate(issues);
        return result;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final Path dataDir) {

        this.carryoverDataLogic.initialize(dataDir);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void saveSummary(final CarryoverSummaryModel summary) throws IOException {

        this.carryoverDataLogic.saveSummary(summary);

    }

}
