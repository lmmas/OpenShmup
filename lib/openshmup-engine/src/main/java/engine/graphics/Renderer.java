package engine.graphics;

import engine.assets.Shader;
import lombok.Getter;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL33.*;

public abstract class Renderer<G extends Graphic<G, V>, V extends Graphic<G, V>.Vertex> {

    @Getter
    protected int vaoID;
    @Getter
    protected RenderType type;

    final protected int drawingType;

    final protected int vboStrideBytes;

    protected int batchSize;

    protected ArrayList<Batch> batches;

    public Renderer(RenderType type, int drawingType, int vboStrideBytes) {
        this.vaoID = glGenVertexArrays();
        this.type = type;
        this.drawingType = drawingType;
        this.vboStrideBytes = vboStrideBytes;
        this.batches = new ArrayList<>();
    }

    abstract protected Batch createBatchFromGraphic(G graphic);

    protected void addNewBatchFromGraphic(G graphic) {
        Batch newBatch = createBatchFromGraphic(graphic);
        batches.add(newBatch);
    }

    public void draw() {
        glBindVertexArray(this.vaoID);
        for (Batch batch : batches) {
            batch.update();
        }
        glBindVertexArray(0);
    }

    public void addGraphic(G newGraphic) {
        int vertexCount = newGraphic.getVertexCount();
        for (int vertexIndex = 0; vertexIndex < vertexCount; vertexIndex++) {
            V newVertex = newGraphic.getVertex(vertexIndex);
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
                addNewBatchFromGraphic(newGraphic);
                batches.getLast().addVertex(newVertex);
            }
        }
    }

    public abstract class Batch {

        protected int vboID;

        protected ArrayList<V> vertices = new ArrayList<>(batchSize);
        @Getter
        protected Shader shader;

        protected Batch(Shader shader) {
            vboID = glGenBuffers();
            this.shader = shader;
        }

        public void addVertex(V newVertex) {
            assert vertices.size() < batchSize : "Can't add vertex data to the batch";
            assert !vertices.contains(newVertex) : "vertex already in batch";
            vertices.add(newVertex);
            newVertex.dataHasChanged();
        }

        abstract protected boolean canReceiveVertexFrom(G graphic);

        abstract protected void setupVertexAttributes();

        abstract protected void uploadData();

        abstract protected void draw();

        public void dataHasChanged() {
        }

        abstract public void removeVertex(int vertexToRemoveIndex);

        public void cleanupVertices() {
            int i = 0;
            while (i < vertices.size()) {
                V vertex = vertices.get(i);
                if (vertex.getShouldBeRemoved()) {
                    vertex.hasBeenRemoved();
                    removeVertex(i);
                    dataHasChanged();
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
                if (vertex.getDataHasChangedFlag()) {
                    this.uploadData();
                    vertices.forEach(V::resetDataHasChangedFlag);
                    break;
                }
            }
            this.draw();
        }
    }
}