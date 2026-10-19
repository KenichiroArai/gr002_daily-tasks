package kmg.gr.gr002.dailytasks.carryover.infrastructure.logic;

import kmg.fund.infrastructure.cmn.msg.KmgFundCmnMsgTypes;

/**
 * メッセージ（messages.properties、messages-log.properties）の取得<br>
 * <p>
 * 業務を知らない汎用のユーティリティ。キーはメッセージの種類が持つ。埋め込み位置（SLF4J の {}、String.format の %s / %d）はそのまま返す。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public interface MessageLogic {

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
    String get(KmgFundCmnMsgTypes type);

}
