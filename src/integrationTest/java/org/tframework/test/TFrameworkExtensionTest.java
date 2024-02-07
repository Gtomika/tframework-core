/* Licensed under Apache-2.0 2024. */
package org.tframework.test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.tframework.core.Application;
import org.tframework.test.annotations.SetApplicationName;
import org.tframework.test.annotations.SetElements;
import org.tframework.test.annotations.SetProfiles;
import org.tframework.test.annotations.SetProperties;
import org.tframework.test.annotations.SetRootClass;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SetProfiles("test")
@SetProperties("cool.prop=123")
@SetApplicationName("myCoolTestApp")
@SetRootClass(rootClass = TFrameworkExtensionTest.class)
@SetElements(
        rootScanningEnabled = true,
        rootHierarchyScanningEnabled = false,
        internalScanningEnabled = false
)
@ExtendWith(TFrameworkExtension.class)
public class TFrameworkExtensionTest {

    @Test
    public void shouldRun(Application application) {
        assertEquals("myCoolTestApp", application.getName());
        assertEquals(TFrameworkExtensionTest.class, application.getRootClass());

        assertTrue(application.getProfilesContainer().isProfileSet("test"));
        assertEquals("123", application.getPropertiesContainer().getPropertyValue("cool.prop"));
    }

}