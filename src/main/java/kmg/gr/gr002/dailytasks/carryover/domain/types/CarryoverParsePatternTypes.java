package kmg.gr.gr002.dailytasks.carryover.domain.types;

import java.util.HashMap;
import java.util.Map;

import kmg.core.infrastructure.cmn.KmgCmnTypes;

/**
 * 持ち越し解析の正規表現<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings("nls")
public enum CarryoverParsePatternTypes implements KmgCmnTypes<String> {

    /* 定義：開始 */

    /** 指定無し */
    NONE("指定無し", "None", "指定無し", null),

    /** チェックボックスの行 */
    CHECKBOX("チェックボックスの行", "Checkbox", "チェックボックスの行", "^\\s*[-*]\\s*\\[([ xX])\\]\\s*(.*?)\\s*$"),

    /** 持ち越し元の日付を含む内容 */
    DATED_CONTENT("持ち越し元の日付を含む内容", "DatedContent", "持ち越し元の日付を含む内容", "^(.*?)(\\d{4})/(\\d{1,2})/(\\d{1,2})(.*)$"),

    /** 残数 */
    DECLARED_COUNT("残数", "DeclaredCount", "残数", "^\\s*残\\s*[：:]\\s*(\\d+)\\s*$"),

    /** 見出し */
    HEADING("見出し", "Heading", "見出し", "^##(?!#)\\s*(.*?)\\s*$"),

    /** 括弧内の時間表記 */
    PARENTHESES("括弧内の時間表記", "Parentheses", "括弧内の時間表記", "[（(]([^）)]*)[）)]"),

    /** 残り時間の表記 */
    REMAINING_MINUTES("残り時間の表記", "RemainingMinutes", "残り時間の表記", "残り(?:時間)?\\s*[：:]?\\s*(\\d+(?:\\.\\d+)?)\\s*分"),

    /** 時間だけの表記 */
    SIMPLE_MINUTES("時間だけの表記", "SimpleMinutes", "時間だけの表記", "^\\s*(\\d+(?:\\.\\d+)?)\\s*分\\s*$"),

    /** タイトルの日付 */
    TITLE_DATE("タイトルの日付", "TitleDate", "タイトルの日付", "(\\d{4})年(\\d{1,2})月(\\d{1,2})日"),

    /** 項目名の末尾の開き括弧 */
    TRAILING_OPEN_PARENTHESES("項目名の末尾の開き括弧", "TrailingOpenParentheses", "項目名の末尾の開き括弧", "[（(]+$"),

    /** 項目名の末尾の括弧書き */
    TRAILING_PARENTHESES("項目名の末尾の括弧書き", "TrailingParentheses", "項目名の末尾の括弧書き", "[（(][^（()）]*[）)]$"),

    /** 行の分割 */
    LINE_SEPARATOR("行の分割", "LineSeparator", "行の分割", "\\R"),

    /** 区切り線 */
    HORIZONTAL_RULE("区切り線", "HorizontalRule", "区切り線", "^-{3,}$"),

    /* 定義：終了 */
    ;

    /** 種類のマップ */
    private static final Map<String, CarryoverParsePatternTypes> VALUES_MAP = new HashMap<>();

    static {

        /* 種類のマップにプット */
        for (final CarryoverParsePatternTypes type : CarryoverParsePatternTypes.values()) {

            CarryoverParsePatternTypes.VALUES_MAP.put(type.get(), type);

            if (type.getPattern() != null) {

                CarryoverParsePatternTypes.VALUES_MAP.put(type.getPattern(), type);

            }

        }

    }

    /**
     * デフォルトの種類を返す<br>
     *
     * @return デフォルト値
     */
    public static CarryoverParsePatternTypes getDefault() {

        final CarryoverParsePatternTypes result = NONE;
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
    public static CarryoverParsePatternTypes getEnum(final String key) {

        CarryoverParsePatternTypes result = CarryoverParsePatternTypes.VALUES_MAP.get(key);

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
    public static CarryoverParsePatternTypes getInitValue() {

        final CarryoverParsePatternTypes result = NONE;
        return result;

    }

    /** 表示名 */
    private final String displayName;

    /** キー */
    private final String key;

    /** 詳細情報 */
    private final String detail;

    /** 正規表現 */
    private final String pattern;

    /**
     * コンストラクタ<br>
     *
     * @param displayName
     *                    表示名
     * @param key
     *                    キー
     * @param detail
     *                    詳細情報
     * @param pattern
     *                    正規表現
     */
    CarryoverParsePatternTypes(final String displayName, final String key, final String detail, final String pattern) {

        this.displayName = displayName;
        this.key = key;
        this.detail = detail;
        this.pattern = pattern;

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
     * 正規表現を返す。<br>
     *
     * @return 正規表現
     */
    public String getPattern() {

        final String result = this.pattern;
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
