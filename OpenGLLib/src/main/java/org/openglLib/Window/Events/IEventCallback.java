package org.openglLib.Window.Events;

/**
 * Interface for registering a class as a event listener
 */
public interface IEventCallback {
    /**
     * the function every event is send to if registerd in the event system
     * 
     * @param event Abstract class of event get event type with
     * 
     *              <pre>
     *              event.GetEventType();
     *              </pre>
     */
    public void OnEvent(Event event);
}
