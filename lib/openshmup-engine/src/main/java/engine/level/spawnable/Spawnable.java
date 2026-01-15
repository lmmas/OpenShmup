package engine.level.spawnable;

import engine.level.Level;

public interface Spawnable {

    void spawn(Level scene);

    Spawnable copy();

    Spawnable copyWithOffset(float offsetX, float offsetY);
}
