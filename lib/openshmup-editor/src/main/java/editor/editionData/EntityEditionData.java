package editor.editionData;

public sealed interface EntityEditionData permits ShipEditionData, ProjectileEditionData {

    int getId();
}
