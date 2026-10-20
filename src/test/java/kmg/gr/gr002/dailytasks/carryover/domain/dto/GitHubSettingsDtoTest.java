package kmg.gr.gr002.dailytasks.carryover.domain.dto;

import java.nio.file.Path;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverSourceModel;

/**
 * {@link GitHubSettingsDto} のテスト<br>
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
public class GitHubSettingsDtoTest {

    /**
     * テスト対象を作成する<br>
     *
     * @since 0.1.0
     *
     * @return テスト対象
     */
    private static GitHubSettingsDto createTarget() {

        final GitHubSettingsDto result = new GitHubSettingsDto("owner/repo", "test-token");
        return result;

    }

    /**
     * コンストラクタのテスト - 正常系:リポジトリ名を引き継ぐ場合
     *
     * @since 0.1.0
     */
    @Test
    public void testConstructor_normalRepository() {

        /* 期待値の定義 */
        final String expectedRepository = "owner/repo";

        /* 準備 */
        final CarryoverSourceModel testSource = new CarryoverSourceModel("owner/repo", "test-token",
            Path.of("docs", "data"), Path.of("config", "default-minutes.json"));

        /* テスト対象の実行 */
        final GitHubSettingsDto testResult = new GitHubSettingsDto(testSource);

        /* 検証の準備 */
        final String actualRepository = testResult.getRepository();

        /* 検証の実施 */
        Assertions.assertEquals(expectedRepository, actualRepository, "リポジトリ名が一致しません");

    }

    /**
     * コンストラクタのテスト - 正常系:トークンを引き継ぐ場合
     *
     * @since 0.1.0
     */
    @Test
    public void testConstructor_normalToken() {

        /* 期待値の定義 */
        final String expectedToken = "test-token";

        /* 準備 */
        final CarryoverSourceModel testSource = new CarryoverSourceModel("owner/repo", "test-token",
            Path.of("docs", "data"), Path.of("config", "default-minutes.json"));

        /* テスト対象の実行 */
        final GitHubSettingsDto testResult = new GitHubSettingsDto(testSource);

        /* 検証の準備 */
        final String actualToken = testResult.getToken();

        /* 検証の実施 */
        Assertions.assertEquals(expectedToken, actualToken, "トークンが一致しません");

    }

    /**
     * getRepository メソッドのテスト - 正常系:対象リポジトリを返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetRepository_normalValue() {

        /* 期待値の定義 */
        final String expectedRepository = "owner/repo";

        /* 準備 */
        final GitHubSettingsDto testTarget = GitHubSettingsDtoTest.createTarget();

        /* テスト対象の実行 */
        final String testResult = testTarget.getRepository();

        /* 検証の準備 */
        final String actualRepository = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedRepository, actualRepository, "対象リポジトリが一致しません");

    }

    /**
     * getToken メソッドのテスト - 正常系:トークンを返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetToken_normalValue() {

        /* 期待値の定義 */
        final String expectedToken = "test-token";

        /* 準備 */
        final GitHubSettingsDto testTarget = GitHubSettingsDtoTest.createTarget();

        /* テスト対象の実行 */
        final String testResult = testTarget.getToken();

        /* 検証の準備 */
        final String actualToken = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedToken, actualToken, "トークンが一致しません");

    }

    /**
     * toString メソッドのテスト - 正常系:トークンを含めない場合
     *
     * @since 0.1.0
     */
    @Test
    public void testToString_normalWithoutToken() {

        /* 期待値の定義 */
        final String expectedString = "GitHubSettingsDto[repository=owner/repo]";

        /* 準備 */
        final GitHubSettingsDto testTarget = GitHubSettingsDtoTest.createTarget();

        /* テスト対象の実行 */
        final String testResult = testTarget.toString();

        /* 検証の準備 */
        final String  actualString   = testResult;
        final boolean actualHasToken = actualString.contains("test-token");

        /* 検証の実施 */
        Assertions.assertEquals(expectedString, actualString, "文字列表現が一致しません");
        Assertions.assertFalse(actualHasToken, "トークンを含めない必要があります");

    }

}
