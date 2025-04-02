package engine.graphics;
import engine.render.RenderInfo;
import engine.render.Shader;
import engine.render.Renderer;
import engine.render.GraphicType;


public abstract class Graphic<G extends Graphic<G, P>, P extends Graphic<G,P>.Primitive>{
    final protected Shader shader;
    final protected RenderInfo renderInfo;

    public Graphic(int layer, GraphicType type, Shader shader){
        this.renderInfo = new RenderInfo(layer, type);
        this.shader = shader;
    }

    abstract public G copy();

    public Shader getShader() {
        return shader;
    }

    public RenderInfo getRenderInfo(){
        return renderInfo;
    }

    abstract public int getPrimitiveCount();

    abstract public P getPrimitive(int index);
    abstract public void delete();

    abstract public class Primitive{
        Renderer<G,P>.Batch currentBatch;
        abstract public P copy();
        public void setBatch(Renderer<G,P>.Batch newBatch){
            this.currentBatch = newBatch;
        }
        public void tellBatchDataChanged(){
            if(currentBatch !=null){
                currentBatch.dataHasChanged();
            }
        }
        @SuppressWarnings("unchecked")
        public void delete(){
            currentBatch.removePrimitive((P) this);
        }
    }
}
