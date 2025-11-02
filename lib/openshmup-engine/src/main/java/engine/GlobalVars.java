package engine;

final public class GlobalVars {
    public static int MAX_TEXTURE_SLOTS;
    public static float playerSpeed = 0.7f;
    final public static int debugDisplayLayer = 2048;

    final public static class Paths{
        static public String placeholderTextureFile;
        static public String rootFolderAbsolutePath;
        static public String debugFont;

        final public static class Partial {
            final static public String customGamesFolder = "/Games/";
            final static public String customGameConfigFile = "/json/config.json";
            final static public String editorTextureFolder = "/textures/";
            final static public String editorCustomDisplaysFile = "/json/displays.json";
            final static public String editorCustomTrajectoriesFile = "/json/trajectories.json";
            final static public String editorCustomEntitiesFile = "/json/entities.json";
            final static public String editorCustomTimelineFile = "/json/timeline1.json";
            final static public String missingTextureFile = "/lib/openshmup-engine/src/main/resources/textures/missingTexture.png";
            final static public String debugFont = "/lib/openshmup-engine/src/main/resources/fonts/RobotoMono-Regular.ttf";
        }
    }
}
