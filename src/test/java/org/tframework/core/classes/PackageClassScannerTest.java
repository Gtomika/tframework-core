/* Licensed under Apache-2.0 2023. */
package org.tframework.core.classes;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.MDC;

@Slf4j
class PackageClassScannerTest {

    @Test
    public void shouldFindClasses_whenPackageNotInJar() {
        //this package is not in a JAR file when the test runs.
        String packageName = "org.tframework.core.classes";
        var packageClassScanner = new PackageClassScanner(Set.of(packageName));

        var classes = packageClassScanner.scanClasses();
        log.info("Found {} classes in package '{}'", classes.size(), packageName);

        //there may be more classes, but these are guaranteed to be there
        assertTrue(classes.stream().anyMatch(clazz -> clazz.getName().equals(ClassScanner.class.getName())));
        assertTrue(classes.stream().anyMatch(clazz -> clazz.getName().equals(PackageClassScanner.class.getName())));
    }

    @Test
    public void shouldFindClasses_whenPackageInJar() {
        //this package is in a JAR file when the test runs.
        String packageName = "org.slf4j";
        var packageClassScanner = new PackageClassScanner(Set.of(packageName));

        var classes = packageClassScanner.scanClasses();
        log.info("Found {} classes in package inside JAR '{}'", classes.size(), packageName);

        //there may be more classes, but these are guaranteed to be there
        assertTrue(classes.stream().anyMatch(clazz -> clazz.getName().equals(Logger.class.getName())));
        assertTrue(classes.stream().anyMatch(clazz -> clazz.getName().equals(MDC.class.getName())));

        //this is an inner class
        assertTrue(classes.stream().anyMatch(clazz -> clazz.getName().equals(MDC.MDCCloseable.class.getName())));
    }


}
