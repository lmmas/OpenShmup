package engine.entity.extraComponent;

import engine.entity.Entity;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;
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
    public void update(Entity entity, LevelScene scene) {
        double currentTime = entity.getLifetimeSeconds();
        if (scene.getControlState(GameControl.FIRE) && currentTime >= nextShotTimeSeconds) {
            Vec2D position = entity.getPosition();
            for (Spawnable spawnable : spawnables) {
                spawnable.copyWithOffset(position.x, position.y).spawn(scene);
            }
            nextShotTimeSeconds = currentTime + shotPeriodSeconds;
        }
    }
}
