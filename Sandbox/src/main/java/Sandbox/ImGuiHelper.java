package Sandbox;

import org.openglLib.Helper;
import org.openglLib.Input.KeyCode;
import org.openglLib.Window.Events.CharEvent;
import org.openglLib.Window.Events.Event;
import org.openglLib.Window.Events.KeyPressedEvent;
import org.openglLib.Window.Events.KeyReleasedEvent;
import org.openglLib.Window.Events.MouseButtonPressedEvent;
import org.openglLib.Window.Events.MouseMovedEvent;
import org.openglLib.Window.Events.MouseScrollEvent;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.extension.imguizmo.ImGuizmo;
import imgui.flag.ImGuiBackendFlags;
import imgui.flag.ImGuiConfigFlags;
import imgui.flag.ImGuiKey;
import imgui.flag.ImGuiMouseCursor;
import imgui.gl3.ImGuiImplGl3;

public class ImGuiHelper {

	private final long[] mouseCursors = new long[ImGuiMouseCursor.COUNT];
	private final ImGuiImplGl3 imGuiGl3 = new ImGuiImplGl3();

    public static final int GLFW_ARROW_CURSOR = 0x36001;
    public static final int GLFW_IBEAM_CURSOR = 0x36002;
    public static final int GLFW_HRESIZE_CURSOR = 0x36005;
    public static final int GLFW_VRESIZE_CURSOR = 0x36006;
    public static final int GLFW_HAND_CURSOR = 0x36004;

    public void Init() {
        ImGui.createContext();

		final ImGuiIO io = ImGui.getIO();

		io.setIniFilename("imgui.ini");
		io.setConfigFlags(ImGuiConfigFlags.NavEnableKeyboard);
		io.setConfigFlags(ImGuiConfigFlags.DockingEnable);
		io.setBackendFlags(ImGuiBackendFlags.HasMouseCursors);
		io.setBackendPlatformName("imgui_java_impl_glfw");

		final int[] keyMap = new int[ImGuiKey.COUNT];
		keyMap[ImGuiKey.Tab] = KeyCode.KEY_TAB;
		keyMap[ImGuiKey.LeftArrow] = KeyCode.KEY_LEFT;
		keyMap[ImGuiKey.RightArrow] = KeyCode.KEY_RIGHT;
		keyMap[ImGuiKey.UpArrow] = KeyCode.KEY_UP;
		keyMap[ImGuiKey.DownArrow] = KeyCode.KEY_DOWN;
		keyMap[ImGuiKey.PageUp] = KeyCode.KEY_PAGE_UP;
		keyMap[ImGuiKey.PageDown] = KeyCode.KEY_PAGE_DOWN;
		keyMap[ImGuiKey.Home] = KeyCode.KEY_HOME;
		keyMap[ImGuiKey.End] = KeyCode.KEY_END;
		keyMap[ImGuiKey.Insert] = KeyCode.KEY_INSERT;
		keyMap[ImGuiKey.Delete] = KeyCode.KEY_DELETE;
		keyMap[ImGuiKey.Backspace] = KeyCode.KEY_BACKSPACE;
		keyMap[ImGuiKey.Space] = KeyCode.KEY_SPACE;
		keyMap[ImGuiKey.Enter] = KeyCode.KEY_ENTER;
		keyMap[ImGuiKey.Escape] = KeyCode.KEY_ESCAPE;
		keyMap[ImGuiKey.KeyPadEnter] = KeyCode.KEY_KP_ENTER;
		keyMap[ImGuiKey.A] = KeyCode.KEY_A;
		keyMap[ImGuiKey.C] = KeyCode.KEY_C;
		keyMap[ImGuiKey.V] = KeyCode.KEY_V;
		keyMap[ImGuiKey.X] = KeyCode.KEY_X;
		keyMap[ImGuiKey.Y] = KeyCode.KEY_Y;
		keyMap[ImGuiKey.Z] = KeyCode.KEY_Z;
		io.setKeyMap(keyMap);

		mouseCursors[ImGuiMouseCursor.Arrow] = Helper.GLFWCreateCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.TextInput] = Helper.GLFWCreateCursor(GLFW_IBEAM_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeAll] = Helper.GLFWCreateCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNS] = Helper.GLFWCreateCursor(GLFW_VRESIZE_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeEW] = Helper.GLFWCreateCursor(GLFW_HRESIZE_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNESW] = Helper.GLFWCreateCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.ResizeNWSE] = Helper.GLFWCreateCursor(GLFW_ARROW_CURSOR);
		mouseCursors[ImGuiMouseCursor.Hand] = Helper.GLFWCreateCursor(GLFW_HAND_CURSOR);
		mouseCursors[ImGuiMouseCursor.NotAllowed] = Helper.GLFWCreateCursor(GLFW_ARROW_CURSOR);

		imGuiGl3.init("#version 330 core");
    }
	
    private float m_time = 0;

    public void Begin(float width, float height, float dt) {
		ImGuiIO io = ImGui.getIO();
		io.setDisplaySize(width, height);

		float time = m_time + dt;

		io.setDeltaTime(time - m_time);
		m_time = time;

		ImGui.newFrame();
		ImGuizmo.beginFrame();
	}

    public void End() {
		ImGui.render();

		imGuiGl3.renderDrawData(ImGui.getDrawData());
	}

    public void OnEvent(Event event) {
        ImGuiIO io = ImGui.getIO();

		switch (event.GetEventType()) {
			case KeyPressed:
				io.setKeysDown(((KeyPressedEvent) event).GetKeyCode(), true);
				break;
			case KeyReleased:
				io.setKeysDown(((KeyReleasedEvent) event).GetKeyCode(), false);
				break;
			case CharEvent:
				io.addInputCharacter(((CharEvent) event).GetKeyCode());
				break;
			case MouseMoved:
				io.setMousePos((float) ((MouseMovedEvent) event).GetX(), (float) ((MouseMovedEvent) event).GetY());
				break;
			case MouseButtonPressed:
				final boolean[] mouseDown = new boolean[5];

				int button = ((MouseButtonPressedEvent) event).GetMouseCode();

				mouseDown[0] = button == KeyCode.MOUSE_BUTTON_1;
				mouseDown[1] = button == KeyCode.MOUSE_BUTTON_2;
				mouseDown[2] = button == KeyCode.MOUSE_BUTTON_3;
				mouseDown[3] = button == KeyCode.MOUSE_BUTTON_4;
				mouseDown[4] = button == KeyCode.MOUSE_BUTTON_5;

				io.setMouseDown(mouseDown);

				if (!io.getWantCaptureMouse() && mouseDown[1]) {
					ImGui.setWindowFocus(null);
				}
				break;
			case MouseButtonReleased:
				final boolean[] _mouseDown = new boolean[5];

				_mouseDown[0] = false;
				_mouseDown[1] = false;
				_mouseDown[2] = false;
				_mouseDown[3] = false;
				_mouseDown[4] = false;

				io.setMouseDown(_mouseDown);
				break;
			case MouseScrolled:
				io.setMouseWheel(io.getMouseWheel() + (float) ((MouseScrollEvent) event).GetOffsetX());
				io.setMouseWheelH(io.getMouseWheelH() + (float) ((MouseScrollEvent) event).GetOffsetY());
				break;
			default:
				break;
		}
    }
}
