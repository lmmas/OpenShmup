package engine.entity;

import engine.entity.extraComponent.ExtraComponent;
import engine.entity.hitbox.Hitbox;
import engine.entity.sprite.EntitySprite;
import engine.entity.trajectory.Trajectory;
import engine.scene.spawnable.Spawnable;

import java.util.ArrayList;

public class NonShipEntity extends Entity {

    public NonShipEntity(float startingPosX, float startingPosY, float sizeX, float sizeY, float orientationRadians, boolean evil, int entityId, EntitySprite sprite, Trajectory trajectory, Hitbox hitbox, Spawnable deathSpawn, ArrayList<ExtraComponent> extraComponents) {
        super(EntityType.PROJECTILE, startingPosX, startingPosY, sizeX, sizeY, orientationRadians, evil, entityId, sprite, trajectory, hitbox, deathSpawn, extraComponents);
    }

    @Override
    public Entity copy() {
        ArrayList<ExtraComponent> newExtracomponents = new ArrayList<>(extraComponents.size());
        for(ExtraComponent component: extraComponents){
            newExtracomponents.add(component.copyIfNotReusable());
        }
        return new NonShipEntity(trajectoryReferencePosition.x, trajectoryReferencePosition.y, size.x , size.y, orientationRadians, evil, entityId, sprite.copy(), trajectory.copyIfNotReusable(), hitbox.copy(), deathSpawn.copy(), newExtracomponents);
    }
}
