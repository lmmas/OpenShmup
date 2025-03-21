package engine.scene.spawnable;

import engine.Vec2D;
import engine.scene.LevelScene;

public record SceneVisualSpawnInfo(
        int id,
        Vec2D position
) implements Spawnable {

    public SceneVisualSpawnInfo(int id, float positionX, float positionY){
        this(id, new Vec2D(positionX, positionY));
    }

    @Override
    public void spawn(LevelScene scene) {
        scene.addVisualSpawn(this);
    }

    @Override
    public Spawnable copyWithOffset(float offsetX, float offsetY) {
        return new SceneVisualSpawnInfo(id, position.x + offsetX, position.y + offsetY);
    }


}
