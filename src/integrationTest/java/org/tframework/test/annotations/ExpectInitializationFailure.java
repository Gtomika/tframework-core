package org.tframework.test.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Used in conjunction with {@link org.tframework.test.TFrameworkExtension} to indicate that
 * the framework initialization is expected to fail. This may be used when testing initialization
 * failures.
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface ExpectInitializationFailure {
}
