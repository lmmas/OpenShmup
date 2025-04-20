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

    public static class LevelUI {
        public static int upperLayer = 1024;

        public static class Lives {
            public static String textureFilepath;
            public static Vec2D size;
            public static Vec2D position;
            public static Vec2D stride;
        }
    }
}
