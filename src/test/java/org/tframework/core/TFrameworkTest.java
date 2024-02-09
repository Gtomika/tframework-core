/* Licensed under Apache-2.0 2024. */
package org.tframework.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.ElementConstructor;
import org.tframework.core.elements.annotations.InjectElement;

//TODO: change to using mocking here when this functionality is covered in integration tests
@TFrameworkRootClass
public class TFrameworkTest {

    @Test
    public void shouldInitializeFramework() {
        String[] args = new String[] {
                "tframework.profiles=unit-test,demo",
                //this custom properties file instruct the framework to only scan elements from this class
                "tframework.propertyFiles=properties/tframework-unittest-properties.yaml"
        };
        var application = TFramework.start("testApplication", TFrameworkTest.class, args);

        assertEquals("testApplication", application.getName());
        assertEquals(TFrameworkTest.class, application.getRootClass());

        assertTrue(application.getProfilesContainer().isProfileSet("default"));
        assertTrue(application.getProfilesContainer().isProfileSet("unit-test"));
        assertTrue(application.getProfilesContainer().isProfileSet("demo"));

        assertEquals("test", application.getPropertiesContainer().getPropertyValue("custom.string-prop"));
        assertEquals("1", application.getPropertiesContainer().getPropertyValue("custom.int-prop"));
        assertEquals("true", application.getPropertiesContainer().getPropertyValue("custom.boolean-prop"));
        assertEquals(
                List.of("test1", "test2", "test3"),
                application.getPropertiesContainer().getPropertyValueList("custom.list-prop")
        );

        assertTrue(application.getElementsContainer().hasElementContext(TFrameworkTest.class)); //the root class
        assertTrue(application.getElementsContainer().hasElementContext(DummyElement.class)); //from element annotations
        assertTrue(application.getElementsContainer().hasElementContext("importantInteger"));
        assertTrue(application.getElementsContainer().hasElementContext(Application.class)); //pre-constructed
    }

    @Element
    public static class DummyElement {

        @ElementConstructor
        public DummyElement() {}

        @Element(name = "importantInteger")
        public Integer dummyElementMethod() {
            return 1;
        }

    }

    @Element
    public static class FakeElement {

        public FakeElement(@InjectElement("importantInteger") Integer importantInteger) {
        }

    }

}
