/*
Copyright 2024 Tamas Gaspar

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
package org.tframework.core.elements.postprocessing;

import java.lang.reflect.Method;
import java.util.List;
import org.tframework.core.TFrameworkException;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.postprocessing.annotations.PostInitialization;
import org.tframework.core.utils.LogUtils;

/**
 * Thrown by {@link PostInitializationMethodPostProcessor} if it detects an invalid {@link PostInitialization}
 * method, or fails to execute an otherwise valid one.
 */
public class PostInitializationMethodException extends TFrameworkException {

    private static final String TEMPLATE = "Failed to invoke @PostInitialization method '%s' of element '%s': %s";

    /**
     * Creates an exception which describes why the {@link PostInitialization} method was invalid.
     * @param method The {@link PostInitialization} method that was invalid.
     * @param elementContext The {@link ElementContext} to which {@code method} belongs.
     * @param problems A list of problems found that made this method invalid.
     */
    public PostInitializationMethodException(Method method, ElementContext elementContext, List<String> problems) {
        super(TEMPLATE.formatted(
                LogUtils.niceExecutableName(method),
                elementContext.getName(),
                String.join(", ", problems))
        );
    }

    /**
     * Creates an exception which describes why the {@link PostInitialization} method failed to be executed.
     * @param method The {@link PostInitialization} method that could not be executed.
     * @param elementContext The {@link ElementContext} to which {@code method} belongs.
     * @param cause The exception that caused the method to fail.
     */
    public PostInitializationMethodException(Method method, ElementContext elementContext, Exception cause) {
        super(TEMPLATE.formatted(
                LogUtils.niceExecutableName(method),
                elementContext.getName(),
                cause.getMessage()
        ), cause);
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
