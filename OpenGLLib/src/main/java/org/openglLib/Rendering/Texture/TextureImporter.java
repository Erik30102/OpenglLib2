package org.openglLib.Rendering.Texture;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBImage;
import org.openglLib.Rendering.Texture.Texture.TextureFormat;

public class TextureImporter {
    public static IntermidateTexture ImportTexture(String path) {
        IntBuffer width = BufferUtils.createIntBuffer(1);
		IntBuffer height = BufferUtils.createIntBuffer(1);
		IntBuffer channels = BufferUtils.createIntBuffer(1);
		ByteBuffer image = STBImage.stbi_load(
				path, width, height,
				channels, 0);

                byte[] arr = new byte[image.remaining()];
        image.get(arr);

        IntermidateTexture imageData = new IntermidateTexture(width.get(0), height.get(0), arr, 
            (channels.get(0) == 4) ? TextureFormat.RGBA8 : TextureFormat.RGB8);

        return imageData;
    }
}
