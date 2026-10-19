package kmg.gr.gr002.dailytasks.carryover.infrastructure.logic.impl;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import kmg.fund.infrastructure.cmn.msg.KmgFundCmnMsgTypes;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.logic.MessageLogic;

/**
 * Spring の {@link MessageSource} からメッセージを取得する実装<br>
 * <p>
 * 引数を渡さずに取得するため、メッセージ中の {}、%s、%d は書式として解釈されずにそのまま返る。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@Component
public class MessageLogicImpl implements MessageLogic {

    /**
     * メッセージの取得元
     *
     * @since 0.1.0
     */
    @Autowired
    private MessageSource messageSource;

    /**
     * メッセージを取得する<br>
     *
     * @since 0.1.0
     *
     * @param type
     *             メッセージの種類
     *
     * @return メッセージ
     *
     * @throws org.springframework.context.NoSuchMessageException
     *                                                            キーが見つからない場合
     */
    @Override
    public String get(final KmgFundCmnMsgTypes type) {

        final String result = this.messageSource.getMessage(type.get(), null, Locale.ROOT);
        return result;

    }

}
