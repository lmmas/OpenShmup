package engine.entity.shot;

import engine.Vec2D;
import engine.entity.Entity;
import engine.scene.GameControl;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

public class PlayerShot implements EntityShot{
    private LevelScene scene;
    private final Spawnable spawnable;
    private final float shotPeriodSeconds;
    private float nextShotTimeSeconds;

    public PlayerShot(Spawnable spawnable, float shotPeriodSeconds, float firstShotTimeSeconds) {
        this.spawnable = spawnable;
        this.shotPeriodSeconds = shotPeriodSeconds;
        this.nextShotTimeSeconds = firstShotTimeSeconds;
    }

    @Override
    public void setScene(LevelScene scene) {
        this.scene = scene;
    }

    @Override
    public void update(Entity entity) {
        float currentTime = entity.getLifetimeSeconds();
        if(scene.getControlState(GameControl.FIRE) && currentTime >= nextShotTimeSeconds){
            Vec2D position = entity.getPosition();
            spawnable.copyWithOffset(position.x, position.y).spawn(scene);
            nextShotTimeSeconds = currentTime + shotPeriodSeconds;
        }
    }

    @Override
    public EntityShot copyIfNotReusable() {
        return this;
    }
}
