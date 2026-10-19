package kmg.gr.gr002.dailytasks.carryover.domain.repository.dao;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverIssueDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverSummaryDto;

/**
 * 持ち越しのデータ（Issue ごとの解析結果と画面用の集計）を読み書きする DAO<br>
 * <p>
 * domain 層が repository 層に求めるインタフェース。実装は repository 層に置き、保存の形式（JSON ファイルなど）は実装側に任せる。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public interface CarryoverDataDao {

    /**
     * 出力先のディレクトリを受け取り、フィールドに保持する<br>
     *
     * @since 0.1.0
     *
     * @param dataDir
     *                出力先のディレクトリ（例: docs/data）
     */
    void initialize(Path dataDir);

    /**
     * 保存済みの Issue ごとの解析結果を読み込む<br>
     *
     * @since 0.1.0
     *
     * @return Issue 番号と解析結果の対応。保存先がない場合は空
     *
     * @throws IOException
     *                     読み込みに失敗した場合
     */
    Map<Integer, CarryoverIssueDto> loadIssues() throws IOException;

    /**
     * Issue ごとの解析結果を保存する<br>
     *
     * @since 0.1.0
     *
     * @param issue
     *              解析結果
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    void saveIssue(CarryoverIssueDto issue) throws IOException;

    /**
     * 画面用の集計を保存する<br>
     *
     * @since 0.1.0
     *
     * @param summary
     *                画面用の集計
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    void saveSummary(CarryoverSummaryDto summary) throws IOException;

}
