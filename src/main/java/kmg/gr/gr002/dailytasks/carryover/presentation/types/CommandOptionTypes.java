package kmg.gr.gr002.dailytasks.carryover.presentation.types;

import java.util.HashMap;
import java.util.Map;

import kmg.core.infrastructure.cmn.KmgCmnTypes;

/**
 * 持ち越しの収集コマンドの引数<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings("nls")
public enum CommandOptionTypes implements KmgCmnTypes<String> {

    /* 定義：開始 */

    /** 指定無し */
    NONE("指定無し", "None", "指定無し", null),

    /** 全件モード */
    FULL("全件モード", "Full", "全件モード。#1 から最新まで解析し直す", "--full"),

    /** 使い方の表示 */
    HELP("使い方の表示", "Help", "使い方を表示する", "--help"),

    /** 使い方の表示（短縮形） */
    HELP_SHORT("使い方の表示（短縮形）", "HelpShort", "使い方を表示する短縮形", "-h"),

    /* 定義：終了 */
    ;

    /** 種類のマップ */
    private static final Map<String, CommandOptionTypes> VALUES_MAP = new HashMap<>();

    static {

        /* 種類のマップにプット */
        for (final CommandOptionTypes type : CommandOptionTypes.values()) {

            CommandOptionTypes.VALUES_MAP.put(type.get(), type);

            if (type.getOption() != null) {

                CommandOptionTypes.VALUES_MAP.put(type.getOption(), type);

            }

        }

    }

    /**
     * デフォルトの種類を返す<br>
     *
     * @return デフォルト値
     */
    public static CommandOptionTypes getDefault() {

        final CommandOptionTypes result = NONE;
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
    public static CommandOptionTypes getEnum(final String key) {

        CommandOptionTypes result = CommandOptionTypes.VALUES_MAP.get(key);

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
    public static CommandOptionTypes getInitValue() {

        final CommandOptionTypes result = NONE;
        return result;

    }

    /** 詳細情報 */
    private final String detail;

    /** 表示名 */
    private final String displayName;

    /** キー */
    private final String key;

    /** コマンド引数 */
    private final String option;

    /**
     * コンストラクタ<br>
     *
     * @param displayName
     *                    表示名
     * @param key
     *                    キー
     * @param detail
     *                    詳細情報
     * @param option
     *                    コマンド引数
     */
    CommandOptionTypes(final String displayName, final String key, final String detail, final String option) {

        this.displayName = displayName;
        this.key = key;
        this.detail = detail;
        this.option = option;

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
     * コマンド引数を返す。<br>
     *
     * @return コマンド引数
     */
    public String getOption() {

        final String result = this.option;
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
