package kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg;

import java.util.HashMap;
import java.util.Map;

import kmg.fund.infrastructure.cmn.msg.KmgFundCmnExcMsgTypes;
import kmg.fund.infrastructure.cmn.msg.KmgFundCmnValMsgTypes;

/**
 * 持ち越しの収集のバリデーションメッセージの種類<br>
 * <p>
 * Valは、Validationの略。<br>
 * Msgは、Messageの略。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings("nls")
public enum Gr002ValMsgTypes implements KmgFundCmnValMsgTypes, KmgFundCmnExcMsgTypes {

    /* 定義：開始 */

    /**
     * 指定無し
     *
     * @since 0.1.0
     */
    NONE("指定無し", "None", "指定無し", "指定無し"),

    /* 定義：終了 */
    ;

    /**
     * 種類のマップ
     *
     * @since 0.1.0
     */
    private static final Map<String, Gr002ValMsgTypes> VALUES_MAP = new HashMap<>();

    static {

        /* 種類のマップにプット */
        for (final Gr002ValMsgTypes type : Gr002ValMsgTypes.values()) {

            Gr002ValMsgTypes.VALUES_MAP.put(type.get(), type);

        }

    }

    /**
     * デフォルトの種類を返す<br>
     *
     * @since 0.1.0
     *
     * @return デフォルト値
     */
    public static Gr002ValMsgTypes getDefault() {

        final Gr002ValMsgTypes result = NONE;
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
    public static Gr002ValMsgTypes getEnum(final String key) {

        Gr002ValMsgTypes result = Gr002ValMsgTypes.VALUES_MAP.get(key);

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
    public static Gr002ValMsgTypes getInitValue() {

        final Gr002ValMsgTypes result = NONE;
        return result;

    }

    /**
     * 詳細情報
     *
     * @since 0.1.0
     */
    private final String detail;

    /**
     * 表示名
     *
     * @since 0.1.0
     */
    private final String displayName;

    /**
     * メッセージのキー
     *
     * @since 0.1.0
     */
    private final String key;

    /**
     * メッセージの値
     *
     * @since 0.1.0
     */
    private final String value;

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
     * @param value
     *                    メッセージの値
     */
    Gr002ValMsgTypes(final String displayName, final String key, final String detail, final String value) {

        this.displayName = displayName;
        this.key = key;
        this.detail = detail;
        this.value = value;

    }

    /**
     * メッセージのキーを返す。<br>
     *
     * @since 0.1.0
     *
     * @return メッセージのキー
     *
     * @see #getKey()
     */
    @Override
    public String get() {

        final String result = this.getKey();
        return result;

    }

    /**
     * メッセージのキーを返す。<br>
     *
     * @since 0.1.0
     *
     * @return メッセージのキー
     *
     * @see #getKey()
     */
    @Override
    public String getCode() {

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
     * メッセージのキーを返す。<br>
     *
     * @since 0.1.0
     *
     * @return メッセージのキー
     */
    @Override
    public String getKey() {

        final String result = this.key;
        return result;

    }

    /**
     * メッセージの値を返す。
     *
     * @since 0.1.0
     *
     * @return メッセージの値
     */
    @Override
    public String getValue() {

        final String result = this.value;
        return result;

    }

    /**
     * メッセージのキーを返す。<br>
     *
     * @since 0.1.0
     *
     * @return メッセージのキー
     *
     * @see #getKey()
     */
    @Override
    public String toString() {

        final String result = this.getKey();
        return result;

    }

}
