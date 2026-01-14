package editor;

import engine.Engine;
import engine.GlobalVars;
import engine.InputStatesManager;
import engine.graphics.GraphicsManager;
import engine.menu.Menu;
import engine.scene.Scene;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static engine.GlobalVars.Paths.rootFolderAbsolutePath;

final public class Editor extends Engine {

    @Getter
    private static List<EditorGameDataManager> loadedGames = null;

    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            throw new IllegalArgumentException("invalid editor arguments");
        }
        new Editor().run();
    }

    public Editor() throws IOException {
        super();
        setNativeResolution(1920, 1080);
        graphicsManager.set(new GraphicsManager());
        inputStatesManager.set(new InputStatesManager());
        Menu mainMenu = Menus.MainMenu();
        switchCurrentScene(new Scene());
        switchCurrentMenu(mainMenu);

        activeSystemsList = List.of(inputStatesManager, currentMenu, currentScene, graphicsManager);
        window.show();

    }

    public static void loadGames() throws IOException {
        assert rootFolderAbsolutePath != null : "function called before necessary path is set";
        try (Stream<Path> paths = Files.list(rootFolderAbsolutePath.resolve(GlobalVars.Paths.Partial.customGamesFolder))) {
            loadedGames = paths.filter(Files::isDirectory)
                .map(path -> new EditorGameDataManager(path.getFileName().toString()))
                .toList();
        }
        loadedGames.forEach(EditorGameDataManager::loadGameContents);
    }
}
