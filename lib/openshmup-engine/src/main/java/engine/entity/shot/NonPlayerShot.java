package engine.entity.shot;

import engine.Vec2D;
import engine.entity.Entity;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

public class NonPlayerShot implements EntityShot{
    private LevelScene scene;
    private final Entity entity;
    private final Spawnable shot;
    private final float shotPeriodSeconds;
    private float nextShotTimeSeconds;

    public NonPlayerShot(Entity entity, Spawnable shot, float shotPeriodSeconds, float timeBeforeFirstShot) {
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
        if(currentTimeSeconds >= nextShotTimeSeconds) {
            Vec2D position = entity.getPosition();
            shot.copyWithOffset(position.x, position.y).spawn(scene);
            nextShotTimeSeconds = nextShotTimeSeconds + shotPeriodSeconds;
        }
    }

    @Override
    public EntityShot copyIfNotReusable() {
        return null;
    }
}
