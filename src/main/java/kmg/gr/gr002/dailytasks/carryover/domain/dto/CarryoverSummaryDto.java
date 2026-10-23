package kmg.gr.gr002.dailytasks.carryover.domain.dto;

import java.util.List;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailySummaryModel;

/**
 * 画面用の持ち越しの集計の DTO<br>
 * <p>
 * domain 層が repository 層に画面用の集計を渡すための型（domain 層の受け渡しの型）。保存の形式は repository 層に任せる。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class CarryoverSummaryDto {

    /**
     * 日別の集計（日付順）
     *
     * @since 0.1.0
     */
    private final List<DailySummaryDto> days;

    /**
     * 項目名（延べ件数の多い順）
     *
     * @since 0.1.0
     */
    private final List<String> items;

    /**
     * 集計に含めた最新の Issue 番号。Issue がない場合は 0
     *
     * @since 0.1.0
     */
    private final int latestIssue;

    /**
     * 日別の集計を保存用の DTO にする<br>
     *
     * @since 0.1.0
     *
     * @param summary
     *                画面用の集計
     *
     * @return 保存用の日別の集計
     */
    private static List<DailySummaryDto> toDayDtos(final CarryoverSummaryModel summary) {

        final List<DailySummaryDto>   result;
        final List<DailySummaryModel> days = summary.getDays();

        result = days.stream().map(DailySummaryDto::new).toList();
        return result;

    }

    /**
     * 画面用の集計から保存用の DTO を作る<br>
     *
     * @since 0.1.0
     *
     * @param summary
     *                画面用の集計
     */
    public CarryoverSummaryDto(final CarryoverSummaryModel summary) {

        this(summary.getLatestIssue(), summary.getItems(), CarryoverSummaryDto.toDayDtos(summary));

    }

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     *
     * @param latestIssue
     *                    集計に含めた最新の Issue 番号。Issue がない場合は 0
     * @param items
     *                    項目名（延べ件数の多い順）
     * @param days
     *                    日別の集計（日付順）
     */
    public CarryoverSummaryDto(final int latestIssue, final List<String> items, final List<DailySummaryDto> days) {

        this.latestIssue = latestIssue;
        this.items = List.copyOf(items);
        this.days = List.copyOf(days);

    }

    /**
     * 日別の集計を返す<br>
     *
     * @since 0.1.0
     *
     * @return 日別の集計（日付順）
     */
    public List<DailySummaryDto> getDays() {

        final List<DailySummaryDto> result = this.days;
        return result;

    }

    /**
     * 項目名を返す<br>
     *
     * @since 0.1.0
     *
     * @return 項目名（延べ件数の多い順）
     */
    public List<String> getItems() {

        final List<String> result = this.items;
        return result;

    }

    /**
     * 集計に含めた最新の Issue 番号を返す<br>
     *
     * @since 0.1.0
     *
     * @return 最新の Issue 番号。Issue がない場合は 0
     */
    public int getLatestIssue() {

        final int result = this.latestIssue;
        return result;

    }

}
