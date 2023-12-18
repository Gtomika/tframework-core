/* Licensed under Apache-2.0 2023. */
package org.tframework.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@TestAnnotationA("A on C")
@TestAnnotationB("B on C")
@Retention(RetentionPolicy.RUNTIME)
@interface TestAnnotationC {
    String value();
}
