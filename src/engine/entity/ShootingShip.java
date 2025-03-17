package engine.entity;

import engine.entity.hitbox.SimpleHitBox;
import engine.scene.LevelScene;

public class ShootingShip extends Ship{
    public ShootingShip(LevelScene scene, float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, EntitySprite sprite, Trajectory trajectory, SimpleHitBox hitBox, int hitPoints) {
        super(scene, startingPosX, startingPosY, sizeX, sizeY, orientationRadians, evil, sprite, trajectory, hitBox, hitPoints);
    }
}
