package org.tframework.core.ioc.annotations;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.tframework.core.properties.annotations.EnvironmentalVariable;
import org.tframework.core.properties.annotations.Property;

import java.lang.annotation.Annotation;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InjectingAnnotations {

    /**
     * Contains all the annotations that can be used to inject into a field or parameter.
     */
    @Getter
    public static final List<Class<? extends Annotation>> injectingAnnotations =
            List.of(Injected.class, Property.class, EnvironmentalVariable.class);

}
