package engine.graphics;

import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.assets.Shader;
import engine.assets.Texture;

public class Image2D extends Graphic<Image2D, Image2D.ImagePrimitive> {
    static private Shader defaultShader;
    static public void setDefaultShader(Shader defaultShader){
        Image2D.defaultShader = defaultShader;
    }
    protected Texture texture;
    protected ImagePrimitive primitive;
    public Image2D(Texture texture, int layer, boolean dynamic, float sizeX, float sizeY, Shader shader){
        super(layer, dynamic ? RenderType.DYNAMIC_IMAGE : RenderType.STATIC_IMAGE, shader);
        this.texture = texture;
        this.primitive = new ImagePrimitive(sizeX, sizeY);
    }
    public Image2D(Texture texture, int layer,  boolean dynamic, float sizeX, float sizeY){
        this(texture, layer, dynamic, sizeX, sizeY, defaultShader);
    }

    public Image2D(Image2D image){
        super(image);
        this.texture = image.texture;
        this.primitive = new ImagePrimitive(image.primitive);
    }

    public void setPosition(float imagePositionX, float imagePositionY){
        primitive.imagePosition.x = imagePositionX;
        primitive.imagePosition.y = imagePositionY;
        primitive.tellBatchDataChanged();
    }

    public void setScale(float scaleX, float scaleY){
        primitive.imageSize.x = scaleX;
        primitive.imageSize.y = scaleY;
        primitive.tellBatchDataChanged();
    }

    public void setTexturePosition(float texturePositionX, float texturePositionY){
        primitive.texturePosition.x = texturePositionX;
        primitive.texturePosition.y = texturePositionY;
        primitive.tellBatchDataChanged();
    }
    public void setTextureSize(float textureSizeX, float textureSizeY){
        primitive.textureSize.x = textureSizeX;
        primitive.textureSize.y = textureSizeY;
        primitive.tellBatchDataChanged();
    }

    public void setPrimitiveData(float imagePositionX, float imagePositionY, float imageSizeX,  float imageSizeY, float texturePositionX, float texturePositionY, float textureSizeX, float textureSizeY){
        primitive.imagePosition.x = imagePositionX;
        primitive.imagePosition.y = imagePositionY;
        primitive.imageSize.x = imageSizeX;
        primitive.imageSize.y = imageSizeY;
        primitive.texturePosition.x = texturePositionX;
        primitive.texturePosition.y = texturePositionY;
        primitive.textureSize.x = textureSizeX;
        primitive.textureSize.y = textureSizeY;
        primitive.tellBatchDataChanged();
    }

    public Texture getTexture() {
        return texture;
    }

    @Override
    public Image2D copy() {
        return new Image2D(this);
    }

    public Vec2D getPosition(){
        return new Vec2D(primitive.imagePosition);
    }

    public Vec2D getScale(){
        return new Vec2D(primitive.imageSize);
    }

    public int getPrimitiveCount(){
        return 1;
    }

    public ImagePrimitive getPrimitive(int primitiveIndex){
        return this.primitive;
    }

    @Override
    public void delete() {
        primitive.delete();
    }

    public void setColorCoefs(float r, float g, float b, float a){
        primitive.textureColorCoefs.r = r;
        primitive.textureColorCoefs.g = g;
        primitive.textureColorCoefs.b = b;
        primitive.textureColorCoefs.a = a;
    }

    public class ImagePrimitive extends Graphic<Image2D, ImagePrimitive>.Primitive{

        private final Vec2D imagePosition;

        private final Vec2D imageSize;

        private final Vec2D texturePosition;

        private final Vec2D textureSize;

        private final RGBAValue textureColorCoefs;

        private final RGBAValue addedColor;

        public ImagePrimitive(float sizeX, float sizeY){
            this.imagePosition = new Vec2D(0.0f, 0.0f);
            this.imageSize = new Vec2D(sizeX, sizeY);
            this.texturePosition = new Vec2D(0.0f, 0.0f);
            this.textureSize = new Vec2D(1.0f, 1.0f);
            this.textureColorCoefs = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);
            this.addedColor = new RGBAValue(0.0f, 0.0f, 0.0f, 0.0f);
        }

        public ImagePrimitive(ImagePrimitive imagePrimitive){
            this.imagePosition = new Vec2D(imagePrimitive.imagePosition);
            this.imageSize = new Vec2D(imagePrimitive.imageSize);
            this.texturePosition = new Vec2D(imagePrimitive.texturePosition);
            this.textureSize = new Vec2D(imagePrimitive.textureSize);
            this.textureColorCoefs = new RGBAValue(imagePrimitive.textureColorCoefs);
            this.addedColor = new RGBAValue(imagePrimitive.addedColor);
        }

        public Vec2D getImagePosition() {
            return new Vec2D(imagePosition);
        }

        public Vec2D getImageSize() {
            return new Vec2D(imageSize);
        }

        public Vec2D getTexturePosition() {
            return new Vec2D(texturePosition);
        }

        public Vec2D getTextureSize() {
            return new Vec2D(textureSize);
        }

        public Texture getTexture(){
            return Image2D.this.texture;
        }

        public RGBAValue getTextureColorCoefs(){
            return new RGBAValue(textureColorCoefs);
        }

        public RGBAValue getAddedColor() {
            return addedColor;
        }
    }
}
