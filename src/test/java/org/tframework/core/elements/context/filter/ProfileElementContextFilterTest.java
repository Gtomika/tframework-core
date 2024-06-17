/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements.context.filter;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.tframework.core.Application;
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
        filter = new ProfileElementContextFilter(annotationScanner);
        when(elementSource.annotatedSource()).thenReturn(ELEMENT_CLASS);
        when(elementContext.getSource()).thenReturn(elementSource);
        when(elementContext.getName()).thenReturn("test element");
    }

    @Test
    public void shouldKeepElement_whenNoRequiredAndForbiddenProfiles() {
        var app = applicationWithProfiles(Set.of("a", "b"));
        mockScannerToReturnRequiredProfilesAnnotation();
        mockScannerToReturnForbiddenProfilesAnnotation();

        assertFalse(filter.discardElementContext(elementContext, app));
    }

    @Test
    public void shouldKeepElement_whenRequiredProfiles_arePresent() {
        var app = applicationWithProfiles(Set.of("a", "b"));
        mockScannerToReturnRequiredProfilesAnnotation("a", "b");
        mockScannerToReturnForbiddenProfilesAnnotation();

        assertFalse(filter.discardElementContext(elementContext, app));
    }

    @Test
    public void shouldKeepElement_whenForbiddenProfiles_areAbsent() {
        var app = applicationWithProfiles(Set.of());
        mockScannerToReturnRequiredProfilesAnnotation();
        mockScannerToReturnForbiddenProfilesAnnotation("a", "b");

        assertFalse(filter.discardElementContext(elementContext, app));
    }

    @Test
    public void shouldDiscardElement_whenRequiredProfiles_areNotPresent() {
        var app = applicationWithProfiles(Set.of("a"));
        mockScannerToReturnRequiredProfilesAnnotation("a", "b");
        mockScannerToReturnForbiddenProfilesAnnotation();

        assertTrue(filter.discardElementContext(elementContext, app));
    }

    @Test
    public void shouldDiscardElement_whenForbiddenProfiles_arePresent() {
        var app = applicationWithProfiles(Set.of("a", "b"));
        mockScannerToReturnRequiredProfilesAnnotation();
        mockScannerToReturnForbiddenProfilesAnnotation("b");

        assertTrue(filter.discardElementContext(elementContext, app));
    }

    @Test
    public void shouldDiscardElement_whenForbiddenProfiles_arePresent_andRequiredProfiles_areNotPresent() {
        var app = applicationWithProfiles(Set.of("a", "b"));
        mockScannerToReturnRequiredProfilesAnnotation("c");
        mockScannerToReturnForbiddenProfilesAnnotation("b");

        assertTrue(filter.discardElementContext(elementContext, app));
    }

    private Application applicationWithProfiles(Set<String> profiles) {
       var container = ProfilesContainer.fromProfiles(profiles);
       return Application.builder()
               .profilesContainer(container)
               .build();
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
