package jlwgl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glCompileShader;
import static org.lwjgl.opengl.GL20.glCreateShader;
import static org.lwjgl.opengl.GL20.glShaderSource;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.*;
//https://learnopengl.com/Getting-started/OpenGL
//https://www.lwjgl.org/guide
public class HelloWorld {

	public HelloWorld (){
		glfwInit();
		long window = glfwCreateWindow(300, 300, "wsg gang", NULL, NULL);
		if(window == NULL){
			glfwTerminate();
			System.exit(1);
		}
		glfwMakeContextCurrent(window);
		
		glViewport(0, 0, 300, 300);
		glfwSetWindowSizeCallback(window, (windowInner, width, height) -> {
			glViewport(0, 0, width, height);
		});

		
		float vertices[] = {
			-0.5f, -0.5f, 0.0f,
			0.5f, -0.5f, 0.0f,
			0.0f,  0.5f, 0.0f
		};

		int vertexBuffer = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);  
		glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

		String[] vertexShaderSource = {
		"#version 330 core\n",
		"layout (location = 0) in vec3 aPos;\n",
		"void main()\n",
		"{\n",
		"   gl_Position = vec4(aPos.x, aPos.y, aPos.z, 1.0);\n",
		"}\0"};

		long vertexShader = glCreateShader(GL_VERTEX_SHADER);
		glShaderSource(vertexShader, 1, vertexShaderSource, NULL);
		glCompileShader(vertexShader);

		// render loop
		while(!glfwWindowShouldClose(window))
		{
			// input
			processInput(window);

			//render code		
			glClearColor(0.2f, 0.3f, 0.3f, 0f);
			glClear(GL_COLOR_BUFFER_BIT);

			// check events and swap buffers
			glfwSwapBuffers(window);
			glfwPollEvents();
		}
		glfwTerminate();
	}

	public static void processInput(long window)
	{
		if(glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
			glfwSetWindowShouldClose(window, true);
	}

	public static void main(String[] args) {
		new HelloWorld();
	}
}