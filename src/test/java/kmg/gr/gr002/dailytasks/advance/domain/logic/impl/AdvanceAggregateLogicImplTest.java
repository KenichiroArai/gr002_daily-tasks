package kmg.gr.gr002.dailytasks.advance.domain.logic.impl;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverAggregationModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverItemModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailySummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.ItemStatModel;
import kmg.gr.gr002.dailytasks.carryover.application.types.MinutesSourceTypes;
import kmg.gr.gr002.dailytasks.carryover.domain.types.TargetSectionTypes;
import kmg.gr.gr002.dailytasks.testutil.DailyTasksMessageSourceTestUtil;

/**
 * {@link AdvanceAggregateLogicImpl} のテスト<br>
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
public class AdvanceAggregateLogicImplTest {

    /**
     * テスト用の先行項目を作成する<br>
     *
     * @since 0.2.0
     *
     * @param name
     *             項目名
     *
     * @return 持ち越し項目
     */
    private static CarryoverItemModel createAdvanceItem(final String name) {

        final CarryoverItemModel result = new CarryoverItemModel(name, LocalDate.parse("2026-06-18"), false, 20,
            MinutesSourceTypes.PARSED, TargetSectionTypes.ADVANCE.getSection(), "raw");
        return result;

    }

    /**
     * テスト用の解析結果を作成する<br>
     *
     * @since 0.2.0
     *
     * @param number
     *                             Issue 番号
     * @param date
     *                             日付
     * @param sections
     *                             対象セクション名
     * @param advanceDeclaredCount
     *                             本文の「件数：N」
     * @param items
     *                             持ち越し項目
     *
     * @return 解析結果
     */
    private static CarryoverIssueModel createIssue(final int number, final String date, final List<String> sections,
        final Integer advanceDeclaredCount, final List<CarryoverItemModel> items) {

        final CarryoverIssueModel result
            = new CarryoverIssueModel(number, "title", (date == null) ? null : LocalDate.parse(date), "closed",
                Instant.parse("2026-10-06T00:00:00Z"), sections, null, advanceDeclaredCount, items);
        return result;

    }

    /**
     * add メソッドのテスト - 正常系:「先行」が現れるまで日別 0 件の場合
     *
     * @since 0.2.0
     */
    @Test
    public void testAdd_normalBeforeAdvanceSection() {

        /* 期待値の定義 */
        final CarryoverIssueModel issueBefore  = AdvanceAggregateLogicImplTest.createIssue(174, "2026-03-23", List.of(),
            null, List.of());
        final CarryoverIssueModel issueAdvance = AdvanceAggregateLogicImplTest.createIssue(175, "2026-03-24",
            List.of(TargetSectionTypes.ADVANCE.getSection()), 1,
            List.of(AdvanceAggregateLogicImplTest.createAdvanceItem("国語")));

        /* 準備 */
        final AdvanceAggregateLogicImpl testTarget      = new AdvanceAggregateLogicImpl(
            DailyTasksMessageSourceTestUtil.create());
        final CarryoverAggregationModel testAggregation = new CarryoverAggregationModel();

        /* テスト対象の実行 */
        testTarget.add(testAggregation, issueBefore);
        final CarryoverSummaryModel firstSummary = testTarget.toSummary(testAggregation);
        testTarget.add(testAggregation, issueAdvance);
        final CarryoverSummaryModel testResult = testTarget.toSummary(testAggregation);

        /* 検証の準備 */
        final boolean actualFirstEmpty = firstSummary.getDays().isEmpty();
        final int     actualDayCount   = testResult.getDays().size();

        /* 検証の実施 */
        Assertions.assertTrue(actualFirstEmpty, "「先行」が現れる前は日別 0 件になる必要があります");
        Assertions.assertEquals(1, actualDayCount, "「先行」以降は 1 日分の集計が必要です");

    }

    /**
     * add メソッドのテスト - 準正常系:日付がない場合は日別の行を作らない
     *
     * @since 0.2.0
     */
    @Test
    public void testAdd_semiNoDate() {

        /* 期待値の定義 */
        final int expectedLatestIssue = 176;

        /* 準備 */
        final CarryoverIssueModel       testIssue       = AdvanceAggregateLogicImplTest.createIssue(176, null,
            List.of(TargetSectionTypes.ADVANCE.getSection()), 1,
            List.of(AdvanceAggregateLogicImplTest.createAdvanceItem("国語")));
        final AdvanceAggregateLogicImpl testTarget      = new AdvanceAggregateLogicImpl(
            DailyTasksMessageSourceTestUtil.create());
        final CarryoverAggregationModel testAggregation = new CarryoverAggregationModel();

        /* テスト対象の実行 */
        testTarget.add(testAggregation, testIssue);
        final CarryoverSummaryModel testResult = testTarget.toSummary(testAggregation);

        /* 検証の準備 */
        final boolean actualDaysEmpty   = testResult.getDays().isEmpty();
        final int     actualLatestIssue = testResult.getLatestIssue();

        /* 検証の実施 */
        Assertions.assertTrue(actualDaysEmpty, "日付がないときは日別の行を作らない必要があります");
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");

    }

    /**
     * add メソッドのテスト - 準正常系:追跡開始前は日別の行を作らない
     *
     * @since 0.2.0
     */
    @Test
    public void testAdd_semiNotTracking() {

        /* 期待値の定義 */

        /* 準備 */
        final CarryoverIssueModel       testIssue       = AdvanceAggregateLogicImplTest.createIssue(174, "2026-03-23",
            List.of(), null, List.of());
        final AdvanceAggregateLogicImpl testTarget      = new AdvanceAggregateLogicImpl(
            DailyTasksMessageSourceTestUtil.create());
        final CarryoverAggregationModel testAggregation = new CarryoverAggregationModel();

        /* テスト対象の実行 */
        testTarget.add(testAggregation, testIssue);
        final CarryoverSummaryModel testResult = testTarget.toSummary(testAggregation);

        /* 検証の準備 */
        final boolean actualDaysEmpty = testResult.getDays().isEmpty();
        final boolean actualTracking  = testAggregation.isTracking();

        /* 検証の実施 */
        Assertions.assertTrue(actualDaysEmpty, "追跡開始前は日別の行を作らない必要があります");
        Assertions.assertFalse(actualTracking, "「先行」が現れる前は追跡中ではない必要があります");

    }

    /**
     * aggregate メソッドのテスト - 正常系:「先行」セクション以降を日付順に集計する場合
     *
     * @since 0.2.0
     */
    @Test
    public void testAggregate_normalFromFirstAdvanceSection() {

        /* 期待値の定義 */
        final List<LocalDate> expectedDates       = List.of(LocalDate.parse("2026-03-24"),
            LocalDate.parse("2026-03-25"));
        final List<Integer>   expectedCounts      = List.of(1, 0);
        final List<String>    expectedItems       = List.of("国語");
        final int             expectedLatestIssue = 176;

        /* 準備 */
        final List<CarryoverIssueModel> testIssues = List.of(
            AdvanceAggregateLogicImplTest.createIssue(176, "2026-03-25", List.of(), null, List.of()),
            AdvanceAggregateLogicImplTest.createIssue(175, "2026-03-24",
                List.of(TargetSectionTypes.ADVANCE.getSection()), 1,
                List.of(AdvanceAggregateLogicImplTest.createAdvanceItem("国語"),
                    new CarryoverItemModel("音楽", LocalDate.parse("2026-06-18"), false, 15, MinutesSourceTypes.PARSED,
                        TargetSectionTypes.CARRYOVER.getSection(), "raw"))),
            AdvanceAggregateLogicImplTest.createIssue(174, "2026-03-23", List.of(), null, List.of()));
        final AdvanceAggregateLogicImpl testTarget = new AdvanceAggregateLogicImpl(
            DailyTasksMessageSourceTestUtil.create());

        /* テスト対象の実行 */
        final CarryoverSummaryModel testResult = testTarget.aggregate(testIssues);

        /* 検証の準備 */
        final List<DailySummaryModel> days = testResult.getDays();

        @SuppressWarnings("null")
        final List<LocalDate> actualDates       = days.stream().map(DailySummaryModel::getDate).toList();
        final List<Integer>   actualCounts      = days.stream().map(day -> {

                                                    final ItemStatModel total = day.getTotal();
                                                    return total.getCount();

                                                }).toList();
        final List<String>    actualItems       = testResult.getItems();
        final int             actualLatestIssue = testResult.getLatestIssue();

        /* 検証の実施 */
        Assertions.assertEquals(expectedDates, actualDates, "日付が一致しません");
        Assertions.assertEquals(expectedCounts, actualCounts, "件数が一致しません");
        Assertions.assertEquals(expectedItems, actualItems, "項目名が一致しません");
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");

    }

    /**
     * aggregate メソッドのテスト - 正常系:日付なし Issue を日付順の後に足す場合
     *
     * @since 0.2.0
     */
    @Test
    public void testAggregate_normalNullDateIssue() {

        /* 期待値の定義 */
        final int expectedLatestIssue = 176;

        /* 準備 */
        final List<CarryoverIssueModel> testIssues = List.of(
            AdvanceAggregateLogicImplTest.createIssue(176, "2026-03-25", List.of(), null, List.of()),
            AdvanceAggregateLogicImplTest.createIssue(175, "2026-03-24",
                List.of(TargetSectionTypes.ADVANCE.getSection()), 1,
                List.of(AdvanceAggregateLogicImplTest.createAdvanceItem("国語"))),
            AdvanceAggregateLogicImplTest.createIssue(174, null, List.of(TargetSectionTypes.ADVANCE.getSection()),
                1, List.of(AdvanceAggregateLogicImplTest.createAdvanceItem("音楽"))));
        final AdvanceAggregateLogicImpl testTarget = new AdvanceAggregateLogicImpl(
            DailyTasksMessageSourceTestUtil.create());

        /* テスト対象の実行 */
        final CarryoverSummaryModel testResult = testTarget.aggregate(testIssues);

        /* 検証の準備 */
        final int actualLatestIssue = testResult.getLatestIssue();

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");

    }

    /**
     * aggregate メソッドのテスト - 準正常系:解析結果がない場合
     *
     * @since 0.2.0
     */
    @Test
    public void testAggregate_semiEmpty() {

        /* 期待値の定義 */
        final int expectedLatestIssue = 0;

        /* 準備 */
        final AdvanceAggregateLogicImpl testTarget
            = new AdvanceAggregateLogicImpl(DailyTasksMessageSourceTestUtil.create());

        /* テスト対象の実行 */
        final CarryoverSummaryModel testResult = testTarget.aggregate(List.of());

        /* 検証の準備 */
        final int                     actualLatestIssue = testResult.getLatestIssue();
        final List<DailySummaryModel> days              = testResult.getDays();
        final boolean                 actualDaysEmpty   = days.isEmpty();

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");
        Assertions.assertTrue(actualDaysEmpty, "日別の集計は空になる必要があります");

    }

    /**
     * toSummary メソッドのテスト - 正常系:集計の状態から画面用の集計を作る場合
     *
     * @since 0.2.0
     */
    @Test
    public void testToSummary_normalFromAggregation() {

        /* 期待値の定義 */
        final int          expectedLatestIssue = 175;
        final List<String> expectedItems       = List.of("国語");

        /* 準備 */
        final AdvanceAggregateLogicImpl testTarget      = new AdvanceAggregateLogicImpl(
            DailyTasksMessageSourceTestUtil.create());
        final CarryoverAggregationModel testAggregation = new CarryoverAggregationModel();
        final CarryoverIssueModel       testIssue       = AdvanceAggregateLogicImplTest.createIssue(175, "2026-03-24",
            List.of(TargetSectionTypes.ADVANCE.getSection()), 1,
            List.of(AdvanceAggregateLogicImplTest.createAdvanceItem("国語")));
        testTarget.add(testAggregation, testIssue);

        /* テスト対象の実行 */
        final CarryoverSummaryModel testResult = testTarget.toSummary(testAggregation);

        /* 検証の準備 */
        final int          actualLatestIssue = testResult.getLatestIssue();
        final List<String> actualItems       = testResult.getItems();

        /* 検証の実施 */
        Assertions.assertEquals(expectedLatestIssue, actualLatestIssue, "最新の Issue 番号が一致しません");
        Assertions.assertEquals(expectedItems, actualItems, "項目名が一致しません");

    }

}
