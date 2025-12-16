package engine.scene;

import engine.visual.SceneVisual;

import java.util.ArrayList;

public class SceneLayer {
    final private ArrayList<SceneVisual> visuals;
    private int graphicalSubLayerCount;

    public SceneLayer() {
        this.visuals = new ArrayList<>();
        this.graphicalSubLayerCount = 0;
    }

    public int getGraphicalSubLayerCount() {
        return graphicalSubLayerCount;
    }

    public void setGraphicalSubLayerCount(int graphicalSubLayerCount) {
        this.graphicalSubLayerCount = graphicalSubLayerCount;
    }

    public ArrayList<SceneVisual> getVisuals() {
        return visuals;
    }

}
