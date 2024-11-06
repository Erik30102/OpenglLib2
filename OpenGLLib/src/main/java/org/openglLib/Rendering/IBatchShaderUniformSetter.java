package org.openglLib.Rendering;

import java.util.List;

import org.openglLib.Rendering.Texture.Texture2D;

public interface IBatchShaderUniformSetter {
    public void SetUniforms(Shader shader, int texCount, List<Texture2D> textures);
}
