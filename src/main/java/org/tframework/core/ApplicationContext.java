/* Licensed under Apache-2.0 2022. */
package org.tframework.core;

import org.tframework.core.ioc.ManagedClasses;
import org.tframework.core.ioc.annotations.Managed;

/**
 * The singleton application context class, which stores information about the running TFramework
 * application, such as the managed classes.
 */
@Managed
public class ApplicationContext {

  private ManagedClasses managedClasses;
}
