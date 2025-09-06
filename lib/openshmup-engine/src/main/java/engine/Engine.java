package engine;

import debug.DebugMethods;
import engine.scene.LevelScene;
import engine.scene.Scene;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.io.IOException;
import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

final public class Engine {
    public static Engine engine;
    private long glfwWindow;
    final private org.lwjgl.system.Callback debugProc;
    public static EditorDataManager editorDataManager;
    public static AssetManager assetManager;
    public static GraphicsManager graphicsManager;
    public static InputStatesManager inputStatesManager;
    private Scene currentScene;
    private final Runnable initScript;
    private final Runnable inLoopScript;

    public static void main(String[] args) throws IOException {
        if(args.length != 1){
            throw new IllegalArgumentException("invalid engine arguments");
        }
        new Engine(args[0], () -> {}, () -> {});
    }
    public Engine(String gameFolderName, Runnable initScript, Runnable inLoopScript) throws IOException {
        engine = this;
        GlobalVars.Paths.detectRootFolder();
        GlobalVars.Paths.setcustomGameFolder(gameFolderName);
        editorDataManager = new EditorDataManager(gameFolderName);
        editorDataManager.loadGameParameters();
        PlayerSettings.setResolution(GameConfig.getEditionWidth(), GameConfig.getEditionHeight());

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

        glfwWindow = glfwCreateWindow(PlayerSettings.getWindowWidth(), PlayerSettings.getWindowHeight(), "OpenShmup", NULL, NULL);
        assert glfwWindow != NULL:"Unable to create GLFW Window";

        try(MemoryStack stack = stackPush()){
            IntBuffer pWidth = stack.mallocInt(1);
            IntBuffer pHeight = stack.mallocInt(1);

            glfwGetWindowSize(glfwWindow, pWidth, pHeight);
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            assert vidmode != null: "glfwGetVideoMode failure";
            glfwSetWindowPos(glfwWindow,(vidmode.width() - pWidth.get(0)) / 2,(vidmode.height() - pHeight.get(0)) / 2);

            glfwMakeContextCurrent(glfwWindow);
            glfwSwapInterval(1);
            GL.createCapabilities();
            glGetInteger(GL_MAX_TEXTURE_IMAGE_UNITS);
            debugProc = GLUtil.setupDebugMessageCallback();
            GlobalVars.MAX_TEXTURE_SLOTS = glGetInteger(GL_MAX_TEXTURE_IMAGE_UNITS);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glfwShowWindow(glfwWindow);
        }
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
        graphicsManager = new GraphicsManager();
        assetManager = new AssetManager();
        editorDataManager.loadGameContents();
        inputStatesManager = new InputStatesManager(glfwWindow);
        this.initScript = initScript;
        this.inLoopScript = inLoopScript;
        this.run();
    }
    private void run(){
        initScript.run();
        gameInit();
        loop();

        Callbacks.glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        debugProc.free();
    }

    public void gameInit(){
        graphicsManager.clearLayers();
        editorDataManager.getTimeline(0).resetTime();
        this.currentScene = new LevelScene(editorDataManager.getTimeline(0), GameConfig.debugMode);
    }

    private void loop(){
        while (!glfwWindowShouldClose(glfwWindow)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            DebugMethods.checkForOpenGLErrors();
            inLoopScript.run();
            inputStatesManager.updateInputStates();
            currentScene.update();
            graphicsManager.drawGraphics();
            glfwSwapBuffers(glfwWindow);
            glfwPollEvents();
        }
    }

    public long getWindow() {
        return glfwWindow;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }
}
