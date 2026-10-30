package kmg.gr.gr002.dailytasks.testutil;

import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;

/**
 * テスト用のログ取得<br>
 * <p>
 * 対象クラスのロガーに Logback の {@link ListAppender} を追加し、出力されたログメッセージを取得する。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public final class LogCapture implements AutoCloseable {

    /**
     * ログの取得先
     *
     * @since 0.1.0
     */
    private final ListAppender<ILoggingEvent> listAppender;

    /**
     * 対象のロガー
     *
     * @since 0.1.0
     */
    private final Logger logger;

    /**
     * 変更前のログレベル
     *
     * @since 0.1.0
     */
    private final Level previousLevel;

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     *
     * @param target
     *               ログを取得する対象クラス
     */
    public LogCapture(final Class<?> target) {

        this(target, null);

    }

    /**
     * コンストラクタ<br>
     * <p>
     * レベルを指定した場合は取得中だけそのレベルにし、終了時に元へ戻す。
     * </p>
     *
     * @since 0.1.0
     *
     * @param target
     *               ログを取得する対象クラス
     * @param level
     *               取得中のログレベル。変更しない場合は null
     */
    public LogCapture(final Class<?> target, final Level level) {

        this.logger = (Logger) LoggerFactory.getLogger(target);
        this.previousLevel = this.logger.getLevel();

        /* レベルを変更するか */
        if (level != null) {

            // レベルを変更する場合

            // 取得中だけ指定のレベルにする
            this.logger.setLevel(level);

        }

        this.listAppender = new ListAppender<>();
        this.listAppender.start();
        this.logger.addAppender(this.listAppender);

    }

    /**
     * ログの取得を終了する<br>
     *
     * @since 0.1.0
     */
    @Override
    public void close() {

        this.logger.detachAppender(this.listAppender);
        this.listAppender.stop();
        this.logger.setLevel(this.previousLevel);

    }

    /**
     * 取得したログメッセージを返す<br>
     *
     * @since 0.1.0
     *
     * @return ログメッセージ（出力順）
     */
    public String[] getMessages() {

        @SuppressWarnings("null")
        final String[] result
            = this.listAppender.list.stream().map(ILoggingEvent::getFormattedMessage).toArray(String[]::new);
        return result;

    }

}
