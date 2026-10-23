package kmg.gr.gr002.dailytasks.carryover.application.model;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * キーごとの集計値<br>
 * <p>
 * 項目名、または持ち越し元の月（yyyy-MM）と集計値の対応を管理する。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class ItemStatsModel {

    /**
     * キーと集計値の対応
     * <p>
     * キーの昇順で辿るクラス内の索引。外には渡さない。
     * </p>
     *
     * @since 0.1.0
     */
    private final Map<String, ItemStatModel> stats;

    /**
     * コンストラクタ<br>
     * <p>
     * 集計は空にする。
     * </p>
     *
     * @since 0.1.0
     */
    public ItemStatsModel() {

        this.stats = new TreeMap<>();

    }

    /**
     * 集計値を加える<br>
     *
     * @since 0.1.0
     *
     * @param name
     *             キー（項目名、または yyyy-MM）
     * @param stat
     *             集計値
     */
    public void add(final String name, final ItemStatModel stat) {

        this.stats.put(name, stat);

    }

    /**
     * キーの集計値を返す<br>
     *
     * @since 0.1.0
     *
     * @param name
     *             キー（項目名、または yyyy-MM）
     *
     * @return 集計値。未登録の場合は null
     */
    public ItemStatModel find(final String name) {

        final ItemStatModel result = this.stats.get(name);
        return result;

    }

    /**
     * キーの集計値を返す<br>
     * <p>
     * 未登録のキーは、空の集計値を加えてから返す。
     * </p>
     *
     * @since 0.1.0
     *
     * @param name
     *             キー（項目名、または yyyy-MM）
     *
     * @return 集計値
     */
    public ItemStatModel findOrCreate(final String name) {

        ItemStatModel result = this.find(name);

        /* 集計値があるか */
        if (result != null) {

            // 集計値がある場合

            // 既存の集計値を返す
            return result;

        }

        result = new ItemStatModel();
        this.add(name, result);
        return result;

    }

    /**
     * 集計が空かを返す<br>
     *
     * @since 0.1.0
     *
     * @return 空の場合は true
     */
    public boolean isEmpty() {

        final boolean result = this.stats.isEmpty();
        return result;

    }

    /**
     * キーを昇順で返す<br>
     *
     * @since 0.1.0
     *
     * @return キー（昇順）
     */
    public List<String> names() {

        final List<String> result = List.copyOf(this.stats.keySet());
        return result;

    }

}
