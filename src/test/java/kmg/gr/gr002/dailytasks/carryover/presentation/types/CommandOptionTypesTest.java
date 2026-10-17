package kmg.gr.gr002.dailytasks.carryover.presentation.types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * {@link CommandOptionTypes} のテスト<br>
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
public class CommandOptionTypesTest {

    /**
     * get メソッドのテスト - 正常系:全件モードの引数を返す場合
     */
    @Test
    public void testGet_normalFull() {

        /* 期待値の定義 */
        final String expectedKey = "Full";

        /* 準備 */
        final CommandOptionTypes testTarget = CommandOptionTypes.FULL;

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
        final CommandOptionTypes expectedType = CommandOptionTypes.NONE;

        /* テスト対象の実行 */
        final CommandOptionTypes testResult = CommandOptionTypes.getDefault();

        /* 検証の準備 */
        final CommandOptionTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "デフォルト値が一致しません");

    }

    /**
     * getDetail メソッドのテスト - 正常系:使い方の短縮形の詳細
     */
    @Test
    public void testGetDetail_normalHelpShort() {

        /* 期待値の定義 */
        final String expectedDetail = "使い方を表示する短縮形";

        /* 準備 */
        final CommandOptionTypes testTarget = CommandOptionTypes.HELP_SHORT;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDetail();

        /* 検証の準備 */
        final String actualDetail = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDetail, actualDetail, "詳細情報が一致しません");

    }

    /**
     * getDisplayName メソッドのテスト - 正常系:使い方の表示名
     */
    @Test
    public void testGetDisplayName_normalHelp() {

        /* 期待値の定義 */
        final String expectedDisplayName = "使い方の表示";

        /* 準備 */
        final CommandOptionTypes testTarget = CommandOptionTypes.HELP;

        /* テスト対象の実行 */
        final String testResult = testTarget.getDisplayName();

        /* 検証の準備 */
        final String actualDisplayName = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDisplayName, actualDisplayName, "表示名が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 正常系:引数から種類を返す場合
     */
    @Test
    public void testGetEnum_normalFull() {

        /* 期待値の定義 */
        final CommandOptionTypes expectedType = CommandOptionTypes.FULL;

        /* 準備 */
        final String testKey = "--full";

        /* テスト対象の実行 */
        final CommandOptionTypes testResult = CommandOptionTypes.getEnum(testKey);

        /* 検証の準備 */
        final CommandOptionTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "引数の種類が一致しません");

    }

    /**
     * getEnum メソッドのテスト - 準正常系:不明な引数の場合は指定無しを返す
     */
    @Test
    public void testGetEnum_semiUnknownKey() {

        /* 期待値の定義 */
        final CommandOptionTypes expectedType = CommandOptionTypes.NONE;

        /* 準備 */
        final String testKey = "--unknown";

        /* テスト対象の実行 */
        final CommandOptionTypes testResult = CommandOptionTypes.getEnum(testKey);

        /* 検証の準備 */
        final CommandOptionTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "不明な引数の場合は指定無しが返る必要があります");

    }

    /**
     * getInitValue メソッドのテスト - 正常系:指定無しを返す場合
     */
    @Test
    public void testGetInitValue_normalNone() {

        /* 期待値の定義 */
        final CommandOptionTypes expectedType = CommandOptionTypes.NONE;

        /* テスト対象の実行 */
        final CommandOptionTypes testResult = CommandOptionTypes.getInitValue();

        /* 検証の準備 */
        final CommandOptionTypes actualType = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedType, actualType, "初期値が一致しません");

    }

    /**
     * getKey メソッドのテスト - 正常系:使い方の引数
     */
    @Test
    public void testGetKey_normalHelp() {

        /* 期待値の定義 */
        final String expectedKey = "Help";

        /* 準備 */
        final CommandOptionTypes testTarget = CommandOptionTypes.HELP;

        /* テスト対象の実行 */
        final String testResult = testTarget.getKey();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "キーが一致しません");

    }

    /**
     * toString メソッドのテスト - 正常系:短縮形の引数
     */
    @Test
    public void testToString_normalHelpShort() {

        /* 期待値の定義 */
        final String expectedKey = "HelpShort";

        /* 準備 */
        final CommandOptionTypes testTarget = CommandOptionTypes.HELP_SHORT;

        /* テスト対象の実行 */
        final String testResult = testTarget.toString();

        /* 検証の準備 */
        final String actualKey = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedKey, actualKey, "文字列が一致しません");

    }

}
