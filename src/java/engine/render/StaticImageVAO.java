package engine.render;

import static org.lwjgl.opengl.GL33.*;

public class StaticImageVAO extends Image2DVAO {
    public StaticImageVAO(){
        super(RenderType.STATIC_IMAGE, GL_STATIC_DRAW);
    }
}
