package kmg.gr.gr002.dailytasks.advance.repository.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link AdvanceDataFileTypes} のテスト<br>
 *
 * @author KenichiroArai
 *
 * @since 0.2.0
 *
 * @version 0.2.0
 */
@SuppressWarnings({
    "nls", "static-method"
})
public class AdvanceDataFileTypesTest {

    /**
     * get メソッドのテスト - 正常系:集計ファイル名を返す場合
     *
     * @since 0.2.0
     */
    @Test
    public void testGet_normalSummaryFile() {

        /* 期待値の定義 */
        final String expectedKey = "SummaryFile";

        /* 準備 */
        final AdvanceDataFileTypes testTarget = AdvanceDataFileTypes.SUMMARY_FILE;

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
     * @since 0.2.0
     */
    @Test
    public void testGetDefault_normalNone() {

        /* 期待値の定義 */
        final AdvanceDataFileTypes expectedType = AdvanceDataFileTypes.NONE;

        /* テスト対象の実行 */
        final AdvanceDataFileTypes testResult = AdvanceDataFileTypes.getDefault();

        /* 検証の準備 */
        final AdvanceDataFileTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "デフォルト値が一致しません");

    }

    /**
     * getDetail メソッドのテスト - 正常系:拡張子の詳細
     *
     * @since 0.2.0
     */
    @Test
    public void testGetDetail_normalJsonExtension() {

        /* 期待値の定義 */
        final String expectedDetail = "JSON ファイルの拡張子";

        /* 準備 */
        final AdvanceDataFileTypes testTarget = AdvanceDataFileTypes.JSON_EXTENSION;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDetail();

        /* 検証の準備 */
        final String actualDetail = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDetail, actualDetail, "詳細情報が一致しません");

    }

    /**
     * getDisplayName メソッドのテスト - 正常系:ディレクトリ名の表示名
     *
     * @since 0.2.0
     */
    @Test
    public void testGetDisplayName_normalAdvanceDir() {

        /* 期待値の定義 */
        final String expectedDisplayName = "先行の出力を置くディレクトリ名";

        /* 準備 */
        final AdvanceDataFileTypes testTarget = AdvanceDataFileTypes.ADVANCE_DIR;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDisplayName();

        /* 検証の準備 */
        final String actualDisplayName = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDisplayName, actualDisplayName, "表示名が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 正常系:キーから種類を返す場合
     *
     * @since 0.2.0
     */
    @Test
    public void testGetEnum_normalAdvanceDirKey() {

        /* 期待値の定義 */
        final AdvanceDataFileTypes expectedType = AdvanceDataFileTypes.ADVANCE_DIR;

        /* 準備 */
        final String testKey = "AdvanceDir";

        /* テスト対象の実行 */
        final AdvanceDataFileTypes testResult = AdvanceDataFileTypes.getEnum(testKey);

        /* 検証の準備 */
        final AdvanceDataFileTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "ファイルの種類が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 正常系:ファイル名の別名から種類を返す場合
     *
     * @since 0.2.0
     */
    @Test
    public void testGetEnum_normalAdvanceDirAlias() {

        /* 期待値の定義 */
        final AdvanceDataFileTypes expectedType = AdvanceDataFileTypes.ADVANCE_DIR;

        /* 準備 */
        final String testKey = "advance";

        /* テスト対象の実行 */
        final AdvanceDataFileTypes testResult = AdvanceDataFileTypes.getEnum(testKey);

        /* 検証の準備 */
        final AdvanceDataFileTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "別名からの種類が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 正常系:集計ファイル名の別名から種類を返す場合
     *
     * @since 0.2.0
     */
    @Test
    public void testGetEnum_normalSummaryFileAlias() {

        /* 期待値の定義 */
        final AdvanceDataFileTypes expectedType = AdvanceDataFileTypes.SUMMARY_FILE;

        /* 準備 */
        final String testKey = "summary.json";

        /* テスト対象の実行 */
        final AdvanceDataFileTypes testResult = AdvanceDataFileTypes.getEnum(testKey);

        /* 検証の準備 */
        final AdvanceDataFileTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "別名からの種類が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 正常系:拡張子の別名から種類を返す場合
     *
     * @since 0.2.0
     */
    @Test
    public void testGetEnum_normalJsonExtensionAlias() {

        /* 期待値の定義 */
        final AdvanceDataFileTypes expectedType = AdvanceDataFileTypes.JSON_EXTENSION;

        /* 準備 */
        final String testKey = ".json";

        /* テスト対象の実行 */
        final AdvanceDataFileTypes testResult = AdvanceDataFileTypes.getEnum(testKey);

        /* 検証の準備 */
        final AdvanceDataFileTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "別名からの種類が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 準正常系:不明な名前の場合は指定無しを返す
     *
     * @since 0.2.0
     */
    @Test
    public void testGetEnum_semiUnknownKey() {

        /* 期待値の定義 */
        final AdvanceDataFileTypes expectedType = AdvanceDataFileTypes.NONE;

        /* 準備 */
        final String testKey = "other.json";

        /* テスト対象の実行 */
        final AdvanceDataFileTypes testResult = AdvanceDataFileTypes.getEnum(testKey);

        /* 検証の準備 */
        final AdvanceDataFileTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "不明な名前の場合は指定無しが返る必要があります");

    }

    /**
     * getFileName メソッドのテスト - 正常系:先行ディレクトリ名
     *
     * @since 0.2.0
     */
    @Test
    public void testGetFileName_normalAdvanceDir() {

        /* 期待値の定義 */
        final String expectedFileName = "advance";

        /* 準備 */
        final AdvanceDataFileTypes testTarget = AdvanceDataFileTypes.ADVANCE_DIR;

        /* テスト対象の実行 */
        final String testResult = testTarget.getFileName();

        /* 検証の準備 */
        final String actualFileName = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedFileName, actualFileName, "ファイル名が一致しません");

    }

    /**
     * getFileName メソッドのテスト - 準正常系:指定無しは null を返す場合
     *
     * @since 0.2.0
     */
    @Test
    public void testGetFileName_semiNone() {

        /* 期待値の定義 */

        /* 準備 */
        final AdvanceDataFileTypes testTarget = AdvanceDataFileTypes.NONE;

        /* テスト対象の実行 */
        final String testResult = testTarget.getFileName();

        /* 検証の準備 */
        final boolean actualNull = testResult == null;

        /* 検証の実施 */
        Assertions.assertTrue(actualNull, "指定無しのファイル名は null になる必要があります");

    }

    /**
     * getInitValue メソッドのテスト - 正常系:指定無しを返す場合
     *
     * @since 0.2.0
     */
    @Test
    public void testGetInitValue_normalNone() {

        /* 期待値の定義 */
        final AdvanceDataFileTypes expectedType = AdvanceDataFileTypes.NONE;

        /* テスト対象の実行 */
        final AdvanceDataFileTypes testResult = AdvanceDataFileTypes.getInitValue();

        /* 検証の準備 */
        final AdvanceDataFileTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "初期値が一致しません");

    }

    /**
     * getKey メソッドのテスト - 正常系:拡張子
     *
     * @since 0.2.0
     */
    @Test
    public void testGetKey_normalJsonExtension() {

        /* 期待値の定義 */
        final String expectedKey = "JsonExtension";

        /* 準備 */
        final AdvanceDataFileTypes testTarget = AdvanceDataFileTypes.JSON_EXTENSION;

        /* テスト対象の実行 */
        final String testResult = testTarget.getKey();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "キーが一致しません");

    }

    /**
     * toString メソッドのテスト - 正常系:集計ファイル名
     *
     * @since 0.2.0
     */
    @Test
    public void testToString_normalSummaryFile() {

        /* 期待値の定義 */
        final String expectedKey = "SummaryFile";

        /* 準備 */
        final AdvanceDataFileTypes testTarget = AdvanceDataFileTypes.SUMMARY_FILE;

        /* テスト対象の実行 */
        final String testResult = testTarget.toString();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "文字列が一致しません");

    }

}