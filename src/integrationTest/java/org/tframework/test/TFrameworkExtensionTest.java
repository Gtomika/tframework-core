/* Licensed under Apache-2.0 2024. */
package org.tframework.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.tframework.core.Application;

@Profiles("test")
@Properties("cool.prop=123")
@ApplicationName("myCoolTestApp")
@RootClass(TFrameworkExtensionTest.class)
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
