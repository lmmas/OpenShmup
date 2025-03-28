package engine.entity.shot;

import engine.entity.Entity;
import engine.scene.LevelScene;
import engine.scene.spawnable.EmptySpawnable;
import engine.scene.spawnable.Spawnable;

import java.util.Optional;

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

    @Override
    public Spawnable getSpawnable() {
        return EmptySpawnable.getInstance();
    }
}
