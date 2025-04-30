package jlwgl.util;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;

import org.joml.Vector3f;

public class Camera {
    public Camera(long window){
        glfwSetCursorPosCallback(window, (windowInner, xpos, ypos)->{
			if (firstMouse) // initially set to true
			{
				lastX = (float)xpos;
				lastY = (float)ypos;
				firstMouse = false;
			}

			float xoffset = (float)xpos - lastX;
			float yoffset = lastY - (float)ypos;
			lastX = (float)xpos;
			lastY = (float)ypos;
			
			float sensitivity = 0.002f;
			xoffset *= sensitivity;
			yoffset *= sensitivity;

			yaw   += xoffset;
			pitch += yoffset;  

			if(pitch > 1.55f)
			pitch =  1.55f;
			if(pitch < -1.55f)
			pitch = -1.55f;

			Vector3f direction = new Vector3f();
			direction.x = (float)Math.cos(yaw) * (float)Math.cos(pitch);
			direction.y = (float)Math.sin(pitch);
			direction.z = (float)Math.sin(yaw) * (float)Math.cos(pitch);
			cameraFront = direction.normalize();
		}); 
    }
}
