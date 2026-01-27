package editor.editionData;

import editor.attribute.IntegerAttribute;
import editor.attribute.Vec2DAttribute;
import json.JsonFieldNames;
import lombok.Getter;

@Getter final public class DisplaySpawnInfoEditionData implements EditionData, SpawnableEditionData {

    private IntegerAttribute visualID;

    private Vec2DAttribute position;

    public DisplaySpawnInfoEditionData(int visualID, float positionX, float positionY) {
        this.visualID = new IntegerAttribute("Visual ID", JsonFieldNames.DisplaySpawnInfo.id, visualID);
        this.position = new Vec2DAttribute("Spawning position", JsonFieldNames.DisplaySpawnInfo.position, positionX, positionY);
    }
}
