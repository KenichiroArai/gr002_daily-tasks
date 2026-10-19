package kmg.gr.gr002.dailytasks.carryover.domain.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link CarryoverParsePatternTypes} のテスト<br>
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
public class CarryoverParsePatternTypesTest {

    /**
     * get メソッドのテスト - 正常系:チェックボックスの行
     *
     * @since 0.1.0
     */
    @Test
    public void testGet_normalValue() {

        /* 期待値の定義 */
        final String expectedKey = "Checkbox";

        /* 準備 */
        final CarryoverParsePatternTypes testTarget = CarryoverParsePatternTypes.CHECKBOX;

        /* テスト対象の実行 */
        final String testResult = testTarget.get();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "キーが一致しません");

    }

    /**
     * getDefault メソッドのテスト - 正常系:指定無しを返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDefault_normalNone() {

        /* 期待値の定義 */
        final CarryoverParsePatternTypes expectedType = CarryoverParsePatternTypes.NONE;

        /* テスト対象の実行 */
        final CarryoverParsePatternTypes testResult = CarryoverParsePatternTypes.getDefault();

        /* 検証の準備 */
        final CarryoverParsePatternTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "デフォルト値が一致しません");

    }

    /**
     * getDetail メソッドのテスト - 正常系:持ち越し元の日付を含む内容
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDetail_normalValue() {

        /* 期待値の定義 */
        final String expectedDetail = "持ち越し元の日付を含む内容";

        /* 準備 */
        final CarryoverParsePatternTypes testTarget = CarryoverParsePatternTypes.DATED_CONTENT;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDetail();

        /* 検証の準備 */
        final String actualDetail = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDetail, actualDetail, "詳細情報が一致しません");

    }

    /**
     * getDisplayName メソッドのテスト - 正常系:区切り線
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDisplayName_normalValue() {

        /* 期待値の定義 */
        final String expectedDisplayName = "区切り線";

        /* 準備 */
        final CarryoverParsePatternTypes testTarget = CarryoverParsePatternTypes.HORIZONTAL_RULE;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDisplayName();

        /* 検証の準備 */
        final String actualDisplayName = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDisplayName, actualDisplayName, "表示名が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 正常系:チェックボックスの行
     *
     * @since 0.1.0
     */
    @Test
    public void testGetEnum_normalValue() {

        /* 期待値の定義 */
        final CarryoverParsePatternTypes expectedType = CarryoverParsePatternTypes.CHECKBOX;

        /* 準備 */
        final String testKey = "^\\s*[-*]\\s*\\[([ xX])\\]\\s*(.*?)\\s*$";

        /* テスト対象の実行 */
        final CarryoverParsePatternTypes testResult = CarryoverParsePatternTypes.getEnum(testKey);

        /* 検証の準備 */
        final CarryoverParsePatternTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "種類が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 準正常系:不明なキーの場合は指定無しを返す
     *
     * @since 0.1.0
     */
    @Test
    public void testGetEnum_semiUnknownKey() {

        /* 期待値の定義 */
        final CarryoverParsePatternTypes expectedType = CarryoverParsePatternTypes.NONE;

        /* 準備 */
        final String testKey = "unknown-key";

        /* テスト対象の実行 */
        final CarryoverParsePatternTypes testResult = CarryoverParsePatternTypes.getEnum(testKey);

        /* 検証の準備 */
        final CarryoverParsePatternTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "不明なキーの場合は指定無しが返る必要があります");

    }

    /**
     * getInitValue メソッドのテスト - 正常系:指定無しを返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetInitValue_normalNone() {

        /* 期待値の定義 */
        final CarryoverParsePatternTypes expectedType = CarryoverParsePatternTypes.NONE;

        /* テスト対象の実行 */
        final CarryoverParsePatternTypes testResult = CarryoverParsePatternTypes.getInitValue();

        /* 検証の準備 */
        final CarryoverParsePatternTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "初期値が一致しません");

    }

    /**
     * getKey メソッドのテスト - 正常系:持ち越し元の日付を含む内容
     *
     * @since 0.1.0
     */
    @Test
    public void testGetKey_normalValue() {

        /* 期待値の定義 */
        final String expectedKey = "DatedContent";

        /* 準備 */
        final CarryoverParsePatternTypes testTarget = CarryoverParsePatternTypes.DATED_CONTENT;

        /* テスト対象の実行 */
        final String testResult = testTarget.getKey();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "キーが一致しません");

    }

    /**
     * toString メソッドのテスト - 正常系:区切り線
     *
     * @since 0.1.0
     */
    @Test
    public void testToString_normalValue() {

        /* 期待値の定義 */
        final String expectedKey = "HorizontalRule";

        /* 準備 */
        final CarryoverParsePatternTypes testTarget = CarryoverParsePatternTypes.HORIZONTAL_RULE;

        /* テスト対象の実行 */
        final String testResult = testTarget.toString();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "文字列が一致しません");

    }

}
