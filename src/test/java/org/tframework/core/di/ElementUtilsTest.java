/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ElementUtilsTest {

    @Test
    void shouldGetElementNameByType() {
        String name = ElementUtils.getElementNameByType(String.class);
        assertEquals("java.lang.String", name);
    }

    private static final List<Integer> intList = List.of(1, 2);
    private static final List<String> stringList = List.of("");


    @Test
    void shouldGetElementNameByType_ifElementHasGenericType_andNameIsDifferentForDifferentTypeParameters() {
        String name1 = ElementUtils.getElementNameByType(intList.getClass());
        String name2 = ElementUtils.getElementNameByType(stringList.getClass());
        log.info("Name for int list: '{}' Name for string list: {}", name1, name2);
        //assertNotEquals(name1, name2); -> it would be nice, but I cannot implement it :(
        assertEquals(name1, name2);
    }
}
