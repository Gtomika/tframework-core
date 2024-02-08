package org.tframework.core;

import org.tframework.core.elements.annotations.Element;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
