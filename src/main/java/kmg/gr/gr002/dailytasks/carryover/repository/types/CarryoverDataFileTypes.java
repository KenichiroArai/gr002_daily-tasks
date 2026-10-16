package kmg.gr.gr002.dailytasks.carryover.repository.types;

import java.util.HashMap;
import java.util.Map;

import kmg.core.infrastructure.cmn.KmgCmnTypes;

/**
 * 持ち越しの JSON ファイルの名前<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings("nls")
public enum CarryoverDataFileTypes implements KmgCmnTypes<String> {

    /* 定義：開始 */

    /** 指定無し */
    NONE("指定無し", "None", "指定無し"),

    /** Issue ごとの JSON を置くディレクトリ名 */
    ISSUES_DIR("Issue ごとの JSON を置くディレクトリ名", "issues", "Issue ごとの JSON を置くディレクトリ名"),

    /** JSON ファイルの拡張子 */
    JSON_EXTENSION("JSON ファイルの拡張子", ".json", "JSON ファイルの拡張子"),

    /** 画面用の集計の JSON ファイル名 */
    SUMMARY_FILE("画面用の集計の JSON ファイル名", "summary.json", "画面用の集計の JSON ファイル名"),

    /* 定義：終了 */
    ;

    /** 種類のマップ */
    private static final Map<String, CarryoverDataFileTypes> VALUES_MAP = new HashMap<>();

    static {

        /* 種類のマップにプット */
        for (final CarryoverDataFileTypes type : CarryoverDataFileTypes.values()) {

            CarryoverDataFileTypes.VALUES_MAP.put(type.get(), type);

        }

    }

    /**
     * デフォルトの種類を返す<br>
     *
     * @return デフォルト値
     */
    public static CarryoverDataFileTypes getDefault() {

        final CarryoverDataFileTypes result = NONE;
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
    public static CarryoverDataFileTypes getEnum(final String key) {

        CarryoverDataFileTypes result = CarryoverDataFileTypes.VALUES_MAP.get(key);

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
    public static CarryoverDataFileTypes getInitValue() {

        final CarryoverDataFileTypes result = NONE;
        return result;

    }

    /** 表示名 */
    private final String displayName;

    /** キー */
    private final String key;

    /** 詳細情報 */
    private final String detail;

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
    CarryoverDataFileTypes(final String displayName, final String key, final String detail) {

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
