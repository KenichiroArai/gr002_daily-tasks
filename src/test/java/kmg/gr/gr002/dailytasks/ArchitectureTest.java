package kmg.gr.gr002.dailytasks;

import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaConstructor;
import com.tngtech.archunit.core.domain.JavaField;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;

import kmg.core.infrastructure.type.KmgString;

/**
 * 層間のルールのテスト<br>
 * <p>
 * 本番コード（テストを除く）を対象に、AGENTS.md の「層間のルール」を検査する。
 * </p>
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
public class ArchitectureTest {

    /**
     * 検査対象のクラス
     *
     * @since 0.1.0
     */
    private static JavaClasses classes;

    /**
     * ルートパッケージ
     *
     * @since 0.1.0
     */
    private static final String ROOT_PACKAGE = "kmg.gr.gr002.dailytasks";

    /**
     * コンストラクタが1つのときは {@link Autowired} を付けず、2つ以上あるときは使うコンストラクタにだけ付けること
     *
     * @since 0.1.0
     */
    private static final ArchCondition<JavaClass> CONSTRUCTOR_AUTOWIRED_MATCHES_COUNT
        = new ArchCondition<>("omit Autowired on a single constructor and require one when there are multiple") {

            @Override
            public void check(final JavaClass item, final ConditionEvents events) {

                final Set<JavaConstructor> constructors     = item.getConstructors();
                final int                  constructorCount = constructors.size();
                int autowiredCount = 0;

                for (final JavaConstructor constructor : constructors) {

                    /* Autowired が付いているか */
                    final boolean autowired = constructor.isAnnotatedWith(Autowired.class);

                    if (autowired) {

                        // Autowired が付いている場合

                        // 付いているコンストラクタを数える
                        autowiredCount++;

                    }

                }

                /* コンストラクタが1つで Autowired が付いているか */
                if (constructorCount <= 1 && autowiredCount > 0) {

                    // コンストラクタが1つで Autowired が付いている場合

                    // 違反として記録する
                    events.add(SimpleConditionEvent.violated(item, KmgString.concat(item.getFullName(),
                        " has a single constructor annotated with Autowired")));

                }

                /* コンストラクタが複数で Autowired が1つではないか */
                if (constructorCount >= 2 && autowiredCount != 1) {

                    // コンストラクタが複数で Autowired が1つではない場合

                    // 違反として記録する
                    events.add(SimpleConditionEvent.violated(item, KmgString.concat(item.getFullName(),
                        " must annotate exactly one constructor with Autowired")));

                }

            }

        };

    /**
     * フィールドに {@link Autowired} が無いこと
     *
     * @since 0.1.0
     */
    private static final ArchCondition<JavaClass> NO_AUTOWIRED_FIELDS
        = new ArchCondition<>("not annotate fields with Autowired") {

            @Override
            public void check(final JavaClass item, final ConditionEvents events) {

                for (final JavaField field : item.getFields()) {

                    /* フィールドに Autowired が付いているか */
                    final boolean autowired = field.isAnnotatedWith(Autowired.class);

                    if (!autowired) {

                        // フィールドに Autowired が付いていない場合

                        // 次のフィールドへ進む
                        continue;

                    }

                    events.add(SimpleConditionEvent.violated(field,
                        KmgString.concat(field.getFullName(), " is annotated with Autowired")));

                }

            }

        };

    /**
     * 検査対象のクラスを読み込む<br>
     *
     * @since 0.1.0
     */
    @BeforeAll
    public static void importClasses() {

        ArchitectureTest.classes
            = new ClassFileImporter().withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages(ArchitectureTest.ROOT_PACKAGE);

    }

    /**
     * DI のテスト - 正常系:コンストラクタが1つのときは {@link Autowired} を付けず、2つ以上あるときは使うコンストラクタにだけ付け、フィールドには付けない場合
     *
     * @since 0.1.0
     */
    @Test
    public void testInjection_normalConstructorInjection() {

        /* 期待値の定義 */

        /* 準備 */
        final ArchRule noAutowiredFields = ArchRuleDefinition.classes().that().areAnnotatedWith(Component.class).or()
            .areAnnotatedWith(Service.class).or().areAnnotatedWith(Repository.class)
            .should(ArchitectureTest.NO_AUTOWIRED_FIELDS);
        final ArchRule constructorAutowiredMatchesCount = ArchRuleDefinition.classes().that()
            .areAnnotatedWith(Component.class).or().areAnnotatedWith(Service.class).or()
            .areAnnotatedWith(Repository.class).should(ArchitectureTest.CONSTRUCTOR_AUTOWIRED_MATCHES_COUNT);

        /* テスト対象の実行 */
        noAutowiredFields.check(ArchitectureTest.classes);
        constructorAutowiredMatchesCount.check(ArchitectureTest.classes);

        /* 検証の準備 */

        /* 検証の実施 */
        // 違反があれば check が AssertionError を投げる

    }

    /**
     * JSON の形のテスト - 正常系:Jackson のアノテーションを repository 層の dto.json パッケージだけで使っている場合
     *
     * @since 0.1.0
     */
    @Test
    public void testJsonAnnotation_normalOnlyRepositoryJson() {

        /* 期待値の定義 */

        /* 準備 */
        final ArchRule testRule
            = ArchRuleDefinition.noClasses().that().resideOutsideOfPackage("..carryover.repository.dto.json..").should()
                .dependOnClassesThat().resideInAPackage("com.fasterxml.jackson.annotation..");

        /* テスト対象の実行 */
        testRule.check(ArchitectureTest.classes);

        /* 検証の準備 */

        /* 検証の実施 */
        // 違反があれば check が AssertionError を投げる

    }

    /**
     * 層間の参照のテスト - 正常系:すぐ上の層だけを参照し（依存性逆転）、飛び越し・逆向きの参照がない場合
     *
     * @since 0.1.0
     */
    @Test
    public void testLayerDependencies_normalOnlyNextLayer() {

        /* 期待値の定義 */

        /* 準備 */
        // 処理の流れは presentation → application → domain → repository、依存はその逆とする
        final ArchRule testRule = Architectures.layeredArchitecture().consideringOnlyDependenciesInLayers()
            .layer("EntryPoint").definedBy(ArchitectureTest.ROOT_PACKAGE).layer("Presentation")
            .definedBy("..carryover.presentation..").layer("Application").definedBy("..carryover.application..")
            .layer("Domain").definedBy("..carryover.domain..").layer("Repository").definedBy("..carryover.repository..")
            .optionalLayer("Infrastructure").definedBy("..carryover.infrastructure..").whereLayer("EntryPoint")
            .mayNotBeAccessedByAnyLayer().whereLayer("Presentation").mayOnlyBeAccessedByLayers("Application")
            .whereLayer("Application").mayOnlyBeAccessedByLayers("Domain").whereLayer("Domain")
            .mayOnlyBeAccessedByLayers("Repository").whereLayer("Repository").mayNotBeAccessedByAnyLayer()
            .whereLayer("Infrastructure").mayNotAccessAnyLayer();

        /* テスト対象の実行 */
        testRule.check(ArchitectureTest.classes);

        /* 検証の準備 */

        /* 検証の実施 */
        // 違反があれば check が AssertionError を投げる

    }

    /**
     * ロジックの参照のテスト - 正常系:ロジックがサービスを参照していない場合
     *
     * @since 0.1.0
     */
    @Test
    public void testLogic_normalNoServiceDependency() {

        /* 期待値の定義 */

        /* 準備 */
        final ArchRule testRule = ArchRuleDefinition.noClasses().that().resideInAPackage("..carryover..logic..")
            .should().dependOnClassesThat().resideInAPackage("..carryover..service..");

        /* テスト対象の実行 */
        testRule.check(ArchitectureTest.classes);

        /* 検証の準備 */

        /* 検証の実施 */
        // 違反があれば check が AssertionError を投げる

    }

    /**
     * 設定値のテスト - 正常系:@Value で設定値を個別に取得していない場合
     *
     * @since 0.1.0
     */
    @Test
    public void testSettings_normalNoValueAnnotation() {

        /* 期待値の定義 */

        /* 準備 */
        final ArchRule testRule
            = ArchRuleDefinition.noClasses().should().dependOnClassesThat().areAssignableTo(Value.class);

        /* テスト対象の実行 */
        testRule.check(ArchitectureTest.classes);

        /* 検証の準備 */

        /* 検証の実施 */
        // 違反があれば check が AssertionError を投げる

    }

    /**
     * 設定値のテスト - 正常系:設定ファイルを受け取るクラスが presentation 層だけにある場合
     *
     * @since 0.1.0
     */
    @Test
    public void testSettings_normalPropertiesOnlyPresentation() {

        /* 期待値の定義 */

        /* 準備 */
        final ArchRule testRule = ArchRuleDefinition.classes().that().areAnnotatedWith(ConfigurationProperties.class)
            .should().resideInAPackage("..carryover.presentation..");

        /* テスト対象の実行 */
        testRule.check(ArchitectureTest.classes);

        /* 検証の準備 */

        /* 検証の実施 */
        // 違反があれば check が AssertionError を投げる

    }

}
