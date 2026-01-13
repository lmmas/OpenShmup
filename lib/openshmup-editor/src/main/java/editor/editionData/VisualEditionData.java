package editor.editionData;

import editor.attribute.IntegerAttribute;
import editor.attribute.Vec2DAttribute;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
abstract public class VisualEditionData {

    private IntegerAttribute id;

    private IntegerAttribute layer;

    private Vec2DAttribute size;

    public VisualEditionData(int id, int layer, float sizeX, float sizeY) {
        this.id = new IntegerAttribute("id", id);
        this.layer = new IntegerAttribute("Scene layer", layer);
        this.size = new Vec2DAttribute("Size", sizeX, sizeY);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VisualEditionData that)) return false;
        return Objects.equals(id.getValue(), that.id.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
