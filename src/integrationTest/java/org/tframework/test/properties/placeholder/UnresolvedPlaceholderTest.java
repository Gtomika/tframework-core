package org.tframework.test.properties.placeholder;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.core.properties.PropertyNotFoundException;
import org.tframework.test.commons.annotations.ExpectInitializationFailure;
import org.tframework.test.commons.annotations.InjectInitializationException;
import org.tframework.test.commons.annotations.SetProperties;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

import static org.tframework.test.commons.utils.TframeworkAssertions.assertInitializationExceptionWithCause;

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
