/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.scanner;

import java.lang.reflect.Method;
import lombok.Setter;

/**
 * A top level abstraction for a scanner that is able to find elements from methods.
 * These scanners support setting a class to scan.
 */
@Setter
public abstract class ElementMethodScanner implements ElementScanner<Method> {

    protected Class<?> classToScan;

}
