/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.methods;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

@Slf4j
@ExtendWith(MockitoExtension.class)
public class SimpleMethodInvokerTest {

    @Spy
    private TestClass instance;

    private final SimpleMethodInvoker simpleMethodInvoker = new SimpleMethodInvoker();

    @Test
    public void shouldInvokeMethod() throws Exception {
        var method = TestClass.class.getDeclaredMethod("testMethod");
        simpleMethodInvoker.invokeMethodWithNoParametersAndIgnoreResult(instance, method);
        verify(instance, times(1)).testMethod();
    }

    @Test
    public void shouldThrowMethodInvocationException() throws Exception {
        var method = TestClass.class.getDeclaredMethod("testMethodWhichDies");
        var exception = assertThrows(MethodInvocationException.class, () ->
                simpleMethodInvoker.invokeMethodWithNoParametersAndIgnoreResult(instance, method));
        assertEquals(
                exception.getMessageTemplate().formatted(method.getName(), TestClass.class.getName()),
                exception.getMessage()
        );
    }

    static class TestClass {

        public void testMethod() {
            log.debug("Test method invoked");
        }

        public void testMethodWhichDies() {
            throw new RuntimeException("Dead");
        }

    }

}
