package editor.objectAttributes.visual;

import editor.attribute.IntegerAttribute;
import editor.attribute.Vec2DAttribute;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
abstract public class VisualAttributes {

    private IntegerAttribute id;

    private IntegerAttribute layer;

    private Vec2DAttribute size;

    public VisualAttributes(int id, int layer, float sizeX, float sizeY) {
        this.id = new IntegerAttribute("id", id);
        this.layer = new IntegerAttribute("Scene layer", layer);
        this.size = new Vec2DAttribute("Size", sizeX, sizeY);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof VisualAttributes that)) return false;
        return Objects.equals(id.getValue(), that.id.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
