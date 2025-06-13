package engine.graphics;
import engine.render.RenderInfo;
import engine.assets.Shader;
import engine.render.Renderer;
import engine.types.RGBAValue;


public abstract class Graphic<G extends Graphic<G, P>, P extends Graphic<G,P>.Primitive>{
    final protected Shader shader;
    final protected RenderInfo renderInfo;
    final private RGBAValue colorCoefs;
    final private RGBAValue addedColor;

    public Graphic(int layer, GraphicType type, Shader shader){
        this.renderInfo = new RenderInfo(layer, type);
        this.shader = shader;
        this.colorCoefs = new RGBAValue(1.0f, 1.0f, 1.0f, 1.0f);
        this.addedColor = new RGBAValue(0.0f, 0.0f, 0.0f, 0.0f);
    }

    public Graphic(Graphic<?,?> graphic){
        this.renderInfo = new RenderInfo(graphic.renderInfo.layer(), graphic.renderInfo.graphicType());
        this.shader = graphic.shader;
        this.colorCoefs = new RGBAValue(graphic.colorCoefs.r, graphic.colorCoefs.g, graphic.colorCoefs.b, graphic.colorCoefs.a);
        this.addedColor = new RGBAValue(graphic.addedColor.r, graphic.addedColor.g, graphic.addedColor.b, graphic.addedColor.a);
    }

    abstract public G copy();

    public Shader getShader() {
        return shader;
    }

    public RenderInfo getRenderInfo(){
        return renderInfo;
    }

    abstract public int getPrimitiveCount();

    public RGBAValue getColorCoefs() {
        return colorCoefs.copy();
    }

    public void setColorCoefs(float r, float g, float b, float a){
        colorCoefs.r = r;
        colorCoefs.g = g;
        colorCoefs.b = b;
        colorCoefs.a = a;
    }

    public RGBAValue getAddedColor() {
        return addedColor.copy();
    }

    public void setAddedColor(float r, float g, float b, float a){
        addedColor.r = r;
        addedColor.g = g;
        addedColor.b = b;
        addedColor.a = a;
    }

    abstract public P getPrimitive(int index);
    abstract public void delete();

    abstract public class Primitive{
        Renderer<G,P>.Batch currentBatch;
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
