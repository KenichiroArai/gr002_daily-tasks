package kmg.gr.gr002.dailytasks.advance.domain.repository.dao;

import java.io.IOException;
import java.nio.file.Path;

import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverSummaryDto;

/**
 * 先行の画面用集計を読み書きする DAO<br>
 * <p>
 * domain 層が repository 層に求めるインタフェース。実装は repository 層に置き、保存の形式（JSON ファイルなど）は実装側に任せる。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.2.0
 *
 * @version 0.2.0
 */
public interface AdvanceDataDao {

    /**
     * 出力先のディレクトリを受け取り、フィールドに保持する<br>
     *
     * @since 0.2.0
     *
     * @param dataDir
     *                出力先のディレクトリ（例: docs/data）
     */
    void initialize(Path dataDir);

    /**
     * 画面用の集計を保存する<br>
     *
     * @since 0.2.0
     *
     * @param summary
     *                画面用の集計
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    void saveSummary(CarryoverSummaryDto summary) throws IOException;

}