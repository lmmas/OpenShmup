package editor.editionData;

import editor.attribute.BooleanAttribute;
import editor.attribute.IntegerAttribute;
import editor.attribute.Vec2DAttribute;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class ShipEditionData implements EditionData, EntityEditionData {

    private IntegerAttribute idAttribute;

    private BooleanAttribute evil;

    private Vec2DAttribute size;

    private IntegerAttribute spriteId;

    private HitboxEditionData hitbox;

    private IntegerAttribute trajectoryId;

    private List<SpawnableEditionData> deathspawn;

    private List<ExtraComponentEditionData> extraComponents;

    private IntegerAttribute hp;

    public ShipEditionData(int id, boolean evil, float sizeX, float sizeY, int spriteId, HitboxEditionData hitbox, Integer trajectoryId, List<SpawnableEditionData> deathspawn, List<ExtraComponentEditionData> extraComponents, int hp) {
        this.idAttribute = new IntegerAttribute("Entity ID", JsonFieldNames.Projectile.id, id);
        this.evil = new BooleanAttribute("evil", JsonFieldNames.Projectile.evil, evil);
        this.size = new Vec2DAttribute("Size", JsonFieldNames.Projectile.size, sizeX, sizeY);
        this.spriteId = new IntegerAttribute("Sprite visual ID", JsonFieldNames.Projectile.spriteVisualId, spriteId);
        this.hitbox = hitbox;
        this.trajectoryId = new IntegerAttribute("Trajectory ID", JsonFieldNames.Projectile.defaultTrajectoryId, trajectoryId);
        this.deathspawn = deathspawn;
        this.extraComponents = extraComponents;
        this.hp = new IntegerAttribute("HP", JsonFieldNames.Ship.hp, hp);
    }

    @Override public int getId() {
        return idAttribute.getValue();
    }
}
