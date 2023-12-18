/* Licensed under Apache-2.0 2023. */
package org.tframework.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@TestCircularAnnotationB
@Retention(RetentionPolicy.RUNTIME)
public @interface TestCircularAnnotationA {
}
