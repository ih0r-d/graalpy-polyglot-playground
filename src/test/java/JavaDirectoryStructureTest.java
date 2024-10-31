import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.JavaMethod;
import com.tngtech.archunit.core.domain.JavaModifier;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import com.tngtech.archunit.library.Architectures;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.github.ih0rd")
public class JavaDirectoryStructureTest {

    @ArchTest
    public void shouldHaveCorrectPackageStructure(JavaClasses importedClasses) {
        Architectures.layeredArchitecture()
                .consideringOnlyDependenciesInLayers()
                .layer("Contracts").definedBy("com.github.ih0rd.contracts..")
                .layer("Exceptions").definedBy("com.github.ih0rd.exceptions..")
                .layer("Helpers").definedBy("com.github.ih0rd.helpers..")
                .layer("Utils").definedBy("com.github.ih0rd.utils..")
                .whereLayer("Contracts").mayOnlyBeAccessedByLayers("Helpers", "Utils")
                .whereLayer("Exceptions").mayOnlyBeAccessedByLayers("Helpers", "Utils", "Contracts")
                .check(importedClasses);
    }

    @ArchTest
    public void onlyExceptionsInExceptionsPackage(JavaClasses importedClasses) {
        ArchRuleDefinition.classes()
                .that().resideInAPackage("..exceptions..")
                .should().beAssignableTo(Exception.class)
                .check(importedClasses);
    }

    @ArchTest
    public void utilityClassesShouldOnlyHaveStaticMethods(JavaClasses importedClasses) {
        ArchRuleDefinition.classes()
                .that().resideInAPackage("..utils..")
                .should(new ArchCondition<>("only have static methods") {
                    @Override
                    public void check(JavaClass javaClass, ConditionEvents events) {
                        for (JavaMethod method : javaClass.getMethods()) {
                            if (!method.getModifiers().contains(JavaModifier.STATIC)) {
                                String message = String.format("Method %s in class %s is not static", method.getName(), javaClass.getName());
                                events.add(SimpleConditionEvent.violated(javaClass, message));
                            }
                        }
                    }
                })
                .check(importedClasses);
    }

    @ArchTest
    public void helpersShouldNotDependOnContracts(JavaClasses importedClasses) {
        noClasses()
                .that().resideInAPackage("..helpers..")
                .should().dependOnClassesThat().resideInAPackage("..contracts..")
                .check(importedClasses);
    }


    @ArchTest
    public void contractsShouldBeInterfacesOrAbstract(JavaClasses importedClasses) {
        ArchRuleDefinition.classes()
                .that().resideInAPackage("..contracts..")
                .should().beInterfaces()
                .orShould().haveModifier(JavaModifier.ABSTRACT)
                .check(importedClasses);
    }

}