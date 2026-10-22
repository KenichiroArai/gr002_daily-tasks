package kmg.gr.gr002.dailytasks.carryover.domain.repository.dao;

import java.io.IOException;
import java.nio.file.Path;

import kmg.gr.gr002.dailytasks.carryover.domain.dto.DefaultMinutesDto;

/**
 * 項目ごとの標準時間の設定を読み込む DAO<br>
 * <p>
 * domain 層が repository 層に求めるインタフェース。実装は repository 層に置く。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public interface DefaultMinutesDao {

    /**
     * 設定ファイルのパスを受け取り、フィールドに保持する<br>
     *
     * @since 0.1.0
     *
     * @param configFile
     *                   設定ファイルのパス（例: config/default-minutes.json）
     */
    void initialize(Path configFile);

    /**
     * 項目ごとの標準時間を読み込む<br>
     *
     * @since 0.1.0
     *
     * @return 項目ごとの標準時間。設定がない場合は空
     *
     * @throws IOException
     *                     読み込みに失敗した場合
     */
    DefaultMinutesDto load() throws IOException;

}
