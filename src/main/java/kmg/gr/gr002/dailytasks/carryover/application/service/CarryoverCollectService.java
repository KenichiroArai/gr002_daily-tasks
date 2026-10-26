package kmg.gr.gr002.dailytasks.carryover.application.service;

import java.io.IOException;
import java.util.List;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssuesModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSourceModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueRefModel;

/**
 * 持ち越しの収集サービス<br>
 * <p>
 * 統合サービスが呼ぶ、Issue の取得・差分判定・解析・保存。実装は同じ層の impl パッケージに置く。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public interface CarryoverCollectService {

    /**
     * 差分判定して解析する<br>
     * <p>
     * {@link #fetch()} のあとで呼ぶ。保存はしない。解析の途中で失敗したときは、今回解析した Issue はまだ保存されていない。
     * </p>
     *
     * @since 0.1.0
     *
     * @return 解析した Issue の件数
     */
    int analyze();

    /**
     * 渡された参照だけを差分判定して解析し、保存する<br>
     * <p>
     * {@link #fetchRefs()} のあとで呼ぶ。戻り値の解析結果は {@link #getBatchIssues()} でも返す。
     * </p>
     *
     * @since 0.1.0
     *
     * @param batch
     *              この単位の参照（日付順）
     *
     * @return 解析して保存した Issue の件数
     *
     * @throws IOException
     *                     取得、読み込みまたは書き込みに失敗した場合
     */
    int collectBatch(List<DailyTaskIssueRefModel> batch) throws IOException;

    /**
     * 標準時間、保存済みの解析結果、リモートの Issue を取得する<br>
     * <p>
     * {@link #initialize(CarryoverSourceModel, int, boolean)} のあとで呼ぶ。標準時間は解析ロジックへ渡す。
     * </p>
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     読み込みまたは取得に失敗した場合
     */
    void fetch() throws IOException;

    /**
     * 本文を持たない参照を取得し、タイトルから日付を付ける<br>
     * <p>
     * 標準時間の読み込みは、最初の呼び出しで 1 回行う。差分判定の下限もここで決める。
     * </p>
     *
     * @since 0.1.0
     *
     * @return 日々のタスク Issue の参照（作成順）
     *
     * @throws IOException
     *                     読み込みまたは取得に失敗した場合
     */
    List<DailyTaskIssueRefModel> fetchRefs() throws IOException;

    /**
     * 直前の {@link #collectBatch(List)} で扱った解析結果を返す<br>
     *
     * @since 0.1.0
     *
     * @return その単位の解析結果。未収集の場合は空
     */
    List<CarryoverIssueModel> getBatchIssues();

    /**
     * 保存済みと今回解析した Issue を返す<br>
     *
     * @since 0.1.0
     *
     * @return Issue 番号と解析結果の対応。未取得の場合は空
     */
    CarryoverIssuesModel getIssues();

    /**
     * 取得したリモートの Issue の件数を返す<br>
     *
     * @since 0.1.0
     *
     * @return 件数。未取得の場合は 0
     */
    int getRemoteCount();

    /**
     * 収集の取得元と差分判定の条件を受け取り、フィールドに保持する<br>
     * <p>
     * Issue の取得、標準時間の読み込み、解析結果の読み書きは、それぞれが必要とする値で初期化する。
     * </p>
     *
     * @since 0.1.0
     *
     * @param source
     *                    データの取得元と保存先
     * @param recentCount
     *                    差分モードで更新日時に関係なく解析し直す最新の Issue の件数
     * @param full
     *                    true：全件モード（すべての Issue を解析し直す）、false：差分モード
     */
    void initialize(CarryoverSourceModel source, int recentCount, boolean full);

    /**
     * 今回解析した Issue を保存する<br>
     * <p>
     * {@link #analyze()} のあとで呼ぶ。
     * </p>
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    void saveIssues() throws IOException;

}
