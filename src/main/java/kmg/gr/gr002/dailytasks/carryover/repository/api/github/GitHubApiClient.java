package kmg.gr.gr002.dailytasks.carryover.repository.api.github;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kmg.core.infrastructure.type.KmgString;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubSettingsDto;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.context.DailyTasksMessageSource;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg.Gr002GenMsgTypes;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg.Gr002LogMsgTypes;
import kmg.gr.gr002.dailytasks.carryover.repository.types.GitHubApiHeaderTypes;
import kmg.gr.gr002.dailytasks.carryover.repository.types.GitHubApiTextTypes;
import kmg.gr.gr002.dailytasks.carryover.repository.types.GitHubApiUrlTypes;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * GitHub REST API の共通 HTTP クライアント<br>
 * <p>
 * 認証ヘッダ、レート制限のログ、応答の検証など、GitHub API 全般で使う処理を持つ。HTTP クライアントは呼び出し側が生成して閉じる。トークンはログに出力しない。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class GitHubApiClient {

    /**
     * ロガー
     *
     * @since 0.1.0
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubApiClient.class);

    /**
     * レート制限の残りが少ないとみなす閾値
     *
     * @since 0.1.0
     */
    protected static final int RATE_LIMIT_WARN_THRESHOLD = 10;

    /**
     * 正常応答のステータスコード
     *
     * @since 0.1.0
     */
    protected static final int STATUS_OK = 200;

    /**
     * リクエストのタイムアウト
     *
     * @since 0.1.0
     */
    protected static final Duration TIMEOUT = Duration.ofSeconds(30);

    /**
     * API のベース URL（例: https://api.github.com）
     *
     * @since 0.1.0
     */
    protected final String apiBaseUrl = GitHubApiUrlTypes.BASE_URL.getUrl();

    /**
     * HTTP クライアントの生成
     *
     * @since 0.1.0
     */
    protected final Supplier<HttpClient> httpClientSupplier;

    /**
     * JSON の変換
     *
     * @since 0.1.0
     */
    protected final JsonMapper jsonMapper;

    /**
     * メッセージの取得
     *
     * @since 0.1.0
     */
    protected final DailyTasksMessageSource messageSource;

    /**
     * GitHub API の接続設定
     *
     * @since 0.1.0
     */
    protected GitHubSettingsDto settings;

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     *
     * @param jsonMapper
     *                      JSON の変換
     * @param messageSource
     *                      メッセージの取得
     */
    public GitHubApiClient(final JsonMapper jsonMapper, final DailyTasksMessageSource messageSource) {

        this.jsonMapper = jsonMapper;
        this.messageSource = messageSource;
        this.httpClientSupplier = HttpClient::newHttpClient;

    }

    /**
     * GitHub API の接続設定を受け取り、フィールドに保持する<br>
     *
     * @since 0.1.0
     *
     * @param settings
     *                 GitHub API の接続設定
     */
    @SuppressWarnings("hiding")
    public void initialize(final GitHubSettingsDto settings) {

        this.settings = settings;

    }

    /**
     * 応答の JSON を取得する<br>
     *
     * @since 0.1.0
     *
     * @param httpClient
     *                      HTTP クライアント
     * @param uri
     *                      リクエストの URI
     * @param arrayExpected
     *                      true：配列を期待する、false：オブジェクトを期待する
     *
     * @return 応答の JSON
     *
     * @throws IOException
     *                     通信に失敗した場合、または応答が不正な場合
     */
    protected JsonNode fetchResponse(final HttpClient httpClient, final URI uri, final boolean arrayExpected)
        throws IOException {

        final JsonNode result;

        /* リクエストの作成 */
        final HttpRequest.Builder builder = HttpRequest.newBuilder(uri).timeout(GitHubApiClient.TIMEOUT)
            .header(GitHubApiHeaderTypes.ACCEPT.get(), GitHubApiHeaderTypes.GITHUB_JSON.getHeader())
            .header(GitHubApiHeaderTypes.API_VERSION.getHeader(), GitHubApiHeaderTypes.API_VERSION_VALUE.getHeader())
            .GET();

        /* トークンがあるか */
        final boolean hasToken = this.hasToken();

        if (hasToken) {

            // トークンがある場合

            // 認可ヘッダーを付ける
            builder.header(GitHubApiHeaderTypes.AUTHORIZATION.get(),
                KmgString.concat(GitHubApiHeaderTypes.BEARER.getHeader(), this.settings.getToken()));

        }

        /* 送信と応答の検証 */
        final HttpResponse<String> response = this.send(httpClient, builder.build());

        /* 応答が成功か */
        final int statusCode = response.statusCode();

        if (statusCode != GitHubApiClient.STATUS_OK) {

            // 応答が成功ではない場合

            // 取得失敗として例外にする
            final String template = this.messageSource.getExcMessage(Gr002GenMsgTypes.GR002_GEN51000);
            final String message  = String.format(template, statusCode, uri);
            throw new IOException(message);

        }

        /* レート制限のログ */
        final HttpHeaders      headers           = response.headers();
        final String           remainingHeader   = GitHubApiHeaderTypes.RATE_LIMIT_REMAINING.getHeader();
        final Optional<String> remainingOptional = headers.firstValue(remainingHeader);
        final String           remaining         = remainingOptional.orElse(null);

        /* レート制限のヘッダがあるか */
        if (remaining != null) {

            // レート制限のヘッダがある場合

            // 残数をログに出す
            final String           resetHeader   = GitHubApiHeaderTypes.RATE_LIMIT_RESET.getHeader();
            final Optional<String> resetOptional = headers.firstValue(resetHeader);
            final String           reset         = resetOptional.orElse(null);
            final String           resetValue    = reset == null ? GitHubApiTextTypes.EMPTY.getText() : reset;
            final String           debugMessage  = this.messageSource.getLogMessage(Gr002LogMsgTypes.GR002_LOG51002);
            GitHubApiClient.LOGGER.debug(debugMessage, remaining, resetValue);

            /* レート制限の残りが閾値以下か */
            try {

                final int remainingCount = Integer.parseInt(remaining);

                if (remainingCount <= GitHubApiClient.RATE_LIMIT_WARN_THRESHOLD) {

                    // レート制限の残りが閾値以下の場合

                    // 警告を出す
                    final String warnMessage = this.messageSource.getLogMessage(Gr002LogMsgTypes.GR002_LOG51003);
                    GitHubApiClient.LOGGER.warn(warnMessage, remaining, resetValue);

                }

            } catch (@SuppressWarnings("unused") final NumberFormatException e) {

                // 数にできない場合

                // 警告は出さない

            }

        }

        /* 応答本文の JSON 化 */
        result = this.readTree(response.body());

        /* 期待する形か */
        final boolean array  = result.isArray();
        final boolean object = result.isObject();
        final boolean shape  = arrayExpected ? array : object;

        if (!shape) {

            // 期待する形ではない場合

            // 形式の不正として例外にする
            final Gr002GenMsgTypes messageType = arrayExpected ? Gr002GenMsgTypes.GR002_GEN51001
                : Gr002GenMsgTypes.GR002_GEN51003;
            final String           template    = this.messageSource.getExcMessage(messageType);
            final String           message     = String.format(template, uri);
            throw new IOException(message);

        }

        return result;

    }

    /**
     * トークンが指定されているかを返す<br>
     *
     * @since 0.1.0
     *
     * @return true：指定されている、false：指定されていない
     */
    protected boolean hasToken() {

        final boolean result = KmgString.isNotBlank(this.settings.getToken());
        return result;

    }

    /**
     * 応答の本文を JSON として読み込む<br>
     *
     * @since 0.1.0
     *
     * @param body
     *             応答の本文
     *
     * @return JSON
     *
     * @throws IOException
     *                     JSON として読み込めない場合
     */
    protected JsonNode readTree(final String body) throws IOException {

        final JsonNode result;

        try {

            result = this.jsonMapper.readTree(body);

        } catch (final JacksonException e) {

            throw new IOException(e);

        }

        return result;

    }

    /**
     * リクエストを送信する<br>
     *
     * @since 0.1.0
     *
     * @param httpClient
     *                   HTTP クライアント
     * @param request
     *                   リクエスト
     *
     * @return 応答
     *
     * @throws IOException
     *                     通信に失敗した場合、または中断された場合
     */
    protected HttpResponse<String> send(final HttpClient httpClient, final HttpRequest request) throws IOException {

        final HttpResponse<String> result;

        try {

            result = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        } catch (final InterruptedException e) {

            final Thread currentThread = Thread.currentThread();
            currentThread.interrupt();
            final String message = this.messageSource.getExcMessage(Gr002GenMsgTypes.GR002_GEN51002);
            throw new IOException(message, e);

        }

        return result;

    }

}
