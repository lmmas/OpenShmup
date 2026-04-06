package editor.editionData;

import editor.attribute.Attribute;
import editor.attribute.FloatAttribute;
import editor.attribute.IntegerAttribute;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class PlayerControlledTrajectoryEditionData implements TrajectoryEditionData {

    private IntegerAttribute id;

    FloatAttribute playerMovementSpeed;

    public PlayerControlledTrajectoryEditionData(int id, float playerMovementSpeed) {
        this.id = new IntegerAttribute("Trajectory ID", JsonFieldNames.FixedTrajectory.id, id);
        this.playerMovementSpeed = new FloatAttribute("Player movement speed (pix/s)", JsonFieldNames.PlayerControlledTrajectory.playerMovementSpeed, playerMovementSpeed);
    }

    public IntegerAttribute getIdAttribute() {
        return id;
    }

    @Override public int getId() {
        return id.getValue();
    }

    @Override public List<Attribute> getAttributes() {
        return List.of(id, playerMovementSpeed);
    }
}
