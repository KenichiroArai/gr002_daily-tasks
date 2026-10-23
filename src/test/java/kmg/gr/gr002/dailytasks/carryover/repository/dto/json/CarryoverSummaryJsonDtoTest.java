package kmg.gr.gr002.dailytasks.carryover.repository.dto.json;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverSummaryDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.DailySummaryDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.ItemStatDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.ItemStatsDto;

/**
 * {@link CarryoverSummaryJsonDto} のテスト<br>
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
public class CarryoverSummaryJsonDtoTest {

    /**
     * テスト対象を作成する<br>
     *
     * @since 0.1.0
     *
     * @return テスト対象
     */
    private static CarryoverSummaryJsonDto createTarget() {

        final ItemStatJsonDto         total  = new ItemStatJsonDto(1, 30, 0, 0);
        final ItemStatsJsonDto        byItem = new ItemStatsJsonDto();
        byItem.add("英語", total);
        final DailySummaryJsonDto     day    = new DailySummaryJsonDto("2026-03-24", 175, null, total, byItem,
            new ItemStatsJsonDto());
        final CarryoverSummaryJsonDto result = new CarryoverSummaryJsonDto(374, List.of("英語"), List.of(day));
        return result;

    }

    /**
     * 画面用の集計の DTO を作成する<br>
     *
     * @since 0.1.0
     *
     * @return 画面用の集計の DTO
     */
    private static CarryoverSummaryDto createSummaryDto() {

        final ItemStatDto         total         = new ItemStatDto(2, 26.5, 1, 11.5);
        final ItemStatsDto        byItem        = new ItemStatsDto();
        final ItemStatsDto        byOriginMonth = new ItemStatsDto();
        byItem.add("音楽", new ItemStatDto(1, 11.5, 1, 11.5));
        byItem.add("国語", new ItemStatDto(1, 15.0, 0, 0.0));
        byOriginMonth.add("2026-08", new ItemStatDto(1, 11.5, 1, 11.5));
        final DailySummaryDto     day    = new DailySummaryDto("2026-10-06", 371, 2, total, byItem, byOriginMonth);
        final CarryoverSummaryDto result = new CarryoverSummaryDto(371, List.of("音楽", "国語"), List.of(day));
        return result;

    }

    /**
     * コンストラクタのテスト - 正常系:最新の Issue 番号を引き継ぐ場合
     *
     * @since 0.1.0
     */
    @Test
    public void testConstructor_normalLatestIssue() {

        /* 期待値の定義 */
        final int expectedLatestIssue = 371;

        /* 準備 */
        final CarryoverSummaryDto testDto = CarryoverSummaryJsonDtoTest.createSummaryDto();

        /* テスト対象の実行 */
        final CarryoverSummaryJsonDto testResult = new CarryoverSummaryJsonDto(testDto);

        /* 検証の準備 */
        final int actualLatestIssue = testResult.getLatestIssue();

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");

    }

    /**
     * getDays メソッドのテスト - 正常系:日別の集計を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDays_normalValue() {

        /* 期待値の定義 */
        final List<String> expectedDays = List.of("2026-03-24");

        /* 準備 */
        final CarryoverSummaryJsonDto testTarget = CarryoverSummaryJsonDtoTest.createTarget();

        /* テスト対象の実行 */
        final List<DailySummaryJsonDto> testResult = testTarget.getDays();

        /* 検証の準備 */
        @SuppressWarnings("null")
        final List<String> actualDays = testResult.stream().map(DailySummaryJsonDto::getDate).toList();

        /* 検証の実施 */
        Assertions.assertEquals(expectedDays, actualDays, "日別の集計が一致しません");

    }

    /**
     * getItems メソッドのテスト - 正常系:項目名を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetItems_normalValue() {

        /* 期待値の定義 */
        final List<String> expectedItems = List.of("英語");

        /* 準備 */
        final CarryoverSummaryJsonDto testTarget = CarryoverSummaryJsonDtoTest.createTarget();

        /* テスト対象の実行 */
        final List<String> testResult = testTarget.getItems();

        /* 検証の準備 */
        final List<String> actualItems = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedItems, actualItems, "項目名が一致しません");

    }

    /**
     * getLatestIssue メソッドのテスト - 正常系:最新の Issue 番号を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetLatestIssue_normalValue() {

        /* 期待値の定義 */
        final int expectedLatestIssue = 374;

        /* 準備 */
        final CarryoverSummaryJsonDto testTarget = CarryoverSummaryJsonDtoTest.createTarget();

        /* テスト対象の実行 */
        final int testResult = testTarget.getLatestIssue();

        /* 検証の準備 */
        final int actualLatestIssue = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");

    }

}
