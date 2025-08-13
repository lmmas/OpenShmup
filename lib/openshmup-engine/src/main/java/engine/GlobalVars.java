package engine;

import java.net.URISyntaxException;

final public class GlobalVars {
    public static int MAX_TEXTURE_SLOTS;
    public static float playerSpeed = 0.7f;
    final public static int debugDisplayLayer = 2048;

    final public static class Paths{
        static public String placeholderTextureFile;
        static public String customGameFolder;
        static public String customGameParametersFile;
        static public String editorTextureFolder;
        static public String editorCustomDisplaysFile;
        static public String editorCustomTrajectoriesFile;
        static public String editorCustomEntitiesFile;
        static public String editorCustomTimelineFile;
        static public String rootFolderAbsolutePath;
        static public String defaultFont;
        static public void detectRootFolder(){
            try {
                rootFolderAbsolutePath = java.nio.file.Paths.get(Engine.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParent().getParent().getParent().getParent().toString();
                System.out.println(rootFolderAbsolutePath);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

        }
        static public void setcustomGameFolder(String customGameFolder){
            Paths.customGameFolder = rootFolderAbsolutePath + Partial.customGamesFolder + customGameFolder;
            Paths.customGameParametersFile = Paths.customGameFolder + Partial.customGameParametersFile;
            Paths.editorTextureFolder = Paths.customGameFolder + Partial.editorTextureFolder;
            Paths.editorCustomDisplaysFile = Paths.customGameFolder + Partial.editorCustomDisplaysFile;
            Paths.editorCustomTrajectoriesFile = Paths.customGameFolder + Partial.editorCustomTrajectoriesFile;
            Paths.editorCustomEntitiesFile = Paths.customGameFolder + Partial.editorCustomEntitiesFile;
            Paths.editorCustomTimelineFile = Paths.customGameFolder + Partial.editorCustomTimelineFile;
            Paths.placeholderTextureFile = rootFolderAbsolutePath + Partial.missingTextureFile;
            Paths.defaultFont = rootFolderAbsolutePath + Partial.defaultFont;
        }
        final private static class Partial {

            final static public String customGamesFolder = "/Games/";
            final static public String customGameParametersFile = "/json/gameParameters.json";
            final static public String editorTextureFolder = "/textures/";
            final static public String editorCustomDisplaysFile = "/json/displays.json";
            final static public String editorCustomTrajectoriesFile = "/json/trajectories.json";
            final static public String editorCustomEntitiesFile = "/json/entities.json";
            final static public String editorCustomTimelineFile = "/json/timeline1.json";
            final static public String missingTextureFile = "/lib/openshmup-engine/src/main/resources/textures/missingTexture.png";
            final static public String defaultFont = "/lib/openshmup-engine/src/main/resources/fonts/DejaVuSansMono.ttf";
        }
    }
}
