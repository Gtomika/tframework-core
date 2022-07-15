package org.tframework.core.ioc;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.ioc.exceptions.IocException;

/**
 * Contains the methods to initialize the TFramework inversion of control, and holds the data related
 * to IoC.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TFrameworkIoc {

    /**
     * Creates one initialized instance of {@link TFrameworkIoc}.
     * @param rootClass The class annotated with {@link org.tframework.core.TFrameworkRoot}.
     * @throws IocException If the IoC could not be initialized. See cause exceptions for details.
     * @return The initialized instance.
     */
    public static TFrameworkIoc createInstance(Class<?> rootClass) throws IocException {
        return new TFrameworkIoc(
            new ManagedEntitiesRepository(),
            new DependencyResolver()
        ).initializeIoc(rootClass);
    }

    /**
     * Stores and handles the managed classes.
     */
    @Getter
    private final ManagedEntitiesRepository managedEntitiesRepository;

    /**
     * Discovers and resolves dependencies, stores the dependency graph.
     */
    @Getter
    private final DependencyResolver dependencyResolver;

    /**
     * Initialize the IoC, which includes:
     * <ul>
     *     <li>Scanning for managed entities.</li>
     *     <li>Discovering the dependency relations between them.</li>
     *     <li>Injecting the dependencies.</li>
     * </ul>
     * @param rootClass The class annotated with {@link org.tframework.core.TFrameworkRoot}.
     * @throws IocException If the IoC could not be initialized. See cause exceptions for details.
     * @return Itself for chained calls.
     */
    public TFrameworkIoc initializeIoc(Class<?> rootClass) throws IocException {
        log.info("Initializing TFramework IoC...");
        ManagedEntityScanner.scanAndRegisterManagedEntities(rootClass);
        //discover the dependency relations between managed entities
        dependencyResolver.discoverDependencies();
        //resolve them after discovery
        dependencyResolver.resolveDependencies();
        log.info("TFramework IoC initialized!");
        return this;
    }

}
