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
package org.tframework.test.properties.group;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.properties.group.Property;
import org.tframework.core.properties.group.PropertyGroup;
import org.tframework.test.commons.annotations.SetProperties;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@SetProperties({
        "my.props.a=1",
        "my.props.b=bValue",
})
@IsolatedTFrameworkTest
public class PropertyGroupTest {

    @Element
    @PropertyGroup(name = "my.props")
    public static class MyProps {
        private int a; //will be mapped by field name
        @Property("b") private String bProperty; //will be mapped by annotation value
    }

    @Test
    public void shouldCreateAndFillPropertyValueElement(@InjectElement MyProps myProps) {
        assertEquals(1, myProps.a);
        assertEquals("bValue", myProps.bProperty);
    }

}
