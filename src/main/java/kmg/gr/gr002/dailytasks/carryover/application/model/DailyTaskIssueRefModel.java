package kmg.gr.gr002.dailytasks.carryover.application.model;

/**
 * 日々のタスク Issue の参照（本文なし）<br>
 * <p>
 * 単位に分けるための索引。本文は持たない。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class DailyTaskIssueRefModel {

    /**
     * タイトルから取得した日付（yyyy-MM-dd）。取得できない場合は null
     *
     * @since 0.1.0
     */
    private final String date;

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
     * @param date
     *                  タイトルから取得した日付（yyyy-MM-dd）。取得できない場合は null
     */
    public DailyTaskIssueRefModel(final int number, final String title, final String state, final String updatedAt,
        final String date) {

        this.number = number;
        this.title = title;
        this.state = state;
        this.updatedAt = updatedAt;
        this.date = date;

    }

    /**
     * タイトルから取得した日付を返す<br>
     *
     * @since 0.1.0
     *
     * @return 日付（yyyy-MM-dd）。取得できない場合は null
     */
    public String getDate() {

        final String result = this.date;
        return result;

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

}
