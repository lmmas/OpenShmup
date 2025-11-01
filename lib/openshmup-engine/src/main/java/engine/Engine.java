package engine;

import engine.scene.LevelScene;
import org.lwjgl.glfw.Callbacks;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL33.*;

public class Engine extends Application {
    public static EditorDataManager editorDataManager;

    public static void main(String[] args) throws IOException {
        if(args.length != 1){
            throw new IllegalArgumentException("invalid engine arguments");
        }
        new Engine(args[0], () -> {}, () -> {});
    }

    public Engine(String gameFolderName, Runnable initScript, Runnable inLoopScript) throws IOException {
        super(gameFolderName, initScript, inLoopScript);
        editorDataManager = new EditorDataManager(gameFolderName);
        editorDataManager.loadGameParameters();
        editorDataManager.loadGameContents();

        PlayerSettings.setResolution(GameConfig.getEditionWidth(), GameConfig.getEditionHeight());
        glfwSetWindowSize(glfwWindow, PlayerSettings.getWindowWidth(), PlayerSettings.getWindowHeight());
        glViewport(0,0,PlayerSettings.getWindowWidth(), PlayerSettings.getWindowHeight());
        glfwSetWindowPos(glfwWindow, 500, 40);
        glfwShowWindow(glfwWindow);

        run();
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

    public static void gameInit(){
        graphicsManager.clearLayers();
        editorDataManager.getTimeline(0).resetTime();
        currentScene = new LevelScene(editorDataManager.getTimeline(0), GameConfig.debugMode);
    }
}
