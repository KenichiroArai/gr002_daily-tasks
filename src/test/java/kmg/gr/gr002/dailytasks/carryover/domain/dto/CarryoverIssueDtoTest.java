package kmg.gr.gr002.dailytasks.carryover.domain.dto;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverIssueModel;
import kmg.gr.gr002.dailytasks.carryover.application.model.CarryoverItemModel;
import kmg.gr.gr002.dailytasks.carryover.application.types.MinutesSourceTypes;
import kmg.gr.gr002.dailytasks.testutil.MessageLogicTestUtil;

/**
 * {@link CarryoverIssueDto} のテスト<br>
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
public class CarryoverIssueDtoTest {

    /**
     * 持ち越しの解析結果を作成する<br>
     *
     * @since 0.1.0
     *
     * @return 持ち越しの解析結果
     */
    private static CarryoverIssueModel createIssue() {

        final CarryoverItemModel  item   = new CarryoverItemModel("音楽", "2026-08-18", true, 11.5,
            MinutesSourceTypes.PARSED, "持ち越し", "- [x] 音楽2026/08/18（残り時間：11.5分）");
        final CarryoverIssueModel result = new CarryoverIssueModel(371, "2026年10月06日のタスク", "2026-10-06", "open",
            "2026-10-06T14:23:08Z", List.of("持ち越し"), 1, List.of(item));
        return result;

    }

    /**
     * 保存済みの解析結果を作成する<br>
     *
     * @since 0.1.0
     *
     * @param minutesSource
     *                      残り時間の取得元の値
     *
     * @return 保存済みの解析結果
     */
    private static CarryoverIssueDto createIssueDto(final String minutesSource) {

        final CarryoverItemDto  item   = new CarryoverItemDto("国語", "2026-06-18", false, 15.0, minutesSource, "持ち越し",
            "- [ ] 国語2026/06/18");
        final CarryoverIssueDto result = new CarryoverIssueDto(371, "2026年10月06日のタスク", "2026-10-06", "open",
            "2026-10-06T14:23:08Z", List.of("持ち越し"), 1, 1, 15.0, List.of(item));
        return result;

    }

    /**
     * テスト対象を作成する<br>
     *
     * @since 0.1.0
     *
     * @return テスト対象
     */
    private static CarryoverIssueDto createTarget() {

        final CarryoverItemDto  item   = new CarryoverItemDto("国語", "2026-06-18", false, 8.5, "parsed", "持ち越し",
            "- [ ] 国語2026/06/18（残り時間：8.5分）");
        final CarryoverIssueDto result = new CarryoverIssueDto(371, "2026年10月06日のタスク", "2026-10-06", "open",
            "2026-10-06T14:23:08Z", List.of("持ち越し"), 1, 1, 8.5, List.of(item));
        return result;

    }

    /**
     * コンストラクタのテスト - 正常系:件数を集計して引き継ぐ場合
     *
     * @since 0.1.0
     */
    @Test
    public void testConstructor_normalCount() {

        /* 期待値の定義 */
        final int expectedCount = 1;

        /* 準備 */
        final CarryoverIssueModel testIssue = CarryoverIssueDtoTest.createIssue();

        /* テスト対象の実行 */
        final CarryoverIssueDto testResult = new CarryoverIssueDto(testIssue);

        /* 検証の準備 */
        final int actualCount = testResult.getCount();

        /* 検証の実施 */
        Assertions.assertEquals(expectedCount, actualCount, "件数が一致しません");

    }

    /**
     * コンストラクタのテスト - 正常系:残り時間の取得元を保存するときの値に変換する場合
     *
     * @since 0.1.0
     */
    @Test
    public void testConstructor_normalMinutesSource() {

        /* 期待値の定義 */
        final String expectedMinutesSource = "parsed";

        /* 準備 */
        final CarryoverIssueModel testIssue = CarryoverIssueDtoTest.createIssue();

        /* テスト対象の実行 */
        final CarryoverIssueDto testResult = new CarryoverIssueDto(testIssue);

        /* 検証の準備 */
        final String actualMinutesSource = testResult.getItems().get(0).getMinutesSource();

        /* 検証の実施 */
        Assertions.assertEquals(expectedMinutesSource, actualMinutesSource, "残り時間の取得元が一致しません");

    }

    /**
     * getCount メソッドのテスト - 正常系:件数を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetCount_normalValue() {

        /* 期待値の定義 */
        final int expectedCount = 1;

        /* 準備 */
        final CarryoverIssueDto testTarget = CarryoverIssueDtoTest.createTarget();

        /* テスト対象の実行 */
        final int testResult = testTarget.getCount();

        /* 検証の準備 */
        final int actualCount = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedCount, actualCount, "件数が一致しません");

    }

    /**
     * getDate メソッドのテスト - 正常系:その日の日付を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDate_normalValue() {

        /* 期待値の定義 */
        final String expectedDate = "2026-10-06";

        /* 準備 */
        final CarryoverIssueDto testTarget = CarryoverIssueDtoTest.createTarget();

        /* テスト対象の実行 */
        final String testResult = testTarget.getDate();

        /* 検証の準備 */
        final String actualDate = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDate, actualDate, "その日の日付が一致しません");

    }

    /**
     * getDeclaredCount メソッドのテスト - 正常系:残数を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetDeclaredCount_normalValue() {

        /* 期待値の定義 */
        final Integer expectedDeclaredCount = 1;

        /* 準備 */
        final CarryoverIssueDto testTarget = CarryoverIssueDtoTest.createTarget();

        /* テスト対象の実行 */
        final Integer testResult = testTarget.getDeclaredCount();

        /* 検証の準備 */
        final Integer actualDeclaredCount = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedDeclaredCount, actualDeclaredCount, "残数が一致しません");

    }

    /**
     * getItems メソッドのテスト - 正常系:持ち越し項目を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetItems_normalValue() {

        /* 期待値の定義 */
        final List<String> expectedItems = List.of("国語");

        /* 準備 */
        final CarryoverIssueDto testTarget = CarryoverIssueDtoTest.createTarget();

        /* テスト対象の実行 */
        final List<CarryoverItemDto> testResult = testTarget.getItems();

        /* 検証の準備 */
        final List<String> actualItems = testResult.stream().map(CarryoverItemDto::getName).toList();

        /* 検証の実施 */
        Assertions.assertEquals(expectedItems, actualItems, "持ち越し項目が一致しません");

    }

    /**
     * getMinutes メソッドのテスト - 正常系:残り時間の合計を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetMinutes_normalValue() {

        /* 期待値の定義 */
        final double expectedMinutes = 8.5;

        /* 準備 */
        final CarryoverIssueDto testTarget = CarryoverIssueDtoTest.createTarget();

        /* テスト対象の実行 */
        final double testResult = testTarget.getMinutes();

        /* 検証の準備 */
        final double actualMinutes = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedMinutes, actualMinutes, "残り時間の合計が一致しません");

    }

    /**
     * getNumber メソッドのテスト - 正常系:Issue 番号を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetNumber_normalValue() {

        /* 期待値の定義 */
        final int expectedNumber = 371;

        /* 準備 */
        final CarryoverIssueDto testTarget = CarryoverIssueDtoTest.createTarget();

        /* テスト対象の実行 */
        final int testResult = testTarget.getNumber();

        /* 検証の準備 */
        final int actualNumber = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedNumber, actualNumber, "Issue 番号が一致しません");

    }

    /**
     * getSections メソッドのテスト - 正常系:対象セクション名を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetSections_normalValue() {

        /* 期待値の定義 */
        final List<String> expectedSections = List.of("持ち越し");

        /* 準備 */
        final CarryoverIssueDto testTarget = CarryoverIssueDtoTest.createTarget();

        /* テスト対象の実行 */
        final List<String> testResult = testTarget.getSections();

        /* 検証の準備 */
        final List<String> actualSections = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedSections, actualSections, "対象セクション名が一致しません");

    }

    /**
     * getState メソッドのテスト - 正常系:状態を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetState_normalValue() {

        /* 期待値の定義 */
        final String expectedState = "open";

        /* 準備 */
        final CarryoverIssueDto testTarget = CarryoverIssueDtoTest.createTarget();

        /* テスト対象の実行 */
        final String testResult = testTarget.getState();

        /* 検証の準備 */
        final String actualState = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedState, actualState, "状態が一致しません");

    }

    /**
     * getTitle メソッドのテスト - 正常系:タイトルを返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetTitle_normalValue() {

        /* 期待値の定義 */
        final String expectedTitle = "2026年10月06日のタスク";

        /* 準備 */
        final CarryoverIssueDto testTarget = CarryoverIssueDtoTest.createTarget();

        /* テスト対象の実行 */
        final String testResult = testTarget.getTitle();

        /* 検証の準備 */
        final String actualTitle = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedTitle, actualTitle, "タイトルが一致しません");

    }

    /**
     * getUpdatedAt メソッドのテスト - 正常系:更新日時を返す場合
     *
     * @since 0.1.0
     */
    @Test
    public void testGetUpdatedAt_normalValue() {

        /* 期待値の定義 */
        final String expectedUpdatedAt = "2026-10-06T14:23:08Z";

        /* 準備 */
        final CarryoverIssueDto testTarget = CarryoverIssueDtoTest.createTarget();

        /* テスト対象の実行 */
        final String testResult = testTarget.getUpdatedAt();

        /* 検証の準備 */
        final String actualUpdatedAt = testResult;

        /* 検証の実施 */
        Assertions.assertEquals(expectedUpdatedAt, actualUpdatedAt, "更新日時が一致しません");

    }

    /**
     * toCarryoverIssueModel メソッドのテスト - 正常系:Issue の項目を引き継ぐ場合
     *
     * @since 0.1.0
     */
    @Test
    public void testToCarryoverIssueModel_normalIssue() {

        /* 期待値の定義 */
        final String expectedUpdatedAt = "2026-10-06T14:23:08Z";

        /* 準備 */
        final CarryoverIssueDto testDto = CarryoverIssueDtoTest.createIssueDto("default");

        /* テスト対象の実行 */
        final CarryoverIssueModel testResult = testDto.toCarryoverIssueModel(MessageLogicTestUtil.create());

        /* 検証の準備 */
        final String actualUpdatedAt = testResult.getUpdatedAt();

        /* 検証の実施 */
        Assertions.assertEquals(expectedUpdatedAt, actualUpdatedAt, "更新日時が一致しません");

    }

    /**
     * toCarryoverIssueModel メソッドのテスト - 正常系:残り時間の取得元を値から変換する場合
     *
     * @since 0.1.0
     */
    @Test
    public void testToCarryoverIssueModel_normalMinutesSource() {

        /* 期待値の定義 */
        final MinutesSourceTypes expectedMinutesSource = MinutesSourceTypes.DEFAULT;

        /* 準備 */
        final CarryoverIssueDto testDto = CarryoverIssueDtoTest.createIssueDto("default");

        /* テスト対象の実行 */
        final CarryoverIssueModel testResult = testDto.toCarryoverIssueModel(MessageLogicTestUtil.create());

        /* 検証の準備 */
        final MinutesSourceTypes actualMinutesSource = testResult.getItems().get(0).getMinutesSource();

        /* 検証の実施 */
        Assertions.assertEquals(expectedMinutesSource, actualMinutesSource, "残り時間の取得元が一致しません");

    }

    /**
     * toCarryoverIssueModel メソッドのテスト - 準正常系:残り時間の取得元が不明な場合
     *
     * @since 0.1.0
     */
    @Test
    public void testToCarryoverIssueModel_semiUnknownMinutesSource() {

        /* 期待値の定義 */
        final String expectedMessage = "不明な残り時間の取得元です: xxx";

        /* 準備 */
        final CarryoverIssueDto testDto = CarryoverIssueDtoTest.createIssueDto("xxx");

        /* テスト対象の実行 */
        final IllegalArgumentException testException = Assertions.assertThrows(IllegalArgumentException.class,
            () -> testDto.toCarryoverIssueModel(MessageLogicTestUtil.create()));

        /* 検証の準備 */
        final String actualMessage = testException.getMessage();

        /* 検証の実施 */
        Assertions.assertEquals(expectedMessage, actualMessage, "例外のメッセージが一致しません");

    }

}
