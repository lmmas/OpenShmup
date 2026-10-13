package engine;

import edition.GameEditionData;
import engine.gameData.GameDataManager;
import engine.level.Level;
import engine.menu.Menu;
import engine.scene.Scene;
import lombok.Getter;
import lombok.Setter;
import types.Reference;

import java.io.IOException;
import java.io.ObjectInputStream;

final public class Game {

    private Game() {}

    public static GameDataManager gameDataManager;

    public static PlayerSettings playerSettings;

    final private static Reference<Level> currentLevel = new Reference<>(null);
    @Getter
    @Setter
    private static double levelTime;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        if (args.length != 0) {
            throw new IllegalArgumentException("invalid game arguments");
        }
        ObjectInputStream in = new ObjectInputStream(System.in);
        GameEditionData gameEditionData = (GameEditionData) in.readObject();
        gameEditionData.reloadPaths();
        Game.init(gameEditionData);
        Game.run();
    }


    public static void init(GameEditionData gameEditionData) throws IOException {
        Engine.init();
        Engine.initInputStatesManager();
        Engine.initGraphicsManager();

        levelTime = 0.0d;
        gameDataManager = new GameDataLoader().convertToGameObjects(gameEditionData);
        playerSettings = new PlayerSettings();

        playerSettings.setResolution(gameDataManager.config.getNativeWidth(), gameDataManager.config.getNativeHeight());
        Engine.setNativeResolution(gameDataManager.config.getNativeResolution());
        Engine.window.show();
    }

    public static void run() {
        gameStart();
        Engine.loop();
        Engine.terminate();
    }

    public static void gameStart() {
        gameDataManager.getTimeline(0).resetTime();
        Scene levelScene = new Scene();
        Engine.switchCurrentScene(levelScene);
        Engine.switchCurrentMenu(new Menu());
        Level level = new Level(levelScene, gameDataManager.getTimeline(0));
        currentLevel.set(level);
        setCurrentLevelActive(true);
        level.start();
    }

    public static Level getCurrentLevel() {
        return currentLevel.get();
    }

    public static void setCurrentLevelActive(boolean active) {
        Engine.setEngineSystemActive(currentLevel, active);
    }
}
