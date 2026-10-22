package kmg.gr.gr002.dailytasks.carryover.application.model;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Issue 番号と持ち越しの解析結果の対応<br>
 * <p>
 * 番号で引き、番号順に辿る。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public class CarryoverIssuesModel {

    /**
     * Issue 番号と解析結果の対応
     * <p>
     * 番号で引き、番号順に辿るクラス内の索引。外には渡さない。
     * </p>
     *
     * @since 0.1.0
     */
    private final Map<Integer, CarryoverIssueModel> issues;

    /**
     * コンストラクタ<br>
     * <p>
     * 解析結果は空にする。
     * </p>
     *
     * @since 0.1.0
     */
    public CarryoverIssuesModel() {

        this.issues = new TreeMap<>();

    }

    /**
     * 解析結果を複製する<br>
     * <p>
     * 番号順は保つ。複製後の追加は、複製元に影響しない。
     * </p>
     *
     * @since 0.1.0
     *
     * @return 複製した解析結果
     */
    public CarryoverIssuesModel copy() {

        final CarryoverIssuesModel result = new CarryoverIssuesModel();
        result.issues.putAll(this.issues);
        return result;

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
    public CarryoverIssueModel find(final int number) {

        final CarryoverIssueModel result = this.issues.get(number);
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
    public void put(final int number, final CarryoverIssueModel issue) {

        this.issues.put(number, issue);

    }

    /**
     * 解析結果を番号順に返す<br>
     *
     * @since 0.1.0
     *
     * @return 解析結果（番号順）
     */
    public Collection<CarryoverIssueModel> values() {

        final Collection<CarryoverIssueModel> result = this.issues.values();
        return result;

    }

}
