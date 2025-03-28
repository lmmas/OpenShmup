package engine.entity.shot;

import engine.entity.Entity;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

import java.util.Optional;

public interface EntityShot {
    void setScene(LevelScene scene);
    void update(Entity entity);
    EntityShot copyIfNotReusable();
    Spawnable getSpawnable();
    static EntityShot DEFAULT(){
        return EmptyShot.getInstance();
    }
}
