package editor;

import edition.GameEditionData;
import engine.Engine;
import engine.GlobalVars;
import engine.scene.Scene;
import json.readers.GameDataReader;
import lombok.Getter;
import types.IVec2D;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static engine.GlobalVars.Paths.rootFolderAbsolutePath;

final public class Editor {

    @Getter
    private static List<GameEditionData> loadedGames = null;

    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            throw new IllegalArgumentException("invalid editor arguments");
        }
        Editor.init();
        Engine.run();
    }

    public static void init() throws IOException {

        Engine.init();
        Engine.setNativeResolution(new IVec2D(1920, 1080));
        Engine.initInputStatesManager();
        Engine.initGraphicsManager();
        Engine.switchCurrentScene(new Scene());
        Engine.switchCurrentMenu(Menus.MainMenu());

        Engine.window.show();
    }

    public static void loadGames() throws IOException {
        assert rootFolderAbsolutePath != null : "function called before necessary path is set";
        GameDataReader reader = new GameDataReader();
        try (Stream<Path> paths = Files.list(rootFolderAbsolutePath.resolve(GlobalVars.Paths.Partial.customGamesFolder))) {
            loadedGames = paths.filter(Files::isDirectory)
                .map(reader::readGameData)
                .toList();
        }
    }
}
