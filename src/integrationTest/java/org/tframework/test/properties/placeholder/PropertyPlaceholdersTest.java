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
package org.tframework.test.properties.placeholder;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.test.commons.annotations.SetProperties;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

/*
It's not really possible to test environment variables...
 */
@SetProperties({
        "addition-result=2",
        "addition-statement=1+1 is prop{addition-result}",
        "multiplication-statement=2*2 is prop{multiplication-result|4}",
})
@IsolatedTFrameworkTest
public class PropertyPlaceholdersTest {

    @InjectProperty("addition-statement")
    private String additionStatement;

    @InjectProperty("multiplication-statement")
    private String multiplicationStatement;

    @Test
    public void shouldResolvePlaceholders() {
        assertEquals("1+1 is 2", additionStatement);
        assertEquals("2*2 is 4", multiplicationStatement);
    }

}
