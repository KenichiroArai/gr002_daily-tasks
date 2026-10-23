package kmg.gr.gr002.dailytasks.carryover.domain.dto;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverItemModel;
import kmg.gr.gr002.dailytasks.carryover.application.types.MinutesSourceTypes;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.logic.MessageLogic;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg.Gr002GenMsgTypes;

/**
 * 持ち越し項目の DTO（Issue ごとの解析結果の 1 行分）<br>
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
public class CarryoverItemDto {

    /**
     * チェック済みか
     *
     * @since 0.1.0
     */
    private final boolean checked;

    /**
     * 残り時間（分）
     *
     * @since 0.1.0
     */
    private final double minutes;

    /**
     * 残り時間の取得元（parsed / default / unknown）
     *
     * @since 0.1.0
     */
    private final String minutesSource;

    /**
     * 項目名（正規化済み）
     *
     * @since 0.1.0
     */
    private final String name;

    /**
     * 持ち越し元の日付（yyyy-MM-dd）。日付がない行は null
     *
     * @since 0.1.0
     */
    private final String originDate;

    /**
     * 元の行
     *
     * @since 0.1.0
     */
    private final String raw;

    /**
     * セクション名
     *
     * @since 0.1.0
     */
    private final String section;

    /**
     * 残り時間の取得元の名前を返す<br>
     *
     * @since 0.1.0
     *
     * @param item
     *             持ち越し項目
     *
     * @return 取得元の名前（parsed / default / unknown）
     */
    private static String minutesSourceName(final CarryoverItemModel item) {

        final String             result;
        final MinutesSourceTypes source = item.getMinutesSource();

        result = source.getSource();
        return result;

    }

    /**
     * 持ち越し項目から保存用の DTO を作る<br>
     *
     * @since 0.1.0
     *
     * @param item
     *             持ち越し項目
     */
    public CarryoverItemDto(final CarryoverItemModel item) {

        this(item.getName(), item.getOriginDate(), item.isChecked(), item.getMinutes(),
            CarryoverItemDto.minutesSourceName(item), item.getSection(), item.getRaw());

    }

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     *
     * @param name
     *                      項目名（正規化済み）
     * @param originDate
     *                      持ち越し元の日付（yyyy-MM-dd）。日付がない行は null
     * @param checked
     *                      チェック済みか
     * @param minutes
     *                      残り時間（分）
     * @param minutesSource
     *                      残り時間の取得元（parsed / default / unknown）
     * @param section
     *                      セクション名
     * @param raw
     *                      元の行
     */
    public CarryoverItemDto(final String name, final String originDate, final boolean checked, final double minutes,
        final String minutesSource, final String section, final String raw) {

        this.name = name;
        this.originDate = originDate;
        this.checked = checked;
        this.minutes = minutes;
        this.minutesSource = minutesSource;
        this.section = section;
        this.raw = raw;

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

    /**
     * 残り時間の取得元を返す<br>
     *
     * @since 0.1.0
     *
     * @return 残り時間の取得元（parsed / default / unknown）
     */
    public String getMinutesSource() {

        final String result = this.minutesSource;
        return result;

    }

    /**
     * 項目名を返す<br>
     *
     * @since 0.1.0
     *
     * @return 項目名（正規化済み）
     */
    public String getName() {

        final String result = this.name;
        return result;

    }

    /**
     * 持ち越し元の日付を返す<br>
     *
     * @since 0.1.0
     *
     * @return 持ち越し元の日付（yyyy-MM-dd）。日付がない行は null
     */
    public String getOriginDate() {

        final String result = this.originDate;
        return result;

    }

    /**
     * 元の行を返す<br>
     *
     * @since 0.1.0
     *
     * @return 元の行
     */
    public String getRaw() {

        final String result = this.raw;
        return result;

    }

    /**
     * セクション名を返す<br>
     *
     * @since 0.1.0
     *
     * @return セクション名
     */
    public String getSection() {

        final String result = this.section;
        return result;

    }

    /**
     * チェック済みかを返す<br>
     *
     * @since 0.1.0
     *
     * @return true：チェック済み、false：未チェック
     */
    public boolean isChecked() {

        final boolean result = this.checked;
        return result;

    }

    /**
     * 保存済みの持ち越し項目を持ち越し項目に変換する<br>
     *
     * @since 0.1.0
     *
     * @param messageLogic
     *                     メッセージの取得
     *
     * @return 持ち越し項目
     *
     * @throws IllegalArgumentException
     *                                  残り時間の取得元が不明な場合
     */
    public CarryoverItemModel toCarryoverItemModel(final MessageLogic messageLogic) {

        CarryoverItemModel result = null;

        final MinutesSourceTypes minutesSourceType = MinutesSourceTypes.getEnum(this.minutesSource);

        /* 残り時間の取得元が不明か */
        if (minutesSourceType == MinutesSourceTypes.NONE) {

            // 残り時間の取得元が不明な場合

            // 不正な値として例外にする
            final String template = messageLogic.get(Gr002GenMsgTypes.GR002_GEN11102);
            final String message  = String.format(template, this.minutesSource);
            throw new IllegalArgumentException(message);

        }

        result = new CarryoverItemModel(this.name, this.originDate, this.checked, this.minutes, minutesSourceType,
            this.section, this.raw);
        return result;

    }

}
