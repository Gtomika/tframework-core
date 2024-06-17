/* Licensed under Apache-2.0 2024. */
package org.tframework.core.elements;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.io.File;
import java.lang.reflect.Method;
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
import org.tframework.core.elements.annotations.Element;
import org.tframework.core.elements.annotations.ElementConstructor;
import org.tframework.core.elements.context.ElementContext;
import org.tframework.core.elements.context.assembler.ClassElementContextAssembler;
import org.tframework.core.elements.context.assembler.MethodElementContextAssembler;
import org.tframework.core.elements.dependency.resolver.DependencyResolutionInput;
import org.tframework.core.elements.scanner.ElementClassScanner;
import org.tframework.core.elements.scanner.ElementContextBundle;
import org.tframework.core.elements.scanner.ElementMethodScanner;
import org.tframework.core.elements.scanner.ElementScanningResult;
import org.tframework.core.profiles.ProfilesContainer;
import org.tframework.core.properties.PropertiesContainer;
import org.tframework.core.properties.PropertiesContainerFactory;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ElementsInitializationProcessTest {

    @Mock
    private ClassElementContextAssembler classElementContextAssembler;

    @Mock
    private MethodElementContextAssembler methodElementContextAssembler;

    @Mock
    private ElementClassScanner elementClassScanner;

    @Mock
    private ElementMethodScanner elementMethodScanner;

    @Mock
    private ElementContext dummyClassElementContext;

    @Mock
    private ElementContext dummyStringMethodElementContext;

    private ElementsInitializationProcess elementsInitializationProcess;
    private ElementContextBundle elementContextBundle;
    private Method dummyStringMethod;

    @BeforeEach
    void setUp() throws Exception {
        when(dummyClassElementContext.getName()).thenReturn("dummyClassElement");
        doReturn(DummyClass.class).when(dummyClassElementContext).getType();

        when(dummyStringMethodElementContext.getName()).thenReturn("dummyMethodElement");
        doReturn(String.class).when(dummyStringMethodElementContext).getType();

        elementsInitializationProcess = new ElementsInitializationProcess();
        elementContextBundle = ElementContextBundle.builder()
                .elementClassScanners(List.of(elementClassScanner))
                .elementMethodScanners(List.of(elementMethodScanner))
                .classElementContextAssembler(classElementContextAssembler)
                .methodElementContextAssembler(methodElementContextAssembler)
                .build();
        dummyStringMethod = DummyClass.class.getDeclaredMethod("dummyStringCreator");
    }

    @Test
    public void shouldInitializeElementsProcess() {
        //mock class element scanning and assembling
        var classScanningResult = new ElementScanningResult<Class<?>>(DummyClass.class.getAnnotation(Element.class), DummyClass.class);
        when(elementClassScanner.scanElements())
                .thenReturn(Set.of(classScanningResult));
        when(classElementContextAssembler.assemble(eq(classScanningResult), any(DependencyResolutionInput.class)))
                .thenReturn(dummyClassElementContext);

        //mock method element scanning and assembling
        var methodScanningResult = new ElementScanningResult<>(dummyStringMethod.getAnnotation(Element.class), dummyStringMethod);
        when(elementMethodScanner.scanElements())
                .thenReturn(Set.of(methodScanningResult));
        when(methodElementContextAssembler.assemble(eq(methodScanningResult), any(DependencyResolutionInput.class)))
                .thenReturn(dummyStringMethodElementContext);

        var preConstructedElementsData = Set.of(PreConstructedElementData.builder()
                .preConstructedInstance(new File("."))
                .name("importantFile")
                .overrideExistingElement(false)
                .build());

        var input = createDependencyInjectionInput(PropertiesContainerFactory.empty(), preConstructedElementsData);
        var elementsContainer = elementsInitializationProcess.initialize(input, elementContextBundle);

        assertTrue(elementsContainer.hasElementContext("dummyClassElement")); //from element class
        assertTrue(elementsContainer.hasElementContext("dummyMethodElement"));
        assertTrue(elementsContainer.hasElementContext(Application.class)); //from DEFAULT pre-constructed elements
        assertTrue(elementsContainer.hasElementContext(ProfilesContainer.class));
        assertTrue(elementsContainer.hasElementContext(PropertiesContainer.class));
        assertTrue(elementsContainer.hasElementContext(ElementsContainer.class));
        assertTrue(elementsContainer.hasElementContext("importantFile")); //from CUSTOM pre-constructed elements
    }

    private ElementsInitializationInput createDependencyInjectionInput(
            PropertiesContainer propertiesContainer,
            Set<PreConstructedElementData> preConstructedElementData
    ) {
        Application application = Application.empty();
        application.setProfilesContainer(ProfilesContainer.empty());
        application.setPropertiesContainer(propertiesContainer);
        return ElementsInitializationInput.builder()
                .rootClass(ElementsInitializationProcessTest.class)
                .application(application)
                .preConstructedElementData(preConstructedElementData)
                .build();
    }

    /*
    This single element class and its method elements are used for testing the elements process.
     */
    @Element(name = "dummyClassElement")
    public static class DummyClass {

        @ElementConstructor
        public DummyClass() {}

        @Element(name = "dummyMethodElement")
        public String dummyStringCreator() {
            return "dummy";
        }

    }

}
