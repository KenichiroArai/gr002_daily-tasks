package kmg.gr.gr002.dailytasks.carryover.repository.types;

import java.util.HashMap;
import java.util.Map;

import kmg.core.infrastructure.cmn.KmgCmnTypes;

/**
 * GitHub API の URL<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings("nls")
public enum GitHubApiUrlTypes implements KmgCmnTypes<String> {

    /* 定義：開始 */

    /** 指定無し */
    NONE("指定無し", "None", "指定無し", null),

    /** ベース URL */
    BASE_URL("ベース URL", "BaseUrl", "GitHub API のベース URL", "https://api.github.com"),

    /** Issue 一覧の書式 */
    ISSUES_QUERY("Issue 一覧の書式", "IssuesQuery", "Issue 一覧を取得する URL の書式",
        "%s/repos/%s/issues?state=all&sort=created&direction=asc&per_page=%d&page=%d"),

    /* 定義：終了 */
    ;

    /** 種類のマップ */
    private static final Map<String, GitHubApiUrlTypes> VALUES_MAP = new HashMap<>();

    static {

        /* 種類のマップにプット */
        for (final GitHubApiUrlTypes type : GitHubApiUrlTypes.values()) {

            GitHubApiUrlTypes.VALUES_MAP.put(type.get(), type);

            if (type.getUrl() != null) {

                GitHubApiUrlTypes.VALUES_MAP.put(type.getUrl(), type);

            }

        }

    }

    /**
     * デフォルトの種類を返す<br>
     *
     * @return デフォルト値
     */
    public static GitHubApiUrlTypes getDefault() {

        final GitHubApiUrlTypes result = NONE;
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
    public static GitHubApiUrlTypes getEnum(final String key) {

        GitHubApiUrlTypes result = GitHubApiUrlTypes.VALUES_MAP.get(key);

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
    public static GitHubApiUrlTypes getInitValue() {

        final GitHubApiUrlTypes result = NONE;
        return result;

    }

    /** 表示名 */
    private final String displayName;

    /** キー */
    private final String key;

    /** 詳細情報 */
    private final String detail;

    /** URL */
    private final String url;

    /**
     * コンストラクタ<br>
     *
     * @param displayName
     *                    表示名
     * @param key
     *                    キー
     * @param detail
     *                    詳細情報
     * @param url
     *                    URL
     */
    GitHubApiUrlTypes(final String displayName, final String key, final String detail, final String url) {

        this.displayName = displayName;
        this.key = key;
        this.detail = detail;
        this.url = url;

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
     * URLを返す。<br>
     *
     * @return URL
     */
    public String getUrl() {

        final String result = this.url;
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
