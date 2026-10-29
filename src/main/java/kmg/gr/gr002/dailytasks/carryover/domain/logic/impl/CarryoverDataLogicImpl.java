package kmg.gr.gr002.dailytasks.carryover.domain.logic.impl;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverDataLogic;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssuesModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverIssueDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverIssuesDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverSummaryDto;
import kmg.gr.gr002.dailytasks.carryover.domain.repository.dao.CarryoverDataDao;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.logic.MessageLogic;

/**
 * 持ち越しの解析結果と画面用の集計を読み書きするロジックの実装<br>
 * <p>
 * application 層の読み書きロジックを実装する。application 層のモデルと domain 層の DTO は、変換先の型が変換元を受け取って作る。保存先は {@link #initialize(Path)}
 * で受け取り、DAO を初期化する。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@Component
@Scope("prototype")
public class CarryoverDataLogicImpl implements CarryoverDataLogic {

    /**
     * JSON ファイルの読み書き
     *
     * @since 0.1.0
     */
    private final CarryoverDataDao carryoverDataDao;

    /**
     * メッセージの取得
     *
     * @since 0.1.0
     */
    private final MessageLogic messageLogic;

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     *
     * @param carryoverDataDao
     *                         JSON ファイルの読み書き
     * @param messageLogic
     *                         メッセージの取得
     */
    public CarryoverDataLogicImpl(final CarryoverDataDao carryoverDataDao, final MessageLogic messageLogic) {

        this.carryoverDataDao = carryoverDataDao;
        this.messageLogic = messageLogic;

    }

    /**
     * 解析結果と集計の保存先を受け取り、読み書き先を初期化する<br>
     *
     * @since 0.1.0
     *
     * @param dataDir
     *                保存先のディレクトリ（例: docs/data）
     */
    @Override
    public void initialize(final Path dataDir) {

        this.carryoverDataDao.initialize(dataDir);

    }

    /**
     * Issue 番号の解析結果を読み込む<br>
     *
     * @since 0.1.0
     *
     * @param number
     *               Issue 番号
     *
     * @return 解析結果。保存されていない場合は null
     *
     * @throws IOException
     *                     読み込みに失敗した場合
     */
    @Override
    public CarryoverIssueModel loadIssue(final int number) throws IOException {

        CarryoverIssueModel     result = null;
        final CarryoverIssueDto issue  = this.carryoverDataDao.loadIssue(number);

        /* 保存済みの解析結果がないか */
        if (issue == null) {

            // 保存済みの解析結果がない場合

            // 未保存として null を返す
            return result;

        }

        /* モデルへの変換 */
        result = issue.toCarryoverIssueModel(this.messageLogic);
        return result;

    }

    /**
     * 保存済みの持ち越しの解析結果を読み込む<br>
     *
     * @since 0.1.0
     *
     * @return Issue 番号と解析結果の対応。保存されていない場合は空
     *
     * @throws IOException
     *                     読み込みに失敗した場合
     */
    @Override
    public CarryoverIssuesModel loadIssues() throws IOException {

        final CarryoverIssuesModel result;
        final CarryoverIssuesDto   issues = this.carryoverDataDao.loadIssues();

        result = issues.toCarryoverIssuesModel(this.messageLogic);
        return result;

    }

    /**
     * 持ち越しの解析結果を保存する<br>
     *
     * @since 0.1.0
     *
     * @param issue
     *              持ち越しの解析結果
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    @Override
    public void saveIssue(final CarryoverIssueModel issue) throws IOException {

        this.carryoverDataDao.saveIssue(new CarryoverIssueDto(issue));

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
    public void saveSummary(final CarryoverSummaryModel summary) throws IOException {

        this.carryoverDataDao.saveSummary(new CarryoverSummaryDto(summary));

    }

}
