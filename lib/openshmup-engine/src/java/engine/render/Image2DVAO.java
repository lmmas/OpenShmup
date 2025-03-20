package engine.render;

import engine.GlobalVars;
import engine.Vec2D;
import engine.graphics.Image2D;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;

import static org.lwjgl.opengl.GL33.*;

abstract public class Image2DVAO extends VAO<Image2D, Image2D.ImagePrimitive>{

    protected VBO createVboFromGraphic(Image2D graphic){
        return new ImageVBO(graphic.getShader(), graphic.getTexture());
    }
    public Image2DVAO(RenderType type, int drawingType){
        super(type, drawingType, 36);
        this.batchSize = 100;
    }

    protected class ImageVBO extends VAO<Image2D, Image2D.ImagePrimitive>.VBO {
        protected ArrayList<Texture> textures;
        protected ArrayList<Integer> textureIndexes;
        protected ByteBuffer dataBuffer;
        protected static final int vertexAttributeCount = 9;

        public ImageVBO(Shader shader, Texture texture){
            super(shader);
            this.textures = new ArrayList<>(GlobalVars.MAX_TEXTURE_SLOTS);
            this.textureIndexes = new ArrayList<>();
            this.dataBuffer = BufferUtils.createByteBuffer(Float.BYTES * batchSize * vertexAttributeCount);
            this.textures.add(texture);
        }

        public void draw(){
            shader.use();
            for(int i = 0; i < textures.size(); i++){
                textures.get(i).bind(i);
            }
            int[] array = new int[GlobalVars.MAX_TEXTURE_SLOTS];
            Arrays.setAll(array, i->i);
            shader.uploadUniformIntArray("TEX_SAMPLER" , array);
            shader.validate();
            glBindBuffer(GL_ARRAY_BUFFER, this.ID);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);
            glEnableVertexAttribArray(3);
            glEnableVertexAttribArray(4);
            glDrawArrays(GL_POINTS, 0, primitives.size());
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glDisableVertexAttribArray(3);
            glDisableVertexAttribArray(4);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }

        @Override
        boolean canReceivePrimitiveFrom(Image2D graphic) {
            if(primitives.size() >= batchSize * graphic.getPrimitiveCount() - 1)
                return false;
            return graphic.getShader() == this.shader && (textures.contains(graphic.getTexture()) || textures.size() < GlobalVars.MAX_TEXTURE_SLOTS);
        }

        public void setupVertexAttributes(){
            int positionLength = 2;
            int quadSizeLength = 2;
            int texturePositionLength = 2;
            int textureSizeLength = 2;
            glBindVertexArray(Image2DVAO.this.ID);
            glBindBuffer(GL_ARRAY_BUFFER, this.ID);
            glVertexAttribPointer(0, positionLength, GL_FLOAT, false, vboStrideBytes, 0);
            glVertexAttribPointer(1, quadSizeLength, GL_FLOAT, false, vboStrideBytes, positionLength * Float.BYTES);
            glVertexAttribPointer(2, texturePositionLength, GL_FLOAT, false, vboStrideBytes, (positionLength + quadSizeLength) * Float.BYTES);
            glVertexAttribPointer(3, textureSizeLength, GL_FLOAT, false, vboStrideBytes, (positionLength + quadSizeLength + texturePositionLength) * Float.BYTES);
            glVertexAttribIPointer(4, 1, GL_INT, vboStrideBytes, (positionLength + quadSizeLength + texturePositionLength + textureSizeLength) * Float.BYTES);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }

        public void uploadData(){
            dataBuffer.clear();
            for(int i = 0; i < primitives.size() ; i++){
                Image2D.ImagePrimitive image = primitives.get(i);
                Vec2D imagePosition = image.getImagePosition();
                Vec2D imageSize = image.getImageSize();
                Vec2D texturePosition = image.getTexturePosition();
                Vec2D textureSize = image.getTextureSize();
                dataBuffer.putFloat(imagePosition.x);
                dataBuffer.putFloat(imagePosition.y);
                dataBuffer.putFloat(imageSize.x);
                dataBuffer.putFloat(imageSize.y);
                dataBuffer.putFloat(texturePosition.x);
                dataBuffer.putFloat(texturePosition.y);
                dataBuffer.putFloat(textureSize.x);
                dataBuffer.putFloat(textureSize.y);
                dataBuffer.putInt(textureIndexes.get(i));
            }
            dataBuffer.flip();
            glBindBuffer(GL_ARRAY_BUFFER, this.ID);
            glBufferData(GL_ARRAY_BUFFER, dataBuffer, drawingType);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            dataBuffer.flip();
        }

        @Override
        public void addPrimitive(Image2D.ImagePrimitive newPrimitive) {
            super.addPrimitive(newPrimitive);
            int textureIndex = textures.indexOf(newPrimitive.getTexture());
            assert textures.size() < GlobalVars.MAX_TEXTURE_SLOTS || textureIndex != -1: "invalid primitive texture";
            if(textureIndex != -1){
                textureIndexes.add(textureIndex);
            }
            else{
                textures.add(newPrimitive.getTexture());
                textureIndexes.add(textures.size() - 1);
            }
        }
    }
}
