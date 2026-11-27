package kmg.gr.gr002.dailytasks.advance.domain.logic.impl;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import kmg.gr.gr002.dailytasks.advance.application.logic.AdvanceDataLogic;
import kmg.gr.gr002.dailytasks.advance.domain.repository.dao.AdvanceDataDao;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSummaryModel;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.CarryoverSummaryDto;

/**
 * 先行の画面用の集計を読み書きするロジックの実装<br>
 * <p>
 * application 層の読み書きロジックを実装する。application 層のモデルと domain 層の DTO は、変換先の型が変換元を受け取って作る。保存先は {@link #initialize(Path)}
 * で受け取り、DAO を初期化する。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.2.0
 *
 * @version 0.2.0
 */
@Component
@Scope("prototype")
public class AdvanceDataLogicImpl implements AdvanceDataLogic {

    /**
     * JSON ファイルの書き込み
     *
     * @since 0.2.0
     */
    private final AdvanceDataDao advanceDataDao;

    /**
     * コンストラクタ<br>
     *
     * @since 0.2.0
     *
     * @param advanceDataDao
     *                       JSON ファイルの書き込み
     */
    public AdvanceDataLogicImpl(final AdvanceDataDao advanceDataDao) {

        this.advanceDataDao = advanceDataDao;

    }

    /**
     * 集計の保存先を受け取り、読み書き先を初期化する<br>
     *
     * @since 0.2.0
     *
     * @param dataDir
     *                保存先のディレクトリ（例: docs/data）
     */
    @Override
    public void initialize(final Path dataDir) {

        this.advanceDataDao.initialize(dataDir);

    }

    /**
     * 画面用の集計を保存する<br>
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
    public void saveSummary(final CarryoverSummaryModel summary) throws IOException {

        this.advanceDataDao.saveSummary(new CarryoverSummaryDto(summary));

    }

}