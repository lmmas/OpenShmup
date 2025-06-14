package engine.graphics;

import engine.GlobalVars;
import engine.render.RenderInfo;
import engine.types.Vec2D;
import engine.assets.Shader;
import engine.assets.Texture;

abstract public class Image2D extends Graphic<Image2D, Image2D.ImagePrimitive> {
    static private Shader defaultShader;
    static public void setDefaultShader(Shader defaultShader){
        Image2D.defaultShader = defaultShader;
    }
    protected Texture texture;
    protected ImagePrimitive primitive;
    public Image2D(Texture texture, int layer, GraphicType type, float sizeX, float sizeY, Shader shader){
        super(layer, type, shader);
        this.texture = texture;
        this.primitive = new ImagePrimitive(sizeX, sizeY);
    }
    public Image2D(Texture texture, int layer, GraphicType type, float sizeX, float sizeY){
        this(texture, layer, type, sizeX, sizeY, defaultShader);
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
    public void setSize(float imageSizeX, float imageSizeY){
        primitive.imageSize.x = imageSizeX;
        primitive.imageSize.y = imageSizeY;
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

    public void setOrientation(float orientation){
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
    public class ImagePrimitive extends Graphic<Image2D, ImagePrimitive>.Primitive{

        private Vec2D imagePosition;

        private Vec2D imageSize;

        private Vec2D texturePosition;

        private Vec2D textureSize;
        public ImagePrimitive(float sizeX, float sizeY){
            this.imagePosition = new Vec2D(0.0f, 0.0f);
            this.imageSize = new Vec2D(sizeX, sizeY);
            this.texturePosition = new Vec2D(0.0f, 0.0f);
            this.textureSize = new Vec2D(1.0f, 1.0f);
        }
        public ImagePrimitive(float imagePositionX, float imagePositionY, float imageSizeX, float imageSizeY, float texturePositionX, float texturePositionY, float textureSizeX, float textureSizeY){
            this.imagePosition = new Vec2D(imagePositionX, imagePositionY);
            this.imageSize = new Vec2D(imageSizeX, imageSizeY);
            this.texturePosition = new Vec2D(texturePositionX, texturePositionY);
            this.textureSize = new Vec2D(textureSizeX, textureSizeY);
        }

        public ImagePrimitive(ImagePrimitive imagePrimitive){
            this.imagePosition = new Vec2D(imagePrimitive.imagePosition.x, imagePrimitive.imagePosition.y);
            this.imageSize = new Vec2D(imagePrimitive.imageSize.x, imagePrimitive.imageSize.y);
            this.texturePosition = new Vec2D(imagePrimitive.texturePosition.x, imagePrimitive.texturePosition.y);
            this.textureSize = new Vec2D(imagePrimitive.textureSize.x, imagePrimitive.textureSize.y);
        }

        public Vec2D getImagePosition() {
            return imagePosition.copy();
        }

        public Vec2D getImageSize() {
            return imageSize.copy();
        }

        public Vec2D getTexturePosition() {
            return texturePosition.copy();
        }

        public Vec2D getTextureSize() {
            return textureSize.copy();
        }

        public Texture getTexture(){
            return Image2D.this.texture;
        }
    }
}
