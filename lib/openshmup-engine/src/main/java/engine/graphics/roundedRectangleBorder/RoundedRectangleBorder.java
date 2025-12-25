package engine.graphics.roundedRectangleBorder;

import engine.assets.Shader;
import engine.graphics.Graphic;
import engine.graphics.RenderType;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import static engine.Engine.assetManager;

final public class RoundedRectangleBorder extends Graphic<RoundedRectangleBorder, RoundedRectangleBorder.RoundedRectangleBorderVertex> {
    final static public String defaultShader = "/lib/openshmup-engine/src/main/resources/shaders/roundedRectangleBorder.glsl";

    private final RoundedRectangleBorderVertex vertex;

    public RoundedRectangleBorder(float sizeX, float sizeY, float positionX, float positionY, float roundingRadius, float borderWidth, float r, float g, float b, float a, Shader shader) {
        super(RenderType.ROUNDED_RECTANGLE_BORDER, shader);
        this.vertex = new RoundedRectangleBorderVertex(sizeX, sizeY, positionX, positionY, roundingRadius, borderWidth, r, g, b, a);
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

    @Override
    public int getVertexCount() {
        return 1;
    }

    @Override
    public RoundedRectangleBorderVertex getVertex(int index) {
        return vertex;
    }

    public Vec2D getPosition() {
        return new Vec2D(vertex.position);
    }

    public Vec2D getScale() {
        return new Vec2D(vertex.size);
    }

    @Override
    public void remove() {
        vertex.remove();
    }

    public void setPosition(float positionX, float positionY) {
        vertex.position.x = positionX;
        vertex.position.y = positionY;
        vertex.dataHasChanged();
    }

    public void setScale(float scaleX, float scaleY) {
        vertex.size.x = scaleX;
        vertex.size.y = scaleY;
        vertex.dataHasChanged();
    }

    public class RoundedRectangleBorderVertex extends Graphic<RoundedRectangleBorder, RoundedRectangleBorderVertex>.Vertex {

        private final Vec2D size;

        private final Vec2D position;

        private float roundingRadius;

        private float borderWidth;

        private final RGBAValue color;

        public Vec2D getPosition() {
            return new Vec2D(position);
        }

        public Vec2D getSize() {
            return new Vec2D(size);
        }

        public float getRoundingRadius() {
            return roundingRadius;
        }

        public float getBorderWidth() {
            return borderWidth;
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


