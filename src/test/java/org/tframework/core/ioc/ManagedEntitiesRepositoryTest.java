/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.tframework.core.ioc.constants.ManagingType;
import org.tframework.core.ioc.containers.ManagedSingletonContainer;
import org.tframework.core.ioc.exceptions.MultipleManagedEntitiesException;
import org.tframework.core.ioc.exceptions.NameNotUniqueException;
import org.tframework.core.ioc.exceptions.NoSuchManagedEntityException;

class ManagedEntitiesRepositoryTest {

    private ManagedEntitiesRepository managedEntitiesRepository;
    private TestManagedClass testManagedClass;

    @BeforeEach
    public void init() {
        managedEntitiesRepository = new ManagedEntitiesRepository();
        testManagedClass = new TestManagedClass();

        var container = createManagedSingletonContainer();
        managedEntitiesRepository.registerManagedEntityContainer(container);
    }

    @Test
    public void testRegisterDuplicateName() {
        assertThrows(NameNotUniqueException.class, () ->
                managedEntitiesRepository.registerManagedEntityContainer(createManagedSingletonContainer()));
    }

    @Test
    public void testGrab() {
        var receivedContainer = managedEntitiesRepository.grabManagedEntityContainer(TestManagedClass.class);
        assertEquals(TestManagedClass.class.getName(), receivedContainer.getName());
        assertEquals(ManagingType.SINGLETON, receivedContainer.getManagingType());
        assertEquals(testManagedClass, receivedContainer.grabInstance());
        assertEquals(TestManagedClass.class, receivedContainer.getInstanceType());
    }

    @Test
    public void testGrabNotExisting() {
        assertThrows(NoSuchManagedEntityException.class,
                () -> managedEntitiesRepository.grabManagedEntityContainer(Integer.class));
    }

    @Test
    public void testGetManagingTypeByClassNotManagedClass() {
        assertThrows(NoSuchManagedEntityException.class,
                () -> managedEntitiesRepository.getManagingTypeByClass(File.class));
    }

    @Test
    public void testGetManagingTypeByClassMultipleManaged() {
        var container = new ManagedSingletonContainer<>(
                "Test managed 2", TestManagedClass.class, testManagedClass
        );
        managedEntitiesRepository.registerManagedEntityContainer(container);
        assertThrows(MultipleManagedEntitiesException.class,
                () -> managedEntitiesRepository.getManagingTypeByClass(TestManagedClass.class));
    }

    @Test
    public void testGetManagingTypeByName() {
        ManagingType type = managedEntitiesRepository.getManagingTypeByName(TestManagedClass.class.getName());
        assertEquals(ManagingType.SINGLETON, type);
    }

    @Test
    public void testIterateAllEntities() {
        var allContainers = managedEntitiesRepository.iterateManagedEntities();
        for(var container: allContainers) {
            assertEquals(testManagedClass, container.grabInstance());
        }
    }

    @Test
    public void testGetManagedEntityByName() {
        var container = managedEntitiesRepository.grabManagedEntityContainer(TestManagedClass.class.getName());
        assertEquals(TestManagedClass.class.getName(), container.getName());
    }

    @Test
    public void testGetManagedEntityByNameNotFound() {
        assertThrows(NoSuchManagedEntityException.class,
                () -> managedEntitiesRepository.grabManagedEntityContainer("hello there"));
    }

    static class TestManagedClass {
        public TestManagedClass() {}
    }

    private ManagedSingletonContainer<TestManagedClass> createManagedSingletonContainer() {
        return new ManagedSingletonContainer<>(
                TestManagedClass.class.getName(), TestManagedClass.class, testManagedClass
        );
    }

}