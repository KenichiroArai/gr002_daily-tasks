package kmg.gr.gr002.dailytasks.carryover.application.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.List;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;

/**
 * 持ち越しの集計サービス<br>
 * <p>
 * 統合サービスが呼ぶ、日別集計の作成と保存。実装は同じ層の impl パッケージに置く。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public interface CarryoverAggregateService {

    /**
     * 解析結果から日別の集計を作る<br>
     * <p>
     * 保存はしない。
     * </p>
     *
     * @since 0.1.0
     *
     * @param issues
     *               持ち越しの解析結果
     *
     * @return 画面用の集計
     */
    CarryoverSummaryModel aggregate(Collection<CarryoverIssueModel> issues);

    /**
     * 解析結果を、実行中の集計の状態へ日付順のまま足す<br>
     *
     * @since 0.1.0
     *
     * @param issues
     *               この単位の解析結果（日付順）
     */
    void append(List<CarryoverIssueModel> issues);

    /**
     * 集計の保存先を受け取り、フィールドに保持する<br>
     *
     * @since 0.1.0
     *
     * @param dataDir
     *                保存先のディレクトリ（例: docs/data）
     */
    void initialize(Path dataDir);

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

    /**
     * ここまでの集計の状態から画面用の集計を作る<br>
     *
     * @since 0.1.0
     *
     * @return 画面用の集計
     */
    CarryoverSummaryModel summary();

}
