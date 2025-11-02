package engine;

import engine.scene.LevelScene;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWVidMode;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL33.*;

public class Engine extends Application {
    public static GameDataManager gameDataManager;

    public static void main(String[] args) throws IOException {
        if(args.length != 1){
            throw new IllegalArgumentException("invalid engine arguments");
        }
        new Engine(args[0], () -> {}, () -> {});
    }

    public Engine(String gameFolderName, Runnable initScript, Runnable inLoopScript) throws IOException {
        super(gameFolderName, initScript, inLoopScript);
        gameDataManager = new GameDataManager(gameFolderName);
        gameDataManager.loadGameParameters();
        gameDataManager.loadGameContents();
        GameConfig gameConfig = gameDataManager.config;

        PlayerSettings.setResolution(gameConfig.getEditionWidth(), gameConfig.getEditionHeight());
        windowResolution.x = gameConfig.getEditionWidth();
        windowResolution.y = gameConfig.getEditionHeight();
        glfwSetWindowSize(glfwWindow, PlayerSettings.getWindowWidth(), PlayerSettings.getWindowHeight());
        glViewport(0,0,PlayerSettings.getWindowWidth(), PlayerSettings.getWindowHeight());
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        assert vidmode != null : "glfwGetVideoMode failure";
        glfwSetWindowPos(glfwWindow, (vidmode.width() - windowResolution.x) / 2, (vidmode.height() - windowResolution.y) / 2);
        glfwShowWindow(glfwWindow);

        run();
    }

    private void run(){
        initScript.run();
        gameInit();
        loop();

        terminate();
    }

    public static void gameInit(){
        graphicsManager.clearLayers();
        gameDataManager.getTimeline(0).resetTime();
        currentScene = new LevelScene(gameDataManager.getTimeline(0), gameDataManager.config.debugMode);
    }
}
