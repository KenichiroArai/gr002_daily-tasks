package kmg.gr.gr002.dailytasks.carryover.domain.dto;

import kmg.gr.gr002.dailytasks.carryover.application.model.ItemStatModel;

/**
 * 持ち越し項目の件数と残り時間の集計値の DTO<br>
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
public class ItemStatDto {

    /**
     * チェック済みの件数
     *
     * @since 0.1.0
     */
    private final int checkedCount;

    /**
     * チェック済みの残り時間（分）
     *
     * @since 0.1.0
     */
    private final double checkedMinutes;

    /**
     * 件数
     *
     * @since 0.1.0
     */
    private final int count;

    /**
     * 残り時間（分）
     *
     * @since 0.1.0
     */
    private final double minutes;

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     *
     * @param count
     *                       件数
     * @param minutes
     *                       残り時間（分）
     * @param checkedCount
     *                       チェック済みの件数
     * @param checkedMinutes
     *                       チェック済みの残り時間（分）
     */
    public ItemStatDto(final int count, final double minutes, final int checkedCount, final double checkedMinutes) {

        this.count = count;
        this.minutes = minutes;
        this.checkedCount = checkedCount;
        this.checkedMinutes = checkedMinutes;

    }

    /**
     * 集計値から保存用の DTO を作る<br>
     *
     * @since 0.1.0
     *
     * @param stat
     *             集計値
     */
    public ItemStatDto(final ItemStatModel stat) {

        this(stat.getCount(), stat.getMinutes(), stat.getCheckedCount(), stat.getCheckedMinutes());

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
