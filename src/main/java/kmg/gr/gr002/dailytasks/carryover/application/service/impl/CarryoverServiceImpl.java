package kmg.gr.gr002.dailytasks.carryover.application.service.impl;

import java.io.IOException;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssuesModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSourceModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.application.service.CarryoverAggregateService;
import kmg.gr.gr002.dailytasks.carryover.application.service.CarryoverCollectService;
import kmg.gr.gr002.dailytasks.carryover.application.types.MinutesSourceTypes;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.logic.MessageLogic;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg.Gr002LogMsgTypes;
import kmg.gr.gr002.dailytasks.carryover.presentation.model.CarryoverPropertiesModel;
import kmg.gr.gr002.dailytasks.carryover.presentation.service.CarryoverService;

/**
 * 持ち越しの収集・集計を統合するサービスの実装<br>
 * <p>
 * presentation 層のサービスを実装する。収集と集計は役割ごとのサービスへ分け、{@link #collect()} と {@link #aggregate()} から決まった順で呼ぶ。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@Service
@Scope("prototype")
public class CarryoverServiceImpl implements CarryoverService {

    /**
     * ロガー
     *
     * @since 0.1.0
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CarryoverServiceImpl.class);

    /**
     * 残り時間の取得元ごとの行数を返す<br>
     *
     * @since 0.1.0
     *
     * @param issues
     *               解析結果
     * @param source
     *               残り時間の取得元
     *
     * @return 行数
     */
    private static long countBySource(final Collection<CarryoverIssueModel> issues, final MinutesSourceTypes source) {

        final long result = issues.stream().flatMap(issue -> issue.getItems().stream())
            .filter(item -> item.getMinutesSource() == source).count();
        return result;

    }

    /**
     * 持ち越しの集計
     *
     * @since 0.1.0
     */
    private final CarryoverAggregateService carryoverAggregateService;

    /**
     * 持ち越しの収集
     *
     * @since 0.1.0
     */
    private final CarryoverCollectService carryoverCollectService;

    /**
     * メッセージの取得
     *
     * @since 0.1.0
     */
    private final MessageLogic messageLogic;

    /**
     * 全件モードか
     *
     * @since 0.1.0
     */
    private boolean full;

    /**
     * 保存済みと今回解析した Issue
     *
     * @since 0.1.0
     */
    private CarryoverIssuesModel issues;

    /**
     * 取得したリモートの Issue の件数
     *
     * @since 0.1.0
     */
    private int remoteCount;

    /**
     * 解析して保存した Issue の件数
     *
     * @since 0.1.0
     */
    private int savedCount;

    /**
     * コンストラクタ<br>
     * <p>
     * 解析結果は空にする。
     * </p>
     *
     * @since 0.1.0
     *
     * @param carryoverAggregateService
     *                                  持ち越しの集計
     * @param carryoverCollectService
     *                                  持ち越しの収集
     * @param messageLogic
     *                                  メッセージの取得
     */
    public CarryoverServiceImpl(final CarryoverAggregateService carryoverAggregateService,
        final CarryoverCollectService carryoverCollectService, final MessageLogic messageLogic) {

        this.carryoverAggregateService = carryoverAggregateService;
        this.carryoverCollectService = carryoverCollectService;
        this.messageLogic = messageLogic;
        this.issues = new CarryoverIssuesModel();

    }

    /**
     * 保存済みと今回解析した Issue から日別の集計を作り、保存する<br>
     * <p>
     * {@link #collect()} のあとで呼ぶ。結果のログも出す。
     * </p>
     *
     * @since 0.1.0
     *
     * @throws IOException
     *                     書き込みに失敗した場合
     */
    @Override
    public void aggregate() throws IOException {

        final CarryoverSummaryModel summary = this.carryoverAggregateService.aggregate(this.issues.values());
        this.carryoverAggregateService.saveSummary(summary);
        this.logResult(summary);

    }

    /**
     * Issue を取得して解析し、Issue ごとの JSON を出力する<br>
     * <p>
     * {@link #initialize(CarryoverPropertiesModel, boolean)} のあとで呼ぶ。差分モードでは、Issue 番号が大きい順の最新の指定件数（設定の
     * recentCount）を除き、保存済みの JSON と更新日時が同じ Issue は解析しない。集計は {@link #aggregate()} で行う。
     * </p>
     *
     * @since 0.1.0
     *
     * @return 解析して保存した Issue の件数
     *
     * @throws IOException
     *                     取得、読み込みまたは書き込みに失敗した場合
     */
    @Override
    public int collect() throws IOException {

        this.carryoverCollectService.fetch();
        final int result = this.carryoverCollectService.analyze();
        this.carryoverCollectService.saveIssues();
        this.issues = this.carryoverCollectService.getIssues().copy();
        this.savedCount = result;
        this.remoteCount = this.carryoverCollectService.getRemoteCount();
        return result;

    }

    /**
     * 収集の設定とモードを受け取り、フィールドに保持する<br>
     * <p>
     * 設定はデータの取得元と保存先に変換し、収集と集計のサービスへ渡す。
     * </p>
     *
     * @since 0.1.0
     *
     * @param properties
     *                   持ち越しの収集の設定
     * @param full
     *                   true：全件モード（すべての Issue を解析し直す）、false：差分モード
     */
    @SuppressWarnings("hiding")
    @Override
    public void initialize(final CarryoverPropertiesModel properties, final boolean full) {

        final CarryoverSourceModel source = new CarryoverSourceModel(properties);
        this.full = full;
        this.carryoverCollectService.initialize(source, properties.getRecentCount(), full);
        this.carryoverAggregateService.initialize(source.getDataDir());

    }

    /**
     * 収集と集計の結果をログに出す<br>
     *
     * @since 0.1.0
     *
     * @param summary
     *                画面用の集計
     */
    private void logResult(final CarryoverSummaryModel summary) {

        final String collectMessage = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12000);
        final String modeName       = this.modeName();
        CarryoverServiceImpl.LOGGER.info(collectMessage, modeName, this.remoteCount, this.savedCount,
            summary.getDays().size());

        final String minutesSourceMessage = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12001);
        final long   defaultCount         = CarryoverServiceImpl.countBySource(this.issues.values(),
            MinutesSourceTypes.DEFAULT);
        final long   unknownCount         = CarryoverServiceImpl.countBySource(this.issues.values(),
            MinutesSourceTypes.UNKNOWN);
        CarryoverServiceImpl.LOGGER.info(minutesSourceMessage, defaultCount, unknownCount);

    }

    /**
     * ログに出すモードの名前を返す<br>
     *
     * @since 0.1.0
     *
     * @return モードの名前（全件 / 差分）
     */
    private String modeName() {

        String result = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12012);

        /* 全件モードか */
        if (!this.full) {

            // 全件モードではない場合

            // 差分の名前を返す
            return result;

        }

        result = this.messageLogic.get(Gr002LogMsgTypes.GR002_LOG12011);
        return result;

    }

}
