package engine.graphics;

import engine.assets.Shader;
import engine.types.Vec2D;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public abstract class Graphic<G extends Graphic<G, V>, V extends Graphic<G, V>.Vertex> {

    final protected RenderType renderType;

    final protected Shader shader;

    public Graphic(Graphic<?, ?> graphic) {
        this.renderType = graphic.renderType;
        this.shader = graphic.shader;
    }

    abstract public Vec2D getPosition();

    abstract public Vec2D getScale();

    abstract public void setPosition(float positionX, float positionY);

    abstract public void setScale(float scaleX, float scaleY);

    abstract public int getVertexCount();

    abstract public V getVertex(int index);

    abstract public void remove();

    abstract public class Vertex {

        protected boolean dataHasChangedFlag = true;

        protected boolean shouldBeRemovedFlag = false;

        public Vertex() {
            this.shouldBeRemovedFlag = false;
        }

        public boolean getDataHasChangedFlag() {
            return dataHasChangedFlag;
        }

        public void dataHasChanged() {
            this.dataHasChangedFlag = true;
        }

        public void resetDataHasChangedFlag() {
            this.dataHasChangedFlag = false;
        }

        public boolean getShouldBeRemoved() {
            return shouldBeRemovedFlag;
        }

        public void remove() {
            this.shouldBeRemovedFlag = true;
        }

        public void hasBeenRemoved() {
            this.shouldBeRemovedFlag = false;
        }
    }
}
