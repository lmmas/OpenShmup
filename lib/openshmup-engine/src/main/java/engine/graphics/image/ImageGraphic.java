package engine.graphics.image;

import engine.assets.Shader;
import engine.assets.Texture;
import engine.graphics.Graphic;
import engine.graphics.RenderType;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static engine.Engine.assetManager;

final public class ImageGraphic extends Graphic<ImageGraphic.ImageVertex> {

    final static public Path defaultShader = Paths.get("lib/openshmup-engine/src/main/resources/shaders/simpleImage2D.glsl");

    final private ImageVertex vertex;

    public ImageGraphic(Texture texture, boolean dynamic, Vec2D imageSize, Vec2D imagePosition, Vec2D textureSize, Vec2D texturePosition, RGBAValue textureColorCoefs, RGBAValue addedColor, Shader shader) {
        super(dynamic ? RenderType.DYNAMIC_IMAGE : RenderType.STATIC_IMAGE, shader, new ArrayList<>(1));
        this.vertex = new ImageVertex(texture, imageSize, imagePosition, textureSize, texturePosition, textureColorCoefs, addedColor);
        this.getVertexList().add(vertex);
    }

    public ImageGraphic(Texture texture, boolean dynamic, Vec2D imageSize, Vec2D imagePosition, Vec2D textureSize, Vec2D texturePosition, RGBAValue textureColorCoefs, RGBAValue addedColor) {
        this(texture, dynamic, imageSize, imagePosition, textureSize, texturePosition, textureColorCoefs, addedColor, assetManager.getShader(defaultShader));
    }

    public ImageGraphic(ImageGraphic imageGraphic) {
        super(imageGraphic);
        this.vertex = this.getVertexList().getFirst();
    }

    public void setPosition(Vec2D position) {
        vertex.imagePosition = position;
        vertex.setDataHasChanged();
    }

    public void setScale(Vec2D scale) {
        vertex.imageSize = scale;
        vertex.setDataHasChanged();
    }

    public void setTexturePosition(Vec2D texturePosition) {
        vertex.texturePosition = texturePosition;
        vertex.setDataHasChanged();
    }

    public void setTextureSize(Vec2D textureSize) {
        vertex.textureSize = textureSize;
        vertex.setDataHasChanged();
    }

    public Vec2D getPosition() {
        return vertex.imagePosition;
    }

    public Vec2D getScale() {
        return vertex.imageSize;
    }

    public Texture getTexture() {
        return vertex.texture;
    }

    @Override
    public void remove() {
        vertex.setShouldBeRemoved();
    }

    public void setColorCoefs(RGBAValue colorCoefs) {
        vertex.textureColorCoefs = colorCoefs;
        vertex.setDataHasChanged();
    }

    public void setAddedColor(RGBAValue addedColor) {
        vertex.addedColor = addedColor;
        vertex.setDataHasChanged();
    }

    @Getter @AllArgsConstructor
    public class ImageVertex extends Graphic<ImageVertex>.Vertex<ImageVertex> {

        private final Texture texture;

        private Vec2D imageSize;

        private Vec2D imagePosition;

        private Vec2D textureSize;

        private Vec2D texturePosition;

        private RGBAValue textureColorCoefs;

        private RGBAValue addedColor;

        public ImageVertex(ImageVertex imageVertex) {
            this.imagePosition = imageVertex.imagePosition;
            this.imageSize = imageVertex.imageSize;
            this.texture = imageVertex.texture;
            this.texturePosition = imageVertex.texturePosition;
            this.textureSize = imageVertex.textureSize;
            this.textureColorCoefs = imageVertex.textureColorCoefs;
            this.addedColor = imageVertex.addedColor;
        }

        @Override
        public ImageVertex copy() {
            return new ImageVertex(this);
        }
    }
}
