package kmg.gr.gr002.dailytasks.carryover.presentation.model;

import java.nio.file.Path;

import org.springframework.boot.context.properties.ConfigurationProperties;

import kmg.gr.gr002.dailytasks.carryover.presentation.types.CarryoverPropertiesFormatTypes;

/**
 * 持ち越しの収集の設定<br>
 * <p>
 * application.properties の {@code carryover.*} を Spring Boot がコンストラクタで設定する。設定ファイルの値を受け取るのは presentation
 * 層のこのクラスだけとし、presentation 層が application 層に引き継ぐ設定としてもそのまま使う。パスはコンストラクタで 1
 * 回だけ変換して保持する。トークンはログに出さないよう、{@link #toString()} に含めない。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@ConfigurationProperties(prefix = "carryover")
public class CarryoverPropertiesModel {

    /* オリジナル */

    /**
     * 出力先のディレクトリ（例: docs/data）
     *
     * @since 0.1.0
     */
    private final String dataDir;

    /**
     * 出力先のディレクトリのパス
     *
     * @since 0.1.0
     */
    private final Path dataDirPath;

    /**
     * 標準時間の設定ファイル（例: config/default-minutes.json）
     *
     * @since 0.1.0
     */
    private final String defaultMinutesFile;

    /**
     * 標準時間の設定ファイルのパス
     *
     * @since 0.1.0
     */
    private final Path defaultMinutesFilePath;

    /**
     * 収集と集計を繰り返す単位（Issue の件数）。1 未満のときは全件を 1 単位にする
     *
     * @since 0.1.0
     */
    private final int batchSize;

    /**
     * 差分モードで更新日時に関係なく解析し直す最新の Issue の件数
     *
     * @since 0.1.0
     */
    private final int recentCount;

    /* 変換後 */

    /**
     * 対象リポジトリ（owner/name）
     *
     * @since 0.1.0
     */
    private final String repository;

    /**
     * GitHub API のトークン。未設定の場合は空文字
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
     *                           対象リポジトリ（owner/name）
     * @param token
     *                           GitHub API のトークン。未設定の場合は空文字
     * @param dataDir
     *                           出力先のディレクトリ（例: docs/data）
     * @param defaultMinutesFile
     *                           標準時間の設定ファイル（例: config/default-minutes.json）
     * @param recentCount
     *                           差分モードで更新日時に関係なく解析し直す最新の Issue の件数
     * @param batchSize
     *                           収集と集計を繰り返す単位（Issue の件数）。1 未満のときは全件を 1 単位にする
     */
    public CarryoverPropertiesModel(final String repository, final String token, final String dataDir,
        final String defaultMinutesFile, final int recentCount, final int batchSize) {

        /* オリジナル */
        this.repository = repository;
        this.token = token;
        this.dataDir = dataDir;
        this.defaultMinutesFile = defaultMinutesFile;
        this.recentCount = recentCount;
        this.batchSize = batchSize;

        /* 変換後 */
        this.dataDirPath = Path.of(dataDir);
        this.defaultMinutesFilePath = Path.of(defaultMinutesFile);

    }

    /**
     * 収集と集計を繰り返す単位を返す<br>
     *
     * @since 0.1.0
     *
     * @return 単位（Issue の件数）。1 未満のときは全件を 1 単位にする
     */
    public int getBatchSize() {

        final int result = this.batchSize;
        return result;

    }

    /**
     * 出力先のディレクトリを返す<br>
     *
     * @since 0.1.0
     *
     * @return 出力先のディレクトリ
     */
    public Path getDataDir() {

        final Path result = this.dataDirPath;
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

        final Path result = this.defaultMinutesFilePath;
        return result;

    }

    /**
     * 差分モードで更新日時に関係なく解析し直す最新の Issue の件数を返す<br>
     *
     * @since 0.1.0
     *
     * @return 差分モードで更新日時に関係なく解析し直す最新の Issue の件数
     */
    public int getRecentCount() {

        final int result = this.recentCount;
        return result;

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
     * GitHub API のトークンを返す<br>
     *
     * @since 0.1.0
     *
     * @return GitHub API のトークン。未設定の場合は空文字
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

        final String result = String.format(CarryoverPropertiesFormatTypes.TO_STRING.getFormat(), this.repository,
            this.dataDir, this.defaultMinutesFile, Integer.valueOf(this.recentCount), Integer.valueOf(this.batchSize));
        return result;

    }

}
