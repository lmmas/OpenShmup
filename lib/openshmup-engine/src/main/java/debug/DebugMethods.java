package debug;

import static org.lwjgl.opengl.GL33.*;
final public class DebugMethods {
    static public void checkForOpenGLErrors(){
        int error = glGetError();
        if (error != GL_NO_ERROR) {
            System.out.println("OpenGL Error: " + error);
        }
    }
}