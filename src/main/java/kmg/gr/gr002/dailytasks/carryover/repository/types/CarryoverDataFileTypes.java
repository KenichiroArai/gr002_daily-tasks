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

    /**
     * 指定無し
     *
     * @since 0.1.0
     */
    NONE("指定無し", "None", "指定無し", null),

    /**
     * Issue ごとの JSON を置くディレクトリ名
     *
     * @since 0.1.0
     */
    ISSUES_DIR("Issue ごとの JSON を置くディレクトリ名", "IssuesDir", "Issue ごとの JSON を置くディレクトリ名", "issues"),

    /**
     * JSON ファイルの拡張子
     *
     * @since 0.1.0
     */
    JSON_EXTENSION("JSON ファイルの拡張子", "JsonExtension", "JSON ファイルの拡張子", ".json"),

    /**
     * 画面用の集計の JSON ファイル名
     *
     * @since 0.1.0
     */
    SUMMARY_FILE("画面用の集計の JSON ファイル名", "SummaryFile", "画面用の集計の JSON ファイル名", "summary.json"),

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
    private static final Map<String, CarryoverDataFileTypes> VALUES_MAP = new HashMap<>();

    static {

        /* 種類のマップにプット */
        for (final CarryoverDataFileTypes type : CarryoverDataFileTypes.values()) {

            CarryoverDataFileTypes.VALUES_MAP.put(type.get(), type);

            /* 別名があるか */
            final String fileName = type.getFileName();

            if (fileName != null) {

                // 別名がある場合

                // 別名でも引けるようにする
                CarryoverDataFileTypes.VALUES_MAP.put(fileName, type);

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
     * @since 0.1.0
     *
     * @param key
     *            キー
     *
     * @return 種類。指定無し（NONE）：キーが存在しない場合。
     */
    public static CarryoverDataFileTypes getEnum(final String key) {

        CarryoverDataFileTypes result = CarryoverDataFileTypes.VALUES_MAP.get(key);

        /* キーが存在しないか */
        if (result == null) {

            // キーが存在しない場合

            // 指定無しにする
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
    public static CarryoverDataFileTypes getInitValue() {

        final CarryoverDataFileTypes result = NONE;
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
     * ファイル名
     *
     * @since 0.1.0
     */
    private final String fileName;

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
     * @param fileName
     *                    ファイル名
     */
    CarryoverDataFileTypes(final String displayName, final String key, final String detail, final String fileName) {

        this.displayName = displayName;
        this.key = key;
        this.detail = detail;
        this.fileName = fileName;

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
     * ファイル名を返す。<br>
     *
     * @since 0.1.0
     *
     * @return ファイル名
     */
    public String getFileName() {

        final String result = this.fileName;
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
