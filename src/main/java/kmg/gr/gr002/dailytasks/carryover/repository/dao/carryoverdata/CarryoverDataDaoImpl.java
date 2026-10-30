package kmg.gr.gr002.dailytasks.carryover.repository.dao.carryoverdata;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import kmg.core.infrastructure.type.KmgString;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverIssueDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverIssuesDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverSummaryDto;
import kmg.gr.gr002.dailytasks.carryover.domain.repository.dao.CarryoverDataDao;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.logic.MessageLogic;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg.Gr002LogMsgTypes;
import kmg.gr.gr002.dailytasks.carryover.repository.dto.json.CarryoverIssueJsonDto;
import kmg.gr.gr002.dailytasks.carryover.repository.dto.json.CarryoverSummaryJsonDto;
import kmg.gr.gr002.dailytasks.carryover.repository.types.CarryoverDataFileTypes;
import kmg.gr.gr002.dailytasks.carryover.repository.types.CarryoverDataFormatTypes;
import tools.jackson.core.JacksonException;
import tools.jackson.core.util.DefaultIndenter;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.core.util.Separators;
import tools.jackson.databind.ObjectWriter;
import tools.jackson.databind.json.JsonMapper;

/**
 * 持ち越しの JSON ファイル（Issue ごとの解析結果と画面用の集計）を読み書きする DAO の実装<br>
 * <p>
 * domain 層の DAO を実装する。受け取った DTO は JSON の形の型が変換して読み書きする。出力は OS に関係なく改行を LF に統一し、差分が安定するようにする。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@Repository
@Scope("prototype")
public class CarryoverDataDaoImpl implements CarryoverDataDao {

    /**
     * ロガー
     *
     * @since 0.1.0
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CarryoverDataDaoImpl.class);

    /**
     * JSON ファイルか<br>
     *
     * @since 0.1.0
     *
     * @param path
     *             調べるパス
     *
     * @return true：拡張子が JSON、false：それ以外
     */
    private static boolean jsonFile(final Path path) {

        final boolean result;
        final Path    fileName = path.getFileName();
        final String  name     = fileName.toString();

        result = name.endsWith(CarryoverDataFileTypes.JSON_EXTENSION.getFileName());
        return result;

    }

    /**
     * JSON の変換
     *
     * @since 0.1.0
     */
    private final JsonMapper jsonMapper;

    /**
     * メッセージの取得
     *
     * @since 0.1.0
     */
    private final MessageLogic messageLogic;

    /**
     * 出力先のディレクトリ（例: docs/data）
     *
     * @since 0.1.0
     */
    private Path dataDir;

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     *
     * @param jsonMapper
     *                     JSON の変換
     * @param messageLogic
     *                     メッセージの取得
     */
    public CarryoverDataDaoImpl(final JsonMapper jsonMapper, final MessageLogic messageLogic) {

        this.jsonMapper = jsonMapper;
        this.messageLogic = messageLogic;

    }

    /**
     * 改行を LF に統一した整形出力を作る<br>
     *
     * @since 0.1.0
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
     * 出力先のディレクトリを受け取り、フィールドに保持する<br>
     *
     * @since 0.1.0
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
     * Issue ごとの解析結果のパスを返す<br>
     *
     * @since 0.1.0
     *
     * @param number
     *               Issue 番号
     *
     * @return JSON ファイルのパス
     */
    private Path issuePath(final int number) {

        final Path result = this.dataDir.resolve(CarryoverDataFileTypes.ISSUES_DIR.getFileName())
            .resolve(String.format(CarryoverDataFormatTypes.ISSUE_FILE_NAME.getFormat(), number,
                CarryoverDataFileTypes.JSON_EXTENSION.getFileName()));
        return result;

    }

    /**
     * Issue 番号の解析結果を読み込む<br>
     *
     * @since 0.1.0
     *
     * @param number
     *               Issue 番号
     *
     * @return 解析結果。ファイルがない場合は null
     *
     * @throws IOException
     *                     読み込みに失敗した場合
     */
    @Override
    public CarryoverIssueDto loadIssue(final int number) throws IOException {

        CarryoverIssueDto result = null;
        final Path        path   = this.issuePath(number);

        /* 解析結果のファイルがあるか */
        final boolean stored = Files.isRegularFile(path);

        if (!stored) {

            // 解析結果のファイルがない場合

            // 未保存として null を返す
            final String message = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12032);
            CarryoverDataDaoImpl.LOGGER.debug(message, Integer.valueOf(number));
            return result;

        }

        /* JSON の読み込み */
        final String loadMessage = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12031);
        CarryoverDataDaoImpl.LOGGER.debug(loadMessage, path);
        final CarryoverIssueJsonDto issueJson = this.readIssue(path);

        /* DTO への変換 */
        result = issueJson.toCarryoverIssueDto();
        return result;

    }

    /**
     * 保存済みの Issue ごとの解析結果を読み込む<br>
     *
     * @since 0.1.0
     *
     * @return Issue 番号と解析結果の対応。ディレクトリがない場合は空
     *
     * @throws IOException
     *                     読み込みに失敗した場合
     */
    @Override
    public CarryoverIssuesDto loadIssues() throws IOException {

        final CarryoverIssuesDto result    = new CarryoverIssuesDto();
        final Path               issuesDir = this.dataDir.resolve(CarryoverDataFileTypes.ISSUES_DIR.getFileName());

        /* Issue のディレクトリがあるか */
        final boolean issuesDirectory = Files.isDirectory(issuesDir);

        if (!issuesDirectory) {

            // Issue のディレクトリがない場合

            // 空の解析結果を返す
            return result;

        }

        /* Issue JSON の読み込み */
        try (Stream<Path> paths = Files.list(issuesDir)) {

            for (final Path path : paths.filter(CarryoverDataDaoImpl::jsonFile).toList()) {

                final String loadMessage = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12031);
                CarryoverDataDaoImpl.LOGGER.debug(loadMessage, path);
                final CarryoverIssueJsonDto issueJson = this.readIssue(path);
                final CarryoverIssueDto     issue     = issueJson.toCarryoverIssueDto();
                result.put(issue.getNumber(), issue);

            }

        }

        return result;

    }

    /**
     * Issue ごとの解析結果の JSON ファイルを読み込む<br>
     *
     * @since 0.1.0
     *
     * @param path
     *             JSON ファイル
     *
     * @return 解析結果
     *
     * @throws IOException
     *                     読み込みまたは JSON の変換に失敗した場合
     */
    private CarryoverIssueJsonDto readIssue(final Path path) throws IOException {

        final CarryoverIssueJsonDto result;

        try {

            result = this.jsonMapper.readValue(path.toFile(), CarryoverIssueJsonDto.class);

        } catch (final JacksonException e) {

            throw new IOException(e);

        }

        return result;

    }

    /**
     * Issue ごとの解析結果を保存する<br>
     * <p>
     * ファイル名は Issue 番号を 4 桁でゼロ埋めした名前（例: 0001.json）とする。
     * </p>
     *
     * @since 0.1.0
     *
     * @param issue
     *              解析結果
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    @Override
    public void saveIssue(final CarryoverIssueDto issue) throws IOException {

        final Path                  path      = this.issuePath(issue.getNumber());
        final CarryoverIssueJsonDto issueJson = new CarryoverIssueJsonDto(issue);
        final String                message   = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12033);
        CarryoverDataDaoImpl.LOGGER.debug(message, path);
        this.write(path, issueJson);

    }

    /**
     * 画面用の集計を保存する<br>
     *
     * @since 0.1.0
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
            .resolve(CarryoverDataFileTypes.SUMMARY_FILE.getFileName());
        final CarryoverSummaryJsonDto summaryJson = new CarryoverSummaryJsonDto(summary);
        final String                  message     = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12034);
        CarryoverDataDaoImpl.LOGGER.debug(message, path);
        this.write(path, summaryJson);

    }

    /**
     * オブジェクトを JSON ファイルに書き込む<br>
     *
     * @since 0.1.0
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

        String json = null;

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
