package engine.scene.visual.style;

import engine.scene.visual.SceneVisual;
import lombok.Getter;
import types.LayerMap;

import java.util.Map;

final public class GroupVisual extends SceneVisual {
    @Getter
    final private LayerMap<SceneVisual> visualsMap;

    public GroupVisual(LayerMap<SceneVisual> visuals) {
        this.visualsMap = visuals;
        visuals.forEachObject((visual, visualLayer) -> this.graphicalLayers.addLayers(visual.getGraphicalLayers(), visualLayer));
    }

    public GroupVisual(Map<SceneVisual, Integer> visuals) {
        this(new LayerMap<>(visuals));
    }
    @Override
    public SceneVisual copy() {
        LayerMap<SceneVisual> copiesMap = new LayerMap<>();
        this.visualsMap.forEachObject((visual, layer) -> copiesMap.add(visual.copy(), layer));
        return new GroupVisual(copiesMap);
    }
    @Override
    public void updateGraphicsColor() {
        visualsMap.forEachObject(SceneVisual::updateGraphicsColor);
    }
}
