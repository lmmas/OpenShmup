package engine.entity;

import engine.entity.hitbox.Hitbox;
import engine.entity.hitbox.SimpleHitBox;
import engine.entity.shot.EntityShot;
import engine.entity.sprite.EntitySprite;
import engine.entity.trajectory.Trajectory;
import engine.scene.spawnable.Spawnable;

public class Ship extends Entity{
    private int hitPoints;

    public Ship(float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, int entityId, EntitySprite sprite, Trajectory trajectory, Hitbox hitbox, EntityShot shot, Spawnable deathSpawn, int hitPoints) {
        super(startingPosX, startingPosY, sizeX, sizeY, orientationRadians, evil, entityId, sprite, trajectory, hitbox, shot, deathSpawn);
        this.hitPoints = hitPoints;
    }


    public void takeDamage(int damageValue){
        hitPoints -= damageValue;
    }

    public boolean isDead(){
        return hitPoints <= 0;
    }

    public int getHP() {
        return hitPoints;
    }

    @Override
    public Entity copy() {
        return new Ship(trajectoryStartingPosition.x, trajectoryStartingPosition.y, size.x, size.y, orientationRadians, evil, entityId, sprite.copy(), trajectory.copyIfNotReusable(), hitbox.copy(), shot.copyIfNotReusable(), deathSpawn.copy(), hitPoints);
    }

    @Override
    public boolean isShip() {
        return true;
    }
}
