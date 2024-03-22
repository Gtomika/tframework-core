/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.filter;

import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.filter.annotation.ForbiddenProfile;
import org.tframework.core.elements.context.filter.annotation.RequiredProfile;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.reflection.annotations.AnnotationScanner;

/**
 * An {@link ElementContextFilter} that can discard element contexts based on the profiles set.
 * This filter is controlled by annotations placed on the elements:
 * <ul>
 *     <li>To activate elements based on profiles: {@link RequiredProfile}</li>
 *     <li>To activate elements based on the lack of profiles: {@link ForbiddenProfile}</li>
 * </ul>
 */
@Slf4j
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class ProfileElementContextFilter implements ElementContextFilter {

    private final ProfilesContainer propertiesContainer;
    private final AnnotationScanner annotationScanner;

    @Override
    public boolean discardElementContext(ElementContext elementContext) {
        return !allRequiredProfilesPresent(elementContext)
                || !allForbiddenProfilesAbsent(elementContext);
    }

    private boolean allRequiredProfilesPresent(ElementContext elementContext) {
        var annotatedElementSource = elementContext.getSource().annotatedSource();
        var requiredProfiles = annotationScanner.scan(annotatedElementSource, RequiredProfile.class)
                .stream()
                .flatMap(requiredProfileAnnotation -> Arrays.stream(requiredProfileAnnotation.value()))
                .collect(Collectors.toSet());

        if(requiredProfiles.isEmpty()) {
            log.debug("Element context '{}' does not require any profiles", elementContext.getName());
            return true; //if there are no required profiles, this check is passed
        } else {
            log.debug("Element context '{}' requires the following profiles: {}", elementContext.getName(), requiredProfiles);
            return requiredProfiles.stream()
                    .allMatch(requiredProfile -> isRequiredProfilePresent(requiredProfile, elementContext));
        }
    }

    private boolean isRequiredProfilePresent(String requiredProfile, ElementContext elementContext) {
        if(propertiesContainer.isProfileSet(requiredProfile)) {
            return true;
        } else {
            log.debug("The element context '{}' requires profile '{}', but it isn't present",
                    elementContext.getName(), requiredProfile);
            return false;
        }
    }

    private boolean allForbiddenProfilesAbsent(ElementContext elementContext) {
        var annotatedElementSource = elementContext.getSource().annotatedSource();
        var forbiddenProfiles = annotationScanner.scan(annotatedElementSource, ForbiddenProfile.class)
                .stream()
                .flatMap(forbiddenProfileAnnotation -> Arrays.stream(forbiddenProfileAnnotation.value()))
                .collect(Collectors.toSet());

        if(forbiddenProfiles.isEmpty()) {
            log.debug("Element context '{}' does not forbid any profiles", elementContext.getName());
            return true;
        } else {
            log.debug("Element context '{}' forbids the following profiles: {}", elementContext.getName(), forbiddenProfiles);
            return forbiddenProfiles.stream()
                    .allMatch(forbiddenProfile -> isForbiddenProfileAbsent(forbiddenProfile, elementContext));
        }
    }

    private boolean isForbiddenProfileAbsent(String forbiddenProfile, ElementContext elementContext) {
        if(propertiesContainer.isProfileSet(forbiddenProfile)) {
            log.debug("The element context '{}' requires profile '{}' to be absent, but it is present",
                    elementContext.getName(), forbiddenProfile);
            return false;
        } else {
            return true;
        }
    }
}
