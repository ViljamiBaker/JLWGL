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
import static org.lwjgl.opengl.GL20.glGetUniformLocation;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL43.*;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GLDebugMessageCallback;

import external.math.*;
import jlwgl.util.*;
//https://learnopengl.com/Getting-started/Transformations
public class Transformations {
    boolean wireframe = false;
    float offset = 0.0f;
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
		glEnableVertexAttribArray(1); 
		
		// textureCoords
		glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * Float.BYTES, Float.BYTES*6);
		glEnableVertexAttribArray(2); 

        Texture tex1 = new Texture("normal.jpg");
        Texture tex2 = new Texture("image lol.png");

        Matrix4f trans = new Matrix4f();
        trans.multiply(Matrix4f.rotate((float)Math.PI, new Vector3f(0, 0, 1)));
        trans.multiply(Matrix4f.scale(new Vector3f(0.5f, 0.5f, 0.5f)));
        shader.use();
        shader.setUniform("transform", trans);
        while(!glfwWindowShouldClose(window))
		{
			// input
			processInput(window);

			//render code		
			glClearColor(0.2f, 0.3f, 0.3f, 0f);
			glClear(GL_COLOR_BUFFER_BIT);

			shader.use();
			glActiveTexture(GL_TEXTURE0);
			tex1.bind();
			glActiveTexture(GL_TEXTURE1);
			tex2.bind();
			//shader.setFloat("time", (float)glfwGetTime());
			shader.setFloat("offset", offset);
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
		if(glfwGetKey(window, GLFW_KEY_UP)==GLFW_PRESS){
			offset += 0.01f;
		}
		if(glfwGetKey(window, GLFW_KEY_DOWN)==GLFW_PRESS){
			offset -= 0.01f;
		}
	}

    public static void main(String[] args) {
        new Transformations();
    }
}
