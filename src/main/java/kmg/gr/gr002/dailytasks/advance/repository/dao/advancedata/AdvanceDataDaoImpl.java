package kmg.gr.gr002.dailytasks.advance.repository.dao.advancedata;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import kmg.core.infrastructure.type.KmgString;
import kmg.gr.gr002.dailytasks.advance.domain.repository.dao.AdvanceDataDao;
import kmg.gr.gr002.dailytasks.advance.repository.types.AdvanceDataFileTypes;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverSummaryDto;
import kmg.gr.gr002.dailytasks.carryover.repository.dto.json.CarryoverSummaryJsonDto;
import kmg.gr.gr002.dailytasks.carryover.repository.types.CarryoverDataFormatTypes;
import kmg.gr.gr002.dailytasks.infrastructure.context.DailyTasksMessageSource;
import kmg.gr.gr002.dailytasks.infrastructure.types.msg.Gr002LogMsgTypes;
import tools.jackson.core.JacksonException;
import tools.jackson.core.util.DefaultIndenter;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.core.util.Separators;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.json.JsonMapper;

/**
 * 先行の JSON ファイル（画面用の集計）を読み書きする DAO の実装<br>
 * <p>
 * domain 層の DAO を実装する。受け取った DTO は carryover と同じ JSON の形の型が変換して書き込む。出力は OS に関係なく改行を LF に統一し、差分が安定するようにする。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.2.0
 *
 * @version 0.2.0
 */
@Repository
@Scope("prototype")
public class AdvanceDataDaoImpl implements AdvanceDataDao {

    /**
     * ロガー
     *
     * @since 0.2.0
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(AdvanceDataDaoImpl.class);

    /**
     * JSON の変換
     *
     * @since 0.2.0
     */
    private final JsonMapper jsonMapper;

    /**
     * メッセージの取得
     *
     * @since 0.2.0
     */
    private final DailyTasksMessageSource messageSource;

    /**
     * 出力先のディレクトリ（例: docs/data）
     *
     * @since 0.2.0
     */
    private Path dataDir;

    /**
     * コンストラクタ<br>
     *
     * @since 0.2.0
     *
     * @param jsonMapper
     *                      JSON の変換
     * @param messageSource
     *                      メッセージの取得
     */
    public AdvanceDataDaoImpl(final JsonMapper jsonMapper, final DailyTasksMessageSource messageSource) {

        this.jsonMapper = jsonMapper;
        this.messageSource = messageSource;

    }

    /**
     * 出力先のディレクトリを受け取り、フィールドに保持する<br>
     *
     * @since 0.2.0
     *
     * @param dataDir
     *                出力先のディレクトリ（例: docs/data）
     */
    @SuppressWarnings("hiding")
    @Override
    public void initialize(final Path dataDir) {

        this.dataDir = dataDir;

    }

    /**
     * 画面用の集計を保存する<br>
     * <p>
     * 保存先は dataDir/advance/summary.json とする。
     * </p>
     *
     * @since 0.2.0
     *
     * @param summary
     *                画面用の集計
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    @Override
    public void saveSummary(final CarryoverSummaryDto summary) throws IOException {

        final Path                    path        = this.dataDir
            .resolve(AdvanceDataFileTypes.ADVANCE_DIR.getFileName())
            .resolve(AdvanceDataFileTypes.SUMMARY_FILE.getFileName());
        final CarryoverSummaryJsonDto summaryJson = new CarryoverSummaryJsonDto(summary);
        final String                  message     = this.messageSource.getLogMessage(Gr002LogMsgTypes.GR002_LOG52003);
        AdvanceDataDaoImpl.LOGGER.debug(message, path);
        this.write(path, summaryJson);

    }

    /**
     * 改行を LF に統一した整形出力を作る<br>
     *
     * @since 0.2.0
     *
     * @return JSON の出力
     */
    private ObjectWriter createObjectWriter() {

        /* 改行を LF に統一した整形出力 */
        final ObjectWriter         result;
        final DefaultIndenter      indenter = new DefaultIndenter(CarryoverDataFormatTypes.INDENT.getFormat(),
            CarryoverDataFormatTypes.LINE_FEED.getFormat());
        final DefaultPrettyPrinter printer  = new DefaultPrettyPrinter()
            .withSeparators(Separators.createDefaultInstance().withObjectNameValueSpacing(Separators.Spacing.AFTER))
            .withObjectIndenter(indenter).withArrayIndenter(indenter);
        final ObjectWriter         writer   = this.jsonMapper.writer();

        result = writer.with(printer);
        return result;

    }

    /**
     * オブジェクトを JSON ファイルに書き込む<br>
     *
     * @since 0.2.0
     *
     * @param path
     *              出力先
     * @param value
     *              出力するオブジェクト
     *
     * @throws IOException
     *                     書き込みまたは JSON の変換に失敗した場合
     */
    private void write(final Path path, final Object value) throws IOException {

        final String json;

        try {

            final ObjectWriter writer = this.createObjectWriter();
            json = KmgString.concat(writer.writeValueAsString(value), CarryoverDataFormatTypes.LINE_FEED.getFormat());

        } catch (final JacksonException e) {

            throw new IOException(e);

        }

        Files.createDirectories(path.getParent());
        Files.writeString(path, json, StandardCharsets.UTF_8);

    }

}