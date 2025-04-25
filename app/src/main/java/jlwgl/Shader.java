package jlwgl;

import static jlwgl.ShaderLoader.*;
import static org.lwjgl.opengl.GL20.glUseProgram;

public class Shader {
    int ID;

    public Shader(String vert, String frag){
        ID = loadShader(vert, frag);
    }

    public void use(){
        glUseProgram(ID);
    }

    void setBool(String name, boolean value){

    }
}
