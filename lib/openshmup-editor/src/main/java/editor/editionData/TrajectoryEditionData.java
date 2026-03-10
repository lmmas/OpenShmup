package editor.editionData;

public sealed interface TrajectoryEditionData extends EditionData permits FixedTrajectoryEditionData, PlayerControlledTrajectoryEditionData {
}
