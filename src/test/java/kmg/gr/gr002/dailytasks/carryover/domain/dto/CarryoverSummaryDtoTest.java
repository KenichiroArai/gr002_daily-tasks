package kmg.gr.gr002.dailytasks.carryover.domain.dto;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverItemModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailySummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.types.MinutesSourceTypes;

/**
 * {@link CarryoverSummaryDto} のテスト<br>
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
public class CarryoverSummaryDtoTest {

    /**
     * 画面用の集計を作成する<br>
     *
     * @since 0.1.0
     *
     * @return 画面用の集計
     */
    private static CarryoverSummaryModel createSummary() {

        final CarryoverItemModel  item  = new CarryoverItemModel("音楽", "2026-08-18", true, 11.5,
            MinutesSourceTypes.PARSED, "持ち越し", "- [x] 音楽2026/08/18（残り時間：11.5分）");
        final CarryoverIssueModel issue = new CarryoverIssueModel(371, "2026年10月06日のタスク", "2026-10-06", "open",
            "2026-10-06T14:23:08Z", List.of("持ち越し"), 1, List.of(item));
        final DailySummaryModel   day   = new DailySummaryModel("2026-10-06", 371, 1);
        day.add(issue.getItems().get(0));
        final CarryoverSummaryModel result = new CarryoverSummaryModel(371, List.of("音楽"), List.of(day));
        return result;

    }

    /**
     * テスト対象を作成する<br>
     *
     * @since 0.1.0
     *
     * @return テスト対象
     */
    private static CarryoverSummaryDto createTarget() {

        final ItemStatDto         total  = new ItemStatDto(1, 30, 0, 0);
        final ItemStatsDto     byItem = new ItemStatsDto();
        byItem.add("英語", total);
        final DailySummaryDto     day    = new DailySummaryDto("2026-03-24", 175, null, total, byItem,
            new ItemStatsDto());
        final CarryoverSummaryDto result = new CarryoverSummaryDto(374, List.of("英語"), List.of(day));
        return result;

    }

    /**
     * コンストラクタのテスト - 正常系:項目ごとの集計を変換する場合
     *
     * @since 0.1.0
     */
    @Test
    public void testConstructor_normalByItem() {

        /* 期待値の定義 */
        final int expectedCheckedCount = 1;

        /* 準備 */
        final CarryoverSummaryModel testSummary = CarryoverSummaryDtoTest.createSummary();

        /* テスト対象の実行 */
        final CarryoverSummaryDto testResult = new CarryoverSummaryDto(testSummary);

        /* 検証の準備 */
        final DailySummaryDto actualDay          = testResult.getDays().get(0);
        final int             actualCheckedCount = actualDay.getByItem().find("音楽").getCheckedCount();

        /* 検証の実施 */
        Assertions.assertEquals(expectedCheckedCount, actualCheckedCount, "項目ごとのチェック済み件数が一致しません");

    }

    /**
     * コンストラクタのテスト - 正常系:持ち越し元の月ごとの集計を変換する場合
     *
     * @since 0.1.0
     */
    @Test
    public void testConstructor_normalByOriginMonth() {

        /* 期待値の定義 */
        final int expectedCount = 1;

        /* 準備 */
        final CarryoverSummaryModel testSummary = CarryoverSummaryDtoTest.createSummary();

        /* テスト対象の実行 */
        final CarryoverSummaryDto testResult = new CarryoverSummaryDto(testSummary);

        /* 検証の準備 */
        final DailySummaryDto actualDay   = testResult.getDays().get(0);
        final int             actualCount = actualDay.getByOriginMonth().find("2026-08").getCount();

        /* 検証の実施 */
        Assertions.assertEquals(expectedCount, actualCount, "持ち越し元の月ごとの件数が一致しません");

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
        final CarryoverSummaryModel testSummary = CarryoverSummaryDtoTest.createSummary();

        /* テスト対象の実行 */
        final CarryoverSummaryDto testResult = new CarryoverSummaryDto(testSummary);

        /* 検証の準備 */
        final int actualLatestIssue = testResult.getLatestIssue();

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");

    }

    /**
     * コンストラクタのテスト - 正常系:日別の全体の集計を変換する場合
     *
     * @since 0.1.0
     */
    @Test
    public void testConstructor_normalTotal() {

        /* 期待値の定義 */
        final double expectedMinutes = 11.5;

        /* 準備 */
        final CarryoverSummaryModel testSummary = CarryoverSummaryDtoTest.createSummary();

        /* テスト対象の実行 */
        final CarryoverSummaryDto testResult = new CarryoverSummaryDto(testSummary);

        /* 検証の準備 */
        final ItemStatDto actualTotal   = testResult.getDays().get(0).getTotal();
        final double      actualMinutes = actualTotal.getMinutes();

        /* 検証の実施 */
        Assertions.assertEquals(expectedMinutes, actualMinutes, "全体の残り時間が一致しません");

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
        final CarryoverSummaryDto testTarget = CarryoverSummaryDtoTest.createTarget();

        /* テスト対象の実行 */
        final List<DailySummaryDto> testResult = testTarget.getDays();

        /* 検証の準備 */
        @SuppressWarnings("null")
        final List<String> actualDays = testResult.stream().map(DailySummaryDto::getDate).toList();

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
        final CarryoverSummaryDto testTarget = CarryoverSummaryDtoTest.createTarget();

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
        final CarryoverSummaryDto testTarget = CarryoverSummaryDtoTest.createTarget();

        /* テスト対象の実行 */
        final int testResult = testTarget.getLatestIssue();

        /* 検証の準備 */
        final int actualLatestIssue = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");

    }

}
