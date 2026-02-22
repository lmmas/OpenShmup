package engine.graphics.roundedRectangleBorder;

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

final public class RoundedRectangleBorder extends Graphic<RoundedRectangleBorder.RoundedRectangleBorderVertex> {

    final static public Path defaultShader = Paths.get("lib/openshmup-engine/src/main/resources/shaders/roundedRectangleBorder.glsl");

    private final RoundedRectangleBorderVertex vertex;

    public RoundedRectangleBorder(float sizeX, float sizeY, float positionX, float positionY, float roundingRadius, float borderWidth, float r, float g, float b, float a, Shader shader) {
        super(RenderType.ROUNDED_RECTANGLE_BORDER, shader, new ArrayList<>(1));
        this.vertex = new RoundedRectangleBorderVertex(sizeX, sizeY, positionX, positionY, roundingRadius, borderWidth, r, g, b, a);
        this.getVertexList().add(vertex);
    }

    public RoundedRectangleBorder(float sizeX, float sizeY, float positionX, float positionY, float roundingRadius, float borderWidth, float r, float g, float b, float a) {
        this(sizeX, sizeY, positionX, positionY, roundingRadius, borderWidth, r, g, b, a, assetManager.getShader(defaultShader));
    }

    public RoundedRectangleBorder(RoundedRectangleBorder roundedRectangleBorder) {
        this(
            roundedRectangleBorder.vertex.size.x, roundedRectangleBorder.vertex.size.y,
            roundedRectangleBorder.vertex.position.x, roundedRectangleBorder.vertex.position.y,
            roundedRectangleBorder.vertex.roundingRadius,
            roundedRectangleBorder.vertex.borderWidth,
            roundedRectangleBorder.vertex.color.r, roundedRectangleBorder.vertex.color.g, roundedRectangleBorder.vertex.color.b, roundedRectangleBorder.vertex.color.a,
            roundedRectangleBorder.shader
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

    public void setColor(float r, float g, float b, float a) {
        vertex.color.r = r;
        vertex.color.g = g;
        vertex.color.b = b;
        vertex.color.a = a;
        vertex.setDataHasChanged();
    }

    public class RoundedRectangleBorderVertex extends Graphic<RoundedRectangleBorderVertex>.Vertex<RoundedRectangleBorderVertex> {

        private final Vec2D size;

        private final Vec2D position;
        @Getter
        private final float roundingRadius;
        @Getter
        private final float borderWidth;

        private final RGBAValue color;

        @Override
        public RoundedRectangleBorderVertex copy() {
            return new RoundedRectangleBorderVertex(size.x, size.y, position.x, position.y, roundingRadius, borderWidth, color.r, color.g, color.b, color.a);
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

        public RoundedRectangleBorderVertex(float sizeX, float sizeY, float positionX, float positionY, float roundingRadius, float borderWidth, float r, float g, float b, float a) {
            this.size = new Vec2D(sizeX, sizeY);
            this.position = new Vec2D(positionX, positionY);
            this.roundingRadius = roundingRadius;
            this.borderWidth = borderWidth;
            this.color = new RGBAValue(r, g, b, a);
        }
    }
}


