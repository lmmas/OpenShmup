package engine;

import debug.DebugMethods;
import engine.scene.Scene;
import engine.types.IVec2D;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.io.IOException;
import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

abstract public class Application {
    public static AssetManager assetManager;
    public static InputStatesManager inputStatesManager;
    public static GraphicsManager graphicsManager;
    protected final Runnable initScript;
    protected final Runnable inLoopScript;
    static public Window window;
    protected Callback debugProc;
    public static Scene currentScene;

    public Application(String gameFolderName, Runnable initScript, Runnable inLoopScript) throws IOException {
        this.initScript = initScript;
        this.inLoopScript = inLoopScript;
        GlobalVars.Paths.detectRootFolder();
        GlobalVars.Paths.setcustomGameFolder(gameFolderName);

        OpenGLInitialization();
        graphicsManager = new GraphicsManager();
        assetManager = new AssetManager();
        inputStatesManager = new InputStatesManager(window.getGlfwWindow());
    }

    protected void OpenGLInitialization(){
        GLFWErrorCallback.createPrint(System.err).set();
        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);

        long glfwWindow = glfwCreateWindow(1920, 1080, "OpenShmup", NULL, NULL);
        assert glfwWindow != NULL:"Unable to create GLFW Window";
        window = new Window(glfwWindow);

        glfwMakeContextCurrent(glfwWindow);
        glfwSwapInterval(1);
        GL.createCapabilities();
        debugProc = GLUtil.setupDebugMessageCallback();
        GlobalVars.MAX_TEXTURE_SLOTS = glGetInteger(GL_MAX_TEXTURE_IMAGE_UNITS);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glClearColor(0.0f, 0.0f, 0.0f, 1.0f);


        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
    }

    protected void loop(){
        while (!glfwWindowShouldClose(window.getGlfwWindow())) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            DebugMethods.checkForOpenGLErrors();
            inLoopScript.run();
            inputStatesManager.updateInputStates();
            currentScene.update();
            graphicsManager.drawGraphics();
            glfwSwapBuffers(window.getGlfwWindow());
            glfwPollEvents();
        }
    }

    protected void terminate(){
        Callbacks.glfwFreeCallbacks(window.getGlfwWindow());
        glfwDestroyWindow(window.getGlfwWindow());

        glfwTerminate();
        debugProc.free();
    }
}
