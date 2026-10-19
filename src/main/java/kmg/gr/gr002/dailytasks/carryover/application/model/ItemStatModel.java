package kmg.gr.gr002.dailytasks.carryover.application.model;

/**
 * 持ち越し項目の件数と残り時間の集計値<br>
 * <p>
 * チェック済みの件数・残り時間も保持し、未チェック分は全体との差で求める。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class ItemStatModel {

    /**
     * チェック済みの件数
     *
     * @since 0.1.0
     */
    private int checkedCount;

    /**
     * チェック済みの残り時間（分）
     *
     * @since 0.1.0
     */
    private double checkedMinutes;

    /**
     * 件数
     *
     * @since 0.1.0
     */
    private int count;

    /**
     * 残り時間（分）
     *
     * @since 0.1.0
     */
    private double minutes;

    /**
     * 持ち越し項目を集計に加える<br>
     *
     * @since 0.1.0
     *
     * @param item
     *             持ち越し項目
     */
    public void add(final CarryoverItemModel item) {

        /* 全体の集計 */
        this.count++;
        this.minutes += item.getMinutes();

        if (!item.isChecked()) {

            return;

        }

        /* チェック済みの集計 */
        this.checkedCount++;
        this.checkedMinutes += item.getMinutes();

    }

    /**
     * チェック済みの件数を返す<br>
     *
     * @since 0.1.0
     *
     * @return チェック済みの件数
     */
    public int getCheckedCount() {

        final int result = this.checkedCount;
        return result;

    }

    /**
     * チェック済みの残り時間（分）を返す<br>
     *
     * @since 0.1.0
     *
     * @return チェック済みの残り時間（分）
     */
    public double getCheckedMinutes() {

        final double result = this.checkedMinutes;
        return result;

    }

    /**
     * 件数を返す<br>
     *
     * @since 0.1.0
     *
     * @return 件数
     */
    public int getCount() {

        final int result = this.count;
        return result;

    }

    /**
     * 残り時間（分）を返す<br>
     *
     * @since 0.1.0
     *
     * @return 残り時間（分）
     */
    public double getMinutes() {

        final double result = this.minutes;
        return result;

    }

}
