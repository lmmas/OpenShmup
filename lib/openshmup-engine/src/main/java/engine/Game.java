package engine;

import engine.gameData.GameDataManager;
import engine.level.Level;
import engine.menu.Menu;
import engine.scene.Scene;
import engine.types.Reference;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

final public class Game extends Engine {

    public static GameDataManager gameDataManager;

    public static PlayerSettings playerSettings;

    final private static Reference<Level> currentLevel = new Reference<>(null);
    @Getter @Setter
    private static double levelTime;

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            throw new IllegalArgumentException("invalid engine arguments");
        }
        Game.init(args[0]);
        Game.run();
    }

    private Game() {}

    public static void init(String gameFolderName) throws IOException {
        Engine.init();
        Engine.initInputStatesManager();
        Engine.initGraphicsManager();

        levelTime = 0.0d;
        gameDataManager = new GameDataManager(gameFolderName);
        gameDataManager.loadGameConfig();
        gameDataManager.loadGameContents();
        playerSettings = new PlayerSettings();

        playerSettings.setResolution(gameDataManager.config.getNativeWidth(), gameDataManager.config.getNativeHeight());
        Engine.setNativeResolution(gameDataManager.config.getNativeWidth(), gameDataManager.config.getNativeHeight());
        window.show();
    }

    public static void run() {
        gameStart();
        Engine.loop();
        Engine.terminate();
    }

    public static void gameStart() {
        gameDataManager.getTimeline(0).resetTime();
        Scene levelScene = new Scene();
        Menu gameMenu = new Menu();
        switchCurrentScene(levelScene);
        switchCurrentMenu(gameMenu);
        currentLevel.set(new Level(currentScene.get(), gameDataManager.getTimeline(0)));
        setCurrentLevelActive(true);
        getCurrentScene().startTimer();
        getCurrentLevel().start();
    }

    public static Level getCurrentLevel() {
        return currentLevel.get();
    }

    public static void setCurrentLevelActive(boolean active) {
        setEngineSystemActive(currentLevel, active);
    }
}
