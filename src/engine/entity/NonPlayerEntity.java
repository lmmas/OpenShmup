package engine.entity;

import engine.Vec2D;
import engine.scene.LevelScene;

public class NonPlayerEntity {
    protected LevelScene scene;
    final protected Vec2D startingPosition;
    protected Vec2D position;
    protected Vec2D size;
    protected float orientationRadiants;
    protected boolean evil;
    protected boolean invincible = false;
    protected int entityId;
    protected float lifetimeSeconds;
    protected EntitySprite sprite;
    protected Trajectory trajectory;
    final private float startingTimeSeconds;

    public NonPlayerEntity(LevelScene scene, EntitySprite sprite, float startingPosX, float startingPosY, Trajectory trajectory, float sizeX, float sizeY, boolean evil) {
        this.scene = scene;
        this.sprite = sprite;
        this.startingPosition = new Vec2D(startingPosX, startingPosY);
        this.position = new Vec2D(startingPosX, startingPosY);
        this.size = new Vec2D(sizeX, sizeY);
        this.trajectory = trajectory.copyIfNotReusable();
        this.evil = evil;
        this.startingTimeSeconds = scene.getSceneTime();
        sprite.setPosition(startingPosX, startingPosY);
        sprite.setSize(sizeX, sizeY);
        scene.addEntity(this);
    }

    public EntitySprite getSprite() {
        return sprite;
    }

    public Vec2D getStartingPosition() {
        return new Vec2D(startingPosition);
    }

    public Vec2D getPosition(){
        return new Vec2D(position);
    }

    public float getOrientationRadiants() {
        return orientationRadiants;
    }

    final public float getLifetimeSeconds() {
        return lifetimeSeconds;
    }

    public boolean isEvil() {
        return evil;
    }

    public void setPosition(float positionX, float positionY){
        position.x = positionX;
        position.y = positionY;
        sprite.setPosition(position.x, position.y);
    }

    public void setSize(float sizeX, float sizeY){
        this.size.x = sizeX;
        this.size.y = sizeY;
    }

    public void setOrientationRadiants(float orientationRadiants) {
        this.orientationRadiants = orientationRadiants;
    }

    public void update(){
        float currentTimeSeconds = scene.getSceneTime();
        lifetimeSeconds = currentTimeSeconds - startingTimeSeconds;
        trajectory.update(this);
        sprite.update();
    }

    public void handleCollisions(){

    }

    public void delete(){
        sprite.delete();
    }
}
