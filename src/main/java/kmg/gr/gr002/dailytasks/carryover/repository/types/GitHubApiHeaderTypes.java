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

    /**
     * 指定無し
     *
     * @since 0.1.0
     */
    NONE("指定無し", "None", "指定無し", null),

    /**
     * Accept
     *
     * @since 0.1.0
     */
    ACCEPT("Accept", "Accept", "Accept ヘッダ名", null),

    /**
     * GitHub JSON
     *
     * @since 0.1.0
     */
    GITHUB_JSON("GitHub JSON", "GithubJson", "Accept ヘッダの値", "application/vnd.github+json"),

    /**
     * API バージョン
     *
     * @since 0.1.0
     */
    API_VERSION("API バージョン", "ApiVersion", "API バージョンのヘッダ名", "X-GitHub-Api-Version"),

    /**
     * API バージョンの値
     *
     * @since 0.1.0
     */
    API_VERSION_VALUE("API バージョンの値", "ApiVersionValue", "API バージョンのヘッダの値", "2022-11-28"),

    /**
     * Authorization
     *
     * @since 0.1.0
     */
    AUTHORIZATION("Authorization", "Authorization", "Authorization ヘッダ名", null),

    /**
     * Bearer
     *
     * @since 0.1.0
     */
    BEARER("Bearer", "Bearer", "Bearer スキーム", "Bearer "),

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
    private static final Map<String, GitHubApiHeaderTypes> VALUES_MAP = new HashMap<>();

    static {

        /* 種類のマップにプット */
        for (final GitHubApiHeaderTypes type : GitHubApiHeaderTypes.values()) {

            GitHubApiHeaderTypes.VALUES_MAP.put(type.get(), type);

            /* 別名があるか */
            final String header = type.getHeader();

            if (header != null) {

                // 別名がある場合

                // 別名でも引けるようにする
                GitHubApiHeaderTypes.VALUES_MAP.put(header, type);

            }

        }

    }

    /**
     * デフォルトの種類を返す<br>
     *
     * @since 0.1.0
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
     * @since 0.1.0
     *
     * @param key
     *            キー
     *
     * @return 種類。指定無し（NONE）：キーが存在しない場合。
     */
    public static GitHubApiHeaderTypes getEnum(final String key) {

        GitHubApiHeaderTypes result = GitHubApiHeaderTypes.VALUES_MAP.get(key);

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
    public static GitHubApiHeaderTypes getInitValue() {

        final GitHubApiHeaderTypes result = NONE;
        return result;

    }

    /**
     * 表示名
     *
     * @since 0.1.0
     */
    private final String displayName;

    /**
     * キー
     *
     * @since 0.1.0
     */
    private final String key;

    /**
     * 詳細情報
     *
     * @since 0.1.0
     */
    private final String detail;

    /**
     * ヘッダ
     *
     * @since 0.1.0
     */
    private final String header;

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
     * @since 0.1.0
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
     * ヘッダを返す。<br>
     *
     * @since 0.1.0
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
     * @since 0.1.0
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
     * @since 0.1.0
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
