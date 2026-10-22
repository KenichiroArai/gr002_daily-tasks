package kmg.gr.gr002.dailytasks.carryover.domain.dto;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import kmg.gr.gr002.dailytasks.carryover.application.model.ItemStatsModel;

/**
 * キーごとの集計値の DTO<br>
 * <p>
 * domain 層の受け渡しの型。項目名、または持ち越し元の月（yyyy-MM）と集計値の対応を管理する。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class ItemStatsDto {

    /**
     * キーと集計値の対応
     * <p>
     * 渡された順を保つクラス内の索引。JSON の並び順を変えないため。外には渡さない。
     * </p>
     *
     * @since 0.1.0
     */
    private final Map<String, ItemStatDto> stats;

    /**
     * コンストラクタ<br>
     * <p>
     * 集計は空にする。
     * </p>
     *
     * @since 0.1.0
     */
    public ItemStatsDto() {

        this.stats = new LinkedHashMap<>();

    }

    /**
     * コンストラクタ<br>
     * <p>
     * 並び順は元のままにする。
     * </p>
     *
     * @since 0.1.0
     *
     * @param source
     *               複製元
     */
    public ItemStatsDto(final ItemStatsDto source) {

        this();

        for (final String name : source.names()) {

            this.add(name, source.find(name));

        }

    }

    /**
     * 集計値から保存用の DTO を作る<br>
     * <p>
     * 並び順は元のままにする。
     * </p>
     *
     * @since 0.1.0
     *
     * @param stats
     *              キーごとの集計値
     */
    public ItemStatsDto(final ItemStatsModel stats) {

        this();

        for (final String name : stats.names()) {

            this.add(name, new ItemStatDto(stats.find(name)));

        }

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
    public void add(final String name, final ItemStatDto stat) {

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
    public ItemStatDto find(final String name) {

        final ItemStatDto result = this.stats.get(name);
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
     * キーを加えた順で返す<br>
     *
     * @since 0.1.0
     *
     * @return キー（加えた順）
     */
    public List<String> names() {

        final List<String> result = List.copyOf(this.stats.keySet());
        return result;

    }

}
