package jlwgl.programs;
import static jlwgl.util.LUTILVB.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import jlwgl.util.*;
//https://learnopengl.com/Lighting/Colors
public class Colors {
    boolean wireframe = false;
	boolean pDownLast = false;
	boolean lockMouse = true;
	boolean lDownLast = false;
	float deltaTime = 0.0f;	// Time between current frame and last frame
	float lastFrame = 0.0f; // Time of last frame
	Camera camera;
    public Colors(){

        init();

        long window = createWindow(800, 600, "Colors");
		glEnable(GL_DEPTH_TEST);  
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		enableDebug();

		camera = new Camera(window);

        float vertices[] = {
			-0.5f, -0.5f, -0.5f,
			 0.5f, -0.5f, -0.5f,
			 0.5f,  0.5f, -0.5f,
			 0.5f,  0.5f, -0.5f,
			-0.5f,  0.5f, -0.5f,
			-0.5f, -0.5f, -0.5f,
		
			-0.5f, -0.5f,  0.5f,
			 0.5f, -0.5f,  0.5f,
			 0.5f,  0.5f,  0.5f,
			 0.5f,  0.5f,  0.5f,
			-0.5f,  0.5f,  0.5f,
			-0.5f, -0.5f,  0.5f,
		
			-0.5f,  0.5f,  0.5f,
			-0.5f,  0.5f, -0.5f,
			-0.5f, -0.5f, -0.5f,
			-0.5f, -0.5f, -0.5f,
			-0.5f, -0.5f,  0.5f,
			-0.5f,  0.5f,  0.5f,
		
			 0.5f,  0.5f,  0.5f,
			 0.5f,  0.5f, -0.5f,
			 0.5f, -0.5f, -0.5f,
			 0.5f, -0.5f, -0.5f,
			 0.5f, -0.5f,  0.5f,
			 0.5f,  0.5f,  0.5f,
		
			-0.5f, -0.5f, -0.5f,
			 0.5f, -0.5f, -0.5f,
			 0.5f, -0.5f,  0.5f,
			 0.5f, -0.5f,  0.5f,
			-0.5f, -0.5f,  0.5f,
			-0.5f, -0.5f, -0.5f,
		
			-0.5f,  0.5f, -0.5f,
			 0.5f,  0.5f, -0.5f,
			 0.5f,  0.5f,  0.5f,
			 0.5f,  0.5f,  0.5f,
			-0.5f,  0.5f,  0.5f,
			-0.5f,  0.5f, -0.5f,
		};

		Matrix4f projection = new Matrix4f().perspective(1.0f, 300.0f / 300.0f, 0.1f, 100.0f);
		Matrix4f view = new Matrix4f();

		int cubeVertexArray = glGenVertexArrays();
		int vertexBuffer = glGenBuffers();

		glBindBuffer(GL_ARRAY_BUFFER, vertexBuffer);
    	glBufferData(GL_ARRAY_BUFFER, vertices, GL_STATIC_DRAW);

		glBindVertexArray(cubeVertexArray);

		glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES*3, 0);
    	glEnableVertexAttribArray(0);

		int lightCubeVertexArray = glGenVertexArrays();

		glBindVertexArray(lightCubeVertexArray);


		glVertexAttribPointer(0, 3, GL_FLOAT, false, Float.BYTES*3, 0);
    	glEnableVertexAttribArray(0);


        Shader lightingShader = new Shader("shaderVertexColors", "shaderFragColors");

		Shader shaderLC = new Shader("shaderVertexColors", "shaderFragColorsLC");
		
        while(!glfwWindowShouldClose(window))
		{
			float currentFrame = (float)glfwGetTime();
			deltaTime = currentFrame - lastFrame;
			lastFrame = currentFrame;  

			// input
			processInput(window);

			//render code		
			glClearColor(0.2f, 0.3f, 0.3f, 0f);
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

			lightingShader.use();
			if(!camera.projectionGood()){
				projection = camera.getProjection();
				lightingShader.setUniform("projection", projection);
			}
			view = camera.getVeiw();
			lightingShader.setUniform("view", view);

			// be sure to activate shader when setting uniforms/drawing objects
			lightingShader.use();		
			lightingShader.setUniform("objectColor", new Vector3f(0.0f, 0.49f, 0.31f));
			lightingShader.setUniform("lightColor",  new Vector3f(0.0f, 0.49f, 0.31f));

			// world transformation
			Matrix4f model = new Matrix4f();
			lightingShader.setUniform("model", model);

			// render the cube
			glBindVertexArray(cubeVertexArray);
			glDrawArrays(GL_TRIANGLES, 0, 36);


			// also draw the lamp object
			shaderLC.use();
			shaderLC.setUniform("projection", projection);
			shaderLC.setUniform("view", view);
			model = new Matrix4f();
			model.translate(new Vector3f(1,1,3));
			model.scale(new Vector3f(0.2f)); // a smaller cube
			shaderLC.setUniform("model", model);

			glBindVertexArray(lightCubeVertexArray);
			glDrawArrays(GL_TRIANGLES, 0, 36);

			

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
		
		if(glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS&&!pDownLast){
			if(!wireframe){
				glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			}else{
				glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			}
			wireframe = !wireframe;
		}
		if(glfwGetKey(window, GLFW_KEY_L) == GLFW_PRESS&&!lDownLast){
			System.out.println(lockMouse);
			if(lockMouse){
				lockMouse = false;
				glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
			}else{
				lockMouse = true;
				glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
			}
			camera.setMouseMoveEnabled(lockMouse);
		}
		lDownLast = glfwGetKey(window, GLFW_KEY_L) == GLFW_PRESS;
		pDownLast = glfwGetKey(window, GLFW_KEY_P) == GLFW_PRESS;
		if(lockMouse){
			camera.processInput(deltaTime);
		}
	}

    public static void main(String[] args) {
        new Colors();
    }
}
