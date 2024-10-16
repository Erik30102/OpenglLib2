package org.openglLib.Window.Events;

public class CharEvent extends Event {

	private int keycode;

	public CharEvent(int keycode) {
		this.keycode = keycode;
	}

	/**
	 * @return Key Code for the given Char Event.
	 */
	public int GetKeyCode() {
		return this.keycode;
	}

	@Override
	public EventType GetEventType() {
		return EventType.CharEvent;
	}

	@Override
	public String GetName() {
		return "CharEvent";
	}

	@Override
	public String toString() {
		return "CharEvent for KeyCode: " + keycode;
	}
}