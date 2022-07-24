/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tframework.core.TestUtils;

class DependencyResolverTest {

    private DependencyResolver dependencyResolver;

    @BeforeEach
    public void setUp() {
        //TODO: set application context instance up with mock managed entities
        dependencyResolver = new DependencyResolver();
    }

    @Test
    public void testIsSelfDependencyTrue() {
        Boolean self = TestUtils.invokePrivateMethod(dependencyResolver, "isSelfDependency", "dp", "dp");
        assertTrue(self);
    }

    @Test
    public void testIsSelfDependencyFalse() {
        Boolean self = TestUtils.invokePrivateMethod(dependencyResolver, "isSelfDependency", "dp", "not dp");
        assertFalse(self);
    }

    @Test
    public void testAddToDependencyGraph() {
        TestUtils.invokePrivateMethod(dependencyResolver, "addDependencyRelationToGraph",
                "node1", "node2");
        assertEquals(2, dependencyResolver.getDependencyGraphSize());
    }
}