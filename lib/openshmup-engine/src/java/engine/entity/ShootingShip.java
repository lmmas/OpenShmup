package engine.entity;

import engine.entity.hitbox.EntitySprite;
import engine.entity.hitbox.SimpleHitBox;
import engine.entity.trajectory.Trajectory;
import engine.scene.LevelScene;
import engine.scene.spawnable.Spawnable;

public class ShootingShip extends Ship{
    Spawnable shot;
    float shootPeriodSeconds;
    public ShootingShip(LevelScene scene, float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, EntitySprite sprite, Trajectory trajectory, SimpleHitBox hitBox, int hitPoints) {
        super(scene, startingPosX, startingPosY, sizeX, sizeY, orientationRadians, evil, sprite, trajectory, hitBox, hitPoints);
    }
}
