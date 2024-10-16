package org.openglLib.Rendering.Texture;

import org.lwjgl.opengl.GL46;
import java.nio.ByteBuffer;

public class Texture2D extends Texture {
    
    private int width, height;

    public Texture2D(int width, int height, TextureFormat format, TextureFiltering filtering, TextureWrapMode wrapMode, byte[] data) {
        this.width = width;
        this.height = height;
        this.textureFormat = format;
        this.textureFiltering = filtering;
        this.wrapMode = wrapMode;
        
        this.textureID = GL46.glCreateTextures(GL46.GL_TEXTURE_2D);
        GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.textureID);

        // Apply Wrap and Filtering
        GL46.glTextureParameteri(this.textureID, GL46.GL_TEXTURE_MIN_FILTER,
				this.InternalFilteringToGLFiltering(filtering));
		GL46.glTextureParameteri(this.textureID, GL46.GL_TEXTURE_MAG_FILTER,
				this.InternalFilteringToGLFiltering(filtering));

		GL46.glTextureParameteri(this.textureID, GL46.GL_TEXTURE_WRAP_S, this.InternalWrapModeToGLWrapMode(wrapMode));
		GL46.glTextureParameteri(this.textureID, GL46.GL_TEXTURE_WRAP_T, this.InternalWrapModeToGLWrapMode(wrapMode));
   
        // Wrap byte array in byteBuffer
        ByteBuffer byteBufferWithData = ByteBuffer.wrap(data);

        // Upload texture data to GPU
        GL46.glTexImage2D(GL46.GL_TEXTURE_2D, 0, 
            this.InternalFormatToGLDataFormat(format), 
            width, height, 0, 
            this.InternalFormatToGLDataFormat(format), 
            GL46.GL_UNSIGNED_BYTE, byteBufferWithData);

        GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
    }

    @Override
    public TextureType GetTextureType() {
        return TextureType.TEXTURE2D;
    }

    @Override
    public void Bind(int slot) {
        GL46.glActiveTexture(GL46.GL_TEXTURE0 + slot);
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, this.textureID);
    }

    @Override
    public void Unbind() {
		GL46.glBindTexture(GL46.GL_TEXTURE_2D, 0);
    }

    public int GetWidth() {
        return width;
    }

    public int GetHeight() {
        return height;
    }
}
