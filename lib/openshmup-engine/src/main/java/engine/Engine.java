package engine;

import debug.DebugMethods;
import engine.assets.AssetManager;
import engine.graphics.GraphicsManager;
import engine.scene.Scene;
import engine.types.IVec2D;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;

import java.io.IOException;
import java.net.URISyntaxException;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Engine {
    public static AssetManager assetManager;
    public static InputStatesManager inputStatesManager;
    public static GraphicsManager graphicsManager;
    private static Runnable inLoopScript;
    static public Window window;
    static private IVec2D nativeResolution;
    protected static Callback debugProc;
    public static Scene currentScene;
    private static boolean programShouldTerminate = false;

    public Engine() throws IOException {
        Engine.inLoopScript = null;
        nativeResolution = new IVec2D(1920, 1080);
        detectRootFolder();

        OpenGLInitialization();
        graphicsManager = new GraphicsManager();
        assetManager = new AssetManager();
        inputStatesManager = new InputStatesManager(window.getGlfwWindow());
    }

    private void detectRootFolder() {
        try {
            String rootFolderAbsolutePath = java.nio.file.Paths.get(Engine.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().getParent().getParent().getParent().toString();
            GlobalVars.Paths.rootFolderAbsolutePath = rootFolderAbsolutePath;
            System.out.println(rootFolderAbsolutePath);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }

    private void OpenGLInitialization() {
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
        assert glfwWindow != NULL : "Unable to create GLFW Window";
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

    public static void setProgramShouldTerminate() {
        programShouldTerminate = true;
    }

    protected void loop() {
        while (!programShouldTerminate) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            DebugMethods.checkForOpenGLErrors();
            if (inLoopScript != null) {
                inLoopScript.run();
            }
            inputStatesManager.updateInputStates();
            if (currentScene != null) {
                currentScene.handleInputs();
                currentScene.update();
            }
            graphicsManager.drawGraphics();
            glfwSwapBuffers(window.getGlfwWindow());
            glfwPollEvents();
            if (glfwWindowShouldClose(window.getGlfwWindow())) {
                programShouldTerminate = true;
            }
        }
    }

    protected void terminate() {
        Callbacks.glfwFreeCallbacks(window.getGlfwWindow());
        glfwDestroyWindow(window.getGlfwWindow());
        glfwTerminate();
        debugProc.free();
    }

    public void run() {
        loop();
        terminate();
    }

    public static void setInLoopScript(Runnable script) {
        inLoopScript = script;
    }

    public static void setCurrentScene(Scene scene) {
        currentScene = scene;
    }

    public static int getNativeWidth() {
        return nativeResolution.x;
    }

    public static int getNativeHeight() {
        return nativeResolution.y;
    }

    public static void setNativeResolution(int width, int height) {
        nativeResolution.x = width;
        nativeResolution.y = height;
        window.setResolution(width, height);
    }
}
