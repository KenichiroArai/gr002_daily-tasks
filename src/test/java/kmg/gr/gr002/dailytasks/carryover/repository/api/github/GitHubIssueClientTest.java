package kmg.gr.gr002.dailytasks.carryover.repository.api.github;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubSettingsDto;
import kmg.gr.gr002.dailytasks.carryover.repository.types.GitHubApiUrlTypes;
import kmg.gr.gr002.dailytasks.testutil.DailyTasksMessageSourceTestUtil;
import kmg.gr.gr002.dailytasks.testutil.ReflectionTestUtil;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * {@link GitHubIssueClient} のテスト<br>
 * <p>
 * GitHub API への実通信は行わず、{@link StubHttpClient} の応答を使う。
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
public class GitHubIssueClientTest {

    /**
     * テスト用の API のベース URL
     *
     * @since 0.1.0
     */
    private static final String API_BASE_URL = "https://api.example.com";

    /**
     * 1 ページ目の URL
     *
     * @since 0.1.0
     */
    private static final String PAGE1_URI
        = "https://api.example.com/repos/owner/repo/issues?state=all&sort=created&direction=asc&per_page=100&page=1";

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

        final GitHubSettingsDto result = new GitHubSettingsDto(GitHubIssueClientTest.REPOSITORY, token);
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
    private static GitHubIssueClient createTarget(final HttpClient httpClient) {

        final Supplier<HttpClient> httpClientSupplier = () -> httpClient;
        final GitHubIssueClient    result             = new GitHubIssueClient(new JsonMapper(),
            DailyTasksMessageSourceTestUtil.create());
        ReflectionTestUtil.setField(result, "httpClientSupplier", httpClientSupplier);
        ReflectionTestUtil.setField(result, "apiBaseUrl", GitHubIssueClientTest.API_BASE_URL);
        return result;

    }

    /**
     * protected の fetchPage メソッドを呼び出す<br>
     *
     * @since 0.1.0
     *
     * @param target
     *                   テスト対象
     * @param httpClient
     *                   HTTP クライアント
     * @param settings
     *                   接続設定
     * @param page
     *                   ページ番号
     *
     * @return API の応答
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    private static JsonNode fetchPage(final GitHubIssueClient target, final HttpClient httpClient,
        final GitHubSettingsDto settings, final int page) throws Exception {

        target.initialize(settings);
        final JsonNode result = ReflectionTestUtil.invoke(target, "fetchPage", new Class<?>[] {
            HttpClient.class, int.class, GitHubApiUrlTypes.class
        }, httpClient, Integer.valueOf(page), GitHubApiUrlTypes.ISSUES_QUERY);
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
     * protected の textOrEmpty メソッドを呼び出す<br>
     *
     * @since 0.1.0
     *
     * @param node
     *                  API の応答
     * @param fieldName
     *                  項目名
     *
     * @return 項目の値
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    private static String textOrEmpty(final JsonNode node, final String fieldName) throws Exception {

        final String result = ReflectionTestUtil.invokeStatic(GitHubIssueClient.class, "textOrEmpty", new Class<?>[] {
            JsonNode.class, String.class
        }, node, fieldName);
        return result;

    }

    /**
     * protected の toIssue メソッドを呼び出す<br>
     *
     * @since 0.1.0
     *
     * @param node
     *             API の応答の Issue
     *
     * @return Issue
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    private static GitHubIssueDto toIssue(final JsonNode node) throws Exception {

        final GitHubIssueDto result
            = ReflectionTestUtil.invokeStatic(GitHubIssueClient.class, "toIssue", new Class<?>[] {
                JsonNode.class
            }, node);
        return result;

    }

    /**
     * fetchIssue メソッドのテスト - 異常系:応答がオブジェクトではない場合
     *
     * @since 0.1.0
     */
    @Test
    public void testFetchIssue_errorNotObject() {

        /* 期待値の定義 */
        final String expectedMessage = "[Gr002Gen51003] GitHub API の応答がオブジェクトではありません: uri="
            + "https://api.example.com/repos/owner/repo/issues/7";

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient()) {

            testHttpClient.addResponse(200, "[]");
            final GitHubIssueClient testTarget = GitHubIssueClientTest.createTarget(testHttpClient);
            testTarget.initialize(GitHubIssueClientTest.createSettings("token"));

            /* テスト対象の実行 */
            final IOException testException
                = Assertions.assertThrows(IOException.class, () -> testTarget.fetchIssue(7));

            /* 検証の準備 */
            final String actualMessage = testException.getMessage();

            /* 検証の実施 */
            Assertions.assertEquals(expectedMessage, actualMessage, "例外のメッセージが一致しません");

        }

    }

    /**
     * fetchIssue メソッドのテスト - 正常系:Issue を 1 件取得する場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testFetchIssue_normalValue() throws IOException {

        /* 期待値の定義 */
        final String expectedBody = "本文";

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient()) {

            testHttpClient.addResponse(200, GitHubIssueClientTest.issueJson(7));
            final GitHubIssueClient testTarget = GitHubIssueClientTest.createTarget(testHttpClient);
            testTarget.initialize(GitHubIssueClientTest.createSettings("token"));

            /* テスト対象の実行 */
            final GitHubIssueDto testResult = testTarget.fetchIssue(7);

            /* 検証の準備 */
            final String actualBody = testResult.getBody();

            /* 検証の実施 */
            Assertions.assertEquals(expectedBody, actualBody, "本文が一致しません");

        }

    }

    /**
     * fetchPage メソッドのテスト - 異常系:応答が配列ではない場合
     *
     * @since 0.1.0
     */
    @Test
    public void testFetchPage_errorNotArray() {

        /* 期待値の定義 */
        final String expectedMessage
            = "[Gr002Gen51001] GitHub API の応答が配列ではありません: uri=" + GitHubIssueClientTest.PAGE1_URI;

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient()) {

            testHttpClient.addResponse(200, "{\"message\":\"x\"}");
            final GitHubIssueClient testTarget   = GitHubIssueClientTest.createTarget(testHttpClient);
            final GitHubSettingsDto testSettings = GitHubIssueClientTest.createSettings(null);

            /* テスト対象の実行 */
            final IOException testException = Assertions.assertThrows(IOException.class,
                () -> GitHubIssueClientTest.fetchPage(testTarget, testHttpClient, testSettings, 1));

            /* 検証の準備 */
            final String actualMessage = testException.getMessage();

            /* 検証の実施 */
            Assertions.assertEquals(expectedMessage, actualMessage, "例外のメッセージが一致しません");

        }

    }

    /**
     * fetchPage メソッドのテスト - 異常系:ステータスコードが 200 以外の場合
     *
     * @since 0.1.0
     */
    @Test
    public void testFetchPage_errorStatus() {

        /* 期待値の定義 */
        final String expectedMessage
            = "[Gr002Gen51000] GitHub API の呼び出しに失敗しました: status=403, uri=" + GitHubIssueClientTest.PAGE1_URI;

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient()) {

            testHttpClient.addResponse(403, "{}");
            final GitHubIssueClient testTarget   = GitHubIssueClientTest.createTarget(testHttpClient);
            final GitHubSettingsDto testSettings = GitHubIssueClientTest.createSettings(null);

            /* テスト対象の実行 */
            final IOException testException = Assertions.assertThrows(IOException.class,
                () -> GitHubIssueClientTest.fetchPage(testTarget, testHttpClient, testSettings, 1));

            /* 検証の準備 */
            final String actualMessage = testException.getMessage();

            /* 検証の実施 */
            Assertions.assertEquals(expectedMessage, actualMessage, "例外のメッセージが一致しません");

        }

    }

    /**
     * fetchPage メソッドのテスト - 正常系:トークンを指定しない場合は認証ヘッダを付けない
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testFetchPage_normalWithoutToken() throws Exception {

        /* 期待値の定義 */
        final Optional<String> expectedAuthorization = Optional.empty();

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient()) {

            testHttpClient.addResponse(200, "[]");
            final GitHubIssueClient testTarget = GitHubIssueClientTest.createTarget(testHttpClient);

            /* テスト対象の実行 */
            GitHubIssueClientTest.fetchPage(testTarget, testHttpClient, GitHubIssueClientTest.createSettings(null), 1);

            /* 検証の準備 */
            final List<HttpRequest> requests            = testHttpClient.getRequests();
            final HttpRequest       request             = requests.get(0);
            final HttpHeaders       headers             = request.headers();
            final Optional<String>  actualAuthorization = headers.firstValue("Authorization");

            /* 検証の実施 */
            Assertions.assertEquals(expectedAuthorization, actualAuthorization, "認証ヘッダが一致しません");

        }

    }

    /**
     * fetchPage メソッドのテスト - 正常系:トークンを指定した場合は認証ヘッダを付ける
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testFetchPage_normalWithToken() throws Exception {

        /* 期待値の定義 */
        final Optional<String> expectedAuthorization = Optional.of("Bearer test-token");
        final String           expectedUri           = GitHubIssueClientTest.PAGE1_URI;

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient()) {

            testHttpClient.addResponse(200, "[]");
            final GitHubIssueClient testTarget = GitHubIssueClientTest.createTarget(testHttpClient);

            /* テスト対象の実行 */
            GitHubIssueClientTest.fetchPage(testTarget, testHttpClient,
                GitHubIssueClientTest.createSettings("test-token"), 1);

            /* 検証の準備 */
            final List<HttpRequest> requests            = testHttpClient.getRequests();
            final HttpRequest       request             = requests.get(0);
            final HttpHeaders       headers             = request.headers();
            final URI               uri                 = request.uri();
            final Optional<String>  actualAuthorization = headers.firstValue("Authorization");
            final String            actualUri           = uri.toString();

            /* 検証の実施 */
            Assertions.assertEquals(expectedAuthorization, actualAuthorization, "認証ヘッダが一致しません");
            Assertions.assertEquals(expectedUri, actualUri, "URL が一致しません");

        }

    }

    /**
     * parseUpdatedAt メソッドのテスト - 更新日時の文字列が空の場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外
     */
    @Test
    public void testParseUpdatedAt_semiBlank() throws Exception {

        /* 期待値の定義 */
        final Instant expected = null;

        /* 準備 */

        /* テスト対象の実行 */
        final Instant testResult = ReflectionTestUtil.invokeStatic(GitHubIssueClient.class, "parseUpdatedAt",
            new Class<?>[] {
                String.class
            }, " ");

        /* 検証の準備 */
        final Instant actual = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expected, actual, "変換結果が一致しません");

    }

    /**
     * parseUpdatedAt メソッドのテスト - 更新日時の文字列が null の場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外
     */
    @Test
    public void testParseUpdatedAt_semiNull() throws Exception {

        /* 期待値の定義 */
        final Instant expected = null;

        /* 準備 */

        /* テスト対象の実行 */
        final Instant testResult = ReflectionTestUtil.invokeStatic(GitHubIssueClient.class, "parseUpdatedAt",
            new Class<?>[] {
                String.class
            }, (String) null);

        /* 検証の準備 */
        final Instant actual = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expected, actual, "変換結果が一致しません");

    }

    /**
     * textOrEmpty メソッドのテスト - 正常系:値がある場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testTextOrEmpty_normalValue() throws Exception {

        /* 期待値の定義 */
        final String expectedValue = "本文";

        /* 準備 */
        final JsonMapper mapper   = new JsonMapper();
        final JsonNode   testNode = mapper.readTree("{\"body\":\"本文\"}");

        /* テスト対象の実行 */
        final String testResult = GitHubIssueClientTest.textOrEmpty(testNode, "body");

        /* 検証の準備 */
        final String actualValue = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedValue, actualValue, "値が一致しません");

    }

    /**
     * textOrEmpty メソッドのテスト - 準正常系:項目がない場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testTextOrEmpty_semiMissing() throws Exception {

        /* 期待値の定義 */
        final String expectedValue = "";

        /* 準備 */
        final JsonMapper mapper   = new JsonMapper();
        final JsonNode   testNode = mapper.readTree("{}");

        /* テスト対象の実行 */
        final String testResult = GitHubIssueClientTest.textOrEmpty(testNode, "body");

        /* 検証の準備 */
        final String actualValue = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedValue, actualValue, "値が一致しません");

    }

    /**
     * textOrEmpty メソッドのテスト - 準正常系:値が null の場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testTextOrEmpty_semiNull() throws Exception {

        /* 期待値の定義 */
        final String expectedValue = "";

        /* 準備 */
        final JsonMapper mapper   = new JsonMapper();
        final JsonNode   testNode = mapper.readTree("{\"body\":null}");

        /* テスト対象の実行 */
        final String testResult = GitHubIssueClientTest.textOrEmpty(testNode, "body");

        /* 検証の準備 */
        final String actualValue = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedValue, actualValue, "値が一致しません");

    }

    /**
     * toIssue メソッドのテスト - 正常系:API の応答を Issue に変換する場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testToIssue_normalConvert() throws Exception {

        /* 期待値の定義 */
        final int     expectedNumber    = 371;
        final String  expectedTitle     = "2026年10月06日のタスク";
        final String  expectedState     = "open";
        final Instant expectedUpdatedAt = Instant.parse("2026-10-06T14:23:08Z");
        final String  expectedBody      = "本文";

        /* 準備 */
        final JsonMapper mapper   = new JsonMapper();
        final JsonNode   testNode = mapper.readTree(GitHubIssueClientTest.issueJson(371));

        /* テスト対象の実行 */
        final GitHubIssueDto testResult = GitHubIssueClientTest.toIssue(testNode);

        /* 検証の準備 */
        final int     actualNumber    = testResult.getNumber();
        final String  actualTitle     = testResult.getTitle();
        final String  actualState     = testResult.getState();
        final Instant actualUpdatedAt = testResult.getUpdatedAt();
        final String  actualBody      = testResult.getBody();

        /* 検証の実施 */
        Assertions.assertEquals(expectedNumber, actualNumber, "Issue 番号が一致しません");
        Assertions.assertEquals(expectedTitle, actualTitle, "タイトルが一致しません");
        Assertions.assertEquals(expectedState, actualState, "状態が一致しません");
        Assertions.assertEquals(expectedUpdatedAt, actualUpdatedAt, "更新日時が一致しません");
        Assertions.assertEquals(expectedBody, actualBody, "本文が一致しません");

    }

}
