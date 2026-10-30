package kmg.gr.gr002.dailytasks.carryover.infrastructure.types.msg;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link Gr002LogMsgTypes} のテスト<br>
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
public class Gr002LogMsgTypesTest {

    /**
     * get メソッドのテスト - 正常系:収集結果のキーを返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGet_normalCollectResult() {

        /* 期待値の定義 */
        final String expectedKey = "Gr002Log12000";

        /* 準備 */
        final Gr002LogMsgTypes testTarget = Gr002LogMsgTypes.GR002_LOG12000;

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
        final Gr002LogMsgTypes testTarget = Gr002LogMsgTypes.NONE;

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
        final Gr002LogMsgTypes expectedType = Gr002LogMsgTypes.NONE;

        /* テスト対象の実行 */
        final Gr002LogMsgTypes testResult = Gr002LogMsgTypes.getDefault();

        /* 検証の準備 */
        final Gr002LogMsgTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "デフォルト値が一致しません");

    }

    /**
     * getDetail メソッドのテスト - 正常系:差分の文面
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDetail_normalModeDiff() {

        /* 期待値の定義 */
        final String expectedDetail = "差分モードの名前";

        /* 準備 */
        final Gr002LogMsgTypes testTarget = Gr002LogMsgTypes.GR002_LOG12012;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDetail();

        /* 検証の準備 */
        final String actualDetail = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDetail, actualDetail, "詳細情報が一致しません");

    }

    /**
     * getDisplayName メソッドのテスト - 正常系:全件の文面
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDisplayName_normalModeFull() {

        /* 期待値の定義 */
        final String expectedDisplayName = "全件";

        /* 準備 */
        final Gr002LogMsgTypes testTarget = Gr002LogMsgTypes.GR002_LOG12011;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDisplayName();

        /* 検証の準備 */
        final String actualDisplayName = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDisplayName, actualDisplayName, "表示名が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 正常系:定数名から種類を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetEnum_normalFetched() {

        /* 期待値の定義 */
        final Gr002LogMsgTypes expectedType = Gr002LogMsgTypes.GR002_LOG12010;

        /* 準備 */
        final String testKey = "Gr002Log12010";

        /* テスト対象の実行 */
        final Gr002LogMsgTypes testResult = Gr002LogMsgTypes.getEnum(testKey);

        /* 検証の準備 */
        final Gr002LogMsgTypes actualType = testResult;

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
        final Gr002LogMsgTypes expectedType = Gr002LogMsgTypes.NONE;

        /* 準備 */
        final String testKey = "GR002_LOG99999";

        /* テスト対象の実行 */
        final Gr002LogMsgTypes testResult = Gr002LogMsgTypes.getEnum(testKey);

        /* 検証の準備 */
        final Gr002LogMsgTypes actualType = testResult;

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
        final Gr002LogMsgTypes expectedType = Gr002LogMsgTypes.NONE;

        /* テスト対象の実行 */
        final Gr002LogMsgTypes testResult = Gr002LogMsgTypes.getInitValue();

        /* 検証の準備 */
        final Gr002LogMsgTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "初期値が一致しません");

    }

    /**
     * getKey メソッドのテスト - 正常系:設定ファイルがない場合のキー
     *
     * @since 0.1.0
     */
    @Test
    public void testGetKey_normalFileNotFound() {

        /* 期待値の定義 */
        final String expectedKey = "Gr002Log12009";

        /* 準備 */
        final Gr002LogMsgTypes testTarget = Gr002LogMsgTypes.GR002_LOG12009;

        /* テスト対象の実行 */
        final String testResult = testTarget.getKey();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "キーが一致しません");

    }

    /**
     * getValue メソッドのテスト - 正常系:補完結果の文面
     *
     * @since 0.1.0
     */
    @Test
    public void testGetValue_normalMinutesSourceResult() {

        /* 期待値の定義 */
        final String expectedValue = "標準時間で補完した行={} 件、標準時間が未登録の行={} 件";

        /* 準備 */
        final Gr002LogMsgTypes testTarget = Gr002LogMsgTypes.GR002_LOG12001;

        /* テスト対象の実行 */
        final String testResult = testTarget.getValue();

        /* 検証の準備 */
        final String actualValue = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedValue, actualValue, "メッセージの値が一致しません");

    }

    /**
     * getValue メソッドのテスト - 正常系:収集開始の文面
     *
     * @since 0.1.0
     */
    @Test
    public void testGetValue_normalCollectStart() {

        /* 期待値の定義 */
        final String expectedValue
            = "収集を開始します: モード={}、repository={}、dataDir={}、recentCount={}、batchSize={}";

        /* 準備 */
        final Gr002LogMsgTypes testTarget = Gr002LogMsgTypes.GR002_LOG12013;

        /* テスト対象の実行 */
        final String testResult = testTarget.getValue();

        /* 検証の準備 */
        final String actualValue = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedValue, actualValue, "メッセージの値が一致しません");

    }

    /**
     * getKey メソッドのテスト - 正常系:summary.json の保存のキー
     *
     * @since 0.1.0
     */
    @Test
    public void testGetKey_normalSummarySave() {

        /* 期待値の定義 */
        final String expectedKey = "Gr002Log12034";

        /* 準備 */
        final Gr002LogMsgTypes testTarget = Gr002LogMsgTypes.GR002_LOG12034;

        /* テスト対象の実行 */
        final String testResult = testTarget.getKey();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "キーが一致しません");

    }

    /**
     * toString メソッドのテスト - 正常系:想定外の行のキー
     *
     * @since 0.1.0
     */
    @Test
    public void testToString_normalUnexpectedLine() {

        /* 期待値の定義 */
        final String expectedKey = "Gr002Log12007";

        /* 準備 */
        final Gr002LogMsgTypes testTarget = Gr002LogMsgTypes.GR002_LOG12007;

        /* テスト対象の実行 */
        final String testResult = testTarget.toString();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "文字列が一致しません");

    }

}
