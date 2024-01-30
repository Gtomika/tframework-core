/* Licensed under Apache-2.0 2023. */
package org.tframework.core.reflection.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@TestAnnotationA("A on B")
@Retention(RetentionPolicy.RUNTIME)
@interface TestAnnotationB {
    String value();
}
