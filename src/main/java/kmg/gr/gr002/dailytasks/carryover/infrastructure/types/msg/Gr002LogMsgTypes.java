package kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg;

import java.util.HashMap;
import java.util.Map;

import kmg.fund.infrastructure.cmn.msg.KmgFundCmnLogMsgTypes;

/**
 * 持ち越しの収集のログメッセージの種類<br>
 * <p>
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
public enum Gr002LogMsgTypes implements KmgFundCmnLogMsgTypes {

    /* 定義：開始 */

    /**
     * 指定無し
     *
     * @since 0.1.0
     */
    NONE("指定無し", "None", "指定無し", "指定無し"),

    /**
     * 収集結果
     *
     * @since 0.1.0
     */
    GR002_LOG12000("収集結果", "Gr002Log12000", "収集の件数を出すログ", "モード={}、取得={} 件、解析・保存={} 件、集計={} 日"),

    /**
     * 標準時間の補完結果
     *
     * @since 0.1.0
     */
    GR002_LOG12001("標準時間の補完結果", "Gr002Log12001", "残り時間の取得元ごとの行数を出すログ", "標準時間で補完した行={} 件、標準時間が未登録の行={} 件"),

    /**
     * タイトルの日付がない
     *
     * @since 0.1.0
     */
    GR002_LOG12002("タイトルの日付がない", "Gr002Log12002", "タイトルから日付を取得できない場合のログ", "#{} タイトルから日付を取得できません: {}"),

    /**
     * 持ち越し元の日付がない
     *
     * @since 0.1.0
     */
    GR002_LOG12003("持ち越し元の日付がない", "Gr002Log12003", "持ち越し元の日付がない場合のログ", "#{} 持ち越し元の日付がありません: {}"),

    /**
     * 項目名がない
     *
     * @since 0.1.0
     */
    GR002_LOG12004("項目名がない", "Gr002Log12004", "項目名を取得できない場合のログ", "#{} 項目名を取得できません: {}"),

    /**
     * 時間表記を解釈できない
     *
     * @since 0.1.0
     */
    GR002_LOG12005("時間表記を解釈できない", "Gr002Log12005", "時間表記を解釈できず標準時間で補完する場合のログ", "#{} 時間表記を解釈できないため標準時間で補完します: {}"),

    /**
     * 標準時間が未登録
     *
     * @since 0.1.0
     */
    GR002_LOG12006("標準時間が未登録", "Gr002Log12006", "標準時間が未登録のため 0 分とする場合のログ", "#{} 標準時間が未登録のため 0 分とします: {}"),

    /**
     * 想定外の行
     *
     * @since 0.1.0
     */
    GR002_LOG12007("想定外の行", "Gr002Log12007", "想定外の行がある場合のログ", "#{} 想定外の行です: {}"),

    /**
     * 残数と解析件数の食い違い
     *
     * @since 0.1.0
     */
    GR002_LOG12008("残数と解析件数の食い違い", "Gr002Log12008", "残数と解析件数が一致しない場合のログ", "#{} 残数と解析件数が一致しません: 残={}, 解析={}"),

    /**
     * 標準時間の設定ファイルがない
     *
     * @since 0.1.0
     */
    GR002_LOG12009("標準時間の設定ファイルがない", "Gr002Log12009", "標準時間の設定ファイルがない場合のログ", "標準時間の設定ファイルがありません: {}"),

    /**
     * Issue の取得件数
     *
     * @since 0.1.0
     */
    GR002_LOG12010("Issue の取得件数", "Gr002Log12010", "取得した Issue の件数を出すログ", "Issue を {} 件取得しました（{} ページ）"),

    /**
     * 全件
     *
     * @since 0.1.0
     */
    GR002_LOG12011("全件", "Gr002Log12011", "全件モードの名前", "全件"),

    /**
     * 差分
     *
     * @since 0.1.0
     */
    GR002_LOG12012("差分", "Gr002Log12012", "差分モードの名前", "差分"),

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
    private static final Map<String, Gr002LogMsgTypes> VALUES_MAP = new HashMap<>();

    static {

        /* 種類のマップにプット */
        for (final Gr002LogMsgTypes type : Gr002LogMsgTypes.values()) {

            Gr002LogMsgTypes.VALUES_MAP.put(type.get(), type);

        }

    }

    /**
     * デフォルトの種類を返す<br>
     *
     * @since 0.1.0
     *
     * @return デフォルト値
     */
    public static Gr002LogMsgTypes getDefault() {

        final Gr002LogMsgTypes result = NONE;
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
    public static Gr002LogMsgTypes getEnum(final String key) {

        Gr002LogMsgTypes result = Gr002LogMsgTypes.VALUES_MAP.get(key);

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
    public static Gr002LogMsgTypes getInitValue() {

        final Gr002LogMsgTypes result = NONE;
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
    Gr002LogMsgTypes(final String displayName, final String key, final String detail, final String value) {

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
