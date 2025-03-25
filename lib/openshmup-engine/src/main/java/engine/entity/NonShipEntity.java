package engine.entity;

import engine.entity.hitbox.SimpleHitBox;
import engine.entity.shot.EntityShot;
import engine.entity.sprite.EntitySprite;
import engine.entity.trajectory.Trajectory;
import engine.scene.spawnable.Spawnable;

public class NonShipEntity extends Entity {

    public NonShipEntity(float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, EntitySprite sprite, Trajectory trajectory, SimpleHitBox hitbox, EntityShot shot, Spawnable deathSpawn) {
        super(startingPosX, startingPosY, sizeX, sizeY, orientationRadians, evil, sprite, trajectory, hitbox, shot, deathSpawn);
    }

    @Override
    public boolean isShip() {
        return false;
    }
}
