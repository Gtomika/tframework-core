/* Licensed under Apache-2.0 2024. */
package org.tframework.elements.dependency;

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
