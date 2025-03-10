package engine.visual;
import engine.render.Shader;
import engine.render.VAO;
import engine.render.RenderType;

import java.util.ArrayList;


public abstract class Visual<V extends Visual<V, P>, P extends Visual<V,P>.Primitive>{
    final protected Shader shader;
    final protected RenderType type;
    protected int layer;
    ArrayList<P> primitives;
    VAO<V,P> vao;
    public Visual(int layer, RenderType type, Shader shader){
        this.layer = layer;
        this.type = type;
        this.shader = shader;
    }

    public Shader getShader() {
        return shader;
    }

    public int getLayer() {
        return layer;
    }

    public void setLayer(int layer) {
        this.layer = layer;
    }

    public RenderType getType() {
        return type;
    }

    abstract public int getPrimitiveCount();

    abstract public P getPrimitive(int index);

    abstract public class Primitive{
        VAO<V,P>.VBO currentVbo;
        public void setVbo(VAO<V,P>.VBO newVbo){
            this.currentVbo = newVbo;
            tellVboDataChanged();
        }
        public void tellVboDataChanged(){
            if(currentVbo!=null){
                currentVbo.dataHasChanged();
            }
        }
    }
}
