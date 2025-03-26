package engine.render;
import static org.lwjgl.opengl.GL33.*;

public class DynamicImageVAO extends Image2DVAO {
    public DynamicImageVAO(){
        super(RenderType.DYNAMIC_IMAGE, GL_STREAM_DRAW);
    }

}
