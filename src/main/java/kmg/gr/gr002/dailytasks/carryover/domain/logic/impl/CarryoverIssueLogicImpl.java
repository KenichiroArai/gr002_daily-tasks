package kmg.gr.gr002.dailytasks.carryover.domain.logic.impl;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import kmg.gr.gr002.dailytasks.carryover.application.logic.CarryoverIssueLogic;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSourceModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DailyTaskIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.DefaultMinutesModel;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverIssueDto;
import kmg.gr.gr002.dailytasks.carryover.domain.logic.CarryoverDtoConvertLogic;
import kmg.gr.gr002.dailytasks.carryover.domain.repository.api.GitHubIssueApi;
import kmg.gr.gr002.dailytasks.carryover.domain.repository.dao.CarryoverDataDao;
import kmg.gr.gr002.dailytasks.carryover.domain.repository.dao.DefaultMinutesDao;

/**
 * 持ち越しのデータの取得・保存ロジックの実装<br>
 * <p>
 * application 層のデータの取得・保存ロジックを実装する。application 層のモデルと domain 層の DTO を {@link CarryoverDtoConvertLogic} で変換し、domain 層の外部
 * API・DAO（実装は repository 層）に渡す。データの取得元と保存先は {@link #initialize(CarryoverSourceModel)} で受け取り、domain 層の型（接続設定の
 * DTO・パス）に変換して外部 API と DAO を初期化する。
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
public class CarryoverIssueLogicImpl implements CarryoverIssueLogic {

    /**
     * JSON ファイルの読み書き
     *
     * @since 0.1.0
     */
    @Autowired
    private CarryoverDataDao carryoverDataDao;

    /**
     * 標準時間の設定の読み込み
     *
     * @since 0.1.0
     */
    @Autowired
    private DefaultMinutesDao defaultMinutesDao;

    /**
     * DTO とモデルの変換
     *
     * @since 0.1.0
     */
    @Autowired
    private CarryoverDtoConvertLogic dtoConvertLogic;

    /**
     * GitHub の Issue の取得
     *
     * @since 0.1.0
     */
    @Autowired
    private GitHubIssueApi gitHubIssueApi;

    /**
     * データの取得元と保存先
     *
     * @since 0.1.0
     */
    private CarryoverSourceModel source;

    /**
     * すべての日々のタスク Issue を取得する<br>
     *
     * @since 0.1.0
     *
     * @return 日々のタスク Issue（作成順）
     *
     * @throws IOException
     *                     取得に失敗した場合
     */
    @Override
    public List<DailyTaskIssueModel> fetchAllIssues() throws IOException {

        final List<DailyTaskIssueModel> result
            = this.gitHubIssueApi.fetchAllIssues().stream().map(this.dtoConvertLogic::toDailyTaskIssue).toList();
        return result;

    }

    /**
     * データの取得元と保存先を受け取り、フィールドに保持する<br>
     * <p>
     * 外部 API と DAO も同じ取得元と保存先で初期化する。
     * </p>
     *
     * @since 0.1.0
     *
     * @param source
     *               データの取得元と保存先
     */
    @SuppressWarnings("hiding")
    @Override
    public void initialize(final CarryoverSourceModel source) {

        this.source = source;
        this.gitHubIssueApi.initialize(this.dtoConvertLogic.toGitHubSettingsDto(this.source));
        this.defaultMinutesDao.initialize(this.source.getDefaultMinutesFile());
        this.carryoverDataDao.initialize(this.source.getDataDir());

    }

    /**
     * 項目ごとの標準時間を読み込む<br>
     *
     * @since 0.1.0
     *
     * @return 項目ごとの標準時間。設定がない場合は空
     *
     * @throws IOException
     *                     読み込みに失敗した場合
     */
    @Override
    public DefaultMinutesModel loadDefaultMinutes() throws IOException {

        final Map<String, Double> minutesByName = this.defaultMinutesDao.load();
        final DefaultMinutesModel result        = this.dtoConvertLogic.toDefaultMinutes(minutesByName);
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
    public Map<Integer, CarryoverIssueModel> loadIssues() throws IOException {

        final Map<Integer, CarryoverIssueModel> result = new TreeMap<>();

        for (final Map.Entry<Integer, CarryoverIssueDto> entry : this.carryoverDataDao.loadIssues().entrySet()) {

            result.put(entry.getKey(), this.dtoConvertLogic.toCarryoverIssue(entry.getValue()));

        }

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

        this.carryoverDataDao.saveIssue(this.dtoConvertLogic.toCarryoverIssueDto(issue));

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

        this.carryoverDataDao.saveSummary(this.dtoConvertLogic.toCarryoverSummaryDto(summary));

    }

}
