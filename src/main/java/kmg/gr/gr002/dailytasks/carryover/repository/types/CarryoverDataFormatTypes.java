package kmg.gr.gr002.dailytasks.carryover.repository.types;

import java.util.HashMap;
import java.util.Map;

import kmg.core.infrastructure.cmn.KmgCmnTypes;

/**
 * 持ち越し JSON の出力書式<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings("nls")
public enum CarryoverDataFormatTypes implements KmgCmnTypes<String> {

    /* 定義：開始 */

    /**
     * 指定無し
     *
     * @since 0.1.0
     */
    NONE("指定無し", "None", "指定無し", null),

    /**
     * インデント
     *
     * @since 0.1.0
     */
    INDENT("インデント", "Indent", "JSON 整形のインデント", "  "),

    /**
     * 改行
     *
     * @since 0.1.0
     */
    LINE_FEED("改行", "LineFeed", "JSON の改行", "\n"),

    /**
     * Issue ファイル名の書式
     *
     * @since 0.1.0
     */
    ISSUE_FILE_NAME("Issue ファイル名の書式", "IssueFileName", "Issue 番号を 4 桁でゼロ埋めする書式", "%04d%s"),

    /* 定義：終了 */
    ;

    /**
     * 種類のマップ
     * <p>
     * 列挙定数の名前から定数を引くクラス内の索引。外には渡さない。
     * </p>
     *
     * @since 0.1.0
     */
    private static final Map<String, CarryoverDataFormatTypes> VALUES_MAP = new HashMap<>();

    static {

        /* 種類のマップにプット */
        for (final CarryoverDataFormatTypes type : CarryoverDataFormatTypes.values()) {

            CarryoverDataFormatTypes.VALUES_MAP.put(type.get(), type);

            if (type.getFormat() != null) {

                CarryoverDataFormatTypes.VALUES_MAP.put(type.getFormat(), type);

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
    public static CarryoverDataFormatTypes getDefault() {

        final CarryoverDataFormatTypes result = NONE;
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
    public static CarryoverDataFormatTypes getEnum(final String key) {

        CarryoverDataFormatTypes result = CarryoverDataFormatTypes.VALUES_MAP.get(key);

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
    public static CarryoverDataFormatTypes getInitValue() {

        final CarryoverDataFormatTypes result = NONE;
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
     * 出力書式
     *
     * @since 0.1.0
     */
    private final String format;

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
     * @param format
     *                    出力書式
     */
    CarryoverDataFormatTypes(final String displayName, final String key, final String detail, final String format) {

        this.displayName = displayName;
        this.key = key;
        this.detail = detail;
        this.format = format;

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
     * 出力書式を返す。<br>
     *
     * @since 0.1.0
     *
     * @return 出力書式
     */
    public String getFormat() {

        final String result = this.format;
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
