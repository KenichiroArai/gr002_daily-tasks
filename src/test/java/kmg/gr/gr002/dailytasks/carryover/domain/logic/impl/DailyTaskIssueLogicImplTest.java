package kmg.gr.gr002.dailytasks.carryover.domain.logic.impl;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueRefModel;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueRefDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubSettingsDto;
import kmg.gr.gr002.dailytasks.carryover.domain.repository.api.GitHubIssueApi;

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
         * 番号が一致する Issue を返す<br>
         *
         * @since 0.1.0
         *
         * @param number
         *               Issue 番号
         *
         * @return 番号が一致する Issue。ない場合は空の Issue
         */
        @Override
        public GitHubIssueDto fetchIssue(final int number) {

            GitHubIssueDto result = new GitHubIssueDto(number, "", "", "", "");

            for (final GitHubIssueDto issue : this.issues) {

                final int issueNumber = issue.getNumber();

                if (issueNumber == number) {

                    result = issue;

                }

            }

            return result;

        }

        /**
         * 設定した Issue の参照を返す<br>
         *
         * @since 0.1.0
         *
         * @return 設定した Issue の参照
         */
        @Override
        public List<GitHubIssueRefDto> fetchIssueRefs() {

            final List<GitHubIssueRefDto> result = new ArrayList<>();

            for (final GitHubIssueDto issue : this.issues) {

                result.add(new GitHubIssueRefDto(issue.getNumber(), issue.getTitle(), issue.getState(),
                    issue.getUpdatedAt()));

            }

            return result;

        }

        /**
         * 設定した Issue のうち最新の指定件数の参照を返す<br>
         *
         * @since 0.1.0
         *
         * @param limit
         *              取得する最大件数
         *
         * @return 設定した Issue の参照（作成順）
         */
        @Override
        public List<GitHubIssueRefDto> fetchRecentIssueRefs(final int limit) {

            final List<GitHubIssueRefDto> all    = this.fetchIssueRefs();
            final int                     size   = all.size();
            final int                     from   = Math.max(0, size - Math.max(limit, 0));
            final List<GitHubIssueRefDto> result = new ArrayList<>(all.subList(from, size));
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

        final DailyTaskIssueLogicImpl result = new DailyTaskIssueLogicImpl(gitHubIssueApi);
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
        final DailyTaskIssueModel firstIssue = testResult.get(0);
        final String actualTitle = firstIssue.getTitle();

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
        final GitHubSettingsDto settings = testGitHub.getSettings();
        final String actualRepository = settings.getRepository();
        final String actualToken = settings.getToken();

        /* 検証の実施 */
        Assertions.assertEquals(expectedRepository, actualRepository, "対象リポジトリが一致しません");
        Assertions.assertEquals(expectedToken, actualToken, "トークンが一致しません");

    }

    /**
     * fetchIssue メソッドのテスト - 正常系:取得した 1 件をモデルに変換する場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   取得に失敗した場合
     */
    @Test
    public void testFetchIssue_normalConvert() throws Exception {

        /* 期待値の定義 */
        final String expectedBody = "本文";

        /* 準備 */
        final StubGitHubIssueApi     testGitHub = new StubGitHubIssueApi(List.of(
            new GitHubIssueDto(1, "別のタスク", "open", "2026-10-01T00:00:00Z", "別"),
            new GitHubIssueDto(7, "2026年10月06日のタスク", "open", "2026-10-06T14:23:08Z", "本文")));
        final DailyTaskIssueLogicImpl testTarget = DailyTaskIssueLogicImplTest.createTarget(testGitHub);

        /* テスト対象の実行 */
        final DailyTaskIssueModel testResult = testTarget.fetchIssue(7);

        /* 検証の準備 */
        final String actualBody = testResult.getBody();

        /* 検証の実施 */
        Assertions.assertEquals(expectedBody, actualBody, "本文が一致しません");

    }

    /**
     * fetchIssueRefs メソッドのテスト - 正常系:日付を null にした参照を返す場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   取得に失敗した場合
     */
    @Test
    public void testFetchIssueRefs_normalDateNull() throws Exception {

        /* 期待値の定義 */
        final int expectedNumber = 7;

        /* 準備 */
        final StubGitHubIssueApi     testGitHub = new StubGitHubIssueApi(
            List.of(new GitHubIssueDto(7, "2026年10月06日のタスク", "open", "2026-10-06T14:23:08Z", "本文")));
        final DailyTaskIssueLogicImpl testTarget = DailyTaskIssueLogicImplTest.createTarget(testGitHub);

        /* テスト対象の実行 */
        final List<DailyTaskIssueRefModel> testResult = testTarget.fetchIssueRefs();

        /* 検証の準備 */
        final DailyTaskIssueRefModel firstRef    = testResult.get(0);
        final int                    actualNumber = firstRef.getNumber();
        final boolean                actualNoDate = firstRef.getDate() == null;

        /* 検証の実施 */
        Assertions.assertEquals(expectedNumber, actualNumber, "Issue 番号が一致しません");
        Assertions.assertTrue(actualNoDate, "日付は null になる必要があります");

    }

    /**
     * fetchIssueRefs メソッドのテスト - 準正常系:参照がない場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   取得に失敗した場合
     */
    @Test
    public void testFetchIssueRefs_semiEmpty() throws Exception {

        /* 期待値の定義 */

        /* 準備 */
        final StubGitHubIssueApi     testGitHub = new StubGitHubIssueApi(List.of());
        final DailyTaskIssueLogicImpl testTarget = DailyTaskIssueLogicImplTest.createTarget(testGitHub);

        /* テスト対象の実行 */
        final List<DailyTaskIssueRefModel> testResult = testTarget.fetchIssueRefs();

        /* 検証の準備 */
        final boolean actualEmpty = testResult.isEmpty();

        /* 検証の実施 */
        Assertions.assertTrue(actualEmpty, "参照は空になる必要があります");

    }

    /**
     * fetchRecentIssueRefs メソッドのテスト - 正常系:最新の指定件数をモデルに変換する場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   取得に失敗した場合
     */
    @Test
    public void testFetchRecentIssueRefs_normalLimit() throws Exception {

        /* 期待値の定義 */
        final List<Integer> expectedNumbers = List.of(2, 3);

        /* 準備 */
        final StubGitHubIssueApi testGitHub = new StubGitHubIssueApi(List.of(
            new GitHubIssueDto(1, "1日のタスク", "closed", "u1", "本文"),
            new GitHubIssueDto(2, "2日のタスク", "closed", "u2", "本文"),
            new GitHubIssueDto(3, "3日のタスク", "open", "u3", "本文")));
        final DailyTaskIssueLogicImpl testTarget = DailyTaskIssueLogicImplTest.createTarget(testGitHub);

        /* テスト対象の実行 */
        final List<DailyTaskIssueRefModel> testResult = testTarget.fetchRecentIssueRefs(2);

        /* 検証の準備 */
        final List<Integer> actualNumbers = testResult.stream().map(DailyTaskIssueRefModel::getNumber).toList();

        /* 検証の実施 */
        Assertions.assertEquals(expectedNumbers, actualNumbers, "Issue 番号が一致しません");

    }

}
