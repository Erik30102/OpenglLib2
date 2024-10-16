package Sandbox;

import org.openglLib.RenderApi;
import org.openglLib.Window.Window;

public class main {
    public static void main(String[] args) {
		Window window = new Window(600, 380, "Test", event -> {

		});

		while(!window.ShouldClose()){
			RenderApi.ClearScreen();
			RenderApi.ClearScreenColor(0.4f, 0.2f, 0.1f);
			
			window.OnUpdate();
		}
	}
}
