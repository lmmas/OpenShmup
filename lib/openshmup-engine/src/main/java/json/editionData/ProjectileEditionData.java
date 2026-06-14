package json.editionData;

import engine.types.Vec2D;
import json.attribute.*;
import lombok.Getter;

import java.util.List;

@Getter final public class ProjectileEditionData implements EntityEditionData {

    final private IntegerAttribute idAttribute;

    final private BooleanAttribute evil;

    final private Vec2DAttribute size;

    final private IntegerAttribute spriteId;

    final private EditionDataAttribute<HitboxEditionData> hitbox;

    final private IntegerAttribute trajectoryId;

    final private ListAttribute<SpawnEditionData> deathspawn;

    final private ListAttribute<ShotEditionData> shots;

    private ProjectileEditionData() {
        this.idAttribute = new IntegerAttribute(Keys.Entity.Projectile.id);
        this.evil = new BooleanAttribute(Keys.Entity.Projectile.evil);
        this.size = new Vec2DAttribute(Keys.Entity.Projectile.size);
        this.spriteId = new IntegerAttribute(Keys.Entity.Projectile.spriteVisualId);
        this.hitbox = new EditionDataAttribute<>(Keys.Entity.Projectile.hitbox);
        this.trajectoryId = new IntegerAttribute(Keys.Entity.Projectile.defaultTrajectoryId);
        this.deathspawn = new ListAttribute<>(Keys.Entity.Projectile.deathSpawn);
        this.shots = new ListAttribute<>(Keys.Entity.Projectile.shots);
    }

    public ProjectileEditionData(int id, boolean evil, Vec2D size, int spriteId, HitboxEditionData hitbox, Integer trajectoryId, List<SpawnEditionData> deathspawn, List<ShotEditionData> shots) {
        this();
        this.idAttribute.setValue(id);
        this.evil.setValue(evil);
        this.size.setValue(size);
        this.spriteId.setValue(spriteId);
        this.hitbox.setData(hitbox);
        this.trajectoryId.setValue(trajectoryId);
        this.deathspawn.setDataList(deathspawn);
        this.shots.setDataList(shots);
    }
    @Override
    public int getId() {
        return idAttribute.getValue();
    }
    @Override
    public Category getCategory() {
        return Category.ENTITY;
    }
    @Override
    public Type getType() {
        return Types.Entity.projectile;
    }
    @Override
    public List<Attribute> getAttributes() {
        return List.of(idAttribute, evil, size, spriteId, hitbox, trajectoryId, shots, deathspawn);
    }
    @Override
    public void setToDefault() {
        this.idAttribute.setValue(0);
        this.evil.setValue(true);
        this.size.setValue(Vec2D.ZERO);
        this.spriteId.setValue(0);
        this.hitbox.setData(RectangleHitboxEditionData.DEFAULT());
        this.trajectoryId.setValue(0);
        this.deathspawn.setDataList(List.of());
        this.shots.setDataList(List.of());
    }

    public static ProjectileEditionData DEFAULT() {
        ProjectileEditionData data = new ProjectileEditionData();
        data.setToDefault();
        return data;
    }
}
