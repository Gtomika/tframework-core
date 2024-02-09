/* Licensed under Apache-2.0 2024. */
package org.tframework.core;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.tframework.core.elements.annotations.Element;

/**
 * Annotation to mark the root class with. This annotation must be placed on the class that will be passed
 * to {@link TFramework#start(String, Class, String[])}. It can be part of a composed annotation. As a result of
 * this annotation, the root class will be marked as a singleton element.
 */
@Element
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TFrameworkRootClass {
}
