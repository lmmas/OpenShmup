package engine.level.entity;

import engine.level.entity.extraComponent.ExtraComponent;
import engine.level.entity.hitbox.Hitbox;
import engine.level.entity.trajectory.Trajectory;
import engine.level.spawnable.Spawnable;
import engine.scene.visual.SceneVisual;

import java.util.ArrayList;
import java.util.List;

final public class Ship extends Entity {

    private int hitPoints;

    public Ship(float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, int entityId, SceneVisual sprite, Trajectory trajectory, Hitbox hitbox, List<Spawnable> deathSpawn, ArrayList<ExtraComponent> extraComponents, int hitPoints) {
        super(EntityType.SHIP, startingPosX, startingPosY, sizeX, sizeY, orientationRadians, evil, entityId, sprite, trajectory, hitbox, deathSpawn, extraComponents);
        this.hitPoints = hitPoints;
    }


    public void takeDamage(int damageValue) {
        hitPoints -= damageValue;
    }

    public boolean isDead() {
        return hitPoints <= 0;
    }

    public int getHP() {
        return hitPoints;
    }

    @Override
    public Entity copy() {
        ArrayList<ExtraComponent> newExtracomponents = new ArrayList<>(extraComponents.size());
        for (ExtraComponent component : extraComponents) {
            newExtracomponents.add(component.copyIfNotReusable());
        }
        return new Ship(trajectoryReferencePosition.x, trajectoryReferencePosition.y, size.x, size.y, orientationRadians, evil, entityId, sprite.copy(), trajectory.copyIfNotReusable(), hitbox.copy(), deathSpawn, newExtracomponents, hitPoints);
    }
}
