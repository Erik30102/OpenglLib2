package org.openglLib.Window.Events;

public class KeyReleasedEvent extends Event {
	private int keyCode;

	public KeyReleasedEvent(int keyCode) {
		this.keyCode = keyCode;
	}

	/**
	 * @return Key Code for the given keyboard key released
	 */
	public int GetKeyCode() {
		return this.keyCode;
	}

	@Override
	public EventType GetEventType() {
		return EventType.KeyReleased;
	}

	@Override
	public String GetName() {
		return "KeyPressed Event";
	}

	@Override
	public String toString() {
		return "KeyReleased for KeyCode: " + keyCode;
	}
}