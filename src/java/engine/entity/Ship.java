package engine.entity;

import engine.entity.hitbox.SimpleHitBox;
import engine.scene.LevelScene;

public class Ship extends Entity{
    private int hitPoints;
    public Ship(LevelScene scene, float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, EntitySprite sprite, Trajectory trajectory, SimpleHitBox hitbox, int hitPoints) {
        super(scene, startingPosX, startingPosY, sizeX, sizeY, orientationRadians, evil, sprite, trajectory, hitbox);
        this.hitPoints = hitPoints;
    }

    public void takeDamage(int damageValue){
        hitPoints -= damageValue;
        if(hitPoints <= 0){
            death();
        }
    }

    public void death(){
        delete();
    }
}
