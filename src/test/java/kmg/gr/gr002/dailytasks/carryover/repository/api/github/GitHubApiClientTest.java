package kmg.gr.gr002.dailytasks.carryover.repository.api.github;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import ch.qos.logback.classic.Level;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubSettingsDto;
import kmg.gr.gr002.dailytasks.testutil.DailyTasksMessageSourceTestUtil;
import kmg.gr.gr002.dailytasks.testutil.LogAssertions;
import kmg.gr.gr002.dailytasks.testutil.LogCapture;
import kmg.gr.gr002.dailytasks.testutil.ReflectionTestUtil;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * {@link GitHubApiClient} のテスト<br>
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
public class GitHubApiClientTest {

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

        final GitHubSettingsDto result = new GitHubSettingsDto(GitHubApiClientTest.REPOSITORY, token);
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
    private static GitHubApiClient createTarget(final HttpClient httpClient) {

        final Supplier<HttpClient> httpClientSupplier = () -> httpClient;
        final GitHubApiClient      result             = new GitHubApiClient(new JsonMapper(),
            DailyTasksMessageSourceTestUtil.create());
        ReflectionTestUtil.setField(result, "httpClientSupplier", httpClientSupplier);
        ReflectionTestUtil.setField(result, "apiBaseUrl", GitHubApiClientTest.API_BASE_URL);
        return result;

    }

    /**
     * protected の fetchResponse メソッドを呼び出す<br>
     *
     * @since 0.1.0
     *
     * @param target
     *                      テスト対象
     * @param httpClient
     *                      HTTP クライアント
     * @param uri
     *                      リクエストの URI
     * @param arrayExpected
     *                      true：配列を期待する
     *
     * @return 応答の JSON
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    private static JsonNode fetchResponse(final GitHubApiClient target, final HttpClient httpClient, final URI uri,
        final boolean arrayExpected) throws Exception {

        final JsonNode result = ReflectionTestUtil.invoke(target, "fetchResponse", new Class<?>[] {
            HttpClient.class, URI.class, boolean.class
        }, httpClient, uri, Boolean.valueOf(arrayExpected));
        return result;

    }

    /**
     * protected の hasToken メソッドを呼び出す<br>
     *
     * @since 0.1.0
     *
     * @param settings
     *                 接続設定
     *
     * @return トークンがある場合は true
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    private static boolean hasToken(final GitHubSettingsDto settings) throws Exception {

        final GitHubApiClient target
            = new GitHubApiClient(new JsonMapper(), DailyTasksMessageSourceTestUtil.create());
        target.initialize(settings);
        final boolean result = ReflectionTestUtil.<Boolean>invoke(target, "hasToken", new Class<?>[] {});
        return result;

    }

    /**
     * protected の readTree メソッドを呼び出す<br>
     *
     * @since 0.1.0
     *
     * @param target
     *               テスト対象
     * @param body
     *               応答の本文
     *
     * @return JSON
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    private static JsonNode readTree(final GitHubApiClient target, final String body) throws Exception {

        final JsonNode result = ReflectionTestUtil.invoke(target, "readTree", new Class<?>[] {
            String.class
        }, body);
        return result;

    }

    /**
     * protected の send メソッドを呼び出す<br>
     *
     * @since 0.1.0
     *
     * @param target
     *                   テスト対象
     * @param httpClient
     *                   HTTP クライアント
     * @param request
     *                   リクエスト
     *
     * @return 応答
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    private static HttpResponse<String> send(final GitHubApiClient target, final HttpClient httpClient,
        final HttpRequest request) throws Exception {

        final HttpResponse<String> result = ReflectionTestUtil.invoke(target, "send", new Class<?>[] {
            HttpClient.class, HttpRequest.class
        }, httpClient, request);
        return result;

    }

    /**
     * fetchResponse メソッドのテスト - 正常系:レート制限の残りが十分な場合は警告しない
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testFetchResponse_normalRateLimitOk() throws Exception {

        /* 期待値の定義 */
        final String[] expectedMsgs = {
            "[Gr002Log51002] レート制限: remaining=50、reset=999",
        };

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient();
            LogCapture testLog = new LogCapture(GitHubApiClient.class, Level.DEBUG)) {

            testHttpClient.addResponse(200, "[]",
                Map.of("X-RateLimit-Remaining", List.of("50"), "X-RateLimit-Reset", List.of("999")));
            final GitHubApiClient testTarget = GitHubApiClientTest.createTarget(testHttpClient);
            testTarget.initialize(GitHubApiClientTest.createSettings(null));

            /* テスト対象の実行 */
            GitHubApiClientTest.fetchResponse(testTarget, testHttpClient, URI.create(GitHubApiClientTest.PAGE1_URI),
                true);

            /* 検証の準備 */
            final String[] actualMsgs = testLog.getMessages();

            /* 検証の実施 */
            LogAssertions.assertMessages(expectedMsgs, actualMsgs);

        }

    }

    /**
     * fetchResponse メソッドのテスト - 正常系:レート制限の残りが少ない場合は警告する
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testFetchResponse_normalRateLimitWarn() throws Exception {

        /* 期待値の定義 */
        final String[] expectedMsgs = {
            "[Gr002Log51002] レート制限: remaining=5、reset=123", "[Gr002Log51003] レート制限の残りが少なくなっています: remaining=5、reset=123",
        };

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient();
            LogCapture testLog = new LogCapture(GitHubApiClient.class, Level.DEBUG)) {

            testHttpClient.addResponse(200, "[]",
                Map.of("X-RateLimit-Remaining", List.of("5"), "X-RateLimit-Reset", List.of("123")));
            final GitHubApiClient testTarget = GitHubApiClientTest.createTarget(testHttpClient);
            testTarget.initialize(GitHubApiClientTest.createSettings(null));

            /* テスト対象の実行 */
            GitHubApiClientTest.fetchResponse(testTarget, testHttpClient, URI.create(GitHubApiClientTest.PAGE1_URI),
                true);

            /* 検証の準備 */
            final String[] actualMsgs = testLog.getMessages();

            /* 検証の実施 */
            LogAssertions.assertMessages(expectedMsgs, actualMsgs);

        }

    }

    /**
     * fetchResponse メソッドのテスト - 準正常系:レート制限の残数が数ではない場合は警告しない
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testFetchResponse_semiRateLimitInvalid() throws Exception {

        /* 期待値の定義 */
        final String[] expectedMsgs = {
            "[Gr002Log51002] レート制限: remaining=abc、reset=",
        };

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient();
            LogCapture testLog = new LogCapture(GitHubApiClient.class, Level.DEBUG)) {

            testHttpClient.addResponse(200, "[]", Map.of("X-RateLimit-Remaining", List.of("abc")));
            final GitHubApiClient testTarget = GitHubApiClientTest.createTarget(testHttpClient);
            testTarget.initialize(GitHubApiClientTest.createSettings(null));

            /* テスト対象の実行 */
            GitHubApiClientTest.fetchResponse(testTarget, testHttpClient, URI.create(GitHubApiClientTest.PAGE1_URI),
                true);

            /* 検証の準備 */
            final String[] actualMsgs = testLog.getMessages();

            /* 検証の実施 */
            LogAssertions.assertMessages(expectedMsgs, actualMsgs);

        }

    }

    /**
     * GitHubApiClient のテスト - 正常系:API のベース URL を指定しない場合は GitHub API のベース URL を使う
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testGitHubApiClient_normalDefaultBaseUrl() throws Exception {

        /* 期待値の定義 */
        final String expectedApiBaseUrl = "https://api.github.com";

        /* 準備 */
        final JsonMapper testJsonMapper = new JsonMapper();

        /* テスト対象の実行 */
        final GitHubApiClient testTarget
            = new GitHubApiClient(testJsonMapper, DailyTasksMessageSourceTestUtil.create());

        /* 検証の準備 */
        final String     actualApiBaseUrl = ReflectionTestUtil.getField(testTarget, "apiBaseUrl");
        final JsonMapper actualJsonMapper = ReflectionTestUtil.getField(testTarget, "jsonMapper");

        /* 検証の実施 */
        Assertions.assertEquals(expectedApiBaseUrl, actualApiBaseUrl, "API のベース URL が一致しません");
        Assertions.assertEquals(testJsonMapper, actualJsonMapper, "JSON の変換が一致しません");

    }

    /**
     * hasToken メソッドのテスト - 正常系:トークンを指定した場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testHasToken_normalSpecified() throws Exception {

        /* 期待値の定義 */

        /* 準備 */
        final GitHubSettingsDto testSettings = GitHubApiClientTest.createSettings("test-token");

        /* テスト対象の実行 */
        final boolean testResult = GitHubApiClientTest.hasToken(testSettings);

        /* 検証の準備 */
        final boolean actualHasToken = testResult;

        /* 検証の実施 */
        Assertions.assertTrue(actualHasToken, "トークンありと判定される必要があります");

    }

    /**
     * hasToken メソッドのテスト - 準正常系:トークンが空白の場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testHasToken_semiBlank() throws Exception {

        /* 期待値の定義 */

        /* 準備 */
        final GitHubSettingsDto testSettings = GitHubApiClientTest.createSettings(" ");

        /* テスト対象の実行 */
        final boolean testResult = GitHubApiClientTest.hasToken(testSettings);

        /* 検証の準備 */
        final boolean actualHasToken = testResult;

        /* 検証の実施 */
        Assertions.assertFalse(actualHasToken, "トークンなしと判定される必要があります");

    }

    /**
     * hasToken メソッドのテスト - 準正常系:トークンが null の場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testHasToken_semiNull() throws Exception {

        /* 期待値の定義 */

        /* 準備 */
        final GitHubSettingsDto testSettings = GitHubApiClientTest.createSettings(null);

        /* テスト対象の実行 */
        final boolean testResult = GitHubApiClientTest.hasToken(testSettings);

        /* 検証の準備 */
        final boolean actualHasToken = testResult;

        /* 検証の実施 */
        Assertions.assertFalse(actualHasToken, "トークンなしと判定される必要があります");

    }

    /**
     * readTree メソッドのテスト - 異常系:JSON として読み込めない場合
     *
     * @since 0.1.0
     */
    @Test
    public void testReadTree_errorInvalidJson() {

        /* 期待値の定義 */

        /* 準備 */
        final GitHubApiClient testTarget = GitHubApiClientTest.createTarget(null);

        /* テスト対象の実行 */
        final IOException testException
            = Assertions.assertThrows(IOException.class, () -> GitHubApiClientTest.readTree(testTarget, "[不正"));

        /* 検証の準備 */
        final Throwable actualCause = testException.getCause();

        /* 検証の実施 */
        Assertions.assertInstanceOf(JacksonException.class, actualCause, "原因の例外の型が一致しません");

    }

    /**
     * readTree メソッドのテスト - 正常系:JSON を読み込む場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testReadTree_normalArray() throws Exception {

        /* 期待値の定義 */
        final int expectedSize = 2;

        /* 準備 */
        final GitHubApiClient testTarget = GitHubApiClientTest.createTarget(null);

        /* テスト対象の実行 */
        final JsonNode testResult = GitHubApiClientTest.readTree(testTarget, "[1,2]");

        /* 検証の準備 */
        final int actualSize = testResult.size();

        /* 検証の実施 */
        Assertions.assertEquals(expectedSize, actualSize, "要素数が一致しません");

    }

    /**
     * send メソッドのテスト - 異常系:通信が中断された場合
     *
     * @since 0.1.0
     */
    @Test
    public void testSend_errorInterrupted() {

        /* 期待値の定義 */
        final String expectedMessage = "[Gr002Gen51002] GitHub API の呼び出しが中断されました";

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient()) {

            testHttpClient.setInterruptedException(new InterruptedException("中断"));
            final GitHubApiClient testTarget  = GitHubApiClientTest.createTarget(testHttpClient);
            final HttpRequest     testRequest = HttpRequest.newBuilder(URI.create(GitHubApiClientTest.PAGE1_URI))
                .build();

            /* テスト対象の実行 */
            final IOException testException = Assertions.assertThrows(IOException.class,
                () -> GitHubApiClientTest.send(testTarget, testHttpClient, testRequest));

            /* 検証の準備 */
            final String    actualMessage     = testException.getMessage();
            final Throwable actualCause       = testException.getCause();
            final boolean   actualInterrupted = Thread.interrupted();

            /* 検証の実施 */
            Assertions.assertEquals(expectedMessage, actualMessage, "例外のメッセージが一致しません");
            Assertions.assertInstanceOf(InterruptedException.class, actualCause, "原因の例外の型が一致しません");
            Assertions.assertTrue(actualInterrupted, "割り込み状態が復元される必要があります");

        }

    }

    /**
     * send メソッドのテスト - 正常系:応答を返す場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testSend_normalResponse() throws Exception {

        /* 期待値の定義 */
        final String expectedBody = "[]";

        /* 準備 */
        try (StubHttpClient testHttpClient = new StubHttpClient()) {

            testHttpClient.addResponse(200, "[]");
            final GitHubApiClient testTarget  = GitHubApiClientTest.createTarget(testHttpClient);
            final HttpRequest     testRequest = HttpRequest.newBuilder(URI.create(GitHubApiClientTest.PAGE1_URI))
                .build();

            /* テスト対象の実行 */
            final HttpResponse<String> testResult
                = GitHubApiClientTest.send(testTarget, testHttpClient, testRequest);

            /* 検証の準備 */
            final String actualBody = testResult.body();

            /* 検証の実施 */
            Assertions.assertEquals(expectedBody, actualBody, "本文が一致しません");

        }

    }

}
