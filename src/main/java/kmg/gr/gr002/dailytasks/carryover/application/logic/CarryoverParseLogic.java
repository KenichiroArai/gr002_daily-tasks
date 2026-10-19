package kmg.gr.gr002.dailytasks.carryover.application.logic;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DefaultMinutesModel;

/**
 * 日々のタスク Issue から持ち越し項目を解析する<br>
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
public interface CarryoverParseLogic {

    /**
     * 標準時間を受け取り、フィールドに保持する<br>
     * <p>
     * {@link #parse(DailyTaskIssueModel)} より前に呼ぶ。
     * </p>
     *
     * @since 0.1.0
     *
     * @param defaultMinutes
     *                       時間表記がない行を補完する項目ごとの標準時間
     */
    void initialize(DefaultMinutesModel defaultMinutes);

    /**
     * Issue を解析する<br>
     *
     * @since 0.1.0
     *
     * @param issue
     *              日々のタスク Issue
     *
     * @return 持ち越しの解析結果
     */
    CarryoverIssueModel parse(DailyTaskIssueModel issue);

}
