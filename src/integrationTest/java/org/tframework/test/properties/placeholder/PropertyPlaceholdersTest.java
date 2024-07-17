package org.tframework.test.properties.placeholder;

import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.InjectProperty;
import org.tframework.test.commons.annotations.SetProperties;
import org.tframework.test.junit5.IsolatedTFrameworkTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
