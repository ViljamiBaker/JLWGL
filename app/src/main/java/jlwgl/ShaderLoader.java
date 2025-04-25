package jlwgl;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.GL_COMPILE_STATUS;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glAttachShader;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateProgram;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glDeleteShader;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;
import static org.lwjgl.opengl.GL20.glGetShaderi;
import static org.lwjgl.opengl.GL20.glLinkProgram;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL20.glValidateProgram;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ShaderLoader {
    private ShaderLoader(){}

    public static String loadAsString(String location){
        StringBuilder result = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(location));
            String buffer = "";
            while ((buffer = reader.readLine())!=null) {
                result.append(buffer);
                result.append("\n");
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(e);
        }
        return result.toString();
    }

    public static int loadShader(String vertPath, String fragPath){
        String vert = loadAsString(System.getProperty("user.dir") + "\\app\\src\\main\\java\\jlwgl\\" + vertPath + ".vert");
        String frag = loadAsString(System.getProperty("user.dir") + "\\app\\src\\main\\java\\jlwgl\\" + fragPath + ".frag");

        return create(vert,frag);
    }

    public static int create(String vert, String frag){

        int program = glCreateProgram();

        int vertId = glCreateShader(GL_VERTEX_SHADER);
        int fragId = glCreateShader(GL_FRAGMENT_SHADER);

        glShaderSource(vertId, vert);
        glShaderSource(fragId, frag);

        glCompileShader(vertId);
        if(glGetShaderi(vertId,GL_COMPILE_STATUS) == GL_FALSE){
            System.out.println("vertex shader compile fail :(");
            System.out.println(glGetShaderInfoLog(vertId));
        }

        glCompileShader(fragId);

        if(glGetShaderi(fragId,GL_COMPILE_STATUS) == GL_FALSE){
            System.out.println("fragment shader compile fail :(");
            System.out.println(glGetShaderInfoLog(fragId));
        }

        glAttachShader(program, vertId);
        glAttachShader(program, fragId);

        glLinkProgram(program);
        glValidateProgram(program);

        glDeleteShader(vertId);
        glDeleteShader(fragId);

        return program;
    }
}
