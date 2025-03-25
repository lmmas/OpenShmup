package engine.entity.shot;

import engine.scene.LevelScene;

public interface EntityShot {
    void setScene(LevelScene scene);
    void update(float currentTimeSeconds);
    EntityShot copyIfNotReusable();
    static EntityShot DEFAULT(){
        return EmptyShot.getInstance();
    }
}
