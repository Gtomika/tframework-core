package org.tframework.core.ioc.scan;

import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.tframework.core.TFramework;
import org.tframework.core.TFrameworkRoot;
import org.tframework.core.ioc.IocValidator;
import org.tframework.core.ioc.annotations.Managed;
import org.tframework.core.ioc.exceptions.IocException;

import java.net.URL;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The default implementation of {@link ManagedEntityScanner} that the TFramework uses. This scanner will look for
 * managed entities in the package of the root class, and all subpackages of this package.
 * <p><p>
 * It will also look for managed entities in the Framework packages, those starting with {@code org.tframework}.
 */
@Slf4j
public class DefaultManagedEntityScanner extends ManagedEntityScanner {

    private static final String TFRAMEWORK_PACKAGES_PREFIX = "org.tframework";

    /**
     * Gets a collection of package URLs that will be scanned for managed entities by the scanner. This will include
     * the package (and subpackages) of the root class, and all TFramework packages.
     * @param rootClass he root class of the Tframework application. Usually defined by annotation {@link TFrameworkRoot}.
     *                  Different implementation of {@link ManagedEntityScanner} can interpret it differently.
     */
    @Override
    public Collection<URL> getScannedUrls(Class<?> rootClass) {
        return Stream.concat(
                ClasspathHelper.forPackage(rootClass.getPackageName()).stream(),
                ClasspathHelper.forPackage(TFRAMEWORK_PACKAGES_PREFIX).stream()
        ).collect(Collectors.toSet());
    }

    /**
     * Scans the package, and subpackages of the root class for entities that are to be managed by the TFramework.
     * @param rootClass The only class on the classpath that is annotated with {@link org.tframework.core.TFrameworkRoot}.
     * @throws IocException If an exception happened when attempting to register the managed entities.
     */
    @Override
    public void scanAndRegisterManagedEntities(Class<?> rootClass) throws IocException {
        log.info("Scanning for managed entities in packages '{}' and '{}'...",
                rootClass.getPackageName(), TFRAMEWORK_PACKAGES_PREFIX);
        //scan the class path for managed entities
        Reflections reflections = new Reflections(
                new ConfigurationBuilder()
                        .setUrls(getScannedUrls(rootClass))
        );
        //these are the CLASSES annotated by @Managed -> does not include provider fields
        var managedEntityClasses = reflections.getTypesAnnotatedWith(Managed.class);
        log.info("Found {} class entities annotated with @Managed in the packages '{}' and '{}'",
                managedEntityClasses.size(), rootClass.getPackageName(), TFRAMEWORK_PACKAGES_PREFIX);
        registerManagedEntities(managedEntityClasses);
        //need to find the managed entities defined as provider methods
        //they can only be in managed entities classes
        var providedEntities = reflections.getMethodsAnnotatedWith(Managed.class);
        providedEntities.forEach(pme -> IocValidator.validateProviderMethod(pme, null));
        log.info("Found {} provided entities annotated with @Managed in the packages '{}' and '{}'",
                providedEntities.size(), rootClass.getPackageName(), TFRAMEWORK_PACKAGES_PREFIX);
        registerProvidedEntities(providedEntities);
        //finally, register the ApplicationContext to make it injectable
        registerApplicationContext();
    }

}
