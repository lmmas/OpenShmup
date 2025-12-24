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
        new Game(args[0]).run();
    }

    public Game(String gameFolderName) throws IOException {
        super();
        gameDataManager = new GameDataManager(gameFolderName);
        gameDataManager.loadGameConfig();
        gameDataManager.loadGameContents();
        playerSettings = new PlayerSettings();

        playerSettings.setResolution(gameDataManager.config.getNativeWidth(), gameDataManager.config.getNativeHeight());
        Engine.setNativeResolution(gameDataManager.config.getNativeWidth(), gameDataManager.config.getNativeHeight());
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
        switchCurrentScene(new LevelScene(gameDataManager.getTimeline(0)));
    }
}
