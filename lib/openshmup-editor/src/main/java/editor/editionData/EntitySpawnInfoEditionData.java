package editor.editionData;

import editor.attribute.Attribute;
import editor.attribute.IntegerAttribute;
import editor.attribute.Vec2DAttribute;
import engine.types.Vec2D;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class EntitySpawnInfoEditionData implements SpawnableEditionData {

    private IntegerAttribute entityID;

    private Vec2DAttribute position;

    private IntegerAttribute trajectoryID;

    public EntitySpawnInfoEditionData(int entityID, Vec2D position, Integer trajectoryID) {
        this.entityID = new IntegerAttribute("Visual ID", JsonFieldNames.EntitySpawnInfo.id, entityID);
        this.position = new Vec2DAttribute("Spawning position", JsonFieldNames.EntitySpawnInfo.startingPosition, position);
        this.trajectoryID = new IntegerAttribute("Trajectory ID", JsonFieldNames.EntitySpawnInfo.trajectory, trajectoryID);
    }

    @Override public List<Attribute> getAttributes() {
        return List.of(entityID, position, trajectoryID);
    }
}
