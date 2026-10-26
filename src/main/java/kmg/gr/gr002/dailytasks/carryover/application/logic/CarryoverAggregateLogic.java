package kmg.gr.gr002.dailytasks.carryover.application.logic;

import java.util.Collection;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverAggregationModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;

/**
 * 持ち越しの解析結果を日別に集計する<br>
 * <p>
 * application 層が domain 層に求めるインタフェース。実装は domain 層に置く。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public interface CarryoverAggregateLogic {

    /**
     * 解析結果を 1 件、集計の状態へ足す<br>
     * <p>
     * 日付がある Issue は、呼ぶ側が日付順に渡す。
     * </p>
     *
     * @since 0.1.0
     *
     * @param aggregation
     *                    単位をまたいで持ち越す集計の状態
     * @param issue
     *                    持ち越しの解析結果
     */
    void add(CarryoverAggregationModel aggregation, CarryoverIssueModel issue);

    /**
     * 解析結果を集計する<br>
     *
     * @since 0.1.0
     *
     * @param issues
     *               持ち越しの解析結果
     *
     * @return 画面用の持ち越しの集計
     */
    CarryoverSummaryModel aggregate(Collection<CarryoverIssueModel> issues);

    /**
     * ここまでの集計の状態から画面用の集計を作る<br>
     *
     * @since 0.1.0
     *
     * @param aggregation
     *                    単位をまたいで持ち越す集計の状態
     *
     * @return 画面用の持ち越しの集計
     */
    CarryoverSummaryModel toSummary(CarryoverAggregationModel aggregation);

}
