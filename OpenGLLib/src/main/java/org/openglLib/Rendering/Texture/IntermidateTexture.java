package org.openglLib.Rendering.Texture;

import org.openglLib.Rendering.Texture.Texture.TextureFormat;

public class IntermidateTexture {
    private int width, height;
    private byte[] data;
    private TextureFormat format;

    public IntermidateTexture(int width, int height, byte[] data, TextureFormat format) {
        this.width = width;
        this.height = height;
        this.data = data;
        this.format = format;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public byte[] getData() {
        return this.data;
    }

    public TextureFormat getFormat() {
        return this.format;
    }

}