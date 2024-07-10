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
package org.tframework.core.events.exception;

import java.lang.reflect.Method;
import java.util.List;
import lombok.Getter;
import org.tframework.core.TFrameworkException;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.events.annotations.Subscribe;

/**
 * Thrown when an error occurs while trying to subscribe a {@link Subscribe}
 * method of an element.
 */
@Getter
public class EventSubscriptionException extends TFrameworkException {

    private static final String TEMPLATE = "Failed to subscribe element context '%s', method '%s': %s";

    private final ElementContext elementContext;
    private final Method method;
    private final List<String> errors;

    public EventSubscriptionException(ElementContext elementContext, Method method, List<String> errors) {
        super(TEMPLATE.formatted(elementContext.getName(), method.getName(), String.join(", ", errors)));
        this.elementContext = elementContext;
        this.method = method;
        this.errors = errors;
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
