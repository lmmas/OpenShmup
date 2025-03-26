package engine.entity.shot;

import engine.Vec2D;
import engine.entity.Entity;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

public class NonPlayerShot implements EntityShot{
    private LevelScene scene;
    private final Spawnable spawnable;
    private final float shotPeriodSeconds;
    private float nextShotTimeSeconds;

    public NonPlayerShot(Spawnable spawnable, float shotPeriodSeconds, float timeBeforeFirstShot) {
        this.scene = null;
        this.spawnable = spawnable;
        this.shotPeriodSeconds = shotPeriodSeconds;
        this.nextShotTimeSeconds = timeBeforeFirstShot;
    }

    @Override
    public void setScene(LevelScene scene) {
        this.scene = scene;
    }

    @Override
    public void update(Entity entity) {
        float currentTimeSeconds = entity.getLifetimeSeconds();
        if(currentTimeSeconds >= nextShotTimeSeconds) {
            Vec2D position = entity.getPosition();
            spawnable.copyWithOffset(position.x, position.y).spawn(scene);
            nextShotTimeSeconds = nextShotTimeSeconds + shotPeriodSeconds;
        }
    }

    @Override
    public EntityShot copyIfNotReusable() {
        return new NonPlayerShot(spawnable, shotPeriodSeconds, nextShotTimeSeconds);
    }
}
