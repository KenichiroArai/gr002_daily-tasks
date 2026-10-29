package kmg.gr.gr002.dailytasks.carryover.domain.logic.impl;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Component;

import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverAggregateLogic;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverAggregationModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverItemModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailySummaryModel;

/**
 * 持ち越しの解析結果を日別に集計する実装<br>
 * <p>
 * 対象セクションが最初に現れた Issue 以降を集計対象とする。それ以降で対象セクションがない日は 0 件として扱う。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@Component
public class CarryoverAggregateLogicImpl implements CarryoverAggregateLogic {

    /**
     * 解析結果を 1 件、集計の状態へ足す<br>
     * <p>
     * 日付がある Issue は、呼ぶ側が日付順に渡す。
     * </p>
     *
     * @since 0.1.0
     *
     * @param aggregation
     *                    単位をまたいで持ち越す集計の状態
     * @param issue
     *                    持ち越しの解析結果
     */
    @Override
    public void add(final CarryoverAggregationModel aggregation, final CarryoverIssueModel issue) {

        aggregation.updateLatest(issue.getNumber());

        /* 日付がないか */
        final String date = issue.getDate();

        if (date == null) {

            // 日付がない場合

            // 最大の Issue 番号だけ更新して次へ進む
            return;

        }

        /* セクション追跡の更新 */
        final boolean hasSection = issue.hasSection();
        aggregation.track(hasSection);

        /* 集計対象の追跡中か */
        final boolean tracking = aggregation.isTracking();

        if (!tracking) {

            // 集計対象の追跡中ではない場合

            // 日別の行にはしない
            return;

        }

        /* 日別集計への加算 */
        final DailySummaryModel day = new DailySummaryModel(date, issue.getNumber(), issue.getDeclaredCount());

        for (final CarryoverItemModel item : issue.getItems()) {

            day.add(item);
            aggregation.addItem(item.getName());

        }

        aggregation.addDay(day);

    }

    /**
     * 解析結果を集計する<br>
     *
     * @since 0.1.0
     *
     * @param issues
     *               持ち越しの解析結果
     *
     * @return 画面用の持ち越しの集計
     */
    @SuppressWarnings("null")
    @Override
    public CarryoverSummaryModel aggregate(final Collection<CarryoverIssueModel> issues) {

        final CarryoverSummaryModel     result;
        final CarryoverAggregationModel aggregation = new CarryoverAggregationModel();

        /* 日付のある Issue を日付順に並べる */
        final List<CarryoverIssueModel> sorted = issues.stream().filter(issue -> issue.getDate() != null)
            .sorted(Comparator.comparing(CarryoverIssueModel::getDate).thenComparingInt(CarryoverIssueModel::getNumber))
            .toList();

        /* 日付順 Issue の集計 */
        for (final CarryoverIssueModel issue : sorted) {

            this.add(aggregation, issue);

        }

        /* 日付なし Issue の集計 */
        for (final CarryoverIssueModel issue : issues) {

            /* 日付があるか */
            final String date = issue.getDate();

            if (date != null) {

                // 日付がある場合

                // 日付順の追加で足し終わっている
                continue;

            }

            this.add(aggregation, issue);

        }

        /* 画面用集計への変換 */
        result = this.toSummary(aggregation);
        return result;

    }

    /**
     * ここまでの集計の状態から画面用の集計を作る<br>
     *
     * @since 0.1.0
     *
     * @param aggregation
     *                    単位をまたいで持ち越す集計の状態
     *
     * @return 画面用の持ち越しの集計
     */
    @Override
    public CarryoverSummaryModel toSummary(final CarryoverAggregationModel aggregation) {

        final CarryoverSummaryModel result = new CarryoverSummaryModel(aggregation.getLatestIssue(),
            aggregation.rankedItemNames(), aggregation.getDays());
        return result;

    }

}
