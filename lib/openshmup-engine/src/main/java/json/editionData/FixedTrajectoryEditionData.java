package json.editionData;

import json.attribute.Attribute;
import json.attribute.IntegerAttribute;
import json.attribute.StringAttribute;
import lombok.Getter;

import java.util.List;

@Getter final public class FixedTrajectoryEditionData implements TrajectoryEditionData {

    final private IntegerAttribute id;

    final private StringAttribute trajectoryFunctionX;

    final private StringAttribute trajectoryFunctionY;

    private FixedTrajectoryEditionData() {
        this.id = new IntegerAttribute(Keys.Trajectory.FixedTrajectory.id);
        this.trajectoryFunctionX = new StringAttribute(Keys.Trajectory.FixedTrajectory.functionX);
        this.trajectoryFunctionY = new StringAttribute(Keys.Trajectory.FixedTrajectory.functionY);
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
    public Category getCategory() {
        return Category.TRAJECTORY;
    }
    @Override
    public Type getType() {
        return Types.Trajectory.fixed;
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
