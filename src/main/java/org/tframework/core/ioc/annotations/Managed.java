package org.tframework.core.ioc.annotations;

import org.tframework.core.ioc.ManagedType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Marks a class to be managed by the TFramework. Such a class will be injectable
 * as a dependency into other managed classes.
 * <p>
 * In a managed class, you also have the ability to define lifecycle callbacks with
 * annotations such as {@link AfterConstruct}.
 */
@Target(ElementType.TYPE)
public @interface Managed {

    /**
     * Decides the management type of this class. Default is
     * {@link ManagedType#SINGLETON}.
     */
    ManagedType managedType() default ManagedType.SINGLETON;

    /**
     * Base name for the managed entity. It will have a default value for each type:
     * <ul>
     *     <li>
     *         For singletons: name of the class of this managed entity. For example if the managed
     *         class is a {@link String}, it will be 'java.lang.String'.
     *     </li>
     *     <li>
     *         For multi instances: same as for singleton but the final will be appended with an integer, to keep it unique.
     *         For example if the managed class is a {@link java.util.BitSet}, the managed instances will be called
     *         'java.util.BitSet-1', 'java.util.BitSet-2'
     *     </li>
     * </ul>
     * <p>
     * Names must be unique for each managed entity, and since they are generated from the class name, this means that
     * in case you have multiple managed classes of the same type, you must specify a custom name for them.
     */
    String name() default "";

}
