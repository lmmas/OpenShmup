package engine.level.spawnable;

public interface Spawnable {

    Spawnable copy();

    Spawnable copyWithOffset(float offsetX, float offsetY);
}
