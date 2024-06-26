/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@TestSelfAnnotation("TestSelfAnnotation on TestSelfAnnotation")
@Retention(RetentionPolicy.RUNTIME)
public @interface TestSelfAnnotation {
    String value();
}
