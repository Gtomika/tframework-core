/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di;

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
import org.tframework.core.di.annotations.Element;
import org.tframework.core.di.annotations.ElementConstructor;
import org.tframework.core.di.context.ElementContext;
import org.tframework.core.di.context.SingletonElementContext;
import org.tframework.core.di.context.assembler.ClassElementContextAssembler;
import org.tframework.core.di.context.assembler.MethodElementContextAssembler;
import org.tframework.core.di.context.source.ClassElementSource;
import org.tframework.core.di.context.source.MethodElementSource;
import org.tframework.core.di.scanner.ElementClassScanner;
import org.tframework.core.di.scanner.ElementMethodScanner;
import org.tframework.core.di.scanner.ElementScannersBundle;
import org.tframework.core.di.scanner.ElementScanningResult;

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

        var elementsContainer = dependencyInjectionProcess.initialize(elementScannersBundle);

        assertEquals(ElementsContainer.fromElementContexts(
                List.of(dummyClassElementContext, dummyStringMethodElementContext)
        ), elementsContainer);
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