package engine.entity.extraComponent;

import engine.entity.Entity;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

import java.util.List;

public interface ExtraComponent {

    ExtraComponent copyIfNotReusable();

    List<Spawnable> getSpawnables();

    void init();

    void onRemove();

    void update(Entity entity, LevelScene scene);
}
