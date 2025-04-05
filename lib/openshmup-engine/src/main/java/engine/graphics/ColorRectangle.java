package engine.graphics;

import engine.RGBAValue;
import engine.Vec2D;
import engine.render.Shader;

public class ColorRectangle extends Graphic<ColorRectangle, ColorRectangle.ColorRectanglePrimitive>{
    private ColorRectanglePrimitive primitive;
    public ColorRectangle(int layer, float sizeX, float sizeY, float r, float g, float b, float a, Shader shader){
        super(layer, GraphicType.COLOR_RECTANGLE, shader);
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

    @Override
    public void delete() {
        primitive.delete();
    }

    public class ColorRectanglePrimitive extends Graphic<ColorRectangle, ColorRectanglePrimitive>.Primitive{
        Vec2D position;
        Vec2D size;
        RGBAValue color;

        public Vec2D getPosition() {
            return position.copy();
        }

        public Vec2D getSize() {
            return size.copy();
        }

        public RGBAValue getColor() {
            return color.copy();
        }

        public ColorRectanglePrimitive(float sizeX, float sizeY, float r, float g, float b, float a){
            this.position = new Vec2D(0.0f, 0.0f);
            this.size = new Vec2D(sizeX, sizeY);
            this.color = new RGBAValue(r,g,b,a);
        }
        @Override
        public ColorRectanglePrimitive copy() {
            return new ColorRectanglePrimitive(size.x, size.y, color.r, color.g, color.b, color.a);
        }
    }
}
