package kmg.gr.gr002.dailytasks.carryover.repository.types;

import java.util.HashMap;
import java.util.Map;

import kmg.core.infrastructure.cmn.KmgCmnTypes;

/**
 * GitHub API のヘッダ<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings("nls")
public enum GitHubApiHeaderTypes implements KmgCmnTypes<String> {

    /* 定義：開始 */

    /** 指定無し */
    NONE("指定無し", "None", "指定無し", null),

    /** Accept */
    ACCEPT("Accept", "Accept", "Accept ヘッダ名", null),

    /** GitHub JSON */
    GITHUB_JSON("GitHub JSON", "GithubJson", "Accept ヘッダの値", "application/vnd.github+json"),

    /** API バージョン */
    API_VERSION("API バージョン", "ApiVersion", "API バージョンのヘッダ名", "X-GitHub-Api-Version"),

    /** API バージョンの値 */
    API_VERSION_VALUE("API バージョンの値", "ApiVersionValue", "API バージョンのヘッダの値", "2022-11-28"),

    /** Authorization */
    AUTHORIZATION("Authorization", "Authorization", "Authorization ヘッダ名", null),

    /** Bearer */
    BEARER("Bearer", "Bearer", "Bearer スキーム", "Bearer "),

    /* 定義：終了 */
    ;

    /** 種類のマップ */
    private static final Map<String, GitHubApiHeaderTypes> VALUES_MAP = new HashMap<>();

    static {

        /* 種類のマップにプット */
        for (final GitHubApiHeaderTypes type : GitHubApiHeaderTypes.values()) {

            GitHubApiHeaderTypes.VALUES_MAP.put(type.get(), type);

            if (type.getHeader() != null) {

                GitHubApiHeaderTypes.VALUES_MAP.put(type.getHeader(), type);

            }

        }

    }

    /**
     * デフォルトの種類を返す<br>
     *
     * @return デフォルト値
     */
    public static GitHubApiHeaderTypes getDefault() {

        final GitHubApiHeaderTypes result = NONE;
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
    public static GitHubApiHeaderTypes getEnum(final String key) {

        GitHubApiHeaderTypes result = GitHubApiHeaderTypes.VALUES_MAP.get(key);

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
    public static GitHubApiHeaderTypes getInitValue() {

        final GitHubApiHeaderTypes result = NONE;
        return result;

    }

    /** 表示名 */
    private final String displayName;

    /** キー */
    private final String key;

    /** 詳細情報 */
    private final String detail;

    /** ヘッダ */
    private final String header;

    /**
     * コンストラクタ<br>
     *
     * @param displayName
     *                    表示名
     * @param key
     *                    キー
     * @param detail
     *                    詳細情報
     * @param header
     *                    ヘッダ
     */
    GitHubApiHeaderTypes(final String displayName, final String key, final String detail, final String header) {

        this.displayName = displayName;
        this.key = key;
        this.detail = detail;
        this.header = header;

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
     * ヘッダを返す。<br>
     *
     * @return ヘッダ
     */
    public String getHeader() {

        final String result = this.header;
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
