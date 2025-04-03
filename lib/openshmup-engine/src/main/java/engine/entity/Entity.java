package engine.entity;

import engine.Vec2D;
import engine.entity.extraComponent.ExtraComponent;
import engine.entity.hitbox.Hitbox;
import engine.entity.hitbox.SimpleHitBox;
import engine.entity.shot.EntityShot;
import engine.entity.shot.NonPlayerShot;
import engine.entity.shot.PlayerShot;
import engine.entity.sprite.AnimatedSprite;
import engine.entity.sprite.EntitySprite;
import engine.entity.sprite.SimpleSprite;
import engine.entity.trajectory.FixedTrajectory;
import engine.entity.trajectory.Trajectory;
import engine.scene.display.Animation;
import engine.scene.display.AnimationInfo;
import engine.graphics.DynamicImage;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

import java.util.ArrayList;
import java.util.function.Function;

abstract public class Entity {
    protected LevelScene scene;
    final protected Vec2D trajectoryReferencePosition;
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
    protected ArrayList<ExtraComponent> extraComponents;

    public Entity(float trajectoryReferencePosX, float trajectoryReferencePosY, float sizeX, float sizeY, float orientationRadians, boolean evil, int entityId, EntitySprite sprite, Trajectory trajectory, Hitbox hitbox, EntityShot shot, Spawnable deathSpawn, ArrayList<ExtraComponent> extraComponents) {
        this.scene = null;
        this.trajectoryReferencePosition = new Vec2D(trajectoryReferencePosX, trajectoryReferencePosY);
        this.position = new Vec2D(trajectoryReferencePosX, trajectoryReferencePosY);
        this.size = new Vec2D(sizeX, sizeY);
        this.hitbox = hitbox;
        this.orientationRadians = orientationRadians;
        this.sprite = sprite;
        this.trajectory = trajectory.copyIfNotReusable();
        this.evil = evil;
        this.invincible = false;
        this.entityId = entityId;
        this.startingTimeSeconds = 0.0f;
        this.lifetimeSeconds = 0.0f;
        this.shot = shot;
        this.deathSpawn = deathSpawn;
        this.extraComponents = extraComponents;
        setOrientation(orientationRadians);
        setSize(sizeX, sizeY);
        setPosition(trajectoryReferencePosX, trajectoryReferencePosY);
    }

    abstract public Entity copy();

    public void setScene(LevelScene scene) {
        this.scene = scene;
        trajectory.setScene(scene);
        shot.setScene(scene);
        for(var component: extraComponents){
            component.setScene(scene);
        }
        this.lifetimeSeconds = 0.0f;
        this.startingTimeSeconds = scene.getSceneTimeSeconds();
    }

    abstract public EntityType getType();

    public EntitySprite getSprite() {
        return sprite;
    }

    public Vec2D getTrajectoryReferencePosition() {
        return new Vec2D(trajectoryReferencePosition);
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
        trajectoryReferencePosition.x = startingPositionX;
        trajectoryReferencePosition.y = startingPositionY;
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
        for(ExtraComponent extraComponent: extraComponents){
            extraComponent.update();
        }
        sprite.update(currentTimeSeconds);
    }

    public Spawnable getDeathSpawn() {
        return deathSpawn;
    }

    public EntityShot getShot() {
        return shot;
    }

    public ArrayList<ExtraComponent> getExtraComponents() {
        return extraComponents;
    }

    public void addExtraComponent(ExtraComponent extraComponent){
        extraComponents.add(extraComponent);
    }

    public void deathEvent(){
        if(deathSpawn != null){
            deathSpawn.copyWithOffset(position.x, position.y).spawn(scene);
        }
    }

    public static class Builder{
        private final Vec2D startingPosition = new Vec2D(0.0f, 0.0f);
        private Vec2D size = new Vec2D(0.0f, 0.0f);
        private float orientationRadians = 0.0f;
        private int id = -1;
        private boolean evil = true;
        private boolean isShip = false;
        private EntitySprite sprite = EntitySprite.DEFAULT();
        private Hitbox hitbox = new SimpleHitBox(startingPosition.x, startingPosition.y, size.x, size.y);
        private Spawnable deathSpawn = Spawnable.DEFAULT();
        private int hitPoints = 1;
        private EntityShot shot = EntityShot.DEFAULT();
        private Trajectory trajectory = Trajectory.DEFAULT();
        private ArrayList<ExtraComponent> extraComponents = new ArrayList<>();

        public Builder setHitPoints(int hp){
            this.hitPoints = hp;
            this.isShip = true;
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
                    this.sprite = new SimpleSprite(new DynamicImage(filepath, layer, size.x, size.y));
                }
            }
            return this;
        }

        public Builder createSprite(int layer, AnimationInfo info, float framePeriodSeconds, boolean looping, boolean orientable){
            if(size != null){
                if(orientable){

                }else{
                    this.sprite = new AnimatedSprite(new Animation(layer, info, framePeriodSeconds, looping, size.x, size.y));
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

        public Builder createShot(Spawnable spawnable, float shotPeriodSeconds, float firstShotTimeSeconds){
            assert this.id != -1: "incorrect building steps order: must define the id first";
            if(this.id == 0){
                this.shot = new PlayerShot(spawnable, shotPeriodSeconds, firstShotTimeSeconds);
            }
            else{
                this.shot = new NonPlayerShot(spawnable, shotPeriodSeconds, firstShotTimeSeconds);
            }
            return this;
        }

        public Entity build(){
            assert (sprite != null): "Entity construction error: null fields";
            if(!isShip){
                return new NonShipEntity(startingPosition.x, startingPosition.y, size.x, size.y, orientationRadians, evil, id, sprite, trajectory, hitbox, shot, deathSpawn, extraComponents);
            }
            else{
                return new Ship(startingPosition.x, startingPosition.y, size.x, size.y, orientationRadians, evil, id, sprite, trajectory, hitbox, shot, deathSpawn, extraComponents, hitPoints);
            }
        }
    }
}
