package org.tframework.core.annotations;

import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@Repeatable(RepeatedTestAnnotationA.class)
@interface TestAnnotationA {
    String value();
}
