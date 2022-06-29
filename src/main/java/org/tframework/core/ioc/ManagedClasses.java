package org.tframework.core.ioc;

import org.tframework.core.ioc.exceptions.MultipleManagedEntitiesException;
import org.tframework.core.ioc.exceptions.NameNotUniqueException;
import org.tframework.core.ioc.exceptions.NoSuchManagedEntityException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Stores instances of the classes managed by the TFramework. Injected dependencies are
 * grabbed from here, and methods to allow direct grabbing of managed classes are also provided.
 * <p>
 * This class provides the managed classes wrapped in {@link ManagedContainer}s.
 */
public class ManagedClasses {

    /**
     * Map of all the singleton typed managed classes. Only one instance of each exists, which is
     * wrapped in {@link ManagedContainer}, and can be accessed by passing the {@link Class} as key.
     */
    private Map<String, ManagedContainer<?>> singletons;

    /**
     * Map of all the multi instance managed classes. Each of them is a list, containing all the instances
     * of the managed type, wrapped in {@link ManagedContainer}s.
     */
    private Map<String, List<ManagedContainer<?>>> multiInstances;

    protected <T> void registerManagedSingletonContainer(ManagedContainer<T> container) {

    }

    /**
     * Grab a container of a managed singleton.
     * @param clazz Class of the managed class.
     * @param <T> Type of the managed class.
     * @return Container of the managed singleton.
     * @throws NoSuchManagedEntityException If the provided class is not a managed singleton.
     * @throws MultipleManagedEntitiesException If there are multiple managed singletons with the same type.
     */
    protected <T> ManagedContainer<T> grabSingletonContainer(Class<T> clazz)
            throws NoSuchManagedEntityException, MultipleManagedEntitiesException {
        //collect all candidates
        List<ManagedContainer<?>> candidates = new ArrayList<>();
        singletons.keySet().forEach(name -> {
            ManagedContainer<?> container = singletons.get(name);
            if(container.getInstanceType() == clazz) {
                candidates.add(container);
            }
        });
        if(candidates.isEmpty()) throw new NoSuchManagedEntityException(clazz);
        if(candidates.size() > 1) throw new MultipleManagedEntitiesException(clazz);
        //must be only one
        return (ManagedContainer<T>) candidates.get(0);
    }

    /**
     * Checks all managed entities to see if the provided name is already taken or not.
     * @throws NameNotUniqueException Indicates that the name is not unique.
     */
    private void checkNameUniqueness(String name) throws NameNotUniqueException {
        singletons.keySet().forEach(singletonName -> {
            if(name.equals(singletonName)) throw new NameNotUniqueException(name);
        });
        multiInstances.
    }
}
