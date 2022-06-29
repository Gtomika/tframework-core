/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc.exceptions;

import org.tframework.core.exceptions.TFrameworkRuntimeException;

public class NoSuchManagedEntityException extends TFrameworkRuntimeException {

  public NoSuchManagedEntityException(Class<?> clazz) {
    super(String.format("No managed entity of type '%s' was found.", clazz.getName()));
  }

  public NoSuchManagedEntityException(String name) {
    super(String.format("No managed entity found with name '%s' was found.", name));
  }
}
