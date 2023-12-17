package org.tframework.core.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@interface RepeatedTestAnnotationA {
    TestAnnotationA[] value();
}
