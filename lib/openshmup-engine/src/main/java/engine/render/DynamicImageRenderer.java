package engine.render;
import static org.lwjgl.opengl.GL33.*;

public class DynamicImageRenderer extends Image2DRenderer {
    public DynamicImageRenderer(){
        super(GraphicType.DYNAMIC_IMAGE, GL_STREAM_DRAW);
    }

}
