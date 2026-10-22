package kmg.gr.gr002.dailytasks.carryover.domain.dto;

import kmg.gr.gr002.dailytasks.carryover.application.model.DailySummaryModel;

/**
 * 日別の持ち越しの集計の DTO<br>
 * <p>
 * domain 層の受け渡しの型。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class DailySummaryDto {

    /**
     * 項目ごとの集計（キーは項目名）
     *
     * @since 0.1.0
     */
    private final ItemStatsDto byItem;

    /**
     * 持ち越し元の月ごとの集計（キーは yyyy-MM）
     *
     * @since 0.1.0
     */
    private final ItemStatsDto byOriginMonth;

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
    private final ItemStatDto total;

    /**
     * 日別の集計から保存用の DTO を作る<br>
     * <p>
     * 集計の並び順は元のままにする。
     * </p>
     *
     * @since 0.1.0
     *
     * @param day
     *            日別の集計
     */
    public DailySummaryDto(final DailySummaryModel day) {

        this(day.getDate(), day.getIssue(), day.getDeclaredCount(), new ItemStatDto(day.getTotal()),
            new ItemStatsDto(day.getByItem()), new ItemStatsDto(day.getByOriginMonth()));

    }

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
    public DailySummaryDto(final String date, final int issue, final Integer declaredCount, final ItemStatDto total,
        final ItemStatsDto byItem, final ItemStatsDto byOriginMonth) {

        this.date = date;
        this.issue = issue;
        this.declaredCount = declaredCount;
        this.total = total;
        this.byItem = new ItemStatsDto(byItem);
        this.byOriginMonth = new ItemStatsDto(byOriginMonth);

    }

    /**
     * 項目ごとの集計を返す<br>
     *
     * @since 0.1.0
     *
     * @return 項目ごとの集計（キーは項目名）
     */
    public ItemStatsDto getByItem() {

        final ItemStatsDto result = this.byItem;
        return result;

    }

    /**
     * 持ち越し元の月ごとの集計を返す<br>
     *
     * @since 0.1.0
     *
     * @return 持ち越し元の月ごとの集計（キーは yyyy-MM）
     */
    public ItemStatsDto getByOriginMonth() {

        final ItemStatsDto result = this.byOriginMonth;
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
    public ItemStatDto getTotal() {

        final ItemStatDto result = this.total;
        return result;

    }

}
