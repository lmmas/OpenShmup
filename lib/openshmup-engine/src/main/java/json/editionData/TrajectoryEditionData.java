package json.editionData;

public sealed interface TrajectoryEditionData extends EditionData permits FixedTrajectoryEditionData, PlayerControlledTrajectoryEditionData {

    int getId();

}
