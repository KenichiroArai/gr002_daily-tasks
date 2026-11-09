package kmg.gr.gr002.dailytasks.carryover.domain.logic.impl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import kmg.core.infrastructure.utils.KmgListUtils;
import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverBatchLogic;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueRefModel;

/**
 * 日々のタスク Issue の参照を単位に分ける実装<br>
 * <p>
 * 参照と単位の件数は {@link #initialize(List, int)} で受け取り、フィールドに保持する。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@Component
@Scope("prototype")
public class CarryoverBatchLogicImpl implements CarryoverBatchLogic {

    /**
     * 単位（Issue の件数）。1 未満のときは全件を 1 単位にする
     *
     * @since 0.1.0
     */
    private int batchSize;

    /**
     * 本文を持たない参照
     *
     * @since 0.1.0
     */
    private List<DailyTaskIssueRefModel> refs;

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     */
    public CarryoverBatchLogicImpl() {

        this.refs = List.of();

    }

    /**
     * 参照と単位の件数を受け取り、フィールドに保持する<br>
     * <p>
     * {@link #split()} より前に呼ぶ。
     * </p>
     *
     * @since 0.1.0
     *
     * @param refs
     *                  本文を持たない参照
     * @param batchSize
     *                  単位（Issue の件数）。1 未満のときは全件を 1 単位にする
     */
    @Override
    public void initialize(final List<DailyTaskIssueRefModel> refs, final int batchSize) {

        this.refs = refs;
        this.batchSize = batchSize;

    }

    /**
     * 保持した参照を単位に分ける<br>
     * <p>
     * 日付がある参照は日付順に batchSize 件ずつ。日付がない参照は最後の 1 単位。batchSize が 1 未満のときは全件を 1 単位にする。参照がないときは空の 1 単位にする。
     * </p>
     *
     * @since 0.1.0
     *
     * @return 単位の一覧
     */
    @Override
    @SuppressWarnings("null")
    public List<List<DailyTaskIssueRefModel>> split() {

        final List<List<DailyTaskIssueRefModel>> result = new ArrayList<>();
        /* 日付あり・なしの振り分け */
        final List<DailyTaskIssueRefModel> dated
                                                   = this.refs
                .stream().filter(ref -> ref.getDate() != null).sorted(Comparator
                    .comparing(DailyTaskIssueRefModel::getDate).thenComparingInt(DailyTaskIssueRefModel::getNumber))
                .toList();
        final List<DailyTaskIssueRefModel> undated = this.refs.stream().filter(ref -> ref.getDate() == null).toList();

        /* 全件を 1 単位にするか */
        if (this.batchSize < 1) {

            // 全件を 1 単位にする場合

            // 日付順のあとに、日付がない参照を続ける
            final List<DailyTaskIssueRefModel> all = new ArrayList<>(dated);
            all.addAll(undated);
            result.add(all);
            return result;

        }

        final int datedCount = dated.size();
        int       index      = 0;

        /* 日付のある参照が残っているか */
        boolean remaining = index < datedCount;

        while (remaining) {

            // 日付のある参照が残っている場合

            // 単位の件数だけ取り出す
            final int end = Math.min(index + this.batchSize, datedCount);
            result.add(new ArrayList<>(dated.subList(index, end)));
            index = end;

            /* 日付のある参照が残っているか */
            remaining = index < datedCount;

        }

        /* 日付がない参照があるか */
        final boolean hasUndated = KmgListUtils.isNotEmpty(undated);

        if (hasUndated) {

            // 日付がない参照がある場合

            // 最後の単位にする
            result.add(new ArrayList<>(undated));

        }

        /* 参照がないか */
        final boolean empty = KmgListUtils.isEmpty(result);

        if (empty) {

            // 参照がない場合

            // 空の集計を 1 回書くための単位にする
            result.add(new ArrayList<>());

        }

        return result;

    }

}
