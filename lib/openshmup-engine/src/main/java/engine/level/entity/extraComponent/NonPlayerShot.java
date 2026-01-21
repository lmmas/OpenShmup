package engine.level.entity.extraComponent;

import engine.level.Level;
import engine.level.entity.Entity;
import engine.level.spawnable.Spawnable;
import engine.types.Vec2D;

import java.util.List;

final public class NonPlayerShot implements ExtraComponent {

    private final List<Spawnable> spawnables;

    private final float shotPeriodSeconds;

    private float nextShotTimeSeconds;

    public NonPlayerShot(List<Spawnable> spawnables, float shotPeriodSeconds, float firstShotTimeSeconds) {
        this.spawnables = spawnables;
        this.shotPeriodSeconds = shotPeriodSeconds;
        this.nextShotTimeSeconds = firstShotTimeSeconds;
    }

    @Override
    public ExtraComponent copyIfNotReusable() {
        return new NonPlayerShot(spawnables, shotPeriodSeconds, nextShotTimeSeconds);
    }

    @Override
    public List<Spawnable> getSpawnables() {
        return spawnables;
    }

    @Override
    public void init() {

    }

    @Override
    public void onRemove() {

    }

    @Override
    public void update(Entity entity, Level level) {
        double currentTimeSeconds = entity.getLifetimeSeconds();
        if (currentTimeSeconds >= nextShotTimeSeconds) {
            Vec2D position = entity.getPosition();
            for (var spawnable : spawnables) {
                level.addSpawnable(spawnable.copyWithOffset(position.x, position.y));
            }
            nextShotTimeSeconds = nextShotTimeSeconds + shotPeriodSeconds;
        }
    }
}
