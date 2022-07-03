package org.tframework.core.ioc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.tframework.core.ioc.exceptions.NotConstructibleException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * This class is responsible for creating instances of the managed classes. There are multiple ways
 * how a managed entity can be constructed, see {@link ConstructionMethod}.
 * TODO: add more methods to construct managed entities
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ManagedEntityConstructor {

    /**
     * Possible ways of constructing a managed entity, including not constructible.
     */
    enum ConstructionMethod {

        /**
         * Indicates that the managed entity should be constructed with a public,
         * no argument constructor.
         */
        PUBLIC_NO_ARGS_CONSTRUCTOR,

        /**
         * Indicates that no supported way was found to construct the managed entity.
         */
        NOT_CONSTRUCTIBLE
    }

    /**
     * Create an instance of the managed entity using one of the {@link ConstructionMethod}s.
     * @param managedEntity The class of the managed entity.
     * @return Instance.
     * @throws NotConstructibleException If this entity cannot be constructed with any supported ways.
     */
    public static <T> T constructManagedEntity(Class<T> managedEntity) throws NotConstructibleException {
        ConstructionMethod constructionMethod = determineConstructionMethod(managedEntity);
        switch (constructionMethod) {
            case PUBLIC_NO_ARGS_CONSTRUCTOR:
                return constructWithPublicNoArgsConstructor(managedEntity);
            default:
                throw new NotConstructibleException(managedEntity);
        }
    }

    /**
     * Investigates a class and determines the preferred way to create an instance of it. If no available
     * way is found, then {@link ConstructionMethod#NOT_CONSTRUCTIBLE} is returned.
     * @param mangedEntity The class of the managed entity.
     * @return Preferred construction method.
     */
    private static <T> ConstructionMethod determineConstructionMethod(Class<T> mangedEntity) {
        Constructor<T> publicNoArgs = ConstructorUtils.getMatchingAccessibleConstructor(mangedEntity);
        return publicNoArgs != null ? ConstructionMethod.PUBLIC_NO_ARGS_CONSTRUCTOR : ConstructionMethod.NOT_CONSTRUCTIBLE;
    }

    /**
     * Construct a managed entity with a public, no argument constructor. This should only be called after it
     * was validated that this entity can be constructed as such.
     * @param managedEntity  The class of the managed entity.
     * @return Instance of the class.
     * @throws NotConstructibleException If an exception happened during construction.
     */
    private static <T> T constructWithPublicNoArgsConstructor(Class<T> managedEntity) throws NotConstructibleException {
        Constructor<T> publicNoArgs = ConstructorUtils.getMatchingAccessibleConstructor(managedEntity);
        try {
            return publicNoArgs.newInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new NotConstructibleException(managedEntity, e);
        }
    }
}
