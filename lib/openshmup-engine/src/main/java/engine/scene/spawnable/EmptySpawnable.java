package engine.scene.spawnable;

import engine.scene.LevelScene;

final public class EmptySpawnable implements Spawnable{
    private static EmptySpawnable instance = null;
    private EmptySpawnable(){

    }
    public static EmptySpawnable getInstance(){
        if(instance == null){
            instance = new EmptySpawnable();
        }
        return instance;
    }
    @Override
    public void spawn(LevelScene scene) {

    }

    @Override
    public Spawnable copy() {
        return this;
    }

    @Override
    public Spawnable copyWithOffset(float offsetX, float offsetY) {
        return this;
    }
}
