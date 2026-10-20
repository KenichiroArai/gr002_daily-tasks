/**
 * 持ち越し機能のサービスの実装<br>
 * <p>
 * presentation 層のサービスと、同じ層の役割ごとのサービスを実装する。収集サービスが Issue の取得、解析、保存を行い、集計サービスが日別の集計と保存を行う。統合サービスがそれらを順に呼ぶ。
 * </p>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
package kmg.gr.gr002.dailytasks.carryover.application.service.impl;
