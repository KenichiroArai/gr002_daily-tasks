package kmg.gr.gr002.dailytasks.carryover.repository.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link CarryoverDataFormatTypes} のテスト<br>
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
public class CarryoverDataFormatTypesTest {

    /**
     * get メソッドのテスト - 正常系:インデント
     *
     * @since 0.1.0
     */
    @Test
    public void testGet_normalValue() {

        /* 期待値の定義 */
        final String expectedKey = "Indent";

        /* 準備 */
        final CarryoverDataFormatTypes testTarget = CarryoverDataFormatTypes.INDENT;

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
        final CarryoverDataFormatTypes expectedType = CarryoverDataFormatTypes.NONE;

        /* テスト対象の実行 */
        final CarryoverDataFormatTypes testResult = CarryoverDataFormatTypes.getDefault();

        /* 検証の準備 */
        final CarryoverDataFormatTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "デフォルト値が一致しません");

    }

    /**
     * getDetail メソッドのテスト - 正常系:改行
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDetail_normalValue() {

        /* 期待値の定義 */
        final String expectedDetail = "JSON の改行";

        /* 準備 */
        final CarryoverDataFormatTypes testTarget = CarryoverDataFormatTypes.LINE_FEED;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDetail();

        /* 検証の準備 */
        final String actualDetail = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDetail, actualDetail, "詳細情報が一致しません");

    }

    /**
     * getDisplayName メソッドのテスト - 正常系:Issue ファイル名の書式
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDisplayName_normalValue() {

        /* 期待値の定義 */
        final String expectedDisplayName = "Issue ファイル名の書式";

        /* 準備 */
        final CarryoverDataFormatTypes testTarget = CarryoverDataFormatTypes.ISSUE_FILE_NAME;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDisplayName();

        /* 検証の準備 */
        final String actualDisplayName = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDisplayName, actualDisplayName, "表示名が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 正常系:インデント
     *
     * @since 0.1.0
     */
    @Test
    public void testGetEnum_normalValue() {

        /* 期待値の定義 */
        final CarryoverDataFormatTypes expectedType = CarryoverDataFormatTypes.INDENT;

        /* 準備 */
        final String testKey = "  ";

        /* テスト対象の実行 */
        final CarryoverDataFormatTypes testResult = CarryoverDataFormatTypes.getEnum(testKey);

        /* 検証の準備 */
        final CarryoverDataFormatTypes actualType = testResult;

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
        final CarryoverDataFormatTypes expectedType = CarryoverDataFormatTypes.NONE;

        /* 準備 */
        final String testKey = "unknown-key";

        /* テスト対象の実行 */
        final CarryoverDataFormatTypes testResult = CarryoverDataFormatTypes.getEnum(testKey);

        /* 検証の準備 */
        final CarryoverDataFormatTypes actualType = testResult;

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
        final CarryoverDataFormatTypes expectedType = CarryoverDataFormatTypes.NONE;

        /* テスト対象の実行 */
        final CarryoverDataFormatTypes testResult = CarryoverDataFormatTypes.getInitValue();

        /* 検証の準備 */
        final CarryoverDataFormatTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "初期値が一致しません");

    }

    /**
     * getKey メソッドのテスト - 正常系:改行
     *
     * @since 0.1.0
     */
    @Test
    public void testGetKey_normalValue() {

        /* 期待値の定義 */
        final String expectedKey = "LineFeed";

        /* 準備 */
        final CarryoverDataFormatTypes testTarget = CarryoverDataFormatTypes.LINE_FEED;

        /* テスト対象の実行 */
        final String testResult = testTarget.getKey();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "キーが一致しません");

    }

    /**
     * toString メソッドのテスト - 正常系:Issue ファイル名の書式
     *
     * @since 0.1.0
     */
    @Test
    public void testToString_normalValue() {

        /* 期待値の定義 */
        final String expectedKey = "IssueFileName";

        /* 準備 */
        final CarryoverDataFormatTypes testTarget = CarryoverDataFormatTypes.ISSUE_FILE_NAME;

        /* テスト対象の実行 */
        final String testResult = testTarget.toString();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "文字列が一致しません");

    }

}
