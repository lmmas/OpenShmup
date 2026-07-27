package engine.graphics;

import static org.lwjgl.opengl.GL33.GL_FLOAT;
import static org.lwjgl.opengl.GL33.GL_INT;

public record VBOAttributeInfo(
    int type,
    int size
) {
    public static VBOAttributeInfo VEC2 = new VBOAttributeInfo(GL_FLOAT, 2);
    public static VBOAttributeInfo VEC4 = new VBOAttributeInfo(GL_FLOAT, 4);
    public static VBOAttributeInfo FLOAT = new VBOAttributeInfo(GL_FLOAT, 1);
    public static VBOAttributeInfo INT = new VBOAttributeInfo(GL_INT, 1);
}
