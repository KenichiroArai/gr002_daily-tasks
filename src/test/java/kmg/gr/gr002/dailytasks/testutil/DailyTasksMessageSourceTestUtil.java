package kmg.gr.gr002.dailytasks.testutil;

import java.nio.charset.StandardCharsets;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.util.ReflectionTestUtils;

import kmg.gr.gr002.dailytasks.carryover.infrastructure.context.DailyTasksMessageSource;

/**
 * テスト用のメッセージリソースの生成<br>
 * <p>
 * Spring Boot を起動せずに、本番と同じ messages.properties と messages-log.properties を読む {@link DailyTasksMessageSource} を作る。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings("nls")
public final class DailyTasksMessageSourceTestUtil {

    /**
     * messages.properties を読むメッセージリソースを作る<br>
     *
     * @since 0.1.0
     *
     * @return メッセージリソース
     */
    public static DailyTasksMessageSource create() {

        final ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasenames("messages", "messages-log");
        messageSource.setDefaultEncoding(StandardCharsets.UTF_8.name());
        messageSource.setFallbackToSystemLocale(false);

        final DailyTasksMessageSource result = new DailyTasksMessageSource();
        ReflectionTestUtils.setField(result, "messageSource", messageSource);
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
    private DailyTasksMessageSourceTestUtil() {

        // 処理なし

    }

}
