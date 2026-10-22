package kmg.gr.gr002.dailytasks.carryover.domain.dto;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssuesModel;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.logic.MessageLogic;

/**
 * Issue 番号と持ち越しの解析結果の対応の DTO<br>
 * <p>
 * domain 層が repository 層と、保存済みの解析結果を受け渡すための型。番号で引き、番号順に辿る。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class CarryoverIssuesDto {

    /**
     * Issue 番号と解析結果の対応
     * <p>
     * 番号で引き、番号順に辿るクラス内の索引。外には渡さない。
     * </p>
     *
     * @since 0.1.0
     */
    private final Map<Integer, CarryoverIssueDto> issues;

    /**
     * コンストラクタ<br>
     * <p>
     * 解析結果は空にする。
     * </p>
     *
     * @since 0.1.0
     */
    public CarryoverIssuesDto() {

        this.issues = new TreeMap<>();

    }

    /**
     * Issue 番号の解析結果を返す<br>
     *
     * @since 0.1.0
     *
     * @param number
     *               Issue 番号
     *
     * @return 解析結果。未登録の場合は null
     */
    public CarryoverIssueDto find(final int number) {

        final CarryoverIssueDto result = this.issues.get(number);
        return result;

    }

    /**
     * Issue 番号を返す<br>
     *
     * @since 0.1.0
     *
     * @return Issue 番号（番号順）
     */
    public Set<Integer> numbers() {

        final Set<Integer> result = this.issues.keySet();
        return result;

    }

    /**
     * 解析結果を加える<br>
     *
     * @since 0.1.0
     *
     * @param number
     *               Issue 番号
     * @param issue
     *               解析結果
     */
    public void put(final int number, final CarryoverIssueDto issue) {

        this.issues.put(number, issue);

    }

    /**
     * 解析結果のモデルを作る<br>
     * <p>
     * 番号順は保つ。
     * </p>
     *
     * @since 0.1.0
     *
     * @param messageLogic
     *                     メッセージの取得
     *
     * @return Issue 番号と解析結果の対応
     */
    public CarryoverIssuesModel toCarryoverIssuesModel(final MessageLogic messageLogic) {

        final CarryoverIssuesModel result = new CarryoverIssuesModel();

        for (final Map.Entry<Integer, CarryoverIssueDto> entry : this.issues.entrySet()) {

            result.put(entry.getKey(), entry.getValue().toCarryoverIssueModel(messageLogic));

        }

        return result;

    }

}
