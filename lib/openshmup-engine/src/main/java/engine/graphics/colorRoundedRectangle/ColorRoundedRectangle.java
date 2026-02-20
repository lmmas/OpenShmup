package engine.graphics.colorRoundedRectangle;

import engine.assets.Shader;
import engine.graphics.Graphic;
import engine.graphics.RenderType;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static engine.Engine.assetManager;

final public class ColorRoundedRectangle extends Graphic<ColorRoundedRectangle.ColorRoundedRectangleVertex> {

    final static public Path defaultShader = Paths.get("lib/openshmup-engine/src/main/resources/shaders/colorRoundedRectangle.glsl");

    private final ColorRoundedRectangleVertex vertex;

    public ColorRoundedRectangle(float sizeX, float sizeY, float positionX, float positionY, float roundingRadius, float r, float g, float b, float a, Shader shader) {
        super(RenderType.COLOR_ROUNDED_RECTANGLE, shader, new ArrayList<>(1));
        this.vertex = new ColorRoundedRectangleVertex(sizeX, sizeY, positionX, positionY, roundingRadius, r, g, b, a);
        this.getVertexList().add(vertex);
    }

    public ColorRoundedRectangle(float sizeX, float sizeY, float positionX, float positionY, float roundingRadius, float r, float g, float b, float a) {
        this(sizeX, sizeY, positionX, positionY, roundingRadius, r, g, b, a, assetManager.getShader(defaultShader));
    }

    public ColorRoundedRectangle(ColorRoundedRectangle colorRoundedRectangle) {
        this(
            colorRoundedRectangle.vertex.size.x, colorRoundedRectangle.vertex.size.y,
            colorRoundedRectangle.vertex.position.x, colorRoundedRectangle.vertex.position.y,
            colorRoundedRectangle.vertex.roundingRadius,
            colorRoundedRectangle.vertex.color.r, colorRoundedRectangle.vertex.color.g, colorRoundedRectangle.vertex.color.b, colorRoundedRectangle.vertex.color.a,
            colorRoundedRectangle.shader
        );
    }

    public Vec2D getPosition() {
        return new Vec2D(vertex.position);
    }

    public Vec2D getScale() {
        return new Vec2D(vertex.size);
    }

    @Override
    public void remove() {
        vertex.setShouldBeRemoved();
    }

    public void setPosition(float positionX, float positionY) {
        vertex.position.x = positionX;
        vertex.position.y = positionY;
        vertex.setDataHasChanged();
    }

    public void setScale(float scaleX, float scaleY) {
        vertex.size.x = scaleX;
        vertex.size.y = scaleY;
        vertex.setDataHasChanged();
    }

    public class ColorRoundedRectangleVertex extends Graphic<ColorRoundedRectangleVertex>.Vertex<ColorRoundedRectangleVertex> {

        private final Vec2D size;

        private final Vec2D position;
        @Getter
        private final float roundingRadius;

        private final RGBAValue color;

        @Override
        public ColorRoundedRectangleVertex copy() {
            return new ColorRoundedRectangleVertex(size.x, size.y, position.x, position.y, roundingRadius, color.r, color.g, color.b, color.a);
        }

        public Vec2D getPosition() {
            return new Vec2D(position);
        }

        public Vec2D getSize() {
            return new Vec2D(size);
        }

        public RGBAValue getColor() {
            return new RGBAValue(color);
        }

        public ColorRoundedRectangleVertex(float sizeX, float sizeY, float positionX, float positionY, float roundingRadius, float r, float g, float b, float a) {
            this.size = new Vec2D(sizeX, sizeY);
            this.position = new Vec2D(positionX, positionY);
            this.roundingRadius = roundingRadius;
            this.color = new RGBAValue(r, g, b, a);
        }
    }
}

