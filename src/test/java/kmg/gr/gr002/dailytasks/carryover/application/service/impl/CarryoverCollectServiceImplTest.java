package kmg.gr.gr002.dailytasks.carryover.application.service.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverDataLogic;
import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverParseLogic;
import kmg.gr.gr002.dailytasks.carryover.application.logic.DailyTaskIssueLogic;
import kmg.gr.gr002.dailytasks.carryover.application.logic.DefaultMinutesLogic;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSourceModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DefaultMinutesModel;
import kmg.gr.gr002.dailytasks.carryover.domain.logic.impl.CarryoverParseLogicImpl;
import kmg.gr.gr002.dailytasks.carryover.presentation.model.CarryoverPropertiesModel;
import kmg.gr.gr002.dailytasks.testutil.MessageLogicTestUtil;
import kmg.gr.gr002.dailytasks.testutil.ReflectionTestUtil;

/**
 * {@link CarryoverCollectServiceImpl} のテスト<br>
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
public class CarryoverCollectServiceImplTest {

    /**
     * テスト用の解析結果の読み書き<br>
     *
     * @author KenichiroArai
     *
     * @since 0.1.0
     *
     * @version 0.1.0
     */
    private static final class StubCarryoverDataLogic implements CarryoverDataLogic {

        /**
         * 保存した Issue 番号
         *
         * @since 0.1.0
         */
        private final List<Integer> savedNumbers = new ArrayList<>();

        /**
         * 保存済みの解析結果
         *
         * @since 0.1.0
         */
        private final Map<Integer, CarryoverIssueModel> storedIssues = new TreeMap<>();

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
         * 保存先のディレクトリを受け取る<br>
         *
         * @since 0.1.0
         *
         * @param dataDir
         *                保存先のディレクトリ
         */
        @Override
        public void initialize(final Path dataDir) {

            /* 保存先は収集サービスの初期化で渡される。このテストでは使わない */

        }

        /**
         * 保存済みの解析結果を返す<br>
         *
         * @since 0.1.0
         *
         * @return 保存済みの解析結果
         */
        @Override
        public Map<Integer, CarryoverIssueModel> loadIssues() {

            final Map<Integer, CarryoverIssueModel> result = new TreeMap<>(this.storedIssues);
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
         * 集計は保存しない<br>
         *
         * @since 0.1.0
         *
         * @param summary
         *                画面用の集計
         */
        @Override
        public void saveSummary(final CarryoverSummaryModel summary) {

            /* 収集サービスは集計を保存しない */

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
         * 取得する Issue
         *
         * @since 0.1.0
         */
        private final List<DailyTaskIssueModel> remoteIssues;

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
         * 対象リポジトリとトークンを受け取る<br>
         *
         * @since 0.1.0
         *
         * @param repository
         *                   Issue を取得するリポジトリ（owner/name）
         * @param token
         *                   GitHub API のトークン
         */
        @Override
        public void initialize(final String repository, final String token) {

            /* 取得先は収集サービスの初期化で渡される。このテストでは使わない */

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
         * 設定ファイルを受け取る<br>
         *
         * @since 0.1.0
         *
         * @param defaultMinutesFile
         *                           標準時間の設定ファイル
         */
        @Override
        public void initialize(final Path defaultMinutesFile) {

            /* 設定ファイルは収集サービスの初期化で渡される。このテストでは使わない */

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

            final DefaultMinutesModel result = CarryoverCollectServiceImplTest.DEFAULT_MINUTES;
            return result;

        }

    }

    /**
     * テスト用の標準時間
     *
     * @since 0.1.0
     */
    private static final DefaultMinutesModel DEFAULT_MINUTES = new DefaultMinutesModel(Map.of("国語", (double) 15));

    /**
     * テスト用の Issue #1（時間表記なし、更新日時 u1）
     *
     * @since 0.1.0
     */
    private static final DailyTaskIssueModel ISSUE1
        = new DailyTaskIssueModel(1, "2026年03月24日のタスク", "closed", "u1", "## 負債\n- [ ] 国語2026/03/20");

    /**
     * テスト用の Issue 本文の解析
     *
     * @since 0.1.0
     */
    private static final CarryoverParseLogic PARSE_LOGIC = CarryoverCollectServiceImplTest.createParseLogic();

    /**
     * テスト用の持ち越しの収集の設定
     *
     * @since 0.1.0
     */
    private static final CarryoverPropertiesModel PROPERTIES
        = new CarryoverPropertiesModel("owner/repo", "test-token", "docs/data", "config/default-minutes.json", 10);

    /**
     * #1 から指定件数までの Issue を作成する<br>
     *
     * @since 0.1.0
     *
     * @param count
     *              件数
     *
     * @return 作成した Issue
     */
    private static List<DailyTaskIssueModel> createIssues(final int count) {

        final List<DailyTaskIssueModel> result = IntStream.rangeClosed(1, count)
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
     *                   日々のタスク Issue の取得
     * @param dataLogic
     *                   解析結果の読み書き
     *
     * @return テスト対象
     */
    private static CarryoverCollectServiceImpl createTarget(final DailyTaskIssueLogic issueLogic,
        final CarryoverDataLogic dataLogic) {

        final CarryoverCollectServiceImpl result = new CarryoverCollectServiceImpl(dataLogic,
            CarryoverCollectServiceImplTest.PARSE_LOGIC, issueLogic, new StubDefaultMinutesLogic());
        return result;

    }

    /**
     * private の needsUpdate メソッドを呼び出す<br>
     *
     * @since 0.1.0
     *
     * @param full
     *                    全件モードか
     * @param stored
     *                    保存済みの解析結果
     * @param remoteIssue
     *                    取得した Issue
     * @param recentFrom
     *                    毎回解析し直す Issue 番号の下限
     *
     * @return 解析し直す場合は true
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    private static boolean needsUpdate(final boolean full, final CarryoverIssueModel stored,
        final DailyTaskIssueModel remoteIssue, final int recentFrom) throws Exception {

        final CarryoverCollectServiceImpl target = new CarryoverCollectServiceImpl(null, null, null, null);
        ReflectionTestUtil.setField(target, "full", Boolean.valueOf(full));
        final boolean result = ReflectionTestUtil.<Boolean>invoke(target, "needsUpdate", new Class<?>[] {
            CarryoverIssueModel.class, DailyTaskIssueModel.class, int.class
        }, stored, remoteIssue, Integer.valueOf(recentFrom));
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

        CarryoverCollectServiceImplTest.PARSE_LOGIC.initialize(CarryoverCollectServiceImplTest.DEFAULT_MINUTES);
        final CarryoverIssueModel result = CarryoverCollectServiceImplTest.PARSE_LOGIC.parse(issue);
        return result;

    }

    /**
     * private の recentFrom メソッドを呼び出す<br>
     *
     * @since 0.1.0
     *
     * @param issues
     *               取得した Issue
     * @param count
     *               件数
     *
     * @return 毎回解析し直す Issue 番号の下限
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    private static int recentFrom(final List<DailyTaskIssueModel> issues, final int count) throws Exception {

        final int result = ReflectionTestUtil.<Integer>invokeStatic(CarryoverCollectServiceImpl.class, "recentFrom",
            new Class<?>[] {
                List.class, int.class
            }, issues, Integer.valueOf(count));
        return result;

    }

    /**
     * analyze メソッドのテスト - 正常系:解析の時点では保存しない場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testAnalyze_normalDoesNotSave() throws IOException {

        /* 期待値の定義 */
        final List<Integer> expectedSavedNumbers = List.of();
        final int           expectedCount        = 1;

        /* 準備 */
        final CarryoverCollectServiceImplTest.StubCarryoverDataLogic testDataLogic
            = new CarryoverCollectServiceImplTest.StubCarryoverDataLogic();
        final CarryoverCollectServiceImpl testTarget = CarryoverCollectServiceImplTest.createTarget(
            new CarryoverCollectServiceImplTest.StubDailyTaskIssueLogic(List.of(CarryoverCollectServiceImplTest.ISSUE1)),
            testDataLogic);

        /* テスト対象の実行 */
        testTarget.initialize(new CarryoverSourceModel(CarryoverCollectServiceImplTest.PROPERTIES), 10, true);
        testTarget.fetch();
        final int testResult = testTarget.analyze();

        /* 検証の準備 */
        final int           actualCount        = testResult;
        final List<Integer> actualSavedNumbers = testDataLogic.getSavedNumbers();

        /* 検証の実施 */
        Assertions.assertEquals(expectedCount, actualCount, "解析件数が一致しません");
        Assertions.assertEquals(expectedSavedNumbers, actualSavedNumbers, "解析の時点では保存しない必要があります");

    }

    /**
     * needsUpdate メソッドのテスト - 正常系:全件モードの場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testNeedsUpdate_normalFull() throws Exception {

        /* 期待値の定義 */

        /* 準備 */
        final CarryoverIssueModel testStored = CarryoverCollectServiceImplTest.parse(CarryoverCollectServiceImplTest.ISSUE1);

        /* テスト対象の実行 */
        final boolean testResult = CarryoverCollectServiceImplTest.needsUpdate(true, testStored,
            CarryoverCollectServiceImplTest.ISSUE1, 2);

        /* 検証の準備 */
        final boolean actualNeedsUpdate = testResult;

        /* 検証の実施 */
        Assertions.assertTrue(actualNeedsUpdate, "解析し直すと判定される必要があります");

    }

    /**
     * needsUpdate メソッドのテスト - 正常系:未保存の場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testNeedsUpdate_normalNotStored() throws Exception {

        /* 期待値の定義 */

        /* 準備 */

        /* テスト対象の実行 */
        final boolean testResult = CarryoverCollectServiceImplTest.needsUpdate(false, null,
            CarryoverCollectServiceImplTest.ISSUE1, 2);

        /* 検証の準備 */
        final boolean actualNeedsUpdate = testResult;

        /* 検証の実施 */
        Assertions.assertTrue(actualNeedsUpdate, "解析し直すと判定される必要があります");

    }

    /**
     * needsUpdate メソッドのテスト - 正常系:最新の Issue の場合は更新日時が同じでも解析し直す
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testNeedsUpdate_normalRecent() throws Exception {

        /* 期待値の定義 */

        /* 準備 */
        final CarryoverIssueModel testStored = CarryoverCollectServiceImplTest.parse(CarryoverCollectServiceImplTest.ISSUE1);

        /* テスト対象の実行 */
        final boolean testResult = CarryoverCollectServiceImplTest.needsUpdate(false, testStored,
            CarryoverCollectServiceImplTest.ISSUE1, 1);

        /* 検証の準備 */
        final boolean actualNeedsUpdate = testResult;

        /* 検証の実施 */
        Assertions.assertTrue(actualNeedsUpdate, "解析し直すと判定される必要があります");

    }

    /**
     * needsUpdate メソッドのテスト - 正常系:更新日時が変わった場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testNeedsUpdate_normalUpdated() throws Exception {

        /* 期待値の定義 */

        /* 準備 */
        final CarryoverIssueModel testStored = CarryoverCollectServiceImplTest.parse(CarryoverCollectServiceImplTest.ISSUE1);
        final DailyTaskIssueModel testRemote = new DailyTaskIssueModel(1, "2026年03月24日のタスク", "closed", "u9", "");

        /* テスト対象の実行 */
        final boolean testResult = CarryoverCollectServiceImplTest.needsUpdate(false, testStored, testRemote, 2);

        /* 検証の準備 */
        final boolean actualNeedsUpdate = testResult;

        /* 検証の実施 */
        Assertions.assertTrue(actualNeedsUpdate, "解析し直すと判定される必要があります");

    }

    /**
     * needsUpdate メソッドのテスト - 準正常系:更新日時が同じ場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testNeedsUpdate_semiNotUpdated() throws Exception {

        /* 期待値の定義 */

        /* 準備 */
        final CarryoverIssueModel testStored = CarryoverCollectServiceImplTest.parse(CarryoverCollectServiceImplTest.ISSUE1);

        /* テスト対象の実行 */
        final boolean testResult = CarryoverCollectServiceImplTest.needsUpdate(false, testStored,
            CarryoverCollectServiceImplTest.ISSUE1, 2);

        /* 検証の準備 */
        final boolean actualNeedsUpdate = testResult;

        /* 検証の実施 */
        Assertions.assertFalse(actualNeedsUpdate, "解析し直さないと判定される必要があります");

    }

    /**
     * recentFrom メソッドのテスト - 正常系:件数より少ない Issue しかない場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testRecentFrom_normalLessThanCount() throws Exception {

        /* 期待値の定義 */
        final int expectedRecentFrom = 1;

        /* 準備 */
        final List<DailyTaskIssueModel> testIssues = List.of(new DailyTaskIssueModel(2, "2026年03月25日のタスク", "open",
            "u2", ""), CarryoverCollectServiceImplTest.ISSUE1);

        /* テスト対象の実行 */
        final int testResult = CarryoverCollectServiceImplTest.recentFrom(testIssues, 10);

        /* 検証の準備 */
        final int actualRecentFrom = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedRecentFrom, actualRecentFrom, "Issue 番号の下限が一致しません");

    }

    /**
     * recentFrom メソッドのテスト - 正常系:件数より多い Issue がある場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testRecentFrom_normalMoreThanCount() throws Exception {

        /* 期待値の定義 */
        final int expectedRecentFrom = 3;

        /* 準備 */
        final List<DailyTaskIssueModel> testIssues = CarryoverCollectServiceImplTest.createIssues(12);

        /* テスト対象の実行 */
        final int testResult = CarryoverCollectServiceImplTest.recentFrom(testIssues, 10);

        /* 検証の準備 */
        final int actualRecentFrom = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedRecentFrom, actualRecentFrom, "Issue 番号の下限が一致しません");

    }

    /**
     * recentFrom メソッドのテスト - 準正常系:Issue がない場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testRecentFrom_semiEmpty() throws Exception {

        /* 期待値の定義 */
        final int expectedRecentFrom = 0;

        /* 準備 */
        final List<DailyTaskIssueModel> testIssues = List.of();

        /* テスト対象の実行 */
        final int testResult = CarryoverCollectServiceImplTest.recentFrom(testIssues, 10);

        /* 検証の準備 */
        final int actualRecentFrom = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedRecentFrom, actualRecentFrom, "Issue 番号の下限が一致しません");

    }

    /**
     * saveIssues メソッドのテスト - 正常系:解析した Issue を保存する場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testSaveIssues_normalSavesParsed() throws IOException {

        /* 期待値の定義 */
        final List<Integer> expectedSavedNumbers = List.of(Integer.valueOf(1));

        /* 準備 */
        final CarryoverCollectServiceImplTest.StubCarryoverDataLogic testDataLogic
            = new CarryoverCollectServiceImplTest.StubCarryoverDataLogic();
        final CarryoverCollectServiceImpl testTarget = CarryoverCollectServiceImplTest.createTarget(
            new CarryoverCollectServiceImplTest.StubDailyTaskIssueLogic(List.of(CarryoverCollectServiceImplTest.ISSUE1)),
            testDataLogic);
        testTarget.initialize(new CarryoverSourceModel(CarryoverCollectServiceImplTest.PROPERTIES), 10, true);
        testTarget.fetch();
        testTarget.analyze();

        /* テスト対象の実行 */
        testTarget.saveIssues();

        /* 検証の準備 */
        final List<Integer> actualSavedNumbers = testDataLogic.getSavedNumbers();

        /* 検証の実施 */
        Assertions.assertEquals(expectedSavedNumbers, actualSavedNumbers, "保存した Issue 番号が一致しません");

    }

}
