package engine.visual;

import engine.render.RenderType;
import engine.render.Shader;
import engine.render.Texture;

abstract public class Image2D extends Visual<Image2D, Image2D.ImagePrimitive> {
    Texture texture;
    public class ImagePrimitive extends Visual<Image2D, Image2D.ImagePrimitive>.Primitive{
        private float imagePositionX = 0.0f;
        public float getImagePositionX() {
            return imagePositionX;
        }

        private float imagePositionY = 0.0f;
        public float getImagePositionY() {
            return imagePositionY;
        }

        private float imageSizeX = 0.0f;
        public float getImageSizeX() {
            return imageSizeX;
        }

        private float imageSizeY = 0.0f;
        public float getImageSizeY() {
            return imageSizeY;
        }

        private float texturePositionX = 0.0f;
        public float getTexturePositionX() {
            return texturePositionX;
        }

        private float texturePositionY = 0.0f;
        public float getTexturePositionY() {
            return texturePositionY;
        }

        private float textureSizeX = 1.0f;
        public float getTextureSizeX() {
            return textureSizeX;
        }

        private float textureSizeY = 1.0f;
        public float getTextureSizeY() {
            return textureSizeY;
        }
        public ImagePrimitive(){

        }
    }

    ImagePrimitive primitive;
    public void setPositionX(float imagePositionX) {
        primitive.imagePositionX = imagePositionX;
        primitive.tellVboDataChanged();
    }
    public void setPositionY(float imagePositionY) {
        primitive.imagePositionY = imagePositionY;
        primitive.tellVboDataChanged();
    }
    public void setPosition(float imagePositionX, float imagePositionY){
        primitive.imagePositionX = imagePositionX;
        primitive.imagePositionY = imagePositionY;
        primitive.tellVboDataChanged();
    }
    public void setSizeX(float imageSizeX) {
        primitive.imageSizeX = imageSizeX;
        primitive.tellVboDataChanged();
    }
    public void setSizeY(float imageSizeY) {
        primitive.imageSizeY = imageSizeY;
        primitive.tellVboDataChanged();
    }
    public void setSize(float imageSizeX, float imageSizeY){
        primitive.imageSizeX = imageSizeX;
        primitive.imageSizeY = imageSizeY;
        primitive.tellVboDataChanged();
    }
    public void setTexturePositionX(float texturePositionX) {
        primitive.texturePositionX = texturePositionX;
        primitive.tellVboDataChanged();
    }
    public void setTexturePositionY(float texturePositionY) {
        primitive.texturePositionY = texturePositionY;
        primitive.tellVboDataChanged();
    }
    public void setTextureSizeX(float textureSizeX) {
        primitive.textureSizeX = textureSizeX;
        primitive.tellVboDataChanged();
    }
    public void setTextureSizeY(float textureSizeY) {
        primitive.textureSizeY = textureSizeY;
        primitive.tellVboDataChanged();
    }

    public void setPrimitiveData(float imagePositionX, float imagePositionY, float imageSizeX,  float imageSizeY, float texturePositionX, float texturePositionY, float textureSizeX, float textureSizeY){
        primitive.imagePositionX = imagePositionX;
        primitive.imagePositionY = imagePositionY;
        primitive.imageSizeX = imageSizeX;
        primitive.imageSizeY = imageSizeY;
        primitive.texturePositionX = texturePositionX;
        primitive.texturePositionY = texturePositionY;
        primitive.textureSizeX = textureSizeX;
        primitive.textureSizeY = textureSizeY;
        primitive.tellVboDataChanged();
    }

    public Image2D(String textureFilepath, int layer, RenderType type, Shader shader){
        super(layer, type, shader);
        this.texture = Texture.loadTexture(textureFilepath);
        this.primitive = new ImagePrimitive();
    }
    public Image2D(String textureFilepath, int layer, RenderType type){
        this(textureFilepath, layer, type, Shader.loadShader("resources/shaders/simpleImage2D.glsl"));
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
}
