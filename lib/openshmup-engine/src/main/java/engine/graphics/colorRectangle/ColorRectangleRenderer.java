package engine.graphics.colorRectangle;

import engine.Engine;
import engine.assets.Shader;
import engine.graphics.RenderType;
import engine.graphics.Renderer;
import org.lwjgl.BufferUtils;
import types.RGBAValue;
import types.Vec2D;

import java.nio.FloatBuffer;
import java.util.List;

import static engine.graphics.VBOAttributeInfo.VEC2;
import static engine.graphics.VBOAttributeInfo.VEC4;
import static org.lwjgl.opengl.GL33.*;

final public class ColorRectangleRenderer extends Renderer<ColorRectangleGraphic, ColorRectangleGraphic.ColorRectangleVertex> {

    public ColorRectangleRenderer() {
        super(RenderType.COLOR_RECTANGLE, GL_STREAM_DRAW, List.of(VEC2, VEC2, VEC4));
    }

    @Override
    protected Batch createBatchFromGraphic(ColorRectangleGraphic graphic) {
        return new ColorRectangleBatch(graphic.getShader());
    }

    public class ColorRectangleBatch extends Renderer<ColorRectangleGraphic, ColorRectangleGraphic.ColorRectangleVertex>.Batch {

        final private FloatBuffer dataBuffer;

        public ColorRectangleBatch(Shader shader) {
            super(shader);
            this.dataBuffer = BufferUtils.createFloatBuffer(batchSize * vertexDataSize);
            glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
            glBufferData(GL_ARRAY_BUFFER, dataBuffer, drawingType);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }

        @Override
        protected boolean canReceiveVertexFrom(ColorRectangleGraphic graphic) {
            if (vertices.size() >= batchSize) {
                return false;
            }
            return graphic.getShader() == shader;
        }

        @Override
        protected void uploadData() {
            dataBuffer.clear();
            for (ColorRectangleGraphic.ColorRectangleVertex rectangle : vertices) {
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
            shader.uploadUniform("u_NativeResolution", new int[]{Engine.getNativeWidth(), Engine.getNativeHeight()});
            shader.uploadUniform("u_WindowResolution", new int[]{Engine.window.getWidth(), Engine.window.getHeight()});
            glEnableVertexAttribArray(0);
            glEnableVertexAttribArray(1);
            glEnableVertexAttribArray(2);
            glDrawArrays(GL_POINTS, 0, vertices.size());
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
        }

    }
}
