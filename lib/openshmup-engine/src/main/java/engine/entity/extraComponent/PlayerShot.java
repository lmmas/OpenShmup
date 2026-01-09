package engine.entity.extraComponent;

import engine.entity.Entity;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;
import engine.types.GameControl;
import engine.types.Vec2D;

final public class PlayerShot implements ExtraComponent {

    private final Spawnable spawnable;

    private final double shotPeriodSeconds;

    private double nextShotTimeSeconds;

    public PlayerShot(Spawnable spawnable, double shotPeriodSeconds, double firstShotTimeSeconds) {
        this.spawnable = spawnable;
        this.shotPeriodSeconds = shotPeriodSeconds;
        this.nextShotTimeSeconds = firstShotTimeSeconds;
    }

    @Override
    public ExtraComponent copyIfNotReusable() {
        return new PlayerShot(spawnable, shotPeriodSeconds, nextShotTimeSeconds);
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
        double currentTime = entity.getLifetimeSeconds();
        if (scene.getControlState(GameControl.FIRE) && currentTime >= nextShotTimeSeconds) {
            Vec2D position = entity.getPosition();
            spawnable.copyWithOffset(position.x, position.y).spawn(scene);
            nextShotTimeSeconds = currentTime + shotPeriodSeconds;
        }
    }
}
