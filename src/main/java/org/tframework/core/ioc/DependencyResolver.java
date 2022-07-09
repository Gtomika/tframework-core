package org.tframework.core.ioc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.ioc.exceptions.IocException;

/**
 * Investigates the managed entities and resolves the dependencies between them by creating
 * and injecting instances of managed entities.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DependencyResolver {

    /**
     * Investigates the managed entities and finds the dependencies for each of them. These will be
     * saved as {@link DependencyInformation} objects in the containers of the managed entities.
     * <p>
     * Note that this does not resolve the dependencies, only discovers them. For example if managed entity 'A'
     * has an {@link org.tframework.core.ioc.annotations.Injected} field of managed entity 'B', then this dependency
     * will be discovered and saved in the container of 'A'.
     * @throws IocException If the dependencies could not be discovered or an illegal relation was found between them.
     * The cause exception contains the details.
     */
    public static void discoverDependencies() throws IocException {

    }

    /**
     * Injects the dependencies into each managed entity. This method assumes the dependencies are discovered and valid (
     * can be done with {@link #discoverDependencies()}).
     * @throws IocException If the dependencies could not be injected.
     */
    public static void resolveDependencies() throws IocException {

    }

}
