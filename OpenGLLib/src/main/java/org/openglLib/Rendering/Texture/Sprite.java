package org.openglLib.Rendering.Texture;

public class Sprite {
    private Texture2D texture;
    private float[] uvs;

    public Sprite(Texture2D texture, float[] uvs) {
        this.texture = texture;
        this.uvs = uvs;
    }

    public Texture2D GetTexture() {
        return texture;
    }

    public float[] GetUVs() {
        return uvs;
    }
}
