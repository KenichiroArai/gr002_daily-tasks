package kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg;

import java.util.HashMap;
import java.util.Map;

import kmg.fund.infrastructure.cmn.msg.KmgFundCmnLogMsgTypes;

/**
 * 持ち越しの収集のログメッセージの種類<br>
 * <p>
 * Msgは、Messageの略。
 * </p>
 * <p>
 * メッセージコードの採番規則:<br>
 * 形式: {@code GR002_LOG} + 5桁。種別はプレフィックスのみで表す。<br>
 * 1桁目（層）: {@code 0} 共通 / {@code 1} domain / {@code 2} infrastructure / {@code 3} application /
 * {@code 4} presentation / {@code 5} repository<br>
 * 2桁目（層内）:<br>
 * domain: {@code 0} 共通 / {@code 1} dto / {@code 2} logic / {@code 3} types<br>
 * infrastructure: {@code 0} 共通 / {@code 1} context / {@code 2} types.msg<br>
 * application: {@code 0} 共通 / {@code 1} service / {@code 2} logic / {@code 3} model / {@code 4} types<br>
 * presentation: {@code 0} 共通 / {@code 1} command / {@code 2} model / {@code 3} service / {@code 4} types<br>
 * repository: {@code 0} 共通 / {@code 1} api.github / {@code 2} dao.carryoverdata / {@code 3} dao.defaultminutes /
 * {@code 4} dto.json / {@code 5} types<br>
 * 3〜5桁目: 区分内の連番 {@code 000}〜{@code 999}<br>
 * 番号の帰属は呼び出し側の実装パッケージ。ログは出力箇所ごとに固有 ID。空きにした旧番号は再利用しない。
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

    /* domain.logic */

    /**
     * タイトルの日付がない
     *
     * @since 0.1.0
     */
    GR002_LOG12000("タイトルの日付がない", "Gr002Log12000", "タイトルから日付を取得できない場合のログ", "#{} タイトルから日付を取得できません: {}"),

    /**
     * 持ち越し元の日付がない
     *
     * @since 0.1.0
     */
    GR002_LOG12001("持ち越し元の日付がない", "Gr002Log12001", "持ち越し元の日付がない場合のログ", "#{} 持ち越し元の日付がありません: {}"),

    /**
     * 項目名がない
     *
     * @since 0.1.0
     */
    GR002_LOG12002("項目名がない", "Gr002Log12002", "項目名を取得できない場合のログ", "#{} 項目名を取得できません: {}"),

    /**
     * 時間表記を解釈できない
     *
     * @since 0.1.0
     */
    GR002_LOG12003("時間表記を解釈できない", "Gr002Log12003", "時間表記を解釈できず標準時間で補完する場合のログ", "#{} 時間表記を解釈できないため標準時間で補完します: {}"),

    /**
     * 標準時間が未登録
     *
     * @since 0.1.0
     */
    GR002_LOG12004("標準時間が未登録", "Gr002Log12004", "標準時間が未登録のため 0 分とする場合のログ", "#{} 標準時間が未登録のため 0 分とします: {}"),

    /**
     * 想定外の行
     *
     * @since 0.1.0
     */
    GR002_LOG12005("想定外の行", "Gr002Log12005", "想定外の行がある場合のログ", "#{} 想定外の行です: {}"),

    /**
     * 残数と解析件数の食い違い
     *
     * @since 0.1.0
     */
    GR002_LOG12006("残数と解析件数の食い違い", "Gr002Log12006", "残数と解析件数が一致しない場合のログ", "#{} 残数と解析件数が一致しません: 残={}, 解析={}"),

    /**
     * Issue の解析完了
     *
     * @since 0.1.0
     */
    GR002_LOG12007("Issue の解析完了", "Gr002Log12007", "Issue の解析完了を出すログ", "#{} を解析しました: 項目={} 件"),

    /**
     * 集計完了
     *
     * @since 0.1.0
     */
    GR002_LOG12008("集計完了", "Gr002Log12008", "集計完了を出すログ", "集計しました: 日数={}、最新Issue={}、項目={}"),

    /* application.service */

    /**
     * 収集結果（差分モード）
     *
     * @since 0.1.0
     */
    GR002_LOG31000("収集結果（差分モード）", "Gr002Log31000", "差分モードの収集の件数を出すログ", "モード={}、取得={} 件、解析・保存={} 件、集計={} 日"),

    /**
     * 標準時間の補完結果（差分モード）
     *
     * @since 0.1.0
     */
    GR002_LOG31001("標準時間の補完結果（差分モード）", "Gr002Log31001", "差分モードの残り時間の取得元ごとの行数を出すログ",
        "標準時間で補完した行={} 件、標準時間が未登録の行={} 件"),

    /**
     * 収集結果（全件モード）
     *
     * @since 0.1.0
     */
    GR002_LOG31002("収集結果（全件モード）", "Gr002Log31002", "全件モードの収集の件数を出すログ", "モード={}、取得={} 件、解析・保存={} 件、集計={} 日"),

    /**
     * 標準時間の補完結果（全件モード）
     *
     * @since 0.1.0
     */
    GR002_LOG31003("標準時間の補完結果（全件モード）", "Gr002Log31003", "全件モードの残り時間の取得元ごとの行数を出すログ",
        "標準時間で補完した行={} 件、標準時間が未登録の行={} 件"),

    /**
     * 参照取得結果
     *
     * @since 0.1.0
     */
    GR002_LOG31004("参照取得結果", "Gr002Log31004", "参照とバッチの件数を出すログ", "参照を取得しました: 参照={} 件、バッチ={} 件"),

    /**
     * バッチ開始
     *
     * @since 0.1.0
     */
    GR002_LOG31005("バッチ開始", "Gr002Log31005", "バッチ開始を出すログ", "バッチを開始します: {}/{}、対象={} 件"),

    /**
     * バッチ完了
     *
     * @since 0.1.0
     */
    GR002_LOG31006("バッチ完了", "Gr002Log31006", "バッチ完了を出すログ", "バッチが完了しました: {}/{}、再解析={} 件、再利用={} 件"),

    /**
     * 全件
     *
     * @since 0.1.0
     */
    GR002_LOG31007("全件", "Gr002Log31007", "全件モードの名前", "全件"),

    /**
     * 差分
     *
     * @since 0.1.0
     */
    GR002_LOG31008("差分", "Gr002Log31008", "差分モードの名前", "差分"),

    /**
     * Issue の再解析（全件モード）
     *
     * @since 0.1.0
     */
    GR002_LOG31009("Issue の再解析（全件モード）", "Gr002Log31009", "全件モードのため Issue を再解析するログ", "#{} を再解析します: {}"),

    /**
     * Issue のスキップ
     *
     * @since 0.1.0
     */
    GR002_LOG31010("Issue のスキップ", "Gr002Log31010", "Issue をスキップする理由を出すログ", "#{} をスキップします: {}"),

    /**
     * 再解析理由（全件モード）
     *
     * @since 0.1.0
     */
    GR002_LOG31011("再解析理由（全件モード）", "Gr002Log31011", "全件モードのため再解析する理由", "全件モード"),

    /**
     * 再解析理由（未保存）
     *
     * @since 0.1.0
     */
    GR002_LOG31012("再解析理由（未保存）", "Gr002Log31012", "未保存のため再解析する理由", "未保存"),

    /**
     * 再解析理由（直近）
     *
     * @since 0.1.0
     */
    GR002_LOG31013("再解析理由（直近）", "Gr002Log31013", "直近のため再解析する理由", "直近"),

    /**
     * 再解析理由（更新日時変更）
     *
     * @since 0.1.0
     */
    GR002_LOG31014("再解析理由（更新日時変更）", "Gr002Log31014", "更新日時が変わったため再解析する理由", "更新日時変更"),

    /**
     * スキップ理由（更新日時一致）
     *
     * @since 0.1.0
     */
    GR002_LOG31015("スキップ理由（更新日時一致）", "Gr002Log31015", "更新日時が同じためスキップする理由", "更新日時一致"),

    /**
     * Issue の再解析（未保存）
     *
     * @since 0.1.0
     */
    GR002_LOG31016("Issue の再解析（未保存）", "Gr002Log31016", "未保存のため Issue を再解析するログ", "#{} を再解析します: {}"),

    /**
     * Issue の再解析（直近）
     *
     * @since 0.1.0
     */
    GR002_LOG31017("Issue の再解析（直近）", "Gr002Log31017", "直近のため Issue を再解析するログ", "#{} を再解析します: {}"),

    /**
     * Issue の再解析（更新日時変更）
     *
     * @since 0.1.0
     */
    GR002_LOG31018("Issue の再解析（更新日時変更）", "Gr002Log31018", "更新日時が変わったため Issue を再解析するログ", "#{} を再解析します: {}"),

    /* presentation.command */

    /**
     * 収集開始
     *
     * @since 0.1.0
     */
    GR002_LOG41000("収集開始", "Gr002Log41000", "収集開始時の設定を出すログ",
        "収集を開始します: モード={}、repository={}、dataDir={}、recentCount={}、batchSize={}"),

    /**
     * 収集完了
     *
     * @since 0.1.0
     */
    GR002_LOG41001("収集完了", "Gr002Log41001", "収集完了を出すログ", "収集が完了しました"),

    /**
     * 収集失敗（引数例外）
     *
     * @since 0.1.0
     */
    GR002_LOG41002("収集失敗（引数例外）", "Gr002Log41002", "引数例外による収集失敗を出すログ", "収集に失敗しました: {}"),

    /**
     * 収集失敗（入出力例外）
     *
     * @since 0.1.0
     */
    GR002_LOG41003("収集失敗（入出力例外）", "Gr002Log41003", "入出力例外による収集失敗を出すログ", "収集に失敗しました: {}"),

    /* repository.api.github */

    /**
     * Issue の取得件数（全件）
     *
     * @since 0.1.0
     */
    GR002_LOG51000("Issue の取得件数（全件）", "Gr002Log51000", "全件取得時の Issue の件数を出すログ", "Issue を {} 件取得しました（{} ページ）"),

    /**
     * Issue ページの取得（全件）
     *
     * @since 0.1.0
     */
    GR002_LOG51001("Issue ページの取得（全件）", "Gr002Log51001", "全件取得時の Issue ページの取得を出すログ",
        "Issue ページを取得しました: ページ={}、応答={} 件、累積={} 件"),

    /**
     * レート制限
     *
     * @since 0.1.0
     */
    GR002_LOG51002("レート制限", "Gr002Log51002", "レート制限の残数を出すログ", "レート制限: remaining={}、reset={}"),

    /**
     * レート制限の残りが少ない
     *
     * @since 0.1.0
     */
    GR002_LOG51003("レート制限の残りが少ない", "Gr002Log51003", "レート制限の残りが少ない場合のログ", "レート制限の残りが少なくなっています: remaining={}、reset={}"),

    /**
     * Issue ページの取得（最新件数）
     *
     * @since 0.1.0
     */
    GR002_LOG51004("Issue ページの取得（最新件数）", "Gr002Log51004", "最新件数取得時の Issue ページの取得を出すログ",
        "Issue ページを取得しました: ページ={}、応答={} 件、累積={} 件"),

    /**
     * Issue の取得件数（最新件数）
     *
     * @since 0.1.0
     */
    GR002_LOG51005("Issue の取得件数（最新件数）", "Gr002Log51005", "最新件数取得時の Issue の件数を出すログ", "Issue を {} 件取得しました（{} ページ）"),

    /* repository.dao.carryoverdata */

    /**
     * Issue JSON の読込（単件）
     *
     * @since 0.1.0
     */
    GR002_LOG52000("Issue JSON の読込（単件）", "Gr002Log52000", "単件読込時の Issue JSON の読込を出すログ", "Issue JSON を読み込みます: {}"),

    /**
     * Issue JSON の未保存
     *
     * @since 0.1.0
     */
    GR002_LOG52001("Issue JSON の未保存", "Gr002Log52001", "Issue JSON が未保存の場合のログ", "Issue JSON が未保存です: #{}"),

    /**
     * Issue JSON の保存
     *
     * @since 0.1.0
     */
    GR002_LOG52002("Issue JSON の保存", "Gr002Log52002", "Issue JSON の保存を出すログ", "Issue JSON を保存します: {}"),

    /**
     * summary.json の保存
     *
     * @since 0.1.0
     */
    GR002_LOG52003("summary.json の保存", "Gr002Log52003", "summary.json の保存を出すログ", "summary.json を保存します: {}"),

    /**
     * Issue JSON の読込（一括）
     *
     * @since 0.1.0
     */
    GR002_LOG52004("Issue JSON の読込（一括）", "Gr002Log52004", "一括読込時の Issue JSON の読込を出すログ", "Issue JSON を読み込みます: {}"),

    /* repository.dao.defaultminutes */

    /**
     * 標準時間の設定ファイルがない
     *
     * @since 0.1.0
     */
    GR002_LOG53000("標準時間の設定ファイルがない", "Gr002Log53000", "標準時間の設定ファイルがない場合のログ", "標準時間の設定ファイルがありません: {}"),

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
