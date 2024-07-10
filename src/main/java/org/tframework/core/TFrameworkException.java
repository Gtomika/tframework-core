/*
Copyright 2022 Tamas Gaspar

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.tframework.core;

/**
 *  Base class for all unchecked exceptions in TFramework. All exceptions declared in the framework
 *  should be subclasses of this one.
 */
public abstract class TFrameworkException extends RuntimeException {

    /**
     * Create an exception with a message.
     * @param message Message that should come from {@link #getMessageTemplate()} with formatting applied.
     */
    public TFrameworkException(String message) {
        super(message);
    }

    /**
     * Create an exception with a message and a cause.
     * @param message Message that should come from {@link #getMessageTemplate()} with formatting applied.
     * @param cause Another exception that was the direct cause of this one.
     */
    public TFrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Gets the message template of the exception. This must have the same rules as defined by
     * {@link String#format(String, Object...)}. It is allowed to have no placeholders in the template.
     */
    public abstract String getMessageTemplate();

}
