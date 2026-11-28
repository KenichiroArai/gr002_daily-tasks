package kmg.gr.gr002.dailytasks.advance.domain.logic.impl;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.advance.domain.repository.dao.AdvanceDataDao;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverSummaryDto;

/**
 * {@link AdvanceDataLogicImpl} のテスト<br>
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
public class AdvanceDataLogicImplTest {

    /**
     * テスト用の JSON ファイルの読み書き<br>
     *
     * @author KenichiroArai
     *
     * @since 0.2.0
     *
     * @version 0.2.0
     */
    private static final class StubAdvanceDataDao implements AdvanceDataDao {

        /**
         * 最後に受け取った出力先のディレクトリ
         *
         * @since 0.2.0
         */
        private Path dataDir;

        /**
         * 保存された集計
         *
         * @since 0.2.0
         */
        private CarryoverSummaryDto savedSummary;

        /**
         * 出力先のディレクトリを記録する<br>
         *
         * @since 0.2.0
         *
         * @param targetDir
         *                  出力先のディレクトリ
         */
        @Override
        public void initialize(final Path targetDir) {

            this.dataDir = targetDir;

        }

        /**
         * 集計を記録する<br>
         *
         * @since 0.2.0
         *
         * @param summary
         *                集計
         */
        @Override
        public void saveSummary(final CarryoverSummaryDto summary) throws IOException {

            this.savedSummary = summary;

        }

        /**
         * 最後に受け取った出力先のディレクトリを返す<br>
         *
         * @since 0.2.0
         *
         * @return 最後に受け取った出力先のディレクトリ
         */
        private Path getDataDir() {

            final Path result = this.dataDir;
            return result;

        }

        /**
         * 保存された集計を返す<br>
         *
         * @since 0.2.0
         *
         * @return 保存された集計
         */
        private CarryoverSummaryDto getSavedSummary() {

            final CarryoverSummaryDto result = this.savedSummary;
            return result;

        }

    }

    /**
     * テスト対象を作成する<br>
     *
     * @since 0.2.0
     *
     * @param advanceDataDao
     *                       JSON ファイルの読み書き
     *
     * @return テスト対象
     */
    private static AdvanceDataLogicImpl createTarget(final AdvanceDataDao advanceDataDao) {

        final AdvanceDataLogicImpl result = new AdvanceDataLogicImpl(advanceDataDao);
        return result;

    }

    /**
     * initialize メソッドのテスト - 正常系:保存先を DAO に渡す場合
     *
     * @since 0.2.0
     */
    @Test
    public void testInitialize_normalPath() {

        /* 期待値の定義 */
        final Path expectedDataDir = Path.of("docs", "data");

        /* 準備 */
        final StubAdvanceDataDao   testData   = new StubAdvanceDataDao();
        final AdvanceDataLogicImpl testTarget = AdvanceDataLogicImplTest.createTarget(testData);

        /* テスト対象の実行 */
        testTarget.initialize(Path.of("docs", "data"));

        /* 検証の準備 */
        final Path actualDataDir = testData.getDataDir();

        /* 検証の実施 */
        Assertions.assertEquals(expectedDataDir, actualDataDir, "出力先のディレクトリが一致しません");

    }

    /**
     * saveSummary メソッドのテスト - 正常系:集計を DTO に変換して保存する場合
     *
     * @since 0.2.0
     *
     * @throws Exception
     *                   書き込みに失敗した場合
     */
    @Test
    public void testSaveSummary_normalConvert() throws Exception {

        /* 期待値の定義 */
        final int expectedLatestIssue = 371;

        /* 準備 */
        final StubAdvanceDataDao    testData    = new StubAdvanceDataDao();
        final AdvanceDataLogicImpl  testTarget  = AdvanceDataLogicImplTest.createTarget(testData);
        final CarryoverSummaryModel testSummary = new CarryoverSummaryModel(371, List.of(), List.of());

        /* テスト対象の実行 */
        testTarget.saveSummary(testSummary);

        /* 検証の準備 */
        final CarryoverSummaryDto savedSummary      = testData.getSavedSummary();
        final int                 actualLatestIssue = savedSummary.getLatestIssue();

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");

    }

}
