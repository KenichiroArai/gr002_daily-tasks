package kmg.gr.gr002.dailytasks.carryover.domain.dto;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link DailySummaryDto} のテスト<br>
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
public class DailySummaryDtoTest {

    /**
     * テスト対象を作成する<br>
     *
     * @since 0.1.0
     *
     * @return テスト対象
     */
    private static DailySummaryDto createTarget() {

        final ItemStatsDto byItem        = new ItemStatsDto();
        final ItemStatsDto byOriginMonth = new ItemStatsDto();
        byItem.add("英語", new ItemStatDto(1, 30, 0, 0));
        byItem.add("国語", new ItemStatDto(1, 15, 1, 15));
        byOriginMonth.add("2026-03", new ItemStatDto(2, 45, 1, 15));
        final DailySummaryDto result = new DailySummaryDto("2026-03-24", 175, 2, new ItemStatDto(2, 45, 1, 15), byItem,
            byOriginMonth);
        return result;

    }

    /**
     * getByItem メソッドのテスト - 正常系:項目ごとの集計を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetByItem_normalValue() {

        /* 期待値の定義 */
        final List<String> expectedByItem = List.of("英語", "国語");

        /* 準備 */
        final DailySummaryDto testTarget = DailySummaryDtoTest.createTarget();

        /* テスト対象の実行 */
        final ItemStatsDto testResult = testTarget.getByItem();

        /* 検証の準備 */
        final List<String> actualByItem = testResult.names();

        /* 検証の実施 */
        Assertions.assertEquals(expectedByItem, actualByItem, "項目ごとの集計が一致しません");

    }

    /**
     * getByOriginMonth メソッドのテスト - 正常系:持ち越し元の月ごとの集計を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetByOriginMonth_normalValue() {

        /* 期待値の定義 */
        final List<String> expectedByOriginMonth = List.of("2026-03");

        /* 準備 */
        final DailySummaryDto testTarget = DailySummaryDtoTest.createTarget();

        /* テスト対象の実行 */
        final ItemStatsDto testResult = testTarget.getByOriginMonth();

        /* 検証の準備 */
        final List<String> actualByOriginMonth = testResult.names();

        /* 検証の実施 */
        Assertions.assertEquals(expectedByOriginMonth, actualByOriginMonth, "持ち越し元の月ごとの集計が一致しません");

    }

    /**
     * getDate メソッドのテスト - 正常系:その日の日付を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDate_normalValue() {

        /* 期待値の定義 */
        final String expectedDate = "2026-03-24";

        /* 準備 */
        final DailySummaryDto testTarget = DailySummaryDtoTest.createTarget();

        /* テスト対象の実行 */
        final String testResult = testTarget.getDate();

        /* 検証の準備 */
        final String actualDate = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDate, actualDate, "その日の日付が一致しません");

    }

    /**
     * getDeclaredCount メソッドのテスト - 正常系:残数を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDeclaredCount_normalValue() {

        /* 期待値の定義 */
        final Integer expectedDeclaredCount = 2;

        /* 準備 */
        final DailySummaryDto testTarget = DailySummaryDtoTest.createTarget();

        /* テスト対象の実行 */
        final Integer testResult = testTarget.getDeclaredCount();

        /* 検証の準備 */
        final Integer actualDeclaredCount = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDeclaredCount, actualDeclaredCount, "残数が一致しません");

    }

    /**
     * getIssue メソッドのテスト - 正常系:Issue 番号を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetIssue_normalValue() {

        /* 期待値の定義 */
        final int expectedIssue = 175;

        /* 準備 */
        final DailySummaryDto testTarget = DailySummaryDtoTest.createTarget();

        /* テスト対象の実行 */
        final int testResult = testTarget.getIssue();

        /* 検証の準備 */
        final int actualIssue = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedIssue, actualIssue, "Issue 番号が一致しません");

    }

    /**
     * getTotal メソッドのテスト - 正常系:全体の集計を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetTotal_normalValue() {

        /* 期待値の定義 */
        final int expectedTotal = 2;

        /* 準備 */
        final DailySummaryDto testTarget = DailySummaryDtoTest.createTarget();

        /* テスト対象の実行 */
        final ItemStatDto testResult = testTarget.getTotal();

        /* 検証の準備 */
        final int actualTotal = testResult.getCount();

        /* 検証の実施 */
        Assertions.assertEquals(expectedTotal, actualTotal, "全体の集計が一致しません");

    }

    /**
     * 項目ごとの集計が空でないことを確認する<br>
     *
     * @since 0.1.0
     */
    @Test
    public void testGetByItem_normalNotEmpty() {

        /* 期待値の定義 */

        /* 準備 */
        final DailySummaryDto testTarget = DailySummaryDtoTest.createTarget();

        /* テスト対象の実行 */
        final ItemStatsDto byItem = testTarget.getByItem();
        final boolean testResult = byItem.isEmpty();

        /* 検証の準備 */
        final boolean actualEmpty = testResult;

        /* 検証の実施 */
        Assertions.assertFalse(actualEmpty, "項目ごとの集計は空ではない必要があります");

    }

}
