package editor.editionData;

import editor.attribute.Attribute;
import editor.attribute.IntegerAttribute;
import editor.attribute.Vec2DAttribute;
import engine.types.Vec2D;
import json.JsonFieldNames;
import lombok.Getter;

import java.util.List;

@Getter final public class DisplaySpawnInfoEditionData implements SpawnableEditionData {

    final private IntegerAttribute visualID;

    final private Vec2DAttribute position;

    private DisplaySpawnInfoEditionData() {
        this.visualID = new IntegerAttribute("Visual ID", JsonFieldNames.DisplaySpawnInfo.id);
        this.position = new Vec2DAttribute("Spawning position", JsonFieldNames.DisplaySpawnInfo.position);
    }

    public DisplaySpawnInfoEditionData(int visualID, Vec2D position) {
        this();
        this.visualID.setValue(visualID);
        this.position.setValue(position);
    }

    @Override
    public List<Attribute> getAttributes() {
        return List.of(visualID, position);
    }

    @Override
    public void setToDefault() {
        this.visualID.setValue(0);
        this.position.setValue(Vec2D.ZERO);
    }

    public static DisplaySpawnInfoEditionData DEFAULT() {
        DisplaySpawnInfoEditionData data = new DisplaySpawnInfoEditionData();
        data.setToDefault();
        return data;
    }
}
