package engine;

import java.nio.file.Path;

import static java.nio.file.Paths.get;

final public class GlobalVars {

    public static int MAX_TEXTURE_SLOTS;

    final public static class Paths {

        static public Path rootFolderAbsolutePath;

        static public Path placeholderTextureFile = get("lib/openshmup-engine/src/main/resources/textures/missingTexture.png");

        final static public Path debugFont = get("lib/openshmup-engine/src/main/resources/fonts/RobotoMono-Regular.ttf");

        final public static class Partial {

            final static public Path customGamesFolder = get("Games");

            final static public Path gameConfigFile = get("json/config.json");

            final static public Path gameTextureFolder = get("textures");

            final static public Path gameVisualsFile = get("json/visuals.json");

            final static public Path gameTrajectoriesFile = get("json/trajectories.json");

            final static public Path gameEntitiesFile = get("json/entities.json");

            final static public Path gameTimelineFile = get("json/timeline1.json");

            final static public Path missingTextureFile = get("lib/openshmup-engine/src/main/resources/textures/missingTexture.png");
        }
    }
}
