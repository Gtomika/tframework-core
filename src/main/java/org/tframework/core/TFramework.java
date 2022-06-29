/* Licensed under Apache-2.0 2022. */
package org.tframework.core;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * The core class of the framework, which includes methods to start and stop a TFramework
 * application.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TFramework {

  /**
   * Start the TFramework application. This method is intended to be called from the {@code main}
   * method.
   *
   * @param args Command line arguments, as received in the {@code main} method.
   */
  public static void start(String[] args) {}

  /** Stops the TFramework application gracefully. */
  public static void stop() {}
}
