package kmg.gr.gr002.dailytasks.carryover.application.model;

import java.util.List;

import kmg.gr.gr002.dailytasks.carryover.application.types.IssueStateTypes;

/**
 * 持ち越しの解析結果（Issue 単位）<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class CarryoverIssueModel {

    /**
     * その日の日付（yyyy-MM-dd）。タイトルから取得できない場合は null
     *
     * @since 0.1.0
     */
    private final String date;

    /**
     * 本文の「残：N」の値。記載がない場合は null
     *
     * @since 0.1.0
     */
    private final Integer declaredCount;

    /**
     * 持ち越し項目
     *
     * @since 0.1.0
     */
    private final List<CarryoverItemModel> items;

    /**
     * Issue 番号
     *
     * @since 0.1.0
     */
    private final int number;

    /**
     * 本文にあった対象セクション名
     *
     * @since 0.1.0
     */
    private final List<String> sections;

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
     *                      Issue 番号
     * @param title
     *                      Issue タイトル
     * @param date
     *                      その日の日付（yyyy-MM-dd）。タイトルから取得できない場合は null
     * @param state
     *                      Issue の状態（open / closed）
     * @param updatedAt
     *                      Issue の更新日時（ISO-8601）
     * @param sections
     *                      本文にあった対象セクション名
     * @param declaredCount
     *                      本文の「残：N」の値。記載がない場合は null
     * @param items
     *                      持ち越し項目
     */
    public CarryoverIssueModel(final int number, final String title, final String date, final String state,
        final String updatedAt, final List<String> sections, final Integer declaredCount,
        final List<CarryoverItemModel> items) {

        this.number = number;
        this.title = title;
        this.date = date;
        this.state = state;
        this.updatedAt = updatedAt;
        this.sections = List.copyOf(sections);
        this.declaredCount = declaredCount;
        this.items = List.copyOf(items);

    }

    /**
     * 持ち越し項目の件数を返す<br>
     *
     * @since 0.1.0
     *
     * @return 持ち越し項目の件数
     */
    public int getCount() {

        final int result = this.items.size();
        return result;

    }

    /**
     * その日の日付を返す<br>
     *
     * @since 0.1.0
     *
     * @return その日の日付（yyyy-MM-dd）。タイトルから取得できない場合は null
     */
    public String getDate() {

        final String result = this.date;
        return result;

    }

    /**
     * 本文の「残：N」の値を返す<br>
     *
     * @since 0.1.0
     *
     * @return 「残：N」の値。記載がない場合は null
     */
    public Integer getDeclaredCount() {

        final Integer result = this.declaredCount;
        return result;

    }

    /**
     * 持ち越し項目を返す<br>
     *
     * @since 0.1.0
     *
     * @return 持ち越し項目
     */
    public List<CarryoverItemModel> getItems() {

        final List<CarryoverItemModel> result = this.items;
        return result;

    }

    /**
     * 持ち越し項目の残り時間（分）の合計を返す<br>
     *
     * @since 0.1.0
     *
     * @return 残り時間（分）の合計
     */
    public double getMinutes() {

        final double result = this.items.stream().mapToDouble(CarryoverItemModel::getMinutes).sum();
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
     * 本文にあった対象セクション名を返す<br>
     *
     * @since 0.1.0
     *
     * @return 対象セクション名
     */
    public List<String> getSections() {

        final List<String> result = this.sections;
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
     * 対象セクションがあるかを返す<br>
     *
     * @since 0.1.0
     *
     * @return true：対象セクションがある、false：対象セクションがない
     */
    public boolean hasSection() {

        final boolean result = !this.sections.isEmpty();
        return result;

    }

    /**
     * オープン中かを返す<br>
     *
     * @since 0.1.0
     *
     * @return true：オープン中、false：クローズ済み
     */
    public boolean isOpen() {

        final boolean result = IssueStateTypes.OPEN.getState().equals(this.state);
        return result;

    }

}
