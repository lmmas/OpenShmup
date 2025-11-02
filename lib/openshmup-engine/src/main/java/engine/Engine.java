package engine;

import engine.scene.LevelScene;

import java.io.IOException;

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
        super(initScript, inLoopScript);
        gameDataManager = new GameDataManager(gameFolderName);
        gameDataManager.loadGameConfig();
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
        currentScene = new LevelScene(gameDataManager.getTimeline(0));
    }
}
