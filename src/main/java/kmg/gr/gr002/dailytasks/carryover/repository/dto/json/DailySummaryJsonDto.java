package kmg.gr.gr002.dailytasks.carryover.repository.dto.json;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * 日別の持ち越しの集計の JSON の形<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@JsonPropertyOrder({
    "date", "issue", "declaredCount", "total", "byItem", "byOriginMonth"
})
public class DailySummaryJsonDto {

    /**
     * 項目ごとの集計（キーは項目名）
     *
     * @since 0.1.0
     */
    private final ItemStatsJsonDto byItem;

    /**
     * 持ち越し元の月ごとの集計（キーは yyyy-MM）
     *
     * @since 0.1.0
     */
    private final ItemStatsJsonDto byOriginMonth;

    /**
     * その日の日付（yyyy-MM-dd）
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
     * Issue 番号
     *
     * @since 0.1.0
     */
    private final int issue;

    /**
     * 全体の集計
     *
     * @since 0.1.0
     */
    private final ItemStatJsonDto total;

    /**
     * コンストラクタ<br>
     * <p>
     * 集計の並び順は引数の順を保つ。
     * </p>
     *
     * @since 0.1.0
     *
     * @param date
     *                      その日の日付（yyyy-MM-dd）
     * @param issue
     *                      Issue 番号
     * @param declaredCount
     *                      本文の「残：N」の値。記載がない場合は null
     * @param total
     *                      全体の集計
     * @param byItem
     *                      項目ごとの集計（キーは項目名）
     * @param byOriginMonth
     *                      持ち越し元の月ごとの集計（キーは yyyy-MM）
     */
    public DailySummaryJsonDto(final String date, final int issue, final Integer declaredCount,
        final ItemStatJsonDto total, final ItemStatsJsonDto byItem, final ItemStatsJsonDto byOriginMonth) {

        this.date = date;
        this.issue = issue;
        this.declaredCount = declaredCount;
        this.total = total;
        this.byItem = new ItemStatsJsonDto(byItem);
        this.byOriginMonth = new ItemStatsJsonDto(byOriginMonth);

    }

    /**
     * 項目ごとの集計を返す<br>
     *
     * @since 0.1.0
     *
     * @return 項目ごとの集計（キーは項目名）
     */
    public ItemStatsJsonDto getByItem() {

        final ItemStatsJsonDto result = this.byItem;
        return result;

    }

    /**
     * 持ち越し元の月ごとの集計を返す<br>
     *
     * @since 0.1.0
     *
     * @return 持ち越し元の月ごとの集計（キーは yyyy-MM）
     */
    public ItemStatsJsonDto getByOriginMonth() {

        final ItemStatsJsonDto result = this.byOriginMonth;
        return result;

    }

    /**
     * その日の日付を返す<br>
     *
     * @since 0.1.0
     *
     * @return その日の日付（yyyy-MM-dd）
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
     * Issue 番号を返す<br>
     *
     * @since 0.1.0
     *
     * @return Issue 番号
     */
    public int getIssue() {

        final int result = this.issue;
        return result;

    }

    /**
     * 全体の集計を返す<br>
     *
     * @since 0.1.0
     *
     * @return 全体の集計
     */
    public ItemStatJsonDto getTotal() {

        final ItemStatJsonDto result = this.total;
        return result;

    }

}
