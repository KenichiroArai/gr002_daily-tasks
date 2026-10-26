package kmg.gr.gr002.dailytasks.carryover.domain.repository.api;

import java.io.IOException;
import java.util.List;

import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueRefDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubSettingsDto;

/**
 * GitHub の Issue を取得する外部 API<br>
 * <p>
 * domain 層が repository 層に求めるインタフェース。実装は repository 層に置く。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public interface GitHubIssueApi {

    /**
     * すべての Issue（オープン・クローズ済み）を取得する<br>
     * <p>
     * プルリクエストは除外する。{@link #initialize(GitHubSettingsDto)} のあとで呼ぶ。
     * </p>
     *
     * @since 0.1.0
     *
     * @return Issue（作成順）
     *
     * @throws IOException
     *                     通信に失敗した場合、または応答が不正な場合
     */
    List<GitHubIssueDto> fetchAllIssues() throws IOException;

    /**
     * Issue を 1 件取得する<br>
     * <p>
     * {@link #initialize(GitHubSettingsDto)} のあとで呼ぶ。
     * </p>
     *
     * @since 0.1.0
     *
     * @param number
     *               Issue 番号
     *
     * @return Issue
     *
     * @throws IOException
     *                     通信に失敗した場合、または応答が不正な場合
     */
    GitHubIssueDto fetchIssue(int number) throws IOException;

    /**
     * すべての Issue の参照（本文なし）を取得する<br>
     * <p>
     * プルリクエストは除外する。{@link #initialize(GitHubSettingsDto)} のあとで呼ぶ。
     * </p>
     *
     * @since 0.1.0
     *
     * @return Issue の参照（作成順）。本文は含まない
     *
     * @throws IOException
     *                     通信に失敗した場合、または応答が不正な場合
     */
    List<GitHubIssueRefDto> fetchIssueRefs() throws IOException;

    /**
     * GitHub API の接続設定を受け取り、フィールドに保持する<br>
     *
     * @since 0.1.0
     *
     * @param settings
     *                 GitHub API の接続設定
     */
    void initialize(GitHubSettingsDto settings);

}
