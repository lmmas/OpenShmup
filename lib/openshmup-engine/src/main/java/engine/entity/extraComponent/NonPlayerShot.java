package engine.entity.extraComponent;

import engine.entity.Entity;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;
import engine.types.Vec2D;

final public class NonPlayerShot implements ExtraComponent {

    private final Spawnable spawnable;

    private final float shotPeriodSeconds;

    private float nextShotTimeSeconds;

    public NonPlayerShot(Spawnable spawnable, float shotPeriodSeconds, float firstShotTimeSeconds) {
        this.spawnable = spawnable;
        this.shotPeriodSeconds = shotPeriodSeconds;
        this.nextShotTimeSeconds = firstShotTimeSeconds;
    }

    @Override
    public ExtraComponent copyIfNotReusable() {
        return new NonPlayerShot(spawnable, shotPeriodSeconds, nextShotTimeSeconds);
    }

    @Override
    public Spawnable getSpawnable() {
        return spawnable;
    }

    @Override
    public void init() {

    }

    @Override
    public void onRemove() {

    }

    @Override
    public void update(Entity entity, LevelScene scene) {
        double currentTimeSeconds = entity.getLifetimeSeconds();
        if (currentTimeSeconds >= nextShotTimeSeconds) {
            Vec2D position = entity.getPosition();
            spawnable.copyWithOffset(position.x, position.y).spawn(scene);
            nextShotTimeSeconds = nextShotTimeSeconds + shotPeriodSeconds;
        }
    }
}
