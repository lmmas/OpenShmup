package editor.editionData;

public sealed interface EntityEditionData extends EditionData permits ShipEditionData, ProjectileEditionData {

    int getId();

    static String getType(EntityEditionData data) {
        return switch (data) {
            case ProjectileEditionData ignored -> "projectile";
            case ShipEditionData ignored -> "ship";
        };
    }
}
