package engine.graphics;
import engine.render.RenderInfo;
import engine.assets.Shader;
import engine.render.Renderer;
import engine.types.Vec2D;


public abstract class Graphic<G extends Graphic<G, P>, P extends Graphic<G,P>.Primitive>{
    final protected Shader shader;
    final protected RenderInfo renderInfo;

    public Graphic(int layer, RenderType type, Shader shader){
        this.renderInfo = new RenderInfo(layer, type);
        this.shader = shader;
    }

    public Graphic(Graphic<?,?> graphic){
        this.renderInfo = new RenderInfo(graphic.renderInfo.layer(), graphic.renderInfo.renderType());
        this.shader = graphic.shader;
    }

    abstract public G copy();

    public Shader getShader() {
        return shader;
    }

    public RenderInfo getRenderInfo(){
        return renderInfo;
    }

    abstract public Vec2D getPosition();

    abstract public Vec2D getScale();

    abstract public void setPosition(float positionX, float positionY);

    abstract public void setScale(float scaleX, float scaleY);

    abstract public int getPrimitiveCount();

    abstract public P getPrimitive(int index);

    abstract public void remove();

    abstract public class Primitive{
        Renderer<G,P>.Batch currentBatch;

        protected boolean shouldBeRemovedFlag;

        public Primitive(){
            this.currentBatch = null;
            this.shouldBeRemovedFlag = false;
        }

        public void setBatch(Renderer<G,P>.Batch newBatch){
            this.currentBatch = newBatch;
        }

        public void tellBatchDataChanged(){
            if(currentBatch != null) {
                currentBatch.dataHasChanged();
            }
        }

        public boolean getShouldBeRemoved() {
            return shouldBeRemovedFlag;
        }

        public void remove(){
            this.shouldBeRemovedFlag = true;
        }

        public void hasBeenRemoved(){
            this.shouldBeRemovedFlag = false;
        }
    }
}
