package engine.scene.visual.style;

import engine.scene.visual.SceneVisual;
import layer.LayerMap;
import lombok.Getter;
import types.Vec2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

final public class GroupVisual extends SceneVisual {
    @Getter
    final private LayerMap<SceneVisual> visualsMap;

    final private Map<SceneVisual, Vec2D> positionsMap;

    private final SceneVisual referenceVisual;

    public GroupVisual(LayerMap<SceneVisual> visuals) {
        super(Vec2D.ONE, Vec2D.ZERO);
        assert !visuals.isEmpty() : "visual map is empty";
        this.visualsMap = visuals;

        //fusing the visual layers while preserving correct graphical layer order
        int maxLayerToMerge = visuals.getMaxLayer();
        int layerToMergeIndex = 0;
        int layerOffset = 0;
        while (layerToMergeIndex <= maxLayerToMerge) {
            ArrayList<SceneVisual> layerToMerge = visuals.getLayer(layerToMergeIndex);
            if (layerToMerge != null) {
                int currentMaxLayer = 0;
                for (SceneVisual visual : layerToMerge) {
                    assert !visual.getGraphicalLayers().isEmpty() : "empty visual";
                    int visualMaxLayer = visual.getGraphicalLayers().getMaxLayer();
                    if (visualMaxLayer > currentMaxLayer) {
                        currentMaxLayer = visualMaxLayer;
                    }
                    this.graphicalLayers.addLayers(visual.getGraphicalLayers(), layerOffset);
                }
                layerOffset = layerOffset + currentMaxLayer + 1;
            }
            layerToMergeIndex++;
        }

        this.referenceVisual = visuals.getLayer(visuals.getFirstLayer()).getFirst();
        this.positionsMap = new HashMap<>();
        this.visualsMap.forEachObject((visual) -> {
            Vec2D relativePosition = visual.getPosition().subtract(referenceVisual.getPosition()); // position relative to the reference visual
            positionsMap.put(visual, relativePosition);
        });
    }

    public GroupVisual(Map<SceneVisual, Integer> visuals) {
        this(new LayerMap<>(visuals));
    }

    @Override
    public void setScale(Vec2D scale) {
        //void implementation for now, visual groups can't change scale
    }
    @Override
    public void setPosition(Vec2D position) {
        super.setPosition(position);
        visualsMap.forEachObject(visual -> visual.setPosition(position.add(positionsMap.get(visual))));
    }
    @Override
    public void init() {
        visualsMap.forEachObject(SceneVisual::init);
    }
    @Override
    public void update() {
        visualsMap.forEachObject(SceneVisual::update);
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
