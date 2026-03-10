package editor.editionData;

public sealed interface EntityEditionData extends EditionData permits ShipEditionData, ProjectileEditionData {

    int getId();
}
