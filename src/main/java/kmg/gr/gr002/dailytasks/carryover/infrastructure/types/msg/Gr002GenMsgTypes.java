package kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg;

import java.util.HashMap;
import java.util.Map;

import kmg.fund.infrastructure.cmn.msg.KmgFundCmnExcMsgTypes;
import kmg.fund.infrastructure.cmn.msg.KmgFundCmnGenMsgTypes;

/**
 * 持ち越しの収集の一般メッセージの種類<br>
 * <p>
 * Genは、Generalの略。<br>
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
public enum Gr002GenMsgTypes implements KmgFundCmnGenMsgTypes, KmgFundCmnExcMsgTypes {

    /* 定義：開始 */

    /**
     * 指定無し
     *
     * @since 0.1.0
     */
    NONE("指定無し", "None", "指定無し", "指定無し"),

    /**
     * 使い方
     *
     * @since 0.1.0
     */
    GR002_GEN11100("使い方", "Gr002Gen11100", "コマンドの使い方", """
        使い方: java -jar gr002_daily-tasks-0.1.0.jar [--full] [--help]
          （引数なし）  差分モード。最新 10 件の Issue は毎回解析し直し、それ以外は保存済みの JSON と更新日時が同じなら解析しない
          --full        全件モード。#1 から最新まで解析し直す
          --help, -h    この使い方を表示する
        環境変数 GITHUB_TOKEN が設定されている場合は GitHub API の認証に使う"""),

    /**
     * 不明な引数
     *
     * @since 0.1.0
     */
    GR002_GEN11101("不明な引数", "Gr002Gen11101", "不明な引数が指定された場合", "不明な引数です: %s"),

    /**
     * 不明な残り時間の取得元
     *
     * @since 0.1.0
     */
    GR002_GEN11102("不明な残り時間の取得元", "Gr002Gen11102", "残り時間の取得元が不明な場合", "不明な残り時間の取得元です: %s"),

    /**
     * GitHub API の呼び出し失敗
     *
     * @since 0.1.0
     */
    GR002_GEN11103("GitHub API の呼び出し失敗", "Gr002Gen11103", "GitHub API の呼び出しに失敗した場合",
        "GitHub API の呼び出しに失敗しました: status=%d, uri=%s"),

    /**
     * GitHub API の応答が配列ではない
     *
     * @since 0.1.0
     */
    GR002_GEN11104("GitHub API の応答が配列ではない", "Gr002Gen11104", "GitHub API の応答が配列ではない場合",
        "GitHub API の応答が配列ではありません: uri=%s"),

    /**
     * GitHub API の中断
     *
     * @since 0.1.0
     */
    GR002_GEN11105("GitHub API の中断", "Gr002Gen11105", "GitHub API の呼び出しが中断された場合", "GitHub API の呼び出しが中断されました"),

    /* 定義：終了 */
    ;

    /**
     * 種類のマップ
     *
     * @since 0.1.0
     */
    private static final Map<String, Gr002GenMsgTypes> VALUES_MAP = new HashMap<>();

    static {

        /* 種類のマップにプット */
        for (final Gr002GenMsgTypes type : Gr002GenMsgTypes.values()) {

            Gr002GenMsgTypes.VALUES_MAP.put(type.get(), type);

        }

    }

    /**
     * デフォルトの種類を返す<br>
     *
     * @since 0.1.0
     *
     * @return デフォルト値
     */
    public static Gr002GenMsgTypes getDefault() {

        final Gr002GenMsgTypes result = NONE;
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
    public static Gr002GenMsgTypes getEnum(final String key) {

        Gr002GenMsgTypes result = Gr002GenMsgTypes.VALUES_MAP.get(key);

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
    public static Gr002GenMsgTypes getInitValue() {

        final Gr002GenMsgTypes result = NONE;
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
    Gr002GenMsgTypes(final String displayName, final String key, final String detail, final String value) {

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
