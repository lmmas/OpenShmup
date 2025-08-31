package engine.graphics;

import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.assets.Shader;

public class ColorRectangle extends Graphic<ColorRectangle, ColorRectangle.ColorRectanglePrimitive>{
    private final ColorRectanglePrimitive primitive;
    public ColorRectangle(int layer, float sizeX, float sizeY, float r, float g, float b, float a, Shader shader){
        super(layer, RenderType.COLOR_RECTANGLE, shader);
        this.primitive = new ColorRectanglePrimitive(sizeX, sizeY, r, g, b, a);
    }
    @Override
    public ColorRectangle copy() {
        return new ColorRectangle(renderInfo.layer(), primitive.size.x, primitive.size.y, primitive.color.r, primitive.color.g, primitive.color.b, primitive.color.a, shader);
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
    public void delete() {
        primitive.delete();
    }

    public void setPosition(float positionX, float positionY){
        primitive.position.x = positionX;
        primitive.position.y = positionY;
        primitive.tellBatchDataChanged();
    }

    public void setScale(float scaleX, float scaleY){
        primitive.size.x = scaleX;
        primitive.size.y = scaleY;
        primitive.tellBatchDataChanged();
    }

    public class ColorRectanglePrimitive extends Graphic<ColorRectangle, ColorRectanglePrimitive>.Primitive{

        private final Vec2D position;

        private final Vec2D size;

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

        public ColorRectanglePrimitive(float sizeX, float sizeY, float r, float g, float b, float a){
            this.position = new Vec2D(0.0f, 0.0f);
            this.size = new Vec2D(sizeX, sizeY);
            this.color = new RGBAValue(r,g,b,a);
        }
    }
}
