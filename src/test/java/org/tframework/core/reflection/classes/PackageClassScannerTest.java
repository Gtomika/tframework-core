/*
Copyright 2023 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.core.reflection.classes;

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
        String packageName = ClassScanner.class.getPackageName();
        var packageClassScanner = new PackageClassScanner(Set.of(packageName));

        Set<Class<?>> classes = packageClassScanner.scanClasses();
        log.info("Found {} classes in package '{}'", classes.size(), packageName);

        //there may be more classes, but these are guaranteed to be there
        assertTrue(classes.stream().anyMatch(clazz -> clazz.getName().equals(ClassScanner.class.getName())));
        assertTrue(classes.stream().anyMatch(clazz -> clazz.getName().equals(PackageClassScanner.class.getName())));
    }

    @Test
    public void shouldFindClasses_whenPackageInJar() {
        //this package is in a JAR file when the test runs.
        String packageName = Logger.class.getPackageName();
        var packageClassScanner = new PackageClassScanner(Set.of(packageName));

        Set<Class<?>> classes = packageClassScanner.scanClasses();
        log.info("Found {} classes in package inside JAR '{}'", classes.size(), packageName);

        //there may be more classes, but these are guaranteed to be there
        assertTrue(classes.stream().anyMatch(clazz -> clazz.getName().equals(Logger.class.getName())));
        assertTrue(classes.stream().anyMatch(clazz -> clazz.getName().equals(MDC.class.getName())));

        //this is an inner class
        assertTrue(classes.stream().anyMatch(clazz -> clazz.getName().equals(MDC.MDCCloseable.class.getName())));
    }


}
