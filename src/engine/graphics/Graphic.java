package engine.graphics;
import engine.render.Shader;
import engine.render.VAO;
import engine.render.RenderType;
import engine.scene.Scene;

import java.util.ArrayList;


public abstract class Graphic<G extends Graphic<G, P>, P extends Graphic<G,P>.Primitive>{
    final protected Scene scene;
    final protected Shader shader;
    final protected RenderType type;
    protected int layer;
    VAO<G,P> vao;
    public Graphic(Scene scene, int layer, RenderType type, Shader shader){
        this.scene = scene;
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
    abstract public void delete();

    abstract public class Primitive{
        VAO<G,P>.VBO currentVbo;
        public void setVbo(VAO<G,P>.VBO newVbo){
            this.currentVbo = newVbo;
            tellVboDataChanged();
        }
        public void tellVboDataChanged(){
            if(currentVbo!=null){
                currentVbo.dataHasChanged();
            }
        }
        public void delete(){
            currentVbo.removePrimitive((P) this);
        }
    }
}
