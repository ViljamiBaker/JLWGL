package jlwgl;

import static jlwgl.LUTILVB.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL43.*;

import org.lwjgl.opengl.GLDebugMessageCallback;

//https://learnopengl.com/Getting-started/Textures
public class Textures {
    boolean wireframe = false;
    public Textures(){
        init();
        long window = createWindow(300, 300, "Textures");

		glEnable(GL_DEBUG_OUTPUT);
        glDebugMessageCallback(GLDebugMessageCallback.create(
            (source, type, id, severity, length, message, userParam) -> {
            System.err.println("OpenGL Debug Message:");
            System.err.println("    Source: " + source);
            System.err.println("    Type: " + type);
            System.err.println("    ID: " + id);
            System.err.println("    Severity: " + severity);
            System.err.println("    Message: " + GLDebugMessageCallback.getMessage(length, message));
            }
        ),0);

		Shader shader = new Shader("shaderVertexTexture", "shaderFragTexture");

		float[] vertices = {
			// positions          // colors           // texture coords
			 0.5f,  0.5f, 0.0f,   1.0f, 0.0f, 0.0f,   1.0f, 1.0f,   // top right
			 0.5f, -0.5f, 0.0f,   0.0f, 1.0f, 0.0f,   1.0f, 0.0f,   // bottom right
			-0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 0.0f,   // bottom left
			-0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 1.0f    // top left 
		};

		int[] indecies = {
			0, 1, 3,   
			1, 2, 3,  
		};

		int vertexArray = createVertexArray(vertices, indecies);
		glBindVertexArray(vertexArray);
		// pos
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * Float.BYTES, 0);
		glEnableVertexAttribArray(0); 
		
		// color
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * Float.BYTES, Float.BYTES*3);
		glEnableVertexAttribArray(0); 
		
		// textureCoords
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, Float.BYTES*6);
		glEnableVertexAttribArray(0); 
		
		Texture tex = new Texture(System.getProperty("user.dir") + "\\app\\src\\main\\java\\jlwgl\\textures\\normal.jpg");
		tex.bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

		glGenerateMipmap(GL_TEXTURE_2D);

		shader.use();
		shader.setInt("ourTexture", 0);

        while(!glfwWindowShouldClose(window))
		{
			// input
			processInput(window);

			//render code		
			glClearColor(0.2f, 0.3f, 0.3f, 0f);
			glClear(GL_COLOR_BUFFER_BIT);

			shader.use();
			glActiveTexture(GL_TEXTURE0);
			tex.bind();
			glBindVertexArray(vertexArray);
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);

			// check events and swap buffers
			glfwSwapBuffers(window);
			glfwPollEvents();
		}
		glfwTerminate();
    }

    public void processInput(long window)
	{
		if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
			glfwSetWindowShouldClose(window, true);
		
		if(glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS){
			if(!wireframe){
				glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			}else{
				glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			}
			wireframe = !wireframe;
		}
	}

    public static void main(String[] args) {
        new Textures();
    }
}
