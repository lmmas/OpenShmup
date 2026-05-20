package engine.level.entity;

import engine.hitbox.Hitbox;
import engine.level.entity.extraComponent.ExtraComponent;
import engine.level.entity.trajectory.Trajectory;
import engine.level.spawnable.Spawnable;
import engine.scene.visual.SceneVisual;
import engine.types.Vec2D;

import java.util.ArrayList;
import java.util.List;

final public class Ship extends Entity {

    private int hitPoints;

    public Ship(Vec2D startingPos, Vec2D size, float orientationRadians, boolean evil, int entityId, SceneVisual sprite, Trajectory trajectory, Hitbox hitbox, List<Spawnable> deathSpawn, ArrayList<ExtraComponent> extraComponents, int hitPoints) {
        super(EntityType.SHIP, startingPos, size, orientationRadians, evil, entityId, sprite, trajectory, hitbox, deathSpawn, extraComponents);
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
        return new Ship(trajectoryReferencePosition, size, orientationRadians, evil, entityId, sprite.copy(), trajectory.copyIfNotReusable(), hitbox.copy(), deathSpawn, newExtracomponents, hitPoints);
    }
}
