package editor.editionData;

import editor.attribute.Attribute;
import editor.attribute.IntegerAttribute;
import editor.attribute.Vec2DAttribute;
import engine.types.Vec2D;
import lombok.Getter;

import java.util.List;

@Getter final public class EntitySpawnEditionData implements SpawnEditionData {

    final private IntegerAttribute entityID;

    final private Vec2DAttribute position;

    final private IntegerAttribute trajectoryID;

    private EntitySpawnEditionData() {
        this.entityID = new IntegerAttribute(Keys.Spawn.EntitySpawn.id);
        this.position = new Vec2DAttribute(Keys.Spawn.EntitySpawn.startingPosition);
        this.trajectoryID = new IntegerAttribute(Keys.Spawn.EntitySpawn.trajectory);
    }

    public EntitySpawnEditionData(int entityID, Vec2D position, Integer trajectoryID) {
        this();
        this.entityID.setValue(entityID);
        this.position.setValue(position);
        this.trajectoryID.setValue(trajectoryID);
    }
    @Override
    public Category getCategory() {
        return Category.SPAWN;
    }
    @Override
    public Type getType() {
        return Types.Spawn.entity;
    }
    @Override
    public List<Attribute> getAttributes() {
        return List.of(entityID, position, trajectoryID);
    }
    @Override
    public void setToDefault() {
        this.entityID.setValue(0);
        this.position.setValue(Vec2D.ZERO);
        this.trajectoryID.setValue(0);
    }

    public static EntitySpawnEditionData DEFAULT() {
        EntitySpawnEditionData data = new EntitySpawnEditionData();
        data.setToDefault();
        return data;
    }
}
