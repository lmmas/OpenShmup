package engine.graphics.image;

import engine.graphics.Graphic;
import engine.graphics.RenderType;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.assets.Shader;
import engine.assets.Texture;

import static engine.Application.assetManager;

final public class Image extends Graphic<Image, Image.ImagePrimitive> {
    final static public String defaultShader = "/lib/openshmup-engine/src/main/resources/shaders/simpleImage2D.glsl";

    private Texture texture;
    final private ImagePrimitive primitive;

    public Image(Texture texture, int layer, boolean dynamic,
                 float imageSizeX, float imageSizeY,
                 float imagePositionX, float imagePositionY,
                 float textureSizeX, float textureSizeY,
                 float texturePositionX, float texturePositionY,
                 float textureColorCoefR, float textureColorCoefG, float textureColorCoefB, float textureColorCoefA,
                 float addedColorR, float addedColorG, float addedColorB, float addedColorA,
                 Shader shader) {

        super(layer, dynamic ? RenderType.DYNAMIC_IMAGE : RenderType.STATIC_IMAGE, shader);
        this.texture = texture;
        this.primitive = new ImagePrimitive(
                imageSizeX, imageSizeY,
                imagePositionX, imagePositionY,
                textureSizeX, textureSizeY,
                texturePositionX, texturePositionY,
                textureColorCoefR, textureColorCoefG, textureColorCoefB, textureColorCoefA,
                addedColorR, addedColorG, addedColorB, addedColorA);
    }

    public Image(
            Texture texture, int layer, boolean dynamic,
            float imageSizeX, float imageSizeY,
            float imagePositionX, float imagePositionY,
            float textureSizeX, float textureSizeY,
            float texturePositionX, float texturePositionY,
            float textureColorCoefR, float textureColorCoefG, float textureColorCoefB, float textureColorCoefA,
            float addedColorR, float addedColorG, float addedColorB, float addedColorA
    ) {
        this(
                texture, layer, dynamic,
                imageSizeX, imageSizeY,
                imagePositionX, imagePositionY,
                textureSizeX, textureSizeY,
                texturePositionX, texturePositionY,
                textureColorCoefR, textureColorCoefG, textureColorCoefB, textureColorCoefA,
                addedColorR, addedColorG, addedColorB, addedColorA,
                assetManager.getShader(defaultShader)
        );
    }

    public Image(Image image){
        super(image);
        this.texture = image.texture;
        this.primitive = new ImagePrimitive(image.primitive);
    }

    public void setPosition(float imagePositionX, float imagePositionY){
        primitive.imagePosition.x = imagePositionX;
        primitive.imagePosition.y = imagePositionY;
        primitive.dataHasChanged();
    }

    public void setScale(float scaleX, float scaleY){
        primitive.imageSize.x = scaleX;
        primitive.imageSize.y = scaleY;
        primitive.dataHasChanged();
    }

    public void setTexturePosition(float texturePositionX, float texturePositionY){
        primitive.texturePosition.x = texturePositionX;
        primitive.texturePosition.y = texturePositionY;
        primitive.dataHasChanged();
    }
    public void setTextureSize(float textureSizeX, float textureSizeY){
        primitive.textureSize.x = textureSizeX;
        primitive.textureSize.y = textureSizeY;
        primitive.dataHasChanged();
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
        primitive.dataHasChanged();
    }

    public Texture getTexture() {
        return texture;
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
    public void remove() {
        primitive.remove();
    }

    public void setColorCoefs(float r, float g, float b, float a){
        primitive.textureColorCoefs.r = r;
        primitive.textureColorCoefs.g = g;
        primitive.textureColorCoefs.b = b;
        primitive.textureColorCoefs.a = a;
    }

    public class ImagePrimitive extends Graphic<Image, ImagePrimitive>.Primitive{

        private final Vec2D imageSize;

        private final Vec2D imagePosition;

        private final Vec2D textureSize;

        private final Vec2D texturePosition;

        private final RGBAValue textureColorCoefs;

        private final RGBAValue addedColor;

        public ImagePrimitive(
                float imageSizeX, float imageSizeY,
                float imagePositionX, float imagePositionY,
                float textureSizeX, float textureSizeY,
                float texturePositionX, float texturePositionY,
                float textureColorCoefR, float textureColorCoefG, float textureColorCoefB, float textureColorCoefA,
                float addedColorR, float addedColorG, float addedColorB, float addedColorA
        ){
            this.imageSize = new Vec2D(imageSizeX, imageSizeY);
            this.imagePosition = new Vec2D(imagePositionX, imagePositionY);
            this.textureSize = new Vec2D(textureSizeX, textureSizeY);
            this.texturePosition = new Vec2D(texturePositionX, texturePositionY);
            this.textureColorCoefs = new RGBAValue(textureColorCoefR, textureColorCoefG, textureColorCoefB, textureColorCoefA);
            this.addedColor = new RGBAValue(addedColorR, addedColorG, addedColorB, addedColorA);
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
            return Image.this.texture;
        }

        public RGBAValue getTextureColorCoefs(){
            return new RGBAValue(textureColorCoefs);
        }

        public RGBAValue getAddedColor() {
            return addedColor;
        }
    }
}
