package engine.graphics.image;

import engine.Engine;
import engine.GlobalVars;
import engine.assets.Shader;
import engine.assets.Texture;
import engine.graphics.RenderType;
import engine.graphics.Renderer;
import types.RGBAValue;
import types.Vec2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static engine.graphics.VBOAttributeInfo.*;
import static org.lwjgl.opengl.GL33.*;

final public class ImageRenderer extends Renderer<ImageGraphic, ImageGraphic.ImageVertex> {

    protected Batch createBatchFromGraphic(ImageGraphic graphic) {
        return new ImageBatch(graphic.getShader(), graphic.getTexture());
    }

    public ImageRenderer(RenderType type) {
        super(type, type == RenderType.DYNAMIC_IMAGE ? GL_STREAM_DRAW : GL_STATIC_DRAW, List.of(VEC2, VEC2, VEC2, VEC2, INT, VEC4, VEC4));
        assert type == RenderType.STATIC_IMAGE || type == RenderType.DYNAMIC_IMAGE : "incorrect render type for Image2D renderer";
    }

    protected class ImageBatch extends Renderer<ImageGraphic, ImageGraphic.ImageVertex>.Batch {

        protected ArrayList<Texture> textures;

        protected ArrayList<Integer> textureIndices;

        public ImageBatch(Shader shader, Texture texture) {
            super(shader);
            this.textures = new ArrayList<>(GlobalVars.MAX_TEXTURE_SLOTS);
            this.textureIndices = new ArrayList<>();
            this.textures.add(texture);
        }

        public void draw() {
            shader.use();
            shader.uploadUniform("u_NativeResolution", new int[]{Engine.getNativeWidth(), Engine.getNativeHeight()});
            for (int i = 0; i < textures.size(); i++) {
                textures.get(i).bind(i);
            }
            int[] array = new int[32];
            Arrays.setAll(array, i -> i);
            shader.uploadUniformIntArray("TEX_SAMPLER", array);
            shader.validate();
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);
            glEnableVertexAttribArray(3);
            glEnableVertexAttribArray(4);
            glEnableVertexAttribArray(5);
            glEnableVertexAttribArray(6);
            glDrawArrays(GL_POINTS, 0, vertices.size());
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glDisableVertexAttribArray(3);
            glDisableVertexAttribArray(4);
            glDisableVertexAttribArray(5);
            glDisableVertexAttribArray(6);
        }

        @Override
        protected boolean canReceiveVertexFrom(ImageGraphic graphic) {
            if (vertices.size() + 1 > batchSize)
                return false;
            return graphic.getShader() == this.shader && (textures.contains(graphic.getTexture()) || textures.size() < GlobalVars.MAX_TEXTURE_SLOTS);
        }

        @Override
        public void sendToBuffer(int vertexIndex){
            ImageGraphic.ImageVertex image = vertices.get(vertexIndex);
            Vec2D imagePosition = image.getImagePosition();
            Vec2D imageSize = image.getImageSize();
            Vec2D texturePosition = image.getTexturePosition();
            Vec2D textureSize = image.getTextureSize();
            RGBAValue colorCoefs = image.getTextureColorCoefs();
            RGBAValue addedColor = image.getAddedColor();

            dataBuffer.putFloat(imageSize.x);
            dataBuffer.putFloat(imageSize.y);
            dataBuffer.putFloat(imagePosition.x);
            dataBuffer.putFloat(imagePosition.y);
            dataBuffer.putFloat(textureSize.x);
            dataBuffer.putFloat(textureSize.y);
            dataBuffer.putFloat(texturePosition.x);
            dataBuffer.putFloat(texturePosition.y);
            dataBuffer.putInt(textureIndices.get(vertexIndex));
            dataBuffer.putFloat(colorCoefs.r);
            dataBuffer.putFloat(colorCoefs.g);
            dataBuffer.putFloat(colorCoefs.b);
            dataBuffer.putFloat(colorCoefs.a);
            dataBuffer.putFloat(addedColor.r);
            dataBuffer.putFloat(addedColor.g);
            dataBuffer.putFloat(addedColor.b);
            dataBuffer.putFloat(addedColor.a);
        }

        @Override
        public void addVertex(ImageGraphic.ImageVertex newVertex) {
            assert vertices.size() == textureIndices.size() : "mismatching list sizes between vertices and texture indices";
            super.addVertex(newVertex);
            int textureIndex = textures.indexOf(newVertex.getTexture());
            assert textures.size() < GlobalVars.MAX_TEXTURE_SLOTS || textureIndex != -1 : "invalid vertex texture";
            if (textureIndex != -1) {
                textureIndices.add(textureIndex);
            }
            else {
                textures.add(newVertex.getTexture());
                textureIndices.add(textures.size() - 1);
            }
        }

        @Override
        public void removeVertex(int vertexToRemoveIndex) {
            assert vertices.size() == textureIndices.size() : "mismatching list sizes between vertices and texture indices";
            super.removeVertex(vertexToRemoveIndex);
            textureIndices.remove(vertexToRemoveIndex);
        }
    }
}
