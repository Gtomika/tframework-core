/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.filter.annotation.ForbiddenProfile;
import org.tframework.core.elements.context.filter.annotation.RequiredProfile;
import org.tframework.core.elements.context.source.ElementSource;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.reflection.annotations.AnnotationScanner;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ProfileElementContextFilterTest {

    private static final Class<?> ELEMENT_CLASS = ProfileElementContextFilterTest.class;

    @Mock
    private ElementSource elementSource;

    @Mock
    private ElementContext elementContext;

    @Mock
    private AnnotationScanner annotationScanner;

    private ProfileElementContextFilter filter;

    @BeforeEach
    public void setUp() {
        when(elementSource.annotatedSource()).thenReturn(ELEMENT_CLASS);
        when(elementContext.getSource()).thenReturn(elementSource);
        when(elementContext.getName()).thenReturn("test element");
    }

    @Test
    public void shouldKeepElement_whenNoRequiredAndForbiddenProfiles() {
        initFilterWithProfiles();
        mockScannerToReturnRequiredProfilesAnnotation();
        mockScannerToReturnForbiddenProfilesAnnotation();

        assertFalse(filter.discardElementContext(elementContext));
    }

    @Test
    public void shouldKeepElement_whenRequiredProfiles_arePresent() {
        initFilterWithProfiles("a", "b");
        mockScannerToReturnRequiredProfilesAnnotation("a", "b");
        mockScannerToReturnForbiddenProfilesAnnotation();

        assertFalse(filter.discardElementContext(elementContext));
    }

    @Test
    public void shouldKeepElement_whenForbiddenProfiles_areAbsent() {
        initFilterWithProfiles();
        mockScannerToReturnRequiredProfilesAnnotation();
        mockScannerToReturnForbiddenProfilesAnnotation("a", "b");

        assertFalse(filter.discardElementContext(elementContext));
    }

    @Test
    public void shouldDiscardElement_whenRequiredProfiles_areNotPresent() {
        initFilterWithProfiles("a");
        mockScannerToReturnRequiredProfilesAnnotation("a", "b");
        mockScannerToReturnForbiddenProfilesAnnotation();

        assertTrue(filter.discardElementContext(elementContext));
    }

    @Test
    public void shouldDiscardElement_whenForbiddenProfiles_arePresent() {
        initFilterWithProfiles("a", "b");
        mockScannerToReturnRequiredProfilesAnnotation();
        mockScannerToReturnForbiddenProfilesAnnotation("b");

        assertTrue(filter.discardElementContext(elementContext));
    }

    @Test
    public void shouldDiscardElement_whenForbiddenProfiles_arePresent_andRequiredProfiles_areNotPresent() {
        initFilterWithProfiles("a", "b");
        mockScannerToReturnRequiredProfilesAnnotation("c");
        mockScannerToReturnForbiddenProfilesAnnotation("b");

        assertTrue(filter.discardElementContext(elementContext));
    }

    private void initFilterWithProfiles(String... profiles) {
        filter = new ProfileElementContextFilter(
                ProfilesContainer.fromProfiles(
                        Arrays.stream(profiles).collect(Collectors.toSet())
                ),
                annotationScanner
        );
    }

    private void mockScannerToReturnRequiredProfilesAnnotation(String... profiles) {
        if(profiles.length == 0) {
            when(annotationScanner.scan(ELEMENT_CLASS, RequiredProfile.class))
                    .thenReturn(List.of());
        } else {
            when(annotationScanner.scan(ELEMENT_CLASS, RequiredProfile.class))
                    .thenReturn(List.of(requiredProfileAnnotationWith(profiles)));
        }
    }

    private void mockScannerToReturnForbiddenProfilesAnnotation(String... profiles) {
        if(profiles.length == 0) {
            when(annotationScanner.scan(ELEMENT_CLASS, ForbiddenProfile.class))
                    .thenReturn(List.of());
        } else {
            when(annotationScanner.scan(ELEMENT_CLASS, ForbiddenProfile.class))
                    .thenReturn(List.of(forbiddenProfileAnnotationWith(profiles)));
        }
    }

    private RequiredProfile requiredProfileAnnotationWith(String... profiles) {
        return new RequiredProfile() {
            @Override
            public String[] value() {
                return profiles;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return RequiredProfile.class;
            }
        };
    }

    private ForbiddenProfile forbiddenProfileAnnotationWith(String... profiles) {
        return new ForbiddenProfile() {
            @Override
            public String[] value() {
                return profiles;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ForbiddenProfile.class;
            }
        };
    }

}
