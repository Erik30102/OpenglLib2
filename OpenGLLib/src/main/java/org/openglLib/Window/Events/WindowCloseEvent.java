package org.openglLib.Window.Events;

public class WindowCloseEvent extends Event {

	@Override
	public EventType GetEventType() {
		return EventType.WindowClose;
	}

	@Override
	public String GetName() {
		return "WindowCloseEvent";
	}

}