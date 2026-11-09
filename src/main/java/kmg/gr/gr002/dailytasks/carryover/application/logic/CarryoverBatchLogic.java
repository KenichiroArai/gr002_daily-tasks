package kmg.gr.gr002.dailytasks.carryover.application.logic;

import java.util.List;

import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueRefModel;

/**
 * 日々のタスク Issue の参照を単位に分ける<br>
 * <p>
 * application 層が domain 層に求めるインタフェース。実装は domain 層に置く。参照と単位の件数は {@link #initialize(List, int)} で受け取る。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public interface CarryoverBatchLogic {

    /**
     * 参照と単位の件数を受け取り、フィールドに保持する<br>
     * <p>
     * {@link #split()} より前に呼ぶ。
     * </p>
     *
     * @since 0.1.0
     *
     * @param refs
     *                  本文を持たない参照
     * @param batchSize
     *                  単位（Issue の件数）。1 未満のときは全件を 1 単位にする
     */
    void initialize(List<DailyTaskIssueRefModel> refs, int batchSize);

    /**
     * 保持した参照を単位に分ける<br>
     * <p>
     * 日付がある参照は日付順に batchSize 件ずつ。日付がない参照は最後の 1 単位。batchSize が 1 未満のときは全件を 1 単位にする。参照がないときは空の 1 単位にする。
     * </p>
     *
     * @since 0.1.0
     *
     * @return 単位の一覧
     */
    List<List<DailyTaskIssueRefModel>> split();

}
