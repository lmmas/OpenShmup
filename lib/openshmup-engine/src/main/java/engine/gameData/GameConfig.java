package engine.gameData;

import lombok.Getter;
import lombok.Setter;
import types.IVec2D;
import types.Vec2D;

import java.nio.file.Path;

final public class GameConfig {

    @Getter
    @Setter
    private IVec2D nativeResolution = new IVec2D(0, 0);

    public int getNativeWidth() {
        return nativeResolution.x;
    }

    public int getNativeHeight() {
        return nativeResolution.y;
    }

    public int playerEntityId = 0;

    public static class LevelUI {

        public int contentsLayer = 1000;

        public static class Lives {

            public Path textureFilepath;

            public Vec2D size;

            public Vec2D position;

            public Vec2D stride;
        }

        final public Lives lives = new Lives();
    }

    final public LevelUI levelUI = new LevelUI();

    public int pauseMenuLayer = 2000;
}
