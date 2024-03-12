/* Licensed under Apache-2.0 2024. */
package org.tframework.core.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PrimitivesUtilsTest {

    @Test
    public void shouldConvertPrimitiveToWrapper_ifClassIsPrimitive() {
        assertEquals(Integer.class, PrimitivesUtils.toWrapper(int.class));
    }

    @Test
    public void shouldLeaveClassAsIs_ifClassIsNotPrimitive() {
        assertEquals(String.class, PrimitivesUtils.toWrapper(String.class));
    }

}
