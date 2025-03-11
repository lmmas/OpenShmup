package engine.scene;

import engine.graphics.Graphic;
import engine.entity.NonPlayerEntity;
import engine.entity.PlayerShip;

import java.util.LinkedList;

public class LevelScene extends Scene{
    PlayerShip playerShip;
    LinkedList<NonPlayerEntity> goodEntityList = new LinkedList<>();
    LinkedList<NonPlayerEntity> evilEntityList = new LinkedList<>();
    public LevelScene(long window) {
        super(window);
    }

    public void addEntity(NonPlayerEntity entity){
        Graphic<?,?> sprite = (Graphic<?,?>)entity.getSprite();
        addGraphic(sprite);
        if(entity.isEvil()){
            evilEntityList.add(entity);
        }
        else{
            goodEntityList.add(entity);
        }
    }
    @Override
    public void update() {
        float currentTimeSeconds = (float)(System.currentTimeMillis() - startingTimeMilis)/ 1000.0f;
        float deltaTime = currentTimeSeconds - lastUpdateTime;
        lastUpdateTime = currentTimeSeconds;
        for(SceneVisual visual: visualList){
            visual.update();
        }
        for(NonPlayerEntity entity: goodEntityList){
            entity.actionsAndMoves();
        }
        for(NonPlayerEntity entity: evilEntityList){
            entity.actionsAndMoves();
        }
        if(playerShip != null){
            playerShip.actionsAndMoves(window);
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
    }
}
