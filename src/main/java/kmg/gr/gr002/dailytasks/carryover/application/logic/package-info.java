/**
 * 持ち越し機能の application 層のロジック<br>
 * <p>
 * application 層が domain 層に求めるロジック（Issue の取得、標準時間の読み込み、解析結果と集計の読み書き、Issue 本文の解析、日別の集計）のインタフェースを置く。実装は domain 層に置く。
 * ロジックはサービスを呼ばず、個々のメソッドがサービスなどから何度も呼ばれる。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
package kmg.gr.gr002.dailytasks.carryover.application.logic;
