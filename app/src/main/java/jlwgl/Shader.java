package jlwgl;

import static jlwgl.LUTILVB.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniform1f;
import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Shader {
    int ID;

    public Shader(String vert, String frag){
        ID = loadShader(vert, frag);
    }

    public void use(){
        glUseProgram(ID);
    }

    void setBool(String name, boolean value)
    {         
        glUniform1i(glGetUniformLocation(ID, name), (value ? 1 : 0)); 
    }
    void setInt(String name, int value)
    { 
        glUniform1i(glGetUniformLocation(ID, name), value); 
    }
    void setFloat(String name, float value)
    { 
        glUniform1f(glGetUniformLocation(ID, name), value); 
    } 
}
