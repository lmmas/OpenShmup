package engine.scene.spawnable;

import engine.scene.Level;

public interface Spawnable {

    void spawn(Level scene);

    Spawnable copy();

    Spawnable copyWithOffset(float offsetX, float offsetY);
}
