package engine.level.entity;

import engine.hitbox.Hitbox;
import engine.level.Level;
import engine.level.entity.extraComponent.ExtraComponent;
import engine.level.entity.trajectory.Trajectory;
import engine.level.spawnable.Spawnable;
import engine.scene.visual.SceneVisual;
import lombok.Getter;
import lombok.Setter;
import types.Vec2D;

import java.util.ArrayList;
import java.util.List;

abstract public class Entity {

    protected int entityId;
    @Getter
    protected EntityType type;
    @Getter
    protected boolean evil;

    protected Vec2D position;

    protected Vec2D trajectoryReferencePosition;

    protected Vec2D size;
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

    public Entity(EntityType type, Vec2D trajectoryReferencePos, Vec2D size, float orientationRadians, boolean evil, int entityId, SceneVisual sprite, Trajectory trajectory, Hitbox hitbox, List<Spawnable> deathSpawn, ArrayList<ExtraComponent> extraComponents) {
        this.type = type;
        this.level = null;
        this.trajectoryReferencePosition = trajectoryReferencePos;
        this.position = trajectoryReferencePos;
        this.size = size;
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
        setSize(size);
        setPosition(trajectoryReferencePos);
    }

    abstract public Entity copy();

    public void setSize(Vec2D size) {
        this.size = size;
        sprite.setScale(size);
        hitbox.setSize(size);
    }

    public Vec2D getPosition() {
        return new Vec2D(position);
    }

    public void setPosition(Vec2D position) {
        this.position = position;
        sprite.setPosition(position);
        hitbox.setPosition(position);
    }

    public Vec2D getTrajectoryReferencePosition() {
        return new Vec2D(trajectoryReferencePosition);
    }

    public void setTrajectoryStartingPosition(Vec2D startingPosition) {
        trajectoryReferencePosition = startingPosition;
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
