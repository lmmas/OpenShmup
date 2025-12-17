package editor;

import editor.scenes.MainMenuScene;
import engine.Engine;
import engine.GlobalVars;
import engine.gameData.GameDataManager;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static engine.GlobalVars.Paths.rootFolderAbsolutePath;

final public class Editor extends Engine {
    private static List<GameDataManager> loadedGames = null;

    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            throw new IllegalArgumentException("invalid editor arguments");
        }
        new Editor().run();
    }

    public Editor() throws IOException {
        super();
        setNativeResolution(1920, 1080);
        currentScene = new MainMenuScene();
        window.show();
    }

    public static List<GameDataManager> getLoadedGames() {
        return loadedGames;
    }

    public static void loadGames() throws IOException {
        assert rootFolderAbsolutePath != null : "function called before necessary path is set";
        try (Stream<Path> paths = Files.list(Path.of(rootFolderAbsolutePath + GlobalVars.Paths.Partial.customGamesFolder))) {
            loadedGames = paths.filter(Files::isDirectory)
                .map(path -> new GameDataManager(path.getFileName().toString()))
                .toList();
        }
        loadedGames.forEach(gameDataManager -> {
            gameDataManager.loadGameConfig();
            gameDataManager.loadGameContents();
        });
    }
}
