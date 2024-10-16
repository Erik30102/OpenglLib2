package org.openglLib.Window.Events;

public class MouseMovedEvent extends Event {

	private double x, y;

	public MouseMovedEvent(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * @return the x coordinate of the mouse
	 */
	public double GetX() {
		return x;
	}

	/**
	 * @return the y coordinate of the mouse
	 */
	public double GetY() {
		return y;
	}

	@Override
	public EventType GetEventType() {
		return EventType.MouseMoved;
	}

	@Override
	public String GetName() {
		return "MouseMovedEvent";
	}

    @Override
    public String toString() {
        return "MouseMovedEvent to (" + x + ", " + y + ")";
    }
}