package engine.graphics.roundedRectangleBorder;

import engine.assets.Shader;
import engine.graphics.Graphic;
import engine.graphics.RenderType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import types.RGBAValue;
import types.Vec2D;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static engine.Engine.assetManager;

final public class RoundedRectangleBorder extends Graphic<RoundedRectangleBorder.RoundedRectangleBorderVertex> {

    final static public Path defaultShader = Paths.get("lib/openshmup-engine/src/main/resources/shaders/roundedRectangleBorder.glsl");

    private final RoundedRectangleBorderVertex vertex;

    public RoundedRectangleBorder(Vec2D size, Vec2D position, float roundingRadius, float borderWidth, RGBAValue color, Shader shader) {
        super(RenderType.ROUNDED_RECTANGLE_BORDER, shader, new ArrayList<>(1));
        this.vertex = new RoundedRectangleBorderVertex(size, position, roundingRadius, borderWidth, color);
        this.getVertexList().add(vertex);
    }

    public RoundedRectangleBorder(Vec2D size, Vec2D position, float roundingRadius, float borderWidth, RGBAValue color) {
        this(size, position, roundingRadius, borderWidth, color, assetManager.getShader(defaultShader));
    }

    public RoundedRectangleBorder(RoundedRectangleBorder roundedRectangleBorder) {
        this(roundedRectangleBorder.vertex.size, roundedRectangleBorder.vertex.position, roundedRectangleBorder.vertex.roundingRadius, roundedRectangleBorder.vertex.borderWidth, roundedRectangleBorder.vertex.color, roundedRectangleBorder.shader);
    }

    public Vec2D getPosition() {
        return vertex.position;
    }

    public Vec2D getScale() {
        return vertex.size;
    }

    @Override
    public void remove() {
        vertex.setShouldBeRemoved();
    }

    public void setPosition(Vec2D position) {
        vertex.position = position;
        vertex.setDataHasChanged();
    }

    public void setScale(Vec2D scale) {
        vertex.size = scale;
        vertex.setDataHasChanged();
    }

    public void setColor(RGBAValue color) {
        vertex.color = color;
        vertex.setDataHasChanged();
    }

    @Getter @AllArgsConstructor
    public class RoundedRectangleBorderVertex extends Graphic<RoundedRectangleBorderVertex>.Vertex<RoundedRectangleBorderVertex> {

        private Vec2D size;

        private Vec2D position;

        private float roundingRadius;

        private float borderWidth;

        private RGBAValue color;

        @Override
        public RoundedRectangleBorderVertex copy() {
            return new RoundedRectangleBorderVertex(size, position, roundingRadius, borderWidth, color);
        }
    }
}


