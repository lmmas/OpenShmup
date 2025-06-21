package engine.entity;

import engine.assets.Texture;
import engine.entity.extraComponent.ExtraComponent;
import engine.entity.extraComponent.NonPlayerShot;
import engine.entity.extraComponent.PlayerShot;
import engine.entity.hitbox.CompositeHitbox;
import engine.entity.hitbox.Hitbox;
import engine.entity.hitbox.SimpleRectangleHitbox;
import engine.entity.sprite.AnimatedSprite;
import engine.entity.sprite.EntitySprite;
import engine.entity.sprite.SimpleSprite;
import engine.entity.trajectory.FixedTrajectory;
import engine.entity.trajectory.Trajectory;
import engine.graphics.Image2D;
import engine.scene.LevelScene;
import engine.scene.display.Animation;
import engine.scene.display.AnimationInfo;
import engine.scene.spawnable.Spawnable;
import engine.types.Vec2D;

import java.util.ArrayList;
import java.util.function.Function;

abstract public class Entity {
    protected LevelScene scene;
    protected int entityId;
    protected boolean evil;
    final protected Vec2D position;
    final protected Vec2D trajectoryReferencePosition;
    final protected Vec2D size;
    protected float orientationRadians;
    protected boolean invincible;
    protected float startingTimeSeconds;
    protected float lifetimeSeconds;
    protected EntitySprite sprite;
    protected Hitbox hitbox;
    protected Trajectory trajectory;
    protected Spawnable deathSpawn;
    protected ArrayList<ExtraComponent> extraComponents;

    public Entity(float trajectoryReferencePosX, float trajectoryReferencePosY, float sizeX, float sizeY, float orientationRadians, boolean evil, int entityId, EntitySprite sprite, Trajectory trajectory, Hitbox hitbox, Spawnable deathSpawn, ArrayList<ExtraComponent> extraComponents) {
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
        this.deathSpawn = deathSpawn;
        this.extraComponents = extraComponents;
        setOrientation(orientationRadians);
        setSize(sizeX, sizeY);
        setPosition(trajectoryReferencePosX, trajectoryReferencePosY);
    }

    abstract public Entity copy();

    public void setScene(LevelScene scene) {
        this.scene = scene;
        this.lifetimeSeconds = 0.0f;
        this.startingTimeSeconds = scene.getSceneTimeSeconds();
    }

    abstract public EntityType getType();

    public EntitySprite getSprite() {
        return sprite;
    }

    public Vec2D getTrajectoryReferencePosition() {
        return trajectoryReferencePosition.copy();
    }

    public Vec2D getPosition(){
        return position.copy();
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
        trajectory.update(this, scene);
        for(ExtraComponent extraComponent: extraComponents){
            extraComponent.update(this, scene);
        }
        sprite.update(currentTimeSeconds);
    }

    public Spawnable getDeathSpawn() {
        return deathSpawn;
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
        private int id = -1;
        private EntityType type = EntityType.PROJECTILE;
        private int hitPoints = 1;
        private boolean evil = true;
        private final Vec2D size = new Vec2D(0.0f, 0.0f);
        private final Vec2D startingPosition = new Vec2D(0.0f, 0.0f);
        private float orientationRadians = 0.0f;
        private EntitySprite sprite = EntitySprite.DEFAULT();
        private Hitbox hitbox = Hitbox.DEFAULT();
        private Trajectory trajectory = Trajectory.DEFAULT();
        private Spawnable deathSpawn = Spawnable.DEFAULT();
        private final ArrayList<ExtraComponent> extraComponents = new ArrayList<>();

        public Builder setType(EntityType type){
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
            if(this.id == 0){
                this.evil = false;
            }
            return this;
        }

        public Builder setEvil(boolean evil){
            this.evil = evil;
            return this;
        }

        public Builder createSprite(int layer, Texture texture, boolean orientable){
            if(size != null){
                if(orientable){

                }else{
                    this.sprite = new SimpleSprite(new Image2D(texture, layer, true, size.x, size.y));
                }
            }
            return this;
        }

        public Builder createSprite(int layer, Texture spriteTexture, AnimationInfo info, float framePeriodSeconds, boolean looping, boolean orientable){
            if(size != null){
                if(orientable){

                }else{
                    this.sprite = new AnimatedSprite(new Animation(layer, spriteTexture, info, framePeriodSeconds, looping, size.x, size.y));
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

        public Builder addCompositeHitbox(Texture hitboxTexture, boolean orientable){
            assert size.x != 0.0f && size.y != 0.0f:"Invalid hitbox size";
            if(orientable){

            }
            else{
                hitbox = new CompositeHitbox(hitboxTexture, size.x, size.y);
            }
            return this;
        }

        public Builder addRectangleHitbox(boolean orientable){
            if(orientable){

            }
            else {
                hitbox = new SimpleRectangleHitbox(0.0f, 0.0f, size.x, size.y);
            }
            return this;
        }

        public Builder createShot(Spawnable spawnable, float shotPeriodSeconds, float firstShotTimeSeconds){
            assert this.id != -1: "incorrect building steps order: must define the id first";
            if(this.id == 0){
                extraComponents.add( new PlayerShot(spawnable, shotPeriodSeconds, firstShotTimeSeconds));
            }
            else{
                extraComponents.add( new NonPlayerShot(spawnable, shotPeriodSeconds, firstShotTimeSeconds));
            }
            return this;
        }

        public Builder addExtraComponent(ExtraComponent extraComponent){
            extraComponents.add(extraComponent);
            return this;
        }

        public Entity build(){
            assert (sprite != null): "Entity construction error: null fields";
            if(type != EntityType.SHIP){
                return new NonShipEntity(startingPosition.x, startingPosition.y, size.x, size.y, orientationRadians, evil, id, sprite, trajectory, hitbox, deathSpawn, extraComponents);
            }
            else{
                return new Ship(startingPosition.x, startingPosition.y, size.x, size.y, orientationRadians, evil, id, sprite, trajectory, hitbox, deathSpawn, extraComponents, hitPoints);
            }
        }
    }
}
