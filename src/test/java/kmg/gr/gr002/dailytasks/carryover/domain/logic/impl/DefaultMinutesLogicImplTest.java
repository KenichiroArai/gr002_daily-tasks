package kmg.gr.gr002.dailytasks.carryover.domain.logic.impl;

import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.application.model.DefaultMinutesModel;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.DefaultMinutesDto;
import kmg.gr.gr002.dailytasks.carryover.domain.repository.dao.DefaultMinutesDao;

/**
 * {@link DefaultMinutesLogicImpl} のテスト<br>
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
public class DefaultMinutesLogicImplTest {

    /**
     * テスト用の標準時間の設定の読み込み<br>
     *
     * @author KenichiroArai
     *
     * @since 0.1.0
     *
     * @version 0.1.0
     */
    private static final class StubDefaultMinutesDao implements DefaultMinutesDao {

        /**
         * 受け取った設定ファイルのパス
         *
         * @since 0.1.0
         */
        private Path configFile;

        /**
         * 返す標準時間
         *
         * @since 0.1.0
         */
        private final DefaultMinutesDto minutes;

        /**
         * コンストラクタ<br>
         *
         * @since 0.1.0
         *
         * @param minutes
         *                返す標準時間
         */
        private StubDefaultMinutesDao(final DefaultMinutesDto minutes) {

            this.minutes = minutes;

        }

        /**
         * 受け取った設定ファイルのパスを返す<br>
         *
         * @since 0.1.0
         *
         * @return 受け取った設定ファイルのパス
         */
        private Path getConfigFile() {

            final Path result = this.configFile;
            return result;

        }

        /**
         * 設定ファイルのパスを記録し、設定した標準時間を返す<br>
         *
         * @since 0.1.0
         *
         * @param file
         *             設定ファイルのパス
         */
        @Override
        public void initialize(final Path file) {

            this.configFile = file;

        }

        /**
         * 設定した標準時間を返す<br>
         *
         * @since 0.1.0
         *
         * @return 設定した標準時間
         */
        @Override
        public DefaultMinutesDto load() {

            final DefaultMinutesDto result = this.minutes;
            return result;

        }

    }

    /**
     * テスト対象を作成する<br>
     *
     * @since 0.1.0
     *
     * @param defaultMinutesDao
     *                          標準時間の設定の読み込み
     *
     * @return テスト対象
     */
    private static DefaultMinutesLogicImpl createTarget(final DefaultMinutesDao defaultMinutesDao) {

        final DefaultMinutesLogicImpl result = new DefaultMinutesLogicImpl(defaultMinutesDao);
        return result;

    }

    /**
     * loadDefaultMinutes メソッドのテスト - 正常系:標準時間をモデルに変換する場合
     *
     * @since 0.1.0
     *
     * @throws Exception
     *                   読み込みに失敗した場合
     */
    @Test
    public void testLoadDefaultMinutes_normalConvert() throws Exception {

        /* 期待値の定義 */
        final Double expectedMinutes    = 15.0;
        final Path   expectedConfigFile = Path.of("config", "default-minutes.json");

        /* 準備 */
        final DefaultMinutesDto      testMinutes        = new DefaultMinutesDto();
        testMinutes.add("音楽", 15.0);
        final StubDefaultMinutesDao   testDefaultMinutes = new StubDefaultMinutesDao(testMinutes);
        final DefaultMinutesLogicImpl testTarget         = DefaultMinutesLogicImplTest.createTarget(testDefaultMinutes);

        /* テスト対象の実行 */
        testTarget.initialize(Path.of("config", "default-minutes.json"));
        final DefaultMinutesModel testResult = testTarget.loadDefaultMinutes();

        /* 検証の準備 */
        final Double actualMinutes    = testResult.find("音楽");
        final Path   actualConfigFile = testDefaultMinutes.getConfigFile();

        /* 検証の実施 */
        Assertions.assertEquals(expectedMinutes, actualMinutes, "標準時間が一致しません");
        Assertions.assertEquals(expectedConfigFile, actualConfigFile, "標準時間の設定ファイルが一致しません");

    }

}
