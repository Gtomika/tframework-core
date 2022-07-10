/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.ioc.constants.ManagingType;
import org.tframework.core.ioc.containers.AbstractContainer;
import org.tframework.core.ioc.containers.ManagedMultiInstanceContainer;
import org.tframework.core.ioc.containers.ManagedSingletonContainer;
import org.tframework.core.ioc.exceptions.MultipleManagedEntitiesException;
import org.tframework.core.ioc.exceptions.NameNotUniqueException;
import org.tframework.core.ioc.exceptions.NoSuchManagedEntityException;

import javax.annotation.Nullable;

/**
 * Stores instances of the classes managed by the TFramework. Injected dependencies are grabbed from
 * here, and methods to allow direct grabbing of managed classes are also provided.
 *
 * <p>This class provides the managed classes wrapped in {@link ManagedSingletonContainer}s.
 */
@Slf4j
public class ManagedEntitiesRepository {

    /**
     * Map of all the singleton typed managed classes. Only one instance of each exists, which is
     * wrapped in {@link ManagedSingletonContainer}.
     */
    private final Map<String, ManagedSingletonContainer<?>> singletons;

    /**
     * Map of all the multi instance managed classes wrapped in {@link ManagedMultiInstanceContainer}s.
     */
    private final Map<String, ManagedMultiInstanceContainer<?>> multiInstances;

    public ManagedEntitiesRepository() {
        singletons = new HashMap<>();
        multiInstances = new HashMap<>();
    }

    /**
     * Register a new managed singleton.
     *
     * @param container Container with the singleton, contains all data about it.
     * @param <T> Type of the managed entity.
     * @throws NameNotUniqueException If the managed entity uses a duplicate name.
     */
    public <T> void registerManagedSingletonContainer(ManagedSingletonContainer<T> container)
            throws NameNotUniqueException {
        checkNameUniqueness(container.getName());
        singletons.put(container.getName(), container);
        log.debug(
                "Registered new managed singleton with name '{}' and class '{}'",
                container.getName(),
                container.getInstanceType().getName());
    }

    /**
     * Grab a container of a managed singleton.
     *
     * @param clazz Class of the managed class.
     * @param <T> Type of the managed class.
     * @return Container of the managed singleton.
     * @throws NoSuchManagedEntityException If the provided class is not a managed singleton.
     * @throws MultipleManagedEntitiesException If there are multiple managed singletons with the
     *     same type.
     */
    public <T> ManagedSingletonContainer<T> grabSingletonContainer(Class<T> clazz)
            throws NoSuchManagedEntityException, MultipleManagedEntitiesException {
        // collect all candidates
        var candidates = findManagedCandidatesByClass(clazz, ManagingType.SINGLETON);
        if (candidates.isEmpty()) throw new NoSuchManagedEntityException(clazz);
        if (candidates.size() > 1) throw new MultipleManagedEntitiesException(clazz);
        // must be only one (it's a safe cast)
        return (ManagedSingletonContainer<T>) candidates.get(0);
    }

    /**
     * Finds the managing type of a managed entity by its name.
     * @param name The name of the entity.
     * @return The {@link ManagingType} of the entity with the given name.
     * @throws NoSuchManagedEntityException If there is no managed entity with the given name.
     */
    public ManagingType getManagingTypeByName(String name) throws NoSuchManagedEntityException {
        if(singletons.containsKey(name)) return ManagingType.SINGLETON;
        if(multiInstances.containsKey(name)) return ManagingType.MULTI_INSTANCE;
        throw new NoSuchManagedEntityException(name);
    }

    /**
     * Finds the managing type of a managed entity by its class.
     * @param clazz The class of the managed entity.
     * @return The {@link ManagingType} of the entity with the given class.
     * @throws NoSuchManagedEntityException If there is no entity managed with this class.
     * @throws MultipleManagedEntitiesException If there are multiple managed entities with this class.
     */
    public <T> ManagingType getManagingTypeByClass(Class<T> clazz)
            throws NoSuchManagedEntityException, MultipleManagedEntitiesException {
        var candidates = findManagedCandidatesByClass(clazz, null);
        if (candidates.isEmpty()) throw new NoSuchManagedEntityException(clazz);
        if (candidates.size() > 1) throw new MultipleManagedEntitiesException(clazz);
        return candidates.get(0).getManagingType();
    }

    /**
     * Checks all managed entities to see if the provided name is already taken or not.
     *
     * @throws NameNotUniqueException Indicates that the name is not unique.
     */
    private void checkNameUniqueness(String name) throws NameNotUniqueException {
        singletons.keySet().forEach(singletonName -> {
            if (name.equals(singletonName)) throw new NameNotUniqueException(name);
        });
        multiInstances.keySet().forEach(multiInstanceBaseName -> {
            if (name.equals(multiInstanceBaseName)) throw new NameNotUniqueException(name);
        });
    }

    /**
     * Finds all managed entities with a given class. This method is useful when users want to
     * grab managed entities by class, and there may be multiple of them.
     * @param clazz The class which the candidates must have.
     * @param managingType Limit the candidates by the managing type. This can be null if there
     *                     is no need to limit by type.
     * @return List with all containers of entities that match the provided parameters.
     */
    private <T> List<AbstractContainer<T>> findManagedCandidatesByClass(
            Class<T> clazz,
            @Nullable ManagingType managingType
    ) {
        var candidates = new ArrayList<AbstractContainer<T>>();
        if(managingType == null || managingType == ManagingType.SINGLETON) {
            //look inside singletons
            singletons.keySet().forEach(name -> {
                ManagedSingletonContainer<?> container = singletons.get(name);
                if (container.getInstanceType() == clazz) {
                    candidates.add((ManagedSingletonContainer<T>)container);
                }
            });
        }
        if(managingType == null || managingType == ManagingType.MULTI_INSTANCE) {
            multiInstances.keySet().forEach(name -> {
                ManagedMultiInstanceContainer<?> container = multiInstances.get(name);
                if(container.getInstanceType() == clazz) {
                    candidates.add((ManagedMultiInstanceContainer<T>)container);
                }
            });
        }
        return candidates;
    }
}
