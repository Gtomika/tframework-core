package org.tframework.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@TestAnnotationA("A on B")
@Retention(RetentionPolicy.RUNTIME)
@interface TestAnnotationB {
    String value();
}
