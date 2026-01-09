package engine.entity.extraComponent;

import engine.entity.Entity;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

public interface ExtraComponent {

    ExtraComponent copyIfNotReusable();

    Spawnable getSpawnable();

    void init();

    void onRemove();

    void update(Entity entity, LevelScene scene);
}
