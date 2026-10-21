package kmg.gr.gr002.dailytasks.carryover.domain.logic.impl;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverItemModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.types.MinutesSourceTypes;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverIssueDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverItemDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverSummaryDto;
import kmg.gr.gr002.dailytasks.carryover.domain.repository.dao.CarryoverDataDao;
import kmg.gr.gr002.dailytasks.testutil.MessageLogicTestUtil;

/**
 * {@link CarryoverDataLogicImpl} のテスト<br>
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
public class CarryoverDataLogicImplTest {

    /**
     * テスト用の JSON ファイルの読み書き<br>
     *
     * @author KenichiroArai
     *
     * @since 0.1.0
     *
     * @version 0.1.0
     */
    private static final class StubCarryoverDataDao implements CarryoverDataDao {

        /**
         * 最後に受け取った出力先のディレクトリ
         *
         * @since 0.1.0
         */
        private Path dataDir;

        /**
         * 読み込み結果として返す解析結果
         *
         * @since 0.1.0
         */
        private final Map<Integer, CarryoverIssueDto> issues;

        /**
         * 保存された解析結果
         *
         * @since 0.1.0
         */
        private final List<CarryoverIssueDto> savedIssues = new ArrayList<>();

        /**
         * 保存された集計
         *
         * @since 0.1.0
         */
        private CarryoverSummaryDto savedSummary;

        /**
         * コンストラクタ<br>
         *
         * @since 0.1.0
         *
         * @param issues
         *               読み込み結果として返す解析結果
         */
        private StubCarryoverDataDao(final Map<Integer, CarryoverIssueDto> issues) {

            this.issues = issues;

        }

        /**
         * 最後に受け取った出力先のディレクトリを返す<br>
         *
         * @since 0.1.0
         *
         * @return 最後に受け取った出力先のディレクトリ
         */
        private Path getDataDir() {

            final Path result = this.dataDir;
            return result;

        }

        /**
         * 保存された解析結果を返す<br>
         *
         * @since 0.1.0
         *
         * @return 保存された解析結果
         */
        private List<CarryoverIssueDto> getSavedIssues() {

            final List<CarryoverIssueDto> result = this.savedIssues;
            return result;

        }

        /**
         * 保存された集計を返す<br>
         *
         * @since 0.1.0
         *
         * @return 保存された集計
         */
        private CarryoverSummaryDto getSavedSummary() {

            final CarryoverSummaryDto result = this.savedSummary;
            return result;

        }

        /**
         * 出力先のディレクトリを記録する<br>
         *
         * @since 0.1.0
         *
         * @param targetDir
         *                  出力先のディレクトリ
         */
        @Override
        public void initialize(final Path targetDir) {

            this.dataDir = targetDir;

        }

        /**
         * 設定した解析結果を返す<br>
         *
         * @since 0.1.0
         *
         * @return 設定した解析結果
         */
        @Override
        public Map<Integer, CarryoverIssueDto> loadIssues() {

            final Map<Integer, CarryoverIssueDto> result = this.issues;
            return result;

        }

        /**
         * 解析結果を記録する<br>
         *
         * @since 0.1.0
         *
         * @param issue
         *              解析結果
         */
        @Override
        public void saveIssue(final CarryoverIssueDto issue) {

            this.savedIssues.add(issue);

        }

        /**
         * 集計を記録する<br>
         *
         * @since 0.1.0
         *
         * @param summary
         *                集計
         */
        @Override
        public void saveSummary(final CarryoverSummaryDto summary) {

            this.savedSummary = summary;

        }

    }

    /**
     * テスト対象を作成する<br>
     *
     * @since 0.1.0
     *
     * @param carryoverDataDao
     *                         JSON ファイルの読み書き
     *
     * @return テスト対象
     */
    private static CarryoverDataLogicImpl createTarget(final CarryoverDataDao carryoverDataDao) {

        final CarryoverDataLogicImpl result
            = new CarryoverDataLogicImpl(carryoverDataDao, MessageLogicTestUtil.create());
        return result;

    }

    /**
     * loadIssues メソッドのテスト - 正常系:保存済みの解析結果をモデルに変換する場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   読み込みに失敗した場合
     */
    @Test
    public void testLoadIssues_normalConvert() throws Exception {

        /* 期待値の定義 */
        final MinutesSourceTypes expectedMinutesSource = MinutesSourceTypes.UNKNOWN;
        final Path               expectedDataDir       = Path.of("docs", "data");

        /* 準備 */
        final CarryoverItemDto     testItem   = new CarryoverItemDto("国語", "2026-06-18", false, 0.0, "unknown",
            "持ち越し", "- [ ] 国語2026/06/18");
        final CarryoverIssueDto    testDto    = new CarryoverIssueDto(371, "2026年10月06日のタスク", "2026-10-06", "open",
            "2026-10-06T14:23:08Z", List.of("持ち越し"), null, 1, 0.0, List.of(testItem));
        final StubCarryoverDataDao testData   = new StubCarryoverDataDao(Map.of(371, testDto));
        final CarryoverDataLogicImpl testTarget = CarryoverDataLogicImplTest.createTarget(testData);

        /* テスト対象の実行 */
        testTarget.initialize(Path.of("docs", "data"));
        final Map<Integer, CarryoverIssueModel> testResult = testTarget.loadIssues();

        /* 検証の準備 */
        final MinutesSourceTypes actualMinutesSource = testResult.get(371).getItems().get(0).getMinutesSource();
        final Path               actualDataDir       = testData.getDataDir();

        /* 検証の実施 */
        Assertions.assertEquals(expectedMinutesSource, actualMinutesSource, "残り時間の取得元が一致しません");
        Assertions.assertEquals(expectedDataDir, actualDataDir, "出力先のディレクトリが一致しません");

    }

    /**
     * saveIssue メソッドのテスト - 正常系:解析結果を DTO に変換して保存する場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   書き込みに失敗した場合
     */
    @Test
    public void testSaveIssue_normalConvert() throws Exception {

        /* 期待値の定義 */
        final String expectedMinutesSource = "parsed";
        final Path   expectedDataDir       = Path.of("docs", "data");

        /* 準備 */
        final StubCarryoverDataDao   testData   = new StubCarryoverDataDao(Map.of());
        final CarryoverDataLogicImpl testTarget = CarryoverDataLogicImplTest.createTarget(testData);
        final CarryoverItemModel     testItem   = new CarryoverItemModel("音楽", "2026-08-18", false, 11.5,
            MinutesSourceTypes.PARSED, "持ち越し", "- [ ] 音楽2026/08/18（残り時間：11.5分）");
        final CarryoverIssueModel    testIssue  = new CarryoverIssueModel(371, "2026年10月06日のタスク", "2026-10-06", "open",
            "2026-10-06T14:23:08Z", List.of("持ち越し"), 1, List.of(testItem));

        /* テスト対象の実行 */
        testTarget.initialize(Path.of("docs", "data"));
        testTarget.saveIssue(testIssue);

        /* 検証の準備 */
        final String actualMinutesSource = testData.getSavedIssues().get(0).getItems().get(0).getMinutesSource();
        final Path   actualDataDir       = testData.getDataDir();

        /* 検証の実施 */
        Assertions.assertEquals(expectedMinutesSource, actualMinutesSource, "残り時間の取得元が一致しません");
        Assertions.assertEquals(expectedDataDir, actualDataDir, "出力先のディレクトリが一致しません");

    }

    /**
     * saveSummary メソッドのテスト - 正常系:集計を DTO に変換して保存する場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   書き込みに失敗した場合
     */
    @Test
    public void testSaveSummary_normalConvert() throws Exception {

        /* 期待値の定義 */
        final int  expectedLatestIssue = 371;
        final Path expectedDataDir     = Path.of("docs", "data");

        /* 準備 */
        final StubCarryoverDataDao   testData    = new StubCarryoverDataDao(Map.of());
        final CarryoverDataLogicImpl testTarget  = CarryoverDataLogicImplTest.createTarget(testData);
        final CarryoverSummaryModel  testSummary = new CarryoverSummaryModel(371, List.of(), List.of());

        /* テスト対象の実行 */
        testTarget.initialize(Path.of("docs", "data"));
        testTarget.saveSummary(testSummary);

        /* 検証の準備 */
        final int  actualLatestIssue = testData.getSavedSummary().getLatestIssue();
        final Path actualDataDir     = testData.getDataDir();

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");
        Assertions.assertEquals(expectedDataDir, actualDataDir, "出力先のディレクトリが一致しません");

    }

}
