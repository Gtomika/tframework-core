package org.tframework.core.test.junit5;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

/**
 * Initializes a "quarantined" TFramework application that only uses the test class. Only the managed entities
 * declared inside the test class will be added.
 * TODO: first finish creating {@link org.tframework.core.ioc.scan.QuarantinedManageEntityScanner}.
 * TODO: first finish implementing the property system.
 * TODO: create meta annotation that combines @ExtendWith(this) and {@link org.tframework.core.TFrameworkRoot}.
 */
public class TFrameworkJunit5Extension implements TestInstancePostProcessor {

    /**
     * Callback for post-processing the supplied test instance. For TFramework, it will load a special application context
     * that only contains managed entities that are declared in the test class as nested classes.
     */
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        //TODO: set property to control managed entity scanner, and set it to QuarantinedManageEntityScanner
        //TODO: initialize the TFramework app
    }
}
