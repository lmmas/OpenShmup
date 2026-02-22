package engine.graphics.colorRectangle;

import engine.assets.Shader;
import engine.graphics.Graphic;
import engine.graphics.RenderType;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static engine.Engine.assetManager;

final public class ColorRectangleGraphic extends Graphic<ColorRectangleGraphic.ColorRectangleVertex> {

    final static public Path defaultShader = Paths.get("lib/openshmup-engine/src/main/resources/shaders/colorRectangle.glsl");

    private final ColorRectangleVertex vertex;

    public ColorRectangleGraphic(float sizeX, float sizeY, float positionX, float positionY, float r, float g, float b, float a, Shader shader) {
        super(RenderType.COLOR_RECTANGLE, shader, new ArrayList<>(1));
        this.vertex = new ColorRectangleVertex(sizeX, sizeY, positionX, positionY, r, g, b, a);
        this.getVertexList().add(vertex);
    }

    public ColorRectangleGraphic(float sizeX, float sizeY, float positionX, float positionY, float r, float g, float b, float a) {
        this(sizeX, sizeY, positionX, positionY, r, g, b, a, assetManager.getShader(defaultShader));
    }

    public ColorRectangleGraphic(ColorRectangleGraphic colorRectangleGraphic) {
        this(
            colorRectangleGraphic.vertex.size.x, colorRectangleGraphic.vertex.size.y,
            colorRectangleGraphic.vertex.position.x, colorRectangleGraphic.vertex.position.y,
            colorRectangleGraphic.vertex.color.r, colorRectangleGraphic.vertex.color.g, colorRectangleGraphic.vertex.color.b, colorRectangleGraphic.vertex.color.a,
            colorRectangleGraphic.shader
        );
    }

    public Vec2D getPosition() {
        return new Vec2D(vertex.position);
    }

    public Vec2D getScale() {
        return new Vec2D(vertex.size);
    }

    public RGBAValue getColor() {
        return new RGBAValue(vertex.color);
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

    public void setColor(float r, float g, float b, float a) {
        vertex.color.r = r;
        vertex.color.g = g;
        vertex.color.b = b;
        vertex.color.a = a;
        vertex.setDataHasChanged();
    }

    public class ColorRectangleVertex extends Graphic<ColorRectangleVertex>.Vertex<ColorRectangleVertex> {

        private final Vec2D size;

        private final Vec2D position;

        private final RGBAValue color;

        @Override
        public ColorRectangleVertex copy() {
            return new ColorRectangleVertex(size.x, size.y, position.x, position.y, color.r, color.g, color.b, color.a);
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

        public ColorRectangleVertex(float sizeX, float sizeY, float positionX, float positionY, float r, float g, float b, float a) {
            this.size = new Vec2D(sizeX, sizeY);
            this.position = new Vec2D(positionX, positionY);
            this.color = new RGBAValue(r, g, b, a);
        }
    }
}
