package org.openglLib;

import org.lwjgl.glfw.GLFW;

public class Helper {
    public static long GLFWCreateCursor(int cursor) {
        return GLFW.glfwCreateStandardCursor(cursor);
    }
}
