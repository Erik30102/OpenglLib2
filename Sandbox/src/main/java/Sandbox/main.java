package Sandbox;

import org.openglLib.RenderApi;
import org.openglLib.Window.Window;

import imgui.ImGui;

public class Main {

	public static void main(String[] args) {
		ImGuiHelper img = new ImGuiHelper();

		Window window = new Window(600, 380, "Test", event ->  {
			img.OnEvent(event);
		});
		
		img.Init();;

		while(!window.ShouldClose()){
			RenderApi.ClearScreen();
			RenderApi.ClearScreenColor(0.4f, 0.2f, 0.1f);
			
			img.Begin(600, 380,1);

			ImGui.begin("test");

			ImGui.end();

			img.End();

			window.OnUpdate();
		}
	}
}
