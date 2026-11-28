package kmg.gr.gr002.dailytasks.advance.application.logic;

import java.io.IOException;
import java.nio.file.Path;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;

/**
 * 先行の画面用の集計を読み書きするロジック<br>
 * <p>
 * application 層が domain 層に求めるインタフェース。実装は domain 層に置く。保存先は {@link #initialize(Path)} で受け取る。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.2.0
 *
 * @version 0.2.0
 */
public interface AdvanceDataLogic {

    /**
     * 集計の保存先を受け取り、読み書き先を初期化する<br>
     *
     * @since 0.2.0
     *
     * @param dataDir
     *                保存先のディレクトリ（例: docs/data）
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
    void saveSummary(CarryoverSummaryModel summary) throws IOException;

}
