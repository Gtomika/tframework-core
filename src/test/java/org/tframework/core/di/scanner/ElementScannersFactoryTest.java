/* Licensed under Apache-2.0 2024. */
package org.tframework.core.di.scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.Set;
import org.junit.jupiter.api.Test;
import org.tframework.core.di.DependencyInjectionInput;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainer;

public class ElementScannersFactoryTest {

    @Test
    public void shouldCreateDefaultElementClassScanners() {
        var input = new DependencyInjectionInput(this.getClass(), ProfilesContainer.empty(), PropertiesContainer.empty());
        var scanners = ElementScannersFactory.getDefaultElementClassScanners(input);

        assertEquals(4, scanners.size());
        assertInstanceOf(RootElementClassScanner.class, scanners.get(0));
        assertInstanceOf(InternalElementClassScanner.class, scanners.get(1));
        assertInstanceOf(PackagesElementClassScanner.class, scanners.get(2));
        assertInstanceOf(ClassesElementClassScanner.class, scanners.get(3));
    }

    @Test
    public void shouldCreateDefaultElementMethodScanners() {
        var scanners = ElementScannersFactory.getDefaultElementMethodScanners(Set.of());

        assertEquals(1, scanners.size());
        assertInstanceOf(FixedClassesElementMethodScanner.class, scanners.getFirst());
    }

}
