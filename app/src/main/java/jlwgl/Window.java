package jlwgl;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Window {
    
    long ID;

    public Window(int sizex, int sizey, String name){
		// make a window
		long window = glfwCreateWindow(300, 300, name, NULL, NULL);
		if(window == NULL){
			glfwTerminate();
			System.exit(1);
		}
		glfwMakeContextCurrent(window);
		glViewport(0, 0, sizex, sizey);
		glfwSetWindowSizeCallback(window, (windowInner, width, height) -> {
			glViewport(0, 0, width, height);
		});
        ID = window;
    }

    public void setSize(int x, int y, int w, int h){
        glfwMakeContextCurrent(ID);
		glViewport(x,y,w,h);
    }

    public void makeCurrentContext(){
        glfwMakeContextCurrent(ID);
    }
}
