package engine.scene.spawnable;

import engine.scene.LevelScene;

public interface Spawnable {

    void spawn(LevelScene scene);

    Spawnable copy();

    Spawnable copyWithOffset(float offsetX, float offsetY);
}
