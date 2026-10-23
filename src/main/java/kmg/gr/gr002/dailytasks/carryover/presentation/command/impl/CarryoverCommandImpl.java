package kmg.gr.gr002.dailytasks.carryover.presentation.command.impl;

import java.io.IOException;
import java.io.PrintStream;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import kmg.gr.gr002.dailytasks.carryover.infrastructure.logic.MessageLogic;
import kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg.Gr002GenMsgTypes;
import kmg.gr.gr002.dailytasks.carryover.presentation.command.CarryoverCommand;
import kmg.gr.gr002.dailytasks.carryover.presentation.model.CarryoverPropertiesModel;
import kmg.gr.gr002.dailytasks.carryover.presentation.service.CarryoverService;
import kmg.gr.gr002.dailytasks.carryover.presentation.types.CommandOptionTypes;

/**
 * 持ち越しの収集コマンドの実装<br>
 * <p>
 * 入力の窓口として、設定ファイル（application.properties）の値とコマンドライン引数を presentation 層のサービス（application 層が実装する）へ引き継ぐ。Spring Boot の起動後に
 * {@link CommandLineRunner} として実行される。収集のあと集計を呼ぶ。daily-tasks-git のルートをカレントディレクトリとして実行する。
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
    private final ObjectProvider<CarryoverService> carryoverService;

    /**
     * メッセージの取得
     *
     * @since 0.1.0
     */
    private final MessageLogic messageLogic;

    /**
     * 使い方の出力先
     *
     * @since 0.1.0
     */
    private final PrintStream out;

    /**
     * 全件モードか
     *
     * @since 0.1.0
     */
    private boolean full;

    /**
     * 使い方を表示するか
     *
     * @since 0.1.0
     */
    private boolean help;

    /**
     * 設定ファイルの値
     *
     * @since 0.1.0
     */
    private final CarryoverPropertiesModel properties;

    /**
     * コンストラクタ<br>
     * <p>
     * 使い方は標準出力に出す。全件モードと使い方の表示は false にする。
     * </p>
     *
     * @since 0.1.0
     *
     * @param carryoverService
     *                         持ち越しの収集・集計サービス
     * @param messageLogic
     *                         メッセージの取得
     * @param properties
     *                         設定ファイルの値
     */
    public CarryoverCommandImpl(final ObjectProvider<CarryoverService> carryoverService,
        final MessageLogic messageLogic, final CarryoverPropertiesModel properties) {

        this.carryoverService = carryoverService;
        this.messageLogic = messageLogic;
        this.properties = properties;
        this.out = System.out;
        this.full = false;
        this.help = false;

    }

    /**
     * コマンドライン引数を解析してオプションをフィールドに設定する<br>
     *
     * @since 0.1.0
     *
     * @param args
     *             コマンドライン引数
     *
     * @throws IllegalArgumentException
     *                                  不明な引数が指定された場合
     */
    private void parseArgs(final String[] args) {

        this.full = false;
        this.help = false;

        for (final String arg : args) {

            final CommandOptionTypes option = CommandOptionTypes.getEnum(arg);

            /* 全件モード以外では、使い方の表示だけを更新する */
            this.help = switch (option) {

                case FULL -> {

                    this.full = true;
                    yield this.help;

                }

                case HELP, HELP_SHORT -> {

                    yield true;

                }

                case NONE -> {

                    final String template = this.messageLogic.get(Gr002GenMsgTypes.GR002_GEN11101);
                    final String message  = String.format(template, arg);
                    throw new IllegalArgumentException(message);

                }

            };

        }

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
        this.parseArgs(args);

        /* 使い方の表示か */
        if (this.help) {

            // 使い方の表示の場合

            // 使い方を表示して終了する
            final String usage = this.messageLogic.get(Gr002GenMsgTypes.GR002_GEN11100);
            this.out.println(usage);
            return;

        }

        /* 収集と集計の実行 */
        final CarryoverService service = this.carryoverService.getObject();
        service.initialize(this.properties, this.full);
        service.collect();
        service.aggregate();

    }

}
