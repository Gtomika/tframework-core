/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.assembler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.File;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.ElementContextTestUtils;
import org.tframework.core.elements.context.source.MethodElementSource;
import org.tframework.core.elements.scanner.ElementScanningResult;
import org.tframework.core.reflection.methods.MethodFilter;

@Element
@ExtendWith(MockitoExtension.class)
class MethodElementContextAssemblerTest {

    @Mock
    private ElementContext parentElementContext;

    @Mock
    private MethodFilter methodFilter;

    private MethodElementContextAssembler assembler;

    @BeforeEach
    void setUp() {
        assembler = MethodElementContextAssembler.create(methodFilter);
        assembler.setParentElementContext(parentElementContext);
        doReturn(MethodElementContextAssemblerTest.class).when(parentElementContext).getType();
    }

    @Test
    public void shouldThrowException_whenMethodIsNotPublic() throws Exception {
        Method privateMethod = MethodElementContextAssemblerTest.class.getDeclaredMethod("privateMethod");
        when(methodFilter.isPublic(privateMethod)).thenReturn(false);
        var scanningResult = asScanningResult(privateMethod);

        var exception = assertThrows(ElementContextAssemblingException.class, () -> assembler.assemble(scanningResult));

        String expectedMessage = exception.getMessageTemplate().formatted(
                int.class,
                MethodElementContextAssembler.DECLARED_AS,
                MethodElementContextAssemblerTest.class.getName(),
                MethodElementContextAssembler.NOT_PUBLIC_ERROR
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowException_whenMethodIsStatic() throws Exception {
        Method staticMethod = MethodElementContextAssemblerTest.class.getDeclaredMethod("staticMethod");
        when(methodFilter.isPublic(staticMethod)).thenReturn(true);
        when(methodFilter.isStatic(staticMethod)).thenReturn(true);
        var scanningResult = asScanningResult(staticMethod);

        var exception = assertThrows(ElementContextAssemblingException.class, () -> assembler.assemble(scanningResult));

        String expectedMessage = exception.getMessageTemplate().formatted(
                int.class,
                MethodElementContextAssembler.DECLARED_AS,
                MethodElementContextAssemblerTest.class.getName(),
                MethodElementContextAssembler.STATIC_ERROR
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldThrowException_whenMethodReturnsVoid() throws Exception {
        Method voidMethod = MethodElementContextAssemblerTest.class.getDeclaredMethod("voidMethod");
        when(methodFilter.isPublic(voidMethod)).thenReturn(true);
        when(methodFilter.isStatic(voidMethod)).thenReturn(false);
        when(methodFilter.hasVoidReturnType(voidMethod)).thenReturn(true);
        var scanningResult = asScanningResult(voidMethod);

        var exception = assertThrows(ElementContextAssemblingException.class, () -> assembler.assemble(scanningResult));

        String expectedMessage = exception.getMessageTemplate().formatted(
                int.class,
                MethodElementContextAssembler.DECLARED_AS,
                MethodElementContextAssemblerTest.class.getName(),
                MethodElementContextAssembler.NO_RETURN_TYPE_ERROR
        );
        assertEquals(expectedMessage, exception.getMessage());
    }

    @Test
    public void shouldAssembleElementContext_whenMethodIsValid() throws Exception {
        Method elementMethod = MethodElementContextAssemblerTest.class.getDeclaredMethod("validMethod", String.class, File.class);
        when(methodFilter.isPublic(elementMethod)).thenReturn(true);
        when(methodFilter.isStatic(elementMethod)).thenReturn(false);
        when(methodFilter.hasVoidReturnType(elementMethod)).thenReturn(false);
        var scanningResult = asScanningResult(elementMethod);

        var elementContext = assembler.assemble(scanningResult);

        assertEquals(elementMethod.getReturnType(), elementContext.getType());
        if (elementContext.getSource() instanceof MethodElementSource source) {
            assertEquals(parentElementContext, source.parentElementContext());
            assertEquals(elementMethod, source.method());
        } else {
            fail("Element context source is not a method element source.");
        }
    }

    private ElementScanningResult<Method> asScanningResult(Method method) {
        return new ElementScanningResult<>(method.getAnnotation(Element.class), method);
    }

    @Element
    private int privateMethod() {
        return 0;
    }

    @Element
    public static int staticMethod() {
        return 0;
    }

    @Element
    public int voidMethod() {
        return 0;
    }

    @Element
    public String validMethod(String s, File f) {
        return "";
    }

}
