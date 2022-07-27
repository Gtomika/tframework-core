/* Licensed under Apache-2.0 2022. */
package org.tframework.core.flavors;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.tframework.core.TestUtils;

class ActiveFlavorManagerTest {

    private static final String TEST_FLAVORS = "debug,test,core";

    private ActiveFlavorManager activeFlavorManager;

    @BeforeEach
    public void setUp() throws Exception {
        TestUtils.setEnv(Map.of(
                ActiveFlavorManager.ACTIVE_FLAVOR_VAR_NAME, TEST_FLAVORS
        ));
        activeFlavorManager = new ActiveFlavorManager();
    }

    @Test
    public void testReadActiveFlavorsFromEnvironmentalVariable() {
        assertEquals(Set.of("debug", "test", "core"), activeFlavorManager.getActiveFlavors());
    }

    @ParameterizedTest
    @ValueSource(strings = {"debug", "release", "test_integration"})
    public void testFlavorNameValid(String flavor) {
        Boolean valid = TestUtils.invokePrivateMethod(activeFlavorManager, "isValidFlavorName", flavor);
        assertTrue(valid);
    }

    @ParameterizedTest
    @ValueSource(strings = {"Debug", "RELEASE", "test2"})
    public void testFlavorNameInvalid(String flavor) {
        Boolean valid = TestUtils.invokePrivateMethod(activeFlavorManager, "isValidFlavorName", flavor);
        assertFalse(valid);
    }

    @Test
    public void testFlavorsNotModifiable() {
        var flavors = activeFlavorManager.getActiveFlavors();
        assertThrows(UnsupportedOperationException.class, () -> flavors.add("new_flavor"));
        assertFalse(activeFlavorManager.isActiveFlavor("new_flavor"));
    }

    @Test
    public void testIsActiveFlavorPositive() {
        assertTrue(activeFlavorManager.isActiveFlavor("debug"));
    }

    @Test
    public void testIsActiveFlavorNegative() {
        assertFalse(activeFlavorManager.isActiveFlavor("something"));
    }
}