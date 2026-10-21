package kmg.gr.gr002.dailytasks.carryover.domain.dto;

import java.util.List;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverItemModel;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.logic.MessageLogic;

/**
 * 持ち越しの解析結果（Issue 単位）の DTO<br>
 * <p>
 * domain 層が repository 層と Issue ごとの解析結果を受け渡すための型（domain 層の受け渡しの型）。保存の形式は repository 層に任せる。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class CarryoverIssueDto {

    /**
     * 持ち越し項目の件数
     *
     * @since 0.1.0
     */
    private final int count;

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
    private final List<CarryoverItemDto> items;

    /**
     * 持ち越し項目の残り時間（分）の合計
     *
     * @since 0.1.0
     */
    private final double minutes;

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
     * 持ち越しの解析結果から保存用の DTO を作る<br>
     * <p>
     * 件数と残り時間の合計は、項目のリストから求める。
     * </p>
     *
     * @since 0.1.0
     *
     * @param issue
     *              持ち越しの解析結果
     */
    public CarryoverIssueDto(final CarryoverIssueModel issue) {

        this(issue.getNumber(), issue.getTitle(), issue.getDate(), issue.getState(), issue.getUpdatedAt(),
            issue.getSections(), issue.getDeclaredCount(), issue.getCount(), issue.getMinutes(),
            issue.getItems().stream().map(CarryoverItemDto::new).toList());

    }

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
     * @param count
     *                      持ち越し項目の件数
     * @param minutes
     *                      持ち越し項目の残り時間（分）の合計
     * @param items
     *                      持ち越し項目
     */
    public CarryoverIssueDto(final int number, final String title, final String date, final String state,
        final String updatedAt, final List<String> sections, final Integer declaredCount, final int count,
        final double minutes, final List<CarryoverItemDto> items) {

        this.number = number;
        this.title = title;
        this.date = date;
        this.state = state;
        this.updatedAt = updatedAt;
        this.sections = List.copyOf(sections);
        this.declaredCount = declaredCount;
        this.count = count;
        this.minutes = minutes;
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

        final int result = this.count;
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
    public List<CarryoverItemDto> getItems() {

        final List<CarryoverItemDto> result = this.items;
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

        final double result = this.minutes;
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
     * 保存済みの解析結果を持ち越しの解析結果に変換する<br>
     *
     * @since 0.1.0
     *
     * @param messageLogic
     *                     メッセージの取得
     *
     * @return 持ち越しの解析結果
     *
     * @throws IllegalArgumentException
     *                                  残り時間の取得元が不明な場合
     */
    public CarryoverIssueModel toCarryoverIssueModel(final MessageLogic messageLogic) {

        final CarryoverIssueModel result;

        final List<CarryoverItemModel> itemModels
            = this.items.stream().map(item -> item.toCarryoverItemModel(messageLogic)).toList();
        result = new CarryoverIssueModel(this.number, this.title, this.date, this.state, this.updatedAt, this.sections,
            this.declaredCount, itemModels);
        return result;

    }

}
