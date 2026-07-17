package engine.gameData;

import engine.GlobalVars;

import java.io.Serializable;
import java.nio.file.Path;

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
        gameConfigFile = gameFolder.resolve(GlobalVars.Paths.Partial.gameConfigFile);
        gameTextureFolder = gameFolder.resolve(GlobalVars.Paths.Partial.gameTextureFolder);
        gameVisualsFile = gameFolder.resolve(GlobalVars.Paths.Partial.gameVisualsFile);
        gameTrajectoriesFile = gameFolder.resolve(GlobalVars.Paths.Partial.gameTrajectoriesFile);
        gameEntitiesFile = gameFolder.resolve(GlobalVars.Paths.Partial.gameEntitiesFile);
        gameTimelineFile = gameFolder.resolve(GlobalVars.Paths.Partial.gameTimelineFile);
    }


}
