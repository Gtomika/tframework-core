package org.tframework.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@TestCircularAnnotationA
@Retention(RetentionPolicy.RUNTIME)
public @interface TestCircularAnnotationB {
}
