package engine.graphics;

import engine.assets.Shader;
import engine.scene.Scene;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL33.*;

public abstract class Renderer<G extends Graphic<G,P>, P extends Graphic<G,P>.Primitive>{
    protected int vaoID;
    protected RenderType type;
    final protected int drawingType;
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
            batch.update();
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
            assert !primitives.contains(newPrimitive): "primitive already in batch";
            primitives.add(newPrimitive);
            dataHasChanged = true;
        }
        abstract protected boolean canReceivePrimitiveFrom(G graphic);
        abstract protected void setupVertexAttributes();
        abstract protected void uploadData();
        abstract protected void draw();
        public void dataHasChanged(){
            dataHasChanged = true;
        }

        abstract public void removePrimitive(int primitiveToRemoveIndex);

        public void cleanupPrimitives(){
            int i = 0;
            while (i < primitives.size()){
                P primitive = primitives.get(i);
                if(primitive.getShouldBeRemoved()){
                    primitive.hasBeenRemoved();
                    removePrimitive(i);
                    dataHasChanged();
                }
                else{
                    i++;
                }
            }
        }

        public void update(){
            this.cleanupPrimitives();
            this.setupVertexAttributes();
            this.shader.use();
            for(var primitive: primitives){
                if(primitive.getDataHasChangedFlag()){
                    this.uploadData();
                    primitives.forEach(P::resetDataHasChangedFlag);
                    break;
                }
            }
            this.draw();
        }
    }
}