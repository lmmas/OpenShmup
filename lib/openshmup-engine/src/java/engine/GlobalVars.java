package engine;

public class GlobalVars {
    public static int MAX_TEXTURE_SLOTS;
    public static class Paths{
        final static public String MissingTextureFile = "lib/openshmup-engine/src/resources/textures/missingTexture.png";
        static public String customGameFolder;
        static public String editorTextureFolder;
        static public String editorCustomTrajectoriesFile;
        static public String editorCustomEntitiesFile;
        static public void setcustomGameFolder(String customGameFolder){
            Paths.customGameFolder = Partial.customGamesFolder + customGameFolder;
            Paths.editorTextureFolder = Paths.customGameFolder + Partial.editorTextureFolder;
            Paths.editorCustomTrajectoriesFile = Paths.customGameFolder + Partial.editorCustomTrajectoriesFile;
            Paths.editorCustomEntitiesFile = Paths.customGameFolder + Partial.editorCustomEntitiesFile;
        }
        public static class Partial {
            final static public String customGamesFolder = "Games/";
            final static public String editorTextureFolder = "/textures/";
            final static public String editorCustomTrajectoriesFile = "/json/trajectories.json";
            final static public String editorCustomEntitiesFile = "/json/entities.json";
        }
    }
}
