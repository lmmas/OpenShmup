package engine.scene.spawnable;

import engine.EditorDataManager;
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
    public void spawn(EditorDataManager editorDataManager, LevelScene scene) {
        editorDataManager.buildCustomVisual(scene, id).setPosition(position.x, position.y);
    }
}
