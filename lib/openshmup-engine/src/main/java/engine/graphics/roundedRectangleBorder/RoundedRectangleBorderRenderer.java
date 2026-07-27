package engine.graphics.roundedRectangleBorder;

import engine.Engine;
import engine.assets.Shader;
import engine.graphics.RenderType;
import engine.graphics.Renderer;
import types.RGBAValue;
import types.Vec2D;

import java.util.List;

import static engine.graphics.VBOAttributeInfo.*;
import static org.lwjgl.opengl.GL33.*;

final public class RoundedRectangleBorderRenderer extends Renderer<RoundedRectangleBorder, RoundedRectangleBorder.RoundedRectangleBorderVertex> {

    public RoundedRectangleBorderRenderer() {
        super(RenderType.ROUNDED_RECTANGLE_BORDER, GL_STATIC_DRAW, List.of(VEC2, VEC2, FLOAT, FLOAT, VEC4));
    }

    @Override
    protected Batch createBatchFromGraphic(RoundedRectangleBorder graphic) {
        return new RoundedRectangleBorderBatch(graphic.getShader());
    }

    public class RoundedRectangleBorderBatch extends Renderer<RoundedRectangleBorder, RoundedRectangleBorder.RoundedRectangleBorderVertex>.Batch {

        public RoundedRectangleBorderBatch(Shader shader) {
            super(shader);
        }

        @Override
        protected boolean canReceiveVertexFrom(RoundedRectangleBorder graphic) {
            if (vertices.size() >= batchSize) {
                return false;
            }
            return graphic.getShader() == shader;
        }

        @Override
        protected void sendToBuffer(int vertexIndex){
            RoundedRectangleBorder.RoundedRectangleBorderVertex rectangle = vertices.get(vertexIndex);
            Vec2D position = rectangle.getPosition();
            Vec2D size = rectangle.getSize();
            float roundingRadius = rectangle.getRoundingRadius();
            float borderWidth = rectangle.getBorderWidth();
            RGBAValue color = rectangle.getColor();


            dataBuffer.putFloat(size.x);
            dataBuffer.putFloat(size.y);
            dataBuffer.putFloat(position.x);
            dataBuffer.putFloat(position.y);
            dataBuffer.putFloat(roundingRadius);
            dataBuffer.putFloat(borderWidth);
            dataBuffer.putFloat(color.r);
            dataBuffer.putFloat(color.g);
            dataBuffer.putFloat(color.b);
            dataBuffer.putFloat(color.a);
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
            glEnableVertexAttribArray(4);
            glDrawArrays(GL_POINTS, 0, vertices.size());
            glDisableVertexAttribArray(0);
            glDisableVertexAttribArray(1);
            glDisableVertexAttribArray(2);
            glDisableVertexAttribArray(3);
            glDisableVertexAttribArray(4);
        }
    }
}

