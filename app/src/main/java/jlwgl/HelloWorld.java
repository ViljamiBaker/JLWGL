package jlwgl;

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
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.system.MemoryUtil.*;
import static jlwgl.ShaderLoader.*;

import org.lwjgl.opengl.GL;
//https://learnopengl.com/Getting-started/OpenGL
//https://www.lwjgl.org/guide
public class HelloWorld {

	boolean wireframe = false;

	private int createVertexArray(float vertices[], int indices[]){
		// create a vertexBuffer to store all of the vertexes in
		int vertexBuffer = glGenBuffers(); 

		// create a vertexArray to make things easier (?)
		int vertexArray = glGenVertexArrays();

		// create an element buffer to allow reusing of the vertexes
		int elementBuffer = glGenBuffers();

		// ..:: Initialization code :: ..
		// 1. bind Vertex Array Object
		glBindVertexArray(vertexArray);
		// 2. copy our vertices array in a vertex buffer for OpenGL to use
		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
		glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);
		// 3. copy our index array in a element buffer for OpenGL to use
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBuffer);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);
		// 4. then set the vertex attributes pointers
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 3 * Float.BYTES, 0);
		glEnableVertexAttribArray(0); 
		return vertexArray;
	}

	public HelloWorld (){
		// makes glfw wake up
		glfwInit();
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
		glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
		// make a window
		long window = glfwCreateWindow(300, 300, "wsg gang", NULL, NULL);
		if(window == NULL){
			glfwTerminate();
			System.exit(1);
		}
		// lets us use glfw and gl commands
		glfwMakeContextCurrent(window);
		GL.createCapabilities();
		glViewport(0, 0, 300, 300);
		glfwSetWindowSizeCallback(window, (windowInner, width, height) -> {
			glViewport(0, 0, width, height);
		});

		// ----------- Creation of the shaderProgram -----------

		int shaderProgram1 = loadShader("shaderVertex", "shaderFrag");

		int shaderProgram2 = loadShader("shaderVertex2", "shaderFrag2");

		// ----------- creation of all of the buffers -----------

		// create the vertexes to use in triangles
		float vertices1[] = {
			-0.1f,  0.5f, 0.0f,  
			-0.1f, -0.5f, 0.0f,  
			-0.7f, -0.5f, 0.0f,  
			-0.7f,  0.5f, 0.0f
		};
		float vertices2[] = {
			// positions          colors
			0.1f,  0.5f, 0.0f,  1.0f, 0.0f, 0.0f,
			0.1f, -0.5f, 0.0f,  0.0f, 1.0f, 0.0f,
			0.7f, -0.5f, 0.0f,  0.0f, 0.0f, 1.0f,
			0.7f,  0.5f, 0.0f,  1.0f, 1.0f, 0.0f,
		};
		// create many triangles with reused indecies for efficientices
		int indices1[] = {
			0, 1, 3,   
			1, 2, 3,  
		};
		int indices2[] = {
			0, 1, 3,   
			1, 2, 3,   
		};  

		// create a vertexBuffer to store all of the vertexes in
		int vertexArray1 = createVertexArray(vertices1, indices1); 
		// create a vertexBuffer to store all of the vertexes in
		int vertexBuffer = glGenBuffers(); 

		// create a vertexArray to make things easier (?)
		int vertexArray2 = glGenVertexArrays();
	
		// create an element buffer to allow reusing of the vertexes
		int elementBuffer = glGenBuffers();
	
		// ..:: Initialization code :: ..
		// 1. bind Vertex Array Object
		glBindVertexArray(vertexArray2);
		// 2. copy our vertices array in a vertex buffer for OpenGL to use
		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
		glBufferData(GL_ARRAY_BUFFER, vertices2, GL_STATIC_DRAW);
		// 3. copy our index array in a element buffer for OpenGL to use
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, elementBuffer);
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices2, GL_STATIC_DRAW);
		// 4. then set the vertex attributes pointers
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 6 * Float.BYTES, 0);
		glEnableVertexAttribArray(0); 
		glVertexAttribPointer(1, 3, GL_FLOAT, false, 6 * Float.BYTES, 3 * Float.BYTES);
		glEnableVertexAttribArray(1); 

		// ----------- render loop ----------- 
		while(!glfwWindowShouldClose(window))
		{
			// input
			processInput(window);

			//render code		
			glClearColor(0.2f, 0.3f, 0.3f, 0f);
			glClear(GL_COLOR_BUFFER_BIT);

			float timeValue = (float)glfwGetTime();
			int time = glGetUniformLocation(shaderProgram1, "time");
			glUseProgram(shaderProgram1);
			glUniform1f(time, timeValue);
			glBindVertexArray(vertexArray1);
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
			glBindVertexArray(0);

			glUseProgram(shaderProgram2);
			glBindVertexArray(vertexArray2);
			glDrawElements(GL_TRIANGLES, 6, GL_UNSIGNED_INT, 0);
			glBindVertexArray(0);

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
		new HelloWorld();
	}
}