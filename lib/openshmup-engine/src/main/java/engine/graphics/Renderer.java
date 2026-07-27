package engine.graphics;

import engine.assets.Shader;
import lombok.Getter;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL33.*;

public abstract class Renderer<G extends Graphic<V>, V extends Graphic<V>.Vertex<V>> {

    @Getter
    protected int vaoID;
    @Getter
    protected RenderType type;

    final protected int drawingType;
    final protected int vertexDataSize; //size in words
    final protected int vboStrideBytes;

    protected int batchSize;

    final private ArrayList<Batch> batches;
    final private List<VBOAttributeInfo> attributeInfoList;

    public Renderer(RenderType type, int drawingType, List<VBOAttributeInfo> attributeInfoList) {
        this.vaoID = glGenVertexArrays();
        this.type = type;
        this.drawingType = drawingType;
        this.attributeInfoList = attributeInfoList;
        this.vertexDataSize = attributeInfoList.stream().map(VBOAttributeInfo::size).reduce(0, Integer::sum);
        this.vboStrideBytes = this.vertexDataSize * Float.BYTES;
        this.batches = new ArrayList<>();
        this.batchSize = 100;
    }

    abstract protected Batch createBatchFromGraphic(G graphic);

    public void update() {
        glBindVertexArray(this.vaoID);
        for (Batch batch : batches) {
            batch.update();
        }
        glBindVertexArray(0);
    }

    public void addGraphic(G newGraphic) {
        List<V> vertexList = newGraphic.getVertexList();
        for (V newVertex : vertexList) {
            boolean newVertexAllocated = false;
            int batchIndex = 0;
            while (batchIndex < batches.size()) {
                if (batches.get(batchIndex).canReceiveVertexFrom(newGraphic)) {
                    batches.get(batchIndex).addVertex(newVertex);
                    newVertexAllocated = true;
                    break;
                }
                batchIndex++;
            }
            if (!newVertexAllocated) {
                Batch newBatch = createBatchFromGraphic(newGraphic);
                newBatch.addVertex(newVertex);
                batches.add(newBatch);
            }
        }
    }

    public abstract class Batch {

        protected int vboID;

        protected ArrayList<V> vertices;
        @Getter
        protected Shader shader;
        protected ByteBuffer dataBuffer;
        private boolean dataHasChangedFlag;

        protected Batch(Shader shader) {
            this.vboID = glGenBuffers();
            this.vertices = new ArrayList<>(batchSize);
            this.shader = shader;
            this.dataBuffer = BufferUtils.createByteBuffer(batchSize * vboStrideBytes);
            glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
            glBufferData(GL_ARRAY_BUFFER, dataBuffer, drawingType);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            this.dataHasChangedFlag = false;
        }

        public void addVertex(V newVertex) {
            assert vertices.size() < batchSize : "Can't add vertex data to the batch";
            assert !vertices.contains(newVertex) : "vertex already in batch";
            vertices.add(newVertex);
            newVertex.setDataHasChanged();
        }

        abstract protected boolean canReceiveVertexFrom(G graphic);

        protected void setupVertexAttributes(){
            glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
            int pointerValue = 0;
            for(int i = 0; i < attributeInfoList.size(); i++){
                VBOAttributeInfo info = attributeInfoList.get(i);
                if(info.type() == GL_FLOAT) {
                    glVertexAttribPointer(i, info.size(), info.type(), false, vboStrideBytes, (long) pointerValue * Float.BYTES);
                }
                else if(info.type() == GL_INT){
                    glVertexAttribIPointer(i, info.size(), info.type(), vboStrideBytes, (long) pointerValue * Float.BYTES);
                }
                else{
                    assert false: "incorrect VBO attribute info type";
                }
                pointerValue += info.size();
            }
            glBindBuffer(GL_ARRAY_BUFFER, 0);
        }

        private void uploadData(){
            dataBuffer.clear();
            for (int i = 0; i < vertices.size(); i++) {
                sendToBuffer(i);
            }
            dataBuffer.flip();
            glBindBuffer(GL_ARRAY_BUFFER, this.vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, dataBuffer);
            glBindBuffer(GL_ARRAY_BUFFER, 0);
            dataBuffer.flip();
        }

        abstract protected void sendToBuffer(int vertexIndex);

        abstract protected void draw();

        public void removeVertex(int vertexToRemoveIndex) {
            assert vertexToRemoveIndex < vertices.size() : "index out of bounds";
            vertices.remove(vertexToRemoveIndex);
        }

        public void cleanupVertices() {
            int i = 0;
            while (i < vertices.size()) {
                V vertex = vertices.get(i);
                if (vertex.getShouldBeRemoved()) {
                    removeVertex(i);
                    dataHasChangedFlag = true;
                }
                else {
                    i++;
                }
            }
        }

        public void update() {
            this.cleanupVertices();
            this.setupVertexAttributes();
            this.shader.use();
            for (var vertex : vertices) {
                if (vertex.getDataHasChanged()) {
                    dataHasChangedFlag = true;
                    break;
                }
            }
            if(dataHasChangedFlag){
                this.uploadData();
                vertices.forEach(V::resetDataHasChanged);
                dataHasChangedFlag = false;
            }
            this.draw();
        }
    }
}