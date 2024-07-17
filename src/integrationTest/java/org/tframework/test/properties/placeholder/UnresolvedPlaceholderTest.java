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

import static org.tframework.test.commons.utils.TframeworkAssertions.assertInitializationExceptionWithCause;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.core.properties.PropertyNotFoundException;
import org.tframework.test.commons.annotations.ExpectInitializationFailure;
import org.tframework.test.commons.annotations.InjectInitializationException;
import org.tframework.test.commons.annotations.SetProperties;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

@SetProperties({
        // 'addition-result' is not defined and no default value is provided
        "addition-statement=1+1 is prop{addition-result}",
})
@IsolatedTFrameworkTest
@ExpectInitializationFailure
public class UnresolvedPlaceholderTest {

    @InjectProperty("addition-statement")
    private String additionStatement;

    @Test
    public void shouldFailInitialization(@InjectInitializationException Exception e) {
        assertInitializationExceptionWithCause(e, PropertyNotFoundException.class);
    }

}
