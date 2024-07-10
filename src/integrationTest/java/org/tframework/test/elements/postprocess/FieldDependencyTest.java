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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.test.commons.annotations.SetProperties;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@SetProperties("test.prop=value")
@IsolatedTFrameworkTest
public class FieldDependencyTest {

    /*
    The test class itself will be an element, so it's fields are candidates for injection.
     */

    @InjectElement
    public Application application;

    @InjectProperty("test.prop")
    private String testProp;

    //not annotated, field injection ignores it
    public Integer number;

    @Test
    public void shouldInjectFieldDependencies(
            @InjectElement Application applicationFromParameter,
            @InjectProperty("test.prop") String testPropFromParameter
    ) {
        //application is a singleton element, so same object is injected here and in the field
        assertSame(application, applicationFromParameter);

        assertEquals(testPropFromParameter, testProp);

        assertNull(number);
    }
}
