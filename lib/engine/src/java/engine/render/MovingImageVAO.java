package engine.render;
import static org.lwjgl.opengl.GL33.*;

public class MovingImageVAO extends Image2DVAO {
    public MovingImageVAO(){
        super(RenderType.MOVING_IMAGE, GL_STREAM_DRAW);
    }

}
