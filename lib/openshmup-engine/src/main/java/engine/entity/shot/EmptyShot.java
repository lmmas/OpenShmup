package engine.entity.shot;

import engine.entity.Entity;
import engine.scene.LevelScene;

public class EmptyShot implements EntityShot{
    private static EmptyShot instance = null;
    private EmptyShot(){
        
    }

    public static EmptyShot getInstance(){
        if(instance == null){
            instance = new EmptyShot();
        }
        return instance;
    }
    @Override
    public void setScene(LevelScene scene) {

    }

    @Override
    public void update(Entity entity) {

    }

    @Override
    public EntityShot copyIfNotReusable() {
        return this;
    }
}
