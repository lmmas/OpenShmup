package engine.render;

import engine.visual.Visual;

import java.util.ArrayList;
import java.util.LinkedList;

import static org.lwjgl.opengl.GL33.*;

public abstract class VAO<V extends Visual<V,P>, P extends Visual<V,P>.Primitive>{
    protected engine.Scene scene;
    protected int ID;
    protected RenderType type;
    final protected int drawingType;
    protected int layer;


    final protected int vboStrideBytes;
    protected int batchSize = 100;
    protected ArrayList<VBO> vbos = new ArrayList<VBO>();
    protected LinkedList<V> visualList;
    public VAO(RenderType type, int drawingType, int vboStrideBytes){
        this.ID = glGenVertexArrays();
        this.type = type;
        this.drawingType = drawingType;
        this.vboStrideBytes = vboStrideBytes;
    }
    public int getID(){
        return ID;
    }
    abstract protected VBO createVboFromVisual(V visual);
    protected void addNewVboFromVisual(V visual){
        VBO newVbo = createVboFromVisual(visual);
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

    public void addVisual(V newVisual){
        int primitiveCount = newVisual.getPrimitiveCount();
        for(int i = 0; i < primitiveCount; i++){
            P newPrimitive = newVisual.getPrimitive(i);
            boolean newPrimitiveAllocated = false;
            int vboIndex = 0;
            while(!newPrimitiveAllocated && vboIndex < vbos.size()){
                if (vbos.get(i).canReceivePrimitiveFrom(newVisual)){
                    vbos.get(i).addPrimitive(newPrimitive);
                    newPrimitiveAllocated = true;
                }
                vboIndex++;
            }
            if(!newPrimitiveAllocated){
                addNewVboFromVisual(newVisual);
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
        abstract boolean canReceivePrimitiveFrom(V visual);
        abstract protected void setupVertexAttributes();
        abstract protected void uploadData();
        abstract protected void draw();
        public void dataHasChanged(){
            dataHasChanged = true;
        }
    }
}

