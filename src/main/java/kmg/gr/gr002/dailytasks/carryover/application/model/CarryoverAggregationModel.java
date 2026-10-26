package kmg.gr.gr002.dailytasks.carryover.application.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 単位をまたいで持ち越す集計の状態<br>
 * <p>
 * 実行のたびに新しく作る。共有の集計ロジックのフィールドには置かない。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class CarryoverAggregationModel {

    /**
     * ここまでの日別の集計（日付順）
     *
     * @since 0.1.0
     */
    private final List<DailySummaryModel> days;

    /**
     * 項目名ごとの延べ件数
     * <p>
     * 実行中の集計が項目名を数えるための索引。外には渡さない。
     * </p>
     *
     * @since 0.1.0
     */
    private final Map<String, Integer> itemCounts;

    /**
     * ここまでの最大の Issue 番号。Issue がない場合は 0
     *
     * @since 0.1.0
     */
    private int latestIssue;

    /**
     * 対象セクションの追跡を始めたか
     *
     * @since 0.1.0
     */
    private boolean tracking;

    /**
     * コンストラクタ<br>
     * <p>
     * 追跡はまだ始めていない。日別の集計は空にする。
     * </p>
     *
     * @since 0.1.0
     */
    public CarryoverAggregationModel() {

        this.days = new ArrayList<>();
        this.itemCounts = new HashMap<>();
        this.latestIssue = 0;
        this.tracking = false;

    }

    /**
     * 日別の集計を追加する<br>
     *
     * @since 0.1.0
     *
     * @param day
     *            日別の集計
     */
    public void addDay(final DailySummaryModel day) {

        this.days.add(day);

    }

    /**
     * 項目名の延べ件数を 1 増やす<br>
     *
     * @since 0.1.0
     *
     * @param name
     *             項目名
     */
    public void addItem(final String name) {

        final Integer current = this.itemCounts.get(name);

        /* まだ数えていない項目か */
        if (current == null) {

            // まだ数えていない項目の場合

            // 1 件として登録する
            this.itemCounts.put(name, 1);
            return;

        }

        // 延べ件数を 1 増やす
        final int next = current.intValue() + 1;
        this.itemCounts.put(name, next);

    }

    /**
     * ここまでの日別の集計を返す<br>
     *
     * @since 0.1.0
     *
     * @return 日別の集計（日付順）
     */
    public List<DailySummaryModel> getDays() {

        final List<DailySummaryModel> result = this.days;
        return result;

    }

    /**
     * ここまでの最大の Issue 番号を返す<br>
     *
     * @since 0.1.0
     *
     * @return 最大の Issue 番号。Issue がない場合は 0
     */
    public int getLatestIssue() {

        final int result = this.latestIssue;
        return result;

    }

    /**
     * 対象セクションの追跡を始めているかを返す<br>
     *
     * @since 0.1.0
     *
     * @return true：追跡を始めている、false：まだ始めていない
     */
    public boolean isTracking() {

        final boolean result = this.tracking;
        return result;

    }

    /**
     * 項目名を延べ件数の多い順に返す<br>
     *
     * @since 0.1.0
     *
     * @return 項目名（延べ件数の多い順、同じ件数なら名前の昇順）
     */
    @SuppressWarnings("null")
    public List<String> rankedItemNames() {

        final List<String> result = this.itemCounts.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed().thenComparing(Map.Entry.comparingByKey()))
            .map(Map.Entry::getKey).toList();
        return result;

    }

    /**
     * 対象セクションがあったとき、追跡を始める<br>
     *
     * @since 0.1.0
     *
     * @param hasSection
     *                   true：対象セクションがある、false：対象セクションがない
     */
    public void track(final boolean hasSection) {

        this.tracking = this.tracking || hasSection;

    }

    /**
     * 最大の Issue 番号を更新する<br>
     *
     * @since 0.1.0
     *
     * @param number
     *               Issue 番号
     */
    public void updateLatest(final int number) {

        /* ここまでの最大より大きいか */
        if (number > this.latestIssue) {

            // ここまでの最大より大きい場合

            // 最大の Issue 番号を更新する
            this.latestIssue = number;

        }

    }

}
