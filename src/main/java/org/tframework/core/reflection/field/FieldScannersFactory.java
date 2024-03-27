/* Licensed under Apache-2.0 2024. */
package org.tframework.core.reflection.field;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FieldScannersFactory {

    public static FieldScanner createDefaultFieldScanner() {
        return new SimpleFieldScanner();
    }
}
