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
package org.tframework.core.elements.scanner;

import org.tframework.core.elements.annotations.Element;

/**
 * The result of various element scanners operations.
 * @param elementAnnotation The {@link Element} annotation found on the element.
 * @param annotationSource The component that the {@link Element} annotation was found on.
 * @param <T> Type of the annotation source component.
 * @see ElementScanner
 */
public record ElementScanningResult<T>(
        Element elementAnnotation,
        T annotationSource
) {
}
