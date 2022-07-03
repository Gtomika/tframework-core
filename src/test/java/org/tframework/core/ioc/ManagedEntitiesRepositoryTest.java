/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tframework.core.ioc.exceptions.MultipleManagedEntitiesException;
import org.tframework.core.ioc.exceptions.NameNotUniqueException;
import org.tframework.core.ioc.exceptions.NoSuchManagedEntityException;

class ManagedEntitiesRepositoryTest {

    private ManagedEntitiesRepository managedClasses;
    private String testString;

    @BeforeEach
    public void init() {
        managedClasses = new ManagedEntitiesRepository();
        testString = RandomStringUtils.randomAlphabetic(20);
    }

    @Test
    public void testRegisterSingleton() {
        var container = createManagedSingletonContainer();
        assertDoesNotThrow(() -> managedClasses.registerManagedSingletonContainer(container));
    }

    @Test
    public void testRegisterSingletonDuplicateName() {
        var container = createManagedSingletonContainer();
        managedClasses.registerManagedSingletonContainer(container);
        assertThrows(NameNotUniqueException.class, () ->
                managedClasses.registerManagedSingletonContainer(createManagedSingletonContainer()));
    }

    @Test
    public void testGrabSingleton() {
        var container = createManagedSingletonContainer();
        managedClasses.registerManagedSingletonContainer(container);
        var receivedContainer = managedClasses.grabSingletonContainer(String.class);
        assertEquals(String.class.getName(), receivedContainer.getName());
        assertEquals(ManagedType.SINGLETON, receivedContainer.getManagedType());
        assertEquals(testString, receivedContainer.grab());
        assertEquals(String.class, receivedContainer.getInstanceType());
    }

    @Test
    public void testGrabNotExistingSingleton() {
        assertThrows(NoSuchManagedEntityException.class, () -> managedClasses.grabSingletonContainer(Integer.class));
    }

    @Test
    public void testGrabSingletonMultipleExists() {
        var container1 = createManagedSingletonContainer();
        var container2 = ManagedContainer
                .<String>builder()
                .name("String 2") //specify different name
                .managedType(ManagedType.SINGLETON)
                .instance(testString)
                .instanceType(String.class)
                .build();
        managedClasses.registerManagedSingletonContainer(container1);
        managedClasses.registerManagedSingletonContainer(container2);

        assertThrows(MultipleManagedEntitiesException.class, () -> managedClasses.grabSingletonContainer(String.class));
    }

    private ManagedContainer<String> createManagedSingletonContainer() {
        return ManagedContainer
                .<String>builder()
                .name(String.class.getName())
                .managedType(ManagedType.SINGLETON)
                .instance(testString)
                .instanceType(String.class)
                .build();
    }

}