package engine.graphics;

import engine.Vec2D;
import engine.render.RenderType;
import engine.render.Shader;
import engine.render.Texture;
import engine.scene.Scene;

abstract public class Image2D extends Graphic<Image2D, Image2D.ImagePrimitive> {
    Texture texture;
    ImagePrimitive primitive;
    public Image2D(String textureFilepath, Scene scene, int layer, RenderType type, Shader shader){
        super(scene, layer, type, shader);
        this.texture = Texture.getTexture(textureFilepath);
        texture.upload();
        this.primitive = new ImagePrimitive();
    }
    public Image2D(String textureFilepath, Scene scene, int layer, RenderType type){
        this(textureFilepath, scene, layer, type, Shader.loadShader("lib/openshmup-engine/src/resources/shaders/simpleImage2D.glsl"));
    }

    public void setPosition(float imagePositionX, float imagePositionY){
        primitive.imagePosition.x = imagePositionX;
        primitive.imagePosition.y = imagePositionY;
        primitive.tellVboDataChanged();
    }
    public void setSize(float imageSizeX, float imageSizeY){
        primitive.imageSize.x = imageSizeX;
        primitive.imageSize.y = imageSizeY;
        primitive.tellVboDataChanged();
    }

    public void setTexturePosition(float texturePositionX, float texturePositionY){
        primitive.texturePosition.x = texturePositionX;
        primitive.texturePosition.y = texturePositionY;
        primitive.tellVboDataChanged();
    }
    public void setTextureSize(float textureSizeX, float textureSizeY){
        primitive.textureSize.x = textureSizeX;
        primitive.textureSize.y = textureSizeY;
        primitive.tellVboDataChanged();
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
        primitive.tellVboDataChanged();
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
        public ImagePrimitive(){
            super();
            this.imagePosition = new Vec2D(0.0f, 0.0f);
            this.imageSize = new Vec2D(0.0f, 0.0f);
            this.texturePosition = new Vec2D(0.0f, 0.0f);
            this.textureSize = new Vec2D(1.0f, 1.0f);
        }

        public Vec2D getImagePosition() {
            return imagePosition;
        }

        public Vec2D getImageSize() {
            return imageSize;
        }

        public Vec2D getTexturePosition() {
            return texturePosition;
        }

        public Vec2D getTextureSize() {
            return textureSize;
        }

        public Texture getTexture(){
            return Image2D.this.texture;
        }
    }
}
