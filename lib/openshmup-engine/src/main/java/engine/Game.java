package engine;

import engine.gameData.GameDataManager;
import engine.scene.LevelScene;

import java.io.IOException;

final public class Game extends Engine {
    public static GameDataManager gameDataManager;
    public static PlayerSettings playerSettings;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("invalid engine arguments");
        }
        new Game(args[0], () -> {
        }).run();
    }

    public Game(String gameFolderName, Runnable inLoopScript) throws IOException {
        super(inLoopScript);
        gameDataManager = new GameDataManager(gameFolderName);
        gameDataManager.loadGameConfig();
        gameDataManager.loadGameContents();
        playerSettings = new PlayerSettings();

        playerSettings.setResolution(gameDataManager.config.getNativeWidth(), gameDataManager.config.getNativeHeight());
        window.setResolution(playerSettings.getWindowWidth(), playerSettings.getWindowHeight());
        window.show();
    }

    @Override
    public void run() {
        gameInit();
        loop();
        terminate();
    }

    public static void gameInit() {
        graphicsManager.clearLayers();
        gameDataManager.getTimeline(0).resetTime();
        currentScene = new LevelScene(gameDataManager.getTimeline(0));
    }
}
