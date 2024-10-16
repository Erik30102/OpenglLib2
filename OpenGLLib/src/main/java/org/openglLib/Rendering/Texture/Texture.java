package org.openglLib.Rendering.Texture;

import org.lwjgl.opengl.GL46;

public abstract class Texture {
    public enum TextureType {
		TEXTURE2D,
	};

	public enum TextureWrapMode {
		REPEAT, // repeat the texture if the uv is more than 1
		CLAMP_TO_EDGE, // repeates the last pixel to the edge 
		CLAMP_TO_BORDER // black if the uv is more than 1
	};

	public enum TextureFiltering {
		NEAREST, // for pixel art does not interpolate between pixels
		BILINEAR, // interpolate between pixels
		TRILINEAR, // Same as BILINEAR but better 
	};

	public enum TextureFormat {
		RGB8,
		RGBA8, // with alpha channel
	}

	protected int textureID;

	protected TextureWrapMode wrapMode;
    protected TextureFiltering textureFiltering;
    protected TextureFormat textureFormat;

	/**
	 * @return the texture filtering mode of the texture
	 */
	public TextureFiltering GetTextureFiltering() {
		return textureFiltering;
    }

	/**
	 * @return the texture format of the texture
	 */
	public TextureFormat GetTextureFormat() {
        return textureFormat;
    }
	
	/**
	 * @return the texture wrap mode of the texture
	 */
	public TextureWrapMode GetWrapMode() {
        return wrapMode;
    }

	/**
	 * @return type of texture but currently only texture2D is implemented and probably will be the only one
	 */
	public abstract TextureType GetTextureType();

	/**
	 * @return the ID of the texture in the GPU
	 */
	public int GetTextureID() {
        return textureID;
    }

	/**
     * Binds the texture to the given slot
     * 
     * @param slot The slot to bind the texture to
     */
	public abstract void Bind(int slot);

	/**
     * Unbinds the texture from the current binding point
     */
	public abstract void Unbind();

	/**
     * Disposes the texture and frees the resources
     */
	public void Dispose() {
		GL46.glDeleteTextures(textureID);
	}

	/**
     * Converts the Internal Fomrat to the Opengl Internal Format
     * 
     * @param format The Internal Format to return
     * @return a value which can be passed to the gpu which will be interpreted as the internal format
	 */
	protected int InternalFormatToGLInternalFormat(TextureFormat format) {
		switch (format) {
			case RGB8:
				return GL46.GL_RGB8;
			case RGBA8:
				return GL46.GL_RGBA8;
			default:
				return GL46.GL_RGBA8;
		}
	}

	/**
	 * Converts the Internal Fomrat to the Opengl Data Format
	 * 
	 * @param format The Internal Format to return
	 * @return a value which can be passed to the gpu which will be interpreted as
	 *         the data format
	 */
	protected int InternalFormatToGLDataFormat(TextureFormat format) {
		switch (format) {
			case RGB8:
				return GL46.GL_RGB;
			case RGBA8:
				return GL46.GL_RGBA;
			default:
				return GL46.GL_RGBA;
		}
	}

	/**
     * Converts the Internal Fomrat to the Opengl Wrap Mode
     * 
     * @param wrapMode The Internal Wrap Mode to return
     * @return a value which can be passed to the gpu which will be interpreted as the wrap mode
	 */
	protected int InternalWrapModeToGLWrapMode(TextureWrapMode wrapMode) {
		switch (wrapMode) {
			case REPEAT:
				return GL46.GL_REPEAT;
			case CLAMP_TO_EDGE:
				return GL46.GL_CLAMP_TO_EDGE;
			case CLAMP_TO_BORDER:
				return GL46.GL_CLAMP_TO_BORDER;
			default:
				return GL46.GL_REPEAT;
		}
	}

	/**
	 * Converts the Internal Fomrat to the Opengl Filtering Mode
	 * 
	 * @param filtering The Internal Filtering Mode to return
	 * @return The new Internal Filtering Mode
	 */
	protected int InternalFilteringToGLFiltering(TextureFiltering filtering) {
		switch (filtering) {
			case NEAREST:
				return GL46.GL_NEAREST;
			case BILINEAR:
				return GL46.GL_LINEAR;
			case TRILINEAR:
				return GL46.GL_LINEAR_MIPMAP_LINEAR;
			default:
				return GL46.GL_NEAREST;
		}
	}
}
