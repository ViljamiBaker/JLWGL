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
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glDrawArrays;
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
import org.lwjgl.glfw.GLFWWindowPosCallbackI;
import org.lwjgl.opengl.GLDebugMessageCallback;
import jlwgl.util.*;
//https://learnopengl.com/Getting-started/Transformations
public class Transformations {
    boolean wireframe = false;
	boolean pDownLast = false;
	Vector3f cameraPos = new Vector3f(0.0f, 0.0f,  3.0f);
	Vector3f cameraFront = new Vector3f(0.0f, 0.0f, -1.0f);
	Vector3f cameraUp = new Vector3f(0.0f, 1.0f,  0.0f);
	float fov = 1.0f;
	float yaw = 0;
	float pitch = 0;
	float lastX = 400, lastY = 300;
	float deltaTime = 0.0f;	// Time between current frame and last frame
	float lastFrame = 0.0f; // Time of last frame
	boolean firstMouse = false;
    public Transformations(){

        init();

        long window = createWindow(300, 300, "Transformations");
		glEnable(GL_DEPTH_TEST);  
		glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
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

		int[] indecies = {
			0, 1, 3,   
			1, 2, 3,  
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
		Matrix4f view = new Matrix4f().translate(new Vector3f(0, 0, -3));

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
		glfwSetWindowPosCallback(window, (windowInner, xpos, ypos)->{
			if (firstMouse) // initially set to true
			{
				lastX = xpos;
				lastY = ypos;
				firstMouse = false;
			}

			float xoffset = xpos - lastX;
			float yoffset = lastY - ypos; // reversed since y-coordinates range from bottom to top
			lastX = xpos;
			lastY = ypos;
			
			float sensitivity = 0.1f;
			xoffset *= sensitivity;
			yoffset *= sensitivity;

			yaw   += xoffset;
			pitch += yoffset;  

			if(pitch > 3.0f)
			pitch =  3.0f;
			if(pitch < -3.0f)
			pitch = -3.0f;

			System.out.println(xpos);
			Vector3f direction = new Vector3f();
			direction.x = (float)Math.cos(yaw) * (float)Math.cos(pitch);
			direction.y = (float)Math.sin(pitch);
			direction.z = (float)Math.sin(yaw) * (float)Math.cos(pitch);
			cameraFront = direction.normalize();
		});  

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
			int[] width = new int[1];
			int[] height = new int[1];
			glfwGetWindowSize(window, width, height);

			//Vector3f cameraTarget = new Vector3f(0.0f, 0.0f, 0.0f);
			//Vector3f cameraDirection = pos.sub(cameraTarget).normalize();

			//Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f); 
			//Vector3f cameraRight = up.cross(cameraDirection).normalize();
			//Vector3f cameraUp = cameraDirection.cross(cameraRight);

			projection = new Matrix4f().perspective(fov, (float)width[0]/(float)height[0], 0.1f, 100.0f).lookAt(cameraPos, cameraPos.add(cameraFront, new Vector3f()), cameraUp);
			shader.setUniform("projection", projection);
			view = new Matrix4f();//.translate(pos);
			shader.setUniform("view", view);
			for (int i = 0; i < cubePositions.length; i++) {
				float angle = 20.0f * i + (float)glfwGetTime(); 
				model = new Matrix4f().translate(cubePositions[i]).rotate(angle, new Vector3f(1.0f, 0.3f, 0.5f)); 
				shader.setUniform("model", model);
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
		pDownLast = glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS;
		float cameraSpeed = 2.5f * deltaTime; // adjust accordingly
		if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS){
			cameraPos.add(cameraFront.mul(cameraSpeed, new Vector3f()));
		}
		if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS){
			cameraPos.sub(cameraFront.mul(cameraSpeed, new Vector3f()));
		}
		if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS){
			cameraPos.sub((cameraFront.cross(cameraUp, new Vector3f())).normalize().mul(cameraSpeed,new Vector3f()));
		}
		if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS){
			cameraPos.add((cameraFront.cross(cameraUp, new Vector3f())).normalize().mul(cameraSpeed,new Vector3f()));
		}
		if(glfwGetKey(window, GLFW_KEY_R)==GLFW_PRESS){
			fov += 0.05f;
		}
		if(glfwGetKey(window, GLFW_KEY_F)==GLFW_PRESS){
			fov -= 0.05f;
		}
	}

    public static void main(String[] args) {
        new Transformations();
    }
}
