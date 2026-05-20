package engine.graphics.colorRoundedRectangle;

import engine.assets.Shader;
import engine.graphics.Graphic;
import engine.graphics.RenderType;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static engine.Engine.assetManager;

final public class RoundedColorRectangle extends Graphic<RoundedColorRectangle.ColorRoundedRectangleVertex> {

    final static public Path defaultShader = Paths.get("lib/openshmup-engine/src/main/resources/shaders/colorRoundedRectangle.glsl");

    private final ColorRoundedRectangleVertex vertex;

    public RoundedColorRectangle(Vec2D size, Vec2D position, float roundingRadius, RGBAValue color, Shader shader) {
        super(RenderType.COLOR_ROUNDED_RECTANGLE, shader, new ArrayList<>(1));
        this.vertex = new ColorRoundedRectangleVertex(size, position, roundingRadius, color);
        this.getVertexList().add(vertex);
    }

    public RoundedColorRectangle(Vec2D size, Vec2D position, float roundingRadius, RGBAValue color) {
        this(size, position, roundingRadius, color, assetManager.getShader(defaultShader));
    }

    public RoundedColorRectangle(RoundedColorRectangle roundedColorRectangle) {
        this(roundedColorRectangle.vertex.size, roundedColorRectangle.vertex.position, roundedColorRectangle.vertex.roundingRadius, roundedColorRectangle.vertex.color, roundedColorRectangle.shader);
    }

    public Vec2D getPosition() {
        return vertex.position;
    }

    public Vec2D getScale() {
        return vertex.size;
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

    public void setRoundingRadius(float roundingRadius) {
        vertex.roundingRadius = roundingRadius;
        vertex.setDataHasChanged();
    }

    public void setColor(RGBAValue color) {
        vertex.color = color;
        vertex.setDataHasChanged();
    }

    @Getter @AllArgsConstructor
    public class ColorRoundedRectangleVertex extends Graphic<ColorRoundedRectangleVertex>.Vertex<ColorRoundedRectangleVertex> {

        private Vec2D size;

        private Vec2D position;

        private float roundingRadius;

        private RGBAValue color;

        @Override
        public ColorRoundedRectangleVertex copy() {
            return new ColorRoundedRectangleVertex(size, position, roundingRadius, color);
        }
    }
}

