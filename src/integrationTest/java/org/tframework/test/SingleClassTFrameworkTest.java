/* Licensed under Apache-2.0 2024. */
package org.tframework.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.extension.ExtendWith;
import org.tframework.core.elements.scanner.InternalElementClassScanner;
import org.tframework.core.elements.scanner.RootElementClassScanner;
import org.tframework.core.properties.parsers.PropertyParsingUtils;

@ExtendWith(TFrameworkExtension.class)
@Properties({
        InternalElementClassScanner.TFRAMEWORK_INTERNAL_PACKAGE_PROPERTY + PropertyParsingUtils.PROPERTY_NAME_VALUE_SEPARATOR + "false",
        RootElementClassScanner.ROOT_HIERARCHY_SCANNING_ENABLED_PROPERTY + PropertyParsingUtils.PROPERTY_NAME_VALUE_SEPARATOR + "false",
        InternalElementClassScanner.TFRAMEWORK_INTERNAL_PACKAGE_PROPERTY + PropertyParsingUtils.PROPERTY_NAME_VALUE_SEPARATOR + "false",
})
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SingleClassTFrameworkTest {
}
