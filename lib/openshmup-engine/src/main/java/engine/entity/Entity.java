package engine.entity;

import engine.assets.Texture;
import engine.entity.extraComponent.ExtraComponent;
import engine.entity.extraComponent.NonPlayerShot;
import engine.entity.extraComponent.PlayerShot;
import engine.entity.hitbox.CompositeHitbox;
import engine.entity.hitbox.Hitbox;
import engine.entity.hitbox.SimpleRectangleHitbox;
import engine.entity.trajectory.FixedTrajectory;
import engine.entity.trajectory.Trajectory;
import engine.scene.LevelScene;
import engine.scene.spawnable.EmptySpawnable;
import engine.scene.spawnable.Spawnable;
import engine.types.Vec2D;
import engine.visual.SceneVisual;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.function.Function;

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

    @Setter
    @Getter
    protected boolean invincible;

    protected double startingTimeSeconds;

    protected double lifetimeSeconds;

    @Getter
    protected SceneVisual sprite;

    @Getter
    protected Hitbox hitbox;

    @Setter
    protected Trajectory trajectory;

    @Getter
    protected Spawnable deathSpawn;

    @Getter
    protected ArrayList<ExtraComponent> extraComponents;

    protected LevelScene scene;

    public Entity(EntityType type, float trajectoryReferencePosX, float trajectoryReferencePosY, float sizeX, float sizeY, float orientationRadians, boolean evil, int entityId, SceneVisual sprite, Trajectory trajectory, Hitbox hitbox, Spawnable deathSpawn, ArrayList<ExtraComponent> extraComponents) {
        this.type = type;
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

    final public double getLifetimeSeconds() {
        return lifetimeSeconds;
    }

    public void addExtraComponent(ExtraComponent extraComponent) {
        extraComponents.add(extraComponent);
    }

    public void setScene(LevelScene scene) {
        assert scene != null;
        this.scene = scene;
        this.lifetimeSeconds = 0.0d;
        this.startingTimeSeconds = scene.getSceneTimeSeconds();
    }

    public void update(double currentTimeSeconds) {
        lifetimeSeconds = currentTimeSeconds - startingTimeSeconds;
        trajectory.update(this, scene);
        for (ExtraComponent extraComponent : extraComponents) {
            extraComponent.update(this, scene);
        }
    }

    public void deathEvent() {
        if (deathSpawn != null && !(deathSpawn instanceof EmptySpawnable)) {
            deathSpawn.copyWithOffset(position.x, position.y).spawn(scene);
        }
    }

    public static class Builder {

        private int id = -1;

        private EntityType type = EntityType.PROJECTILE;

        private int hitPoints = 1;

        private boolean evil = true;

        private final Vec2D size = new Vec2D(0.0f, 0.0f);

        private final Vec2D startingPosition = new Vec2D(0.0f, 0.0f);

        private float orientationRadians = 0.0f;

        private SceneVisual sprite = SceneVisual.DEFAULT_EMPTY();

        private Hitbox hitbox = Hitbox.DEFAULT_EMPTY();

        private Trajectory trajectory = Trajectory.DEFAULT_EMPTY();

        private Spawnable deathSpawn = Spawnable.DEFAULT_EMPTY();

        private final ArrayList<ExtraComponent> extraComponents = new ArrayList<>();

        public Builder setType(EntityType type) {
            this.type = type;
            return this;
        }

        public Builder setHitPoints(int hp) {
            this.hitPoints = hp;
            return this;
        }

        public Builder setStartingPosition(float startPosX, float startPosY) {
            this.startingPosition.x = startPosX;
            this.startingPosition.y = startPosY;
            return this;
        }

        public Builder setSize(float sizeX, float sizeY) {
            this.size.x = sizeX;
            this.size.y = sizeY;
            return this;
        }

        public Builder setOrientation(float orientationRadians) {
            this.orientationRadians = orientationRadians;
            return this;
        }

        public Builder setId(int id) {
            this.id = id;
            if (this.id == 0) {
                this.evil = false;
            }
            return this;
        }

        public Builder setEvil(boolean evil) {
            this.evil = evil;
            return this;
        }

        public Builder setSprite(SceneVisual sprite) {
            this.sprite = sprite;
            return this;
        }

        public Builder createFixedTrajectory(Function<Double, Float> trajectoryFunctionX, Function<Double, Float> trajectoryFunctionY) {
            this.trajectory = new FixedTrajectory(trajectoryFunctionX, trajectoryFunctionY);
            return this;
        }

        public Builder createFixedTrajectory(Function<Double, Float> trajectoryFunctionX, Function<Double, Float> trajectoryFunctionY, boolean relative) {
            this.trajectory = new FixedTrajectory(trajectoryFunctionX, trajectoryFunctionY, relative);
            return this;
        }

        public Builder setTrajectory(Trajectory trajectory) {
            this.trajectory = trajectory;
            return this;
        }

        public Builder setDeathSpawn(Spawnable deathSpawn) {
            this.deathSpawn = deathSpawn;
            return this;
        }

        public Builder addCompositeHitbox(Texture hitboxTexture, boolean orientable) {
            assert size.x != 0.0f && size.y != 0.0f : "Invalid hitbox size";
            if (orientable) {

            }
            else {
                hitbox = new CompositeHitbox(hitboxTexture, size.x, size.y);
            }
            return this;
        }

        public Builder addRectangleHitbox(boolean orientable) {
            if (orientable) {

            }
            else {
                hitbox = new SimpleRectangleHitbox(0.0f, 0.0f, size.x, size.y);
            }
            return this;
        }

        public Builder createShot(Spawnable spawnable, float shotPeriodSeconds, float firstShotTimeSeconds) {
            assert this.id != -1 : "incorrect building steps order: must define the id first";
            if (this.id == 0) {
                extraComponents.add(new PlayerShot(spawnable, shotPeriodSeconds, firstShotTimeSeconds));
            }
            else {
                extraComponents.add(new NonPlayerShot(spawnable, shotPeriodSeconds, firstShotTimeSeconds));
            }
            return this;
        }

        public Builder addExtraComponent(ExtraComponent extraComponent) {
            extraComponents.add(extraComponent);
            return this;
        }

        public Entity build() {
            assert (sprite != null) : "Entity construction error: null fields";
            if (type != EntityType.SHIP) {
                return new Projectile(startingPosition.x, startingPosition.y, size.x, size.y, orientationRadians, evil, id, sprite, trajectory, hitbox, deathSpawn, extraComponents);
            }
            else {
                return new Ship(startingPosition.x, startingPosition.y, size.x, size.y, orientationRadians, evil, id, sprite, trajectory, hitbox, deathSpawn, extraComponents, hitPoints);
            }
        }
    }
}
