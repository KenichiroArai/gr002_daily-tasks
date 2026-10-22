package kmg.gr.gr002.dailytasks.carryover.repository.dto.json;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * キーごとの集計値の JSON の形<br>
 * <p>
 * 項目名、または持ち越し元の月（yyyy-MM）と集計値の対応。JSON では動的キーのオブジェクトになる。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class ItemStatsJsonDto {

    /**
     * キーと集計値の対応
     * <p>
     * 渡された順を保つクラス内の索引。JSON の並び順を変えないため。外には渡さない。
     * </p>
     *
     * @since 0.1.0
     */
    private final Map<String, ItemStatJsonDto> stats;

    /**
     * コンストラクタ<br>
     * <p>
     * 集計は空にする。
     * </p>
     *
     * @since 0.1.0
     */
    public ItemStatsJsonDto() {

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
    public ItemStatsJsonDto(final ItemStatsJsonDto source) {

        this();

        for (final String name : source.names()) {

            this.add(name, source.find(name));

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
    public void add(final String name, final ItemStatJsonDto stat) {

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
    public ItemStatJsonDto find(final String name) {

        final ItemStatJsonDto result = this.stats.get(name);
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

    /**
     * JSON オブジェクトとして書く<br>
     * <p>
     * 項目名や月は可変なキーなので、Jackson へ渡す出口だけ Map を返す。呼び出し側は {@link #find(String)} と {@link #names()} を使う。
     * </p>
     *
     * @since 0.1.0
     *
     * @return キーと集計値の対応（加えた順）
     */
    @JsonValue
    public Map<String, ItemStatJsonDto> toJsonObject() {

        final Map<String, ItemStatJsonDto> result = this.stats;
        return result;

    }

}
