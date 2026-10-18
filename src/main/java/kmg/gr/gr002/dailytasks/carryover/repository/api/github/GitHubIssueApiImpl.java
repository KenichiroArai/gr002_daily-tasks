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
import org.springframework.beans.factory.annotation.Autowired;
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
public class GitHubIssueApiImpl implements GitHubIssueApi {

    /** ロガー */
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubIssueApiImpl.class);

    /** 1 ページあたりの取得件数（GitHub API の上限） */
    private static final int PER_PAGE = 100;

    /** 正常応答のステータスコード */
    private static final int STATUS_OK = 200;

    /** リクエストのタイムアウト */
    private static final Duration TIMEOUT = Duration.ofSeconds(30);

    /**
     * トークンが指定されているかを返す<br>
     *
     * @param settings
     *                 GitHub API の接続設定
     *
     * @return true：指定されている、false：指定されていない
     */
    private static boolean hasToken(final GitHubSettingsDto settings) {

        boolean      result = false;
        final String token  = settings.getToken();

        if (token == null) {

            return result;

        }

        result = !token.isBlank();
        return result;

    }

    /**
     * 文字列の項目を返す<br>
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

        if (value == null) {

            return result;

        }

        if (value.isNull()) {

            return result;

        }

        result = value.asString();
        return result;

    }

    /**
     * API の応答を Issue の DTO に変換する<br>
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

    /** API のベース URL（例: https://api.github.com） */
    private String apiBaseUrl = GitHubApiUrlTypes.BASE_URL.getUrl();

    /** HTTP クライアントの生成 */
    private Supplier<HttpClient> httpClientSupplier;

    /** JSON の変換 */
    @Autowired
    private JsonMapper jsonMapper;

    /** メッセージの取得 */
    @Autowired
    private MessageLogic messageLogic;

    /**
     * コンストラクタ<br>
     */
    public GitHubIssueApiImpl() {

        this.httpClientSupplier = HttpClient::newHttpClient;

    }

    /**
     * すべての Issue（オープン・クローズ済み）を取得する<br>
     * <p>
     * プルリクエストは除外する。
     * </p>
     *
     * @param settings
     *                 GitHub API の接続設定
     *
     * @return Issue（作成順）
     *
     * @throws IOException
     *                     通信に失敗した場合、または応答が不正な場合
     */
    @Override
    public List<GitHubIssueDto> fetchAllIssues(final GitHubSettingsDto settings) throws IOException {

        final List<GitHubIssueDto> result = new ArrayList<>();

        int page = 1;
        int pageSize;

        try (HttpClient httpClient = this.httpClientSupplier.get()) {

            do {

                /* 1 ページ分の取得 */
                final JsonNode issues = this.fetchPage(httpClient, settings, page);
                pageSize = issues.size();

                for (final JsonNode node : issues) {

                    // プルリクエストは対象外
                    if (node.has(GitHubIssueFieldTypes.PULL_REQUEST.getFieldName())) {

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
     * @param httpClient
     *                   HTTP クライアント
     * @param settings
     *                   GitHub API の接続設定
     * @param page
     *                   ページ番号（1 始まり）
     *
     * @return Issue の配列
     *
     * @throws IOException
     *                     通信に失敗した場合、または応答が不正な場合
     */
    private JsonNode fetchPage(final HttpClient httpClient, final GitHubSettingsDto settings, final int page)
        throws IOException {

        JsonNode result = null;

        /* リクエストの作成 */
        final URI                 uri     = URI.create(String.format(GitHubApiUrlTypes.ISSUES_QUERY.getUrl(),
            this.apiBaseUrl, settings.getRepository(), GitHubIssueApiImpl.PER_PAGE, page));
        final HttpRequest.Builder builder = HttpRequest.newBuilder(uri).timeout(GitHubIssueApiImpl.TIMEOUT)
            .header(GitHubApiHeaderTypes.ACCEPT.get(), GitHubApiHeaderTypes.GITHUB_JSON.getHeader())
            .header(GitHubApiHeaderTypes.API_VERSION.getHeader(), GitHubApiHeaderTypes.API_VERSION_VALUE.getHeader())
            .GET();

        if (GitHubIssueApiImpl.hasToken(settings)) {

            builder.header(GitHubApiHeaderTypes.AUTHORIZATION.get(),
                GitHubApiHeaderTypes.BEARER.getHeader() + settings.getToken());

        }

        /* 送信と応答の検証 */
        final HttpResponse<String> response = this.send(httpClient, builder.build());

        if (response.statusCode() != GitHubIssueApiImpl.STATUS_OK) {

            final String template = this.messageLogic.get(Gr002GenMsgTypes.GR002_GEN11103);
            final String message  = String.format(template, response.statusCode(), uri);
            throw new IOException(message);

        }

        result = this.readTree(response.body());

        if (!result.isArray()) {

            final String template = this.messageLogic.get(Gr002GenMsgTypes.GR002_GEN11104);
            final String message  = String.format(template, uri);
            throw new IOException(message);

        }

        return result;

    }

    /**
     * 応答の本文を JSON として読み込む<br>
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
