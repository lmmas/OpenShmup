package engine.render;

import engine.assets.Shader;
import engine.graphics.RenderType;
import engine.scene.Scene;
import engine.graphics.Graphic;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL33.*;

public abstract class Renderer<G extends Graphic<G,P>, P extends Graphic<G,P>.Primitive>{
    protected Scene scene;
    protected int vaoID;
    protected RenderType type;
    final protected int drawingType;
    private int layer;
    final protected int vboStrideBytes;
    protected int batchSize;
    protected ArrayList<Batch> batches;
    public Renderer(RenderType type, int drawingType, int vboStrideBytes){
        this.vaoID = glGenVertexArrays();
        this.type = type;
        this.drawingType = drawingType;
        this.vboStrideBytes = vboStrideBytes;
        this.batches = new ArrayList<>();
    }
    public int getVaoID(){
        return vaoID;
    }
    abstract protected Batch createBatchFromGraphic(G graphic);
    protected void addNewBatchFromGraphic(G graphic){
        Batch newBatch = createBatchFromGraphic(graphic);
        batches.add(newBatch);
    }

    public RenderType getType() {
        return type;
    }

    public void draw(){
        glBindVertexArray(this.vaoID);
        for(Batch batch : batches) {
            batch.setupVertexAttributes();
            glBindVertexArray(this.vaoID);
            Shader batchShader = batch.getShader();
            batchShader.use();
            if(batch.dataHasChanged){
                batch.uploadData();
                batch.dataHasChanged = false;
            }
            batch.draw();
        }
        glBindVertexArray(0);
    }

    public void addGraphic(G newGraphic){
        int primitiveCount = newGraphic.getPrimitiveCount();
        for(int primitiveIndex = 0; primitiveIndex < primitiveCount; primitiveIndex++){
            P newPrimitive = newGraphic.getPrimitive(primitiveIndex);
            boolean newPrimitiveAllocated = false;
            int batchIndex = 0;
            while(batchIndex < batches.size()){
                if (batches.get(batchIndex).canReceivePrimitiveFrom(newGraphic)){
                    batches.get(batchIndex).addPrimitive(newPrimitive);
                    newPrimitiveAllocated = true;
                    break;
                }
                batchIndex++;
            }
            if(!newPrimitiveAllocated){
                addNewBatchFromGraphic(newGraphic);
                batches.getLast().addPrimitive(newPrimitive);
            }
        }
    }

    public abstract class Batch {
        protected int vboID;
        protected ArrayList<P> primitives = new ArrayList<>(batchSize);
        protected boolean dataHasChanged = true;
        protected Shader shader;
        protected Batch(Shader shader){
            vboID = glGenBuffers();
            this.shader = shader;
        }

        public Shader getShader() {
            return shader;
        }

        public void addPrimitive(P newPrimitive){
            assert primitives.size() < batchSize: "Can't add primitive data to the batch";
            primitives.add(newPrimitive);
            newPrimitive.setBatch(this);
            dataHasChanged = true;
        }
        abstract boolean canReceivePrimitiveFrom(G graphic);
        abstract protected void setupVertexAttributes();
        abstract protected void uploadData();
        abstract protected void draw();
        public void dataHasChanged(){
            dataHasChanged = true;
        }
        public void removePrimitive(P primitive){
            primitives.remove(primitive);
        }
    }
}