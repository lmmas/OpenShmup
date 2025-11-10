package engine.graphics.colorRectangle;

import engine.Application;
import engine.assets.Shader;
import engine.graphics.Renderer;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import engine.graphics.RenderType;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

final public class ColorRectangleRenderer extends Renderer<ColorRectangle, ColorRectangle.ColorRectanglePrimitive> {
    final static private int vertexFloatCount = 8;
    public ColorRectangleRenderer(){
        super(RenderType.COLOR_RECTANGLE, GL_DYNAMIC_DRAW, vertexFloatCount * Float.BYTES);
        this.batchSize = 100;
    }
    @Override
    protected Batch createBatchFromGraphic(ColorRectangle graphic) {
        return new ColorRectangleBatch(graphic.getShader());
    }
    public class ColorRectangleBatch extends Renderer<ColorRectangle, ColorRectangle.ColorRectanglePrimitive>.Batch{
        final private FloatBuffer dataBuffer;
        public ColorRectangleBatch(Shader shader){
            super(shader);
            this.dataBuffer = BufferUtils.createFloatBuffer(batchSize * vertexFloatCount * Float.BYTES);
            glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
            glBufferData(GL_ARRAY_BUFFER, dataBuffer, drawingType);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }
        @Override
        protected boolean canReceivePrimitiveFrom(ColorRectangle graphic) {
            if(primitives.size() >= batchSize){
                return false;
            }
            return graphic.getShader() == shader;
        }

        @Override
        protected void setupVertexAttributes() {
            int quadSizeLength = 2;
            int positionLength = 2;
            int colorLength = 4;
            glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
            glVertexAttribPointer(0, quadSizeLength, GL_FLOAT, false, vboStrideBytes, 0);
            glVertexAttribPointer(1, positionLength, GL_FLOAT, false, vboStrideBytes, quadSizeLength * Float.BYTES);
            glVertexAttribPointer(2, colorLength, GL_FLOAT, false, vboStrideBytes, (quadSizeLength + positionLength) * Float.BYTES);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }

        @Override
        protected void uploadData() {
            dataBuffer.clear();
            for (ColorRectangle.ColorRectanglePrimitive rectangle : primitives) {
                Vec2D position = rectangle.getPosition();
                Vec2D size = rectangle.getSize();
                RGBAValue color = rectangle.getColor();


                dataBuffer.put(size.x);
                dataBuffer.put(size.y);
                dataBuffer.put(position.x);
                dataBuffer.put(position.y);
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
            shader.uploadUniform("u_WindowResolution", new int[]{Application.window.getWidth(), Application.window.getHeight()});
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);
            glDrawArrays(GL_POINTS, 0, primitives.size());
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
        }

        @Override
        public void removePrimitive(int primitiveToRemoveIndex) {
            assert primitiveToRemoveIndex < primitives.size(): "index out of bounds";
            primitives.remove(primitiveToRemoveIndex);
        }
    }
}
