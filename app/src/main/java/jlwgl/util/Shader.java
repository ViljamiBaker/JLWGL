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

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Arrays;

import org.joml.Matrix2f;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

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
        glUniform2fv(glGetUniformLocation(ID, name), value.get(ByteBuffer.allocateDirect(2*Float.BYTES).asFloatBuffer()));
    }

    public void setUniform(String name, Vector3f value) {
        glUniform3fv(glGetUniformLocation(ID, name), value.get(ByteBuffer.allocateDirect(3*Float.BYTES).asFloatBuffer()));
    }

    public void setUniform(String name, Vector4f value) {
        glUniform4fv(glGetUniformLocation(ID, name), value.get(ByteBuffer.allocateDirect(4*Float.BYTES).asFloatBuffer()));
    }

    public void setUniform(String name, Matrix2f value) {
        glUniformMatrix2fv(glGetUniformLocation(ID, name), false, value.get(new float[4]));
    }

    public void setUniform(String name, Matrix3f value) {
        glUniformMatrix3fv(glGetUniformLocation(ID, name), false, value.get(new float[9]));
    }

    public void setUniform(String name, Matrix4f value) {
        glUniformMatrix4fv(glGetUniformLocation(ID, name), false, value.get(new float[16]));
    }
}
