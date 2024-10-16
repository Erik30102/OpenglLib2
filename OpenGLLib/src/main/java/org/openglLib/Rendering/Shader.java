package org.openglLib.Rendering;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Vector;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL46;
import org.lwjgl.system.MemoryStack;

public class Shader {
    public enum ShaderType {
		VERTEX,
		FRAGMENT
	}

	private static int InternalShaderTypeToOpenglType(ShaderType type) {
		switch (type) {
			case VERTEX:
				return GL46.GL_VERTEX_SHADER;
			case FRAGMENT:
				return GL46.GL_FRAGMENT_SHADER;
		}
		System.err.println("Unknown shader type: " + type.toString());

		return 0;
	}

	private static ShaderType StringToInternalShaderType(String type) {
		switch (type) {
			case "vertex":
				return ShaderType.VERTEX;
			case "fragment":
				return ShaderType.FRAGMENT;
		}
		System.err.println("Unknown string shader type: " + type.toString());

        return null;
	}

    private int ShaderId;
	private HashMap<String, Integer> uniforms = new HashMap<String, Integer>();

    /**
     * Processed the shader source into vertex and fragment shader sources.
     * The type of the shader is defined in the first line with #type x and then when wanting to write the other part of the shader just write #type y and the scond part will start
     * 
     * @param src
     * @return index 0: vertex; index 1: fragment
     */
    private static String[] GetProccesedShaderSource(String src) {
        String vertexSrc = "";
		String fragmentSrc = "";

		int index = src.indexOf("#type ", 0);
		while (index >= 0) {
			int eol = src.indexOf("\n", index);
			String type = src.substring(index + 6, eol).trim();

			ShaderType shaderType = StringToInternalShaderType(type);
			index = src.indexOf("#type ", eol);

			if (shaderType == ShaderType.FRAGMENT) {
				fragmentSrc = src.substring(eol, index == -1 ? src.length() : index);
			} else {
				vertexSrc = src.substring(eol, index == -1 ? src.length() : index);
			}
		}

		return new String[] { vertexSrc, fragmentSrc };
    }


	public Shader(String soruce) {
		String[] vertexSrcAndFragSrc = GetProccesedShaderSource(soruce);

        this.LoadShader(vertexSrcAndFragSrc[0], vertexSrcAndFragSrc[1]);
	}

	private void LoadShader(String vertexSrc, String fragmentSrc) {
		this.ShaderId = GL46.glCreateProgram();

		int vertexShaderId = CompileShader(ShaderType.VERTEX, vertexSrc);
		int fragmentShaderId = CompileShader(ShaderType.FRAGMENT, fragmentSrc);

		GL46.glAttachShader(this.ShaderId, vertexShaderId);
		GL46.glAttachShader(this.ShaderId, fragmentShaderId);
		GL46.glLinkProgram(this.ShaderId);

		GL46.glDeleteShader(vertexShaderId);
		GL46.glDeleteShader(fragmentShaderId);

		if (GL46.glGetProgrami(this.ShaderId, GL46.GL_LINK_STATUS) != GL46.GL_TRUE) {
            System.err.println("Could not link shader program: "
                    + GL46.glGetProgramInfoLog(this.ShaderId));
            return;
        }

		GL46.glValidateProgram(this.ShaderId);
	}

	private int CompileShader(ShaderType type, String source) {
		int shaderId = GL46.glCreateShader(InternalShaderTypeToOpenglType(type));

		GL46.glShaderSource(shaderId, source);
		GL46.glCompileShader(shaderId);

		return shaderId;
	}

	// TODO: implmenet binary upload
	public void tests() {
		int length = GL46.glGetProgrami(ShaderId, GL46.GL_PROGRAM_BINARY_LENGTH);

		ByteBuffer binary = ByteBuffer.allocate(length);
		IntBuffer binaryFormat = IntBuffer.allocate(1);

		GL46.glGetProgramBinary(ShaderId, null, binaryFormat, binary);
	
		
	}

	/**
	 * Used to activate the shader for the next draw calls till the shader is
	 * unbinded.
	 */
	public void bind() {
		GL46.glUseProgram(this.ShaderId);
	}

	/**
	 * disables the Shader
	 */
	public void unbind() {
		GL46.glUseProgram(0);
	}

    // ------- Setting uniforms ----------------------------------------------------

    /**
	 * Load a uniforms location into the uniform hashmap
	 * 
	 * @param uniformName the uniform to load
	 */
	private void initUniform(String uniformName) {
		uniforms.put(uniformName, GL46.glGetUniformLocation(this.ShaderId, uniformName));
	}

    /**
	 * Checks the uniform hashmap to see if the uniform exists. If it does not exist
	 * it'll load it
	 * 
	 * @param uniformName the name of the uniform
	 */
	private void chckUniform(String uniformName) {
		if (!uniforms.containsKey(uniformName)) {
			initUniform(uniformName);
		}
	}

	/**
	 * A method to load a float value to a specific location.
	 *
	 * @param location the location to load the float value
	 * @param val      the float value to be loaded
	 */
	public void loadFloat(String location, float val) {
		chckUniform(location);
		GL46.glUniform1f(uniforms.get(location), val);
	}

	/**
	 * Method to load a Vector2f value to a specified location.
	 *
	 * @param location the location to load the value to
	 * @param val      the Vector2f value to load
	 */
	public void loadVector2(String location, Vector2f val) {
		chckUniform(location);
		GL46.glUniform2f(uniforms.get(location), val.x(), val.y());
	}

	/**
	 * Method to load a Vector3f value to a specified location.
	 *
	 * @param location the location to load the value to
	 * @param val      the Vector3f value to load
	 */
	public void loadVector3(String location, Vector3f val) {
		chckUniform(location);
		GL46.glUniform3f(uniforms.get(location), val.x(), val.y(), val.z());
	}

	/**
	 * Loads an integer value into the specified location in the shader program.
	 *
	 * @param location the name of the uniform variable in the shader program
	 * @param val      the integer value to be loaded
	 */
	public void loadInt(String location, int val) {
		chckUniform(location);
		GL46.glUniform1i(uniforms.get(location), val);
	}

	/**
	 * Loads a boolean value into the specified uniform location represented with a
	 * 1 for true and a 0 for false.
	 *
	 * @param location the name of the uniform location
	 * @param v        the boolean value to be loaded
	 */
	public void loadBool(String location, boolean v) {
		chckUniform(location);
		GL46.glUniform1i(uniforms.get(location), v ? 1 : 0);
	}

	/**
	 * Loads a 4x4 matrix into a uniform variable in the shader program.
	 *
	 * @param location the name of the uniform variable
	 * @param val      the matrix to load
	 */
	public void loadMatrix4(String location, Matrix4f val) {
		chckUniform(location);
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer fb = val.get(stack.mallocFloat(16));
			GL46.glUniformMatrix4fv(uniforms.get(location), false, fb);
		}
	}
}
