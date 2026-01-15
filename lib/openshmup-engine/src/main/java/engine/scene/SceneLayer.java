package engine.scene;

import engine.scene.visual.SceneVisual;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
public class SceneLayer {

    final private ArrayList<SceneVisual> visuals;
    @Setter
    private int graphicalSubLayerCount;

    public SceneLayer() {
        this.visuals = new ArrayList<>();
        this.graphicalSubLayerCount = 0;
    }

}
