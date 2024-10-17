package org.openglLib.Window;

import org.lwjgl.glfw.GLFW;
import org.openglLib.RenderApi;
import org.openglLib.Window.Events.CharEvent;
import org.openglLib.Window.Events.IEventCallback;
import org.openglLib.Window.Events.KeyPressedEvent;
import org.openglLib.Window.Events.KeyReleasedEvent;
import org.openglLib.Window.Events.MouseButtonPressedEvent;
import org.openglLib.Window.Events.MouseButtonReleasedEvent;
import org.openglLib.Window.Events.MouseMovedEvent;
import org.openglLib.Window.Events.MouseScrollEvent;
import org.openglLib.Window.Events.WindowCloseEvent;
import org.openglLib.Window.Events.WindowResizeEvent;

public class Window {
    private int width, height;
    private String title;

    private long windowPtr;
    private long montiorPtr;

    private IEventCallback EventCallback;

    public Window(int width, int height, String title, IEventCallback EventCallback) {
        this.height = height;
        this.width = width;
        this.title = title;
        this.EventCallback = EventCallback;

        if(!GLFW.glfwInit()) {
            throw new IllegalStateException("Failed to initialize GLFW");
        }

        GLFW.glfwDefaultWindowHints();
        GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GLFW.GLFW_TRUE);

        this.windowPtr = GLFW.glfwCreateWindow(this.width, this.height, this.title, 0, 0);
        if (this.windowPtr == 0) {
            GLFW.glfwTerminate();
            throw new IllegalStateException("Failed to create GLFW window");
        }

        this.SetupCallbacks();

        GLFW.glfwMakeContextCurrent(this.windowPtr);
        GLFW.glfwSwapInterval(1);
		GLFW.glfwShowWindow(this.windowPtr);
        // this.montiorPtr = GLFW.glfwGetPrimaryMonitor();

        RenderApi.Init();
    }

    private void SetupCallbacks() {
		GLFW.glfwSetErrorCallback((error, description) -> {
			System.err.println("GLFW error: " + error + ", description: " + description);
		});

		GLFW.glfwSetWindowSizeCallback(this.windowPtr, (window, width, height) -> {
			this.height = height;
			this.width = width;

			WindowResizeEvent event = new WindowResizeEvent(width, height);
			EventCallback.OnEvent(event);
		});

		GLFW.glfwSetKeyCallback(this.windowPtr, (w, key, scancode, action, mods) -> {
			switch (action) {
				case GLFW.GLFW_PRESS: {
					KeyPressedEvent event = new KeyPressedEvent(key, false);
					EventCallback.OnEvent(event);
				}
					break;
				case GLFW.GLFW_RELEASE: {
					KeyReleasedEvent event = new KeyReleasedEvent(key);
					EventCallback.OnEvent(event);
				}
					break;
				case GLFW.GLFW_REPEAT: {
					KeyPressedEvent event = new KeyPressedEvent(key, true);
					EventCallback.OnEvent(event);
				}
					break;
			}
		});

		GLFW.glfwSetWindowCloseCallback(this.windowPtr, (window) -> {
			WindowCloseEvent event = new WindowCloseEvent();
			EventCallback.OnEvent(event);
		});

		GLFW.glfwSetMouseButtonCallback(this.windowPtr, (w, button, action, mods) -> {
			switch (action) {
				case GLFW.GLFW_PRESS: {
					MouseButtonPressedEvent event = new MouseButtonPressedEvent(button);
					EventCallback.OnEvent(event);
				}
					break;
				case GLFW.GLFW_RELEASE: {
					MouseButtonReleasedEvent event = new MouseButtonReleasedEvent(button);
					EventCallback.OnEvent(event);
				}
					break;
			}
		});

		GLFW.glfwSetScrollCallback(this.windowPtr, (w, xOffset, yOffset) -> {
			MouseScrollEvent event = new MouseScrollEvent(xOffset, yOffset);
			EventCallback.OnEvent(event);
		});

		GLFW.glfwSetCursorPosCallback(this.windowPtr, (window, xPos, yPos) -> {
			MouseMovedEvent event = new MouseMovedEvent(xPos, yPos);
			EventCallback.OnEvent(event);
		});

		GLFW.glfwSetCharCallback(this.windowPtr, (w, c) -> {
			CharEvent event = new CharEvent(c);
			EventCallback.OnEvent(event);
		});
	}

    /**
	 * @return The height of the window
	 */
	public int GetHeight() {
		return this.height;
	}

	/**
	 * @return The width of the window
	 */
	public int GetWidth() {
		return this.width;
	}

	/**
	 * Swap buffer and Poll events
	 * has to be called every frame
	 */
	public void OnUpdate() {
		GLFW.glfwPollEvents();
		GLFW.glfwSwapBuffers(this.windowPtr);
	}

    /**
	 * Shuts down the window by destroying the window pointer.
	 */
	public void Shutdown() {
		GLFW.glfwDestroyWindow(this.windowPtr);
	}

    /**
     * Use this method in a main while loop to stop the app when window crashes/closes
     * 
     * @return if the window is closed or crashed
     */
    public boolean ShouldClose() {
        return GLFW.glfwWindowShouldClose(this.windowPtr);
    }

}
