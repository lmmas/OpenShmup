package engine.graphics.colorRectangle;

import engine.Engine;
import engine.assets.Shader;
import engine.graphics.RenderType;
import engine.graphics.Renderer;
import types.RGBAValue;
import types.Vec2D;

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

        public ColorRectangleBatch(Shader shader) {
            super(shader);
        }

        @Override
        protected boolean canReceiveVertexFrom(ColorRectangleGraphic graphic) {
            if (vertices.size() >= batchSize) {
                return false;
            }
            return graphic.getShader() == shader;
        }

        @Override
        public void sendToBuffer(int vertexIndex){
            ColorRectangleGraphic.ColorRectangleVertex rectangle = vertices.get(vertexIndex);
            Vec2D position = rectangle.getPosition();
            Vec2D size = rectangle.getSize();
            RGBAValue color = rectangle.getColor();

            dataBuffer.putFloat(size.x);
            dataBuffer.putFloat(size.y);
            dataBuffer.putFloat(position.x);
            dataBuffer.putFloat(position.y);
            dataBuffer.putFloat(color.r);
            dataBuffer.putFloat(color.g);
            dataBuffer.putFloat(color.b);
            dataBuffer.putFloat(color.a);
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
