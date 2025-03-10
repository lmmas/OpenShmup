package engine.render;

import engine.GlobalVars;
import engine.visual.Image2D;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;

import static org.lwjgl.opengl.GL33.*;

abstract public class Image2DVAO extends VAO<Image2D, Image2D.ImagePrimitive>{

    protected VBO createVboFromVisual(Image2D image){
        return new ImageVBO(image.getShader(), image.getTexture());
    }
    public Image2DVAO(RenderType type, int drawingType){
        super(type, drawingType, 32);
        this.batchSize = 100;
    }

    protected class ImageVBO extends VAO<Image2D, Image2D.ImagePrimitive>.VBO {
        protected ArrayList<Texture> textures = new ArrayList<>(GlobalVars.MAX_TEXTURE_SLOTS);
        protected ByteBuffer dataBuffer = BufferUtils.createByteBuffer(Float.BYTES * batchSize * vertexAttributeCount);
        protected static final int vertexAttributeCount = 8;

        public ImageVBO(Shader shader, Texture texture){
            super(shader);
            this.textures.add(texture);
        }

        public void draw(){
            for(int i = 0; i < textures.size(); i++){
                textures.get(i).bind(i);
            }
            glBindBuffer(GL_ARRAY_BUFFER, this.ID);
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);
            glEnableVertexAttribArray(3);
            shader.validate();
            glDrawArrays(GL_POINTS, 0, primitives.size());
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glDisableVertexAttribArray(3);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }

        @Override
        boolean canReceivePrimitiveFrom(Image2D image) {
            if(primitives.size() >= batchSize * image.getPrimitiveCount() - 1)
                return false;
            return image.getShader() == this.shader && (textures.contains(image.getTexture()) || textures.size() < GlobalVars.MAX_TEXTURE_SLOTS);
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
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        }

        public void uploadData(){
            dataBuffer.clear();
            for(Image2D.ImagePrimitive image: primitives){
                dataBuffer.putFloat(image.getImagePositionX());
                dataBuffer.putFloat(image.getImagePositionY());
                dataBuffer.putFloat(image.getImageSizeX());
                dataBuffer.putFloat(image.getImageSizeY());
                dataBuffer.putFloat(image.getTexturePositionX());
                dataBuffer.putFloat(image.getTexturePositionY());
                dataBuffer.putFloat(image.getTextureSizeX());
                dataBuffer.putFloat(image.getTextureSizeY());
            }
            dataBuffer.flip();
            glBindBuffer(GL_ARRAY_BUFFER, this.ID);
            glBufferData(GL_ARRAY_BUFFER, dataBuffer, drawingType);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            dataBuffer.flip();
        }
    }
}
