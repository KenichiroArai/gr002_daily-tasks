package kmg.gr.gr002.dailytasks.carryover.application.logic;

import java.io.IOException;
import java.util.List;

import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueRefModel;

/**
 * 日々のタスク Issue を取得するロジック<br>
 * <p>
 * application 層が domain 層に求めるインタフェース。実装は domain 層に置く。接続に必要な値は {@link #initialize(String, String)} で受け取る。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public interface DailyTaskIssueLogic {

    /**
     * すべての日々のタスク Issue を取得する<br>
     *
     * @since 0.1.0
     *
     * @return 日々のタスク Issue（作成順）
     *
     * @throws IOException
     *                     取得に失敗した場合
     */
    List<DailyTaskIssueModel> fetchAllIssues() throws IOException;

    /**
     * Issue を 1 件取得する<br>
     *
     * @since 0.1.0
     *
     * @param number
     *               Issue 番号
     *
     * @return 日々のタスク Issue
     *
     * @throws IOException
     *                     取得に失敗した場合
     */
    DailyTaskIssueModel fetchIssue(int number) throws IOException;

    /**
     * すべての日々のタスク Issue の参照（本文なし）を取得する<br>
     * <p>
     * 日付はまだ入れない。
     * </p>
     *
     * @since 0.1.0
     *
     * @return 日々のタスク Issue の参照（作成順）。日付は null
     *
     * @throws IOException
     *                     取得に失敗した場合
     */
    List<DailyTaskIssueRefModel> fetchIssueRefs() throws IOException;

    /**
     * 最新の指定件数までの日々のタスク Issue の参照（本文なし）を取得する<br>
     * <p>
     * 日付はまだ入れない。戻り値は作成昇順。
     * </p>
     *
     * @since 0.1.0
     *
     * @param limit
     *              取得する最大件数
     *
     * @return 日々のタスク Issue の参照（作成順）。日付は null
     *
     * @throws IOException
     *                     取得に失敗した場合
     */
    List<DailyTaskIssueRefModel> fetchRecentIssueRefs(int limit) throws IOException;

    /**
     * 対象リポジトリとトークンを受け取り、Issue の取得先を初期化する<br>
     * <p>
     * 外部 API も同じ接続設定で初期化する。
     * </p>
     *
     * @since 0.1.0
     *
     * @param repository
     *                   Issue を取得するリポジトリ（owner/name）
     * @param token
     *                   GitHub API のトークン。未指定の場合は null
     */
    void initialize(String repository, String token);

}
