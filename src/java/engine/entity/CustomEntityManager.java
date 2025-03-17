package engine.entity;

import engine.Vec2D;
import engine.graphics.AnimationInfo;
import engine.scene.LevelScene;

import java.util.HashMap;
import java.util.function.BiFunction;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class CustomEntityManager {
    private HashMap<Integer, BiFunction<LevelScene, Vec2D, Entity>> customEntities;

    public CustomEntityManager(){
        this.customEntities = new HashMap<>();
        BiFunction<LevelScene, Vec2D, Entity> entityConstructor1 = (scene, startingPosition) ->{
            AnimationInfo testAnimInfo = new AnimationInfo("src/resources/textures/enemy-medium.png", 2, 32, 16, 0, 0, 32, 0);
            return new Entity.Builder().setScene(scene)
                    .setStartingPosition(startingPosition.x, startingPosition.y).setSize(0.5f,0.5f)
                    .createSprite(3,testAnimInfo, 0.25f, true, false)
                    .createFixedTrajectory(t-> 0.3f * (float) cos(t) + 0.5f, t-> 0.3f * (float) sin(t) + 0.5f, false)
                    .setId(1)
                    .build();
        };
        addCustomEntity(1, entityConstructor1);
    }

    public void addCustomEntity(int id, BiFunction<LevelScene, Vec2D, Entity> constructor){
        customEntities.put(id, constructor);
    }

    public Entity createCustomEntity(LevelScene scene, int id, Vec2D startingPosition){
        return customEntities.get(id).apply(scene, startingPosition);
    }
}
