/* Licensed under Apache-2.0 2024. */
package org.tframework.test.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to mark a test parameter to inject the exception that caused the framework
 * to fail initialization. This may be used to check negative cases, where the
 * framework initialization is expected to fail. The parameter annotated with
 * this must be an {@link Exception}.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectInitializationException {
}
