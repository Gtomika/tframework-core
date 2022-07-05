package org.tframework.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation marks the root class (or interface, etc...) of a TFramework application. This annotation is
 * required to be present on exactly one class in the classpath.
 * <p>
 * Operation performed by the framework (such as scanning for managed classes, injecting dependencies) will
 * only happen in:
 * <ul>
 *     <li>In the package of the class annotated with this.</li>
 *     <li>In the subpackages (applied recursively) of the package mentioned above.</li>
 * </ul>
 * <p>
 * Usually this annotation is placed on the same class which calls {@link TFramework#start(String[])}, because this
 * class is at the top of the package hierarchy. However, it can be on any class, but note that operations will only
 * be performed in the package and subpackages of this class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TFrameworkRoot {}
