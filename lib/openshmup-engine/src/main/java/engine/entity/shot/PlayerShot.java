package engine.entity.shot;

import engine.entity.Entity;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

public class PlayerShot implements EntityShot{
    private LevelScene scene;
    private final Entity entity;
    private final Spawnable shot;
    private final float shotPeriodSeconds;
    private float nextShotTimeSeconds;

    public PlayerShot(Entity entity, Spawnable shot, float shotPeriodSeconds, float timeBeforeFirstShot) {
        this.entity = entity;
        this.shot = shot;
        this.shotPeriodSeconds = shotPeriodSeconds;
        this.nextShotTimeSeconds = timeBeforeFirstShot;
    }

    @Override
    public void setScene(LevelScene scene) {

    }

    @Override
    public void update(float currentTimeSeconds) {

    }

    @Override
    public EntityShot copyIfNotReusable() {
        return this;
    }
}
