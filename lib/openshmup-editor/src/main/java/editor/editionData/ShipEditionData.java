package editor.editionData;

import editor.attribute.*;
import engine.types.Vec2D;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class ShipEditionData implements EntityEditionData {

    final private IntegerAttribute idAttribute;

    final private BooleanAttribute evil;

    final private Vec2DAttribute size;

    final private IntegerAttribute spriteId;

    final private EditionDataAttribute<HitboxEditionData> hitbox;

    final private IntegerAttribute trajectoryId;

    final private ListAttribute<SpawnableEditionData> deathspawn;

    final private ListAttribute<ShotEditionData> shots;

    final private IntegerAttribute hp;

    private ShipEditionData() {
        this.idAttribute = new IntegerAttribute("Entity ID", JsonFieldNames.Ship.id);
        this.evil = new BooleanAttribute("evil", JsonFieldNames.Ship.evil);
        this.size = new Vec2DAttribute("Size", JsonFieldNames.Ship.size);
        this.spriteId = new IntegerAttribute("Sprite visual ID", JsonFieldNames.Ship.spriteVisualId);
        this.hitbox = new EditionDataAttribute<>("hitbox", JsonFieldNames.Ship.hitbox);
        this.trajectoryId = new IntegerAttribute("Trajectory ID", JsonFieldNames.Ship.defaultTrajectoryId);
        this.deathspawn = new ListAttribute<>("Death spawn", JsonFieldNames.Ship.deathSpawn);
        this.shots = new ListAttribute<>("Shot", "");
        this.hp = new IntegerAttribute("HP", JsonFieldNames.Ship.hp);
    }

    public ShipEditionData(int id, boolean evil, Vec2D size, int spriteId, HitboxEditionData hitbox, Integer trajectoryId, List<SpawnableEditionData> deathspawn, List<ShotEditionData> shots, int hp) {
        this();
        this.idAttribute.setValue(id);
        this.evil.setValue(evil);
        this.size.setValue(size);
        this.spriteId.setValue(spriteId);
        this.hitbox.setData(hitbox);
        this.trajectoryId.setValue(trajectoryId);
        this.deathspawn.setDataList(deathspawn);
        this.shots.setDataList(shots);
        this.hp.setValue(hp);
    }

    @Override public int getId() {
        return idAttribute.getValue();
    }

    @Override public List<Attribute> getAttributes() {
        return List.of(idAttribute, evil, size, spriteId, hitbox, trajectoryId, deathspawn, shots);
    }

    @Override public void setToDefault() {
        this.idAttribute.setValue(0);
        this.evil.setValue(true);
        this.size.setValue(Vec2D.ZERO);
        this.spriteId.setValue(0);
        this.hitbox.setData(SimpleRectangleHitboxEditionData.DEFAULT());
        this.trajectoryId.setValue(0);
        this.deathspawn.setDataList(List.of());
        this.shots.setDataList(List.of());
        this.hp.setValue(1);
    }

    public static ShipEditionData DEFAULT() {
        ShipEditionData data = new ShipEditionData();
        data.setToDefault();
        return data;
    }
}
