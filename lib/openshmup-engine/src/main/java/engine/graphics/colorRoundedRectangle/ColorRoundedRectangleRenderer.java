package engine.graphics.colorRoundedRectangle;

import engine.assets.Shader;
import engine.graphics.Renderer;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.graphics.RenderType;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

final public class ColorRoundedRectangleRenderer extends Renderer<ColorRoundedRectangle, ColorRoundedRectangle.ColorRoundedRectanglePrimitive> {
    final static private int vertexFloatCount = 9;

    public ColorRoundedRectangleRenderer(){
        super(RenderType.COLOR_ROUNDED_RECTANGLE, GL_STATIC_DRAW, vertexFloatCount * Float.BYTES);
        this.batchSize = 100;
    }

    @Override
    protected Batch createBatchFromGraphic(ColorRoundedRectangle graphic) {
        return new ColorRoundedRectangleBatch(graphic.getShader());
    }

    public class ColorRoundedRectangleBatch extends Renderer<ColorRoundedRectangle, ColorRoundedRectangle.ColorRoundedRectanglePrimitive>.Batch{
        final private FloatBuffer dataBuffer;
        public ColorRoundedRectangleBatch(Shader shader){
            super(shader);
            this.dataBuffer = BufferUtils.createFloatBuffer(batchSize * vertexFloatCount * Float.BYTES);
            glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
            glBufferData(GL_ARRAY_BUFFER, dataBuffer, drawingType);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
        @Override
        protected boolean canReceivePrimitiveFrom(ColorRoundedRectangle graphic) {
            if(primitives.size() >= batchSize){
                return false;
            }
            return graphic.getShader() == shader;
        }

        @Override
        protected void setupVertexAttributes() {
            int quadSizeLength = 2;
            int positionLength = 2;
            int roundingRadiusLength = 1;
            int colorLength = 4;
            glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
            glVertexAttribPointer(0, quadSizeLength, GL_FLOAT, false, vboStrideBytes, 0);
            glVertexAttribPointer(1, positionLength, GL_FLOAT, false, vboStrideBytes, quadSizeLength * Float.BYTES);
            glVertexAttribPointer(2, roundingRadiusLength, GL_FLOAT, false, vboStrideBytes, (quadSizeLength + positionLength) * Float.BYTES);
            glVertexAttribPointer(3, colorLength, GL_FLOAT, false, vboStrideBytes, (quadSizeLength + positionLength + roundingRadiusLength) * Float.BYTES);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }

        @Override
        protected void uploadData() {
            dataBuffer.clear();
            for (ColorRoundedRectangle.ColorRoundedRectanglePrimitive rectangle : primitives) {
                Vec2D position = rectangle.getPosition();
                Vec2D size = rectangle.getSize();
                float roundingRadius = rectangle.getRoundingRadius();
                RGBAValue color = rectangle.getColor();


                dataBuffer.put(size.x);
                dataBuffer.put(size.y);
                dataBuffer.put(position.x);
                dataBuffer.put(position.y);
                dataBuffer.put(roundingRadius);
                dataBuffer.put(color.r);
                dataBuffer.put(color.g);
                dataBuffer.put(color.b);
                dataBuffer.put(color.a);
            }
            dataBuffer.flip();
            glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, dataBuffer);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            dataBuffer.flip();
        }

        @Override
        protected void draw() {
            shader.use();
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);
            glEnableVertexAttribArray(3);
            glDrawArrays(GL_POINTS, 0, primitives.size());
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glDisableVertexAttribArray(3);
        }

        @Override
        public void removePrimitive(int primitiveToRemoveIndex) {
            assert primitiveToRemoveIndex < primitives.size(): "index out of bounds";
            primitives.remove(primitiveToRemoveIndex);
        }
    }
}
