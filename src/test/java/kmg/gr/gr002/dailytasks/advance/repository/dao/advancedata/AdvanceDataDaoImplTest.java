package kmg.gr.gr002.dailytasks.advance.repository.dao.advancedata;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import kmg.core.infrastructure.type.KmgString;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverSummaryDto;
import kmg.gr.gr002.dailytasks.testutil.DailyTasksMessageSourceTestUtil;
import kmg.gr.gr002.dailytasks.testutil.ReflectionTestUtil;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

/**
 * {@link AdvanceDataDaoImpl} のテスト<br>
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
public class AdvanceDataDaoImplTest {

    /**
     * JSON に変換できない Bean<br>
     * <p>
     * ゲッターが例外を投げるため、JSON への変換に失敗する。
     * </p>
     *
     * @author KenichiroArai
     *
     * @since 0.2.0
     *
     * @version 0.2.0
     */
    public static final class FailingBean {

        /**
         * 常に例外を投げる<br>
         *
         * @since 0.2.0
         *
         * @return 返らない
         *
         * @throws IllegalStateException
         *                               常に投げる
         */
        public String getValue() {

            throw new IllegalStateException("変換できません");

        }

    }

    /**
     * テスト対象を作成する<br>
     *
     * @since 0.2.0
     *
     * @return テスト対象
     */
    private static AdvanceDataDaoImpl createTarget() {

        final AdvanceDataDaoImpl result
            = new AdvanceDataDaoImpl(new JsonMapper(), DailyTasksMessageSourceTestUtil.create());
        return result;

    }

    /**
     * private の write メソッドを呼び出す<br>
     *
     * @since 0.2.0
     *
     * @param target
     *               テスト対象
     * @param path
     *               書き込み先
     * @param value
     *               書き込む値
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    private static void write(final AdvanceDataDaoImpl target, final Path path, final Object value) throws Exception {

        ReflectionTestUtil.invoke(target, "write", new Class<?>[] {
            Path.class, Object.class
        }, path, value);

    }

    /**
     * テスト用の一時ディレクトリ
     *
     * @since 0.2.0
     */
    @TempDir
    Path tempDir;

    /**
     * initialize メソッドのテスト - 正常系:出力先を受け取る場合
     *
     * @since 0.2.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testInitialize_normalPath() throws IOException {

        /* 期待値の定義 */
        final String expectedJson
            = "{\n  \"latestIssue\": 371,\n  \"items\": [\n    \"国語\"\n  ],\n  \"days\": [ ]\n}\n";

        /* 準備 */
        final AdvanceDataDaoImpl testTarget = AdvanceDataDaoImplTest.createTarget();

        /* テスト対象の実行 */
        testTarget.initialize(this.tempDir);
        testTarget.saveSummary(new CarryoverSummaryDto(371, List.of("国語"), List.of()));

        /* 検証の準備 */
        final String actualJson = Files.readString(
            this.tempDir.resolve("advance").resolve("summary.json"), StandardCharsets.UTF_8);

        /* 検証の実施 */
        Assertions.assertEquals(expectedJson, actualJson, "JSON が一致しません");

    }

    /**
     * saveSummary メソッドのテスト - 正常系:画面用の集計を保存する場合
     *
     * @since 0.2.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testSaveSummary_normalSave() throws IOException {

        /* 期待値の定義 */
        final String expectedJson
            = "{\n  \"latestIssue\": 371,\n  \"items\": [\n    \"国語\"\n  ],\n  \"days\": [ ]\n}\n";

        /* 準備 */
        final AdvanceDataDaoImpl testTarget = AdvanceDataDaoImplTest.createTarget();

        /* テスト対象の実行 */
        testTarget.initialize(this.tempDir);
        testTarget.saveSummary(new CarryoverSummaryDto(371, List.of("国語"), List.of()));

        /* 検証の準備 */
        final Path   testPath   = this.tempDir.resolve("advance").resolve("summary.json");
        final String actualJson = Files.readString(testPath, StandardCharsets.UTF_8);
        final boolean actualStartsWith = actualJson.startsWith("{\n");
        final boolean actualHasCr      = actualJson.contains("\r");
        final boolean actualEndsWithLf = actualJson.endsWith("}\n");

        /* 検証の実施 */
        Assertions.assertEquals(expectedJson, actualJson, "JSON が一致しません");
        Assertions.assertTrue(actualStartsWith, KmgString.concat("JSON の先頭が一致しません: ", actualJson));
        Assertions.assertFalse(actualHasCr, "改行は LF である必要があります");
        Assertions.assertTrue(actualEndsWithLf, "末尾は改行である必要があります");

    }

    /**
     * write メソッドのテスト - 異常系:JSON に変換できない場合
     *
     * @since 0.2.0
     */
    @Test
    public void testWrite_errorNotSerializable() {

        /* 期待値の定義 */

        /* 準備 */
        final AdvanceDataDaoImpl testTarget = AdvanceDataDaoImplTest.createTarget();
        final Path                 testPath   = this.tempDir.resolve("error.json");

        /* テスト対象の実行 */
        final IOException testException = Assertions.assertThrows(IOException.class,
            () -> AdvanceDataDaoImplTest.write(testTarget, testPath, new FailingBean()));

        /* 検証の準備 */
        final Throwable actualCause  = testException.getCause();
        final boolean   actualExists = Files.exists(testPath);

        /* 検証の実施 */
        Assertions.assertInstanceOf(JacksonException.class, actualCause, "原因の例外の型が一致しません");
        Assertions.assertFalse(actualExists, "ファイルは作成されない必要があります");

    }

    /**
     * write メソッドのテスト - 正常系:親ディレクトリを作成して書き込む場合
     *
     * @since 0.2.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testWrite_normalCreateParent() throws Exception {

        /* 期待値の定義 */
        final String expectedJson = "[\n  1\n]\n";

        /* 準備 */
        final AdvanceDataDaoImpl testTarget = AdvanceDataDaoImplTest.createTarget();
        final Path                 dirA       = this.tempDir.resolve("a");
        final Path                 dirB       = dirA.resolve("b");
        final Path                 testPath   = dirB.resolve("c.json");

        /* テスト対象の実行 */
        AdvanceDataDaoImplTest.write(testTarget, testPath, List.of(1));

        /* 検証の準備 */
        final String actualJson = Files.readString(testPath, StandardCharsets.UTF_8);

        /* 検証の実施 */
        Assertions.assertEquals(expectedJson, actualJson, "JSON が一致しません");

    }

}