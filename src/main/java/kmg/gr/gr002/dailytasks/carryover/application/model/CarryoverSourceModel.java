package kmg.gr.gr002.dailytasks.carryover.application.model;

import java.nio.file.Path;

import kmg.gr.gr002.dailytasks.carryover.application.types.CarryoverSourceFormatTypes;

/**
 * 持ち越しのデータの取得元と保存先<br>
 * <p>
 * application 層が domain 層に引き継ぐ設定（application 層の受け渡しの型）。トークンはログに出さないよう、{@link #toString()} に含めない。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class CarryoverSourceModel {

    /**
     * 解析結果と集計の保存先のディレクトリ（例: docs/data）
     *
     * @since 0.1.0
     */
    private final Path dataDir;

    /**
     * 標準時間の設定ファイル（例: config/default-minutes.json）
     *
     * @since 0.1.0
     */
    private final Path defaultMinutesFile;

    /**
     * Issue を取得するリポジトリ（owner/name）
     *
     * @since 0.1.0
     */
    private final String repository;

    /**
     * GitHub API のトークン。未指定の場合は null
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
     *                           Issue を取得するリポジトリ（owner/name）
     * @param token
     *                           GitHub API のトークン。未指定の場合は null
     * @param dataDir
     *                           解析結果と集計の保存先のディレクトリ（例: docs/data）
     * @param defaultMinutesFile
     *                           標準時間の設定ファイル（例: config/default-minutes.json）
     */
    public CarryoverSourceModel(final String repository, final String token, final Path dataDir,
        final Path defaultMinutesFile) {

        this.repository = repository;
        this.token = token;
        this.dataDir = dataDir;
        this.defaultMinutesFile = defaultMinutesFile;

    }

    /**
     * 解析結果と集計の保存先のディレクトリを返す<br>
     *
     * @since 0.1.0
     *
     * @return 保存先のディレクトリ
     */
    public Path getDataDir() {

        final Path result = this.dataDir;
        return result;

    }

    /**
     * 標準時間の設定ファイルを返す<br>
     *
     * @since 0.1.0
     *
     * @return 標準時間の設定ファイル
     */
    public Path getDefaultMinutesFile() {

        final Path result = this.defaultMinutesFile;
        return result;

    }

    /**
     * Issue を取得するリポジトリを返す<br>
     *
     * @since 0.1.0
     *
     * @return Issue を取得するリポジトリ（owner/name）
     */
    public String getRepository() {

        final String result = this.repository;
        return result;

    }

    /**
     * GitHub API のトークンを返す<br>
     *
     * @since 0.1.0
     *
     * @return GitHub API のトークン。未指定の場合は null
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

        final String result = String.format(CarryoverSourceFormatTypes.TO_STRING.getFormat(), this.repository,
            this.dataDir, this.defaultMinutesFile);
        return result;

    }

}
