/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.ElementConstructor;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.SingletonElementContext;
import org.tframework.core.elements.context.assembler.ClassElementContextAssembler;
import org.tframework.core.elements.context.assembler.MethodElementContextAssembler;
import org.tframework.core.elements.context.source.ClassElementSource;
import org.tframework.core.elements.context.source.MethodElementSource;
import org.tframework.core.elements.scanner.ElementClassScanner;
import org.tframework.core.elements.scanner.ElementMethodScanner;
import org.tframework.core.elements.scanner.ElementScannersBundle;
import org.tframework.core.elements.scanner.ElementScanningResult;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainer;

@ExtendWith(MockitoExtension.class)
class DependencyInjectionProcessTest {

    @Mock
    private ClassElementContextAssembler classElementContextAssembler;

    @Mock
    private MethodElementContextAssembler methodElementContextAssembler;

    @Mock
    private ElementClassScanner elementClassScanner;

    @Mock
    private ElementMethodScanner elementMethodScanner;

    private DependencyInjectionProcess dependencyInjectionProcess;
    private ElementScannersBundle elementScannersBundle;

    @BeforeEach
    void setUp() {
        dependencyInjectionProcess = DependencyInjectionProcess.builder()
                .classElementContextAssembler(classElementContextAssembler)
                .methodElementContextAssembler(methodElementContextAssembler)
                .build();
        elementScannersBundle = ElementScannersBundle.builder()
                .elementClassScanners(List.of(elementClassScanner))
                .elementMethodScanners(List.of(elementMethodScanner))
                .build();
    }

    @Test
    public void shouldInitializeDependencyInjectionProcess() {
        //mock class element scanning and assembling
        var classScanningResult = new ElementScanningResult<Class<?>>(DummyClass.class.getAnnotation(Element.class), DummyClass.class);
        when(elementClassScanner.scanElements()).thenReturn(Set.of(classScanningResult));
        when(classElementContextAssembler.assemble(classScanningResult)).thenReturn(dummyClassElementContext);

        //mock method element scanning and assembling
        var methodScanningResult = new ElementScanningResult<>(dummyStringMethod.getAnnotation(Element.class), dummyStringMethod);
        when(elementMethodScanner.scanElements()).thenReturn(Set.of(methodScanningResult));
        when(methodElementContextAssembler.assemble(methodScanningResult)).thenReturn(dummyStringMethodElementContext);

        var input = createDummyDependencyInjectionInput();
        var elementsContainer = dependencyInjectionProcess.initialize(input, elementScannersBundle);

        assertEquals(ElementsContainer.fromElementContexts(
                List.of(dummyClassElementContext, dummyStringMethodElementContext)
        ), elementsContainer);
    }

    private DependencyInjectionInput createDummyDependencyInjectionInput() {
        return DependencyInjectionInput.builder()
                .rootClass(DependencyInjectionProcessTest.class)
                .profilesContainer(ProfilesContainer.empty())
                .propertiesContainer(PropertiesContainer.empty())
                .build();
    }

    /*
    This single element class and its method elements are used for testing the dependency injection process.
     */
    @Element
    static class DummyClass {

        @ElementConstructor
        public DummyClass() {}

        @Element
        public String dummyStringMethod() {
            return "dummy";
        }

    }

    private static final Method dummyStringMethod;
    private static final ElementContext dummyClassElementContext;
    private static final ElementContext dummyStringMethodElementContext;

    static {
        try {
            dummyClassElementContext = SingletonElementContext.from(
                    DummyClass.class.getAnnotation(Element.class),
                    DummyClass.class,
                    new ClassElementSource(DummyClass.class.getConstructor())
            );
            dummyStringMethod = DummyClass.class.getMethod("dummyStringMethod");
            dummyStringMethodElementContext = SingletonElementContext.from(
                    dummyStringMethod.getAnnotation(Element.class),
                    String.class,
                    new MethodElementSource(dummyStringMethod, dummyClassElementContext)
            );
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
