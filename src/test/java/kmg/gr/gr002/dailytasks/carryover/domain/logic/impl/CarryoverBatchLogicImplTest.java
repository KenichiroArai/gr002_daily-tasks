package kmg.gr.gr002.dailytasks.carryover.domain.logic.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueRefModel;

/**
 * {@link CarryoverBatchLogicImpl} のテスト<br>
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
public class CarryoverBatchLogicImplTest {

    /**
     * テスト用の参照を作成する<br>
     *
     * @since 0.1.0
     *
     * @param number
     *               Issue 番号
     * @param date
     *               日付。null のときは日付なし
     *
     * @return 参照
     */
    private static DailyTaskIssueRefModel createRef(final int number, final LocalDate date) {

        final DailyTaskIssueRefModel result
            = new DailyTaskIssueRefModel(number, "title", "open", Instant.parse("2026-10-06T00:00:00Z"), date);
        return result;

    }

    /**
     * initialize メソッドのテスト - 正常系:参照と単位の件数を保持する場合
     *
     * @since 0.1.0
     */
    @Test
    public void testInitialize_normalKeepFields() {

        /* 期待値の定義 */
        final int expectedBatchCount = 1;
        final int expectedFirstNumber = 1;

        /* 準備 */
        final CarryoverBatchLogicImpl      testTarget = new CarryoverBatchLogicImpl();
        final List<DailyTaskIssueRefModel> testRefs
            = List.of(CarryoverBatchLogicImplTest.createRef(1, LocalDate.parse("2026-01-01")));

        /* テスト対象の実行 */
        testTarget.initialize(testRefs, 2);
        final List<List<DailyTaskIssueRefModel>> testBatches = testTarget.split();

        /* 検証の準備 */
        final int actualBatchCount  = testBatches.size();
        final int actualFirstNumber = testBatches.get(0).get(0).getNumber();

        /* 検証の実施 */
        Assertions.assertEquals(expectedBatchCount, actualBatchCount, "単位の件数が一致しません");
        Assertions.assertEquals(expectedFirstNumber, actualFirstNumber, "保持した参照の番号が一致しません");

    }

    /**
     * split メソッドのテスト - 正常系:日付がある参照を単位の件数で分ける場合
     *
     * @since 0.1.0
     */
    @Test
    public void testSplit_normalChunkByBatchSize() {

        /* 期待値の定義 */
        final int expectedBatchCount = 2;
        final int expectedFirstSize  = 2;
        final int expectedSecondSize = 1;
        final int expectedFirstNumber  = 1;
        final int expectedSecondNumber = 2;
        final int expectedThirdNumber  = 3;

        /* 準備 */
        final CarryoverBatchLogicImpl      testTarget = new CarryoverBatchLogicImpl();
        final List<DailyTaskIssueRefModel> testRefs   = List.of(
            CarryoverBatchLogicImplTest.createRef(2, LocalDate.parse("2026-01-02")),
            CarryoverBatchLogicImplTest.createRef(1, LocalDate.parse("2026-01-01")),
            CarryoverBatchLogicImplTest.createRef(3, LocalDate.parse("2026-01-03")));
        testTarget.initialize(testRefs, 2);

        /* テスト対象の実行 */
        final List<List<DailyTaskIssueRefModel>> testBatches = testTarget.split();

        /* 検証の準備 */
        final int actualBatchCount   = testBatches.size();
        final int actualFirstSize    = testBatches.get(0).size();
        final int actualSecondSize   = testBatches.get(1).size();
        final int actualFirstNumber  = testBatches.get(0).get(0).getNumber();
        final int actualSecondNumber = testBatches.get(0).get(1).getNumber();
        final int actualThirdNumber  = testBatches.get(1).get(0).getNumber();

        /* 検証の実施 */
        Assertions.assertEquals(expectedBatchCount, actualBatchCount, "単位の件数が一致しません");
        Assertions.assertEquals(expectedFirstSize, actualFirstSize, "1 単位目の件数が一致しません");
        Assertions.assertEquals(expectedSecondSize, actualSecondSize, "2 単位目の件数が一致しません");
        Assertions.assertEquals(expectedFirstNumber, actualFirstNumber, "日付順の先頭が一致しません");
        Assertions.assertEquals(expectedSecondNumber, actualSecondNumber, "日付順の 2 件目が一致しません");
        Assertions.assertEquals(expectedThirdNumber, actualThirdNumber, "日付順の 3 件目が一致しません");

    }

    /**
     * split メソッドのテスト - 正常系:同じ日付は番号順にする場合
     *
     * @since 0.1.0
     */
    @Test
    public void testSplit_normalSameDateByNumber() {

        /* 期待値の定義 */
        final int expectedFirstNumber  = 1;
        final int expectedSecondNumber = 2;

        /* 準備 */
        final CarryoverBatchLogicImpl      testTarget = new CarryoverBatchLogicImpl();
        final LocalDate                    testDate   = LocalDate.parse("2026-01-01");
        final List<DailyTaskIssueRefModel> testRefs   = List.of(CarryoverBatchLogicImplTest.createRef(2, testDate),
            CarryoverBatchLogicImplTest.createRef(1, testDate));
        testTarget.initialize(testRefs, 10);

        /* テスト対象の実行 */
        final List<List<DailyTaskIssueRefModel>> testBatches = testTarget.split();

        /* 検証の準備 */
        final int actualFirstNumber  = testBatches.get(0).get(0).getNumber();
        final int actualSecondNumber = testBatches.get(0).get(1).getNumber();

        /* 検証の実施 */
        Assertions.assertEquals(expectedFirstNumber, actualFirstNumber, "同じ日付の先頭番号が一致しません");
        Assertions.assertEquals(expectedSecondNumber, actualSecondNumber, "同じ日付の 2 件目の番号が一致しません");

    }

    /**
     * split メソッドのテスト - 正常系:日付がない参照を最後の単位にする場合
     *
     * @since 0.1.0
     */
    @Test
    public void testSplit_normalUndatedLastBatch() {

        /* 期待値の定義 */
        final int expectedBatchCount   = 2;
        final int expectedDatedNumber  = 1;
        final int expectedUndatedNumber = 99;

        /* 準備 */
        final CarryoverBatchLogicImpl      testTarget = new CarryoverBatchLogicImpl();
        final List<DailyTaskIssueRefModel> testRefs   = List.of(
            CarryoverBatchLogicImplTest.createRef(99, null),
            CarryoverBatchLogicImplTest.createRef(1, LocalDate.parse("2026-01-01")));
        testTarget.initialize(testRefs, 10);

        /* テスト対象の実行 */
        final List<List<DailyTaskIssueRefModel>> testBatches = testTarget.split();

        /* 検証の準備 */
        final int actualBatchCount    = testBatches.size();
        final int actualDatedNumber   = testBatches.get(0).get(0).getNumber();
        final int actualUndatedNumber = testBatches.get(1).get(0).getNumber();

        /* 検証の実施 */
        Assertions.assertEquals(expectedBatchCount, actualBatchCount, "単位の件数が一致しません");
        Assertions.assertEquals(expectedDatedNumber, actualDatedNumber, "日付ありの番号が一致しません");
        Assertions.assertEquals(expectedUndatedNumber, actualUndatedNumber, "日付なしの番号が一致しません");

    }

    /**
     * split メソッドのテスト - 正常系:単位の件数が 1 未満のときは全件を 1 単位にする場合
     *
     * @since 0.1.0
     */
    @Test
    public void testSplit_normalBatchSizeLessThanOne() {

        /* 期待値の定義 */
        final int expectedBatchCount = 1;
        final int expectedSize       = 3;
        final int expectedFirstNumber  = 1;
        final int expectedSecondNumber = 2;
        final int expectedThirdNumber  = 99;

        /* 準備 */
        final CarryoverBatchLogicImpl      testTarget = new CarryoverBatchLogicImpl();
        final List<DailyTaskIssueRefModel> testRefs   = List.of(
            CarryoverBatchLogicImplTest.createRef(2, LocalDate.parse("2026-01-02")),
            CarryoverBatchLogicImplTest.createRef(99, null),
            CarryoverBatchLogicImplTest.createRef(1, LocalDate.parse("2026-01-01")));
        testTarget.initialize(testRefs, 0);

        /* テスト対象の実行 */
        final List<List<DailyTaskIssueRefModel>> testBatches = testTarget.split();

        /* 検証の準備 */
        final int actualBatchCount   = testBatches.size();
        final int actualSize         = testBatches.get(0).size();
        final int actualFirstNumber  = testBatches.get(0).get(0).getNumber();
        final int actualSecondNumber = testBatches.get(0).get(1).getNumber();
        final int actualThirdNumber  = testBatches.get(0).get(2).getNumber();

        /* 検証の実施 */
        Assertions.assertEquals(expectedBatchCount, actualBatchCount, "単位の件数が一致しません");
        Assertions.assertEquals(expectedSize, actualSize, "1 単位の件数が一致しません");
        Assertions.assertEquals(expectedFirstNumber, actualFirstNumber, "日付順の先頭が一致しません");
        Assertions.assertEquals(expectedSecondNumber, actualSecondNumber, "日付順の 2 件目が一致しません");
        Assertions.assertEquals(expectedThirdNumber, actualThirdNumber, "日付なしが末尾に続く必要があります");

    }

    /**
     * split メソッドのテスト - 準正常系:参照がないときは空の 1 単位にする場合
     *
     * @since 0.1.0
     */
    @Test
    public void testSplit_semiEmptyRefs() {

        /* 期待値の定義 */
        final int expectedBatchCount = 1;
        final int expectedSize       = 0;

        /* 準備 */
        final CarryoverBatchLogicImpl testTarget = new CarryoverBatchLogicImpl();
        testTarget.initialize(List.of(), 10);

        /* テスト対象の実行 */
        final List<List<DailyTaskIssueRefModel>> testBatches = testTarget.split();

        /* 検証の準備 */
        final int actualBatchCount = testBatches.size();
        final int actualSize       = testBatches.get(0).size();

        /* 検証の実施 */
        Assertions.assertEquals(expectedBatchCount, actualBatchCount, "単位の件数が一致しません");
        Assertions.assertEquals(expectedSize, actualSize, "空の単位である必要があります");

    }

    /**
     * split メソッドのテスト - 準正常系:日付がある参照がなく日付がない参照だけがある場合
     *
     * @since 0.1.0
     */
    @Test
    public void testSplit_semiOnlyUndated() {

        /* 期待値の定義 */
        final int expectedBatchCount = 1;
        final int expectedNumber     = 7;

        /* 準備 */
        final CarryoverBatchLogicImpl      testTarget = new CarryoverBatchLogicImpl();
        final List<DailyTaskIssueRefModel> testRefs
            = List.of(CarryoverBatchLogicImplTest.createRef(7, null));
        testTarget.initialize(testRefs, 10);

        /* テスト対象の実行 */
        final List<List<DailyTaskIssueRefModel>> testBatches = testTarget.split();

        /* 検証の準備 */
        final int actualBatchCount = testBatches.size();
        final int actualNumber     = testBatches.get(0).get(0).getNumber();

        /* 検証の実施 */
        Assertions.assertEquals(expectedBatchCount, actualBatchCount, "単位の件数が一致しません");
        Assertions.assertEquals(expectedNumber, actualNumber, "日付なしの番号が一致しません");

    }

}
