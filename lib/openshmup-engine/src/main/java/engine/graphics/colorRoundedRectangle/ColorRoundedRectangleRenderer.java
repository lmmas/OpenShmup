package engine.graphics.colorRoundedRectangle;

import engine.Engine;
import engine.assets.Shader;
import engine.graphics.RenderType;
import engine.graphics.Renderer;
import org.lwjgl.BufferUtils;
import types.RGBAValue;
import types.Vec2D;

import java.nio.FloatBuffer;
import java.util.List;

import static engine.graphics.VBOAttributeInfo.*;
import static org.lwjgl.opengl.GL33.*;

final public class ColorRoundedRectangleRenderer extends Renderer<RoundedColorRectangle, RoundedColorRectangle.ColorRoundedRectangleVertex> {

    public ColorRoundedRectangleRenderer() {
        super(RenderType.COLOR_ROUNDED_RECTANGLE, GL_STATIC_DRAW, List.of(VEC2, VEC2, FLOAT, VEC4));
    }

    @Override
    protected Batch createBatchFromGraphic(RoundedColorRectangle graphic) {
        return new ColorRoundedRectangleBatch(graphic.getShader());
    }

    public class ColorRoundedRectangleBatch extends Renderer<RoundedColorRectangle, RoundedColorRectangle.ColorRoundedRectangleVertex>.Batch {

        final private FloatBuffer dataBuffer;

        public ColorRoundedRectangleBatch(Shader shader) {
            super(shader);
            this.dataBuffer = BufferUtils.createFloatBuffer(batchSize * vertexDataSize);
            glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
            glBufferData(GL_ARRAY_BUFFER, dataBuffer, drawingType);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }

        @Override
        protected boolean canReceiveVertexFrom(RoundedColorRectangle graphic) {
            if (vertices.size() >= batchSize) {
                return false;
            }
            return graphic.getShader() == shader;
        }

        @Override
        protected void uploadData() {
            dataBuffer.clear();
            for (RoundedColorRectangle.ColorRoundedRectangleVertex rectangle : vertices) {
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
            shader.uploadUniform("u_NativeResolution", new int[]{Engine.getNativeWidth(), Engine.getNativeHeight()});
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

    }
}
