package kmg.gr.gr002.dailytasks.carryover.domain.dto;

import java.util.LinkedHashMap;
import java.util.Map;

import kmg.gr.gr002.dailytasks.carryover.application.model.DefaultMinutesModel;

/**
 * 項目ごとの標準時間の DTO<br>
 * <p>
 * domain 層が repository 層と標準時間を受け渡すための型。項目名と標準時間の対応を管理する。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class DefaultMinutesDto {

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
    public DefaultMinutesDto() {

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
     * 標準時間のモデルを作る<br>
     *
     * @since 0.1.0
     *
     * @return 項目ごとの標準時間
     */
    public DefaultMinutesModel toDefaultMinutesModel() {

        final DefaultMinutesModel result = new DefaultMinutesModel();

        for (final Map.Entry<String, Double> entry : this.minutesByName.entrySet()) {

            result.add(entry.getKey(), entry.getValue());

        }

        return result;

    }

}
