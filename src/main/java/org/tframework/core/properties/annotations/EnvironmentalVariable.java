package org.tframework.core.properties.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A specialized property injector, which is used to inject environmental
 * variables as fields and parameters.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface EnvironmentalVariable {

    /**
     * The name of the environmental variable to be injected.
     */
    String value();

}
