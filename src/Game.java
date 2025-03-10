import debug.DebugMethods;
import edit.GameParameters;
import engine.PlayerSettings;
import engine.GlobalVars;
import engine.TestScene;
import engine.Scene;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Game {
    private long glfwWindow;

    private Scene currentScene;
    public void run(){
        GameParameters.useDefaultParameters();
        init();
        loop();

        Callbacks.glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(){
        GLFWErrorCallback.createPrint(System.err).set();
        assert glfwInit(): "Unable to initialize GLFW";

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

        PlayerSettings.setResolution(1000, 1200);
        glfwWindow = glfwCreateWindow(PlayerSettings.getWindowWidth(), PlayerSettings.getWindowHeight(), "OpenShmup", NULL, NULL);
        assert glfwWindow != NULL:"Unable to create GLFW Window";

        glfwSetKeyCallback(glfwWindow, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true);
        });
        glfwSetFramebufferSizeCallback(glfwWindow, (window, width, height) -> {
            glViewport(0, 0, width, height);  // Adjust the viewport to the new window size
        });

        try(MemoryStack stack = stackPush()){
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(glfwWindow, pWidth, pHeight);

            glfwSetWindowPos(glfwWindow,(vidmode.width() - pWidth.get(0)) / 2,(vidmode.height() - pHeight.get(0)) / 2);

            glfwMakeContextCurrent(glfwWindow);
            glfwSwapInterval(0);
            GL.createCapabilities();
            glGetInteger(GL_MAX_TEXTURE_IMAGE_UNITS);
            glViewport(0, 0, PlayerSettings.getWindowWidth(), PlayerSettings.getWindowHeight());
            org.lwjgl.system.Callback debugProc = GLUtil.setupDebugMessageCallback();
            GlobalVars.MAX_TEXTURE_SLOTS = glGetInteger(GL_MAX_TEXTURE_IMAGE_UNITS);
            glEnable(GL_BLEND);
            glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glfwShowWindow(glfwWindow);
        }
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
    }

    public void loop(){
        TestScene testScene = new TestScene(glfwWindow);
        while(!glfwWindowShouldClose(glfwWindow)) {
            glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
            DebugMethods.checkForOpenGLErrors();
            testScene.update();
            testScene.draw();
            glfwSwapBuffers(glfwWindow);
            glfwPollEvents();
        }
    }

    public static void main(String[] args){
        new Game().run();
    }
}
