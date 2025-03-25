package engine.entity;

import engine.entity.hitbox.SimpleHitBox;
import engine.entity.shot.EntityShot;
import engine.entity.sprite.EntitySprite;
import engine.entity.trajectory.Trajectory;
import engine.scene.spawnable.Spawnable;

public class Ship extends Entity{
    private int hitPoints;

    public Ship(float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, EntitySprite sprite, Trajectory trajectory, SimpleHitBox hitbox, EntityShot shot, Spawnable deathSpawn, int hitPoints) {
        super(startingPosX, startingPosY, sizeX, sizeY, orientationRadians, evil, sprite, trajectory, hitbox, shot, deathSpawn);
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
    public boolean isShip() {
        return true;
    }
}
