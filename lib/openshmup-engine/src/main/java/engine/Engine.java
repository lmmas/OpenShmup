package engine;

import engine.scene.LevelScene;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWVidMode;

import java.io.IOException;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL33.*;

final public class Engine extends Application {
    public static GameDataManager gameDataManager;
    public static PlayerSettings playerSettings;

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
        playerSettings = new PlayerSettings();

        playerSettings.setResolution(gameDataManager.config.getEditionWidth(), gameDataManager.config.getEditionHeight());
        window.setResolution(playerSettings.getWindowWidth(), playerSettings.getWindowHeight());
        window.show();

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
