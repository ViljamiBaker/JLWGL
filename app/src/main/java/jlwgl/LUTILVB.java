package jlwgl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.GL;

public class LUTILVB {

    public static void init(){
        // makes glfw wake up
		glfwInit();
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		// lets us use glfw and gl commands
		GL.createCapabilities();
    }

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
        String vert = loadAsString(System.getProperty("user.dir") + "\\app\\src\\main\\java\\jlwgl\\shaderPrograms\\" + vertPath + ".vert");
        String frag = loadAsString(System.getProperty("user.dir") + "\\app\\src\\main\\java\\jlwgl\\shaderPrograms\\\\" + fragPath + ".frag");

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
