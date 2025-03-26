package engine.entity;

import engine.Vec2D;
import engine.entity.hitbox.Hitbox;
import engine.entity.hitbox.SimpleHitBox;
import engine.entity.shot.EntityShot;
import engine.entity.shot.NonPlayerShot;
import engine.entity.shot.PlayerShot;
import engine.entity.sprite.EntitySprite;
import engine.entity.trajectory.FixedTrajectory;
import engine.entity.trajectory.Trajectory;
import engine.graphics.Animation;
import engine.graphics.AnimationInfo;
import engine.scene.display.DynamicImage;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

import java.util.function.Function;

abstract public class Entity {
    protected LevelScene scene;
    final protected Vec2D trajectoryStartingPosition;
    final protected Vec2D position;
    final protected Vec2D size;
    protected float orientationRadians;
    protected boolean evil;
    protected boolean invincible;
    protected int entityId;
    protected float lifetimeSeconds;
    protected float startingTimeSeconds;
    protected EntitySprite sprite;
    protected Hitbox hitbox;
    protected Trajectory trajectory;
    protected EntityShot shot;
    protected Spawnable deathSpawn;

    public Entity(float trajectoryStartingPosX, float trajectoryStartingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, int entityId, EntitySprite sprite, Trajectory trajectory, Hitbox hitbox, EntityShot shot, Spawnable deathSpawn) {
        this.scene = null;
        this.trajectoryStartingPosition = new Vec2D(trajectoryStartingPosX, trajectoryStartingPosY);
        this.position = new Vec2D(trajectoryStartingPosX, trajectoryStartingPosY);
        this.size = new Vec2D(sizeX, sizeY);
        this.hitbox = hitbox;
        this.orientationRadians = orientationRadians;
        this.sprite = sprite;
        this.trajectory = trajectory.copyIfNotReusable();
        this.evil = evil;
        this.entityId = entityId;
        this.invincible = false;
        this.deathSpawn = deathSpawn;
        this.shot = shot;
        this.startingTimeSeconds = 0.0f;
        setOrientation(orientationRadians);
        setSize(sizeX, sizeY);
        setPosition(trajectoryStartingPosX, trajectoryStartingPosY);
    }

    abstract public Entity copy();

    public void setScene(LevelScene scene) {
        this.scene = scene;
        trajectory.setScene(scene);
        shot.setScene(scene);
        this.startingTimeSeconds = scene.getSceneTimeSeconds();
    }
    abstract public boolean isShip();

    public EntitySprite getSprite() {
        return sprite;
    }

    public Vec2D getTrajectoryStartingPosition() {
        return new Vec2D(trajectoryStartingPosition);
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

    public Hitbox getHitbox(){
        return hitbox;
    }

    public void setPosition(float positionX, float positionY){
        position.x = positionX;
        position.y = positionY;
        sprite.setPosition(positionX, positionY);
        hitbox.setPosition(positionX, positionY);
    }

    public void setTrajectoryStartingPosition(float startingPositionX, float startingPositionY){
        trajectoryStartingPosition.x = startingPositionX;
        trajectoryStartingPosition.y = startingPositionY;
    }
    public void setSize(float sizeX, float sizeY){
        this.size.x = sizeX;
        this.size.y = sizeY;
        sprite.setSize(sizeX, sizeY);
        hitbox.setSize(sizeX, sizeY);
    }

    public void setOrientation(float orientationRadians) {
        this.orientationRadians = orientationRadians;
        sprite.setOrientation(orientationRadians);
        hitbox.setOrientation(orientationRadians);
    }

    public void setTrajectory(Trajectory trajectory) {
        this.trajectory = trajectory;
    }

    public void update(float currentTimeSeconds){
        lifetimeSeconds = currentTimeSeconds - startingTimeSeconds;
        trajectory.update(this);
        shot.update(this);
        sprite.update(currentTimeSeconds);
    }
    public void deathEvent(){
        if(deathSpawn != null){
            deathSpawn.copyWithOffset(position.x, position.y).spawn(scene);
        }
    }

    public static class Builder{
        private final Vec2D startingPosition = new Vec2D(0.0f, 0.0f);
        private Vec2D size;
        private float orientationRadians = 0.0f;
        private int id = -1;
        private boolean evil = true;
        private boolean isShip = false;
        private EntitySprite sprite = EntitySprite.DEFAULT();
        private Hitbox hitbox = Hitbox.DEFAULT();
        private Spawnable deathSpawn = Spawnable.DEFAULT();
        private int hitPoints = 1;
        private EntityShot shot = EntityShot.DEFAULT();
        private Trajectory trajectory = Trajectory.DEFAULT();

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
            this.size = new Vec2D(sizeX, sizeY);
            return this;
        }

        public Builder setOrientation(float orientationRadians){
            this.orientationRadians = orientationRadians;
            return this;
        }

        public Builder setId(int id){
            this.id = id;
            if(this.id == 0){
                this.evil = false;
            }
            return this;
        }

        public Builder setEvil(boolean evil){
            this.evil = evil;
            return this;
        }

        public Builder createSprite(int layer, String filepath, boolean orientable){
            if(size != null){
                if(orientable){

                }else{
                    this.sprite = new DynamicImage(filepath, layer, size.x, size.y);
                }
            }
            return this;
        }

        public Builder createSprite(int layer, AnimationInfo info, float framePeriodSeconds, boolean looping, boolean orientable){
            if(size != null){
                if(orientable){

                }else{
                    this.sprite = new Animation(layer, info, framePeriodSeconds, looping, size.x, size.y);
                }
            }
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

        public Builder createShot(Spawnable spawnable, float shotPeriodSeconds, float timeBeforeFirstShot){
            assert this.id != -1: "incorrect building steps order: must define the id first";
            if(this.id == 0){
                this.shot = new PlayerShot(spawnable, shotPeriodSeconds, timeBeforeFirstShot);
            }
            else{
                this.shot = new NonPlayerShot(spawnable, shotPeriodSeconds, timeBeforeFirstShot);
            }
            return this;
        }

        public Entity build(){
            if(hitbox == null){
                hitbox = new SimpleHitBox(startingPosition.x, startingPosition.y, size.x, size.y);
            }
            assert (sprite != null): "Entity construction error: null fields";
            if(isShip){
                return new NonShipEntity(startingPosition.x, startingPosition.y, size.x, size.y, orientationRadians, evil, id, sprite, trajectory, hitbox, shot, deathSpawn);
            }
            else{
                return new Ship(startingPosition.x, startingPosition.y, size.x, size.y, orientationRadians, evil, id, sprite, trajectory, hitbox, shot, deathSpawn,hitPoints);
            }
        }
    }
}
