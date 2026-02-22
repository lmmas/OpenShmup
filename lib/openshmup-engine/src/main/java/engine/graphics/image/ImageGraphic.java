package engine.graphics.image;

import engine.assets.Shader;
import engine.assets.Texture;
import engine.graphics.Graphic;
import engine.graphics.RenderType;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static engine.Engine.assetManager;

final public class ImageGraphic extends Graphic<ImageGraphic.ImageVertex> {

    final static public Path defaultShader = Paths.get("lib/openshmup-engine/src/main/resources/shaders/simpleImage2D.glsl");

    final private ImageVertex vertex;

    public ImageGraphic(Texture texture, boolean dynamic,
                        float imageSizeX, float imageSizeY,
                        float imagePositionX, float imagePositionY,
                        float textureSizeX, float textureSizeY,
                        float texturePositionX, float texturePositionY,
                        float textureColorCoefR, float textureColorCoefG, float textureColorCoefB, float textureColorCoefA,
                        float addedColorR, float addedColorG, float addedColorB, float addedColorA,
                        Shader shader) {

        super(dynamic ? RenderType.DYNAMIC_IMAGE : RenderType.STATIC_IMAGE, shader, new ArrayList<>(1));
        this.vertex = new ImageVertex(
            imageSizeX, imageSizeY,
            imagePositionX, imagePositionY,
            texture,
            textureSizeX, textureSizeY,
            texturePositionX, texturePositionY,
            textureColorCoefR, textureColorCoefG, textureColorCoefB, textureColorCoefA,
            addedColorR, addedColorG, addedColorB, addedColorA);
        this.getVertexList().add(vertex);
    }

    public ImageGraphic(
        Texture texture, boolean dynamic,
        float imageSizeX, float imageSizeY,
        float imagePositionX, float imagePositionY,
        float textureSizeX, float textureSizeY,
        float texturePositionX, float texturePositionY,
        float textureColorCoefR, float textureColorCoefG, float textureColorCoefB, float textureColorCoefA,
        float addedColorR, float addedColorG, float addedColorB, float addedColorA
    ) {
        this(
            texture, dynamic,
            imageSizeX, imageSizeY,
            imagePositionX, imagePositionY,
            textureSizeX, textureSizeY,
            texturePositionX, texturePositionY,
            textureColorCoefR, textureColorCoefG, textureColorCoefB, textureColorCoefA,
            addedColorR, addedColorG, addedColorB, addedColorA,
            assetManager.getShader(defaultShader)
        );
    }

    public ImageGraphic(ImageGraphic imageGraphic) {
        super(imageGraphic);
        this.vertex = this.getVertexList().getFirst();
    }

    public void setPosition(float imagePositionX, float imagePositionY) {
        vertex.imagePosition.x = imagePositionX;
        vertex.imagePosition.y = imagePositionY;
        vertex.setDataHasChanged();
    }

    public void setScale(float scaleX, float scaleY) {
        vertex.imageSize.x = scaleX;
        vertex.imageSize.y = scaleY;
        vertex.setDataHasChanged();
    }

    public void setTexturePosition(float texturePositionX, float texturePositionY) {
        vertex.texturePosition.x = texturePositionX;
        vertex.texturePosition.y = texturePositionY;
        vertex.setDataHasChanged();
    }

    public void setTextureSize(float textureSizeX, float textureSizeY) {
        vertex.textureSize.x = textureSizeX;
        vertex.textureSize.y = textureSizeY;
        vertex.setDataHasChanged();
    }

    public void setVertexData(float imagePositionX, float imagePositionY, float imageSizeX, float imageSizeY, float texturePositionX, float texturePositionY, float textureSizeX, float textureSizeY) {
        vertex.imagePosition.x = imagePositionX;
        vertex.imagePosition.y = imagePositionY;
        vertex.imageSize.x = imageSizeX;
        vertex.imageSize.y = imageSizeY;
        vertex.texturePosition.x = texturePositionX;
        vertex.texturePosition.y = texturePositionY;
        vertex.textureSize.x = textureSizeX;
        vertex.textureSize.y = textureSizeY;
        vertex.setDataHasChanged();
    }

    public Vec2D getPosition() {
        return new Vec2D(vertex.imagePosition);
    }

    public Vec2D getScale() {
        return new Vec2D(vertex.imageSize);
    }

    public Texture getTexture() {
        return vertex.texture;
    }

    @Override
    public void remove() {
        vertex.setShouldBeRemoved();
    }

    public void setColorCoefs(float r, float g, float b, float a) {
        vertex.textureColorCoefs.r = r;
        vertex.textureColorCoefs.g = g;
        vertex.textureColorCoefs.b = b;
        vertex.textureColorCoefs.a = a;
        vertex.setDataHasChanged();
    }

    public void setAddedColor(float r, float g, float b, float a) {
        vertex.addedColor.r = r;
        vertex.addedColor.g = g;
        vertex.addedColor.b = b;
        vertex.addedColor.a = a;
        vertex.setDataHasChanged();
    }

    public class ImageVertex extends Graphic<ImageVertex>.Vertex<ImageVertex> {

        private final Vec2D imageSize;

        private final Vec2D imagePosition;
        @Getter
        private final Texture texture;

        private final Vec2D textureSize;

        private final Vec2D texturePosition;

        private final RGBAValue textureColorCoefs;

        private final RGBAValue addedColor;

        public ImageVertex(
            float imageSizeX, float imageSizeY,
            float imagePositionX, float imagePositionY,
            Texture texture,
            float textureSizeX, float textureSizeY,
            float texturePositionX, float texturePositionY,
            float textureColorCoefR, float textureColorCoefG, float textureColorCoefB, float textureColorCoefA,
            float addedColorR, float addedColorG, float addedColorB, float addedColorA
        ) {
            this.imageSize = new Vec2D(imageSizeX, imageSizeY);
            this.imagePosition = new Vec2D(imagePositionX, imagePositionY);
            this.texture = texture;
            this.textureSize = new Vec2D(textureSizeX, textureSizeY);
            this.texturePosition = new Vec2D(texturePositionX, texturePositionY);
            this.textureColorCoefs = new RGBAValue(textureColorCoefR, textureColorCoefG, textureColorCoefB, textureColorCoefA);
            this.addedColor = new RGBAValue(addedColorR, addedColorG, addedColorB, addedColorA);
        }

        public ImageVertex(ImageVertex imageVertex) {
            this.imagePosition = new Vec2D(imageVertex.imagePosition);
            this.imageSize = new Vec2D(imageVertex.imageSize);
            this.texture = imageVertex.texture;
            this.texturePosition = new Vec2D(imageVertex.texturePosition);
            this.textureSize = new Vec2D(imageVertex.textureSize);
            this.textureColorCoefs = new RGBAValue(imageVertex.textureColorCoefs);
            this.addedColor = new RGBAValue(imageVertex.addedColor);
        }

        @Override
        public ImageVertex copy() {
            return new ImageVertex(this);
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

        public RGBAValue getTextureColorCoefs() {
            return new RGBAValue(textureColorCoefs);
        }

        public RGBAValue getAddedColor() {
            return new RGBAValue(addedColor);
        }
    }
}
