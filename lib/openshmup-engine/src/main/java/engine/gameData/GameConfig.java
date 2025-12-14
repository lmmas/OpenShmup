package engine.gameData;

import engine.types.IVec2D;
import engine.types.Vec2D;

final public class GameConfig {
    final private IVec2D nativeResolution = new IVec2D(0, 0);

    public IVec2D getNativeResolution() {
        return new IVec2D(nativeResolution);
    }

    public int getNativeWidth() {
        return nativeResolution.x;
    }

    public int getNativeHeight() {
        return nativeResolution.y;
    }

    public void setNativeResolution(int nativeWidth, int nativeHeight) {
        this.nativeResolution.x = nativeWidth;
        this.nativeResolution.y = nativeHeight;
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
