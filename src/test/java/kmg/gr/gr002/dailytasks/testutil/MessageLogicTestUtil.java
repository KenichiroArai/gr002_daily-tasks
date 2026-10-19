package kmg.gr.gr002.dailytasks.testutil;

import java.nio.charset.StandardCharsets;

import org.springframework.context.support.ResourceBundleMessageSource;

import kmg.gr.gr002.dailytasks.carryover.infrastructure.logic.MessageLogic;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.logic.impl.MessageLogicImpl;

/**
 * テスト用のメッセージの取得の生成<br>
 * <p>
 * Spring Boot を起動せずに、本番と同じ messages.properties と messages-log.properties を読む {@link MessageLogic} を作る。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings("nls")
public final class MessageLogicTestUtil {

    /**
     * messages.properties を読むメッセージの取得を作る<br>
     *
     * @since 0.1.0
     *
     * @return メッセージの取得
     */
    public static MessageLogic create() {

        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages", "messages-log");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setFallbackToSystemLocale(false);

        final MessageLogicImpl impl = new MessageLogicImpl();
        ReflectionTestUtil.setField(impl, "messageSource", messageSource);
        final MessageLogic result = impl;
        return result;

    }

    /**
     * コンストラクタ<br>
     * <p>
     * インスタンス化しない。
     * </p>
     *
     * @since 0.1.0
     */
    private MessageLogicTestUtil() {

        // 処理なし

    }

}
