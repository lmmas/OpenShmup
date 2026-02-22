package engine.level.entity;

import engine.hitbox.Hitbox;
import engine.level.Level;
import engine.level.entity.extraComponent.ExtraComponent;
import engine.level.entity.trajectory.Trajectory;
import engine.level.spawnable.Spawnable;
import engine.scene.visual.SceneVisual;
import engine.types.Vec2D;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

abstract public class Entity {

    protected int entityId;
    @Getter
    protected EntityType type;
    @Getter
    protected boolean evil;

    final protected Vec2D position;

    final protected Vec2D trajectoryReferencePosition;

    final protected Vec2D size;
    @Getter
    protected float orientationRadians;
    @Getter @Setter
    protected boolean invincible;

    protected double startingTimeSeconds;
    @Getter
    protected double lifetimeSeconds;
    @Getter
    protected SceneVisual sprite;
    @Getter
    protected Hitbox hitbox;
    @Setter
    protected Trajectory trajectory;
    @Getter
    protected List<Spawnable> deathSpawn;
    @Getter
    protected ArrayList<ExtraComponent> extraComponents;

    protected Level level;

    public Entity(EntityType type, float trajectoryReferencePosX, float trajectoryReferencePosY, float sizeX, float sizeY, float orientationRadians, boolean evil, int entityId, SceneVisual sprite, Trajectory trajectory, Hitbox hitbox, List<Spawnable> deathSpawn, ArrayList<ExtraComponent> extraComponents) {
        this.type = type;
        this.level = null;
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
        this.startingTimeSeconds = 0.0d;
        this.lifetimeSeconds = 0.0d;
        this.deathSpawn = deathSpawn;
        this.extraComponents = extraComponents;
        setOrientation(orientationRadians);
        setSize(sizeX, sizeY);
        setPosition(trajectoryReferencePosX, trajectoryReferencePosY);
    }

    abstract public Entity copy();

    public void setSize(float sizeX, float sizeY) {
        this.size.x = sizeX;
        this.size.y = sizeY;
        sprite.setScale(sizeX, sizeY);
        hitbox.setSize(sizeX, sizeY);
    }

    public Vec2D getPosition() {
        return new Vec2D(position);
    }

    public void setPosition(float positionX, float positionY) {
        position.x = positionX;
        position.y = positionY;
        sprite.setPosition(positionX, positionY);
        hitbox.setPosition(positionX, positionY);
    }

    public Vec2D getTrajectoryReferencePosition() {
        return new Vec2D(trajectoryReferencePosition);
    }

    public void setTrajectoryStartingPosition(float startingPositionX, float startingPositionY) {
        trajectoryReferencePosition.x = startingPositionX;
        trajectoryReferencePosition.y = startingPositionY;
    }

    public void setOrientation(float orientationRadians) {
        this.orientationRadians = orientationRadians;
        hitbox.setOrientation(orientationRadians);
    }

    public void addExtraComponent(ExtraComponent extraComponent) {
        extraComponents.add(extraComponent);
    }

    public void init(Level level) {
        assert level != null;
        this.level = level;
        this.lifetimeSeconds = 0.0d;
        this.startingTimeSeconds = level.getLevelTimeSeconds();
    }

    public void update(double currentTimeSeconds) {
        lifetimeSeconds = currentTimeSeconds - startingTimeSeconds;
        trajectory.update(this, level);
        for (ExtraComponent extraComponent : extraComponents) {
            extraComponent.update(this, level);
        }
    }
}
