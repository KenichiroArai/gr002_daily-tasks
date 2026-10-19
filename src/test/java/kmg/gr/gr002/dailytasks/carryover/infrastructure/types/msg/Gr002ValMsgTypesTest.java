package kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link Gr002ValMsgTypes} のテスト<br>
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
public class Gr002ValMsgTypesTest {

    /**
     * get メソッドのテスト - 正常系:指定無しのキーを返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGet_normalNone() {

        /* 期待値の定義 */
        final String expectedKey = "None";

        /* 準備 */
        final Gr002ValMsgTypes testTarget = Gr002ValMsgTypes.NONE;

        /* テスト対象の実行 */
        final String testResult = testTarget.get();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "キーが一致しません");

    }

    /**
     * getCode メソッドのテスト - 正常系:指定無しのコードを返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetCode_normalNone() {

        /* 期待値の定義 */
        final String expectedCode = "None";

        /* 準備 */
        final Gr002ValMsgTypes testTarget = Gr002ValMsgTypes.NONE;

        /* テスト対象の実行 */
        final String testResult = testTarget.getCode();

        /* 検証の準備 */
        final String actualCode = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedCode, actualCode, "コードが一致しません");

    }

    /**
     * getDefault メソッドのテスト - 正常系:指定無しを返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDefault_normalNone() {

        /* 期待値の定義 */
        final Gr002ValMsgTypes expectedType = Gr002ValMsgTypes.NONE;

        /* テスト対象の実行 */
        final Gr002ValMsgTypes testResult = Gr002ValMsgTypes.getDefault();

        /* 検証の準備 */
        final Gr002ValMsgTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "デフォルト値が一致しません");

    }

    /**
     * getDetail メソッドのテスト - 正常系:指定無しの詳細
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDetail_normalNone() {

        /* 期待値の定義 */
        final String expectedDetail = "指定無し";

        /* 準備 */
        final Gr002ValMsgTypes testTarget = Gr002ValMsgTypes.NONE;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDetail();

        /* 検証の準備 */
        final String actualDetail = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDetail, actualDetail, "詳細情報が一致しません");

    }

    /**
     * getDisplayName メソッドのテスト - 正常系:指定無しの表示名
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDisplayName_normalNone() {

        /* 期待値の定義 */
        final String expectedDisplayName = "指定無し";

        /* 準備 */
        final Gr002ValMsgTypes testTarget = Gr002ValMsgTypes.NONE;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDisplayName();

        /* 検証の準備 */
        final String actualDisplayName = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDisplayName, actualDisplayName, "表示名が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 正常系:指定無しのキーから種類を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetEnum_normalNone() {

        /* 期待値の定義 */
        final Gr002ValMsgTypes expectedType = Gr002ValMsgTypes.NONE;

        /* 準備 */
        final String testKey = "None";

        /* テスト対象の実行 */
        final Gr002ValMsgTypes testResult = Gr002ValMsgTypes.getEnum(testKey);

        /* 検証の準備 */
        final Gr002ValMsgTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "メッセージの種類が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 準正常系:不明なキーの場合は指定無しを返す
     *
     * @since 0.1.0
     */
    @Test
    public void testGetEnum_semiUnknownKey() {

        /* 期待値の定義 */
        final Gr002ValMsgTypes expectedType = Gr002ValMsgTypes.NONE;

        /* 準備 */
        final String testKey = "GR002_VAL99999";

        /* テスト対象の実行 */
        final Gr002ValMsgTypes testResult = Gr002ValMsgTypes.getEnum(testKey);

        /* 検証の準備 */
        final Gr002ValMsgTypes actualType = testResult;

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
        final Gr002ValMsgTypes expectedType = Gr002ValMsgTypes.NONE;

        /* テスト対象の実行 */
        final Gr002ValMsgTypes testResult = Gr002ValMsgTypes.getInitValue();

        /* 検証の準備 */
        final Gr002ValMsgTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "初期値が一致しません");

    }

    /**
     * getKey メソッドのテスト - 正常系:指定無しのキー
     *
     * @since 0.1.0
     */
    @Test
    public void testGetKey_normalNone() {

        /* 期待値の定義 */
        final String expectedKey = "None";

        /* 準備 */
        final Gr002ValMsgTypes testTarget = Gr002ValMsgTypes.NONE;

        /* テスト対象の実行 */
        final String testResult = testTarget.getKey();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "キーが一致しません");

    }

    /**
     * getValue メソッドのテスト - 正常系:指定無しの文面
     *
     * @since 0.1.0
     */
    @Test
    public void testGetValue_normalNone() {

        /* 期待値の定義 */
        final String expectedValue = "指定無し";

        /* 準備 */
        final Gr002ValMsgTypes testTarget = Gr002ValMsgTypes.NONE;

        /* テスト対象の実行 */
        final String testResult = testTarget.getValue();

        /* 検証の準備 */
        final String actualValue = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedValue, actualValue, "メッセージの値が一致しません");

    }

    /**
     * toString メソッドのテスト - 正常系:指定無しのキー
     *
     * @since 0.1.0
     */
    @Test
    public void testToString_normalNone() {

        /* 期待値の定義 */
        final String expectedKey = "None";

        /* 準備 */
        final Gr002ValMsgTypes testTarget = Gr002ValMsgTypes.NONE;

        /* テスト対象の実行 */
        final String testResult = testTarget.toString();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "文字列が一致しません");

    }

}
