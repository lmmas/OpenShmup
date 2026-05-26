package editor.editionData;

import editor.attribute.Attribute;
import editor.attribute.IntegerAttribute;
import editor.attribute.Vec2DAttribute;
import engine.types.Vec2D;
import lombok.Getter;

import java.util.List;

@Getter final public class DisplaySpawnEditionData implements SpawnEditionData {

    final private IntegerAttribute visualID;

    final private Vec2DAttribute position;

    private DisplaySpawnEditionData() {
        this.visualID = new IntegerAttribute(Keys.Spawn.DisplaySpawn.id);
        this.position = new Vec2DAttribute(Keys.Spawn.DisplaySpawn.position);
    }

    public DisplaySpawnEditionData(int visualID, Vec2D position) {
        this();
        this.visualID.setValue(visualID);
        this.position.setValue(position);
    }
    @Override
    public Category getCategory() {
        return Category.SPAWN;
    }
    @Override
    public Type getType() {
        return Types.Spawn.display;
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

    public static DisplaySpawnEditionData DEFAULT() {
        DisplaySpawnEditionData data = new DisplaySpawnEditionData();
        data.setToDefault();
        return data;
    }
}
