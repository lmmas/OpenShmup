package engine.render;

import engine.scene.Scene;
import engine.graphics.Graphic;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL33.*;

public abstract class VAO<G extends Graphic<G,P>, P extends Graphic<G,P>.Primitive>{
    protected Scene scene;
    protected int ID;
    protected RenderType type;
    final protected int drawingType;
    protected int layer;
    final protected int vboStrideBytes;
    protected int batchSize = 100;
    protected ArrayList<VBO> vbos = new ArrayList<VBO>();
    public VAO(RenderType type, int drawingType, int vboStrideBytes){
        this.ID = glGenVertexArrays();
        this.type = type;
        this.drawingType = drawingType;
        this.vboStrideBytes = vboStrideBytes;
    }
    public int getID(){
        return ID;
    }
    abstract protected VBO createVboFromGraphic(G graphic);
    protected void addNewVboFromGraphic(G graphic){
        VBO newVbo = createVboFromGraphic(graphic);
        vbos.add(newVbo);
    }

    public RenderType getType() {
        return type;
    }

    public void draw(){
        glBindVertexArray(this.ID);
        for(VBO vbo: vbos) {
            Shader vboShader = vbo.getShader();
            vboShader.uploadTexture("TEX_SAMPLER", 0);
            vboShader.use();
            if(vbo.dataHasChanged){
                vbo.uploadData();
                vbo.dataHasChanged = false;
            }
            vbo.draw();
        }
        glBindVertexArray(0);
    }

    public void addGraphic(G newGraphic){
        int primitiveCount = newGraphic.getPrimitiveCount();
        for(int i = 0; i < primitiveCount; i++){
            P newPrimitive = newGraphic.getPrimitive(i);
            boolean newPrimitiveAllocated = false;
            int vboIndex = 0;
            while(!newPrimitiveAllocated && vboIndex < vbos.size()){
                if (vbos.get(i).canReceivePrimitiveFrom(newGraphic)){
                    vbos.get(i).addPrimitive(newPrimitive);
                    newPrimitiveAllocated = true;
                }
                vboIndex++;
            }
            if(!newPrimitiveAllocated){
                addNewVboFromGraphic(newGraphic);
                vbos.getLast().addPrimitive(newPrimitive);
            }
        }
    }

    public abstract class VBO{
        protected int ID;
        protected ArrayList<P> primitives = new ArrayList<>(batchSize);
        protected boolean dataHasChanged = true;
        protected Shader shader;
        protected VBO(Shader shader){
            ID = glGenBuffers();
            this.shader = shader;
            setupVertexAttributes();
        }

        public Shader getShader() {
            return shader;
        }

        public void addPrimitive(P newPrimitive){
            assert primitives.size() < batchSize: "Can't add primitive data to VBO";
            primitives.add(newPrimitive);
            newPrimitive.setVbo(this);
        }
        abstract boolean canReceivePrimitiveFrom(G graphic);
        abstract protected void setupVertexAttributes();
        abstract protected void uploadData();
        abstract protected void draw();
        public void dataHasChanged(){
            dataHasChanged = true;
        }
    }
}