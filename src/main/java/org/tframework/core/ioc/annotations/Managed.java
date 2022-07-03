/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.tframework.core.ioc.ManagingType;

/**
 * Marks a class to be managed by the TFramework. Such a class will be injectable as a dependency
 * into other managed classes.
 * <p>
 * In a managed class, you also have the ability to define lifecycle callbacks with annotations
 * such as {@link AfterConstruct}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Managed {

    /**
     * This value is used as name of the managed entity, if the user does not specify a custom value.
     */
    String DEFAULT_MANAGED_NAME = "";

    /**
     * Decides the management type of this class. Default is {@link ManagingType#SINGLETON}.
     */
    ManagingType managedType() default ManagingType.SINGLETON;

    /**
     * Base name for the managed entity. It will have a default value created from the class name of the
     * managed entity. For example, in case of managing a String, the default name is 'java.lang.String'.
     * <p>
     * Names must be unique for each managed entity, and since they are generated from the class
     * name, this means that in case you have multiple managed classes of the same type, you must
     * specify a custom name for them.
     * <p>
     * The name is only allowed to contain alphanumeric characters and '.' and '-' characters.
     */
    String name() default DEFAULT_MANAGED_NAME;
}
