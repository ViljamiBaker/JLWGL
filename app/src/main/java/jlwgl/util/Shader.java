package jlwgl.util;

import static jlwgl.util.LUTILVB.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2fv;
import static org.lwjgl.opengl.GL20.glUniform3fv;
import static org.lwjgl.opengl.GL20.glUniform4fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix2fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix3fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glUseProgram;

import java.nio.FloatBuffer;

import org.lwjgl.system.MemoryStack;

import external.math.Matrix2f;
import external.math.Matrix3f;
import external.math.Matrix4f;
import external.math.Vector2f;
import external.math.Vector3f;
import external.math.Vector4f;

public class Shader {
    int ID;

    public Shader(String vert, String frag){
        ID = loadShader(vert, frag);
    }

    public void use(){
        glUseProgram(ID);
    }

    public void setBool(String name, boolean value)
    {         
        glUniform1i(glGetUniformLocation(ID, name), (value ? 1 : 0)); 
    }
    public void setInt(String name, int value)
    { 
        glUniform1i(glGetUniformLocation(ID, name), value); 
    }
    public void setFloat(String name, float value)
    { 
        glUniform1f(glGetUniformLocation(ID, name), value); 
    } 
    public int getID() {
        return ID;
    }

    public void setUniform(String name, Vector2f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(2);
            value.toBuffer(buffer);
            glUniform2fv(glGetUniformLocation(ID, name), buffer);
        }
    }

    public void setUniform(String name, Vector3f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(3);
            value.toBuffer(buffer);
            glUniform3fv(glGetUniformLocation(ID, name), buffer);
        }
    }

    public void setUniform(String name, Vector4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(4);
            value.toBuffer(buffer);
            glUniform4fv(glGetUniformLocation(ID, name), buffer);
        }
    }

    public void setUniform(String name, Matrix2f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(2 * 2);
            value.toBuffer(buffer);
            glUniformMatrix2fv(glGetUniformLocation(ID, name), false, buffer);
        }
    }

    public void setUniform(String name, Matrix3f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(3 * 3);
            value.toBuffer(buffer);
            glUniformMatrix3fv(glGetUniformLocation(ID, name), false, buffer);
        }
    }

    public void setUniform(String name, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer buffer = stack.mallocFloat(4 * 4);
            value.toBuffer(buffer);
            glUniformMatrix4fv(glGetUniformLocation(ID, name), false, buffer);
        }
    }
}
