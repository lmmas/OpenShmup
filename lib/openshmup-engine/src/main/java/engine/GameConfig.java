package engine;

import engine.types.Vec2D;

final public class GameConfig {
    private int editionWidth;
    private int editionHeight;

    public int getEditionWidth() {
        return editionWidth;
    }

    public int getEditionHeight() {
        return editionHeight;
    }

    public void setEditionResolution(int editionWidth, int editionHeight) {
        this.editionWidth = editionWidth;
        this.editionHeight = editionHeight;
    }

    public boolean debugMode;

    public static class LevelUI {
        public int contentsLayer = 1000;

        public static class Lives {
            public String textureFilepath;
            public Vec2D size;
            public Vec2D position;
            public Vec2D stride;
        }

        final public Lives lives = new Lives();
    }

    final public LevelUI levelUI = new LevelUI();

    public int pauseMenuLayer = 2000;
}
