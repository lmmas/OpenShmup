package editor.editionData;

public sealed interface TrajectoryEditionData extends EditionData permits FixedTrajectoryEditionData, PlayerControlledTrajectoryEditionData {

    int getId();

    static String getType(TrajectoryEditionData data) {
        return switch (data) {
            case FixedTrajectoryEditionData ignored -> "fixed";
            case PlayerControlledTrajectoryEditionData ignored -> "player";
        };
    }
}
