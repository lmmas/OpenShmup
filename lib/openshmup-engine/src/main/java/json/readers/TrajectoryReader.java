package json.readers;

import edition.EditionData;

@FunctionalInterface
public interface TrajectoryReader {

    EditionData fromJson(JsonNodeReader node);

    static EditionData FixedTrajectory(JsonNodeReader node) {
        int id = node.readInt(EditionData.Keys.Trajectory.FixedTrajectory.id);
        String functionXString = node.readString(EditionData.Keys.Trajectory.FixedTrajectory.functionX);
        String functionYString = node.readString(EditionData.Keys.Trajectory.FixedTrajectory.functionY);
        return EditionData.Trajectory.FixedTrajectory(id, functionXString, functionYString);
    }

    static EditionData PlayerControlledTrajectory(JsonNodeReader node) {
        int id = node.readInt(EditionData.Keys.Trajectory.PlayerControlledTrajectory.id);
        float playerMovementSpeed = node.readFloat(EditionData.Keys.Trajectory.PlayerControlledTrajectory.playerMovementSpeed);
        return EditionData.Trajectory.PlayerControlledTrajectory(id, playerMovementSpeed);
    }
}
