package engine.entity;

import engine.GlobalVars;
import engine.Vec2D;
import engine.entity.hitbox.EntitySprite;
import engine.entity.hitbox.SimpleHitBox;
import engine.entity.trajectory.FixedTrajectory;
import engine.entity.trajectory.PlayerControlledTrajectory;
import engine.entity.trajectory.Trajectory;
import engine.graphics.Animation;
import engine.graphics.AnimationInfo;
import engine.graphics.MovingImage;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

import java.util.function.Function;

abstract public class Entity {
    final protected LevelScene scene;
    final protected EntityType type;
    final protected Vec2D startingPosition;
    final protected Vec2D position;
    final protected Vec2D size;
    protected float orientationRadians;
    protected boolean evil;
    protected boolean invincible;
    protected int entityId;
    protected float lifetimeSeconds;
    protected float startingTimeSeconds;
    protected EntitySprite sprite;
    protected SimpleHitBox hitbox;
    protected Trajectory trajectory;
    protected Spawnable deathSpawn;

    public Entity(LevelScene scene, EntityType type, float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, EntitySprite sprite, Trajectory trajectory, SimpleHitBox hitbox, Spawnable deathSpawn) {
        this.scene = scene;
        this.type = type;
        this.startingPosition = new Vec2D(startingPosX, startingPosY);
        this.position = new Vec2D(startingPosX, startingPosY);
        sprite.setPosition(startingPosX, startingPosY);
        this.size = new Vec2D(sizeX, sizeY);
        sprite.setSize(sizeX, sizeY);
        this.hitbox = hitbox;
        this.orientationRadians = orientationRadians;
        sprite.setOrientation(orientationRadians);
        this.sprite = sprite;
        this.trajectory = trajectory.copyIfNotReusable();
        this.evil = evil;
        this.invincible = false;
        this.startingTimeSeconds = scene.getSceneTimeSeconds();
        this.deathSpawn = deathSpawn;
    }

    public EntityType getType(){
        return type;
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

    public boolean isInvincible(){
        return invincible;
    }

    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    public SimpleHitBox getHitbox(){
        return hitbox;
    }

    public void setPosition(float positionX, float positionY){
        position.x = positionX;
        position.y = positionY;
        sprite.setPosition(positionX, positionY);
        hitbox.setPosition(positionX, positionY);
    }

    public void setStartingPosition(float startingPositionX, float startingPositionY){
        startingPosition.x = startingPositionX;
        startingPosition.y = startingPositionY;
    }
    public void setSize(float sizeX, float sizeY){
        this.size.x = sizeX;
        this.size.y = sizeY;
        sprite.setSize(sizeX, sizeY);
        hitbox.setSize(sizeX, sizeY);
    }

    public void setOrientationRadians(float orientationRadians) {
        this.orientationRadians = orientationRadians;
    }

    public void setTrajectory(Trajectory trajectory) {
        this.trajectory = trajectory;
    }

    public void update(){
        float currentTimeSeconds = scene.getSceneTimeSeconds();
        lifetimeSeconds = currentTimeSeconds - startingTimeSeconds;
        trajectory.update(this);
        sprite.update();
    }
    public void deathEvent(){
        if(deathSpawn != null){
            deathSpawn.copyWithOffset(position.x, position.y).spawn(scene);
        }
    }

    public static class Builder{
        private LevelScene scene;
        private final Vec2D startingPosition = new Vec2D(0.0f, 0.0f);
        private final Vec2D size = new Vec2D(0.0f, 0.0f);
        private float orientationRadians = 0.0f;
        private int id = 0;
        private boolean evil = true;
        private EntityType type = null;
        private EntitySprite sprite = null;
        private SimpleHitBox hitbox = null;
        private Spawnable deathSpawn = null;
        private int hitPoints = 1;
        private Spawnable shot = null;
        private float shotPeriodSeconds = 0.0f;
        private float timeBeforeFirstShotSeconds = 0.0f;
        private Trajectory trajectory = Trajectory.DEFAULT();
        public Builder setScene(LevelScene scene){
            this.scene = scene;
            return this;
        }

        public Builder setType(EntityType type) {
            this.type = type;
            return this;
        }

        public Builder setHitPoints(int hp){
            this.hitPoints = hp;
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
        public Builder setDeathSpawn(Spawnable deathSpawn){
            this.deathSpawn = deathSpawn;
            return this;
        }

        public Builder setShot(Spawnable shot, float shotPeriodSeconds, float timeBeforeFirstShotSeconds){
            this.shot = shot;
            this.shotPeriodSeconds = shotPeriodSeconds;
            this.timeBeforeFirstShotSeconds = timeBeforeFirstShotSeconds;
            return this;
        }

        public Entity build(){
            if(hitbox == null){
                hitbox = new SimpleHitBox(startingPosition.x, startingPosition.y, size.x, size.y);
            }
            assert (scene != null && type != null && sprite != null && trajectory !=null): "Entity construction error: null fields";
            if(id == 0){
                return new PlayerShip(scene, startingPosition.x, startingPosition.y, size.x, size.y, orientationRadians, sprite, hitbox, deathSpawn, hitPoints, shot, shotPeriodSeconds, timeBeforeFirstShotSeconds);
            }
            if(type == EntityType.PROJECTILE){
                return new Projectile(scene, startingPosition.x, startingPosition.y, size.x, size.y, orientationRadians, evil, sprite, trajectory, hitbox, deathSpawn);
            }
            else{
                if(shot != null){
                    return new ShootingShip(scene, startingPosition.x, startingPosition.y, size.x, size.y, orientationRadians, evil, sprite, trajectory, hitbox, deathSpawn, hitPoints, shot, shotPeriodSeconds, timeBeforeFirstShotSeconds);
                }
                return new Ship(scene, startingPosition.x, startingPosition.y, size.x, size.y, orientationRadians, evil, sprite, trajectory, hitbox, deathSpawn,hitPoints);
            }
        }
    }
}
