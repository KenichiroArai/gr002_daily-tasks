package kmg.gr.gr002.dailytasks.carryover.infrastructure.context;

import org.springframework.stereotype.Component;

import kmg.fund.infrastructure.context.KmgMessageSource;

/**
 * 毎日タスクのメッセージリソース<br>
 * <p>
 * kmg-fund の {@link KmgMessageSource} を継承する。追加のロジックは持たない。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@Component
public class DailyTasksMessageSource extends KmgMessageSource {

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     */
    public DailyTasksMessageSource() {

        // 処理なし

    }

}
