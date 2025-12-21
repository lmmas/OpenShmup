package engine;

final public class GlobalVars {
    public static int MAX_TEXTURE_SLOTS;
    public static float playerSpeed = 700f;
    final public static int debugDisplayLayer = 2048;

    final public static class Paths {
        static public String rootFolderAbsolutePath;
        static public String placeholderTextureFile = "/lib/openshmup-engine/src/main/resources/textures/missingTexture.png";
        final static public String debugFont = "/lib/openshmup-engine/src/main/resources/fonts/RobotoMono-Regular.ttf";

        final public static class Partial {
            final static public String customGamesFolder = "/Games/";
            final static public String gameConfigFile = "/json/config.json";
            final static public String gameTextureFolder = "/textures/";
            final static public String gameVisualsFile = "/json/visuals.json";
            final static public String gameTrajectoriesFile = "/json/trajectories.json";
            final static public String gameEntitiesFile = "/json/entities.json";
            final static public String gameTimelineFile = "/json/timeline1.json";
            final static public String missingTextureFile = "/lib/openshmup-engine/src/main/resources/textures/missingTexture.png";
        }
    }
}
