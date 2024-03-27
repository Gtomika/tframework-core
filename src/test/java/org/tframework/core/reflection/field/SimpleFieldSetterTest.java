/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class SimpleFieldSetterTest {

    private final SimpleFieldSetter fieldSetter = new SimpleFieldSetter();

    static class TestClass {

        public String string1;

        private String string2;

    }

    @Test
    public void shouldSetAccessibleField() throws Exception {
        var object = new TestClass();
        var accessibleField = TestClass.class.getDeclaredField("string1");

        fieldSetter.setFieldValue(object, accessibleField, "value1");

        assertEquals("value1", object.string1);
    }

    @Test
    public void shouldSetNotAccessibleField() throws Exception {
        var object = new TestClass();
        var notAccessibleField = TestClass.class.getDeclaredField("string2");

        fieldSetter.setFieldValue(object, notAccessibleField, "value2");

        assertEquals("value2", object.string2);
    }

    @Test
    public void shouldThrowFieldSettingException_whenEncountersException() throws Exception {
        var object = new TestClass();
        var field = TestClass.class.getDeclaredField("string1");

        var exception = assertThrows(FieldSettingException.class, () -> fieldSetter.setFieldValue(object, field, 1));

        assertEquals(
                exception.getMessageTemplate().formatted(field.getName(), TestClass.class.getName()),
                exception.getMessage()
        );
        assertInstanceOf(IllegalArgumentException.class, exception.getCause());
    }

}
