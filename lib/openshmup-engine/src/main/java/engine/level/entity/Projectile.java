package engine.level.entity;

import engine.hitbox.Hitbox;
import engine.level.entity.extraComponent.ExtraComponent;
import engine.level.entity.trajectory.Trajectory;
import engine.level.spawnable.Spawnable;
import engine.scene.visual.SceneVisual;
import types.Vec2D;

import java.util.ArrayList;
import java.util.List;

final public class Projectile extends Entity {

    public Projectile(Vec2D startingPos, Vec2D size, float orientationRadians, boolean evil, int entityId, SceneVisual sprite, Trajectory trajectory, Hitbox hitbox, List<Spawnable> deathSpawn, ArrayList<ExtraComponent> extraComponents) {
        super(EntityType.PROJECTILE, startingPos, size, orientationRadians, evil, entityId, sprite, trajectory, hitbox, deathSpawn, extraComponents);
    }

    @Override
    public Entity copy() {
        ArrayList<ExtraComponent> newExtracomponents = new ArrayList<>(extraComponents.size());
        for (ExtraComponent component : extraComponents) {
            newExtracomponents.add(component.copyIfNotReusable());
        }
        return new Projectile(trajectoryReferencePosition, size, orientationRadians, evil, entityId, sprite.copy(), trajectory.copyIfNotReusable(), hitbox.copy(), deathSpawn, newExtracomponents);
    }
}
