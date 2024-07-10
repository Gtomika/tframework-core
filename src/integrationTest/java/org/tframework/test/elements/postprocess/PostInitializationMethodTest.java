/*
Copyright 2024 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.test.elements.postprocess;

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
