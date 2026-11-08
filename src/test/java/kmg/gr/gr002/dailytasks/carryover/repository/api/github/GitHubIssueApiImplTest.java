package kmg.gr.gr002.dailytasks.carryover.repository.api.github;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Instant;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.core.infrastructure.type.KmgString;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueRefDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubSettingsDto;
import kmg.gr.gr002.dailytasks.testutil.DailyTasksMessageSourceTestUtil;
import kmg.gr.gr002.dailytasks.testutil.LogAssertions;
import kmg.gr.gr002.dailytasks.testutil.LogCapture;
import kmg.gr.gr002.dailytasks.testutil.ReflectionTestUtil;
import tools.jackson.databind.json.JsonMapper;

/**
 * {@link GitHubIssueApiImpl} のテスト<br>
 * <p>
 * GitHub API への実通信は行わず、{@link StubHttpClient} の応答を使う。持ち越し固有のページングとプルリクエスト除外を検証する。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings({
    "nls", "static-method", "resource",
})
public class GitHubIssueApiImplTest {

    /**
     * テスト用の API のベース URL
     *
     * @since 0.1.0
     */
    private static final String API_BASE_URL = "https://api.example.com";

    /**
     * 1 ページ目の URL（作成降順）
     *
     * @since 0.1.0
     */
    private static final String PAGE1_DESC_URI
        = "https://api.example.com/repos/owner/repo/issues?state=all&sort=created&direction=desc&per_page=100&page=1";

    /**
     * テスト用のリポジトリ
     *
     * @since 0.1.0
     */
    private static final String REPOSITORY = "owner/repo";

    /**
     * 接続設定を作成する<br>
     *
     * @since 0.1.0
     *
     * @param token
     *              トークン
     *
     * @return 接続設定
     */
    private static GitHubSettingsDto createSettings(final String token) {

        final GitHubSettingsDto result = new GitHubSettingsDto(GitHubIssueApiImplTest.REPOSITORY, token);
        return result;

    }

    /**
     * スタブの HTTP クライアントを使うテスト対象を作成する<br>
     *
     * @since 0.1.0
     *
     * @param httpClient
     *                   HTTP クライアント
     *
     * @return テスト対象
     */
    private static GitHubIssueApiImpl createTarget(final HttpClient httpClient) {

        final Supplier<HttpClient> httpClientSupplier = () -> httpClient;
        final GitHubIssueApiImpl   result             = new GitHubIssueApiImpl(new JsonMapper(),
            DailyTasksMessageSourceTestUtil.create());
        ReflectionTestUtil.setField(result, "httpClientSupplier", httpClientSupplier);
        ReflectionTestUtil.setField(result, "apiBaseUrl", GitHubIssueApiImplTest.API_BASE_URL);
        return result;

    }

    /**
     * Issue の JSON を作成する<br>
     *
     * @since 0.1.0
     *
     * @param number
     *               Issue 番号
     *
     * @return Issue の JSON
     */
    private static String issueJson(final int number) {

        final String result = String.format(
            "{\"number\":%d,\"title\":\"2026年10月06日のタスク\",\"state\":\"open\",\"updated_at\":\"2026-10-06T14:23:08Z\",\"body\":\"本文\"}",
            Integer.valueOf(number));
        return result;

    }

    /**
     * fetchAllIssues メソッドのテスト - 正常系:複数ページを取得する場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testFetchAllIssues_normalMultiplePages() throws IOException {

        /* 期待値の定義 */
        final int expectedSize         = 100;
        final int expectedRequestCount = 2;

        /* 準備 */
        final String testPage1 = IntStream.rangeClosed(1, 100).mapToObj(GitHubIssueApiImplTest::issueJson)
            .collect(Collectors.joining(",", "[", "]"));

        try (StubHttpClient testHttpClient = new StubHttpClient()) {

            testHttpClient.addResponse(200, testPage1);
            testHttpClient.addResponse(200, "[]");
            final GitHubIssueApiImpl testTarget = GitHubIssueApiImplTest.createTarget(testHttpClient);
            testTarget.initialize(GitHubIssueApiImplTest.createSettings(null));

            /* テスト対象の実行 */
            final List<GitHubIssueDto> testResult = testTarget.fetchAllIssues();

            /* 検証の準備 */
            final int               actualSize         = testResult.size();
            final List<HttpRequest> requests           = testHttpClient.getRequests();
            final int               actualRequestCount = requests.size();

            /* 検証の実施 */
            Assertions.assertEquals(expectedSize, actualSize, "Issue の件数が一致しません");
            Assertions.assertEquals(expectedRequestCount, actualRequestCount, "リクエスト数が一致しません");

        }

    }

    /**
     * fetchAllIssues メソッドのテスト - 正常系:1 ページでプルリクエストを除外する場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testFetchAllIssues_normalSinglePageWithoutPullRequest() throws IOException {

        /* 期待値の定義 */
        final List<Integer> expectedNumbers = List.of(1);
        final String[]      expectedMsgs    = {
            "[Gr002Log51000] Issue を 1 件取得しました（1 ページ）",
        };

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient();
            LogCapture testLog = new LogCapture(GitHubIssueApiImpl.class)) {

            testHttpClient.addResponse(200,
                KmgString.concat("[", GitHubIssueApiImplTest.issueJson(1), ",{\"number\":2,\"pull_request\":{}}]"));
            final GitHubIssueApiImpl testTarget = GitHubIssueApiImplTest.createTarget(testHttpClient);
            testTarget.initialize(GitHubIssueApiImplTest.createSettings(null));

            /* テスト対象の実行 */
            final List<GitHubIssueDto> testResult = testTarget.fetchAllIssues();

            /* 検証の準備 */
            final String[]      actualMsgs    = testLog.getMessages();
            @SuppressWarnings("null")
            final List<Integer> actualNumbers = testResult.stream().map(GitHubIssueDto::getNumber).toList();

            /* 検証の実施 */
            Assertions.assertEquals(expectedNumbers, actualNumbers, "Issue 番号が一致しません");
            LogAssertions.assertMessages(expectedMsgs, actualMsgs);

        }

    }

    /**
     * fetchIssueRefs メソッドのテスト - 正常系:本文を持たない参照を返す場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testFetchIssueRefs_normalWithoutBody() throws IOException {

        /* 期待値の定義 */
        final int     expectedNumber    = 1;
        final String  expectedTitle     = "2026年10月06日のタスク";
        final String  expectedState     = "open";
        final Instant expectedUpdatedAt = Instant.parse("2026-10-06T14:23:08Z");

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient()) {

            testHttpClient.addResponse(200, KmgString.concat("[", GitHubIssueApiImplTest.issueJson(1), "]"));
            final GitHubIssueApiImpl testTarget = GitHubIssueApiImplTest.createTarget(testHttpClient);
            testTarget.initialize(GitHubIssueApiImplTest.createSettings(null));

            /* テスト対象の実行 */
            final List<GitHubIssueRefDto> testResult = testTarget.fetchIssueRefs();

            /* 検証の準備 */
            final GitHubIssueRefDto actualRef       = testResult.get(0);
            final int               actualNumber    = actualRef.getNumber();
            final String            actualTitle     = actualRef.getTitle();
            final String            actualState     = actualRef.getState();
            final Instant           actualUpdatedAt = actualRef.getUpdatedAt();

            /* 検証の実施 */
            Assertions.assertEquals(expectedNumber, actualNumber, "Issue 番号が一致しません");
            Assertions.assertEquals(expectedTitle, actualTitle, "タイトルが一致しません");
            Assertions.assertEquals(expectedState, actualState, "状態が一致しません");
            Assertions.assertEquals(expectedUpdatedAt, actualUpdatedAt, "更新日時が一致しません");

        }

    }

    /**
     * fetchRecentIssueRefs メソッドのテスト - 正常系:上限件数で打ち切り作成昇順で返す場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testFetchRecentIssueRefs_normalLimitAsc() throws IOException {

        /* 期待値の定義 */
        final List<Integer> expectedNumbers = List.of(2, 3);
        final String        expectedUri     = GitHubIssueApiImplTest.PAGE1_DESC_URI;

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient()) {

            testHttpClient.addResponse(200, KmgString.concat("[", GitHubIssueApiImplTest.issueJson(3), ",",
                GitHubIssueApiImplTest.issueJson(2), ",", GitHubIssueApiImplTest.issueJson(1), "]"));
            final GitHubIssueApiImpl testTarget = GitHubIssueApiImplTest.createTarget(testHttpClient);
            testTarget.initialize(GitHubIssueApiImplTest.createSettings(null));

            /* テスト対象の実行 */
            final List<GitHubIssueRefDto> testResult = testTarget.fetchRecentIssueRefs(2);

            /* 検証の準備 */
            @SuppressWarnings("null")
            final List<Integer> actualNumbers = testResult.stream().map(GitHubIssueRefDto::getNumber).toList();
            final URI           actualUri     = testHttpClient.getRequests().get(0).uri();

            /* 検証の実施 */
            Assertions.assertEquals(expectedNumbers, actualNumbers, "Issue 番号が一致しません");
            Assertions.assertEquals(expectedUri, actualUri.toString(), "作成降順の URL である必要があります");

        }

    }

    /**
     * fetchRecentIssueRefs メソッドのテスト - 正常系:上限に達する前に最終ページで打ち切る場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testFetchRecentIssueRefs_normalShortPage() throws IOException {

        /* 期待値の定義 */
        final List<Integer> expectedNumbers = List.of(1, 2);
        final int           expectedPages   = 1;

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient()) {

            testHttpClient.addResponse(200, KmgString.concat("[", GitHubIssueApiImplTest.issueJson(2), ",",
                GitHubIssueApiImplTest.issueJson(1), "]"));
            final GitHubIssueApiImpl testTarget = GitHubIssueApiImplTest.createTarget(testHttpClient);
            testTarget.initialize(GitHubIssueApiImplTest.createSettings(null));

            /* テスト対象の実行 */
            final List<GitHubIssueRefDto> testResult = testTarget.fetchRecentIssueRefs(10);

            /* 検証の準備 */
            @SuppressWarnings("null")
            final List<Integer> actualNumbers = testResult.stream().map(GitHubIssueRefDto::getNumber).toList();
            final int           actualPages   = testHttpClient.getRequests().size();

            /* 検証の実施 */
            Assertions.assertEquals(expectedNumbers, actualNumbers, "Issue 番号が一致しません");
            Assertions.assertEquals(expectedPages, actualPages, "ページ数が一致しません");

        }

    }

    /**
     * fetchRecentIssueRefs メソッドのテスト - 正常系:プルリクエストを除いて次ページから足りる件数を取る場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testFetchRecentIssueRefs_normalSkipPullRequest() throws IOException {

        /* 期待値の定義 */
        final List<Integer> expectedNumbers = List.of(1);
        final int           expectedPages   = 2;

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient()) {

            final String page1
                = IntStream.rangeClosed(1, 100).mapToObj(number -> "{\"number\":" + number + ",\"pull_request\":{}}")
                    .collect(Collectors.joining(",", "[", "]"));
            testHttpClient.addResponse(200, page1);
            testHttpClient.addResponse(200, KmgString.concat("[", GitHubIssueApiImplTest.issueJson(1), "]"));
            final GitHubIssueApiImpl testTarget = GitHubIssueApiImplTest.createTarget(testHttpClient);
            testTarget.initialize(GitHubIssueApiImplTest.createSettings(null));

            /* テスト対象の実行 */
            final List<GitHubIssueRefDto> testResult = testTarget.fetchRecentIssueRefs(1);

            /* 検証の準備 */
            @SuppressWarnings("null")
            final List<Integer> actualNumbers = testResult.stream().map(GitHubIssueRefDto::getNumber).toList();
            final int           actualPages   = testHttpClient.getRequests().size();

            /* 検証の実施 */
            Assertions.assertEquals(expectedNumbers, actualNumbers, "Issue 番号が一致しません");
            Assertions.assertEquals(expectedPages, actualPages, "ページ数が一致しません");

        }

    }

    /**
     * fetchRecentIssueRefs メソッドのテスト - 準正常系:上限が 1 未満のときは空を返す場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testFetchRecentIssueRefs_semiLimitBelowOne() throws IOException {

        /* 期待値の定義 */

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient()) {

            final GitHubIssueApiImpl testTarget = GitHubIssueApiImplTest.createTarget(testHttpClient);
            testTarget.initialize(GitHubIssueApiImplTest.createSettings(null));

            /* テスト対象の実行 */
            final List<GitHubIssueRefDto> testResult = testTarget.fetchRecentIssueRefs(0);

            /* 検証の準備 */
            final boolean actualEmpty = testResult.isEmpty();
            final int     actualPages = testHttpClient.getRequests().size();

            /* 検証の実施 */
            Assertions.assertTrue(actualEmpty, "参照は空になる必要があります");
            Assertions.assertEquals(0, actualPages, "API を呼ばない必要があります");

        }

    }

}
