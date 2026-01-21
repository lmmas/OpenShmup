package engine.level.entity.extraComponent;

import engine.level.Level;
import engine.level.entity.Entity;
import engine.level.spawnable.Spawnable;
import engine.types.GameControl;
import engine.types.Vec2D;

import java.util.List;

final public class PlayerShot implements ExtraComponent {

    private final List<Spawnable> spawnables;

    private final double shotPeriodSeconds;

    private double nextShotTimeSeconds;

    public PlayerShot(List<Spawnable> spawnables, double shotPeriodSeconds, double firstShotTimeSeconds) {
        this.spawnables = spawnables;
        this.shotPeriodSeconds = shotPeriodSeconds;
        this.nextShotTimeSeconds = firstShotTimeSeconds;
    }

    @Override
    public ExtraComponent copyIfNotReusable() {
        return new PlayerShot(spawnables, shotPeriodSeconds, nextShotTimeSeconds);
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
        double currentTime = entity.getLifetimeSeconds();
        if (level.getControlState(GameControl.FIRE) && currentTime >= nextShotTimeSeconds) {
            Vec2D position = entity.getPosition();
            for (Spawnable spawnable : spawnables) {
                level.addSpawnable(spawnable.copyWithOffset(position.x, position.y));
            }
            nextShotTimeSeconds = currentTime + shotPeriodSeconds;
        }
    }
}
