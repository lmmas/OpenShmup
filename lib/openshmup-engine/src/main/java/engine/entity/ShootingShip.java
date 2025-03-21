package engine.entity;

import engine.entity.hitbox.EntitySprite;
import engine.entity.hitbox.SimpleHitBox;
import engine.entity.trajectory.Trajectory;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

public class ShootingShip extends Ship{
    protected Spawnable shot;
    protected float shotPeriodSeconds;
    protected float nextShotTimeSeconds;
    public ShootingShip(LevelScene scene, float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, EntitySprite sprite, Trajectory trajectory, SimpleHitBox hitBox,
                        int hitPoints,
                        Spawnable shot, float shotPeriodSeconds, float timeBeforeFirstShotSeconds) {
        super(scene, startingPosX, startingPosY, sizeX, sizeY, orientationRadians, evil, sprite, trajectory, hitBox, hitPoints);
        this.shot = shot;
        this.shotPeriodSeconds = shotPeriodSeconds;
        this.nextShotTimeSeconds = startingTimeSeconds + timeBeforeFirstShotSeconds;
    }

    @Override
    public void update() {
        super.update();
        if(nextShotTimeSeconds < lifetimeSeconds){
            shot.copyWithOffset(position.x, position.y).spawn(scene);
            nextShotTimeSeconds = nextShotTimeSeconds + shotPeriodSeconds;
        }
    }
}
