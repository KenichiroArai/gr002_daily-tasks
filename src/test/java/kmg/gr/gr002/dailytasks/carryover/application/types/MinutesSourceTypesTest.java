package kmg.gr.gr002.dailytasks.carryover.application.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link MinutesSourceTypes} のテスト<br>
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
public class MinutesSourceTypesTest {

    /**
     * get メソッドのテスト - 正常系:保存するときの値を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGet_normalDefault() {

        /* 期待値の定義 */
        final String expectedKey = "Default";

        /* 準備 */
        final MinutesSourceTypes testTarget = MinutesSourceTypes.DEFAULT;

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
        final MinutesSourceTypes expectedType = MinutesSourceTypes.NONE;

        /* テスト対象の実行 */
        final MinutesSourceTypes testResult = MinutesSourceTypes.getDefault();

        /* 検証の準備 */
        final MinutesSourceTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "デフォルト値が一致しません");

    }

    /**
     * getDetail メソッドのテスト - 正常系:本文から取得した場合の詳細
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDetail_normalParsed() {

        /* 期待値の定義 */
        final String expectedDetail = "Issue 本文の時間表記から取得";

        /* 準備 */
        final MinutesSourceTypes testTarget = MinutesSourceTypes.PARSED;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDetail();

        /* 検証の準備 */
        final String actualDetail = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDetail, actualDetail, "詳細情報が一致しません");

    }

    /**
     * getDisplayName メソッドのテスト - 正常系:標準時間が未登録の場合の表示名
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDisplayName_normalUnknown() {

        /* 期待値の定義 */
        final String expectedDisplayName = "標準時間が未登録のため 0 分で補完";

        /* 準備 */
        final MinutesSourceTypes testTarget = MinutesSourceTypes.UNKNOWN;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDisplayName();

        /* 検証の準備 */
        final String actualDisplayName = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDisplayName, actualDisplayName, "表示名が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 正常系:保存するときの値から取得元を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetEnum_normalDefault() {

        /* 期待値の定義 */
        final MinutesSourceTypes expectedType = MinutesSourceTypes.DEFAULT;

        /* 準備 */
        final String testKey = "default";

        /* テスト対象の実行 */
        final MinutesSourceTypes testResult = MinutesSourceTypes.getEnum(testKey);

        /* 検証の準備 */
        final MinutesSourceTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "残り時間の取得元が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 準正常系:不明なキーの場合は指定無しを返す
     *
     * @since 0.1.0
     */
    @Test
    public void testGetEnum_semiUnknownKey() {

        /* 期待値の定義 */
        final MinutesSourceTypes expectedType = MinutesSourceTypes.NONE;

        /* 準備 */
        final String testKey = "xxx";

        /* テスト対象の実行 */
        final MinutesSourceTypes testResult = MinutesSourceTypes.getEnum(testKey);

        /* 検証の準備 */
        final MinutesSourceTypes actualType = testResult;

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
        final MinutesSourceTypes expectedType = MinutesSourceTypes.NONE;

        /* テスト対象の実行 */
        final MinutesSourceTypes testResult = MinutesSourceTypes.getInitValue();

        /* 検証の準備 */
        final MinutesSourceTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "初期値が一致しません");

    }

    /**
     * getKey メソッドのテスト - 正常系:標準時間で補完した場合のキー
     *
     * @since 0.1.0
     */
    @Test
    public void testGetKey_normalDefault() {

        /* 期待値の定義 */
        final String expectedKey = "Default";

        /* 準備 */
        final MinutesSourceTypes testTarget = MinutesSourceTypes.DEFAULT;

        /* テスト対象の実行 */
        final String testResult = testTarget.getKey();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "キーが一致しません");

    }

    /**
     * toString メソッドのテスト - 正常系:本文から取得した場合のキー
     *
     * @since 0.1.0
     */
    @Test
    public void testToString_normalParsed() {

        /* 期待値の定義 */
        final String expectedKey = "Parsed";

        /* 準備 */
        final MinutesSourceTypes testTarget = MinutesSourceTypes.PARSED;

        /* テスト対象の実行 */
        final String testResult = testTarget.toString();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "文字列が一致しません");

    }

}
