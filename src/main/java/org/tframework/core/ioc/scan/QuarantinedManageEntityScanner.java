package org.tframework.core.ioc.scan;

import org.tframework.core.TFrameworkRoot;
import org.tframework.core.ioc.ManagedEntitiesRepository;
import org.tframework.core.ioc.exceptions.IocException;

/**
 * A {@link ManagedEntityScanner} implementation that only uses scans for managed entities inside the root class itself.
 * This is useful for example during unit tests, where nested classes can be defined as managed entities, and these will
 * not affect the other unit test classes.
 */
public class QuarantinedManageEntityScanner extends ManagedEntityScanner {

    /**
     * Find and register all managed entities and store them in the {@link ManagedEntitiesRepository}.
     *
     * @param rootClass The root class of the Tframework application. Usually defined by annotation {@link TFrameworkRoot}.
     *                  Different implementation of {@link ManagedEntityScanner} can interpret it differently.
     * @throws IocException If the scanning or registering failed.
     */
    @Override
    public void scanAndRegisterManagedEntities(Class<?> rootClass) throws IocException {
        //TODO: find classes and provider methods annotated with @Managed
    }
}
