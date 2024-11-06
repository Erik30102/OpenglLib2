package org.openglLib.Rendering;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL46;
import org.openglLib.RenderApi;
import org.openglLib.Buffers.BufferElement;
import org.openglLib.Buffers.BufferLayout;
import org.openglLib.Buffers.IndexBuffer;
import org.openglLib.Buffers.VertexArray;
import org.openglLib.Buffers.VertexBuffer;
import org.openglLib.Buffers.BufferElement.DataType;
import org.openglLib.Buffers.VertexBuffer.BUFFER_USAGE;
import org.openglLib.Rendering.Texture.Sprite;
import org.openglLib.Rendering.Texture.Texture2D;

import java.util.ArrayList;
import java.util.List;

public class SpriteBatch {
    private Shader shader;

    private final int MAX_BATCH_SIZE = 1000;

    private VertexArray vao;
    private VertexBuffer vbo;

    private boolean hasRoom = true;
    private boolean hasRoomTextures = true;

    private int texCount = 0;

    private float[] vertices;
    private List<Texture2D> textures;

    private int numOfSprites;
    private final int OFFSET_VERTICES = 6;
    
    private int[] texSlots;

    private static final float[] standardUVS = new float[] {
        1,1,
        0,0,
        1,0,
        0,1
    };

    public SpriteBatch(Shader shader) {
        this.shader = shader;
        this.texSlots = CreateTexSlots(GetMaxBindedTextures());
        this.textures = new ArrayList<>();

        this.Start();
    }

    private void Start() {
        BufferLayout layout = new BufferLayout(new BufferElement[] {
            new BufferElement(DataType.VEC3),
            new BufferElement(DataType.VEC2),
            new BufferElement(DataType.FLOAT)
        });

        this.vertices = new float[MAX_BATCH_SIZE * OFFSET_VERTICES];

        vao = new VertexArray();
        vao.bind();
        vbo = new VertexBuffer(this.vertices, BUFFER_USAGE.DYNAMIC_DRAW);
        vbo.SetLayout(layout);

        IndexBuffer ibo = new IndexBuffer(generateIndices());

        vao.AddIndexBuffer(ibo);
        vao.AddVertexBuffer(vbo);
    }

    private int[] generateIndices() {
        int[] indices = new int[MAX_BATCH_SIZE * 6];

        for (int i = 0; i < MAX_BATCH_SIZE; i++) {
            indices[i * 6] = i * 4 + 2;
            indices[i * 6 + 1] = i * 4 + 1;
            indices[i * 6 + 2] = i * 4 + 0;
            indices[i * 6 + 3] = i * 4 + 0;
            indices[i * 6 + 4] = i * 4 + 1;
            indices[i * 6 + 5] = i * 4 + 3;
        }

        return indices;
    }

    public void AddSprite(Texture2D texutre,float[] pos, float[] scale, float rotation)
    {
        AddSprite(texutre, standardUVS, pos, scale, rotation);
    }
    
    public void AddSprite(Sprite sprite, float[] pos, float[] scale, float rotation) {
        AddSprite(sprite.GetTexture(), sprite.GetUVs(), pos, scale, rotation);
    }

    public void AddSprite(Texture2D texture, float[] uvs, float[] pos, float[] scale, float rotation) {
        if(!textures.contains(texture)){
            textures.add(texture);
            texCount++;
            if(texCount >= this.texSlots.length)
            {
                hasRoomTextures = false;
            } 
       }

       Matrix4f transformMatrix = new Matrix4f().identity().translate(pos[0],
       pos[1], 10).scale(scale[0], scale[1], 1) 
				.rotateZ((float) Math.toRadians(rotation));

		Vector4f[] vec = new Vector4f[] {
				new Vector4f(0.5f, -0.5f, 0f, 1f).mul(transformMatrix),
				new Vector4f(-0.5f, 0.5f, 0f, 1f).mul(transformMatrix),
				new Vector4f(0.5f, 0.5f, 0f, 1f).mul(transformMatrix),
				new Vector4f(-0.5f, -0.5f, 0f, 1f).mul(transformMatrix),
		};

        int texIndex = textures.indexOf(texture);

        for (int i = 0; i < 4; i++) {
			int start = numOfSprites * OFFSET_VERTICES * 4;

			vertices[start + i * 6 + 0] = vec[i].x;
			vertices[start + i * 6 + 1] = vec[i].y;
			vertices[start + i * 6 + 2] = vec[i].z;
			vertices[start + i * 6 + 3] = uvs[i*2];
			vertices[start + i * 6 + 4] = uvs[i*2+1];
			vertices[start + i * 6 + 5] = texIndex;
		}

        this.numOfSprites++;

        if(numOfSprites >= MAX_BATCH_SIZE) {
            hasRoom = false;
        }
    }

    public void ReloadData() {
        this.vbo.bind();
        this.vbo.SetData(vertices);
        this.vbo.unbind();
    }

    public void Render(IBatchShaderUniformSetter shaderUnformSetter) {
        shader.bind();
        shaderUnformSetter.SetUniforms(shader, texCount, textures);

        this.vao.bind();
        RenderApi.DrawStatic(numOfSprites * 6);

        textures.get(0).Unbind();
    }

    private int GetMaxBindedTextures() {
        return GL46.glGetInteger(GL46.GL_MAX_TEXTURE_IMAGE_UNITS);
    }

    private int[] CreateTexSlots(int count) {
        int[] slots = new int[count];
        for (int i = 0; i < count; i++) {
            slots[i] = i;
        }
        return slots;
    }

    public boolean IsTextureLoaded(Texture2D tex) {
        return this.textures.contains(tex);
    }

    public boolean HasRoomTextures() {
        return hasRoomTextures;
    }

    public boolean HasRoom() {
        return hasRoom;
    }

    public void Begin() {
        texCount = 0;
        numOfSprites = 0;
        hasRoom = true;
        hasRoomTextures = true;

        textures.clear();
    }
}
