package kmg.gr.gr002.dailytasks.carryover.domain.logic.impl;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueModel;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubSettingsDto;
import kmg.gr.gr002.dailytasks.carryover.domain.repository.api.GitHubIssueApi;
import kmg.gr.gr002.dailytasks.testutil.ReflectionTestUtil;

/**
 * {@link DailyTaskIssueLogicImpl} のテスト<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings({
    "nls", "static-method"
})
public class DailyTaskIssueLogicImplTest {

    /**
     * テスト用の GitHub の Issue の取得<br>
     *
     * @author KenichiroArai
     *
     * @since 0.1.0
     *
     * @version 0.1.0
     */
    private static final class StubGitHubIssueApi implements GitHubIssueApi {

        /**
         * 返す Issue
         *
         * @since 0.1.0
         */
        private final List<GitHubIssueDto> issues;

        /**
         * 受け取った接続設定
         *
         * @since 0.1.0
         */
        private GitHubSettingsDto settings;

        /**
         * コンストラクタ<br>
         *
         * @since 0.1.0
         *
         * @param issues
         *               返す Issue
         */
        private StubGitHubIssueApi(final List<GitHubIssueDto> issues) {

            this.issues = issues;

        }

        /**
         * 設定した Issue を返す<br>
         *
         * @since 0.1.0
         *
         * @return 設定した Issue
         */
        @Override
        public List<GitHubIssueDto> fetchAllIssues() {

            final List<GitHubIssueDto> result = this.issues;
            return result;

        }

        /**
         * 受け取った接続設定を返す<br>
         *
         * @since 0.1.0
         *
         * @return 受け取った接続設定
         */
        private GitHubSettingsDto getSettings() {

            final GitHubSettingsDto result = this.settings;
            return result;

        }

        /**
         * 接続設定を記録する<br>
         *
         * @since 0.1.0
         *
         * @param gitHubSettings
         *                       接続設定
         */
        @Override
        public void initialize(final GitHubSettingsDto gitHubSettings) {

            this.settings = gitHubSettings;

        }

    }

    /**
     * テスト対象を作成する<br>
     *
     * @since 0.1.0
     *
     * @param gitHubIssueApi
     *                       GitHub の Issue の取得
     *
     * @return テスト対象
     */
    private static DailyTaskIssueLogicImpl createTarget(final GitHubIssueApi gitHubIssueApi) {

        final DailyTaskIssueLogicImpl result = new DailyTaskIssueLogicImpl();
        ReflectionTestUtil.setField(result, "gitHubIssueApi", gitHubIssueApi);
        return result;

    }

    /**
     * fetchAllIssues メソッドのテスト - 正常系:取得した Issue をモデルに変換する場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   取得に失敗した場合
     */
    @Test
    public void testFetchAllIssues_normalConvert() throws Exception {

        /* 期待値の定義 */
        final String expectedTitle = "2026年10月06日のタスク";

        /* 準備 */
        final StubGitHubIssueApi     testGitHub = new StubGitHubIssueApi(
            List.of(new GitHubIssueDto(371, "2026年10月06日のタスク", "open", "2026-10-06T14:23:08Z", "本文")));
        final DailyTaskIssueLogicImpl testTarget = DailyTaskIssueLogicImplTest.createTarget(testGitHub);

        /* テスト対象の実行 */
        testTarget.initialize("owner/repo", "test-token");
        final List<DailyTaskIssueModel> testResult = testTarget.fetchAllIssues();

        /* 検証の準備 */
        final String actualTitle = testResult.get(0).getTitle();

        /* 検証の実施 */
        Assertions.assertEquals(expectedTitle, actualTitle, "タイトルが一致しません");

    }

    /**
     * fetchAllIssues メソッドのテスト - 正常系:リポジトリとトークンを接続設定に変換して渡す場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   取得に失敗した場合
     */
    @Test
    public void testFetchAllIssues_normalSettings() throws Exception {

        /* 期待値の定義 */
        final String expectedRepository = "owner/repo";
        final String expectedToken      = "test-token";

        /* 準備 */
        final StubGitHubIssueApi     testGitHub = new StubGitHubIssueApi(List.of());
        final DailyTaskIssueLogicImpl testTarget = DailyTaskIssueLogicImplTest.createTarget(testGitHub);

        /* テスト対象の実行 */
        testTarget.initialize("owner/repo", "test-token");
        testTarget.fetchAllIssues();

        /* 検証の準備 */
        final String actualRepository = testGitHub.getSettings().getRepository();
        final String actualToken      = testGitHub.getSettings().getToken();

        /* 検証の実施 */
        Assertions.assertEquals(expectedRepository, actualRepository, "対象リポジトリが一致しません");
        Assertions.assertEquals(expectedToken, actualToken, "トークンが一致しません");

    }

}
