package engine.scene.spawnable;

import engine.EditorDataManager;
import engine.scene.LevelScene;

public interface Spawnable {
    void spawn(EditorDataManager editorDataManager, LevelScene scene);
}
