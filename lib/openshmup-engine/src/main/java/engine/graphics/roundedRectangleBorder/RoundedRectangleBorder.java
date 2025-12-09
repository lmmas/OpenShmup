package engine.graphics.roundedRectangleBorder;

import engine.assets.Shader;
import engine.graphics.Graphic;
import engine.graphics.RenderType;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import static engine.Application.assetManager;

final public class RoundedRectangleBorder extends Graphic<RoundedRectangleBorder, RoundedRectangleBorder.RoundedRectangleBorderPrimitive> {
    final static public String defaultShader = "/lib/openshmup-engine/src/main/resources/shaders/roundedRectangleBorder.glsl";

    private final RoundedRectangleBorderPrimitive primitive;

    public RoundedRectangleBorder(float sizeX, float sizeY, float positionX, float positionY, float roundingRadius, float borderWidth, float r, float g, float b, float a, Shader shader) {
        super(RenderType.ROUNDED_RECTANGLE_BORDER, shader);
        this.primitive = new RoundedRectangleBorder.RoundedRectangleBorderPrimitive(sizeX, sizeY, positionX, positionY, roundingRadius, borderWidth, r, g, b, a);
    }

    public RoundedRectangleBorder(float sizeX, float sizeY, float positionX, float positionY, float roundingRadius, float borderWidth, float r, float g, float b, float a) {
        this(sizeX, sizeY, positionX, positionY, roundingRadius, borderWidth, r, g, b, a, assetManager.getShader(defaultShader));
    }

    public RoundedRectangleBorder(RoundedRectangleBorder roundedRectangleBorder) {
        this(
            roundedRectangleBorder.primitive.size.x, roundedRectangleBorder.primitive.size.y,
            roundedRectangleBorder.primitive.position.x, roundedRectangleBorder.primitive.position.y,
            roundedRectangleBorder.primitive.roundingRadius,
            roundedRectangleBorder.primitive.borderWidth,
            roundedRectangleBorder.primitive.color.r, roundedRectangleBorder.primitive.color.g, roundedRectangleBorder.primitive.color.b, roundedRectangleBorder.primitive.color.a,
            roundedRectangleBorder.shader
        );
    }

    @Override
    public int getPrimitiveCount() {
        return 1;
    }

    @Override
    public RoundedRectangleBorderPrimitive getPrimitive(int index) {
        return primitive;
    }

    public Vec2D getPosition() {
        return new Vec2D(primitive.position);
    }

    public Vec2D getScale() {
        return new Vec2D(primitive.size);
    }

    @Override
    public void remove() {
        primitive.remove();
    }

    public void setPosition(float positionX, float positionY) {
        primitive.position.x = positionX;
        primitive.position.y = positionY;
        primitive.dataHasChanged();
    }

    public void setScale(float scaleX, float scaleY) {
        primitive.size.x = scaleX;
        primitive.size.y = scaleY;
        primitive.dataHasChanged();
    }

    public class RoundedRectangleBorderPrimitive extends Graphic<RoundedRectangleBorder, RoundedRectangleBorder.RoundedRectangleBorderPrimitive>.Primitive {

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

        public RoundedRectangleBorderPrimitive(float sizeX, float sizeY, float positionX, float positionY, float roundingRadius, float borderWidth, float r, float g, float b, float a) {
            this.size = new Vec2D(sizeX, sizeY);
            this.position = new Vec2D(positionX, positionY);
            this.roundingRadius = roundingRadius;
            this.borderWidth = borderWidth;
            this.color = new RGBAValue(r, g, b, a);
        }
    }
}


