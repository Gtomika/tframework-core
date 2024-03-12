package org.tframework.core.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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