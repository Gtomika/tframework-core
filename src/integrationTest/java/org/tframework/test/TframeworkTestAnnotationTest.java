package org.tframework.test;

import org.junit.jupiter.api.Test;
import org.tframework.core.Application;
import org.tframework.test.annotations.BasePackage;
import org.tframework.test.annotations.TFrameworkTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@BasePackage("org.tframework.test")
@TFrameworkTest
public class TframeworkTestAnnotationTest {

    @Test
    public void shouldLaunchFullApplication(Application application) {
        assertEquals(DummyRootClass.class, application.getRootClass());
    }

}
