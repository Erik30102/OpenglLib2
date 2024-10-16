package org.openglLib.Window.Events;

public class KeyPressedEvent extends Event {
    private int keyCode;
    private boolean reapeat;

    public KeyPressedEvent(int keyCode, boolean reapeat) {
        this.keyCode = keyCode;
        this.reapeat = reapeat;
    }

    /**
     * @return Key Code for the given keyboard key pressed
     */
    public int GetKeyCode() {
        return this.keyCode;
    }

    @Override
    public EventType GetEventType() {
        return EventType.KeyPressed;
    }

    @Override
    public String GetName() {
        return "KeyPressed Event";
    }

    /**
     * Returns whether the key press is pressed continuously or not.
     * 
     * @return Whether the key press is pressed continuously or not
     */
    public boolean isKeyRepeat() {
        return this.reapeat;
    }

    @Override
    public String toString() {
        return "KeyPressedEvent for KeyCode: " + keyCode;
    }
}