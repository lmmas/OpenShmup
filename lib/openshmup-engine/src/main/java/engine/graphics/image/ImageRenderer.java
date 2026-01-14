package engine.graphics.image;

import engine.Engine;
import engine.GlobalVars;
import engine.assets.Shader;
import engine.assets.Texture;
import engine.graphics.RenderType;
import engine.graphics.Renderer;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL33.*;

final public class ImageRenderer extends Renderer<Image, Image.ImageVertex> {

    protected Batch createBatchFromGraphic(Image graphic) {
        return new ImageBatch(graphic.getShader(), graphic.getTexture());
    }

    public ImageRenderer(RenderType type) {
        super(type, type == RenderType.DYNAMIC_IMAGE ? GL_STREAM_DRAW : GL_STATIC_DRAW, 68);
        assert type == RenderType.STATIC_IMAGE || type == RenderType.DYNAMIC_IMAGE : "incorrect render type for Image2D renderer";
        this.batchSize = 100;
    }

    protected class ImageBatch extends Renderer<Image, Image.ImageVertex>.Batch {

        protected ArrayList<Texture> textures;

        protected ArrayList<Integer> textureIndexes;

        final protected ByteBuffer dataBuffer;

        protected static final int vertexAttributeCount = 17;

        public ImageBatch(Shader shader, Texture texture) {
            super(shader);
            this.textures = new ArrayList<>(GlobalVars.MAX_TEXTURE_SLOTS);
            this.textureIndexes = new ArrayList<>();
            this.dataBuffer = BufferUtils.createByteBuffer(Float.BYTES * batchSize * vertexAttributeCount);
            this.textures.add(texture);
            glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
            glBufferData(GL_ARRAY_BUFFER, dataBuffer, drawingType);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
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
        }

        @Override
        protected boolean canReceiveVertexFrom(Image graphic) {
            if (vertices.size() + 1 > batchSize)
                return false;
            return graphic.getShader() == this.shader && (textures.contains(graphic.getTexture()) || textures.size() < GlobalVars.MAX_TEXTURE_SLOTS);
        }

        public void setupVertexAttributes() {
            int quadSizeLength = 2;
            int positionLength = 2;
            int textureSizeLength = 2;
            int texturePositionLength = 2;
            int textureIndexLength = 1;
            int colorCoefsLength = 4;
            int addedColorLength = 4;
            glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
            glVertexAttribPointer(0, quadSizeLength, GL_FLOAT, false, vboStrideBytes, 0);
            glVertexAttribPointer(1, positionLength, GL_FLOAT, false, vboStrideBytes, quadSizeLength * Float.BYTES);
            glVertexAttribPointer(2, textureSizeLength, GL_FLOAT, false, vboStrideBytes, (quadSizeLength + positionLength) * Float.BYTES);
            glVertexAttribPointer(3, texturePositionLength, GL_FLOAT, false, vboStrideBytes, (quadSizeLength + positionLength + textureSizeLength) * Float.BYTES);
            glVertexAttribIPointer(4, textureIndexLength, GL_INT, vboStrideBytes, (quadSizeLength + positionLength + textureSizeLength + texturePositionLength) * Float.BYTES);
            glVertexAttribPointer(5, colorCoefsLength, GL_FLOAT, false, vboStrideBytes, (quadSizeLength + positionLength + textureSizeLength + texturePositionLength) * Float.BYTES + textureIndexLength * Integer.BYTES);
            glVertexAttribPointer(6, addedColorLength, GL_FLOAT, false, vboStrideBytes, (quadSizeLength + positionLength + textureSizeLength + texturePositionLength + colorCoefsLength) * Float.BYTES + textureIndexLength * Integer.BYTES);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }

        public void uploadData() {
            dataBuffer.clear();
            for (int i = 0; i < vertices.size(); i++) {
                Image.ImageVertex image = vertices.get(i);
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
                dataBuffer.putInt(textureIndexes.get(i));
                dataBuffer.putFloat(colorCoefs.r);
                dataBuffer.putFloat(colorCoefs.g);
                dataBuffer.putFloat(colorCoefs.b);
                dataBuffer.putFloat(colorCoefs.a);
                dataBuffer.putFloat(addedColor.r);
                dataBuffer.putFloat(addedColor.g);
                dataBuffer.putFloat(addedColor.b);
                dataBuffer.putFloat(addedColor.a);
            }
            dataBuffer.flip();
            glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, dataBuffer);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            dataBuffer.flip();
        }

        @Override
        public void addVertex(Image.ImageVertex newVertex) {
            assert vertices.size() == textureIndexes.size() : "mismatching list sizes between vertices and texture indices";
            super.addVertex(newVertex);
            int textureIndex = textures.indexOf(newVertex.getTexture());
            assert textures.size() < GlobalVars.MAX_TEXTURE_SLOTS || textureIndex != -1 : "invalid vertex texture";
            if (textureIndex != -1) {
                textureIndexes.add(textureIndex);
            }
            else {
                textures.add(newVertex.getTexture());
                textureIndexes.add(textures.size() - 1);
            }
        }

        @Override
        public void removeVertex(int vertexToRemoveIndex) {
            assert vertices.size() == textureIndexes.size() : "mismatching list sizes between vertices and texture indices";
            assert vertexToRemoveIndex < vertices.size() : "index out of bounds";
            vertices.remove(vertexToRemoveIndex);
            textureIndexes.remove(vertexToRemoveIndex);
        }
    }
}
