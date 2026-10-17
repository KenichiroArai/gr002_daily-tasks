package kmg.gr.gr002.dailytasks.carryover.domain.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link TargetSectionTypes} のテスト<br>
 *
 * @author KenichiroArai
 *
 * @since 0.1.0
 *
 * @version 0.1.0
 */
@SuppressWarnings({
    "nls", "static-method"
})
public class TargetSectionTypesTest {

    /**
     * get メソッドのテスト - 正常系:持ち越しのセクション名を返す場合
     */
    @Test
    public void testGet_normalCarryover() {

        /* 期待値の定義 */
        final String expectedKey = "Carryover";

        /* 準備 */
        final TargetSectionTypes testTarget = TargetSectionTypes.CARRYOVER;

        /* テスト対象の実行 */
        final String testResult = testTarget.get();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "キーが一致しません");

    }

    /**
     * getDefault メソッドのテスト - 正常系:指定無しを返す場合
     */
    @Test
    public void testGetDefault_normalNone() {

        /* 期待値の定義 */
        final TargetSectionTypes expectedType = TargetSectionTypes.NONE;

        /* テスト対象の実行 */
        final TargetSectionTypes testResult = TargetSectionTypes.getDefault();

        /* 検証の準備 */
        final TargetSectionTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "デフォルト値が一致しません");

    }

    /**
     * getDetail メソッドのテスト - 正常系:負債の詳細
     */
    @Test
    public void testGetDetail_normalDebt() {

        /* 期待値の定義 */
        final String expectedDetail = "旧名称の対象セクション";

        /* 準備 */
        final TargetSectionTypes testTarget = TargetSectionTypes.DEBT;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDetail();

        /* 検証の準備 */
        final String actualDetail = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDetail, actualDetail, "詳細情報が一致しません");

    }

    /**
     * getDisplayName メソッドのテスト - 正常系:繰り越しの表示名
     */
    @Test
    public void testGetDisplayName_normalLegacy() {

        /* 期待値の定義 */
        final String expectedDisplayName = "繰り越し";

        /* 準備 */
        final TargetSectionTypes testTarget = TargetSectionTypes.CARRYOVER_LEGACY;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDisplayName();

        /* 検証の準備 */
        final String actualDisplayName = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDisplayName, actualDisplayName, "表示名が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 正常系:セクション名から種類を返す場合
     */
    @Test
    public void testGetEnum_normalDebt() {

        /* 期待値の定義 */
        final TargetSectionTypes expectedType = TargetSectionTypes.DEBT;

        /* 準備 */
        final String testKey = "負債";

        /* テスト対象の実行 */
        final TargetSectionTypes testResult = TargetSectionTypes.getEnum(testKey);

        /* 検証の準備 */
        final TargetSectionTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "対象セクションが一致しません");

    }

    /**
     * getEnum メソッドのテスト - 準正常系:対象外のセクション名の場合は指定無しを返す
     */
    @Test
    public void testGetEnum_semiUnknownKey() {

        /* 期待値の定義 */
        final TargetSectionTypes expectedType = TargetSectionTypes.NONE;

        /* 準備 */
        final String testKey = "メモ";

        /* テスト対象の実行 */
        final TargetSectionTypes testResult = TargetSectionTypes.getEnum(testKey);

        /* 検証の準備 */
        final TargetSectionTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "対象外の場合は指定無しが返る必要があります");

    }

    /**
     * getInitValue メソッドのテスト - 正常系:指定無しを返す場合
     */
    @Test
    public void testGetInitValue_normalNone() {

        /* 期待値の定義 */
        final TargetSectionTypes expectedType = TargetSectionTypes.NONE;

        /* テスト対象の実行 */
        final TargetSectionTypes testResult = TargetSectionTypes.getInitValue();

        /* 検証の準備 */
        final TargetSectionTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "初期値が一致しません");

    }

    /**
     * getKey メソッドのテスト - 正常系:繰り越しのセクション名
     */
    @Test
    public void testGetKey_normalLegacy() {

        /* 期待値の定義 */
        final String expectedKey = "CarryoverLegacy";

        /* 準備 */
        final TargetSectionTypes testTarget = TargetSectionTypes.CARRYOVER_LEGACY;

        /* テスト対象の実行 */
        final String testResult = testTarget.getKey();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "キーが一致しません");

    }

    /**
     * toString メソッドのテスト - 正常系:持ち越しのセクション名
     */
    @Test
    public void testToString_normalCarryover() {

        /* 期待値の定義 */
        final String expectedKey = "Carryover";

        /* 準備 */
        final TargetSectionTypes testTarget = TargetSectionTypes.CARRYOVER;

        /* テスト対象の実行 */
        final String testResult = testTarget.toString();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "文字列が一致しません");

    }

}
