package path;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;

final public class GamePaths implements Serializable {

    final public Path gameFolder;

    final public Path gameConfigFile;

    final public Path gameTextureFolder;

    final public Path gameVisualsFile;

    final public Path gameTrajectoriesFile;

    final public Path gameEntitiesFile;

    final public Path gameTimelineFile;

    public GamePaths(Path gameFolderPath) {
        gameFolder = gameFolderPath;
        gameConfigFile = gameFolder.resolve(PartialPaths.gameConfigFile);
        gameTextureFolder = gameFolder.resolve(PartialPaths.gameTextureFolder);
        gameVisualsFile = gameFolder.resolve(PartialPaths.gameVisualsFile);
        gameTrajectoriesFile = gameFolder.resolve(PartialPaths.gameTrajectoriesFile);
        gameEntitiesFile = gameFolder.resolve(PartialPaths.gameEntitiesFile);
        gameTimelineFile = gameFolder.resolve(PartialPaths.gameTimelineFile);
    }

    final public static class PartialPaths {

        final static public Path gameConfigFile = Paths.get("json/config.json");

        final static public Path gameTextureFolder = Paths.get("textures");

        final static public Path gameVisualsFile = Paths.get("json/visuals.json");

        final static public Path gameTrajectoriesFile = Paths.get("json/trajectories.json");

        final static public Path gameEntitiesFile = Paths.get("json/entities.json");

        final static public Path gameTimelineFile = Paths.get("json/timeline1.json");
    }


}
