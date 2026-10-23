package kmg.gr.gr002.dailytasks.carryover.domain.logic.impl;

import java.io.IOException;
import java.nio.file.Path;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import kmg.gr.gr002.dailytasks.carryover.application.logic.DefaultMinutesLogic;
import kmg.gr.gr002.dailytasks.carryover.application.model.DefaultMinutesModel;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.DefaultMinutesDto;
import kmg.gr.gr002.dailytasks.carryover.domain.repository.dao.DefaultMinutesDao;

/**
 * 項目ごとの標準時間を読み込むロジックの実装<br>
 * <p>
 * application 層の読み込みロジックを実装する。読み込んだ対応はモデルが受け取る。設定ファイルは {@link #initialize(Path)} で受け取り、DAO を初期化する。
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
public class DefaultMinutesLogicImpl implements DefaultMinutesLogic {

    /**
     * 標準時間の設定の読み込み
     *
     * @since 0.1.0
     */
    private final DefaultMinutesDao defaultMinutesDao;

    /**
     * コンストラクタ<br>
     *
     * @since 0.1.0
     *
     * @param defaultMinutesDao
     *                          標準時間の設定の読み込み
     */
    public DefaultMinutesLogicImpl(final DefaultMinutesDao defaultMinutesDao) {

        this.defaultMinutesDao = defaultMinutesDao;

    }

    /**
     * 標準時間の設定ファイルを受け取り、読み込み先を初期化する<br>
     *
     * @since 0.1.0
     *
     * @param defaultMinutesFile
     *                           標準時間の設定ファイル（例: config/default-minutes.json）
     */
    @Override
    public void initialize(final Path defaultMinutesFile) {

        this.defaultMinutesDao.initialize(defaultMinutesFile);

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

        final DefaultMinutesModel result;
        final DefaultMinutesDto   minutes = this.defaultMinutesDao.load();

        result = minutes.toDefaultMinutesModel();
        return result;

    }

}
