package editor.editionData;

import editor.attribute.IntegerAttribute;
import editor.attribute.StringAttribute;
import json.JsonFieldNames;
import lombok.Getter;

@Getter final public class FixedTrajectoryEditionData implements EditionData, TrajectoryEditionData {

    private IntegerAttribute id;

    private StringAttribute trajectoryFunctionX;

    private StringAttribute trajectoryFunctionY;

    public FixedTrajectoryEditionData(int id, String trajectoryFunctionX, String trajectoryFunctionY) {
        this.id = new IntegerAttribute("Trajectory ID", JsonFieldNames.FixedTrajectory.id, id);
        this.trajectoryFunctionX = new StringAttribute("Trajectory function X", JsonFieldNames.FixedTrajectory.functionX, trajectoryFunctionX);
        this.trajectoryFunctionY = new StringAttribute("Trajectory function Y", JsonFieldNames.FixedTrajectory.functionY, trajectoryFunctionY);
    }
}
