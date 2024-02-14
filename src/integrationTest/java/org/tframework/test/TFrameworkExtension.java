/* Licensed under Apache-2.0 2024. */
package org.tframework.test;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestInstanceFactory;
import org.junit.jupiter.api.extension.TestInstanceFactoryContext;
import org.junit.jupiter.api.extension.TestInstantiationException;
import org.slf4j.MDC;
import org.tframework.core.Application;
import org.tframework.core.TFramework;
import org.tframework.core.TFrameworkRootClass;
import org.tframework.core.elements.annotations.InjectElement;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.dependency.DependencyDefinition;
import org.tframework.core.elements.dependency.InjectAnnotationScanner;
import org.tframework.core.elements.dependency.graph.ElementDependencyGraph;
import org.tframework.core.elements.dependency.resolver.DependencyResolverAggregator;
import org.tframework.core.elements.dependency.resolver.DependencyResolversFactory;
import org.tframework.core.elements.scanner.ClassesElementClassScanner;
import org.tframework.core.elements.scanner.InternalElementClassScanner;
import org.tframework.core.elements.scanner.PackagesElementClassScanner;
import org.tframework.core.elements.scanner.RootElementClassScanner;
import org.tframework.core.readers.ReadersFactory;
import org.tframework.core.readers.SystemPropertyNotFoundException;
import org.tframework.core.readers.SystemPropertyReader;
import org.tframework.core.reflection.annotations.AnnotationScanner;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;
import org.tframework.test.annotations.ExpectInitializationFailure;
import org.tframework.test.annotations.InjectInitializationException;
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
import java.util.concurrent.Executors;

import static org.tframework.core.profiles.scanners.SystemPropertyProfileScanner.PROFILES_SYSTEM_PROPERTY;

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
 * There are some ways to get the launched {@link Application} object or any other element or property from the
 * application.
 * <ul>
 *     <li>They can be field injected into the test class, because the test class is an element.</li>
 *     <li>
 *         They can be added as a parameter to JUnit methods such as {@link org.junit.jupiter.api.BeforeEach} and {@link org.junit.jupiter.api.Test}.
 *         Parameters need to be annotated with {@code @InjectX} annotations such as {@link InjectElement}.
 *     </li>
 * </ul>
 *
 * <h3>Initialization failure</h3>
 * There are cases where the test expects the application initialization to fail. In these cases:
 * <ul>
 *     <li>Place the {@link ExpectInitializationFailure} annotation.</li>
 *     <li>
 *         The {@link InjectInitializationException} can be used on an {@link Exception} typed test method parameter to inject the
 *         exception that caused the failure, which can be asserted inside the test.
 *     </li>
 * </ul>
 * @see org.tframework.test.annotations.IsolatedTFrameworkTest
 * @see org.tframework.test.annotations.TFrameworkTest
 */
@Slf4j
public class TFrameworkExtension implements Extension, BeforeAllCallback, TestInstanceFactory, AfterAllCallback, ParameterResolver {

    private static final String SOURCE_ANNOTATION = "sourceAnnotation";

    private static final String TOO_MANY_ROOT_CLASSES_ERROR_TEMPLATE = "More than one class was found to annotated with '" +
            TFrameworkRootClass.class.getName() + "' on the classpath: %s";
    private static final String NO_ROOT_CLASS_ERROR = "No root class was found that is annotated with '" +
            TFrameworkRootClass.class.getName() + "', but exactly one is required.";
    private static final String UNEXPECTED_START_ERROR = "Application initialization was expected to fail, but it succeeded!";

    private static final int SCAN_THREAD_AMOUNT = 5;

    private final AnnotationScanner annotationScanner = AnnotationScannersFactory.createComposedAnnotationScanner();
    private final SystemPropertyReader systemPropertyReader = ReadersFactory.createSystemPropertyReader();
    private final InjectAnnotationScanner injectAnnotationScanner = new InjectAnnotationScanner(annotationScanner);
    private DependencyResolverAggregator dependencyResolverAggregator;

    private boolean successfulAppInitialization;
    private Application application;
    private ElementContext testClassElementContext;
    private Exception initializationException;

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        var testClass = extensionContext.getRequiredTestClass();
        setTestClassForElementScanning(testClass);

        placeProfilesForApplication(testClass);
        placePropertiesForApplication(testClass);
        placeElementSettingsForApplication(testClass);

        boolean expectFailure = detectExpectFailure(testClass);
        log.debug("Starting TFramework application from test class '{}'. Failure expected: {}", testClass.getName(), expectFailure);
        try {
            application = TFramework.start(
                    findApplicationName(testClass),
                    findRootClass(testClass),
                    findCommandLineArguments(testClass)
            );

            //these resolvers will be used to inject parameters into test methods
            dependencyResolverAggregator = DependencyResolverAggregator.usingResolvers(List.of(
                    DependencyResolversFactory.createElementDependencyResolver(application.getElementsContainer()),
                    DependencyResolversFactory.createPropertyDependencyResolver(application.getPropertiesContainer())
            ));

            successfulAppInitialization = true;
        } catch (Exception e) {
            log.warn("TFramework application failed to initialize", e);
            initializationException = e;
            successfulAppInitialization = false;
        }
        checkUnexpectedApplicationState(expectFailure, successfulAppInitialization);
    }

    @Override
    public Object createTestInstance(TestInstanceFactoryContext testInstanceFactoryContext, ExtensionContext extensionContext) throws TestInstantiationException {
        if(successfulAppInitialization) {
            testClassElementContext = application.getElementsContainer()
                    .getElementContext(extensionContext.getRequiredTestClass());
            log.debug("Found the test class element context '{}', requesting test instance...", testClassElementContext.getName());
            return testClassElementContext.requestInstance();
        } else {
            try {
                log.debug("Application initialization failed, so instance will be constructed normally");
                return testInstanceFactoryContext.getTestClass().getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new TestInstantiationException("Failed to create test instance", e);
            }
        }
    }

    @Override
    public void afterAll(ExtensionContext context) {
        if(successfulAppInitialization) {
            log.debug("Shutting down the '{}' application after all tests", application.getName());
            TFramework.stop(application);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if(parameterContext.getDeclaringExecutable() instanceof Constructor<?>) {
            log.error("Injecting the application into the test instance constructor is not supported, because " +
                    "the application is not yet started.");
            return false;
        }
        if(successfulAppInitialization) {
            return injectAnnotationScanner.hasAnyInjectAnnotations(parameterContext.getParameter());
        } else {
            return annotationScanner.hasAnnotation(parameterContext.getParameter(), InjectInitializationException.class);
        }
    }

    private static final String DECLARED_AS_TEST_PARAMETER = "JUnit 5 test method parameter";

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        if(successfulAppInitialization) {
            var definition = DependencyDefinition.fromParameter(parameterContext.getParameter());
            return dependencyResolverAggregator.resolveDependency(
                    definition,
                    testClassElementContext,
                    ElementDependencyGraph.empty(),
                    DECLARED_AS_TEST_PARAMETER
            );
        } else {
            return initializationException;
        }
    }

    private boolean detectExpectFailure(Class<?> testClass) {
        return annotationScanner.hasAnnotation(testClass, ExpectInitializationFailure.class);
    }

    private void checkUnexpectedApplicationState(boolean expectFailure, boolean applicationInitialized) throws Exception {
        if(applicationInitialized && expectFailure) {
            throw new IllegalStateException(UNEXPECTED_START_ERROR);
        }
        if(!applicationInitialized && !expectFailure) {
            //if the app expected to start, but did not, transiently rethrow the original exception
            throw initializationException;
        }
    }

    private void setTestClassForElementScanning(Class<?> testClass) {
        TestActionsUtils.setFrameworkPropertyIntoSystemProperties(
                ClassesElementClassScanner.SCAN_CLASSES_PROPERTY + "-junit-extension-test-class",
                List.of(testClass.getName())
        );
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
                        new PredicateExecutor<>(
                                "useTestClassAsRoot",
                                SetRootClass::useTestClassAsRoot,
                                () -> testClass
                        ),
                        new PredicateExecutor<>(
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
        ClassGraph classGraph = new ClassGraph()
                .enableClassInfo()
                .enableAnnotationInfo();

        try(var scanResult = classGraph.scan(Executors.newFixedThreadPool(SCAN_THREAD_AMOUNT), SCAN_THREAD_AMOUNT)) {
            var rootClassCandidates = scanResult.getAllClasses()
                    .filter(this::isClassDirectlyAnnotatedWithTframeworkRoot);

            if(rootClassCandidates.size() > 1) {
                throw new IllegalStateException(TOO_MANY_ROOT_CLASSES_ERROR_TEMPLATE.formatted(rootClassCandidates.getNames()));
            }
            if(rootClassCandidates.isEmpty()) {
                throw new IllegalStateException(NO_ROOT_CLASS_ERROR);
            }
            return rootClassCandidates.getFirst().loadClass();
        }
    }

    private boolean isClassDirectlyAnnotatedWithTframeworkRoot(ClassInfo info) {
        boolean isDirectlyAnnotated = info.getAnnotationInfo().directOnly().stream().anyMatch(annotationInfo -> {
            return annotationInfo.getName().equals(TFrameworkRootClass.class.getName());
        });
        return isDirectlyAnnotated && info.isStandardClass();
    }

    private String[] findCommandLineArguments(Class<?> testClass) {
        return annotationScanner.scanOneStrict(testClass, SetCommandLineArguments.class)
                .map(SetCommandLineArguments::value)
                .orElseGet(() -> {
                    log.debug("No '{}' test annotation was found, not setting any command line arguments", SetCommandLineArguments.class.getName());
                    return new String[] {};
                });
    }

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

            MDC.put(SOURCE_ANNOTATION, SetProperties.class.getName());
            properties.forEach(TestActionsUtils::setRawFrameworkPropertyIntoSystemProperties);
            MDC.remove(SOURCE_ANNOTATION);
        });
    }

    private void placeElementSettingsForApplication(Class<?> testClass) {
        annotationScanner.scanOneStrict(testClass, SetElements.class).ifPresent(setElementsAnnotation -> {
            MDC.put(SOURCE_ANNOTATION, SetElements.class.getName());

            TestActionsUtils.setFrameworkPropertyIntoSystemProperties(
                    RootElementClassScanner.ROOT_SCANNING_ENABLED_PROPERTY,
                    String.valueOf(setElementsAnnotation.rootScanningEnabled())
            );

            TestActionsUtils.setFrameworkPropertyIntoSystemProperties(
                    RootElementClassScanner.ROOT_HIERARCHY_SCANNING_ENABLED_PROPERTY,
                    String.valueOf(setElementsAnnotation.rootHierarchyScanningEnabled())
            );

            TestActionsUtils.setFrameworkPropertyIntoSystemProperties(
                    InternalElementClassScanner.TFRAMEWORK_INTERNAL_SCAN_ENABLED_PROPERTY,
                    String.valueOf(setElementsAnnotation.internalScanningEnabled())
            );

            TestActionsUtils.setFrameworkPropertyIntoSystemProperties(
                    PackagesElementClassScanner.SCAN_PACKAGES_PROPERTY,
                    Arrays.asList(setElementsAnnotation.scanAdditionalPackages())
            );

            TestActionsUtils.setFrameworkPropertyIntoSystemProperties(
                    ClassesElementClassScanner.SCAN_CLASSES_PROPERTY,
                    Arrays.asList(setElementsAnnotation.scanAdditionalClasses())
            );

            MDC.remove(SOURCE_ANNOTATION);
        });

    }

}
