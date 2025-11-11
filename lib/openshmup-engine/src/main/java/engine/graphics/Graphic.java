package engine.graphics;
import engine.assets.Shader;
import engine.types.Vec2D;


public abstract class Graphic<G extends Graphic<G, P>, P extends Graphic<G,P>.Primitive>{
    final protected RenderType renderType;
    final protected Shader shader;

    public Graphic(RenderType type, Shader shader){
        this.renderType = type;
        this.shader = shader;
    }

    public Graphic(Graphic<?,?> graphic){
        this.renderType = graphic.renderType;
        this.shader = graphic.shader;
    }

    public Shader getShader() {
        return shader;
    }

    public RenderType getRenderType(){
        return renderType;
    }

    abstract public Vec2D getPosition();

    abstract public Vec2D getScale();

    abstract public void setPosition(float positionX, float positionY);

    abstract public void setScale(float scaleX, float scaleY);

    abstract public int getPrimitiveCount();

    abstract public P getPrimitive(int index);

    abstract public void remove();

    abstract public class Primitive{
        protected boolean dataHasChangedFlag = true;
        protected boolean shouldBeRemovedFlag = false;

        public Primitive(){
            this.shouldBeRemovedFlag = false;
        }

        public boolean getDataHasChangedFlag(){
            return dataHasChangedFlag;
        }

        public void dataHasChanged(){
            this.dataHasChangedFlag = true;
        }

        public void resetDataHasChangedFlag(){
            this.dataHasChangedFlag = false;
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
