package editor.editionData;

import editor.attribute.*;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class ShipEditionData implements EntityEditionData {

    private IntegerAttribute idAttribute;

    private BooleanAttribute evil;

    private Vec2DAttribute size;

    private IntegerAttribute spriteId;

    private EditionDataAttribute<HitboxEditionData> hitbox;

    private IntegerAttribute trajectoryId;

    private ListAttribute<SpawnableEditionData> deathspawn;

    private ListAttribute<ExtraComponentEditionData> extraComponents;

    private IntegerAttribute hp;

    public ShipEditionData(int id, boolean evil, float sizeX, float sizeY, int spriteId, HitboxEditionData hitbox, Integer trajectoryId, List<SpawnableEditionData> deathspawn, List<ExtraComponentEditionData> extraComponents, int hp) {
        this.idAttribute = new IntegerAttribute("Entity ID", JsonFieldNames.Ship.id, id);
        this.evil = new BooleanAttribute("evil", JsonFieldNames.Ship.evil, evil);
        this.size = new Vec2DAttribute("Size", JsonFieldNames.Ship.size, sizeX, sizeY);
        this.spriteId = new IntegerAttribute("Sprite visual ID", JsonFieldNames.Ship.spriteVisualId, spriteId);
        this.hitbox = new EditionDataAttribute<>("hitbox", JsonFieldNames.Ship.hitbox, hitbox);
        this.trajectoryId = new IntegerAttribute("Trajectory ID", JsonFieldNames.Ship.defaultTrajectoryId, trajectoryId);
        this.deathspawn = new ListAttribute<>("Death spawn", JsonFieldNames.Ship.deathSpawn, deathspawn);
        this.extraComponents = new ListAttribute<>("Extra Components", "", extraComponents);
        this.hp = new IntegerAttribute("HP", JsonFieldNames.Ship.hp, hp);
    }

    @Override public int getId() {
        return idAttribute.getValue();
    }

    @Override public List<Attribute> getAttributes() {
        return List.of(idAttribute, evil, size, spriteId, hitbox, trajectoryId, deathspawn, extraComponents);
    }
}
