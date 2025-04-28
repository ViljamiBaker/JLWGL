package jlwgl.programs;
import static jlwgl.util.LUTILVB.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL43.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GLDebugMessageCallback;
import jlwgl.util.*;
//https://learnopengl.com/Getting-started/Transformations
public class Transformations {
    boolean wireframe = false;
    public Transformations(){

        init();

        long window = createWindow(300, 300, "Transformations");

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

        Shader shader = new Shader("shaderVertexTransformations", "shaderFragTransformations");

        float[] vertices = {
			// positions        // texture coords
			 0.5f,  0.5f, 0.0f, 1.0f, 1.0f,   // top right
			 0.5f, -0.5f, 0.0f, 1.0f, 0.0f,   // bottom right
			-0.5f, -0.5f, 0.0f, 0.0f, 0.0f,   // bottom left
			-0.5f,  0.5f, 0.0f, 0.0f, 1.0f    // top left 
		};

		int[] indecies = {
			0, 1, 3,   
			1, 2, 3,  
		};

		int vertexArray = createVertexArray(vertices, indecies);
		glBindVertexArray(vertexArray);
		// pos
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
		glEnableVertexAttribArray(0); 
		
		// textureCoords
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, Float.BYTES*3);
		glEnableVertexAttribArray(1); 

        Texture tex1 = new Texture("normal.jpg");
        Texture tex2 = new Texture("image lol.png");

        shader.use();
        while(!glfwWindowShouldClose(window))
		{
			// input
			processInput(window);

			//render code		
			glClearColor(0.2f, 0.3f, 0.3f, 0f);
			glClear(GL_COLOR_BUFFER_BIT);

			shader.use();
			Matrix4f model = new Matrix4f().rotate(0.2f, new Vector3f(-1, 0, 0));
			Matrix4f veiw = new Matrix4f().translate(new Vector3f(0, 0, -9));
			Matrix4f projection = new Matrix4f().perspective(2.0f, 300.0f / 300.0f, 0.1f, 100.0f);
			shader.setUniform("model", model);
			shader.setUniform("veiw", veiw);
			shader.setUniform("projection", projection);
			glActiveTexture(GL_TEXTURE0);
			tex1.bind();
			glActiveTexture(GL_TEXTURE1);
			tex2.bind();
			//shader.setFloat("time", (float)glfwGetTime());
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
        new Transformations();
    }
}
