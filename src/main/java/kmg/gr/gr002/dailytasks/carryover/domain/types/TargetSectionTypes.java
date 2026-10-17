package kmg.gr.gr002.dailytasks.carryover.domain.types;

import java.util.HashMap;
import java.util.Map;

import kmg.core.infrastructure.cmn.KmgCmnTypes;

/**
 * 持ち越しの対象セクション<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings("nls")
public enum TargetSectionTypes implements KmgCmnTypes<String> {

    /* 定義：開始 */

    /** 指定無し */
    NONE("指定無し", "None", "指定無し", null),

    /** 持ち越し */
    CARRYOVER("持ち越し", "Carryover", "対象セクション", "持ち越し"),

    /** 繰り越し */
    CARRYOVER_LEGACY("繰り越し", "CarryoverLegacy", "旧名称の対象セクション", "繰り越し"),

    /** 負債 */
    DEBT("負債", "Debt", "旧名称の対象セクション", "負債"),

    /* 定義：終了 */
    ;

    /** 種類のマップ */
    private static final Map<String, TargetSectionTypes> VALUES_MAP = new HashMap<>();

    static {

        /* 種類のマップにプット */
        for (final TargetSectionTypes type : TargetSectionTypes.values()) {

            TargetSectionTypes.VALUES_MAP.put(type.get(), type);

            if (type.getSection() != null) {

                TargetSectionTypes.VALUES_MAP.put(type.getSection(), type);

            }

        }

    }

    /**
     * デフォルトの種類を返す<br>
     *
     * @return デフォルト値
     */
    public static TargetSectionTypes getDefault() {

        final TargetSectionTypes result = NONE;
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
    public static TargetSectionTypes getEnum(final String key) {

        TargetSectionTypes result = TargetSectionTypes.VALUES_MAP.get(key);

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
    public static TargetSectionTypes getInitValue() {

        final TargetSectionTypes result = NONE;
        return result;

    }

    /** 詳細情報 */
    private final String detail;

    /** 表示名 */
    private final String displayName;

    /** キー */
    private final String key;

    /** セクション名 */
    private final String section;

    /**
     * コンストラクタ<br>
     *
     * @param displayName
     *                    表示名
     * @param key
     *                    キー
     * @param detail
     *                    詳細情報
     * @param section
     *                    セクション名
     */
    TargetSectionTypes(final String displayName, final String key, final String detail, final String section) {

        this.displayName = displayName;
        this.key = key;
        this.detail = detail;
        this.section = section;

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
     * セクション名を返す。<br>
     *
     * @return セクション名
     */
    public String getSection() {

        final String result = this.section;
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
