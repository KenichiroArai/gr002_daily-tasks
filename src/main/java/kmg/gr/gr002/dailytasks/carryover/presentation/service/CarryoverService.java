package kmg.gr.gr002.dailytasks.carryover.presentation.service;

import java.io.IOException;

import kmg.gr.gr002.dailytasks.carryover.presentation.model.CarryoverPropertiesModel;

/**
 * 持ち越しの収集・集計サービス<br>
 * <p>
 * presentation 層が application 層に求めるインタフェース。実装は application 層に置く。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
public interface CarryoverService {

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
    void aggregate() throws IOException;

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
    int collect() throws IOException;

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
    void initialize(CarryoverPropertiesModel properties, boolean full);

}
