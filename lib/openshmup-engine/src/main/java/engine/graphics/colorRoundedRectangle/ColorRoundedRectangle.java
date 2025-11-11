package engine.graphics.colorRoundedRectangle;

import engine.graphics.Graphic;
import engine.graphics.RenderType;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.assets.Shader;

import static engine.Application.assetManager;

final public class ColorRoundedRectangle extends Graphic<ColorRoundedRectangle, ColorRoundedRectangle.ColorRoundedRectanglePrimitive> {
    final static public String defaultShader = "/lib/openshmup-engine/src/main/resources/shaders/colorRoundedRectangle.glsl";
    private final ColorRoundedRectanglePrimitive primitive;

    public ColorRoundedRectangle(float sizeX, float sizeY, float positionX, float positionY, float roundingRadius, float r, float g, float b, float a, Shader shader){
        super(RenderType.COLOR_ROUNDED_RECTANGLE, shader);
        this.primitive = new ColorRoundedRectanglePrimitive(sizeX, sizeY, positionX, positionY, roundingRadius, r, g, b, a);
    }

    public ColorRoundedRectangle(float sizeX, float sizeY, float positionX, float positionY, float roundingRadius, float r, float g, float b, float a) {
        this(sizeX, sizeY, positionX, positionY, roundingRadius, r, g, b, a, assetManager.getShader(defaultShader));
    }

    public ColorRoundedRectangle(ColorRoundedRectangle colorRoundedRectangle){
        this(
                colorRoundedRectangle.primitive.size.x, colorRoundedRectangle.primitive.size.y,
                colorRoundedRectangle.primitive.position.x, colorRoundedRectangle.primitive.position.y,
                colorRoundedRectangle.primitive.roundingRadius,
                colorRoundedRectangle.primitive.color.r, colorRoundedRectangle.primitive.color.g, colorRoundedRectangle.primitive.color.b, colorRoundedRectangle.primitive.color.a,
                colorRoundedRectangle.shader
        );
    }

    @Override
    public int getPrimitiveCount() {
        return 1;
    }

    @Override
    public ColorRoundedRectanglePrimitive getPrimitive(int index) {
        return primitive;
    }

    public Vec2D getPosition(){
        return new Vec2D(primitive.position);
    }

    public Vec2D getScale(){
        return new Vec2D(primitive.size);
    }

    @Override
    public void remove() {
        primitive.remove();
    }

    public void setPosition(float positionX, float positionY){
        primitive.position.x = positionX;
        primitive.position.y = positionY;
        primitive.dataHasChanged();
    }

    public void setScale(float scaleX, float scaleY){
        primitive.size.x = scaleX;
        primitive.size.y = scaleY;
        primitive.dataHasChanged();
    }

    public class ColorRoundedRectanglePrimitive extends Graphic<ColorRoundedRectangle, ColorRoundedRectanglePrimitive>.Primitive{

        private final Vec2D size;

        private final Vec2D position;

        private float roundingRadius;

        private final RGBAValue color;

        public Vec2D getPosition() {
            return new Vec2D(position);
        }

        public Vec2D getSize() {
            return new Vec2D(size);
        }

        public float getRoundingRadius(){
            return roundingRadius;
        }

        public RGBAValue getColor() {
            return new RGBAValue(color);
        }

        public ColorRoundedRectanglePrimitive(float sizeX, float sizeY, float positionX, float positionY, float roundingRadius,float r, float g, float b, float a){
            this.size = new Vec2D(sizeX, sizeY);
            this.position = new Vec2D(positionX, positionY);
            this.roundingRadius = roundingRadius;
            this.color = new RGBAValue(r,g,b,a);
        }
    }
}

