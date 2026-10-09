package engine.graphics;

import engine.assets.Shader;
import engine.hitbox.RectangleBox;
import lombok.Getter;
import lombok.Setter;
import types.Vec2D;

import java.util.ArrayList;
import java.util.List;

@Getter
public abstract class Graphic<T extends Graphic<T>.Vertex<T>> {

    final protected RenderType renderType;

    final protected Shader shader;
    @Getter
    @Setter
    private RectangleBox scissorBox;

    final private List<T> vertexList;

    public Graphic(RenderType renderType, Shader shader, List<T> vertexList) {
        this.renderType = renderType;
        this.shader = shader;
        this.scissorBox = null;
        this.vertexList = vertexList;
    }

    public Graphic(Graphic<T> graphic) {
        this.renderType = graphic.renderType;
        this.shader = graphic.shader;
        this.scissorBox = null;
        this.vertexList = new ArrayList<>(graphic.vertexList.size());
        for (var vertex : graphic.vertexList) {
            this.vertexList.add(vertex.copy());
        }
    }

    abstract public Vec2D getPosition();

    abstract public Vec2D getScale();

    abstract public void setPosition(Vec2D position);

    abstract public void setScale(Vec2D scale);

    abstract public void remove();

    abstract public class Vertex<V extends Vertex<V>> {

        private boolean dataHasChangedFlag;

        private boolean shouldBeRemovedFlag;

        public abstract V copy();

        public Vertex() {
            this.dataHasChangedFlag = true;
            this.shouldBeRemovedFlag = false;
        }

        public boolean getDataHasChanged() {
            return dataHasChangedFlag;
        }

        public void setDataHasChanged() {
            this.dataHasChangedFlag = true;
        }

        public void resetDataHasChanged() {
            this.dataHasChangedFlag = false;
        }

        public boolean getShouldBeRemoved() {
            return shouldBeRemovedFlag;
        }

        public void setShouldBeRemoved() {
            this.shouldBeRemovedFlag = true;
        }

        public void resetShouldBeRemoved() {
            this.shouldBeRemovedFlag = false;
        }
    }
}
