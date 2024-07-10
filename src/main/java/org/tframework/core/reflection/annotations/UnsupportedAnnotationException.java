/*
Copyright 2023 Tamas Gaspar

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
package org.tframework.core.reflection.annotations;

import java.lang.annotation.Annotation;
import org.tframework.core.TFrameworkException;

/**
 * This exception is thrown when the composed annotation scanning encounters
 * an unsupported annotation that it cannot scan for.
 * @see ComposedAnnotationScanner
 */
public class UnsupportedAnnotationException extends TFrameworkException {

    private static final String TEMPLATE = "Composed annotation scanning is not supported for annotation '%s'";

    public UnsupportedAnnotationException(Class<? extends Annotation> annotationClass) {
        super(TEMPLATE.formatted(annotationClass.getName()));
    }

    @Override
    public String getMessageTemplate() {
        return TEMPLATE;
    }
}
