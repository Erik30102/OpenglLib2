package Sandbox;

import org.openglLib.RenderApi;
import org.openglLib.Rendering.OrthographicCamera;
import org.openglLib.Rendering.Shader;
import org.openglLib.Rendering.SpriteBatch;
import org.openglLib.Rendering.Texture.IntermidateTexture;
import org.openglLib.Rendering.Texture.Texture2D;
import org.openglLib.Rendering.Texture.TextureImporter;
import org.openglLib.Rendering.Texture.Texture.TextureFiltering;
import org.openglLib.Rendering.Texture.Texture.TextureFormat;
import org.openglLib.Rendering.Texture.Texture.TextureWrapMode;
import org.openglLib.Window.Window;

import imgui.ImGui;

public class Main {

	public static void main(String[] args) {
		ImGuiHelper img = new ImGuiHelper();

		Window window = new Window(600, 380, "Test", event ->  {
			img.OnEvent(event);
		});
		
		img.Init();;

		Shader s = new Shader("""
				#type vertex
				#version 330 core

				layout(location = 0) in vec3 a_Position;
				layout(location = 1) in vec2 a_texCoord;
				layout(location = 2) in float a_texId;

				uniform mat4 viewMat;
				uniform mat4 projectionMat;

				out vec2 texCoord;
				out vec2 WordPos;
				out float texId;

				void main() {
					texCoord = a_texCoord;
					texId = a_texId;
					WordPos = a_Position.xy;
					gl_Position = projectionMat * viewMat * vec4(a_Position, 1.0);
				}

				#type fragment
				#version 330 core

				layout(location = 0) out vec4 color;

				in vec2 texCoord;
				in vec2 WordPos;
				in float texId;

				uniform sampler2D textures[8];

				void main() {
					int id = int(texId);
					color = texture(textures[id], texCoord);
				}
				""");

		SpriteBatch b = new SpriteBatch(s);

		IntermidateTexture textureData = TextureImporter.ImportTexture("test.png");

		OrthographicCamera camera = new OrthographicCamera(600, 380);
		Texture2D t = new Texture2D(textureData.getWidth(),textureData.getHeight(), textureData.getFormat(), TextureFiltering.BILINEAR, TextureWrapMode.CLAMP_TO_BORDER, textureData.getData());

		while(!window.ShouldClose()){
			RenderApi.ClearScreen();
			RenderApi.ClearScreenColor(0.1f, 0.1f, 0.1f);
			
			b.Begin();
		
			b.AddSprite(t, new float[] { 0,0}, new float[]{1,1}, 0);
		
			b.ReloadData();
			b.Render((shader, texCount, textures) -> {
				shader.loadMatrix4("viewMat", camera.GetView());
				shader.loadMatrix4("projectionMat", camera.GetProjection());

				for (int i = 0; i < texCount; i++) {
					textures.get(i).Bind(i);
				}

				shader.loadInt("textures[0]", 0);
			});
			
			img.Begin(600, 380,1);

			ImGui.begin("test");

			ImGui.end();

			img.End();

			window.OnUpdate();
		}
	}
}
