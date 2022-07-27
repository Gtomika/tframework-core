/* Licensed under Apache-2.0 2022. */
package org.tframework.core.ioc.annotations;

/**
 * Internal TFramework annotation. It is used to mark <b>singleton</b> instances that are
 * created before the IoC is initialized. These singletons will then be added to the managed
 * singleton classes.
 * <p>
 * Since the managed instance is created not by IoC, it cannot have dependencies like a normal
 * managed entity.
 *
 * <h2>Getting the instance</h2>
 * Since the IoC must be able to get the instance of this pre-constructed singleton, all classes annotated
 * with this must have a static method called {@value GET_INSTANCE_METHOD_NAME} which returns the instance. This method
 * can be of any visibility. No parameters are allowed for it.
 */
public @interface ManagePreConstructedSingleton {

    /**
     * Expected name of the method to be used when getting the pre-constructed instance.
     */
    String GET_INSTANCE_METHOD_NAME = "getInstance";

}
