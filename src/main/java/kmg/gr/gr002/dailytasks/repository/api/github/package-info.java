/**
 * 持ち越し機能の GitHub API の実装<br>
 * <p>
 * {@code GitHubApiClient}（共通 HTTP）、{@code GitHubIssueClient}（Issue 取得・DTO 変換）、
 * {@code GitHubIssueApiImpl}（持ち越し固有のページングとプルリクエスト除外）に分けて、GitHub REST API から Issue を取得する。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.2.0
 *
 * @version 0.2.0
 */
package kmg.gr.gr002.dailytasks.repository.api.github;
