package engine;

import debug.DebugMethods;
import engine.scene.LevelScene;
import engine.scene.Scene;
import engine.scene.TestScene;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL33.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Engine {
    private long glfwWindow;
    private final EditorDataManager editorDataManager;
    private final GraphicsManager graphicsManager;
    private final InputStatesManager inputStatesManager;
    private Scene currentScene;

    public static void main(String[] args){
        if(args.length != 1){
            throw new IllegalArgumentException("invalid engine arguments");
        }
        new Engine(args[0]).run();
    }
    public Engine(String gameFolder){
        GlobalVars.Paths.detectRootFolder();
        GlobalVars.Paths.setcustomGameFolder(gameFolder);
        this.editorDataManager = new EditorDataManager();
        editorDataManager.loadGameParameters();
        this.graphicsManager = new GraphicsManager();
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
            GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            assert vidmode != null: "glfwGetVideoMode failure";
            glfwSetWindowPos(glfwWindow,(vidmode.width() - pWidth.get(0)) / 2,(vidmode.height() - pHeight.get(0)) / 2);

            glfwMakeContextCurrent(glfwWindow);
            glfwSwapInterval(1);
            GL.createCapabilities();
            glGetInteger(GL_MAX_TEXTURE_IMAGE_UNITS);
            org.lwjgl.system.Callback debugProc = GLUtil.setupDebugMessageCallback();
            GlobalVars.MAX_TEXTURE_SLOTS = glGetInteger(GL_MAX_TEXTURE_IMAGE_UNITS);
            glEnable(GL_BLEND);
            glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
            glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            glfwShowWindow(glfwWindow);
        }
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);
        editorDataManager.loadGameContents();
        this.inputStatesManager = new InputStatesManager(glfwWindow);
    }
    public void run(){
        init();
        //testInit();
        loop();

        Callbacks.glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);

        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(){
        this.currentScene = new LevelScene(this, editorDataManager.getTimeline(0), true);
    }

    public void loop(){
        while (!glfwWindowShouldClose(glfwWindow)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            DebugMethods.checkForOpenGLErrors();
            //testInLoop();
            inputStatesManager.updateControlStates();
            currentScene.update();
            graphicsManager.drawGraphics();
            glfwSwapBuffers(glfwWindow);
            glfwPollEvents();
        }
    }

    public long getWindow() {
        return glfwWindow;
    }

    public EditorDataManager getEditorDataManager() {
        return editorDataManager;
    }

    public GraphicsManager getGraphicsManager() {
        return graphicsManager;
    }

    public InputStatesManager getInputStatesManager() {
        return inputStatesManager;
    }

    public Scene getCurrentScene() {
        return currentScene;
    }

    public void testInit(){
        currentScene = new TestScene(this);
    }
    public void testInLoop(){

    }
}
