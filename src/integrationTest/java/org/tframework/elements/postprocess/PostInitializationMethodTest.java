/* Licensed under Apache-2.0 2024. */
package org.tframework.elements.postprocess;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.tframework.core.Application;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.postprocessing.annotations.PostInitialization;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@Slf4j
@IsolatedTFrameworkTest
public class PostInitializationMethodTest {

    private final DummyClass mock;

    public PostInitializationMethodTest() {
        //field is not injected yet
        assertNull(application);
        mock = Mockito.mock(DummyClass.class);
    }

    @InjectElement
    private Application application;

    @PostInitialization
    public void postInit1() {
        //field must be injected at this point
        assertNotNull(application);
        mock.dummyMethod();
    }

    @PostInitialization
    public void postInit2() {
        assertNotNull(application);
        mock.dummyMethod();
    }

    @Test
    public void shouldExecutePostInitializationMethod() {
        verify(mock, times(2)).dummyMethod();
    }

    static class DummyClass {
        public void dummyMethod() {}
    }

}
