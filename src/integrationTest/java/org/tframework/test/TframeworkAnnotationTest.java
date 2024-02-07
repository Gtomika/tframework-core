package org.tframework.test;

import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.core.TFrameworkRootClass;
import org.tframework.test.annotations.BasePackage;
import org.tframework.test.annotations.TFrameworkTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@BasePackage("org.tframework.test")
@TFrameworkTest
@TFrameworkRootClass //this class will also be the root class, but it will be found via classpath scanning
public class TframeworkAnnotationTest {

    @Test
    public void shouldLaunchFullApplication(Application application) {
        assertEquals(TframeworkAnnotationTest.class, application.getRootClass());
    }

}
