package kmg.gr.gr002.dailytasks.carryover.repository.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link CarryoverDataFileTypes} のテスト<br>
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
public class CarryoverDataFileTypesTest {

    /**
     * get メソッドのテスト - 正常系:集計ファイル名を返す場合
     */
    @Test
    public void testGet_normalSummaryFile() {

        /* 期待値の定義 */
        final String expectedKey = "SummaryFile";

        /* 準備 */
        final CarryoverDataFileTypes testTarget = CarryoverDataFileTypes.SUMMARY_FILE;

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
        final CarryoverDataFileTypes expectedType = CarryoverDataFileTypes.NONE;

        /* テスト対象の実行 */
        final CarryoverDataFileTypes testResult = CarryoverDataFileTypes.getDefault();

        /* 検証の準備 */
        final CarryoverDataFileTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "デフォルト値が一致しません");

    }

    /**
     * getDetail メソッドのテスト - 正常系:拡張子の詳細
     */
    @Test
    public void testGetDetail_normalJsonExtension() {

        /* 期待値の定義 */
        final String expectedDetail = "JSON ファイルの拡張子";

        /* 準備 */
        final CarryoverDataFileTypes testTarget = CarryoverDataFileTypes.JSON_EXTENSION;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDetail();

        /* 検証の準備 */
        final String actualDetail = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDetail, actualDetail, "詳細情報が一致しません");

    }

    /**
     * getDisplayName メソッドのテスト - 正常系:ディレクトリ名の表示名
     */
    @Test
    public void testGetDisplayName_normalIssuesDir() {

        /* 期待値の定義 */
        final String expectedDisplayName = "Issue ごとの JSON を置くディレクトリ名";

        /* 準備 */
        final CarryoverDataFileTypes testTarget = CarryoverDataFileTypes.ISSUES_DIR;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDisplayName();

        /* 検証の準備 */
        final String actualDisplayName = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDisplayName, actualDisplayName, "表示名が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 正常系:ファイル名から種類を返す場合
     */
    @Test
    public void testGetEnum_normalIssuesDir() {

        /* 期待値の定義 */
        final CarryoverDataFileTypes expectedType = CarryoverDataFileTypes.ISSUES_DIR;

        /* 準備 */
        final String testKey = "issues";

        /* テスト対象の実行 */
        final CarryoverDataFileTypes testResult = CarryoverDataFileTypes.getEnum(testKey);

        /* 検証の準備 */
        final CarryoverDataFileTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "ファイルの種類が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 準正常系:不明な名前の場合は指定無しを返す
     */
    @Test
    public void testGetEnum_semiUnknownKey() {

        /* 期待値の定義 */
        final CarryoverDataFileTypes expectedType = CarryoverDataFileTypes.NONE;

        /* 準備 */
        final String testKey = "other.json";

        /* テスト対象の実行 */
        final CarryoverDataFileTypes testResult = CarryoverDataFileTypes.getEnum(testKey);

        /* 検証の準備 */
        final CarryoverDataFileTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "不明な名前の場合は指定無しが返る必要があります");

    }

    /**
     * getInitValue メソッドのテスト - 正常系:指定無しを返す場合
     */
    @Test
    public void testGetInitValue_normalNone() {

        /* 期待値の定義 */
        final CarryoverDataFileTypes expectedType = CarryoverDataFileTypes.NONE;

        /* テスト対象の実行 */
        final CarryoverDataFileTypes testResult = CarryoverDataFileTypes.getInitValue();

        /* 検証の準備 */
        final CarryoverDataFileTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "初期値が一致しません");

    }

    /**
     * getKey メソッドのテスト - 正常系:拡張子
     */
    @Test
    public void testGetKey_normalJsonExtension() {

        /* 期待値の定義 */
        final String expectedKey = "JsonExtension";

        /* 準備 */
        final CarryoverDataFileTypes testTarget = CarryoverDataFileTypes.JSON_EXTENSION;

        /* テスト対象の実行 */
        final String testResult = testTarget.getKey();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "キーが一致しません");

    }

    /**
     * toString メソッドのテスト - 正常系:集計ファイル名
     */
    @Test
    public void testToString_normalSummaryFile() {

        /* 期待値の定義 */
        final String expectedKey = "SummaryFile";

        /* 準備 */
        final CarryoverDataFileTypes testTarget = CarryoverDataFileTypes.SUMMARY_FILE;

        /* テスト対象の実行 */
        final String testResult = testTarget.toString();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "文字列が一致しません");

    }

}
