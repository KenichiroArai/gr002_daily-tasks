package kmg.gr.gr002.dailytasks.carryover.domain.dto;

import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueRefModel;

/**
 * GitHub API から取得した Issue の参照（本文なし）の DTO<br>
 * <p>
 * domain 層の受け渡しの型。本文は持たない。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class GitHubIssueRefDto {

    /**
     * Issue 番号
     *
     * @since 0.1.0
     */
    private final int number;

    /**
     * Issue の状態（open / closed）
     *
     * @since 0.1.0
     */
    private final String state;

    /**
     * Issue タイトル
     *
     * @since 0.1.0
     */
    private final String title;

    /**
     * Issue の更新日時（ISO-8601）
     *
     * @since 0.1.0
     */
    private final String updatedAt;

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     *
     * @param number
     *                  Issue 番号
     * @param title
     *                  Issue タイトル
     * @param state
     *                  Issue の状態（open / closed）
     * @param updatedAt
     *                  Issue の更新日時（ISO-8601）
     */
    public GitHubIssueRefDto(final int number, final String title, final String state, final String updatedAt) {

        this.number = number;
        this.title = title;
        this.state = state;
        this.updatedAt = updatedAt;

    }

    /**
     * Issue 番号を返す<br>
     *
     * @since 0.1.0
     *
     * @return Issue 番号
     */
    public int getNumber() {

        final int result = this.number;
        return result;

    }

    /**
     * Issue の状態を返す<br>
     *
     * @since 0.1.0
     *
     * @return Issue の状態（open / closed）
     */
    public String getState() {

        final String result = this.state;
        return result;

    }

    /**
     * Issue タイトルを返す<br>
     *
     * @since 0.1.0
     *
     * @return Issue タイトル
     */
    public String getTitle() {

        final String result = this.title;
        return result;

    }

    /**
     * Issue の更新日時を返す<br>
     *
     * @since 0.1.0
     *
     * @return Issue の更新日時（ISO-8601）
     */
    public String getUpdatedAt() {

        final String result = this.updatedAt;
        return result;

    }

    /**
     * 本文を持たない参照へ変換する<br>
     * <p>
     * 日付はまだ決めていない。
     * </p>
     *
     * @since 0.1.0
     *
     * @return 日々のタスク Issue の参照。日付は null
     */
    public DailyTaskIssueRefModel toDailyTaskIssueRefModel() {

        final DailyTaskIssueRefModel result
            = new DailyTaskIssueRefModel(this.number, this.title, this.state, this.updatedAt, null);
        return result;

    }

}
