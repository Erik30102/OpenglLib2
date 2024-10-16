package org.openglLib.Window.Events;

public abstract class Event {
    public enum EventType {
        WindowClose, WindowResize, WindowFocus, WindowLostFocus,
        KeyPressed, KeyReleased, CharEvent,
        MouseButtonPressed, MouseButtonReleased, MouseMoved, MouseScrolled,
        Other
    };

    /**
     * @return the type of event in a EventType enum
     */
    public abstract EventType GetEventType();

    /**
     * @return the name of the event should probably be destroyed currently only for
     *         debugging purposes should probably use eithe toString() or
     *         GetEventType()
     * 
     */
    @Deprecated
    public abstract String GetName();

    /*
     * @return the name of the event same as the GetName() method
     */
    public String toString() {
        return GetName();
    }
}
