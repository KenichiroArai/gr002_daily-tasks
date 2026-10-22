package kmg.gr.gr002.dailytasks.carryover.application.service.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverDataLogic;
import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverParseLogic;
import kmg.gr.gr002.dailytasks.carryover.application.logic.DailyTaskIssueLogic;
import kmg.gr.gr002.dailytasks.carryover.application.logic.DefaultMinutesLogic;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssuesModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSourceModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DefaultMinutesModel;
import kmg.gr.gr002.dailytasks.carryover.application.service.CarryoverAggregateService;
import kmg.gr.gr002.dailytasks.carryover.application.service.CarryoverCollectService;
import kmg.gr.gr002.dailytasks.carryover.application.types.MinutesSourceTypes;
import kmg.gr.gr002.dailytasks.carryover.domain.logic.impl.CarryoverAggregateLogicImpl;
import kmg.gr.gr002.dailytasks.carryover.domain.logic.impl.CarryoverParseLogicImpl;
import kmg.gr.gr002.dailytasks.carryover.presentation.model.CarryoverPropertiesModel;
import kmg.gr.gr002.dailytasks.testutil.LogAssertions;
import kmg.gr.gr002.dailytasks.testutil.LogCapture;
import kmg.gr.gr002.dailytasks.testutil.MessageLogicTestUtil;
import kmg.gr.gr002.dailytasks.testutil.ReflectionTestUtil;

/**
 * {@link CarryoverServiceImpl} のテスト<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings({
    "nls", "static-method"
})
public class CarryoverServiceImplTest {

    /**
     * 呼び出し順を記録する集計サービス<br>
     *
     * @author KenichiroArai
     *
     * @since 0.1.0
     *
     * @version 0.1.0
     */
    private static final class RecordingAggregateService implements CarryoverAggregateService {

        /**
         * 呼び出しの順
         *
         * @since 0.1.0
         */
        private final List<String> steps;

        /**
         * コンストラクタ<br>
         *
         * @since 0.1.0
         *
         * @param steps
         *              呼び出しの順
         */
        private RecordingAggregateService(final List<String> steps) {

            this.steps = steps;

        }

        /**
         * 呼び出しを記録し、空の集計を返す<br>
         *
         * @since 0.1.0
         *
         * @param issues
         *               持ち越しの解析結果
         *
         * @return 空の集計
         */
        @Override
        public CarryoverSummaryModel aggregate(final Collection<CarryoverIssueModel> issues) {

            this.steps.add("aggregate");
            final CarryoverSummaryModel result = new CarryoverSummaryModel(0, List.of(), List.of());
            return result;

        }

        /**
         * 呼び出しを記録する<br>
         *
         * @since 0.1.0
         *
         * @param dataDir
         *                保存先のディレクトリ
         */
        @Override
        public void initialize(final Path dataDir) {

            this.steps.add("aggregate.initialize");

        }

        /**
         * 呼び出しを記録する<br>
         *
         * @since 0.1.0
         *
         * @param summary
         *                画面用の集計
         */
        @Override
        public void saveSummary(final CarryoverSummaryModel summary) {

            this.steps.add("saveSummary");

        }

    }

    /**
     * 呼び出し順を記録する収集サービス<br>
     *
     * @author KenichiroArai
     *
     * @since 0.1.0
     *
     * @version 0.1.0
     */
    private static final class RecordingCollectService implements CarryoverCollectService {

        /**
         * 呼び出しの順
         *
         * @since 0.1.0
         */
        private final List<String> steps;

        /**
         * コンストラクタ<br>
         *
         * @since 0.1.0
         *
         * @param steps
         *              呼び出しの順
         */
        private RecordingCollectService(final List<String> steps) {

            this.steps = steps;

        }

        /**
         * 呼び出しを記録する<br>
         *
         * @since 0.1.0
         *
         * @return 1
         */
        @Override
        public int analyze() {

            this.steps.add("analyze");
            final int result = 1;
            return result;

        }

        /**
         * 呼び出しを記録する<br>
         *
         * @since 0.1.0
         */
        @Override
        public void fetch() {

            this.steps.add("fetch");

        }

        /**
         * 空の解析結果を返す<br>
         *
         * @since 0.1.0
         *
         * @return 空の解析結果
         */
        @Override
        public CarryoverIssuesModel getIssues() {

            final CarryoverIssuesModel result = new CarryoverIssuesModel();
            return result;

        }

        /**
         * 0 を返す<br>
         *
         * @since 0.1.0
         *
         * @return 0
         */
        @Override
        public int getRemoteCount() {

            final int result = 0;
            return result;

        }

        /**
         * 呼び出しを記録する<br>
         *
         * @since 0.1.0
         *
         * @param source
         *                    データの取得元と保存先
         * @param recentCount
         *                    差分モードで解析し直す最新の件数
         * @param full
         *                    全件モードか
         */
        @Override
        public void initialize(final CarryoverSourceModel source, final int recentCount, final boolean full) {

            this.steps.add("collect.initialize");

        }

        /**
         * 呼び出しを記録する<br>
         *
         * @since 0.1.0
         */
        @Override
        public void saveIssues() {

            this.steps.add("saveIssues");

        }

    }

    /**
     * テスト用の解析結果と集計の読み書き<br>
     * <p>
     * ファイルの入出力をせず、保存済みの解析結果をメモリ上で扱う。
     * </p>
     *
     * @author KenichiroArai
     *
     * @since 0.1.0
     *
     * @version 0.1.0
     */
    private static final class StubCarryoverDataLogic implements CarryoverDataLogic {

        /**
         * 受け取った保存先のディレクトリ
         *
         * @since 0.1.0
         */
        private Path dataDir;

        /**
         * 保存した Issue 番号
         *
         * @since 0.1.0
         */
        private final List<Integer> savedNumbers = new ArrayList<>();

        /**
         * 保存した集計
         *
         * @since 0.1.0
         */
        private CarryoverSummaryModel savedSummary;

        /**
         * 保存済みの解析結果
         *
         * @since 0.1.0
         */
        private final CarryoverIssuesModel storedIssues = new CarryoverIssuesModel();

        /**
         * 受け取った保存先のディレクトリを返す<br>
         *
         * @since 0.1.0
         *
         * @return 受け取った保存先のディレクトリ
         */
        private Path getDataDir() {

            final Path result = this.dataDir;
            return result;

        }

        /**
         * 保存した Issue 番号を返す<br>
         *
         * @since 0.1.0
         *
         * @return 保存した Issue 番号
         */
        private List<Integer> getSavedNumbers() {

            final List<Integer> result = this.savedNumbers;
            return result;

        }

        /**
         * 保存した集計を返す<br>
         *
         * @since 0.1.0
         *
         * @return 保存した集計。保存していない場合は null
         */
        private CarryoverSummaryModel getSavedSummary() {

            final CarryoverSummaryModel result = this.savedSummary;
            return result;

        }

        /**
         * 保存先のディレクトリを記録する<br>
         *
         * @since 0.1.0
         *
         * @param dataDir
         *                保存先のディレクトリ
         */
        @Override
        public void initialize(@SuppressWarnings("hiding") final Path dataDir) {

            this.dataDir = dataDir;

        }

        /**
         * 保存済みの解析結果を返す<br>
         *
         * @since 0.1.0
         *
         * @return 保存済みの解析結果
         */
        @Override
        public CarryoverIssuesModel loadIssues() {

            final CarryoverIssuesModel result = this.storedIssues.copy();
            return result;

        }

        /**
         * 保存した Issue 番号を記録する<br>
         *
         * @since 0.1.0
         *
         * @param issue
         *              解析結果
         */
        @Override
        public void saveIssue(final CarryoverIssueModel issue) {

            this.savedNumbers.add(issue.getNumber());

        }

        /**
         * 保存した集計を記録する<br>
         *
         * @since 0.1.0
         *
         * @param summary
         *                画面用の集計
         */
        @Override
        public void saveSummary(final CarryoverSummaryModel summary) {

            this.savedSummary = summary;

        }

        /**
         * 保存済みの解析結果を登録する<br>
         *
         * @since 0.1.0
         *
         * @param issue
         *              解析結果
         */
        private void store(final CarryoverIssueModel issue) {

            this.storedIssues.put(issue.getNumber(), issue);

        }

    }

    /**
     * テスト用の日々のタスク Issue の取得<br>
     *
     * @author KenichiroArai
     *
     * @since 0.1.0
     *
     * @version 0.1.0
     */
    private static final class StubDailyTaskIssueLogic implements DailyTaskIssueLogic {

        /**
         * 受け取った対象リポジトリ
         *
         * @since 0.1.0
         */
        private String repository;

        /**
         * 取得する Issue
         *
         * @since 0.1.0
         */
        private final List<DailyTaskIssueModel> remoteIssues;

        /**
         * 受け取ったトークン
         *
         * @since 0.1.0
         */
        private String token;

        /**
         * コンストラクタ<br>
         *
         * @since 0.1.0
         *
         * @param remoteIssues
         *                     取得する Issue
         */
        private StubDailyTaskIssueLogic(final List<DailyTaskIssueModel> remoteIssues) {

            this.remoteIssues = remoteIssues;

        }

        /**
         * 登録した Issue を返す<br>
         *
         * @since 0.1.0
         *
         * @return 登録した Issue
         */
        @Override
        public List<DailyTaskIssueModel> fetchAllIssues() {

            final List<DailyTaskIssueModel> result = this.remoteIssues;
            return result;

        }

        /**
         * 受け取った対象リポジトリを返す<br>
         *
         * @since 0.1.0
         *
         * @return 受け取った対象リポジトリ
         */
        private String getRepository() {

            final String result = this.repository;
            return result;

        }

        /**
         * 受け取ったトークンを返す<br>
         *
         * @since 0.1.0
         *
         * @return 受け取ったトークン
         */
        private String getToken() {

            final String result = this.token;
            return result;

        }

        /**
         * 対象リポジトリとトークンを記録する<br>
         *
         * @since 0.1.0
         *
         * @param repository
         *                   Issue を取得するリポジトリ（owner/name）
         * @param token
         *                   GitHub API のトークン
         */
        @Override
        public void initialize(@SuppressWarnings("hiding") final String repository,
            @SuppressWarnings("hiding") final String token) {

            this.repository = repository;
            this.token = token;

        }

    }

    /**
     * テスト用の標準時間の読み込み<br>
     *
     * @author KenichiroArai
     *
     * @since 0.1.0
     *
     * @version 0.1.0
     */
    private static final class StubDefaultMinutesLogic implements DefaultMinutesLogic {

        /**
         * 受け取った設定ファイル
         *
         * @since 0.1.0
         */
        private Path defaultMinutesFile;

        /**
         * 受け取った設定ファイルを返す<br>
         *
         * @since 0.1.0
         *
         * @return 受け取った設定ファイル
         */
        private Path getDefaultMinutesFile() {

            final Path result = this.defaultMinutesFile;
            return result;

        }

        /**
         * 設定ファイルを記録する<br>
         *
         * @since 0.1.0
         *
         * @param defaultMinutesFile
         *                           標準時間の設定ファイル
         */
        @Override
        public void initialize(@SuppressWarnings("hiding") final Path defaultMinutesFile) {

            this.defaultMinutesFile = defaultMinutesFile;

        }

        /**
         * テスト用の標準時間を返す<br>
         *
         * @since 0.1.0
         *
         * @return テスト用の標準時間
         */
        @Override
        public DefaultMinutesModel loadDefaultMinutes() {

            final DefaultMinutesModel result = CarryoverServiceImplTest.DEFAULT_MINUTES;
            return result;

        }

    }

    /**
     * テスト用の標準時間
     *
     * @since 0.1.0
     */
    private static final DefaultMinutesModel DEFAULT_MINUTES = CarryoverServiceImplTest.defaultMinutes();

    /**
     * テスト用の Issue #1（時間表記なし、更新日時 u1）
     *
     * @since 0.1.0
     */
    private static final DailyTaskIssueModel ISSUE1
        = new DailyTaskIssueModel(1, "2026年03月24日のタスク", "closed", "u1", "## 負債\n- [ ] 国語2026/03/20");

    /**
     * テスト用の Issue #2（時間表記あり、更新日時 u2）
     *
     * @since 0.1.0
     */
    private static final DailyTaskIssueModel ISSUE2
        = new DailyTaskIssueModel(2, "2026年03月25日のタスク", "open", "u2", "## 負債\n- [ ] 英語2026/03/20（残り時間：10分）");

    /**
     * テスト用の Issue 本文の解析
     *
     * @since 0.1.0
     */
    private static final CarryoverParseLogic PARSE_LOGIC = CarryoverServiceImplTest.createParseLogic();

    /**
     * テスト用の持ち越しの収集の設定
     *
     * @since 0.1.0
     */
    private static final CarryoverPropertiesModel PROPERTIES
        = new CarryoverPropertiesModel("owner/repo", "test-token", "docs/data", "config/default-minutes.json", 10);

    /**
     * private の countBySource メソッドを呼び出す<br>
     *
     * @since 0.1.0
     *
     * @param issues
     *               解析結果
     * @param source
     *               残り時間の取得元
     *
     * @return 行数
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    private static long countBySource(final Collection<CarryoverIssueModel> issues, final MinutesSourceTypes source)
        throws Exception {

        final long result
            = ReflectionTestUtil.<Long>invokeStatic(CarryoverServiceImpl.class, "countBySource", new Class<?>[] {
                Collection.class, MinutesSourceTypes.class
            }, issues, source);
        return result;

    }

    /**
     * テスト用の標準時間を作る<br>
     *
     * @since 0.1.0
     *
     * @return 項目ごとの標準時間
     */
    private static DefaultMinutesModel defaultMinutes() {

        final DefaultMinutesModel result = new DefaultMinutesModel();
        result.add("国語", 15.0);
        return result;

    }

    /**
     * #1 から指定件数までの Issue を作成する<br>
     * <p>
     * Issue #N の日付は 2026年03月N日、更新日時は uN とする。
     * </p>
     *
     * @since 0.1.0
     *
     * @param count
     *              件数
     *
     * @return 作成した Issue
     */
    private static List<DailyTaskIssueModel> createIssues(final int count) {

        final List<DailyTaskIssueModel> result
            = IntStream.rangeClosed(1, count)
                .mapToObj(number -> new DailyTaskIssueModel(number,
                    String.format("2026年03月%02d日のタスク", Integer.valueOf(number)), "closed", "u" + number,
                    "## 負債\n- [ ] 英語2026/03/01（残り時間：10分）"))
                .toList();
        return result;

    }

    /**
     * 解析ロジックを作成する<br>
     *
     * @since 0.1.0
     *
     * @return 解析ロジック
     */
    private static CarryoverParseLogic createParseLogic() {

        final CarryoverParseLogicImpl result = new CarryoverParseLogicImpl(MessageLogicTestUtil.create());
        return result;

    }

    /**
     * テスト対象を作成する<br>
     *
     * @since 0.1.0
     *
     * @param issueLogic
     *                     日々のタスク Issue の取得
     * @param minutesLogic
     *                     標準時間の読み込み
     * @param dataLogic
     *                     解析結果と集計の読み書き
     *
     * @return テスト対象
     */
    private static CarryoverServiceImpl createTarget(final DailyTaskIssueLogic issueLogic,
        final DefaultMinutesLogic minutesLogic, final CarryoverDataLogic dataLogic) {

        final CarryoverCollectServiceImpl   collectService   = new CarryoverCollectServiceImpl(dataLogic,
            CarryoverServiceImplTest.PARSE_LOGIC, issueLogic, minutesLogic);
        final CarryoverAggregateServiceImpl aggregateService = new CarryoverAggregateServiceImpl(
            new CarryoverAggregateLogicImpl(), dataLogic);
        final CarryoverServiceImpl          result           = new CarryoverServiceImpl(aggregateService,
            collectService, MessageLogicTestUtil.create());
        return result;

    }

    /**
     * テスト用の標準時間で Issue を解析する<br>
     *
     * @since 0.1.0
     *
     * @param issue
     *              日々のタスク Issue
     *
     * @return 持ち越しの解析結果
     */
    private static CarryoverIssueModel parse(final DailyTaskIssueModel issue) {

        CarryoverServiceImplTest.PARSE_LOGIC.initialize(CarryoverServiceImplTest.DEFAULT_MINUTES);
        final CarryoverIssueModel result = CarryoverServiceImplTest.PARSE_LOGIC.parse(issue);
        return result;

    }

    /**
     * collect メソッドのテスト - 正常系:差分モードの場合は最新 10 件より前で更新日時が同じ保存済みの Issue を解析しない
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testCollect_normalDiff() throws IOException {

        /* 期待値の定義 */
        final int      expectedCount = 10;
        final String[] expectedMsgs  = {
            "モード=差分、取得=12 件、解析・保存=10 件、集計=12 日", "標準時間で補完した行=0 件、標準時間が未登録の行=0 件",
        };

        /* 準備 */
        final List<DailyTaskIssueModel> testIssues     = CarryoverServiceImplTest.createIssues(12);
        final StubDailyTaskIssueLogic   testIssueLogic = new StubDailyTaskIssueLogic(testIssues);
        final StubCarryoverDataLogic    testDataLogic  = new StubCarryoverDataLogic();
        testDataLogic.store(CarryoverServiceImplTest.parse(testIssues.get(0)));
        testDataLogic.store(CarryoverServiceImplTest.parse(testIssues.get(1)));
        final CarryoverServiceImpl testTarget
            = CarryoverServiceImplTest.createTarget(testIssueLogic, new StubDefaultMinutesLogic(), testDataLogic);

        /* テスト対象の実行 */
        try (LogCapture testLog = new LogCapture(CarryoverServiceImpl.class)) {

            testTarget.initialize(CarryoverServiceImplTest.PROPERTIES, false);
            final int testResult = testTarget.collect();
            testTarget.aggregate();

            /* 検証の準備 */
            final String[] actualMsgs  = testLog.getMessages();
            final int      actualCount = testResult;

            /* 検証の実施 */
            Assertions.assertEquals(expectedCount, actualCount, "解析件数が一致しません");
            LogAssertions.assertMessages(expectedMsgs, actualMsgs);

        }

    }

    /**
     * collect メソッドのテスト - 正常系:差分モードの場合は最新 10 件の保存済みの Issue を更新日時が同じでも解析し直す
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testCollect_normalDiffRecent() throws IOException {

        /* 期待値の定義 */
        final int      expectedCount = 2;
        final String[] expectedMsgs  = {
            "モード=差分、取得=2 件、解析・保存=2 件、集計=2 日", "標準時間で補完した行=1 件、標準時間が未登録の行=0 件",
        };

        /* 準備 */
        final StubDailyTaskIssueLogic testIssueLogic = new StubDailyTaskIssueLogic(
            List.of(CarryoverServiceImplTest.ISSUE1, CarryoverServiceImplTest.ISSUE2));
        final StubCarryoverDataLogic  testDataLogic  = new StubCarryoverDataLogic();
        testDataLogic.store(CarryoverServiceImplTest.parse(CarryoverServiceImplTest.ISSUE1));
        final CarryoverServiceImpl testTarget
            = CarryoverServiceImplTest.createTarget(testIssueLogic, new StubDefaultMinutesLogic(), testDataLogic);

        /* テスト対象の実行 */
        try (LogCapture testLog = new LogCapture(CarryoverServiceImpl.class)) {

            testTarget.initialize(CarryoverServiceImplTest.PROPERTIES, false);
            final int testResult = testTarget.collect();
            testTarget.aggregate();

            /* 検証の準備 */
            final String[] actualMsgs  = testLog.getMessages();
            final int      actualCount = testResult;

            /* 検証の実施 */
            Assertions.assertEquals(expectedCount, actualCount, "解析件数が一致しません");
            LogAssertions.assertMessages(expectedMsgs, actualMsgs);

        }

    }

    /**
     * collect メソッドのテスト - 正常系:全件モードの場合はすべての Issue を解析する
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testCollect_normalFull() throws IOException {

        /* 期待値の定義 */
        final int           expectedCount        = 2;
        final List<Integer> expectedSavedNumbers = List.of(1, 2);
        final int           expectedDays         = 2;
        final String[]      expectedMsgs         = {
            "モード=全件、取得=2 件、解析・保存=2 件、集計=2 日", "標準時間で補完した行=1 件、標準時間が未登録の行=0 件",
        };

        /* 準備 */
        final StubDailyTaskIssueLogic testIssueLogic = new StubDailyTaskIssueLogic(
            List.of(CarryoverServiceImplTest.ISSUE1, CarryoverServiceImplTest.ISSUE2));
        final StubCarryoverDataLogic  testDataLogic  = new StubCarryoverDataLogic();
        testDataLogic.store(CarryoverServiceImplTest.parse(CarryoverServiceImplTest.ISSUE1));
        final CarryoverServiceImpl testTarget
            = CarryoverServiceImplTest.createTarget(testIssueLogic, new StubDefaultMinutesLogic(), testDataLogic);

        /* テスト対象の実行 */
        try (LogCapture testLog = new LogCapture(CarryoverServiceImpl.class)) {

            testTarget.initialize(CarryoverServiceImplTest.PROPERTIES, true);
            final int testResult = testTarget.collect();
            testTarget.aggregate();

            /* 検証の準備 */
            final String[]      actualMsgs         = testLog.getMessages();
            final int           actualCount        = testResult;
            final List<Integer> actualSavedNumbers = testDataLogic.getSavedNumbers();
            final int           actualDays         = testDataLogic.getSavedSummary().getDays().size();

            /* 検証の実施 */
            Assertions.assertEquals(expectedCount, actualCount, "解析件数が一致しません");
            Assertions.assertEquals(expectedSavedNumbers, actualSavedNumbers, "保存した Issue 番号が一致しません");
            Assertions.assertEquals(expectedDays, actualDays, "集計の日数が一致しません");
            LogAssertions.assertMessages(expectedMsgs, actualMsgs);

        }

    }

    /**
     * collect と aggregate のテスト - 正常系:取得、解析、保存、集計、集計の保存の順で呼ぶ場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testCollect_normalOrder() throws IOException {

        /* 期待値の定義 */
        final List<String> expectedSteps = List.of("collect.initialize", "aggregate.initialize", "fetch", "analyze",
            "saveIssues", "aggregate", "saveSummary");

        /* 準備 */
        final List<String>         testSteps  = new ArrayList<>();
        final CarryoverServiceImpl testTarget = new CarryoverServiceImpl(
            new CarryoverServiceImplTest.RecordingAggregateService(testSteps),
            new CarryoverServiceImplTest.RecordingCollectService(testSteps), MessageLogicTestUtil.create());

        /* テスト対象の実行 */
        testTarget.initialize(CarryoverServiceImplTest.PROPERTIES, false);
        testTarget.collect();
        testTarget.aggregate();

        /* 検証の準備 */
        final List<String> actualSteps = testSteps;

        /* 検証の実施 */
        Assertions.assertEquals(expectedSteps, actualSteps, "呼び出しの順が一致しません");

    }

    /**
     * collect メソッドのテスト - 正常系:設定を役割ごとのロジックが必要とする値に分けて渡す場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testCollect_normalSource() throws IOException {

        /* 期待値の定義 */
        final String expectedRepository  = "owner/repo";
        final String expectedToken       = "test-token";
        final Path   expectedMinutesFile = Path.of("config", "default-minutes.json");
        final Path   expectedDataDir     = Path.of("docs", "data");

        /* 準備 */
        final StubDailyTaskIssueLogic testIssueLogic   = new StubDailyTaskIssueLogic(
            List.of(CarryoverServiceImplTest.ISSUE1));
        final StubDefaultMinutesLogic testMinutesLogic = new StubDefaultMinutesLogic();
        final StubCarryoverDataLogic  testDataLogic    = new StubCarryoverDataLogic();
        final CarryoverServiceImpl    testTarget       = CarryoverServiceImplTest.createTarget(testIssueLogic,
            testMinutesLogic, testDataLogic);

        /* テスト対象の実行 */
        testTarget.initialize(CarryoverServiceImplTest.PROPERTIES, true);
        testTarget.collect();

        /* 検証の準備 */
        final String actualRepository  = testIssueLogic.getRepository();
        final String actualToken       = testIssueLogic.getToken();
        final Path   actualMinutesFile = testMinutesLogic.getDefaultMinutesFile();
        final Path   actualDataDir     = testDataLogic.getDataDir();

        /* 検証の実施 */
        Assertions.assertEquals(expectedRepository, actualRepository, "リポジトリが一致しません");
        Assertions.assertEquals(expectedToken, actualToken, "トークンが一致しません");
        Assertions.assertEquals(expectedMinutesFile, actualMinutesFile, "標準時間の設定ファイルが一致しません");
        Assertions.assertEquals(expectedDataDir, actualDataDir, "保存先が一致しません");

    }

    /**
     * countBySource メソッドのテスト - 正常系:取得元ごとの行数を数える場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testCountBySource_normalCount() throws Exception {

        /* 期待値の定義 */
        final long expectedCount = 1;

        /* 準備 */
        final CarryoverIssuesModel testIssues = new CarryoverIssuesModel();
        testIssues.put(1, CarryoverServiceImplTest.parse(CarryoverServiceImplTest.ISSUE1));
        testIssues.put(2, CarryoverServiceImplTest.parse(CarryoverServiceImplTest.ISSUE2));

        /* テスト対象の実行 */
        final long testResult = CarryoverServiceImplTest.countBySource(testIssues.values(), MinutesSourceTypes.PARSED);

        /* 検証の準備 */
        final long actualCount = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedCount, actualCount, "行数が一致しません");

    }

}
