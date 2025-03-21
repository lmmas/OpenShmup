package engine.scene.spawnable;

import engine.scene.LevelScene;

public interface Spawnable {
    void spawn(LevelScene scene);
    Spawnable copyWithOffset(float offsetX, float offsetY);
}
