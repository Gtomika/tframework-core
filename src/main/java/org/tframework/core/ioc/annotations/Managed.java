/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.tframework.core.ioc.ManagedType;

/**
 * Marks a class to be managed by the TFramework. Such a class will be injectable as a dependency
 * into other managed classes, and vice versa.
 * <p>
 * In a managed class, you also have the ability to define lifecycle callbacks with annotations
 * such as {@link AfterConstruct} and {@link BeforeDestruct}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Managed {

    String UNSPECIFIED_ENTITY_NAME = "";

    /**
     * Decides the management type of this class. Default is {@link ManagedType#SINGLETON}.
     * @see ManagedType
     */
    ManagedType type() default ManagedType.SINGLETON;

    /**
     * Name for the managed entity. A default value will be derived from the class of the entity.
     * For example, in case of managing a String, the default name is 'java.lang.String'. This means that custom
     * names must be defined when multiple entities with the same class are used.
     * <p>
     * Names must be unique for each managed entity. Only allowed to contain letters and the '-' character.
     */
    String name() default UNSPECIFIED_ENTITY_NAME;
}
