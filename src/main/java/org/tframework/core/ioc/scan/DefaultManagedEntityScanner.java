package org.tframework.core.ioc.scan;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.tframework.core.ioc.IocValidator;
import org.tframework.core.ioc.annotations.Managed;
import org.tframework.core.ioc.exceptions.IocException;

/**
 * The default implementation of {@link ManagedEntityScanner} that the TFramework uses. This scanner will look for
 * managed entities in the package of the root class, and all subpackages of this package.
 */
@Slf4j
public class DefaultManagedEntityScanner extends ManagedEntityScanner {

    /**
     * Scans the package, and subpackages of the root class for entities that are to be managed by the TFramework.
     * @param rootClass The only class on the classpath that is annotated with {@link org.tframework.core.TFrameworkRoot}.
     * @throws IocException If an exception happened when attempting to register the managed entities.
     */
    @Override
    public void scanAndRegisterManagedEntities(Class<?> rootClass) throws IocException {
        log.info("Scanning for managed entities in package '{}' and subpackages...", rootClass.getPackageName());
        //scan the class path for managed entities
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(ClasspathHelper.forPackage(rootClass.getPackageName()))
        );
        //these are the CLASSES annotated by @Managed -> does not include provider fields
        var managedEntityClasses = reflections.getTypesAnnotatedWith(Managed.class);
        log.info("Found {} class entities annotated with @Managed in the subpackages of '{}'",
                managedEntityClasses.size(), rootClass.getPackageName());
        registerManagedEntities(managedEntityClasses);
        //need to find the managed entities defined as provider methods
        //they can only be in managed entities classes
        var providedEntities = reflections.getMethodsAnnotatedWith(Managed.class);
        providedEntities.forEach(pme -> IocValidator.validateProviderMethod(pme, null));
        log.info("Found {} provided entities annotated with @Managed in the subpackages of '{}'",
                providedEntities.size(), rootClass.getPackageName());
        registerProvidedEntities(providedEntities);
        //finally, register the ApplicationContext to make it injectable
        registerApplicationContext();
    }

}
