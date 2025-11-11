package engine.graphics.colorRectangle;

import engine.graphics.Graphic;
import engine.graphics.RenderType;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.assets.Shader;

import static engine.Application.assetManager;

final public class ColorRectangle extends Graphic<ColorRectangle, ColorRectangle.ColorRectanglePrimitive> {
    final static public String defaultShader = "/lib/openshmup-engine/src/main/resources/shaders/colorRectangle.glsl";
    private final ColorRectanglePrimitive primitive;

    public ColorRectangle(float sizeX, float sizeY, float positionX, float positionY, float r, float g, float b, float a, Shader shader){
        super(RenderType.COLOR_RECTANGLE, shader);
        this.primitive = new ColorRectanglePrimitive(sizeX, sizeY, positionX, positionY, r, g, b, a);
    }

    public ColorRectangle(float sizeX, float sizeY, float positionX, float positionY, float r, float g, float b, float a) {
        this(sizeX, sizeY, positionX, positionY, r, g, b, a, assetManager.getShader(defaultShader));
    }

    public ColorRectangle(ColorRectangle colorRectangle){
        this(
                colorRectangle.primitive.size.x, colorRectangle.primitive.size.y, colorRectangle.primitive.position.x, colorRectangle.primitive.position.y,
                colorRectangle.primitive.color.r, colorRectangle.primitive.color.g, colorRectangle.primitive.color.b, colorRectangle.primitive.color.b,
                colorRectangle.shader
        );
    }

    @Override
    public int getPrimitiveCount() {
        return 1;
    }

    @Override
    public ColorRectanglePrimitive getPrimitive(int index) {
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

    public class ColorRectanglePrimitive extends Graphic<ColorRectangle, ColorRectanglePrimitive>.Primitive{

        private final Vec2D size;

        private final Vec2D position;

        private final RGBAValue color;

        public Vec2D getPosition() {
            return new Vec2D(position);
        }

        public Vec2D getSize() {
            return new Vec2D(size);
        }

        public RGBAValue getColor() {
            return new RGBAValue(color);
        }

        public ColorRectanglePrimitive(float sizeX, float sizeY, float positionX, float positionY, float r, float g, float b, float a){
            this.size = new Vec2D(sizeX, sizeY);
            this.position = new Vec2D(positionX, positionY);
            this.color = new RGBAValue(r,g,b,a);
        }
    }
}
