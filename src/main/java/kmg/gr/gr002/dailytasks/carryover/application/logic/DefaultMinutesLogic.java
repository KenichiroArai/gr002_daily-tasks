package kmg.gr.gr002.dailytasks.carryover.application.logic;

import java.io.IOException;
import java.nio.file.Path;

import kmg.gr.gr002.dailytasks.carryover.application.model.DefaultMinutesModel;

/**
 * 項目ごとの標準時間を読み込むロジック<br>
 * <p>
 * application 層が domain 層に求めるインタフェース。実装は domain 層に置く。設定ファイルは
 * {@link #initialize(Path)} で受け取る。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public interface DefaultMinutesLogic {

    /**
     * 標準時間の設定ファイルを受け取り、読み込み先を初期化する<br>
     *
     * @since 0.1.0
     *
     * @param defaultMinutesFile
     *                           標準時間の設定ファイル（例: config/default-minutes.json）
     */
    void initialize(Path defaultMinutesFile);

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
    DefaultMinutesModel loadDefaultMinutes() throws IOException;

}
