package engine;

import engine.gameData.GameDataManager;
import engine.graphics.GraphicsManager;
import engine.scene.Level;
import engine.scene.Scene;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

final public class Game extends Engine {

    public static GameDataManager gameDataManager;

    public static PlayerSettings playerSettings;

    public static Level currentLevel;
    @Getter @Setter
    private static double levelTime;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("invalid engine arguments");
        }
        new Game(args[0]).run();
    }

    public Game(String gameFolderName) throws IOException {
        super();
        levelTime = 0.0d;

        graphicsManager = new GraphicsManager();
        inputStatesManager = new InputStatesManager();
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
        gameDataManager.getTimeline(0).resetTime();
        Scene levelScene = new Scene();
        currentLevel = new Level(levelScene, gameDataManager.getTimeline(0));
        switchCurrentScene(levelScene);
        currentScene.start();
        currentLevel.start();

        Engine.activeSystemsList = List.of(inputStatesManager, currentLevel, currentScene, graphicsManager);
    }
}
