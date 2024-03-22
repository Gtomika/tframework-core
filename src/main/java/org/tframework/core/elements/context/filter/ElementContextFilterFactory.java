/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.filter;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.tframework.core.elements.ElementsInitializationInput;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.reflection.annotations.AnnotationScannersFactory;

/**
 * Creates {@link ElementContextFilter}s and {@link ElementContextFilterAggregator}s.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ElementContextFilterFactory {

    /**
     * Creates an {@link ElementContextFilterAggregator} with all the default filters of the framework.
     * @param input {@link ElementsInitializationInput} with all data required to create the filters.
     */
    public static ElementContextFilterAggregator createDefaultElementContextFilterAggregator(ElementsInitializationInput input) {
        List<ElementContextFilter> filters = List.of(
                createProfileElementContextFilter(input.application().getProfilesContainer())
        );
        return ElementContextFilterAggregator.usingFilters(filters);
    }

    private static ProfileElementContextFilter createProfileElementContextFilter(ProfilesContainer profilesContainer) {
        var annotationScanner = AnnotationScannersFactory.createComposedAnnotationScanner();
        return new ProfileElementContextFilter(profilesContainer, annotationScanner);
    }

}
