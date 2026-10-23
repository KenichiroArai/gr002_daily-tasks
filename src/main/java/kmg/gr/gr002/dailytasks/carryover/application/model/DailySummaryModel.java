package kmg.gr.gr002.dailytasks.carryover.application.model;

/**
 * 日別の持ち越しの集計<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class DailySummaryModel {

    /**
     * 項目ごとの集計
     *
     * @since 0.1.0
     */
    private final ItemStatsModel byItem;

    /**
     * 持ち越し元の月（yyyy-MM）ごとの集計
     *
     * @since 0.1.0
     */
    private final ItemStatsModel byOriginMonth;

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
    private final ItemStatModel total;

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     *
     * @param date
     *                      その日の日付（yyyy-MM-dd）
     * @param issue
     *                      Issue 番号
     * @param declaredCount
     *                      本文の「残：N」の値。記載がない場合は null
     */
    public DailySummaryModel(final String date, final int issue, final Integer declaredCount) {

        this.date = date;
        this.issue = issue;
        this.declaredCount = declaredCount;
        this.total = new ItemStatModel();
        this.byItem = new ItemStatsModel();
        this.byOriginMonth = new ItemStatsModel();

    }

    /**
     * 持ち越し項目を集計に加える<br>
     *
     * @since 0.1.0
     *
     * @param item
     *             持ち越し項目
     */
    public void add(final CarryoverItemModel item) {

        /* 全体と項目ごとの集計 */
        this.total.add(item);
        final ItemStatModel itemStat = this.byItem.findOrCreate(item.getName());
        itemStat.add(item);

        /* 持ち越し元の日付があるか */
        final String originDate = item.getOriginDate();

        if (originDate == null) {

            // 持ち越し元の日付がない場合

            // 月ごとの集計に含めない
            return;

        }

        /* 持ち越し元の月ごとの集計 */
        final String        month     = originDate.substring(0, 7);
        final ItemStatModel monthStat = this.byOriginMonth.findOrCreate(month);
        monthStat.add(item);

    }

    /**
     * 項目ごとの集計を返す<br>
     *
     * @since 0.1.0
     *
     * @return 項目ごとの集計（キーは項目名）
     */
    public ItemStatsModel getByItem() {

        final ItemStatsModel result = this.byItem;
        return result;

    }

    /**
     * 持ち越し元の月ごとの集計を返す<br>
     *
     * @since 0.1.0
     *
     * @return 持ち越し元の月ごとの集計（キーは yyyy-MM）
     */
    public ItemStatsModel getByOriginMonth() {

        final ItemStatsModel result = this.byOriginMonth;
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
    public ItemStatModel getTotal() {

        final ItemStatModel result = this.total;
        return result;

    }

}
