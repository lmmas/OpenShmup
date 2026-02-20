package engine.graphics;

import engine.assets.Shader;
import engine.types.Vec2D;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
public abstract class Graphic<T extends Graphic<T>.Vertex<T>> {

    final protected RenderType renderType;

    final protected Shader shader;

    final private List<T> vertexList;

    public Graphic(Graphic<T> graphic) {
        this.renderType = graphic.renderType;
        this.shader = graphic.shader;
        this.vertexList = new ArrayList<>(graphic.vertexList.size());
        for (var vertex : graphic.vertexList) {
            this.vertexList.add(vertex.copy());
        }
    }

    abstract public Vec2D getPosition();

    abstract public Vec2D getScale();

    abstract public void setPosition(float positionX, float positionY);

    abstract public void setScale(float scaleX, float scaleY);

    abstract public void remove();

    abstract public class Vertex<V extends Vertex<V>> {

        protected boolean dataHasChangedFlag = true;

        protected boolean shouldBeRemovedFlag = false;

        public abstract V copy();

        public Vertex() {
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
