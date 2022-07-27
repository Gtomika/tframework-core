package org.tframework.core.ioc;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.ioc.annotations.ManagePreConstructedSingleton;
import org.tframework.core.ioc.exceptions.IocException;
import org.tframework.core.ioc.scan.DefaultManagedEntityScanner;
import org.tframework.core.ioc.scan.ManagedEntityScanner;

/**
 * Contains the methods to initialize the TFramework inversion of control, and holds the data related
 * to IoC.
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TFrameworkIoc {

    /**
     * Creates one <b>uninitialized</b> instance of {@link TFrameworkIoc}.
     * @return The instance.
     */
    public static TFrameworkIoc createInstance() {
        return new TFrameworkIoc(
            findManagedEntityScanner(),
            new ManagedEntitiesRepository(),
            new DependencyResolver()
        );
    }

    /**
     * Finds the appropriate {@link ManagedEntityScanner} implementation to be used.
     * TODO: control this with some property.
     */
    private static ManagedEntityScanner findManagedEntityScanner() {
        return new DefaultManagedEntityScanner();
    }

    /**
     * Looks for managed entities.
     */
    @Getter
    private final ManagedEntityScanner managedEntityScanner;

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
     *     <li>
     *         Scanning for pre-constructed entities to be brought into the IoC
     *         ({@link ManagePreConstructedSingleton}).
     *     </li>
     *     <li>Scanning for {@link org.tframework.core.ioc.annotations.Managed} entities.</li>
     *     <li>Discovering the dependency relations between them.</li>
     * </ul>
     * @param rootClass The class annotated with {@link org.tframework.core.TFrameworkRoot}.
     * @throws IocException If the IoC could not be initialized. See cause exceptions for details.
     */
    public void initializeIoc(Class<?> rootClass) throws IocException {
        log.info("Initializing TFramework IoC with root class '{}'.", rootClass.getName());
        managedEntityScanner.scanAndRegisterManagedEntities(rootClass);
        //discover the dependency relations between managed entities
        dependencyResolver.discoverDependencies();
        log.info("TFramework IoC initialized!");
    }

}
