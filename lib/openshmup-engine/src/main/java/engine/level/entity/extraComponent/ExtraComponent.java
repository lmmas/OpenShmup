package engine.level.entity.extraComponent;

import engine.level.Level;
import engine.level.entity.Entity;
import engine.level.spawnable.Spawnable;

import java.util.List;

public interface ExtraComponent {

    ExtraComponent copyIfNotReusable();

    List<Spawnable> getSpawnables();

    void init();

    void onRemove();

    void update(Entity entity, Level level);
}
