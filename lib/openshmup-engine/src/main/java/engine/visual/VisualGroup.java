package engine.visual;

import engine.graphics.Graphic;
import layer.LayerEntry;
import layer.LayerMap;
import lombok.Getter;
import types.Vec2D;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

final public class VisualGroup extends Visual {
    @Getter
    final private LayerMap<Visual> visualsMap;

    final private HashMap<Visual, Vec2D> positionsMap;
    @Getter
    final private HashMap<Visual, Integer> graphicalOffsetsMap;

    private final Visual referenceVisual;

    public VisualGroup(LayerMap<Visual> visuals) {
        super(Vec2D.ONE, Vec2D.ZERO);
        assert !visuals.isEmpty() : "visual map is empty";
        this.visualsMap = visuals;

        //fusing the visual layers while preserving correct graphical layer order
        this.graphicalOffsetsMap = new HashMap<>();
        int maxLayerToMerge = visuals.getMaxLayer();
        int layerToMergeIndex = 0;
        int layerOffset = 0;
        while (layerToMergeIndex <= maxLayerToMerge) {
            ArrayList<Visual> layerToMerge = visuals.getLayer(layerToMergeIndex);
            if (layerToMerge != null) {
                int currentMaxLayer = 0;
                for (Visual visual : layerToMerge) {
                    if (!visual.getGraphicalLayers().isEmpty()) {
                        int visualMaxLayer = visual.getGraphicalLayers().getMaxLayer();
                        if (visualMaxLayer > currentMaxLayer) {
                            currentMaxLayer = visualMaxLayer;
                        }
                        this.graphicalLayers.addLayers(visual.getGraphicalLayers(), layerOffset);
                    }
                    graphicalOffsetsMap.put(visual, layerOffset);
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

    public VisualGroup(Map<Visual, Integer> visuals) {
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
        visualsMap.forEachObject(Visual::init);
    }
    @Override
    public void update() {
        visualsMap.forEachObject(Visual::update);
        visualsMap.forEachObject(visual -> {
            ArrayList<Graphic<?>> graphicsToRemove = visual.getGraphicsToRemove();
            if (!graphicsToRemove.isEmpty()) {
                for (var graphicToRemove : graphicsToRemove) {
                    graphicalLayers.remove(graphicToRemove);
                }
                this.graphicsToRemove.addAll(graphicsToRemove);
                graphicsToRemove.clear();
            }
            ArrayList<LayerEntry<Graphic<?>>> graphicsToAdd = visual.getGraphicsToAdd();
            if (!graphicsToAdd.isEmpty()) {
                int offset = this.graphicalOffsetsMap.get(visual);
                for (var entry : graphicsToAdd) {
                    graphicalLayers.add(entry.object(), offset + entry.layer());
                    this.graphicsToAdd.add(new LayerEntry<>(entry.object(), offset + entry.layer()));
                }
                graphicsToAdd.clear();
            }
        });
    }
    @Override
    public Visual copy() {
        LayerMap<Visual> copiesMap = new LayerMap<>();
        this.visualsMap.forEachObject((visual, layer) -> copiesMap.add(visual.copy(), layer));
        return new VisualGroup(copiesMap);
    }
    @Override
    public void updateGraphicsColor() {
        visualsMap.forEachObject(Visual::updateGraphicsColor);
    }
}
