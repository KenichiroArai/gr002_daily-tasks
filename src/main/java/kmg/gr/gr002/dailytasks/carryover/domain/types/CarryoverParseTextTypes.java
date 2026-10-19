package kmg.gr.gr002.dailytasks.carryover.domain.types;

import java.util.HashMap;
import java.util.Map;

import kmg.core.infrastructure.cmn.KmgCmnTypes;

/**
 * 持ち越し解析の固定文字列<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings("nls")
public enum CarryoverParseTextTypes implements KmgCmnTypes<String> {

    /* 定義：開始 */

    /**
     * 指定無し
     *
     * @since 0.1.0
     */
    NONE("指定無し", "None", "指定無し", null),

    /**
     * 空文字
     *
     * @since 0.1.0
     */
    EMPTY("空文字", "Empty", "空文字", ""),

    /**
     * 未チェック
     *
     * @since 0.1.0
     */
    UNCHECKED("未チェック", "Unchecked", "未チェックのチェックボックス", " "),

    /**
     * 不明
     *
     * @since 0.1.0
     */
    UNKNOWN("不明", "Unknown", "項目名が空の場合の名前", "不明"),

    /**
     * 日付の書式
     *
     * @since 0.1.0
     */
    DATE("日付の書式", "Date", "年・月・日を yyyy-MM-dd にする書式", "%04d-%02d-%02d"),

    /* 定義：終了 */
    ;

    /**
     * 種類のマップ
     *
     * @since 0.1.0
     */
    private static final Map<String, CarryoverParseTextTypes> VALUES_MAP = new HashMap<>();

    static {

        /* 種類のマップにプット */
        for (final CarryoverParseTextTypes type : CarryoverParseTextTypes.values()) {

            CarryoverParseTextTypes.VALUES_MAP.put(type.get(), type);

            if (type.getText() != null) {

                CarryoverParseTextTypes.VALUES_MAP.put(type.getText(), type);

            }

        }

    }

    /**
     * デフォルトの種類を返す<br>
     *
     * @since 0.1.0
     *
     * @return デフォルト値
     */
    public static CarryoverParseTextTypes getDefault() {

        final CarryoverParseTextTypes result = NONE;
        return result;

    }

    /**
     * キーに該当する種類を返す<br>
     * <p>
     * 但し、キーが存在しない場合は、指定無し（NONE）を返す。
     * </p>
     *
     * @since 0.1.0
     *
     * @param key
     *            キー
     *
     * @return 種類。指定無し（NONE）：キーが存在しない場合。
     */
    public static CarryoverParseTextTypes getEnum(final String key) {

        CarryoverParseTextTypes result = CarryoverParseTextTypes.VALUES_MAP.get(key);

        if (result == null) {

            result = NONE;

        }
        return result;

    }

    /**
     * 初期値の種類を返す<br>
     *
     * @since 0.1.0
     *
     * @return 初期値
     */
    public static CarryoverParseTextTypes getInitValue() {

        final CarryoverParseTextTypes result = NONE;
        return result;

    }

    /**
     * 表示名
     *
     * @since 0.1.0
     */
    private final String displayName;

    /**
     * キー
     *
     * @since 0.1.0
     */
    private final String key;

    /**
     * 詳細情報
     *
     * @since 0.1.0
     */
    private final String detail;

    /**
     * 解析で使う文字列
     *
     * @since 0.1.0
     */
    private final String text;

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     *
     * @param displayName
     *                    表示名
     * @param key
     *                    キー
     * @param detail
     *                    詳細情報
     * @param text
     *                    解析で使う文字列
     */
    CarryoverParseTextTypes(final String displayName, final String key, final String detail, final String text) {

        this.displayName = displayName;
        this.key = key;
        this.detail = detail;
        this.text = text;

    }

    /**
     * キーを返す。<br>
     *
     * @since 0.1.0
     *
     * @return キー
     *
     * @see #getKey()
     */
    @Override
    public String get() {

        final String result = this.getKey();
        return result;

    }

    /**
     * 詳細情報を返す。<br>
     *
     * @since 0.1.0
     *
     * @return 詳細情報
     */
    @Override
    public String getDetail() {

        final String result = this.detail;
        return result;

    }

    /**
     * 表示名を返す。<br>
     * <p>
     * 識別するための表示名を返す。
     * </p>
     *
     * @since 0.1.0
     *
     * @return 表示名
     */
    @Override
    public String getDisplayName() {

        final String result = this.displayName;
        return result;

    }

    /**
     * 解析で使う文字列を返す。<br>
     *
     * @since 0.1.0
     *
     * @return 解析で使う文字列
     */
    public String getText() {

        final String result = this.text;
        return result;

    }

    /**
     * キーを返す。<br>
     *
     * @since 0.1.0
     *
     * @return キー
     */
    @Override
    public String getKey() {

        final String result = this.key;
        return result;

    }

    /**
     * キーを返す。<br>
     *
     * @since 0.1.0
     *
     * @return キー
     *
     * @see #getKey()
     */
    @Override
    public String toString() {

        final String result = this.getKey();
        return result;

    }

}
