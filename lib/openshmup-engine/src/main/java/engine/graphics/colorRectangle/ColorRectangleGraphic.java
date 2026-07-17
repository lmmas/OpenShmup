package engine.graphics.colorRectangle;

import engine.assets.Shader;
import engine.graphics.Graphic;
import engine.graphics.RenderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import types.RGBAValue;
import types.Vec2D;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static engine.Engine.assetManager;

final public class ColorRectangleGraphic extends Graphic<ColorRectangleGraphic.ColorRectangleVertex> {

    final static public Path defaultShader = Paths.get("lib/openshmup-engine/src/main/resources/shaders/colorRectangle.glsl");

    private final ColorRectangleVertex vertex;

    public ColorRectangleGraphic(Vec2D size, Vec2D position, RGBAValue color, Shader shader) {
        super(RenderType.COLOR_RECTANGLE, shader, new ArrayList<>(1));
        this.vertex = new ColorRectangleVertex(size, position, color);
        this.getVertexList().add(vertex);
    }

    public ColorRectangleGraphic(Vec2D size, Vec2D position, RGBAValue color) {
        this(size, position, color, assetManager.getShader(defaultShader));
    }

    public ColorRectangleGraphic(ColorRectangleGraphic colorRectangleGraphic) {
        this(colorRectangleGraphic.vertex.size, colorRectangleGraphic.vertex.position, colorRectangleGraphic.vertex.color, colorRectangleGraphic.shader);
    }

    public Vec2D getPosition() {
        return vertex.position;
    }

    public Vec2D getScale() {
        return vertex.size;
    }

    public RGBAValue getColor() {
        return vertex.color;
    }

    @Override
    public void remove() {
        vertex.setShouldBeRemoved();
    }

    public void setPosition(Vec2D position) {
        vertex.position = position;
        vertex.setDataHasChanged();
    }

    public void setScale(Vec2D scale) {
        vertex.size = scale;
        vertex.setDataHasChanged();
    }

    public void setColor(RGBAValue color) {
        vertex.color = color;
        vertex.setDataHasChanged();
    }

    @Getter @AllArgsConstructor
    public class ColorRectangleVertex extends Graphic<ColorRectangleVertex>.Vertex<ColorRectangleVertex> {

        private Vec2D size;

        private Vec2D position;

        private RGBAValue color;

        @Override
        public ColorRectangleVertex copy() {
            return new ColorRectangleVertex(size, position, color);
        }
    }
}
