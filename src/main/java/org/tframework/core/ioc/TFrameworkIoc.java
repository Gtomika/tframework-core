package org.tframework.core.ioc;

import lombok.extern.slf4j.Slf4j;
import org.tframework.core.ioc.exceptions.IocException;

/**
 * Contains the methods to initialize the TFramework inversion of control.
 */
@Slf4j
public class TFrameworkIoc {

    /**
     * Initialize the IoC, which includes:
     * <ul>
     *     <li>Scanning for managed entities.</li>
     *     <li>Discovering the dependency relations between them.</li>
     *     <li>Injecting the dependencies.</li>
     * </ul>
     * @param rootClass The class annotated with {@link org.tframework.core.TFrameworkRoot}.
     * @throws IocException If the IoC could not be initialized. See cause exceptions for details.
     */
    public static void initializeIoc(Class<?> rootClass) throws IocException {
        log.info("Initializing TFramework IoC...");
        ManagedEntityScanner.scanAndRegisterManagedEntities(rootClass);
        //discover the dependency relations between managed entities
        DependencyResolver.discoverDependencies();
        //resolve them after discovery
        DependencyResolver.resolveDependencies();
        log.info("TFramework IoC initialized!");
    }

}
