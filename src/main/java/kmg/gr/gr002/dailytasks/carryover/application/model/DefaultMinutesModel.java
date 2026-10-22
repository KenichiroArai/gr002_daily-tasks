package kmg.gr.gr002.dailytasks.carryover.application.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 項目ごとの標準時間<br>
 * <p>
 * 時間表記のない持ち越し項目の残り時間を補完するために使う。項目名と標準時間の対応を管理する。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class DefaultMinutesModel {

    /**
     * 項目名と標準時間（分）の対応
     * <p>
     * 項目名は可変なので、クラス内の索引に Map を使う。外には渡さない。
     * </p>
     *
     * @since 0.1.0
     */
    private final Map<String, Double> minutesByName;

    /**
     * コンストラクタ<br>
     * <p>
     * 標準時間は空にする。
     * </p>
     *
     * @since 0.1.0
     */
    public DefaultMinutesModel() {

        this.minutesByName = new LinkedHashMap<>();

    }

    /**
     * 項目の標準時間を加える<br>
     *
     * @since 0.1.0
     *
     * @param name
     *                項目名
     * @param minutes
     *                標準時間（分）
     */
    public void add(final String name, final double minutes) {

        this.minutesByName.put(name, minutes);

    }

    /**
     * 項目の標準時間を返す<br>
     *
     * @since 0.1.0
     *
     * @param name
     *             項目名
     *
     * @return 標準時間（分）。未登録の場合は null
     */
    public Double find(final String name) {

        final Double result = this.minutesByName.get(name);
        return result;

    }

}
