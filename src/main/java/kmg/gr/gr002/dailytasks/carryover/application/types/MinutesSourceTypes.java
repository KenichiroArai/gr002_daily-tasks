package kmg.gr.gr002.dailytasks.carryover.application.types;

import java.util.HashMap;
import java.util.Map;

import kmg.core.infrastructure.cmn.KmgCmnTypes;

/**
 * 残り時間の取得元<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings("nls")
public enum MinutesSourceTypes implements KmgCmnTypes<String> {

    /* 定義：開始 */

    /** 項目ごとの標準時間で補完 */
    DEFAULT("項目ごとの標準時間で補完", "default", "項目ごとの標準時間で補完"),

    /** 指定無し */
    NONE("指定無し", "None", "指定無し"),

    /** Issue 本文の時間表記から取得 */
    PARSED("Issue 本文の時間表記から取得", "parsed", "Issue 本文の時間表記から取得"),

    /** 標準時間が未登録のため 0 分で補完 */
    UNKNOWN("標準時間が未登録のため 0 分で補完", "unknown", "標準時間が未登録のため 0 分で補完"),

    /* 定義：終了 */
    ;

    /** 種類のマップ */
    private static final Map<String, MinutesSourceTypes> VALUES_MAP = new HashMap<>();

    static {

        /* 種類のマップにプット */
        for (final MinutesSourceTypes type : MinutesSourceTypes.values()) {

            MinutesSourceTypes.VALUES_MAP.put(type.get(), type);

        }

    }

    /**
     * デフォルトの種類を返す<br>
     *
     * @return デフォルト値
     */
    public static MinutesSourceTypes getDefault() {

        final MinutesSourceTypes result = NONE;
        return result;

    }

    /**
     * キーに該当する種類を返す<br>
     * <p>
     * 但し、キーが存在しない場合は、指定無し（NONE）を返す。
     * </p>
     *
     * @param key
     *            キー
     *
     * @return 種類。指定無し（NONE）：キーが存在しない場合。
     */
    public static MinutesSourceTypes getEnum(final String key) {

        MinutesSourceTypes result = MinutesSourceTypes.VALUES_MAP.get(key);

        if (result == null) {

            result = NONE;

        }
        return result;

    }

    /**
     * 初期値の種類を返す<br>
     *
     * @return 初期値
     */
    public static MinutesSourceTypes getInitValue() {

        final MinutesSourceTypes result = NONE;
        return result;

    }

    /** 詳細情報 */
    private final String detail;

    /** 表示名 */
    private final String displayName;

    /** キー */
    private final String key;

    /**
     * コンストラクタ<br>
     *
     * @param displayName
     *                    表示名
     * @param key
     *                    キー
     * @param detail
     *                    詳細情報
     */
    MinutesSourceTypes(final String displayName, final String key, final String detail) {

        this.displayName = displayName;
        this.key = key;
        this.detail = detail;

    }

    /**
     * キーを返す。<br>
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
     * @return 表示名
     */
    @Override
    public String getDisplayName() {

        final String result = this.displayName;
        return result;

    }

    /**
     * キーを返す。<br>
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
