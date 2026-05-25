package editor.editionData;

import editor.attribute.Attribute;
import editor.attribute.IntegerAttribute;
import editor.attribute.Vec2DAttribute;
import engine.types.Vec2D;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class EntitySpawnInfoEditionData implements SpawnableEditionData {

    final private IntegerAttribute entityID;

    final private Vec2DAttribute position;

    final private IntegerAttribute trajectoryID;

    private EntitySpawnInfoEditionData() {
        this.entityID = new IntegerAttribute("Visual ID", JsonFieldNames.EntitySpawnInfo.id);
        this.position = new Vec2DAttribute("Spawning position", JsonFieldNames.EntitySpawnInfo.startingPosition);
        this.trajectoryID = new IntegerAttribute("Trajectory ID", JsonFieldNames.EntitySpawnInfo.trajectory);
    }

    public EntitySpawnInfoEditionData(int entityID, Vec2D position, Integer trajectoryID) {
        this();
        this.entityID.setValue(entityID);
        this.position.setValue(position);
        this.trajectoryID.setValue(trajectoryID);
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

    public static EntitySpawnInfoEditionData DEFAULT() {
        EntitySpawnInfoEditionData data = new EntitySpawnInfoEditionData();
        data.setToDefault();
        return data;
    }
}
