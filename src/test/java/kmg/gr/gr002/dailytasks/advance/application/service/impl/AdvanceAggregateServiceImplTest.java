package kmg.gr.gr002.dailytasks.advance.application.service.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.advance.application.logic.AdvanceAggregateLogic;
import kmg.gr.gr002.dailytasks.advance.application.logic.AdvanceDataLogic;
import kmg.gr.gr002.dailytasks.advance.domain.logic.impl.AdvanceAggregateLogicImpl;
import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverDataLogic;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverAggregationModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssuesModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverItemModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.types.MinutesSourceTypes;
import kmg.gr.gr002.dailytasks.carryover.domain.types.TargetSectionTypes;
import kmg.gr.gr002.dailytasks.testutil.DailyTasksMessageSourceTestUtil;

/**
 * {@link AdvanceAggregateServiceImpl} のテスト<br>
 *
 * @author KenichiroArai
 *
 * @since 0.2.0
 *
 * @version 0.2.0
 */
@SuppressWarnings({
    "nls", "static-method"
})
public class AdvanceAggregateServiceImplTest {

    /**
     * 呼び出しを記録する先行の日別集計<br>
     *
     * @author KenichiroArai
     *
     * @since 0.2.0
     *
     * @version 0.2.0
     */
    private static final class RecordingAdvanceAggregateLogic implements AdvanceAggregateLogic {

        /**
         * 呼び出しの順
         *
         * @since 0.2.0
         */
        private final List<String> steps;

        /**
         * add の回数
         *
         * @since 0.2.0
         */
        private int addCount;

        /**
         * aggregate の返却値
         *
         * @since 0.2.0
         */
        private final CarryoverSummaryModel aggregateResult;

        /**
         * toSummary の返却値
         *
         * @since 0.2.0
         */
        private final CarryoverSummaryModel toSummaryResult;

        /**
         * コンストラクタ<br>
         *
         * @since 0.2.0
         *
         * @param steps
         *              呼び出しの順
         */
        private RecordingAdvanceAggregateLogic(final List<String> steps) {

            this.steps = steps;
            this.aggregateResult = new CarryoverSummaryModel(100, List.of("国語"), List.of());
            this.toSummaryResult = new CarryoverSummaryModel(200, List.of(), List.of());

        }

        /**
         * 呼び出しを記録する<br>
         *
         * @since 0.2.0
         *
         * @param aggregation
         *                    集計の状態
         * @param issue
         *                    解析結果
         */
        @Override
        public void add(final CarryoverAggregationModel aggregation, final CarryoverIssueModel issue) {

            this.addCount++;
            this.steps.add("add");

        }

        /**
         * 呼び出しを記録し、設定した集計を返す<br>
         *
         * @since 0.2.0
         *
         * @param issues
         *               解析結果
         *
         * @return 設定した集計
         */
        @Override
        public CarryoverSummaryModel aggregate(final Collection<CarryoverIssueModel> issues) {

            this.steps.add("aggregate");
            final CarryoverSummaryModel result = this.aggregateResult;
            return result;

        }

        /**
         * 呼び出しを記録し、設定した集計を返す<br>
         *
         * @since 0.2.0
         *
         * @param aggregation
         *                    集計の状態
         *
         * @return 設定した集計
         */
        @Override
        public CarryoverSummaryModel toSummary(final CarryoverAggregationModel aggregation) {

            this.steps.add("toSummary");
            final CarryoverSummaryModel result = this.toSummaryResult;
            return result;

        }

    }

    /**
     * 呼び出しを記録する先行の画面用集計の保存<br>
     *
     * @author KenichiroArai
     *
     * @since 0.2.0
     *
     * @version 0.2.0
     */
    private static final class RecordingAdvanceDataLogic implements AdvanceDataLogic {

        /**
         * 呼び出しの順
         *
         * @since 0.2.0
         */
        private final List<String> steps;

        /**
         * 最後に保存された集計
         *
         * @since 0.2.0
         */
        private CarryoverSummaryModel savedSummary;

        /**
         * 最後に受け取った出力先
         *
         * @since 0.2.0
         */
        private Path dataDir;

        /**
         * コンストラクタ<br>
         *
         * @since 0.2.0
         *
         * @param steps
         *              呼び出しの順
         */
        private RecordingAdvanceDataLogic(final List<String> steps) {

            this.steps = steps;

        }

        /**
         * 最後に保存された集計を返す<br>
         *
         * @since 0.2.0
         *
         * @return 最後に保存された集計
         */
        private CarryoverSummaryModel getSavedSummary() {

            final CarryoverSummaryModel result = this.savedSummary;
            return result;

        }

        /**
         * 最後に受け取った出力先を返す<br>
         *
         * @since 0.2.0
         *
         * @return 最後に受け取った出力先
         */
        private Path getDataDir() {

            final Path result = this.dataDir;
            return result;

        }

        /**
         * 呼び出しを記録する<br>
         *
         * @since 0.2.0
         *
         * @param targetDir
         *                  出力先のディレクトリ
         */
        @Override
        public void initialize(final Path targetDir) {

            this.dataDir = targetDir;
            this.steps.add("advanceData.initialize");

        }

        /**
         * 呼び出しを記録する<br>
         *
         * @since 0.2.0
         *
         * @param summary
         *                画面用の集計
         */
        @Override
        public void saveSummary(final CarryoverSummaryModel summary) throws IOException {

            this.savedSummary = summary;
            this.steps.add("advanceData.saveSummary");

        }

    }

    /**
     * 呼び出しを記録する持ち越しの解析結果の読み込み<br>
     *
     * @author KenichiroArai
     *
     * @since 0.2.0
     *
     * @version 0.2.0
     */
    private static final class RecordingCarryoverDataLogic implements CarryoverDataLogic {

        /**
         * 呼び出しの順
         *
         * @since 0.2.0
         */
        private final List<String> steps;

        /**
         * loadIssues の返却値
         *
         * @since 0.2.0
         */
        private final CarryoverIssuesModel issuesModel;

        /**
         * コンストラクタ<br>
         *
         * @since 0.2.0
         *
         * @param steps
         *                  呼び出しの順
         * @param issuesModel
         *                  loadIssues の返却値
         */
        private RecordingCarryoverDataLogic(final List<String> steps, final CarryoverIssuesModel issuesModel) {

            this.steps = steps;
            this.issuesModel = issuesModel;

        }

        /**
         * 呼び出しを記録する<br>
         *
         * @since 0.2.0
         *
         * @param targetDir
         *                  出力先のディレクトリ
         */
        @Override
        public void initialize(final Path targetDir) {

            this.steps.add("carryoverData.initialize");

        }

        /**
         * 未使用
         *
         * @since 0.2.0
         */
        @Override
        public CarryoverIssueModel loadIssue(final int number) throws IOException {

            return null;

        }

        /**
         * 設定した解析結果を返す<br>
         *
         * @since 0.2.0
         */
        @Override
        public CarryoverIssuesModel loadIssues() throws IOException {

            this.steps.add("carryoverData.loadIssues");
            final CarryoverIssuesModel result = this.issuesModel;
            return result;

        }

        /**
         * 未使用
         *
         * @since 0.2.0
         */
        @Override
        public void saveIssue(final CarryoverIssueModel issue) throws IOException {

            // 処理なし

        }

        /**
         * 未使用
         *
         * @since 0.2.0
         */
        @Override
        public void saveSummary(final CarryoverSummaryModel summary) throws IOException {

            // 処理なし

        }

    }

    /**
     * テスト用の先行項目を作成する<br>
     *
     * @since 0.2.0
     *
     * @param name
     *             項目名
     *
     * @return 持ち越し項目
     */
    private static CarryoverItemModel createAdvanceItem(final String name) {

        final CarryoverItemModel result = new CarryoverItemModel(name, LocalDate.parse("2026-06-18"), false, 20,
            MinutesSourceTypes.PARSED, TargetSectionTypes.ADVANCE.getSection(), "raw");
        return result;

    }

    /**
     * aggregate メソッドのテスト - 正常系:日別の集計へ委譲する場合
     *
     * @since 0.2.0
     */
    @Test
    public void testAggregate_normalDelegate() {

        /* 期待値の定義 */
        final int expectedLatestIssue = 100;

        /* 準備 */
        final List<String>                  steps      = new ArrayList<>();
        final RecordingAdvanceAggregateLogic aggregateLogic = new RecordingAdvanceAggregateLogic(steps);
        final RecordingAdvanceDataLogic      dataLogic      = new RecordingAdvanceDataLogic(steps);
        final RecordingCarryoverDataLogic    carryoverData   = new RecordingCarryoverDataLogic(steps,
            new CarryoverIssuesModel());
        final AdvanceAggregateServiceImpl  testTarget     = new AdvanceAggregateServiceImpl(aggregateLogic, dataLogic,
            carryoverData);

        /* テスト対象の実行 */
        final CarryoverSummaryModel testResult = testTarget.aggregate(List.of());

        /* 検証の準備 */
        final int actualLatestIssue = testResult.getLatestIssue();

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");
        Assertions.assertEquals(List.of("aggregate"), steps, "呼び出しの順が一致しません");

    }

    /**
     * append メソッドのテスト - 正常系:解析結果を日別の集計へ足す場合
     *
     * @since 0.2.0
     */
    @Test
    public void testAppend_normalAdds() {

        /* 期待値の定義 */
        final int expectedAddCount = 2;

        /* 準備 */
        final List<String>                  steps      = new ArrayList<>();
        final RecordingAdvanceAggregateLogic aggregateLogic = new RecordingAdvanceAggregateLogic(steps);
        final RecordingAdvanceDataLogic      dataLogic      = new RecordingAdvanceDataLogic(steps);
        final RecordingCarryoverDataLogic    carryoverData   = new RecordingCarryoverDataLogic(steps,
            new CarryoverIssuesModel());
        final AdvanceAggregateServiceImpl  testTarget     = new AdvanceAggregateServiceImpl(aggregateLogic, dataLogic,
            carryoverData);
        final List<CarryoverIssueModel>    testIssues     = List.of(
            new CarryoverIssueModel(174, "title", LocalDate.parse("2026-03-23"), "closed", Instant.parse("2026-10-06T00:00:00Z"),
                List.of(), null, List.of()),
            new CarryoverIssueModel(175, "title", LocalDate.parse("2026-03-24"), "closed", Instant.parse("2026-10-06T00:00:00Z"),
                List.of(TargetSectionTypes.ADVANCE.getSection()), null,
                List.of(AdvanceAggregateServiceImplTest.createAdvanceItem("国語"))));

        /* テスト対象の実行 */
        testTarget.append(testIssues);

        /* 検証の準備 */
        final int actualAddCount = aggregateLogic.addCount;

        /* 検証の実施 */
        Assertions.assertEquals(expectedAddCount, actualAddCount, "add の回数が一致しません");

    }

    /**
     * clear メソッドのテスト - 正常系:集計の状態をクリアする場合
     *
     * @since 0.2.0
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    @Test
    public void testClear_normalReset() throws IOException {

        /* 期待値の定義 */
        final int expectedLatestIssue = 0;

        /* 準備 */
        final List<String>               steps           = new ArrayList<>();
        final AdvanceAggregateLogicImpl  aggregateLogic  = new AdvanceAggregateLogicImpl(
            DailyTasksMessageSourceTestUtil.create());
        final RecordingAdvanceDataLogic    dataLogic       = new RecordingAdvanceDataLogic(steps);
        final RecordingCarryoverDataLogic carryoverData    = new RecordingCarryoverDataLogic(steps,
            new CarryoverIssuesModel());
        final AdvanceAggregateServiceImpl testTarget      = new AdvanceAggregateServiceImpl(aggregateLogic, dataLogic,
            carryoverData);
        final List<CarryoverIssueModel>   testIssues      = List.of(new CarryoverIssueModel(175, "title",
            LocalDate.parse("2026-03-24"), "closed", Instant.parse("2026-10-06T00:00:00Z"),
            List.of(TargetSectionTypes.ADVANCE.getSection()), null, Integer.valueOf(1),
            List.of(AdvanceAggregateServiceImplTest.createAdvanceItem("国語"))));

        /* テスト対象の実行 */
        testTarget.append(testIssues);
        testTarget.clear();
        testTarget.saveCurrentSummary();

        /* 検証の準備 */
        final CarryoverSummaryModel savedSummary      = dataLogic.getSavedSummary();
        final int                   actualLatestIssue = savedSummary.getLatestIssue();
        final boolean               actualDaysEmpty   = savedSummary.getDays().isEmpty();

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");
        Assertions.assertTrue(actualDaysEmpty, "クリア後は日別の集計が空になる必要があります");

    }

    /**
     * initialize メソッドのテスト - 正常系:保存先を両方のロジックへ渡す場合
     *
     * @since 0.2.0
     */
    @Test
    public void testInitialize_normalBoth() {

        /* 期待値の定義 */
        final Path expectedDataDir = Path.of("docs", "data");

        /* 準備 */
        final List<String>                  steps      = new ArrayList<>();
        final RecordingAdvanceAggregateLogic aggregateLogic = new RecordingAdvanceAggregateLogic(steps);
        final RecordingAdvanceDataLogic      dataLogic      = new RecordingAdvanceDataLogic(steps);
        final RecordingCarryoverDataLogic    carryoverData   = new RecordingCarryoverDataLogic(steps,
            new CarryoverIssuesModel());
        final AdvanceAggregateServiceImpl  testTarget     = new AdvanceAggregateServiceImpl(aggregateLogic, dataLogic,
            carryoverData);

        /* テスト対象の実行 */
        testTarget.initialize(Path.of("docs", "data"));

        /* 検証の準備 */
        final Path actualDataDir = dataLogic.getDataDir();

        /* 検証の実施 */
        Assertions.assertEquals(expectedDataDir, actualDataDir, "出力先のディレクトリが一致しません");
        Assertions.assertEquals(List.of("advanceData.initialize", "carryoverData.initialize"), steps,
            "呼び出しの順が一致しません");

    }

    /**
     * rebuildFromStored メソッドのテスト - 正常系:保存済みの解析結果から集計を作り直す場合
     *
     * @since 0.2.0
     *
     * @throws IOException
     *                     読み込みに失敗した場合
     */
    @Test
    public void testRebuildFromStored_normal() throws IOException {

        /* 期待値の定義 */
        final int expectedLatestIssue = 175;

        /* 準備 */
        final List<String>            steps       = new ArrayList<>();
        final AdvanceAggregateLogicImpl aggregateLogic = new AdvanceAggregateLogicImpl(
            DailyTasksMessageSourceTestUtil.create());
        final RecordingAdvanceDataLogic dataLogic       = new RecordingAdvanceDataLogic(steps);
        final CarryoverIssuesModel      issuesModel     = new CarryoverIssuesModel();
        issuesModel.put(175, new CarryoverIssueModel(175, "title", LocalDate.parse("2026-03-24"), "closed",
            Instant.parse("2026-10-06T00:00:00Z"), List.of(TargetSectionTypes.ADVANCE.getSection()), null,
            Integer.valueOf(1), List.of(AdvanceAggregateServiceImplTest.createAdvanceItem("国語"))));
        final RecordingCarryoverDataLogic carryoverData = new RecordingCarryoverDataLogic(steps, issuesModel);
        final AdvanceAggregateServiceImpl testTarget    = new AdvanceAggregateServiceImpl(aggregateLogic, dataLogic,
            carryoverData);

        /* テスト対象の実行 */
        final CarryoverSummaryModel testResult = testTarget.rebuildFromStored();

        /* 検証の準備 */
        final int actualLatestIssue = testResult.getLatestIssue();

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");
        Assertions.assertEquals(List.of("carryoverData.loadIssues"), steps, "呼び出しの順が一致しません");

    }

    /**
     * saveCurrentSummary メソッドのテスト - 正常系:集計の状態から画面用の集計を保存する場合
     *
     * @since 0.2.0
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    @Test
    public void testSaveCurrentSummary_normal() throws IOException {

        /* 期待値の定義 */
        final int expectedLatestIssue = 200;

        /* 準備 */
        final List<String>                  steps      = new ArrayList<>();
        final RecordingAdvanceAggregateLogic aggregateLogic = new RecordingAdvanceAggregateLogic(steps);
        final RecordingAdvanceDataLogic      dataLogic      = new RecordingAdvanceDataLogic(steps);
        final RecordingCarryoverDataLogic    carryoverData   = new RecordingCarryoverDataLogic(steps,
            new CarryoverIssuesModel());
        final AdvanceAggregateServiceImpl  testTarget     = new AdvanceAggregateServiceImpl(aggregateLogic, dataLogic,
            carryoverData);

        /* テスト対象の実行 */
        testTarget.saveCurrentSummary();

        /* 検証の準備 */
        final CarryoverSummaryModel savedSummary      = dataLogic.getSavedSummary();
        final int                   actualLatestIssue = savedSummary.getLatestIssue();

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");
        Assertions.assertEquals(List.of("toSummary", "advanceData.saveSummary"), steps, "呼び出しの順が一致しません");

    }

    /**
     * saveSummary メソッドのテスト - 正常系:画面用の集計を保存する場合
     *
     * @since 0.2.0
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    @Test
    public void testSaveSummary_normalSave() throws IOException {

        /* 期待値の定義 */
        final int expectedLatestIssue = 371;

        /* 準備 */
        final List<String>                  steps      = new ArrayList<>();
        final RecordingAdvanceAggregateLogic aggregateLogic = new RecordingAdvanceAggregateLogic(steps);
        final RecordingAdvanceDataLogic      dataLogic      = new RecordingAdvanceDataLogic(steps);
        final RecordingCarryoverDataLogic    carryoverData   = new RecordingCarryoverDataLogic(steps,
            new CarryoverIssuesModel());
        final AdvanceAggregateServiceImpl  testTarget     = new AdvanceAggregateServiceImpl(aggregateLogic, dataLogic,
            carryoverData);
        final CarryoverSummaryModel       testSummary    = new CarryoverSummaryModel(371, List.of("国語"), List.of());

        /* テスト対象の実行 */
        testTarget.saveSummary(testSummary);

        /* 検証の準備 */
        final CarryoverSummaryModel savedSummary      = dataLogic.getSavedSummary();
        final int                   actualLatestIssue = savedSummary.getLatestIssue();

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");
        Assertions.assertEquals(List.of("advanceData.saveSummary"), steps, "呼び出しの順が一致しません");

    }

}