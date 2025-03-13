package engine.entity;

import engine.graphics.EntitySprite;
import engine.scene.LevelScene;
import engine.scene.Scene;

public class NonPlayerEntity {
    LevelScene scene;
    protected EntitySprite sprite;
    final protected float startingPosX;
    final protected float startingPosY;
    protected Trajectory trajectory;
    protected float positionX;
    protected float positionY;
    protected float sizeX;
    protected float sizeY;
    protected boolean evil;
    protected int entityId;
    final private float startingTimeSeconds;
    private float lifetimeSeconds;

    public NonPlayerEntity(LevelScene scene, EntitySprite sprite, float startingPosX, float startingPosY, Trajectory trajectory, float sizeX, float sizeY, boolean evil) {
        this.scene = scene;
        this.sprite = sprite;
        this.startingPosX = startingPosX;
        this.startingPosY = startingPosY;
        this.positionX = startingPosX;
        this.positionY = startingPosY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.trajectory = trajectory;
        this.evil = evil;
        this.startingTimeSeconds = scene.getSceneTime();
        sprite.setPosition(startingPosX, startingPosY);
        sprite.setSize(sizeX, sizeY);
        scene.addEntity(this);
    }

    public EntitySprite getSprite() {
        return sprite;
    }

    public float getStartingPosX() {
        return startingPosX;
    }

    public float getStartingPosY() {
        return startingPosY;
    }

    final public float getLifetimeSeconds() {
        return lifetimeSeconds;
    }

    public boolean isEvil() {
        return evil;
    }

    public void actionsAndMoves(){
        float currentTimeSeconds = scene.getSceneTime();
        lifetimeSeconds = currentTimeSeconds - startingTimeSeconds;
        positionX = trajectory.getX(this);
        positionY = trajectory.getY(this);
        sprite.setPosition(positionX, positionY);
    }

    public void handleCollisions(){

    }
}
