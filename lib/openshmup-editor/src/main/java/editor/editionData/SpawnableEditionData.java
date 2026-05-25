package editor.editionData;

public sealed interface SpawnableEditionData extends EditionData permits DisplaySpawnInfoEditionData, EntitySpawnInfoEditionData {

    static String getType(SpawnableEditionData data) {
        return switch (data) {
            case DisplaySpawnInfoEditionData ignored -> "display";
            case EntitySpawnInfoEditionData ignored -> "entity";
        };
    }
}
