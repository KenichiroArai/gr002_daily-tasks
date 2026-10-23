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

import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueDto;
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

        final GitHubIssueDto result = new GitHubIssueDto(node.path(GitHubIssueFieldTypes.NUMBER.getFieldName()).asInt(),
            GitHubIssueApiImpl.textOrEmpty(node, GitHubIssueFieldTypes.TITLE.getFieldName()),
            GitHubIssueApiImpl.textOrEmpty(node, GitHubIssueFieldTypes.STATE.getFieldName()),
            GitHubIssueApiImpl.textOrEmpty(node, GitHubIssueFieldTypes.UPDATED_AT.getFieldName()),
            GitHubIssueApiImpl.textOrEmpty(node, GitHubIssueFieldTypes.BODY.getFieldName()));
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

                    result.add(GitHubIssueApiImpl.toIssue(node));

                }

                page++;

            } while (pageSize >= GitHubIssueApiImpl.PER_PAGE);

        }

        final String message = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12010);
        GitHubIssueApiImpl.LOGGER.info(message, result.size(), page - 1);
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

        JsonNode result = null;

        /* リクエストの作成 */
        final URI                 uri     = URI.create(String.format(GitHubApiUrlTypes.ISSUES_QUERY.getUrl(),
            this.apiBaseUrl, this.settings.getRepository(), GitHubIssueApiImpl.PER_PAGE, page));
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
                GitHubApiHeaderTypes.BEARER.getHeader() + this.settings.getToken());

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

        result = this.readTree(response.body());

        /* 応答が配列か */
        final boolean array = result.isArray();

        if (!array) {

            // 応答が配列ではない場合

            // 形式の不正として例外にする
            final String template = this.messageLogic.get(Gr002GenMsgTypes.GR002_GEN11104);
            final String message  = String.format(template, uri);
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

        JsonNode result = null;

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

        HttpResponse<String> result = null;

        try {

            result = httpClient.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

        } catch (final InterruptedException e) {

            Thread.currentThread().interrupt();
            final String message = this.messageLogic.get(Gr002GenMsgTypes.GR002_GEN11105);
            throw new IOException(message, e);

        }

        return result;

    }

}
