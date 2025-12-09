package engine.gameData;

import engine.GlobalVars;

final public class GamePaths {
    final public String customGameFolder;
    final public String customGameConfigFile;
    final public String editorTextureFolder;
    final public String editorCustomDisplaysFile;
    final public String editorCustomTrajectoriesFile;
    final public String editorCustomEntitiesFile;
    final public String editorCustomTimelineFile;

    public GamePaths(String gameFolderName) {
        customGameFolder = GlobalVars.Paths.Partial.customGamesFolder + gameFolderName;
        customGameConfigFile = customGameFolder + GlobalVars.Paths.Partial.customGameConfigFile;
        editorTextureFolder = customGameFolder + GlobalVars.Paths.Partial.editorTextureFolder;
        editorCustomDisplaysFile = customGameFolder + GlobalVars.Paths.Partial.editorCustomDisplaysFile;
        editorCustomTrajectoriesFile = customGameFolder + GlobalVars.Paths.Partial.editorCustomTrajectoriesFile;
        editorCustomEntitiesFile = customGameFolder + GlobalVars.Paths.Partial.editorCustomEntitiesFile;
        editorCustomTimelineFile = customGameFolder + GlobalVars.Paths.Partial.editorCustomTimelineFile;
    }


}
