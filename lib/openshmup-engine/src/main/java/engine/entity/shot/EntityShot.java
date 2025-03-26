package engine.entity.shot;

import engine.entity.Entity;
import engine.scene.LevelScene;

public interface EntityShot {
    void setScene(LevelScene scene);
    void update(Entity entity);
    EntityShot copyIfNotReusable();
    static EntityShot DEFAULT(){
        return EmptyShot.getInstance();
    }
}
