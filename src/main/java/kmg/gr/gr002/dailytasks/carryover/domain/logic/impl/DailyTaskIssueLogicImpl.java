package kmg.gr.gr002.dailytasks.carryover.domain.logic.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import kmg.gr.gr002.dailytasks.carryover.application.logic.DailyTaskIssueLogic;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueRefModel;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubIssueRefDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.GitHubSettingsDto;
import kmg.gr.gr002.dailytasks.carryover.domain.repository.api.GitHubIssueApi;

/**
 * 日々のタスク Issue を取得するロジックの実装<br>
 * <p>
 * application 層の取得ロジックを実装する。取得した Issue は DTO がモデルへ変換する。接続設定は {@link #initialize(String, String)} で受け取り、domain 層の DTO
 * に変換して外部 API を初期化する。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@Component
@Scope("prototype")
public class DailyTaskIssueLogicImpl implements DailyTaskIssueLogic {

    /**
     * GitHub の Issue の取得
     *
     * @since 0.1.0
     */
    private final GitHubIssueApi gitHubIssueApi;

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     *
     * @param gitHubIssueApi
     *                       GitHub の Issue の取得
     */
    public DailyTaskIssueLogicImpl(final GitHubIssueApi gitHubIssueApi) {

        this.gitHubIssueApi = gitHubIssueApi;

    }

    /**
     * すべての日々のタスク Issue を取得する<br>
     *
     * @since 0.1.0
     *
     * @return 日々のタスク Issue（作成順）
     *
     * @throws IOException
     *                     取得に失敗した場合
     */
    @Override
    public List<DailyTaskIssueModel> fetchAllIssues() throws IOException {

        final List<DailyTaskIssueModel> result;
        final List<GitHubIssueDto>      issues = this.gitHubIssueApi.fetchAllIssues();

        @SuppressWarnings("null")
        final List<DailyTaskIssueModel> models = issues.stream().map(GitHubIssueDto::toDailyTaskIssueModel).toList();
        result = models;
        return result;

    }

    /**
     * Issue を 1 件取得する<br>
     *
     * @since 0.1.0
     *
     * @param number
     *               Issue 番号
     *
     * @return 日々のタスク Issue
     *
     * @throws IOException
     *                     取得に失敗した場合
     */
    @Override
    public DailyTaskIssueModel fetchIssue(final int number) throws IOException {

        final DailyTaskIssueModel result;
        final GitHubIssueDto      issue = this.gitHubIssueApi.fetchIssue(number);

        result = issue.toDailyTaskIssueModel();
        return result;

    }

    /**
     * すべての日々のタスク Issue の参照（本文なし）を取得する<br>
     * <p>
     * 日付はまだ入れない。
     * </p>
     *
     * @since 0.1.0
     *
     * @return 日々のタスク Issue の参照（作成順）。日付は null
     *
     * @throws IOException
     *                     取得に失敗した場合
     */
    @Override
    public List<DailyTaskIssueRefModel> fetchIssueRefs() throws IOException {

        final List<DailyTaskIssueRefModel> result = new ArrayList<>();
        final List<GitHubIssueRefDto>      refs   = this.gitHubIssueApi.fetchIssueRefs();

        for (final GitHubIssueRefDto ref : refs) {

            result.add(ref.toDailyTaskIssueRefModel());

        }

        return result;

    }

    /**
     * 最新の指定件数までの日々のタスク Issue の参照（本文なし）を取得する<br>
     * <p>
     * 日付はまだ入れない。戻り値は作成昇順。
     * </p>
     *
     * @since 0.1.0
     *
     * @param limit
     *              取得する最大件数
     *
     * @return 日々のタスク Issue の参照（作成順）。日付は null
     *
     * @throws IOException
     *                     取得に失敗した場合
     */
    @Override
    public List<DailyTaskIssueRefModel> fetchRecentIssueRefs(final int limit) throws IOException {

        final List<DailyTaskIssueRefModel> result = new ArrayList<>();
        final List<GitHubIssueRefDto>      refs   = this.gitHubIssueApi.fetchRecentIssueRefs(limit);

        for (final GitHubIssueRefDto ref : refs) {

            result.add(ref.toDailyTaskIssueRefModel());

        }

        return result;

    }

    /**
     * 対象リポジトリとトークンを受け取り、Issue の取得先を初期化する<br>
     * <p>
     * 外部 API も同じ接続設定で初期化する。
     * </p>
     *
     * @since 0.1.0
     *
     * @param repository
     *                   Issue を取得するリポジトリ（owner/name）
     * @param token
     *                   GitHub API のトークン。未指定の場合は null
     */
    @Override
    public void initialize(final String repository, final String token) {

        this.gitHubIssueApi.initialize(new GitHubSettingsDto(repository, token));

    }

}
