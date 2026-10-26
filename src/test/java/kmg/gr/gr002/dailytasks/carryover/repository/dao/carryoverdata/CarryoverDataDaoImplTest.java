package kmg.gr.gr002.dailytasks.carryover.repository.dao.carryoverdata;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import kmg.core.infrastructure.type.KmgString;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverIssueDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverIssuesDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverItemDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverSummaryDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.DailySummaryDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.ItemStatDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.ItemStatsDto;
import kmg.gr.gr002.dailytasks.testutil.ReflectionTestUtil;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

/**
 * {@link CarryoverDataDaoImpl} のテスト<br>
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
public class CarryoverDataDaoImplTest {

    /**
     * JSON に変換できない Bean<br>
     * <p>
     * ゲッターが例外を投げるため、JSON への変換に失敗する。
     * </p>
     *
     * @author KenichiroArai
     *
     * @since 0.1.0
     *
     * @version 0.1.0
     */
    public static final class FailingBean {

        /**
         * 常に例外を投げる<br>
         *
         * @since 0.1.0
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
     * テスト用の解析結果を作成する<br>
     *
     * @since 0.1.0
     *
     * @param number
     *               Issue 番号
     *
     * @return 解析結果
     */
    private static CarryoverIssueDto createIssue(final int number) {

        final CarryoverItemDto  item   = new CarryoverItemDto("国語", "2026-06-18", true, 8.5, "parsed", "持ち越し",
            "- [x] 国語2026/06/18（残り時間：8.5分）");
        final CarryoverIssueDto result = new CarryoverIssueDto(number, "2026年10月06日のタスク", "2026-10-06", "open",
            "2026-10-06T14:23:08Z", List.of("持ち越し"), 1, 1, 8.5, List.of(item));
        return result;

    }

    /**
     * テスト対象を作成する<br>
     *
     * @since 0.1.0
     *
     * @return テスト対象
     */
    private static CarryoverDataDaoImpl createTarget() {

        final CarryoverDataDaoImpl result = new CarryoverDataDaoImpl(new JsonMapper());
        return result;

    }

    /**
     * private の write メソッドを呼び出す<br>
     *
     * @since 0.1.0
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
    private static void write(final CarryoverDataDaoImpl target, final Path path, final Object value) throws Exception {

        ReflectionTestUtil.invoke(target, "write", new Class<?>[] {
            Path.class, Object.class
        }, path, value);

    }

    /**
     * テスト用の一時ディレクトリ
     *
     * @since 0.1.0
     */
    @TempDir
    Path tempDir;

    /**
     * loadIssues メソッドのテスト - 異常系:JSON が不正な場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testLoadIssues_errorInvalidJson() throws IOException {

        /* 期待値の定義 */

        /* 準備 */
        final Path testIssuesDir = Files.createDirectories(this.tempDir.resolve("issues"));
        Files.writeString(testIssuesDir.resolve("0001.json"), "{不正");
        final CarryoverDataDaoImpl testTarget = CarryoverDataDaoImplTest.createTarget();

        /* テスト対象の実行 */
        testTarget.initialize(this.tempDir);
        final IOException testException = Assertions.assertThrows(IOException.class, testTarget::loadIssues);

        /* 検証の準備 */
        final Throwable actualCause = testException.getCause();

        /* 検証の実施 */
        Assertions.assertInstanceOf(JacksonException.class, actualCause, "原因の例外の型が一致しません");

    }

    /**
     * loadIssues メソッドのテスト - 正常系:保存済みの JSON を読み込む場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testLoadIssues_normalSaved() throws IOException {

        /* 期待値の定義 */
        final Set<Integer> expectedNumbers       = Set.of(1, 371);
        final double       expectedMinutes       = 8.5;
        final String       expectedMinutesSource = "parsed";

        /* 準備 */
        final CarryoverDataDaoImpl testTarget = CarryoverDataDaoImplTest.createTarget();
        testTarget.initialize(this.tempDir);
        testTarget.saveIssue(CarryoverDataDaoImplTest.createIssue(1));
        testTarget.saveIssue(CarryoverDataDaoImplTest.createIssue(371));
        final Path issuesDir = this.tempDir.resolve("issues");
        final Path memo      = issuesDir.resolve("memo.txt");
        Files.writeString(memo, "対象外");

        /* テスト対象の実行 */
        final CarryoverIssuesDto testResult = testTarget.loadIssues();

        /* 検証の準備 */
        final Set<Integer>     actualNumbers       = testResult.numbers();
        final CarryoverIssueDto       found               = testResult.find(371);
        final List<CarryoverItemDto>  items               = found.getItems();
        final CarryoverItemDto        actualItem          = items.get(0);
        final double           actualMinutes       = actualItem.getMinutes();
        final String           actualMinutesSource = actualItem.getMinutesSource();
        final boolean          actualChecked       = actualItem.isChecked();

        /* 検証の実施 */
        Assertions.assertEquals(expectedNumbers, actualNumbers, "Issue 番号が一致しません");
        Assertions.assertEquals(expectedMinutes, actualMinutes, "残り時間が一致しません");
        Assertions.assertEquals(expectedMinutesSource, actualMinutesSource, "残り時間の取得元が一致しません");
        Assertions.assertTrue(actualChecked, "チェック済みになっていません");

    }

    /**
     * loadIssues メソッドのテスト - 準正常系:ディレクトリがない場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testLoadIssues_semiNoDirectory() throws IOException {

        /* 期待値の定義 */

        /* 準備 */
        final CarryoverDataDaoImpl testTarget = CarryoverDataDaoImplTest.createTarget();

        /* テスト対象の実行 */
        testTarget.initialize(this.tempDir.resolve("none"));
        final CarryoverIssuesDto testResult = testTarget.loadIssues();

        /* 検証の準備 */
        final Set<Integer> numbers     = testResult.numbers();
        final boolean      actualEmpty = numbers.isEmpty();

        /* 検証の実施 */
        Assertions.assertTrue(actualEmpty, "空になる必要があります");

    }

    /**
     * saveIssue メソッドのテスト - 正常系:4 桁ゼロ埋めのファイル名で LF 改行の JSON を保存する場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testSaveIssue_normalFileName() throws IOException {

        /* 期待値の定義 */
        final String expectedFirstLines = "{\n  \"number\": 7,\n  \"title\": \"2026年10月06日のタスク\",\n";

        /* 準備 */
        final CarryoverDataDaoImpl testTarget = CarryoverDataDaoImplTest.createTarget();

        /* テスト対象の実行 */
        testTarget.initialize(this.tempDir);
        testTarget.saveIssue(CarryoverDataDaoImplTest.createIssue(7));

        /* 検証の準備 */
        final Path    issuesDir      = this.tempDir.resolve("issues");
        final Path    issueFile      = issuesDir.resolve("0007.json");
        final String  actualJson     = Files.readString(issueFile, StandardCharsets.UTF_8);
        final boolean actualStartsWith = actualJson.startsWith(expectedFirstLines);
        final boolean actualHasCr      = actualJson.contains("\r");
        final boolean actualEndsWithLf = actualJson.endsWith("}\n");

        /* 検証の実施 */
        Assertions.assertTrue(actualStartsWith,
            KmgString.concat("JSON の先頭が一致しません: ", actualJson));
        Assertions.assertFalse(actualHasCr, "改行は LF である必要があります");
        Assertions.assertTrue(actualEndsWithLf, "末尾は改行である必要があります");

    }

    /**
     * saveSummary メソッドのテスト - 正常系:日別の集計を指定した順で保存する場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testSaveSummary_normalDays() throws IOException {

        /* 期待値の定義 */
        final String expectedJson = """
            {
              "latestIssue": 175,
              "items": [ ],
              "days": [
                {
                  "date": "2026-03-24",
                  "issue": 175,
                  "declaredCount": null,
                  "total": {
                    "count": 1,
                    "minutes": 30.0,
                    "checkedCount": 0,
                    "checkedMinutes": 0.0
                  },
                  "byItem": {
                    "英語": {
                      "count": 1,
                      "minutes": 30.0,
                      "checkedCount": 0,
                      "checkedMinutes": 0.0
                    },
                    "国語": {
                      "count": 0,
                      "minutes": 0.0,
                      "checkedCount": 0,
                      "checkedMinutes": 0.0
                    }
                  },
                  "byOriginMonth": { }
                }
              ]
            }
            """;

        /* 準備 */
        final CarryoverDataDaoImpl     testTarget    = CarryoverDataDaoImplTest.createTarget();
        final ItemStatDto              testStat      = new ItemStatDto(1, 30, 0, 0);
        final ItemStatDto              testEmptyStat = new ItemStatDto(0, 0, 0, 0);
        final ItemStatsDto testByItem = new ItemStatsDto();
        testByItem.add("英語", testStat);
        testByItem.add("国語", testEmptyStat);
        final DailySummaryDto testDay = new DailySummaryDto("2026-03-24", 175, null, testStat, testByItem,
            new ItemStatsDto());

        /* テスト対象の実行 */
        testTarget.initialize(this.tempDir);
        testTarget.saveSummary(new CarryoverSummaryDto(175, List.of(), List.of(testDay)));

        /* 検証の準備 */
        final String actualJson = Files.readString(this.tempDir.resolve("summary.json"), StandardCharsets.UTF_8);

        /* 検証の実施 */
        Assertions.assertEquals(expectedJson, actualJson, "JSON が一致しません");

    }

    /**
     * saveSummary メソッドのテスト - 正常系:画面用の集計を保存する場合
     *
     * @since 0.1.0
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
        final CarryoverDataDaoImpl testTarget = CarryoverDataDaoImplTest.createTarget();

        /* テスト対象の実行 */
        testTarget.initialize(this.tempDir);
        testTarget.saveSummary(new CarryoverSummaryDto(371, List.of("国語"), List.of()));

        /* 検証の準備 */
        final String actualJson = Files.readString(this.tempDir.resolve("summary.json"), StandardCharsets.UTF_8);

        /* 検証の実施 */
        Assertions.assertEquals(expectedJson, actualJson, "JSON が一致しません");

    }

    /**
     * write メソッドのテスト - 異常系:JSON に変換できない場合
     *
     * @since 0.1.0
     */
    @Test
    public void testWrite_errorNotSerializable() {

        /* 期待値の定義 */

        /* 準備 */
        final CarryoverDataDaoImpl testTarget = CarryoverDataDaoImplTest.createTarget();
        final Path                 testPath   = this.tempDir.resolve("error.json");

        /* テスト対象の実行 */
        final IOException testException = Assertions.assertThrows(IOException.class,
            () -> CarryoverDataDaoImplTest.write(testTarget, testPath, new FailingBean()));

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
     * @since 0.1.0
     *
     * @throws Exception
     *                   例外が発生した場合
     */
    @Test
    public void testWrite_normalCreateParent() throws Exception {

        /* 期待値の定義 */
        final String expectedJson = "[\n  1\n]\n";

        /* 準備 */
        final CarryoverDataDaoImpl testTarget = CarryoverDataDaoImplTest.createTarget();
        final Path                 dirA       = this.tempDir.resolve("a");
        final Path                 dirB       = dirA.resolve("b");
        final Path                 testPath   = dirB.resolve("c.json");

        /* テスト対象の実行 */
        CarryoverDataDaoImplTest.write(testTarget, testPath, List.of(1));

        /* 検証の準備 */
        final String actualJson = Files.readString(testPath, StandardCharsets.UTF_8);

        /* 検証の実施 */
        Assertions.assertEquals(expectedJson, actualJson, "JSON が一致しません");

    }

    /**
     * loadIssue メソッドのテスト - 正常系:保存した 1 件を読み込む場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testLoadIssue_normalFile() throws IOException {

        /* 期待値の定義 */
        final int expectedNumber = 7;

        /* 準備 */
        final CarryoverDataDaoImpl testTarget = CarryoverDataDaoImplTest.createTarget();
        testTarget.initialize(this.tempDir);
        testTarget.saveIssue(CarryoverDataDaoImplTest.createIssue(expectedNumber));

        /* テスト対象の実行 */
        final CarryoverIssueDto testResult = testTarget.loadIssue(expectedNumber);

        /* 検証の準備 */
        final int actualNumber = testResult.getNumber();

        /* 検証の実施 */
        Assertions.assertEquals(expectedNumber, actualNumber, "Issue 番号が一致しません");

    }

    /**
     * loadIssue メソッドのテスト - 準正常系:ファイルがない場合
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     入出力エラーが発生した場合
     */
    @Test
    public void testLoadIssue_semiMissing() throws IOException {

        /* 期待値の定義 */

        /* 準備 */
        final CarryoverDataDaoImpl testTarget = CarryoverDataDaoImplTest.createTarget();
        testTarget.initialize(this.tempDir);

        /* テスト対象の実行 */
        final CarryoverIssueDto testResult = testTarget.loadIssue(7);

        /* 検証の準備 */
        final boolean actualMissing = testResult == null;

        /* 検証の実施 */
        Assertions.assertTrue(actualMissing, "ファイルがないときは null になる必要があります");

    }

}
