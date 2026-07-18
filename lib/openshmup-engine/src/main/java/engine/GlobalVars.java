package engine;

import java.nio.file.Path;

import static java.nio.file.Paths.get;

final public class GlobalVars {

    private GlobalVars() {}

    public static int MAX_TEXTURE_SLOTS;

    final public static class Paths {

        private Paths() {}

        static public Path rootFolderAbsolutePath;

        static public Path placeholderTextureFile = get("lib/openshmup-engine/src/main/resources/textures/missingTexture.png");

        final static public Path debugFont = get("lib/openshmup-engine/src/main/resources/fonts/RobotoMono-Regular.ttf");

        final public static class Partial {

            private Partial() {}

            final static public Path customGamesFolder = get("Games");
        }
    }
}
