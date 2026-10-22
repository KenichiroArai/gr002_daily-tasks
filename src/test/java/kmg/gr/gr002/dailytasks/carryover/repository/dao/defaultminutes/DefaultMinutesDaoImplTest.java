package kmg.gr.gr002.dailytasks.carryover.repository.dao.defaultminutes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import kmg.gr.gr002.dailytasks.carryover.domain.dto.DefaultMinutesDto;

import kmg.gr.gr002.dailytasks.testutil.LogAssertions;
import kmg.gr.gr002.dailytasks.testutil.LogCapture;
import kmg.gr.gr002.dailytasks.testutil.MessageLogicTestUtil;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

/**
 * {@link DefaultMinutesDaoImpl} のテスト<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings("nls")
public class DefaultMinutesDaoImplTest {

    /**
     * テスト対象を作成する<br>
     *
     * @since 0.1.0
     *
     * @return テスト対象
     */
    private static DefaultMinutesDaoImpl createTarget() {

        final DefaultMinutesDaoImpl result = new DefaultMinutesDaoImpl(new JsonMapper(), MessageLogicTestUtil.create());
        return result;

    }

    /**
     * テスト用の一時ディレクトリ
     *
     * @since 0.1.0
     */
    @TempDir
    Path tempDir;

    /**
     * load メソッドのテスト - 異常系:設定ファイルが不正な場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testLoad_errorInvalidJson() throws IOException {

        /* 期待値の定義 */

        /* 準備 */
        final Path testConfig = this.tempDir.resolve("default-minutes.json");
        Files.writeString(testConfig, "[不正");
        final DefaultMinutesDaoImpl testTarget = DefaultMinutesDaoImplTest.createTarget();

        /* テスト対象の実行 */
        testTarget.initialize(testConfig);
        final IOException testException = Assertions.assertThrows(IOException.class, testTarget::load);

        /* 検証の準備 */
        final Throwable actualCause = testException.getCause();

        /* 検証の実施 */
        Assertions.assertInstanceOf(JacksonException.class, actualCause, "原因の例外の型が一致しません");

    }

    /**
     * load メソッドのテスト - 正常系:設定ファイルを読み込む場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testLoad_normalConfig() throws IOException {

        /* 期待値の定義 */
        final Double expectedMinutes = (double) 30;

        /* 準備 */
        final Path testConfig = this.tempDir.resolve("default-minutes.json");
        Files.writeString(testConfig, "{\"高校数学\": 30, \"国語\": 15}");
        final DefaultMinutesDaoImpl testTarget = DefaultMinutesDaoImplTest.createTarget();

        /* テスト対象の実行 */
        testTarget.initialize(testConfig);
        final DefaultMinutesDto testResult = testTarget.load();

        /* 検証の準備 */
        final Double actualMinutes = testResult.toDefaultMinutesModel().find("高校数学");

        /* 検証の実施 */
        Assertions.assertEquals(expectedMinutes, actualMinutes, "標準時間が一致しません");

    }

    /**
     * load メソッドのテスト - 準正常系:設定ファイルがない場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testLoad_semiNoFile() throws IOException {

        /* 期待値の定義 */
        final Path     testConfig   = this.tempDir.resolve("none.json");
        final String[] expectedMsgs = {
            "標準時間の設定ファイルがありません: " + testConfig,
        };

        /* 準備 */
        final DefaultMinutesDaoImpl testTarget = DefaultMinutesDaoImplTest.createTarget();

        /* テスト対象の実行 */
        try (LogCapture testLog = new LogCapture(DefaultMinutesDaoImpl.class)) {

            testTarget.initialize(testConfig);
            final DefaultMinutesDto testResult = testTarget.load();

            /* 検証の準備 */
            final String[] actualMsgs    = testLog.getMessages();
            final Double   actualMinutes = testResult.toDefaultMinutesModel().find("国語");

            /* 検証の実施 */
            Assertions.assertNull(actualMinutes, "標準時間は未登録になる必要があります");
            LogAssertions.assertMessages(expectedMsgs, actualMsgs);

        }

    }

}
