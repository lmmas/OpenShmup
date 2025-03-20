package engine;

public class GlobalVars {
    public static int MAX_TEXTURE_SLOTS;
    final public static float SECONDS = 1.0f;
    public static class Paths{
        final static public String MissingTextureFile = "lib/openshmup-engine/src/resources/textures/missingTexture.png";
        static public String customGameFolder;
        static public String customGameParametersFile;
        static public String editorTextureFolder;
        static public String editorCustomVisualsFile;
        static public String editorCustomTrajectoriesFile;
        static public String editorCustomEntitiesFile;
        static public String editorCustomTimelineFile;
        static public void setcustomGameFolder(String customGameFolder){
            Paths.customGameFolder = Partial.customGamesFolder + customGameFolder;
            Paths.customGameParametersFile = Paths.customGameFolder + Partial.customGameParametersFile;
            Paths.editorTextureFolder = Paths.customGameFolder + Partial.editorTextureFolder;
            Paths.editorCustomVisualsFile = Paths.customGameFolder + Partial.editorCustomVisualsFile;
            Paths.editorCustomTrajectoriesFile = Paths.customGameFolder + Partial.editorCustomTrajectoriesFile;
            Paths.editorCustomEntitiesFile = Paths.customGameFolder + Partial.editorCustomEntitiesFile;
            Paths.editorCustomTimelineFile = Paths.customGameFolder + Partial.editorCustomTimelineFile;
        }
        public static class Partial {
            final static public String customGamesFolder = "Games/";
            final static public String customGameParametersFile = "/json/gameParameters.json";
            final static public String editorTextureFolder = "/textures/";
            final static public String editorCustomVisualsFile = "/json/visuals.json";
            final static public String editorCustomTrajectoriesFile = "/json/trajectories.json";
            final static public String editorCustomEntitiesFile = "/json/entities.json";
            final static public String editorCustomTimelineFile = "/json/timeline1.json";
        }
    }

    static final public class EditionParameters {
        private static int editionWidth;
        private static int editionHeight;
        public static int getEditionWidth() {
            return editionWidth;
        }

        public static int getEditionHeight() {
            return editionHeight;
        }

        public static  void setEditionResolution(int editionWidth, int editionHeight){
            EditionParameters.editionWidth = editionWidth;
            EditionParameters.editionHeight = editionHeight;
        }
    }
}
