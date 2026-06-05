package json.editionData;

import json.attribute.Attribute;
import json.attribute.FloatAttribute;
import json.attribute.IntegerAttribute;
import lombok.Getter;

import java.util.List;

@Getter final public class PlayerControlledTrajectoryEditionData implements TrajectoryEditionData {

    final private IntegerAttribute id;

    final private FloatAttribute playerMovementSpeed;

    private PlayerControlledTrajectoryEditionData() {
        this.id = new IntegerAttribute(Keys.Trajectory.FixedTrajectory.id);
        this.playerMovementSpeed = new FloatAttribute(Keys.Trajectory.PlayerControlledTrajectory.playerMovementSpeed);
    }

    public PlayerControlledTrajectoryEditionData(int id, float playerMovementSpeed) {
        this();
        this.id.setValue(id);
        this.playerMovementSpeed.setValue(playerMovementSpeed);
    }

    public IntegerAttribute getIdAttribute() {
        return id;
    }

    @Override
    public int getId() {
        return id.getValue();
    }
    @Override
    public Category getCategory() {
        return Category.TRAJECTORY;
    }
    @Override
    public Type getType() {
        return Types.Trajectory.player;
    }
    @Override
    public List<Attribute> getAttributes() {
        return List.of(id, playerMovementSpeed);
    }

    @Override
    public void setToDefault() {
        this.id.setValue(0);
        this.playerMovementSpeed.setValue(100.0f);
    }

    public static PlayerControlledTrajectoryEditionData DEFAULT() {
        PlayerControlledTrajectoryEditionData data = new PlayerControlledTrajectoryEditionData();
        data.setToDefault();
        return data;
    }
}
