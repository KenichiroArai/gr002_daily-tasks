package kmg.gr.gr002.dailytasks.carryover.repository.dto.json;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.domain.dto.DailySummaryDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.ItemStatDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.ItemStatsDto;

/**
 * {@link DailySummaryJsonDto} のテスト<br>
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
public class DailySummaryJsonDtoTest {

    /**
     * テスト対象を作成する<br>
     *
     * @since 0.1.0
     *
     * @return テスト対象
     */
    private static DailySummaryJsonDto createTarget() {

        final ItemStatsJsonDto byItem        = new ItemStatsJsonDto();
        final ItemStatsJsonDto byOriginMonth = new ItemStatsJsonDto();
        byItem.add("英語", new ItemStatJsonDto(1, 30, 0, 0));
        byItem.add("国語", new ItemStatJsonDto(1, 15, 1, 15));
        byOriginMonth.add("2026-03", new ItemStatJsonDto(2, 45, 1, 15));
        final DailySummaryJsonDto result = new DailySummaryJsonDto("2026-03-24", 175, 2,
            new ItemStatJsonDto(2, 45, 1, 15), byItem, byOriginMonth);
        return result;

    }

    /**
     * 日別の集計の DTO を作成する<br>
     *
     * @since 0.1.0
     *
     * @return 日別の集計の DTO
     */
    private static DailySummaryDto createDayDto() {

        final ItemStatDto     total         = new ItemStatDto(2, 26.5, 1, 11.5);
        final ItemStatsDto    byItem        = new ItemStatsDto();
        final ItemStatsDto    byOriginMonth = new ItemStatsDto();
        byItem.add("音楽", new ItemStatDto(1, 11.5, 1, 11.5));
        byItem.add("国語", new ItemStatDto(1, 15.0, 0, 0.0));
        byOriginMonth.add("2026-08", new ItemStatDto(1, 11.5, 1, 11.5));
        final DailySummaryDto result = new DailySummaryDto("2026-10-06", 371, 2, total, byItem, byOriginMonth);
        return result;

    }

    /**
     * コンストラクタのテスト - 正常系:項目ごとの集計を並び順のまま変換する場合
     *
     * @since 0.1.0
     */
    @Test
    public void testConstructor_normalByItem() {

        /* 期待値の定義 */
        final List<String> expectedKeys = List.of("音楽", "国語");

        /* 準備 */
        final DailySummaryDto testDto = DailySummaryJsonDtoTest.createDayDto();

        /* テスト対象の実行 */
        final DailySummaryJsonDto testResult = new DailySummaryJsonDto(testDto);

        /* 検証の準備 */
        final ItemStatsJsonDto byItem     = testResult.getByItem();
        final List<String>     actualKeys = byItem.names();

        /* 検証の実施 */
        Assertions.assertEquals(expectedKeys, actualKeys, "項目ごとの集計の並び順が一致しません");

    }

    /**
     * コンストラクタのテスト - 正常系:持ち越し元の月ごとの集計を変換する場合
     *
     * @since 0.1.0
     */
    @Test
    public void testConstructor_normalByOriginMonth() {

        /* 期待値の定義 */
        final double expectedCheckedMinutes = 11.5;

        /* 準備 */
        final DailySummaryDto testDto = DailySummaryJsonDtoTest.createDayDto();

        /* テスト対象の実行 */
        final DailySummaryJsonDto testResult = new DailySummaryJsonDto(testDto);

        /* 検証の準備 */
        final ItemStatsJsonDto byOriginMonth       = testResult.getByOriginMonth();
        final ItemStatJsonDto  actualStat          = byOriginMonth.find("2026-08");
        final double           actualCheckedMinutes = actualStat.getCheckedMinutes();

        /* 検証の実施 */
        Assertions.assertEquals(expectedCheckedMinutes, actualCheckedMinutes, "チェック済みの残り時間が一致しません");

    }

    /**
     * コンストラクタのテスト - 正常系:残数を引き継ぐ場合
     *
     * @since 0.1.0
     */
    @Test
    public void testConstructor_normalDeclaredCount() {

        /* 期待値の定義 */
        final Integer expectedDeclaredCount = 2;

        /* 準備 */
        final DailySummaryDto testDto = DailySummaryJsonDtoTest.createDayDto();

        /* テスト対象の実行 */
        final DailySummaryJsonDto testResult = new DailySummaryJsonDto(testDto);

        /* 検証の準備 */
        final Integer actualDeclaredCount = testResult.getDeclaredCount();

        /* 検証の実施 */
        Assertions.assertEquals(expectedDeclaredCount, actualDeclaredCount, "残数が一致しません");

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
        final DailySummaryJsonDto testTarget = DailySummaryJsonDtoTest.createTarget();

        /* テスト対象の実行 */
        final ItemStatsJsonDto testResult = testTarget.getByItem();

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
        final DailySummaryJsonDto testTarget = DailySummaryJsonDtoTest.createTarget();

        /* テスト対象の実行 */
        final ItemStatsJsonDto testResult = testTarget.getByOriginMonth();

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
        final DailySummaryJsonDto testTarget = DailySummaryJsonDtoTest.createTarget();

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
        final DailySummaryJsonDto testTarget = DailySummaryJsonDtoTest.createTarget();

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
        final DailySummaryJsonDto testTarget = DailySummaryJsonDtoTest.createTarget();

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
        final DailySummaryJsonDto testTarget = DailySummaryJsonDtoTest.createTarget();

        /* テスト対象の実行 */
        final ItemStatJsonDto testResult = testTarget.getTotal();

        /* 検証の準備 */
        final int actualTotal = testResult.getCount();

        /* 検証の実施 */
        Assertions.assertEquals(expectedTotal, actualTotal, "全体の集計が一致しません");

    }

    /**
     * JSON オブジェクトとしての集計を確認する<br>
     *
     * @since 0.1.0
     */
    @Test
    public void testGetByItem_normalJsonObject() {

        /* 期待値の定義 */
        final int expectedCount = 1;

        /* 準備 */
        final DailySummaryJsonDto testTarget = DailySummaryJsonDtoTest.createTarget();

        /* テスト対象の実行 */
        final ItemStatsJsonDto byItem = testTarget.getByItem();
        final boolean testResult = byItem.isEmpty();

        /* 検証の準備 */
        final Map<String, ItemStatJsonDto> stats = byItem.toJsonObject();
        final ItemStatJsonDto english = stats.get("英語");
        final int actualCount = english.getCount();

        /* 検証の実施 */
        Assertions.assertFalse(testResult, "項目ごとの集計は空ではない必要があります");
        Assertions.assertEquals(expectedCount, actualCount, "項目ごとの件数が一致しません");

    }

}
