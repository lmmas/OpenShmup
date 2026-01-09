package engine.gameData;

import engine.GlobalVars;

final public class GamePaths {

    final public String gameFolder;

    final public String gameConfigFile;

    final public String gameTextureFolder;

    final public String gameVisualsFile;

    final public String gameTrajectoriesFile;

    final public String gameEntitiesFile;

    final public String gameTimelineFile;

    public GamePaths(String gameFolderName) {
        gameFolder = GlobalVars.Paths.Partial.customGamesFolder + gameFolderName;
        gameConfigFile = gameFolder + GlobalVars.Paths.Partial.gameConfigFile;
        gameTextureFolder = gameFolder + GlobalVars.Paths.Partial.gameTextureFolder;
        gameVisualsFile = gameFolder + GlobalVars.Paths.Partial.gameVisualsFile;
        gameTrajectoriesFile = gameFolder + GlobalVars.Paths.Partial.gameTrajectoriesFile;
        gameEntitiesFile = gameFolder + GlobalVars.Paths.Partial.gameEntitiesFile;
        gameTimelineFile = gameFolder + GlobalVars.Paths.Partial.gameTimelineFile;
    }


}
