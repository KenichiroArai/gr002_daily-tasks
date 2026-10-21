package kmg.gr.gr002.dailytasks.carryover.application.logic;

import java.io.IOException;
import java.util.List;

import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueModel;

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
