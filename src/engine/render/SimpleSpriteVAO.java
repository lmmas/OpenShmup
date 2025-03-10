package engine.render;
import static org.lwjgl.opengl.GL33.*;

public class SimpleSpriteVAO extends Image2DVAO {
    public SimpleSpriteVAO(){
        super(RenderType.SIMPLE_SPRITE, GL_STREAM_DRAW);
    }

}
