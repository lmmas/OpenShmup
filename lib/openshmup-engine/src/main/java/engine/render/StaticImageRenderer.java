package engine.render;

import static org.lwjgl.opengl.GL33.*;

public class StaticImageRenderer extends Image2DRenderer {
    public StaticImageRenderer(){
        super(GraphicType.STATIC_IMAGE, GL_STATIC_DRAW);
    }
}
