package editor.editionData;

import editor.attribute.FloatAttribute;
import editor.attribute.IntegerAttribute;
import json.JsonFieldNames;
import lombok.Getter;

@Getter final public class PlayerControlledTrajectoryEditionData implements EditionData, TrajectoryEditionData {

    private IntegerAttribute id;

    FloatAttribute playerMovementSpeed;

    public PlayerControlledTrajectoryEditionData(int id, float playerMovementSpeed) {
        this.id = new IntegerAttribute("Trajectory ID", JsonFieldNames.FixedTrajectory.id, id);
        this.playerMovementSpeed = new FloatAttribute("Player movement speed (pix/s)", JsonFieldNames.PlayerControlledTrajectory.playerMovementSpeed, playerMovementSpeed);
    }
}
