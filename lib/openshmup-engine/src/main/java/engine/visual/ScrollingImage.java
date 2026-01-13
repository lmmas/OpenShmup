package engine.visual;

import engine.Engine;
import engine.assets.Texture;
import engine.graphics.Graphic;
import engine.graphics.image.Image;
import engine.types.Vec2D;

import java.util.List;

final public class ScrollingImage extends SceneVisual {

    private final Image image1;

    final private Vec2D position1;

    private final Image image2;

    final private Vec2D position2;

    final private Vec2D size;

    boolean horizontalScrolling;

    private final float speed;

    private double lastUpdateTimeSeconds;

    public ScrollingImage(Texture texture, int layer, float sizeX, float sizeY, float speed, boolean horizontalScrolling) {
        super(layer, List.of(0, 0));
        this.size = new Vec2D(sizeX, sizeY);
        this.position1 = new Vec2D((float) Engine.getNativeWidth() / 2, (float) Engine.getNativeHeight() / 2);
        this.position2 = new Vec2D(0.0f, 0.0f);
        this.image1 = new Image(texture, true,
            sizeX, sizeY,
            position1.x, position1.y,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 0.0f);
        this.image2 = new Image(texture, true,
            sizeX, sizeY,
            position2.x, position2.y,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 0.0f);
        this.speed = speed;
        this.horizontalScrolling = horizontalScrolling;
        lastUpdateTimeSeconds = 0.0f;
        this.setPosition((float) Engine.getNativeWidth() / 2, (float) Engine.getNativeHeight() / 2);
    }

    public ScrollingImage(ScrollingImage scrollingImage) {
        super(scrollingImage.sceneLayerIndex, List.of(0, 0));
        this.image1 = new Image(scrollingImage.image1);
        this.position1 = new Vec2D(scrollingImage.position1);
        this.position2 = new Vec2D(scrollingImage.position2);
        this.image2 = new Image(scrollingImage.image2);
        this.size = new Vec2D(scrollingImage.size);
        this.horizontalScrolling = scrollingImage.horizontalScrolling;
        this.speed = scrollingImage.speed;
        this.lastUpdateTimeSeconds = scrollingImage.lastUpdateTimeSeconds;
    }

    @Override
    public SceneVisual copy() {
        return new ScrollingImage(this);
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of(image1, image2);
    }

    @Override
    public void setPosition(float positionX, float positionY) {
        position1.x = positionX;
        position1.y = positionY;
        if (horizontalScrolling) {
            this.position2.x = this.position1.x - Math.signum(speed) * size.x;
            this.position2.y = position1.y;
        }
        else {
            this.position2.x = position1.x;
            this.position2.y = position1.y - Math.signum(speed) * size.y;
        }
        image1.setPosition(position1.x, position1.y);
        image2.setPosition(position2.x, position2.y);
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        this.size.x = scaleX;
        this.size.y = scaleY;
        image1.setScale(scaleX, scaleY);
        image2.setScale(scaleX, scaleY);
    }

    @Override
    public void initDisplay(double startingTimeSeconds) {
        this.lastUpdateTimeSeconds = startingTimeSeconds;
    }

    @Override
    public void update(double currentTimeSeconds) {
        double deltaTime = currentTimeSeconds - lastUpdateTimeSeconds;
        if (horizontalScrolling) {
            position1.x += (float) (speed * deltaTime);
            position2.x += (float) (speed * deltaTime);
            float screenWidth = (float) Engine.getNativeWidth();
            if (position1.x > screenWidth / 2 + size.x) {
                position1.x -= 2 * size.x;
            }
            if (position1.x < screenWidth / 2 - size.x) {
                position1.x += 2 * size.x;
            }
            if (position2.x > screenWidth / 2 + size.x) {
                position2.x -= 2 * size.x;
            }
            if (position2.x < screenWidth / 2 - size.x) {
                position2.x += 2 * size.x;
            }
        }
        else {
            float screenHeight = (float) Engine.getNativeHeight();
            position1.y += speed * deltaTime;
            position2.y += speed * deltaTime;
            if (position1.y > screenHeight / 2 + size.y) {
                position1.y -= 2 * size.y;
            }
            if (position1.y < screenHeight / 2 - size.y) {
                position1.y += 2 * size.y;
            }
            if (position2.y > screenHeight / 2 + size.y) {
                position2.y -= 2 * size.y;
            }
            if (position2.y < screenHeight / 2 - size.y) {
                position2.y += 2 * size.y;
            }
        }
        image1.setPosition(position1.x, position1.y);
        image2.setPosition(position2.x, position2.y);
        lastUpdateTimeSeconds = currentTimeSeconds;
    }
}
