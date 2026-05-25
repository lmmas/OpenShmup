package editor.editionData;

import editor.attribute.Attribute;
import editor.attribute.IntegerAttribute;
import editor.attribute.StringAttribute;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class FixedTrajectoryEditionData implements TrajectoryEditionData {

    final private IntegerAttribute id;

    final private StringAttribute trajectoryFunctionX;

    final private StringAttribute trajectoryFunctionY;

    private FixedTrajectoryEditionData() {
        this.id = new IntegerAttribute("Trajectory ID", JsonFieldNames.FixedTrajectory.id);
        this.trajectoryFunctionX = new StringAttribute("Trajectory function X", JsonFieldNames.FixedTrajectory.functionX);
        this.trajectoryFunctionY = new StringAttribute("Trajectory function Y", JsonFieldNames.FixedTrajectory.functionY);
    }

    public FixedTrajectoryEditionData(int id, String trajectoryFunctionX, String trajectoryFunctionY) {
        this();
        this.id.setValue(id);
        this.trajectoryFunctionX.setValue(trajectoryFunctionX);
        this.trajectoryFunctionY.setValue(trajectoryFunctionY);
    }

    public IntegerAttribute getIdAttribute() {
        return id;
    }

    @Override
    public int getId() {
        return id.getValue();
    }

    @Override
    public List<Attribute> getAttributes() {
        return List.of(id, trajectoryFunctionX, trajectoryFunctionY);
    }

    @Override
    public void setToDefault() {
        this.id.setValue(0);
        this.trajectoryFunctionX.setValue("");
        this.trajectoryFunctionY.setValue("");
    }

    public static FixedTrajectoryEditionData DEFAULT() {
        FixedTrajectoryEditionData data = new FixedTrajectoryEditionData();
        data.setToDefault();
        return data;
    }
}
