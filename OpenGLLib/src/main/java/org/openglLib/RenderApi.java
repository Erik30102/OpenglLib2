package org.openglLib;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL46;
import org.lwjgl.opengl.GLDebugMessageCallback;

public class RenderApi {
    public static void Init() {
        GL.createCapabilities();
        
        GL46.glEnable(GL46.GL_DEBUG_OUTPUT);
		GL46.glEnable(GL46.GL_DEBUG_OUTPUT_SYNCHRONOUS);
		GL46.glDebugMessageCallback((source, type, id, severity, length, message, userParam) -> {
			String msg = GLDebugMessageCallback.getMessage(length, message);

			switch (type) {
				case GL46.GL_DEBUG_TYPE_ERROR:
					msg = "[ERROR] " + msg;
					break;
				case GL46.GL_DEBUG_TYPE_DEPRECATED_BEHAVIOR:
					msg = "[DEPRECATED BEHAVIOR] " + msg;
					break;
				case GL46.GL_DEBUG_TYPE_UNDEFINED_BEHAVIOR:
					msg = "[UNDEFINED BEHAVIOR] " + msg;
					break;
				case GL46.GL_DEBUG_TYPE_PORTABILITY:
					msg = "[PORTABILITY] " + msg;
					break;
				case GL46.GL_DEBUG_TYPE_PERFORMANCE:
					msg = "[PERFORMANCE] " + msg;
					break;
				case GL46.GL_DEBUG_TYPE_OTHER:
					msg = "[OTHER] " + msg;
					break;
			}

			switch (severity) {
				case GL46.GL_DEBUG_SEVERITY_HIGH:
                    System.err.println(msg);
                    break;
				case GL46.GL_DEBUG_SEVERITY_MEDIUM:
                    System.err.println(msg);
                    break;
				case GL46.GL_DEBUG_SEVERITY_LOW:
					System.out.println(msg);
					break;
				case GL46.GL_DEBUG_SEVERITY_NOTIFICATION:
					// Logger.c_info(msg);
					break;
				default:
					break;
			}
		}, 0);

		GL46.glEnable(GL46.GL_DEPTH_TEST);
		GL46.glEnable(GL46.GL_LINE_SMOOTH);
		GL46.glEnable(GL46.GL_BLEND);
		GL46.glBlendFunc(GL46.GL_SRC_ALPHA, GL46.GL_ONE_MINUS_SRC_ALPHA);
    }

    /**
     * Clears the screen and the internal depth buffer.
     */
    public static void ClearScreen() {
        GL46.glClear(GL46.GL_COLOR_BUFFER_BIT | GL46.GL_DEPTH_BUFFER_BIT);
    }

    /**
     * Overrides the screen in given color. Call <code>RenderApi.ClearScreen()</code> before
     * 
     * @param r red color component (0-1)
     * @param g green color component (0-1)
     * @param b blue color component (0-1)
     */
    public static void ClearScreenColor(float r, float g, float b) {
        GL46.glClearColor(r, g, b, 1);
    }

    /**
     * Sets the viewport to the specified dimensions.
     * 
     * @param width width of the viewport
     * @param height height of the viewport
     */
    public static void SetViewport(int width, int height) {
        SetViewport(0, 0, width, height);
    }

    /**
     * Sets the viewport to start at specified position and to dimensions.
     * 
     * @param x start position in x-axis
     * @param y start position in y-axis
     * @param width width of the viewport
     * @param height height of the viewport
     */
    public static void SetViewport(int x, int y, int width, int height) {
        GL46.glViewport(x, y, width, height);
    }
}
