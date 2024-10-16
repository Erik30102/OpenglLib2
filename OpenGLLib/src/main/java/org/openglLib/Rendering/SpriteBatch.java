package org.openglLib.Rendering;

import org.lwjgl.opengl.GL46;
import org.openglLib.Buffers.BufferElement;
import org.openglLib.Buffers.BufferLayout;
import org.openglLib.Buffers.IndexBuffer;
import org.openglLib.Buffers.VertexArray;
import org.openglLib.Buffers.VertexBuffer;
import org.openglLib.Buffers.BufferElement.DataType;
import org.openglLib.Buffers.VertexBuffer.BUFFER_USAGE;
import org.openglLib.Rendering.Texture.Texture2D;

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

    public SpriteBatch(Shader shader) {
        this.shader = shader;
        this.texSlots = CreateTexSlots(GetMaxBindedTextures());

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

        for (int i = 0; i < indices.length; i++) {
            indices[i * 6] = i * 4 + 2;
            indices[i * 6 + 1] = i * 4 + 1;
            indices[i * 6 + 2] = i * 4 + 0;
            indices[i * 6 + 3] = i * 4 + 0;
            indices[i * 6 + 4] = i * 4 + 1;
            indices[i * 6 + 5] = i * 4 + 3;
        }

        return indices;
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

    public boolean HasRoomTextures() {
        return hasRoomTextures;
    }

    public boolean HasRoom() {
        return hasRoom;
    }
}
