package engine;

import engine.visual.Visual;
import entity.NonPlayerEntity;
import entity.PlayerShip;

import java.util.LinkedList;

public class LevelScene extends Scene{
    PlayerShip playerShip;
    LinkedList<NonPlayerEntity> goodEntityList = new LinkedList<>();
    LinkedList<NonPlayerEntity> evilEntityList = new LinkedList<>();
    protected long startingTimeMillis = System.currentTimeMillis();
    protected float lastUpdateTime = 0.0f;

    public LevelScene(long window) {
        super(window);
    }

    public void addEntity(NonPlayerEntity entity){
        Visual<?,?> sprite = (Visual<?,?>)entity.getSprite();
        addVisual(sprite);
        if(entity.isEvil()){
            evilEntityList.add(entity);
        }
        else{
            goodEntityList.add(entity);
        }
    }
    @Override
    public void update() {
        float currentTimeSeconds = (float)(System.currentTimeMillis() - startingTimeMillis)/ 1000.0f;
        float deltaTime = currentTimeSeconds - lastUpdateTime;
        for(NonPlayerEntity entity: goodEntityList){
            entity.actionsAndMoves(currentTimeSeconds);
        }
        for(NonPlayerEntity entity: evilEntityList){
            entity.actionsAndMoves(currentTimeSeconds);
        }
        if(playerShip != null){
            playerShip.actionsAndMoves(window, deltaTime);
        }
        for(NonPlayerEntity entity: evilEntityList){
            entity.handleCollisions();
        }
        for(NonPlayerEntity entity: goodEntityList){
            entity.handleCollisions();
        }
        if(playerShip!=null){
            playerShip.handleCollisions();
        }
        lastUpdateTime = currentTimeSeconds;
    }
}
