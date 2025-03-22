package engine.entity.trajectory;

import engine.entity.Entity;
import engine.scene.LevelScene;

public class EmptyTrajectory implements Trajectory {
    private static EmptyTrajectory instance = null;
    private EmptyTrajectory(){

    }
    public static EmptyTrajectory getInstance(){
        if(instance == null){
            instance = new EmptyTrajectory();
        }
        return instance;
    }
    @Override
    public Trajectory copyIfNotReusable() {
        return this;
    }

    @Override
    public void update(Entity entity) {

    }

    @Override
    public void setScene(LevelScene scene) {

    }
}
