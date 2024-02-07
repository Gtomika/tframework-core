/* Licensed under Apache-2.0 2024. */
package org.tframework.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;
import org.tframework.core.Application;
import org.tframework.core.TFramework;
import org.tframework.core.elements.PreConstructedElementData;
import org.tframework.core.properties.parsers.PropertyParsingUtils;
import org.tframework.core.properties.parsers.SeparatedProperty;
import org.tframework.core.readers.ReadersFactory;
import org.tframework.core.readers.SystemPropertyNotFoundException;
import org.tframework.core.readers.SystemPropertyReader;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;
import org.tframework.test.annotations.SetApplicationName;
import org.tframework.test.annotations.SetCommandLineArguments;
import org.tframework.test.annotations.SetElements;
import org.tframework.test.annotations.SetProfiles;
import org.tframework.test.annotations.SetProperties;
import org.tframework.test.annotations.SetRootClass;
import org.tframework.test.utils.PredicateExecutor;
import org.tframework.test.utils.TestActionsUtils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.tframework.core.profiles.scanners.SystemPropertyProfileScanner.PROFILES_SYSTEM_PROPERTY;
import static org.tframework.core.properties.scanners.SystemPropertyScanner.PROPERTY_PREFIX;

/**
 * This is a JUnit 5 extension that allows to easily start TFramework applications. <b>The test instance created by JUnit will
 * be added as an element, allowing to field inject dependencies</b>. The application will be started before the tests, once.
 * It will be stopped after all tests are completed.
 *
 * <h3>Configuring the application</h3>
 * Additional annotations can be used on the test class to specify the details of the started application:
 * <ul>
 *     <li>{@link SetApplicationName} can be used to set a custom application name. By default, the name will be deduced from the test class.</li>
 *     <li>{@link SetRootClass} can be used to set an application root class. By default, the test class will be the root class.</li>
 *     <li>{@link SetCommandLineArguments} can be used to specify arguments passed to the application. By default nothing will be passed.</li>
 *     <li>{@link SetProfiles} can be used to set profiles.</li>
 *     <li>{@link SetProperties} can be used to control general properties.</li>
 *     <li>{@link SetElements} can be used to control element related settings, such as what to scan.</li>
 * </ul>
 * A {@link org.tframework.core.reflection.annotations.ComposedAnnotationScanner} will be used to pick up these annotations,
 * so it is possible to use composed meta annotations that combine {@link org.junit.jupiter.api.extension.ExtendWith} and
 * the other annotations described above.
 *
 * <h3>Using the application</h3>
 * There are some ways to get the launched {@link Application} object.
 * <ul>
 *     <li><b>Recommended:</b> it can be field injected into the test class, because the test class is an element.</li>
 *     <li>It can be added as a parameter to JUnit methods such as {@link org.junit.jupiter.api.BeforeEach} and {@link org.junit.jupiter.api.Test}.</li>
 * </ul>
 * @see org.tframework.test.annotations.SingleClassTFrameworkTest
 */
@Slf4j
public class TFrameworkExtension implements Extension, TestInstancePostProcessor, AfterAllCallback, ParameterResolver {

    private final AnnotationScanner annotationScanner = AnnotationScannersFactory.createComposedAnnotationScanner();
    private final SystemPropertyReader systemPropertyReader = ReadersFactory.createSystemPropertyReader();

    private Application application;

    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        var testClass = testInstance.getClass();
        placeProfilesForApplication(testClass);
        placePropertiesForApplication(testClass);
        placeElementSettingsForApplication(testClass);

        log.debug("Starting TFramework application for test instance '{}'...", testClass.getName());
        application = TFramework.start(
                findApplicationName(testClass),
                findRootClass(testClass),
                findCommandLineArguments(testClass),
                Set.of(PreConstructedElementData.from(testInstance))
        );
    }

    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        if(application != null) {
            log.debug("Shutting down the '{}' application after all tests", application.getName());
            TFramework.stop(application);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if(parameterContext.getDeclaringExecutable() instanceof Constructor<?>) {
            log.debug("Injecting the application into the test instance constructor is not supported, because " +
                    "the application is not yet started.");
            return false;
        }
        return parameterContext.getParameter().getType().equals(Application.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return application;
    }

    private String findApplicationName(Class<?> testClass) {
        return annotationScanner.scanOneStrict(testClass, SetApplicationName.class)
                .map(SetApplicationName::value)
                .orElseGet(() -> {
                    String defaultName = testClass.getName();
                    log.debug("No '{}' test annotation was found, using default name '{}' for the application",
                            SetApplicationName.class.getName(), defaultName);
                    return defaultName;
                });
    }

    private Class<?> findRootClass(Class<?> testClass) {
        var rootClassAnnotationOptional = annotationScanner.scanOneStrict(testClass, SetRootClass.class);
        if(rootClassAnnotationOptional.isPresent()) {
            var rootClassAnnotation = rootClassAnnotationOptional.get();
            if(!rootClassAnnotation.rootClass().equals(SetRootClass.ROOT_CLASS_NOT_DIRECTLY_SPECIFIED)) {
                //root class was directly specified, so let's use that
                return rootClassAnnotation.rootClass();
            } else {
                //root class was not specified, let's check the boolean fields
                //this will run only of exactly one field is true, and the corresponding action will be used to find root class
                return TestActionsUtils.executeIfExactlyOneIsTrue(rootClassAnnotation, List.of(
                        new PredicateExecutor<SetRootClass, Class<?>>(
                                "useTestClassAsRoot",
                                SetRootClass::useTestClassAsRoot,
                                () -> testClass
                        ),
                        new PredicateExecutor<SetRootClass, Class<?>>(
                                "findRootClassOnClasspath",
                                SetRootClass::findRootClassOnClasspath,
                                this::findRootClassOnClasspath
                        )
                ));
            }
        } else {
            log.debug("No '{}' test annotation was found, using test class as default root class '{}'",
                    SetRootClass.class.getName(), testClass.getName());
            return testClass;
        }
    }

    private Class<?> findRootClassOnClasspath() {
        return null; //TODO
    }

    private String[] findCommandLineArguments(Class<?> testClass) {
        return annotationScanner.scanOneStrict(testClass, SetCommandLineArguments.class)
                .map(SetCommandLineArguments::value)
                .orElseGet(() -> {
                    log.debug("No '{}' test annotation was found, not setting any command line arguments", SetCommandLineArguments.class.getName());
                    return new String[] {};
                });
    }

    /*
    Profiles and properties are set through the System properties. I'm not sure if this is a great approach,
    but it is what seems to be best, considering all scanners that the framework supports.
     */

    private void placeProfilesForApplication(Class<?> testClass) {
        annotationScanner.scan(testClass, SetProfiles.class).forEach(profilesAnnotation -> {
            var profiles = Arrays.asList(profilesAnnotation.value());
            if(profiles.isEmpty()) return;
            log.debug("The following profiles will be activated by '{}' test annotation: {}", SetProfiles.class.getName(), profiles);

            String profilesActivated = String.join(",", profiles);
            try {
                String profilesInProperty = systemPropertyReader.readSystemProperty(PROFILES_SYSTEM_PROPERTY);
                log.debug("System property '{}' already set, appending new values '{}'", PROFILES_SYSTEM_PROPERTY, profilesActivated);
                profilesInProperty += "," + profilesActivated;
                System.setProperty(PROFILES_SYSTEM_PROPERTY, profilesInProperty);
            } catch (SystemPropertyNotFoundException e) {
                log.debug("System property '{}' not set, adding value '{}'", PROFILES_SYSTEM_PROPERTY, profilesActivated);
                System.setProperty(PROFILES_SYSTEM_PROPERTY, profilesActivated);
            }
        });
    }

    private void placePropertiesForApplication(Class<?> testClass) {
        annotationScanner.scan(testClass, SetProperties.class).forEach(propertiesAnnotation -> {
            var properties = Arrays.asList(propertiesAnnotation.value());
            if(properties.isEmpty()) return;

            properties.forEach(property -> {
                SeparatedProperty separatedProperty = PropertyParsingUtils.separateNameValue(property);
                log.debug("The following property will be set by the '{}' test annotation: {} = {}",
                        SetProperties.class.getName(), separatedProperty.name(), separatedProperty.value());

                //if it exists, it will simply be overridden
                System.setProperty(PROPERTY_PREFIX + separatedProperty.name(), separatedProperty.value());
            });
        });
    }

    private void placeElementSettingsForApplication(Class<?> testClass) {
        //TODO
    }

}
