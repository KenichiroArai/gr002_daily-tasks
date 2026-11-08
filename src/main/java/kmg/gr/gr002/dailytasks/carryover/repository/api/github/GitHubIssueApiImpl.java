package kmg.gr.gr002.dailytasks.carryover.repository.api.github;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueRefDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubSettingsDto;
import kmg.gr.gr002.dailytasks.carryover.domain.repository.api.GitHubIssueApi;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.context.DailyTasksMessageSource;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg.Gr002LogMsgTypes;
import kmg.gr.gr002.dailytasks.carryover.repository.types.GitHubApiUrlTypes;
import kmg.gr.gr002.dailytasks.carryover.repository.types.GitHubIssueFieldTypes;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

/**
 * GitHub REST API で Issue を取得する外部 API の実装<br>
 * <p>
 * domain 層の外部 API を実装し、持ち越し向けにプルリクエスト除外とページングを行う。HTTP と単件取得は親クラスに委譲する。
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
public class GitHubIssueApiImpl extends GitHubIssueClient implements GitHubIssueApi {

    /**
     * ロガー
     *
     * @since 0.1.0
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GitHubIssueApiImpl.class);

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
    public GitHubIssueApiImpl(final JsonMapper jsonMapper, final DailyTasksMessageSource messageSource) {

        super(jsonMapper, messageSource);

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

            result.add(GitHubIssueClient.toIssue(node));

        }

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

            result.add(GitHubIssueClient.toRef(node));

        }

        return result;

    }

    /**
     * 最新の指定件数までの Issue の参照（本文なし）を取得する<br>
     * <p>
     * 作成降順で取得し、プルリクエストを除いたうえで上限件数に達したら打ち切る。戻り値は作成昇順。{@link #initialize(GitHubSettingsDto)} のあとで呼ぶ。
     * </p>
     *
     * @since 0.1.0
     *
     * @param limit
     *              取得する最大件数。1 未満のときは空を返す
     *
     * @return Issue の参照（作成順）。本文は含まない
     *
     * @throws IOException
     *                     通信に失敗した場合、または応答が不正な場合
     */
    @Override
    public List<GitHubIssueRefDto> fetchRecentIssueRefs(final int limit) throws IOException {

        final List<GitHubIssueRefDto> result = new ArrayList<>();

        /* 上限が 1 未満か */
        if (limit < 1) {

            // 上限が 1 未満の場合

            // 空を返す
            return result;

        }

        final List<JsonNode> nodes = this.fetchRecentIssueNodes(limit);

        for (final JsonNode node : nodes) {

            result.add(GitHubIssueClient.toRef(node));

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

        try (HttpClient httpClient = this.httpClientSupplier.get()) {

            int pageSize;

            do {

                /* 1 ページ分の取得 */
                final JsonNode issues = this.fetchPage(httpClient, page, GitHubApiUrlTypes.ISSUES_QUERY);
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

                final String pageMessage = this.messageSource.getLogMessage(Gr002LogMsgTypes.GR002_LOG51001);
                GitHubIssueApiImpl.LOGGER.debug(pageMessage, page, pageSize, result.size());
                page++;

            } while (pageSize >= GitHubIssueClient.PER_PAGE);

        }

        /* 取得件数のログ出力 */
        final String message = this.messageSource.getLogMessage(Gr002LogMsgTypes.GR002_LOG51000);
        GitHubIssueApiImpl.LOGGER.info(message, result.size(), page - 1);
        return result;

    }

    /**
     * プルリクエストを除いた最新の Issue の JSON を、上限件数まで取得する<br>
     * <p>
     * 作成降順でページングし、上限に達したら打ち切る。戻り値は作成昇順。
     * </p>
     *
     * @since 0.1.0
     *
     * @param limit
     *              取得する最大件数
     *
     * @return Issue の JSON（作成順）
     *
     * @throws IOException
     *                     通信に失敗した場合、または応答が不正な場合
     */
    private List<JsonNode> fetchRecentIssueNodes(final int limit) throws IOException {

        final List<JsonNode> result = new ArrayList<>();

        int page = 1;

        try (HttpClient httpClient = this.httpClientSupplier.get()) {

            int pageSize;

            do {

                /* 1 ページ分の取得 */
                final JsonNode issues = this.fetchPage(httpClient, page, GitHubApiUrlTypes.ISSUES_QUERY_DESC);
                pageSize = issues.size();

                for (final JsonNode node : issues) {

                    /* 上限に達したか */
                    final boolean enough = result.size() >= limit;

                    if (enough) {

                        // 上限に達した場合

                        // これ以上は取らない
                        break;

                    }

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

                final String pageMessage = this.messageSource.getLogMessage(Gr002LogMsgTypes.GR002_LOG51004);
                GitHubIssueApiImpl.LOGGER.debug(pageMessage, page, pageSize, result.size());
                page++;

                /* 上限に達したか */
                final boolean enough = result.size() >= limit;

                if (enough) {

                    // 上限に達した場合

                    // 次のページは取らない
                    break;

                }

            } while (pageSize >= GitHubIssueClient.PER_PAGE);

        }

        /* 作成昇順に並べ替える */
        Collections.reverse(result);

        /* 取得件数のログ出力 */
        final String message = this.messageSource.getLogMessage(Gr002LogMsgTypes.GR002_LOG51005);
        GitHubIssueApiImpl.LOGGER.info(message, result.size(), page - 1);
        return result;

    }

}
