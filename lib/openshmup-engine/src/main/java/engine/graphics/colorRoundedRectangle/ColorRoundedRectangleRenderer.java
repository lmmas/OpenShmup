package engine.graphics.colorRoundedRectangle;

import engine.Engine;
import engine.assets.Shader;
import engine.graphics.RenderType;
import engine.graphics.Renderer;
import engine.types.RGBAValue;
import engine.types.Vec2D;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

final public class ColorRoundedRectangleRenderer extends Renderer<ColorRoundedRectangle, ColorRoundedRectangle.ColorRoundedRectangleVertex> {

    final static private int vertexFloatCount = 9;

    public ColorRoundedRectangleRenderer() {
        super(RenderType.COLOR_ROUNDED_RECTANGLE, GL_STATIC_DRAW, vertexFloatCount * Float.BYTES);
        this.batchSize = 100;
    }

    @Override
    protected Batch createBatchFromGraphic(ColorRoundedRectangle graphic) {
        return new ColorRoundedRectangleBatch(graphic.getShader());
    }

    public class ColorRoundedRectangleBatch extends Renderer<ColorRoundedRectangle, ColorRoundedRectangle.ColorRoundedRectangleVertex>.Batch {

        final private FloatBuffer dataBuffer;

        public ColorRoundedRectangleBatch(Shader shader) {
            super(shader);
            this.dataBuffer = BufferUtils.createFloatBuffer(batchSize * vertexFloatCount * Float.BYTES);
            glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
            glBufferData(GL_ARRAY_BUFFER, dataBuffer, drawingType);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }

        @Override
        protected boolean canReceiveVertexFrom(ColorRoundedRectangle graphic) {
            if (vertices.size() >= batchSize) {
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
            for (ColorRoundedRectangle.ColorRoundedRectangleVertex rectangle : vertices) {
                Vec2D position = rectangle.getPosition();
                Vec2D size = rectangle.getSize();
                float roundingRadius = rectangle.getRoundingRadius();
                RGBAValue color = rectangle.getColor();

                dataBuffer.put(size.x / Engine.getNativeWidth());
                dataBuffer.put(size.y / Engine.getNativeHeight());
                dataBuffer.put(position.x / Engine.getNativeWidth());
                dataBuffer.put(position.y / Engine.getNativeHeight());
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
            shader.uploadUniform("u_NativeAspectRatio", (float) Engine.getNativeWidth() / Engine.getNativeHeight());
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);
            glEnableVertexAttribArray(3);
            glDrawArrays(GL_POINTS, 0, vertices.size());
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glDisableVertexAttribArray(3);
        }

        @Override
        public void removeVertex(int vertexToRemoveIndex) {
            assert vertexToRemoveIndex < vertices.size() : "index out of bounds";
            vertices.remove(vertexToRemoveIndex);
        }
    }
}
