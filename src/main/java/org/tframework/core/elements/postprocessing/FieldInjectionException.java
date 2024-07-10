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

import java.lang.reflect.Field;
import java.util.List;
import org.tframework.core.TFrameworkException;
import org.tframework.core.elements.context.ElementContext;

/**
 * Thrown when {@link FieldInjectionPostProcessor} encounters issues or invalid data during field injection.
 */
public class FieldInjectionException extends TFrameworkException {

    private static final String TEMPLATE = "Failed to inject into the field '%s' of element '%s': %s";

    public FieldInjectionException(Field field, ElementContext elementContext, List<String > problems) {
        super(TEMPLATE.formatted(field.getName(), elementContext.getName(), String.join(", ", problems)));
    }

    public FieldInjectionException(Field field, ElementContext elementContext, Exception cause) {
        super(TEMPLATE.formatted(field.getName(), elementContext.getName(), cause.getMessage()), cause);
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
