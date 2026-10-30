package kmg.gr.gr002.dailytasks.carryover.repository.api.github;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import kmg.core.infrastructure.type.KmgString;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueRefDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubSettingsDto;
import kmg.gr.gr002.dailytasks.carryover.domain.repository.api.GitHubIssueApi;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.logic.MessageLogic;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg.Gr002GenMsgTypes;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg.Gr002LogMsgTypes;
import kmg.gr.gr002.dailytasks.carryover.repository.types.GitHubApiHeaderTypes;
import kmg.gr.gr002.dailytasks.carryover.repository.types.GitHubApiTextTypes;
import kmg.gr.gr002.dailytasks.carryover.repository.types.GitHubApiUrlTypes;
import kmg.gr.gr002.dailytasks.carryover.repository.types.GitHubIssueFieldTypes;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * GitHub REST API で Issue を取得する外部 API の実装<br>
 * <p>
 * domain 層の外部 API を実装し、API の応答を domain 層の DTO に変換して返す。HTTP クライアントは取得のたびに生成して閉じる。トークンはログに出力しない。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@Repository
@Scope("prototype")
public class GitHubIssueApiImpl implements GitHubIssueApi {

    /**
     * ロガー
     *
     * @since 0.1.0
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubIssueApiImpl.class);

    /**
     * 1 ページあたりの取得件数（GitHub API の上限）
     *
     * @since 0.1.0
     */
    private static final int PER_PAGE = 100;

    /**
     * レート制限の残りが少ないとみなす閾値
     *
     * @since 0.1.0
     */
    private static final int RATE_LIMIT_WARN_THRESHOLD = 10;

    /**
     * 正常応答のステータスコード
     *
     * @since 0.1.0
     */
    private static final int STATUS_OK = 200;

    /**
     * リクエストのタイムアウト
     *
     * @since 0.1.0
     */
    private static final Duration TIMEOUT = Duration.ofSeconds(30);

    /**
     * 文字列の項目を返す<br>
     *
     * @since 0.1.0
     *
     * @param node
     *                  JSON
     * @param fieldName
     *                  項目名
     *
     * @return 値。項目がない場合または null の場合は空文字
     */
    private static String textOrEmpty(final JsonNode node, final String fieldName) {

        String         result = GitHubApiTextTypes.EMPTY.getText();
        final JsonNode value  = node.get(fieldName);

        /* フィールドがないか */
        if (value == null) {

            // フィールドがない場合

            // 空文字を返す
            return result;

        }

        /* フィールドが null か */
        final boolean nullValue = value.isNull();

        if (nullValue) {

            // フィールドが null の場合

            // 空文字を返す
            return result;

        }

        result = value.asString();
        return result;

    }

    /**
     * API の応答を Issue の DTO に変換する<br>
     *
     * @since 0.1.0
     *
     * @param node
     *             Issue の JSON
     *
     * @return Issue の DTO
     */
    private static GitHubIssueDto toIssue(final JsonNode node) {

        final GitHubIssueDto result;
        final JsonNode       numberNode = node.path(GitHubIssueFieldTypes.NUMBER.getFieldName());
        final int            number     = numberNode.asInt();

        result = new GitHubIssueDto(number,
            GitHubIssueApiImpl.textOrEmpty(node, GitHubIssueFieldTypes.TITLE.getFieldName()),
            GitHubIssueApiImpl.textOrEmpty(node, GitHubIssueFieldTypes.STATE.getFieldName()),
            GitHubIssueApiImpl.textOrEmpty(node, GitHubIssueFieldTypes.UPDATED_AT.getFieldName()),
            GitHubIssueApiImpl.textOrEmpty(node, GitHubIssueFieldTypes.BODY.getFieldName()));
        return result;

    }

    /**
     * API の応答を本文なしの参照に変換する<br>
     *
     * @since 0.1.0
     *
     * @param node
     *             Issue の JSON
     *
     * @return Issue の参照。本文は含まない
     */
    private static GitHubIssueRefDto toRef(final JsonNode node) {

        final GitHubIssueRefDto result;
        final JsonNode          numberNode = node.path(GitHubIssueFieldTypes.NUMBER.getFieldName());
        final int               number     = numberNode.asInt();

        result = new GitHubIssueRefDto(number,
            GitHubIssueApiImpl.textOrEmpty(node, GitHubIssueFieldTypes.TITLE.getFieldName()),
            GitHubIssueApiImpl.textOrEmpty(node, GitHubIssueFieldTypes.STATE.getFieldName()),
            GitHubIssueApiImpl.textOrEmpty(node, GitHubIssueFieldTypes.UPDATED_AT.getFieldName()));
        return result;

    }

    /**
     * API のベース URL（例: https://api.github.com）
     *
     * @since 0.1.0
     */
    private final String apiBaseUrl = GitHubApiUrlTypes.BASE_URL.getUrl();

    /**
     * HTTP クライアントの生成
     *
     * @since 0.1.0
     */
    private final Supplier<HttpClient> httpClientSupplier;

    /**
     * JSON の変換
     *
     * @since 0.1.0
     */
    private final JsonMapper jsonMapper;

    /**
     * メッセージの取得
     *
     * @since 0.1.0
     */
    private final MessageLogic messageLogic;

    /**
     * GitHub API の接続設定
     *
     * @since 0.1.0
     */
    private GitHubSettingsDto settings;

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     *
     * @param jsonMapper
     *                     JSON の変換
     * @param messageLogic
     *                     メッセージの取得
     */
    public GitHubIssueApiImpl(final JsonMapper jsonMapper, final MessageLogic messageLogic) {

        this.jsonMapper = jsonMapper;
        this.messageLogic = messageLogic;
        this.httpClientSupplier = HttpClient::newHttpClient;

    }

    /**
     * すべての Issue（オープン・クローズ済み）を取得する<br>
     * <p>
     * プルリクエストは除外する。
     * </p>
     *
     * @since 0.1.0
     *
     * @return Issue（作成順）
     *
     * @throws IOException
     *                     通信に失敗した場合、または応答が不正な場合
     */
    @Override
    public List<GitHubIssueDto> fetchAllIssues() throws IOException {

        final List<GitHubIssueDto> result = new ArrayList<>();
        final List<JsonNode>       nodes  = this.fetchIssueNodes();

        for (final JsonNode node : nodes) {

            result.add(GitHubIssueApiImpl.toIssue(node));

        }

        return result;

    }

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
    @Override
    public GitHubIssueDto fetchIssue(final int number) throws IOException {

        final GitHubIssueDto result;

        try (HttpClient httpClient = this.httpClientSupplier.get()) {

            final URI      uri  = URI.create(String.format(GitHubApiUrlTypes.ISSUE.getUrl(), this.apiBaseUrl,
                this.settings.getRepository(), number));
            final JsonNode node = this.fetchResponse(httpClient, uri, false);
            result = GitHubIssueApiImpl.toIssue(node);

        }

        return result;

    }

    /**
     * プルリクエストを除いた Issue の JSON を、作成順に取得する<br>
     *
     * @since 0.1.0
     *
     * @return Issue の JSON（作成順）
     *
     * @throws IOException
     *                     通信に失敗した場合、または応答が不正な場合
     */
    private List<JsonNode> fetchIssueNodes() throws IOException {

        final List<JsonNode> result = new ArrayList<>();

        int page = 1;
        int pageSize;

        try (HttpClient httpClient = this.httpClientSupplier.get()) {

            do {

                /* 1 ページ分の取得 */
                final JsonNode issues = this.fetchPage(httpClient, page);
                pageSize = issues.size();

                for (final JsonNode node : issues) {

                    /* プルリクエストか */
                    final String  pullRequestField = GitHubIssueFieldTypes.PULL_REQUEST.getFieldName();
                    final boolean pullRequest      = node.has(pullRequestField);

                    if (pullRequest) {

                        // プルリクエストの場合

                        // 対象外として次へ進む
                        continue;

                    }

                    result.add(node);

                }

                final String pageMessage = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12028);
                GitHubIssueApiImpl.LOGGER.debug(pageMessage, Integer.valueOf(page), Integer.valueOf(pageSize),
                    Integer.valueOf(result.size()));
                page++;

            } while (pageSize >= GitHubIssueApiImpl.PER_PAGE);

        }

        /* 取得件数のログ出力 */
        final String message = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12010);
        GitHubIssueApiImpl.LOGGER.info(message, result.size(), page - 1);
        return result;

    }

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
    @Override
    public List<GitHubIssueRefDto> fetchIssueRefs() throws IOException {

        final List<GitHubIssueRefDto> result = new ArrayList<>();
        final List<JsonNode>          nodes  = this.fetchIssueNodes();

        for (final JsonNode node : nodes) {

            result.add(GitHubIssueApiImpl.toRef(node));

        }

        return result;

    }

    /**
     * 1 ページ分の Issue を取得する<br>
     *
     * @since 0.1.0
     *
     * @param httpClient
     *                   HTTP クライアント
     * @param page
     *                   ページ番号（1 始まり）
     *
     * @return Issue の配列
     *
     * @throws IOException
     *                     通信に失敗した場合、または応答が不正な場合
     */
    private JsonNode fetchPage(final HttpClient httpClient, final int page) throws IOException {

        final URI      uri    = URI.create(String.format(GitHubApiUrlTypes.ISSUES_QUERY.getUrl(), this.apiBaseUrl,
            this.settings.getRepository(), GitHubIssueApiImpl.PER_PAGE, page));
        final JsonNode result = this.fetchResponse(httpClient, uri, true);
        return result;

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
    private JsonNode fetchResponse(final HttpClient httpClient, final URI uri, final boolean arrayExpected)
        throws IOException {

        final JsonNode result;

        /* リクエストの作成 */
        final HttpRequest.Builder builder = HttpRequest.newBuilder(uri).timeout(GitHubIssueApiImpl.TIMEOUT)
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

        if (statusCode != GitHubIssueApiImpl.STATUS_OK) {

            // 応答が成功ではない場合

            // 取得失敗として例外にする
            final String template = this.messageLogic.get(Gr002GenMsgTypes.GR002_GEN11103);
            final String message  = String.format(template, statusCode, uri);
            throw new IOException(message);

        }

        /* レート制限のログ */
        this.logRateLimit(response);

        /* 応答本文の JSON 化 */
        result = this.readTree(response.body());

        /* 期待する形か */
        final boolean array  = result.isArray();
        final boolean object = result.isObject();
        final boolean shape  = arrayExpected ? array : object;

        if (!shape) {

            // 期待する形ではない場合

            // 形式の不正として例外にする
            final Gr002GenMsgTypes messageType = arrayExpected ? Gr002GenMsgTypes.GR002_GEN11104
                : Gr002GenMsgTypes.GR002_GEN11106;
            final String           template    = this.messageLogic.get(messageType);
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
    private boolean hasToken() {

        boolean      result = false;
        final String token  = this.settings.getToken();

        /* トークンがないか */
        if (token == null) {

            // トークンがない場合

            // トークンなしとする
            return result;

        }

        result = !token.isBlank();
        return result;

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
    @Override
    public void initialize(final GitHubSettingsDto settings) {

        this.settings = settings;

    }

    /**
     * レート制限のヘッダをログに出す<br>
     *
     * @since 0.1.0
     *
     * @param response
     *                 HTTP 応答
     */
    private void logRateLimit(final HttpResponse<String> response) {

        final String remainingHeader = GitHubApiHeaderTypes.RATE_LIMIT_REMAINING.getHeader();
        final String resetHeader     = GitHubApiHeaderTypes.RATE_LIMIT_RESET.getHeader();
        final String remaining       = response.headers().firstValue(remainingHeader).orElse(null);
        final String reset           = response.headers().firstValue(resetHeader).orElse(null);

        /* レート制限のヘッダがあるか */
        if (remaining == null) {

            // レート制限のヘッダがない場合

            // ログに出さない
            return;

        }

        final String resetValue   = reset == null ? GitHubApiTextTypes.EMPTY.getText() : reset;
        final String debugMessage = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12029);
        GitHubIssueApiImpl.LOGGER.debug(debugMessage, remaining, resetValue);

        /* レート制限の残りが閾値以下か */
        final boolean warn;

        try {

            final int remainingCount = Integer.parseInt(remaining);
            warn = remainingCount <= GitHubIssueApiImpl.RATE_LIMIT_WARN_THRESHOLD;

        } catch (final NumberFormatException e) {

            // 数にできない場合

            // 警告は出さない
            return;

        }

        if (!warn) {

            // レート制限の残りが閾値より多い場合

            // 警告は出さない
            return;

        }

        final String warnMessage = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12030);
        GitHubIssueApiImpl.LOGGER.warn(warnMessage, remaining, resetValue);

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
    private JsonNode readTree(final String body) throws IOException {

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
    private HttpResponse<String> send(final HttpClient httpClient, final HttpRequest request) throws IOException {

        final HttpResponse<String> result;

        try {

            result = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        } catch (final InterruptedException e) {

            final Thread currentThread = Thread.currentThread();
            currentThread.interrupt();
            final String message = this.messageLogic.get(Gr002GenMsgTypes.GR002_GEN11105);
            throw new IOException(message, e);

        }

        return result;

    }

}
