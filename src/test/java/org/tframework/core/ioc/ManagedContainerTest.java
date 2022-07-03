/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ManagedContainerTest {

    @Test
    public void testBuildInvalidContainer() {
        var builder = ManagedContainer
                .<Integer>builder()
                .name(Integer.class.getName())
                .instance(1)
                .managedType(ManagedType.MULTI_INSTANCE);
        //instance type is missed
        assertThrows(NullPointerException.class, builder::build);
    }

}