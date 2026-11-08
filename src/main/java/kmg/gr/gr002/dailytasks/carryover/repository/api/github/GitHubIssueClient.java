package kmg.gr.gr002.dailytasks.carryover.repository.api.github;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.time.Instant;

import kmg.core.infrastructure.type.KmgString;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueRefDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubSettingsDto;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.context.DailyTasksMessageSource;
import kmg.gr.gr002.dailytasks.carryover.repository.types.GitHubApiTextTypes;
import kmg.gr.gr002.dailytasks.carryover.repository.types.GitHubApiUrlTypes;
import kmg.gr.gr002.dailytasks.carryover.repository.types.GitHubIssueFieldTypes;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * GitHub REST API の Issue 取得クライアント<br>
 * <p>
 * Issue の URL 組み立て、ページ取得、JSON から domain の DTO への変換を行う。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class GitHubIssueClient extends GitHubApiClient {

    /**
     * 1 ページあたりの取得件数（GitHub API の上限）
     *
     * @since 0.1.0
     */
    protected static final int PER_PAGE = 100;

    /**
     * 更新日時の文字列を日時に変換する<br>
     *
     * @since 0.1.0
     *
     * @param text
     *             更新日時の文字列（ISO-8601）
     *
     * @return 更新日時。文字列が null または空の場合は null
     */
    protected static Instant parseUpdatedAt(final String text) {

        Instant result = null;

        /* 文字列が空か */
        final boolean blank = KmgString.isBlank(text);

        if (blank) {

            // 文字列が空の場合

            // 日時なしとする
            return result;

        }

        result = Instant.parse(text);
        return result;

    }

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
    protected static String textOrEmpty(final JsonNode node, final String fieldName) {

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
    protected static GitHubIssueDto toIssue(final JsonNode node) {

        final GitHubIssueDto result;
        final JsonNode       numberNode    = node.path(GitHubIssueFieldTypes.NUMBER.getFieldName());
        final int            number        = numberNode.asInt();
        final String         title         = GitHubIssueClient.textOrEmpty(node,
            GitHubIssueFieldTypes.TITLE.getFieldName());
        final String         state         = GitHubIssueClient.textOrEmpty(node,
            GitHubIssueFieldTypes.STATE.getFieldName());
        final String         updatedAtText = GitHubIssueClient.textOrEmpty(node,
            GitHubIssueFieldTypes.UPDATED_AT.getFieldName());
        final Instant        updatedAt     = GitHubIssueClient.parseUpdatedAt(updatedAtText);
        final String         body          = GitHubIssueClient.textOrEmpty(node,
            GitHubIssueFieldTypes.BODY.getFieldName());

        result = new GitHubIssueDto(number, title, state, updatedAt, body);
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
    protected static GitHubIssueRefDto toRef(final JsonNode node) {

        final GitHubIssueRefDto result;
        final JsonNode          numberNode    = node.path(GitHubIssueFieldTypes.NUMBER.getFieldName());
        final int               number        = numberNode.asInt();
        final String            title         = GitHubIssueClient.textOrEmpty(node,
            GitHubIssueFieldTypes.TITLE.getFieldName());
        final String            state         = GitHubIssueClient.textOrEmpty(node,
            GitHubIssueFieldTypes.STATE.getFieldName());
        final String            updatedAtText = GitHubIssueClient.textOrEmpty(node,
            GitHubIssueFieldTypes.UPDATED_AT.getFieldName());
        final Instant           updatedAt     = GitHubIssueClient.parseUpdatedAt(updatedAtText);

        result = new GitHubIssueRefDto(number, title, state, updatedAt);
        return result;

    }

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
    public GitHubIssueClient(final JsonMapper jsonMapper, final DailyTasksMessageSource messageSource) {

        super(jsonMapper, messageSource);

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
    public GitHubIssueDto fetchIssue(final int number) throws IOException {

        final GitHubIssueDto result;

        try (HttpClient httpClient = this.httpClientSupplier.get()) {

            final URI      uri  = URI.create(String.format(GitHubApiUrlTypes.ISSUE.getUrl(), this.apiBaseUrl,
                this.settings.getRepository(), number));
            final JsonNode node = this.fetchResponse(httpClient, uri, false);
            result = GitHubIssueClient.toIssue(node);

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
     * @param queryType
     *                   一覧取得の URL の種類
     *
     * @return Issue の配列
     *
     * @throws IOException
     *                     通信に失敗した場合、または応答が不正な場合
     */
    protected JsonNode fetchPage(final HttpClient httpClient, final int page, final GitHubApiUrlTypes queryType)
        throws IOException {

        final JsonNode result;
        final URI      uri = URI.create(String.format(queryType.getUrl(), this.apiBaseUrl,
            this.settings.getRepository(), GitHubIssueClient.PER_PAGE, page));
        result = this.fetchResponse(httpClient, uri, true);
        return result;

    }

}
