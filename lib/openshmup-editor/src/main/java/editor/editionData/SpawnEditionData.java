package editor.editionData;

public sealed interface SpawnEditionData extends EditionData permits DisplaySpawnEditionData, EntitySpawnEditionData {

    static String getType(SpawnEditionData data) {
        return switch (data) {
            case DisplaySpawnEditionData ignored -> "display";
            case EntitySpawnEditionData ignored -> "entity";
        };
    }
}
