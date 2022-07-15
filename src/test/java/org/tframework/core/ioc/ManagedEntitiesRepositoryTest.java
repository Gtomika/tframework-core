/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tframework.core.ioc.constants.ManagingType;
import org.tframework.core.ioc.containers.ManagedSingletonContainer;
import org.tframework.core.ioc.exceptions.MultipleManagedEntitiesException;
import org.tframework.core.ioc.exceptions.NameNotUniqueException;
import org.tframework.core.ioc.exceptions.NoSuchManagedEntityException;

class ManagedEntitiesRepositoryTest {

    private ManagedEntitiesRepository managedEntitiesRepository;
    private String testString;

    @BeforeEach
    public void init() {
        managedEntitiesRepository = new ManagedEntitiesRepository();
        testString = RandomStringUtils.randomAlphabetic(20);

        var container = createManagedSingletonContainer();
        managedEntitiesRepository.registerManagedSingletonContainer(container);
    }

    @Test
    public void testRegisterSingletonDuplicateName() {
        assertThrows(NameNotUniqueException.class, () ->
                managedEntitiesRepository.registerManagedSingletonContainer(createManagedSingletonContainer()));
    }

    @Test
    public void testGrabSingleton() {
        var receivedContainer = managedEntitiesRepository.grabSingletonContainer(String.class);
        assertEquals(String.class.getName(), receivedContainer.getName());
        assertEquals(ManagingType.SINGLETON, receivedContainer.getManagingType());
        assertEquals(testString, receivedContainer.grabInstance());
        assertEquals(String.class, receivedContainer.getInstanceType());
    }

    @Test
    public void testGrabNotExistingSingleton() {
        assertThrows(NoSuchManagedEntityException.class, () -> managedEntitiesRepository.grabSingletonContainer(Integer.class));
    }

    @Test
    public void testGrabSingletonMultipleExists() {
        var container = new ManagedSingletonContainer<>(
            "String 2", String.class, testString
        );
        managedEntitiesRepository.registerManagedSingletonContainer(container);

        assertThrows(MultipleManagedEntitiesException.class, () -> managedEntitiesRepository.grabSingletonContainer(String.class));
    }

    @Test
    public void testGetManagingTypeByClassSingleton() {
        ManagingType type = managedEntitiesRepository.getManagingTypeByClass(String.class);
        assertEquals(ManagingType.SINGLETON, type);
    }

    @Test
    public void testGetManagingTypeByClassSingletonNotManagedClass() {
        assertThrows(NoSuchManagedEntityException.class,
                () -> managedEntitiesRepository.getManagingTypeByClass(File.class));
    }

    @Test
    public void testGetManagingTypeByClassSingletonMultipleManaged() {
        var container = new ManagedSingletonContainer<>(
                "String 2", String.class, testString
        );
        managedEntitiesRepository.registerManagedSingletonContainer(container);
        assertThrows(MultipleManagedEntitiesException.class,
                () -> managedEntitiesRepository.getManagingTypeByClass(String.class));
    }

    @Test
    public void testGetManagingTypeByNameSingleton() {
        ManagingType type = managedEntitiesRepository.getManagingTypeByName(String.class.getName());
        assertEquals(ManagingType.SINGLETON, type);
    }

    @Test
    public void testGetManagingTypeByNameSingletonNoSuchName() {
        assertThrows(NoSuchManagedEntityException.class,
                () -> managedEntitiesRepository.getManagingTypeByName("Hello there"));
    }

    @Test
    public void testIterateAllEntities() {
        var allContainers = managedEntitiesRepository.iterateManagedEntities();
        for(var container: allContainers) {
            assertEquals(testString, container.grabInstance().toString());
        }
    }

    private ManagedSingletonContainer<String> createManagedSingletonContainer() {
        return  new ManagedSingletonContainer<>(
                String.class.getName(), String.class, testString
        );
    }

}