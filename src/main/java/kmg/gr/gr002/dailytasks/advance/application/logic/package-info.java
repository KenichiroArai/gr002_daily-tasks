/**
 * 先行機能の application 層のロジック<br>
 * <p>
 * application 層が domain 層に求めるロジック（画面用集計の読み書き、日別の集計）のインタフェースを置く。実装は domain 層に置く。 ロジックはサービスを呼ばず、個々のメソッドがサービスなどから何度も呼ばれる。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.2.0
 *
 * @version 0.2.0
 */
package kmg.gr.gr002.dailytasks.advance.application.logic;