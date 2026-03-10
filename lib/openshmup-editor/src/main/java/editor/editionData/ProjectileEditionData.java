package editor.editionData;

import editor.attribute.*;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class ProjectileEditionData implements EntityEditionData {

    private IntegerAttribute idAttribute;

    private BooleanAttribute evil;

    private Vec2DAttribute size;

    private IntegerAttribute spriteId;

    private EditionDataAttribute<HitboxEditionData> hitbox;

    private IntegerAttribute trajectoryId;

    private ListAttribute<SpawnableEditionData> deathspawn;

    private ListAttribute<ExtraComponentEditionData> extraComponents;

    public ProjectileEditionData(int id, boolean evil, float sizeX, float sizeY, int spriteId, HitboxEditionData hitbox, Integer trajectoryId, List<SpawnableEditionData> deathspawn, List<ExtraComponentEditionData> extraComponents) {
        this.idAttribute = new IntegerAttribute("Entity ID", JsonFieldNames.Projectile.id, id);
        this.evil = new BooleanAttribute("evil", JsonFieldNames.Projectile.evil, evil);
        this.size = new Vec2DAttribute("Size", JsonFieldNames.Projectile.size, sizeX, sizeY);
        this.spriteId = new IntegerAttribute("Sprite visual ID", JsonFieldNames.Projectile.spriteVisualId, spriteId);
        this.hitbox = new EditionDataAttribute<>("hitbox", JsonFieldNames.Projectile.hitbox, hitbox);
        this.trajectoryId = new IntegerAttribute("Trajectory ID", JsonFieldNames.Projectile.defaultTrajectoryId, trajectoryId);
        this.deathspawn = new ListAttribute<>("Death spawn", JsonFieldNames.Projectile.deathSpawn, deathspawn);
        this.extraComponents = new ListAttribute<>("Extra Components", "", extraComponents);
    }

    @Override
    public int getId() {
        return idAttribute.getValue();
    }

    @Override public List<Attribute> getAttributes() {
        return List.of(idAttribute, evil, size, spriteId, hitbox, trajectoryId, deathspawn, extraComponents);
    }
}
