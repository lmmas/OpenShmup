package engine;

import engine.types.Vec2D;

final public class GameConfig {
    private static int editionWidth;
    private static int editionHeight;

    public static int getEditionWidth() {
        return editionWidth;
    }

    public static int getEditionHeight() {
        return editionHeight;
    }

    public static void setEditionResolution(int editionWidth, int editionHeight) {
        GameConfig.editionWidth = editionWidth;
        GameConfig.editionHeight = editionHeight;
    }

    public static boolean debugMode;

    public static class LevelUI {
        public static int contentsLayer = 1000;

        public static class Lives {
            public static String textureFilepath;
            public static Vec2D size;
            public static Vec2D position;
            public static Vec2D stride;
        }
    }

    public static int pauseMenuLayer = 2000;
}
