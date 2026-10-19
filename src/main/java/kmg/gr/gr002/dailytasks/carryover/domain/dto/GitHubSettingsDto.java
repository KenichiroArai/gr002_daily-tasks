package kmg.gr.gr002.dailytasks.carryover.domain.dto;

import kmg.gr.gr002.dailytasks.carryover.domain.types.GitHubSettingsFormatTypes;

/**
 * GitHub API の接続設定の DTO<br>
 * <p>
 * domain 層が repository 層に引き継ぐ設定（domain 層の受け渡しの型）。トークンはログに出さないよう、{@link #toString()} に含めない。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class GitHubSettingsDto {

    /**
     * 対象リポジトリ（owner/name）
     *
     * @since 0.1.0
     */
    private final String repository;

    /**
     * API トークン。未指定の場合は null
     *
     * @since 0.1.0
     */
    private final String token;

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     *
     * @param repository
     *                   対象リポジトリ（owner/name）
     * @param token
     *                   API トークン。未指定の場合は null
     */
    public GitHubSettingsDto(final String repository, final String token) {

        this.repository = repository;
        this.token = token;

    }

    /**
     * 対象リポジトリを返す<br>
     *
     * @since 0.1.0
     *
     * @return 対象リポジトリ（owner/name）
     */
    public String getRepository() {

        final String result = this.repository;
        return result;

    }

    /**
     * API トークンを返す<br>
     *
     * @since 0.1.0
     *
     * @return API トークン。未指定の場合は null
     */
    public String getToken() {

        final String result = this.token;
        return result;

    }

    /**
     * 文字列表現を返す<br>
     * <p>
     * トークンは含めない。
     * </p>
     *
     * @since 0.1.0
     *
     * @return 文字列表現
     */
    @Override
    public String toString() {

        final String result = String.format(GitHubSettingsFormatTypes.TO_STRING.getFormat(), this.repository);
        return result;

    }

}
