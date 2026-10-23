package kmg.gr.gr002.dailytasks.carryover.repository.dto.json;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kmg.gr.gr002.dailytasks.carryover.domain.dto.ItemStatDto;
import kmg.gr.gr002.dailytasks.carryover.domain.dto.ItemStatsDto;

/**
 * {@link ItemStatsJsonDto} のテスト<br>
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
public class ItemStatsJsonDtoTest {

    /**
     * コンストラクタのテスト - 正常系:キーの並び順を引き継ぐ場合
     *
     * @since 0.1.0
     */
    @Test
    public void testConstructor_normalOrder() {

        /* 期待値の定義 */
        final List<String> expectedKeys = List.of("音楽", "国語");

        /* 準備 */
        final ItemStatsDto testStats = new ItemStatsDto();
        testStats.add("音楽", new ItemStatDto(1, 11.5, 1, 11.5));
        testStats.add("国語", new ItemStatDto(1, 15.0, 0, 0.0));

        /* テスト対象の実行 */
        final ItemStatsJsonDto testResult = new ItemStatsJsonDto(testStats);

        /* 検証の準備 */
        final List<String> actualKeys = testResult.names();

        /* 検証の実施 */
        Assertions.assertEquals(expectedKeys, actualKeys, "項目ごとの集計の並び順が一致しません");

    }

}
