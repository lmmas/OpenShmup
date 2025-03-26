package engine.graphics;
import engine.render.RenderInfo;
import engine.render.Shader;
import engine.render.VAO;
import engine.render.RenderType;
import engine.scene.Scene;


public abstract class Graphic<G extends Graphic<G, P>, P extends Graphic<G,P>.Primitive>{
    final protected Shader shader;
    final protected RenderType type;
    protected int layer;
    final private RenderInfo renderInfo;

    public Graphic(int layer, RenderType type, Shader shader){
        this.layer = layer;
        this.type = type;
        this.renderInfo = new RenderInfo(layer, type);
        this.shader = shader;
    }

    abstract public G copy();

    public Shader getShader() {
        return shader;
    }

    public int getLayer() {
        return layer;
    }

    public RenderType getType() {
        return type;
    }

    public RenderInfo getRenderInfo(){
        return renderInfo;
    }

    abstract public int getPrimitiveCount();

    abstract public P getPrimitive(int index);
    abstract public void delete();

    abstract public class Primitive{
        VAO<G,P>.VBO currentVbo;
        abstract public P copy();
        public void setVbo(VAO<G,P>.VBO newVbo){
            this.currentVbo = newVbo;
        }
        public void tellVboDataChanged(){
            if(currentVbo!=null){
                currentVbo.dataHasChanged();
            }
        }
        @SuppressWarnings("unchecked")
        public void delete(){
            currentVbo.removePrimitive((P) this);
        }
    }
}
