package org.tframework.core;

import org.tframework.core.elements.annotations.Element;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to mark the root class with. This will make the root class an element.
 */
@Element
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TFrameworkRootClass {
}
