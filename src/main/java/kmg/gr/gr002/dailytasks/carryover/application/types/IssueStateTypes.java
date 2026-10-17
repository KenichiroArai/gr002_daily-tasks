package kmg.gr.gr002.dailytasks.carryover.application.types;

import java.util.HashMap;
import java.util.Map;

import kmg.core.infrastructure.cmn.KmgCmnTypes;

/**
 * Issue の状態<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings("nls")
public enum IssueStateTypes implements KmgCmnTypes<String> {

    /* 定義：開始 */

    /** 指定無し */
    NONE("指定無し", "None", "指定無し", null),

    /** クローズ済み */
    CLOSED("クローズ済み", "Closed", "クローズ済み", "closed"),

    /** オープン中 */
    OPEN("オープン中", "Open", "オープン中", "open"),

    /* 定義：終了 */
    ;

    /** 種類のマップ */
    private static final Map<String, IssueStateTypes> VALUES_MAP = new HashMap<>();

    static {

        /* 種類のマップにプット */
        for (final IssueStateTypes type : IssueStateTypes.values()) {

            IssueStateTypes.VALUES_MAP.put(type.get(), type);

            if (type.getState() != null) {

                IssueStateTypes.VALUES_MAP.put(type.getState(), type);

            }

        }

    }

    /**
     * デフォルトの種類を返す<br>
     *
     * @return デフォルト値
     */
    public static IssueStateTypes getDefault() {

        final IssueStateTypes result = NONE;
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
    public static IssueStateTypes getEnum(final String key) {

        IssueStateTypes result = IssueStateTypes.VALUES_MAP.get(key);

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
    public static IssueStateTypes getInitValue() {

        final IssueStateTypes result = NONE;
        return result;

    }

    /** 表示名 */
    private final String displayName;

    /** キー */
    private final String key;

    /** 詳細情報 */
    private final String detail;

    /** 状態 */
    private final String state;

    /**
     * コンストラクタ<br>
     *
     * @param displayName
     *                    表示名
     * @param key
     *                    キー
     * @param detail
     *                    詳細情報
     * @param state
     *                    状態
     */
    IssueStateTypes(final String displayName, final String key, final String detail, final String state) {

        this.displayName = displayName;
        this.key = key;
        this.detail = detail;
        this.state = state;

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
     * 状態を返す。<br>
     *
     * @return 状態
     */
    public String getState() {

        final String result = this.state;
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
