package kmg.gr.gr002.dailytasks.carryover.domain.logic.impl;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverAggregationModel;
import kmg.gr.gr002.dailytasks.carryover.application.service.impl.CarryoverAggregateServiceImpl;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverItemModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailySummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.ItemStatModel;
import kmg.gr.gr002.dailytasks.carryover.application.types.MinutesSourceTypes;

/**
 * {@link CarryoverAggregateLogicImpl} のテスト<br>
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
public class CarryoverAggregateLogicImplTest {

    /**
     * テスト用の解析結果を作成する<br>
     *
     * @since 0.1.0
     *
     * @param number
     *                 Issue 番号
     * @param date
     *                 日付
     * @param sections
     *                 対象セクション名
     * @param items
     *                 持ち越し項目
     *
     * @return 解析結果
     */
    private static CarryoverIssueModel createIssue(final int number, final String date, final List<String> sections,
        final List<CarryoverItemModel> items) {

        final CarryoverIssueModel result
            = new CarryoverIssueModel(number, "title", date, "closed", "2026-10-06T00:00:00Z", sections, null, items);
        return result;

    }

    /**
     * テスト用の持ち越し項目を作成する<br>
     *
     * @since 0.1.0
     *
     * @param name
     *             項目名
     *
     * @return 持ち越し項目
     */
    private static CarryoverItemModel createItem(final String name) {

        final CarryoverItemModel result
            = new CarryoverItemModel(name, "2026-06-18", false, 15, MinutesSourceTypes.PARSED, "持ち越し", "raw");
        return result;

    }

    /**
     * aggregate メソッドのテスト - 正常系:対象セクションが現れた日以降を日付順に集計する場合
     *
     * @since 0.1.0
     */
    @Test
    public void testAggregate_normalFromFirstSection() {

        /* 期待値の定義 */
        final List<String>  expectedDates       = List.of("2026-03-24", "2026-03-25", "2026-03-26");
        final List<Integer> expectedCounts      = List.of(2, 0, 1);
        final List<String>  expectedItems       = List.of("音楽", "国語");
        final int           expectedLatestIssue = 177;

        /* 準備 */
        final List<CarryoverIssueModel>   testIssues = List.of(
            CarryoverAggregateLogicImplTest.createIssue(177, "2026-03-26", List.of("負債"),
                List.of(CarryoverAggregateLogicImplTest.createItem("音楽"))),
            CarryoverAggregateLogicImplTest.createIssue(176, "2026-03-25", List.of(), List.of()),
            CarryoverAggregateLogicImplTest.createIssue(175, "2026-03-24", List.of("負債"),
                List.of(CarryoverAggregateLogicImplTest.createItem("国語"),
                    CarryoverAggregateLogicImplTest.createItem("音楽"))),
            CarryoverAggregateLogicImplTest.createIssue(174, "2026-03-23", List.of(), List.of()),
            CarryoverAggregateLogicImplTest.createIssue(1, null, List.of("負債"), List.of()));
        final CarryoverAggregateLogicImpl testTarget = new CarryoverAggregateLogicImpl();

        /* テスト対象の実行 */
        final CarryoverSummaryModel testResult = testTarget.aggregate(testIssues);

        /* 検証の準備 */
        final List<DailySummaryModel> days = testResult.getDays();

        @SuppressWarnings("null")
        final List<String>  actualDates       = days.stream().map(DailySummaryModel::getDate).toList();
        final List<Integer> actualCounts      = days.stream().map(day -> {

            final ItemStatModel total = day.getTotal();
            return total.getCount();

        }).toList();
        final List<String>  actualItems       = testResult.getItems();
        final int           actualLatestIssue = testResult.getLatestIssue();

        /* 検証の実施 */
        Assertions.assertEquals(expectedDates, actualDates, "日付が一致しません");
        Assertions.assertEquals(expectedCounts, actualCounts, "件数が一致しません");
        Assertions.assertEquals(expectedItems, actualItems, "項目名が一致しません");
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");

    }

    /**
     * aggregate メソッドのテスト - 準正常系:解析結果がない場合
     *
     * @since 0.1.0
     */
    @Test
    public void testAggregate_semiEmpty() {

        /* 期待値の定義 */
        final int expectedLatestIssue = 0;

        /* 準備 */
        final CarryoverAggregateLogicImpl testTarget = new CarryoverAggregateLogicImpl();

        /* テスト対象の実行 */
        final CarryoverSummaryModel testResult = testTarget.aggregate(List.of());

        /* 検証の準備 */
        final int     actualLatestIssue = testResult.getLatestIssue();
        final List<DailySummaryModel> days             = testResult.getDays();
        final boolean                  actualDaysEmpty = days.isEmpty();

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");
        Assertions.assertTrue(actualDaysEmpty, "日別の集計は空になる必要があります");

    }

    /**
     * add メソッドのテスト - 正常系:2 単位に分けて足すと一括の集計と一致する場合
     *
     * @since 0.1.0
     */
    @Test
    public void testAdd_normalTwoBatches() {

        /* 期待値の定義 */
        final List<CarryoverIssueModel> expectedIssues = List.of(
            CarryoverAggregateLogicImplTest.createIssue(174, "2026-03-23", List.of(), List.of()),
            CarryoverAggregateLogicImplTest.createIssue(175, "2026-03-24", List.of("負債"),
                List.of(CarryoverAggregateLogicImplTest.createItem("国語"))),
            CarryoverAggregateLogicImplTest.createIssue(176, null, List.of("負債"),
                List.of(CarryoverAggregateLogicImplTest.createItem("音楽"))));

        /* 準備 */
        final CarryoverAggregateLogicImpl testTarget     = new CarryoverAggregateLogicImpl();
        final CarryoverAggregationModel   testAggregation = new CarryoverAggregationModel();
        final CarryoverSummaryModel       expectedSummary = testTarget.aggregate(expectedIssues);

        /* テスト対象の実行 */
        testTarget.add(testAggregation, expectedIssues.get(0));
        final CarryoverSummaryModel firstSummary = testTarget.toSummary(testAggregation);
        testTarget.add(testAggregation, expectedIssues.get(1));
        testTarget.add(testAggregation, expectedIssues.get(2));
        final CarryoverSummaryModel testResult = testTarget.toSummary(testAggregation);
        testTarget.add(testAggregation,
            CarryoverAggregateLogicImplTest.createIssue(100, "2026-03-25", List.of("負債"), List.of()));

        /* 検証の準備 */
        final List<DailySummaryModel> firstDays       = firstSummary.getDays();
        final boolean                 actualFirstEmpty = firstDays.isEmpty();
        final int                     actualFirstLatest = firstSummary.getLatestIssue();
        final List<DailySummaryModel> actualDays       = testResult.getDays();
        final List<DailySummaryModel> expectedDays     = expectedSummary.getDays();

        @SuppressWarnings("null")
        final List<String>            actualDates      = actualDays.stream().map(DailySummaryModel::getDate).toList();

        @SuppressWarnings("null")
        final List<String>            expectedDates    = expectedDays.stream().map(DailySummaryModel::getDate).toList();
        final int                     actualLatest     = testResult.getLatestIssue();
        final int                     expectedLatest   = expectedSummary.getLatestIssue();
        final List<String>            actualItems      = testResult.getItems();
        final List<String>            expectedItems    = expectedSummary.getItems();

        /* 検証の実施 */
        Assertions.assertTrue(actualFirstEmpty, "追跡を始める前の単位は日別 0 件になる必要があります");
        Assertions.assertEquals(174, actualFirstLatest, "日付のない Issue の前の最大番号が一致しません");
        Assertions.assertEquals(expectedDates, actualDates, "日付が一致しません");
        Assertions.assertEquals(expectedLatest, actualLatest, "最新の Issue 番号が一致しません");
        Assertions.assertEquals(expectedItems, actualItems, "項目名が一致しません");

    }

    /**
     * aggregate メソッドのテスト - 正常系:集計サービスが一括の集計を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testAggregate_normalService() {

        /* 期待値の定義 */
        final int expectedLatestIssue = 175;

        /* 準備 */
        final CarryoverIssueModel testIssue = CarryoverAggregateLogicImplTest.createIssue(175, "2026-03-24",
            List.of("負債"), List.of(CarryoverAggregateLogicImplTest.createItem("国語")));
        final CarryoverAggregateServiceImpl testTarget = new CarryoverAggregateServiceImpl(
            new CarryoverAggregateLogicImpl(), null);

        /* テスト対象の実行 */
        final CarryoverSummaryModel testResult = testTarget.aggregate(List.of(testIssue));

        /* 検証の準備 */
        final int actualLatestIssue = testResult.getLatestIssue();

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");

    }

}
