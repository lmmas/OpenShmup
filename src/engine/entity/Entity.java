package engine.entity;

import engine.Vec2D;
import engine.entity.trajectory.FixedTrajectory;
import engine.graphics.Animation;
import engine.graphics.AnimationInfo;
import engine.graphics.MovingImage;
import engine.scene.LevelScene;

import java.util.function.Function;

public class Entity {
    protected LevelScene scene;
    final protected Vec2D startingPosition;
    final protected Vec2D position;
    final protected Vec2D size;
    protected float orientationRadians;
    protected boolean evil;
    protected boolean invincible = false;
    protected int entityId;
    protected float lifetimeSeconds;
    protected EntitySprite sprite;
    protected Trajectory trajectory;
    final private float startingTimeSeconds;

    public Entity(LevelScene scene, float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, EntitySprite sprite, Trajectory trajectory) {
        this.scene = scene;
        this.startingPosition = new Vec2D(startingPosX, startingPosY);
        this.position = new Vec2D(startingPosX, startingPosY);
        sprite.setPosition(startingPosX, startingPosY);
        this.size = new Vec2D(sizeX, sizeY);
        sprite.setSize(sizeX, sizeY);
        this.orientationRadians = orientationRadians;
        sprite.setOrientation(orientationRadians);
        this.sprite = sprite;
        this.trajectory = trajectory.copyIfNotReusable();
        this.evil = evil;
        this.startingTimeSeconds = scene.getSceneTime();
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

    public float getOrientationRadians() {
        return orientationRadians;
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

    public void setOrientationRadians(float orientationRadians) {
        this.orientationRadians = orientationRadians;
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

    public static class Builder{
        private LevelScene scene;
        private final Vec2D startingPosition = new Vec2D(0.0f, 0.0f);
        private final Vec2D size = new Vec2D(0.0f, 0.0f);
        private float orientationRadians = 0.0f;
        private int id = 0;
        private boolean evil = true;
        private EntitySprite sprite = null;
        private Trajectory trajectory = Trajectory.DEFAULT();
        public Builder setScene(LevelScene scene){
            this.scene = scene;
            return this;
        }

        public Builder setStartingPosition(float startPosX, float startPosY){
            this.startingPosition.x = startPosX;
            this.startingPosition.y = startPosY;
            return this;
        }

        public Builder setSize(float sizeX, float sizeY) {
            this.size.x = sizeX;
            this.size.y = sizeY;
            return this;
        }

        public Builder setOrientation(float orientationRadians){
            this.orientationRadians = orientationRadians;
            return this;
        }

        public Builder setId(int id){
            this.id = id;
            return this;
        }

        public Builder setEvil(boolean evil){
            this.evil = evil;
            return this;
        }

        public Builder createSprite(int layer, String filepath, boolean orientable){
            if(scene != null){
                if(orientable){

                }else{
                    this.sprite = new MovingImage(filepath, scene, layer);
                }
            }
            return this;
        }

        public Builder createSprite(int layer, AnimationInfo info, float framePeriodSeconds, boolean looping, boolean orientable){
            if(scene != null){
                if(orientable){

                }else{
                    this.sprite = new Animation(scene, layer, info, framePeriodSeconds, looping);
                }
            }
            return this;
        }

        public Builder setSprite(EntitySprite sprite){
            this.sprite = sprite;
            return this;
        }
        public Builder createFixedTrajectory(Function<Float, Float> trajectoryFunctionX, Function<Float, Float> trajectoryFunctionY){
            this.trajectory = new FixedTrajectory(trajectoryFunctionX, trajectoryFunctionY);
            return this;
        }
        public Builder createFixedTrajectory(Function<Float, Float> trajectoryFunctionX, Function<Float, Float> trajectoryFunctionY, boolean relative){
            this.trajectory = new FixedTrajectory(trajectoryFunctionX, trajectoryFunctionY, relative);
            return this;
        }
        public Builder setTrajectory(Trajectory trajectory){
            this.trajectory = trajectory;
            return this;
        }

        public Entity build(){
            if(scene != null && sprite != null && trajectory !=null){
                return new Entity(scene, startingPosition.x, startingPosition.y, size.x, size.y, orientationRadians, evil, sprite, trajectory);
            }
            else{
                throw new NullPointerException();
            }
        }
    }
}
