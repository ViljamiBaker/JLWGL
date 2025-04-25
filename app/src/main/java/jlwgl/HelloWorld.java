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

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Scanner;

import org.lwjgl.PointerBuffer;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL33;
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

	private void checkShader(int shader, String name){
		int[]  success = new int[1];
		GL33.glGetShaderiv(shader, GL_COMPILE_STATUS, success);
		if(success[0]==0)
		{
			String info = GL33.glGetShaderInfoLog(shader,512);
			System.out.println("ERROR::SHADER::"+name+"::COMPILATION_FAILED\n" + info);
		}
	}

	private int createShaderProgram(String vertexShaderFile, String fragmentShaderFile){
		// create a vertex shader to move vertexes around
		String vertexShaderSource = null;
		ArrayList<Integer> lengths = new ArrayList<>();
		try  {
			File i = new File(System.getProperty("user.dir") + "\\app\\src\\main\\java\\jlwgl\\" + vertexShaderFile + ".vert");
			Scanner sc = new Scanner(i);
			StringBuilder sb = new StringBuilder();
			String line = sc.nextLine();

			while (sc.hasNextLine()) {
				line = sc.nextLine() + "\n";
				sb.append(line);
				lengths.add(line.length());
			}
			vertexShaderSource = sb.toString();
			sc.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		int vertexShader = glCreateShader(GL_VERTEX_SHADER);
		GL33.glShaderSource(vertexShader, PointerBuffer.create(ByteBuffer.wrap(vertexShaderSource.getBytes(StandardCharsets.UTF_8))), IntBuffer.wrap(NULL));
		GL33.glCompileShader(vertexShader);
		// error logging if it dont work
		checkShader(vertexShader,"Vertex");

		// create a fragment shader to color in the triangles based off of what the vertex shader says
		String fragmentShaderSource = null;

		try  {
			File i = new File(System.getProperty("user.dir") + "\\app\\src\\main\\java\\jlwgl\\" + fragmentShaderFile + ".frag");
			Scanner sc = new Scanner(i);
			StringBuilder sb = new StringBuilder();
			String line = sc.nextLine();

			while (sc.hasNextLine()) {
				line = sc.nextLine();
				sb.append(line);
				sb.append(System.lineSeparator());
			}
			fragmentShaderSource = sb.toString();
			sc.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

		int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
		GL33.glShaderSource(fragmentShader, fragmentShaderSource);
		GL33.glCompileShader(fragmentShader);	
		// error logging if it dont work
		checkShader(fragmentShader,"Fragment");

		// create a shader program using our vertex and frag shaders
		int shaderProgram = glCreateProgram();

		glAttachShader(shaderProgram, vertexShader);
		glAttachShader(shaderProgram, fragmentShader);
		glLinkProgram(shaderProgram);

		glDeleteShader(vertexShader);
		glDeleteShader(fragmentShader);
		return shaderProgram;
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

		int shaderProgram1 = createShaderProgram("shaderVertex", "shaderFrag");

		int shaderProgram2 = createShaderProgram("shaderVertex2", "shaderFrag2");

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
			0.1f, -0.5f, 0.0f,  1.0f, 0.0f, 0.0f,
			0.7f, -0.5f, 0.0f,  1.0f, 0.0f, 0.0f,
			0.7f,  0.5f, 0.0f,  1.0f, 0.0f, 0.0f,
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
		int vertexArray2 = createVertexArray(vertices2, indices2); 

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