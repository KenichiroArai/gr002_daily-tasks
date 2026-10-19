package kmg.gr.gr002.dailytasks.carryover.presentation.command.impl;

import java.io.IOException;
import java.io.PrintStream;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import kmg.gr.gr002.dailytasks.carryover.infrastructure.logic.MessageLogic;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg.Gr002GenMsgTypes;
import kmg.gr.gr002.dailytasks.carryover.presentation.command.CarryoverCommand;
import kmg.gr.gr002.dailytasks.carryover.presentation.model.CarryoverOptionsModel;
import kmg.gr.gr002.dailytasks.carryover.presentation.model.CarryoverPropertiesModel;
import kmg.gr.gr002.dailytasks.carryover.presentation.service.CarryoverService;
import kmg.gr.gr002.dailytasks.carryover.presentation.types.CommandOptionTypes;

/**
 * 持ち越しの収集コマンドの実装<br>
 * <p>
 * 入力の窓口として、設定ファイル（application.properties）の値とコマンドライン引数を presentation 層のサービス（application 層が実装する）へ引き継ぐ。Spring Boot の起動後に
 * {@link CommandLineRunner} として実行される。daily-tasks-git のルートをカレントディレクトリとして実行する。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@Component
public class CarryoverCommandImpl implements CarryoverCommand {

    /**
     * 持ち越しの収集・集計サービス
     * <p>
     * 実行のたびに新しいインスタンスを取る。サービスは収集の設定をフィールドに保持するためである。
     * </p>
     *
     * @since 0.1.0
     */
    @Autowired
    private ObjectProvider<CarryoverService> carryoverService;

    /**
     * メッセージの取得
     *
     * @since 0.1.0
     */
    @Autowired
    private MessageLogic messageLogic;

    /**
     * 使い方の出力先
     *
     * @since 0.1.0
     */
    private final PrintStream out;

    /**
     * 設定ファイルの値
     *
     * @since 0.1.0
     */
    @Autowired
    private CarryoverPropertiesModel properties;

    /**
     * コンストラクタ<br>
     * <p>
     * 使い方は標準出力に出す。
     * </p>
     *
     * @since 0.1.0
     */
    public CarryoverCommandImpl() {

        this.out = System.out;

    }

    /**
     * コマンドライン引数を解析してオプションを返す<br>
     *
     * @since 0.1.0
     *
     * @param args
     *             コマンドライン引数
     *
     * @return 持ち越しの収集コマンドのオプション
     *
     * @throws IllegalArgumentException
     *                                  不明な引数が指定された場合
     */
    private CarryoverOptionsModel parseArgs(final String[] args) {

        final CarryoverOptionsModel result = new CarryoverOptionsModel();

        for (final String arg : args) {

            final CommandOptionTypes option = CommandOptionTypes.getEnum(arg);

            if (option == CommandOptionTypes.FULL) {

                result.setFull(true);
                continue;

            }

            if ((option == CommandOptionTypes.HELP) || (option == CommandOptionTypes.HELP_SHORT)) {

                result.setHelp(true);
                continue;

            }

            final String template = this.messageLogic.get(Gr002GenMsgTypes.GR002_GEN11101);
            final String message  = String.format(template, arg);
            throw new IllegalArgumentException(message);

        }

        return result;

    }

    /**
     * Spring Boot の起動後にコマンドを実行する<br>
     *
     * @since 0.1.0
     *
     * @param args
     *             コマンドライン引数
     *
     * @throws IOException
     *                                  取得、読み込みまたは書き込みに失敗した場合
     * @throws IllegalArgumentException
     *                                  不明な引数が指定された場合
     */
    @Override
    public void run(final String... args) throws IOException {

        /* 引数の解析 */
        final CarryoverOptionsModel options = this.parseArgs(args);

        /* 使い方の表示 */
        if (options.isHelp()) {

            final String usage = this.messageLogic.get(Gr002GenMsgTypes.GR002_GEN11100);
            this.out.println(usage);
            return;

        }

        /* 収集の実行 */
        final CarryoverService service = this.carryoverService.getObject();
        service.initialize(this.properties, options.isFull());
        service.collect();

    }

}
