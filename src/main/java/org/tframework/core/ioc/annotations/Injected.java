package org.tframework.core.ioc.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark fields or parameters to be injected by the framework from the
 * pool of the managed entities.
 * <p>
 * It's only possible to inject values into {@link Managed} entities.
 * <p>
 * By default, the type of the field/parameter hosting this annotation will decide which managed
 * entity will be injected. This can be overridden by specifying the entity name explicitly using
 * {@link #name()}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
public @interface Injected {

    /**
     * Name of the managed entity to inject. If you don't provide this, the managed entity will be
     * resolved by the type of the field/parameter.
     */
    String name() default Managed.DEFAULT_MANAGED_NAME;

}
