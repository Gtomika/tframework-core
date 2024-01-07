/**
 * This package contains the classes that search for {@link org.tframework.core.di.annotations.Element}s.
 * There are two types of scanners:
 * <ul>
 *     <li>{@link org.tframework.core.di.scanner.ElementClassScanner}s: these scan for classes to find elements.</li>
 *     <li>
 *         {@link org.tframework.core.di.scanner.ElementMethodScanner}s: these scan for methods inside elements to find
 *         additional elements.
 *     </li>
 * </ul>
 * These scanners can be enabled or disabled by setting the appropriate properties. Each scanner has a reasonable
 * default value for its property, so that it can be used without any configuration. For the defaults, see the
 * individual scanner classes.
 */
@TFrameworkInternal
package org.tframework.core.di.scanner;

import org.tframework.core.TFrameworkInternal;
