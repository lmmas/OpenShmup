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

    public ArrayList<SceneVisual> getVisuals() {
        return visuals;
    }

    public void addVisual(SceneVisual newVisual) {
        visuals.add(newVisual);
        int newVisualMaxGraphicalSubLayer = newVisual.getMaxGraphicalSubLayer();
        if (newVisualMaxGraphicalSubLayer >= graphicalSubLayerCount) {
            graphicalSubLayerCount = newVisualMaxGraphicalSubLayer + 1;
        }
    }


}
