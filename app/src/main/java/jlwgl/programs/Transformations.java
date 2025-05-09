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
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.GL_TEXTURE1;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import jlwgl.util.*;
//https://learnopengl.com/Lighting/Colors
public class Transformations {
    boolean wireframe = false;
	boolean pDownLast = false;
	boolean lockMouse = true;
	boolean lDownLast = false;
	float deltaTime = 0.0f;	// Time between current frame and last frame
	float lastFrame = 0.0f; // Time of last frame
	Camera camera;
    public Transformations(){

        init();

        long window = createWindow(800, 600, "Colors");
		glEnable(GL_DEPTH_TEST);  
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		enableDebug();

		camera = new Camera(window);

        Shader shader = new Shader("shaderVertexTransformations", "shaderFragTransformations");

        float vertices[] = {
			-0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
			 0.5f, -0.5f, -0.5f,  1.0f, 0.0f,
			 0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
			 0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
			-0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
			-0.5f, -0.5f, -0.5f,  0.0f, 0.0f,
		
			-0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
			 0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
			 0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
			 0.5f,  0.5f,  0.5f,  1.0f, 1.0f,
			-0.5f,  0.5f,  0.5f,  0.0f, 1.0f,
			-0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
		
			-0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
			-0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
			-0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
			-0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
			-0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
			-0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
		
			 0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
			 0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
			 0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
			 0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
			 0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
			 0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
		
			-0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
			 0.5f, -0.5f, -0.5f,  1.0f, 1.0f,
			 0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
			 0.5f, -0.5f,  0.5f,  1.0f, 0.0f,
			-0.5f, -0.5f,  0.5f,  0.0f, 0.0f,
			-0.5f, -0.5f, -0.5f,  0.0f, 1.0f,
		
			-0.5f,  0.5f, -0.5f,  0.0f, 1.0f,
			 0.5f,  0.5f, -0.5f,  1.0f, 1.0f,
			 0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
			 0.5f,  0.5f,  0.5f,  1.0f, 0.0f,
			-0.5f,  0.5f,  0.5f,  0.0f, 0.0f,
			-0.5f,  0.5f, -0.5f,  0.0f, 1.0f
		};

		//int vertexArray = createVertexArray(vertices, indecies);
		int vertexArray = createVertexArray(vertices);
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
		Matrix4f projection = new Matrix4f().perspective(1.0f, 300.0f / 300.0f, 0.1f, 100.0f);
		Matrix4f model = new Matrix4f().rotate((float)glfwGetTime() * 1f, new Vector3f(0.5f, 1.0f, 0.0f));
		Matrix4f view = new Matrix4f();

		Vector3f[] cubePositions = {
			new Vector3f( 0.0f,  0.0f,  0.0f), 
			new Vector3f( 2.0f,  5.0f, -15.0f), 
			new Vector3f(-1.5f, -2.2f, -2.5f),  
			new Vector3f(-3.8f, -2.0f, -12.3f),  
			new Vector3f( 2.4f, -0.4f, -3.5f),  
			new Vector3f(-1.7f,  3.0f, -7.5f),  
			new Vector3f( 1.3f, -2.0f, -2.5f),  
			new Vector3f( 1.5f,  2.0f, -2.5f), 
			new Vector3f( 1.5f,  0.2f, -1.5f), 
			new Vector3f(-1.3f,  1.0f, -1.5f)  
		};
		glActiveTexture(GL_TEXTURE0);
		tex1.bind();
		glActiveTexture(GL_TEXTURE1);
		tex2.bind();
		shader.setInt("texture1", (int)0);
		shader.setInt("texture2", (int)1);
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

			shader.use();
			if(!camera.projectionGood()){
				projection = camera.getProjection();
				shader.setUniform("projection", projection);
			}
			view = camera.getVeiw();
			shader.setUniform("view", view);
			for (int i = 0; i < cubePositions.length; i++) {
				float angle = 20.0f * i + (float)glfwGetTime(); 
				model = new Matrix4f().translate(cubePositions[i]).rotate(angle, new Vector3f(1.0f, 0.3f, 0.5f).normalize()); 
				shader.setUniform("model", model);
				shader.setFloat("mixpercent", (float)Math.sin(angle)/2.0f+0.5f);
				//shader.setFloat("time", (float)glfwGetTime());
				glBindVertexArray(vertexArray);
				glDrawArrays(GL_TRIANGLES, 0, 36);
			}

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
        new Transformations();
    }
}
