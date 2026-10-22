package kmg.gr.gr002.dailytasks.carryover.application.logic;

import java.io.IOException;
import java.nio.file.Path;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssuesModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;

/**
 * 持ち越しの解析結果と画面用の集計を読み書きするロジック<br>
 * <p>
 * application 層が domain 層に求めるインタフェース。実装は domain 層に置く。保存先は {@link #initialize(Path)} で受け取る。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public interface CarryoverDataLogic {

    /**
     * 解析結果と集計の保存先を受け取り、読み書き先を初期化する<br>
     *
     * @since 0.1.0
     *
     * @param dataDir
     *                保存先のディレクトリ（例: docs/data）
     */
    void initialize(Path dataDir);

    /**
     * 保存済みの持ち越しの解析結果を読み込む<br>
     *
     * @since 0.1.0
     *
     * @return Issue 番号と解析結果の対応。保存されていない場合は空
     *
     * @throws IOException
     *                     読み込みに失敗した場合
     */
    CarryoverIssuesModel loadIssues() throws IOException;

    /**
     * 持ち越しの解析結果を保存する<br>
     *
     * @since 0.1.0
     *
     * @param issue
     *              持ち越しの解析結果
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    void saveIssue(CarryoverIssueModel issue) throws IOException;

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
    void saveSummary(CarryoverSummaryModel summary) throws IOException;

}
