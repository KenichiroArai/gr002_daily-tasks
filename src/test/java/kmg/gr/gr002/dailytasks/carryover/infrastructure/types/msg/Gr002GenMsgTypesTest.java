package kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link Gr002GenMsgTypes} のテスト<br>
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
public class Gr002GenMsgTypesTest {

    /**
     * get メソッドのテスト - 正常系:不明な引数のキーを返す場合
     */
    @Test
    public void testGet_normalUnknownArgument() {

        /* 期待値の定義 */
        final String expectedKey = "GR002_GEN11101";

        /* 準備 */
        final Gr002GenMsgTypes testTarget = Gr002GenMsgTypes.GR002_GEN11101;

        /* テスト対象の実行 */
        final String testResult = testTarget.get();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "キーが一致しません");

    }

    /**
     * getCode メソッドのテスト - 正常系:指定無しのコードを返す場合
     */
    @Test
    public void testGetCode_normalNone() {

        /* 期待値の定義 */
        final String expectedCode = "None";

        /* 準備 */
        final Gr002GenMsgTypes testTarget = Gr002GenMsgTypes.NONE;

        /* テスト対象の実行 */
        final String testResult = testTarget.getCode();

        /* 検証の準備 */
        final String actualCode = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedCode, actualCode, "コードが一致しません");

    }

    /**
     * getDefault メソッドのテスト - 正常系:指定無しを返す場合
     */
    @Test
    public void testGetDefault_normalNone() {

        /* 期待値の定義 */
        final Gr002GenMsgTypes expectedType = Gr002GenMsgTypes.NONE;

        /* テスト対象の実行 */
        final Gr002GenMsgTypes testResult = Gr002GenMsgTypes.getDefault();

        /* 検証の準備 */
        final Gr002GenMsgTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "デフォルト値が一致しません");

    }

    /**
     * getDetail メソッドのテスト - 正常系:指定無しの詳細
     */
    @Test
    public void testGetDetail_normalNone() {

        /* 期待値の定義 */
        final String expectedDetail = "指定無し";

        /* 準備 */
        final Gr002GenMsgTypes testTarget = Gr002GenMsgTypes.NONE;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDetail();

        /* 検証の準備 */
        final String actualDetail = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDetail, actualDetail, "詳細情報が一致しません");

    }

    /**
     * getDisplayName メソッドのテスト - 正常系:中断の文面
     */
    @Test
    public void testGetDisplayName_normalInterrupted() {

        /* 期待値の定義 */
        final String expectedDisplayName = "GitHub API の中断";

        /* 準備 */
        final Gr002GenMsgTypes testTarget = Gr002GenMsgTypes.GR002_GEN11105;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDisplayName();

        /* 検証の準備 */
        final String actualDisplayName = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDisplayName, actualDisplayName, "表示名が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 正常系:定数名から種類を返す場合
     */
    @Test
    public void testGetEnum_normalUnknownArgument() {

        /* 期待値の定義 */
        final Gr002GenMsgTypes expectedType = Gr002GenMsgTypes.GR002_GEN11101;

        /* 準備 */
        final String testKey = "GR002_GEN11101";

        /* テスト対象の実行 */
        final Gr002GenMsgTypes testResult = Gr002GenMsgTypes.getEnum(testKey);

        /* 検証の準備 */
        final Gr002GenMsgTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "メッセージの種類が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 準正常系:不明なキーの場合は指定無しを返す
     */
    @Test
    public void testGetEnum_semiUnknownKey() {

        /* 期待値の定義 */
        final Gr002GenMsgTypes expectedType = Gr002GenMsgTypes.NONE;

        /* 準備 */
        final String testKey = "GR002_GEN99999";

        /* テスト対象の実行 */
        final Gr002GenMsgTypes testResult = Gr002GenMsgTypes.getEnum(testKey);

        /* 検証の準備 */
        final Gr002GenMsgTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "不明なキーの場合は指定無しが返る必要があります");

    }

    /**
     * getInitValue メソッドのテスト - 正常系:指定無しを返す場合
     */
    @Test
    public void testGetInitValue_normalNone() {

        /* 期待値の定義 */
        final Gr002GenMsgTypes expectedType = Gr002GenMsgTypes.NONE;

        /* テスト対象の実行 */
        final Gr002GenMsgTypes testResult = Gr002GenMsgTypes.getInitValue();

        /* 検証の準備 */
        final Gr002GenMsgTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "初期値が一致しません");

    }

    /**
     * getKey メソッドのテスト - 正常系:使い方のキー
     */
    @Test
    public void testGetKey_normalUsage() {

        /* 期待値の定義 */
        final String expectedKey = "GR002_GEN11100";

        /* 準備 */
        final Gr002GenMsgTypes testTarget = Gr002GenMsgTypes.GR002_GEN11100;

        /* テスト対象の実行 */
        final String testResult = testTarget.getKey();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "キーが一致しません");

    }

    /**
     * getValue メソッドのテスト - 正常系:不明な取得元の文面
     */
    @Test
    public void testGetValue_normalUnknownMinutesSource() {

        /* 期待値の定義 */
        final String expectedValue = "不明な残り時間の取得元です: %s";

        /* 準備 */
        final Gr002GenMsgTypes testTarget = Gr002GenMsgTypes.GR002_GEN11102;

        /* テスト対象の実行 */
        final String testResult = testTarget.getValue();

        /* 検証の準備 */
        final String actualValue = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedValue, actualValue, "メッセージの値が一致しません");

    }

    /**
     * toString メソッドのテスト - 正常系:呼び出し失敗のキー
     */
    @Test
    public void testToString_normalCallFailed() {

        /* 期待値の定義 */
        final String expectedKey = "GR002_GEN11103";

        /* 準備 */
        final Gr002GenMsgTypes testTarget = Gr002GenMsgTypes.GR002_GEN11103;

        /* テスト対象の実行 */
        final String testResult = testTarget.toString();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "文字列が一致しません");

    }

}
