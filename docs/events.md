# Events

Events are a way to trigger actions in your application. Each event has two components:

- **Topic**: This is a string, which groups events together.
- **Payload**: This is an object, which contains the data that is passed to the event handler.

The `Event` record describe this structure, but you typically don't need to interact with it directly. 
There is no need to "register" events, as they are published and subscribed dynamically.

## The Event Manager

The `EventManager` is an element that is responsible for managing events. It has methods for:

- Subscribing to events.
- Unsubscribing from events.
- Publishing events.

Since this is just a regular element, you can inject and use it in your own elements. Please see the
Javadoc of this class for more information.

## Subscribing to Events

There are two ways to subscribe to events. As mentioned above, you can use the `EventManager` directly to 
subscribe to events. However, you can also use the `@Subscribe` annotation. For example:

```java
@Element
public class SomeElement {

  @Subscribe("some-topic")
  public void handleSomeEvent(MyPayload payload) {
    //do something
  }

}
```

This method will be called whenever an event with the topic `some-topic` is published. The payload of the event
will be passed to the method as a parameter.

Some things to note:

- There are some limitations on the method signature. Please see the Javadoc of the `@Subscribe` annotation
for more information. For example, the method must have a single parameter. These are validated at startup.
- You must ensure that the parameter payload type matches the type of actual event payload. This is
not validated at startup, but will cause a runtime error if the types do not match.
- You should not make assumptions about what thread the method will be called on. This is for the framework
to decide.
- You cannot use multiple `@Subscribe` annotations on the same method.

> :gear: **Technical note**: You can check
> [SubscribeElementPostProcessor](../src/main/java/org/tframework/core/events/SubscribeElementPostProcessor.java)
> for implementation. This is an element post processor which makes the subscription in the background.

## Publishing Events

You can publish events using the `EventManager`. Currently, there is no annotation based way to publish events.
For example, to publish an event which the `@Subscribe` method above will handle, you can do the following:

```java
@Element
public class AnotherElement {
    
  private final EventManager eventManager;
  
  public AnotherElement(EventManager eventManager) {
      this.eventManager = eventManager;
  }

  public void someMethod() {
    eventManager.publish("some-topic", new MyPayload());
  }
}
```

Be mindful of the payload type, as mentioned above. In this case, the payload type must be `MyPayload`.

## Core Events

There are some core events that are published by the framework. You can subscribe to these topics to 
get notified about some important framework events. These topics are collected in the `CoreEvents` class 
as constants.

- `CoreEvents.APPLICATION_INITIALIZED`: This event is published when the application is initialized. The 
payload is the `Application` element.