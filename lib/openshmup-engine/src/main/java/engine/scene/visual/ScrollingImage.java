package engine.scene.visual;

import engine.Engine;
import engine.Game;
import engine.assets.Texture;
import engine.graphics.image.ImageGraphic;
import engine.scene.visual.style.TimeReference;
import engine.types.RGBAValue;
import engine.types.Vec2D;

import java.util.ArrayList;
import java.util.List;

final public class ScrollingImage extends SceneVisual {

    private final ImageGraphic imageGraphic1;

    private Vec2D position1;

    private final ImageGraphic imageGraphic2;

    private Vec2D position2;

    private Vec2D size;

    boolean horizontalScrolling;

    private float speed;

    private double lastUpdateTimeSeconds;

    final private TimeReference timeReference;

    public ScrollingImage(Texture texture, int layer, Vec2D size, float speed, boolean horizontalScrolling, TimeReference timeReference) {
        super(layer, new ArrayList<>(2), List.of(0, 0));
        this.size = new Vec2D(size);
        this.position1 = new Vec2D((float) Engine.getNativeWidth() / 2, (float) Engine.getNativeHeight() / 2);
        this.position2 = new Vec2D(0.0f, 0.0f);
        this.imageGraphic1 = new ImageGraphic(texture, true,
            size,
            position1,
            Vec2D.ONE,
            Vec2D.ZERO,
            RGBAValue.ONES,
            RGBAValue.ZEROES);
        graphicsList.add(imageGraphic1);
        this.imageGraphic2 = new ImageGraphic(texture, true,
            size,
            position2,
            Vec2D.ONE,
            Vec2D.ZERO,
            RGBAValue.ONES,
            RGBAValue.ZEROES);
        graphicsList.add(imageGraphic2);
        this.speed = speed;
        this.horizontalScrolling = horizontalScrolling;
        this.timeReference = timeReference;
        lastUpdateTimeSeconds = 0.0f;
    }

    public ScrollingImage(ScrollingImage scrollingImage) {
        super(scrollingImage.sceneLayerIndex, new ArrayList<>(2), List.of(0, 0));
        this.imageGraphic1 = new ImageGraphic(scrollingImage.imageGraphic1);
        graphicsList.add(imageGraphic1);
        this.position1 = new Vec2D(scrollingImage.position1);
        this.position2 = new Vec2D(scrollingImage.position2);
        this.imageGraphic2 = new ImageGraphic(scrollingImage.imageGraphic2);
        graphicsList.add(imageGraphic2);
        this.size = new Vec2D(scrollingImage.size);
        this.horizontalScrolling = scrollingImage.horizontalScrolling;
        this.speed = scrollingImage.speed;
        this.timeReference = scrollingImage.timeReference;
        this.lastUpdateTimeSeconds = scrollingImage.lastUpdateTimeSeconds;
    }

    @Override
    public SceneVisual copy() {
        return new ScrollingImage(this);
    }

    @Override
    public void setPosition(Vec2D position) {
        position1 = position;
        if (horizontalScrolling) {
            this.position2 = new Vec2D(this.position1.x - Math.signum(speed) * size.x, position1.y);
        }
        else {
            this.position2 = new Vec2D(position1.x, position1.y - Math.signum(speed) * size.y);
        }
        imageGraphic1.setPosition(position1);
        imageGraphic2.setPosition(position2);
    }

    @Override
    public void setScale(Vec2D scale) {
        this.size = scale;
        imageGraphic1.setScale(scale);
        imageGraphic2.setScale(scale);
    }

    @Override
    public void init() {
        if (this.timeReference == TimeReference.LEVEL) {
            this.lastUpdateTimeSeconds = Game.getLevelTime();
        }
        else {
            this.lastUpdateTimeSeconds = Engine.getSceneTime();
        }
        this.setPosition(Engine.getNativeResolution().scalar(0.5f));
    }

    @Override
    public void update() {
        double currentTimeSeconds;
        if (timeReference == TimeReference.SCENE) {
            currentTimeSeconds = Engine.getSceneTime();
        }
        else {
            currentTimeSeconds = Game.getLevelTime();
        }
        double deltaTime = currentTimeSeconds - lastUpdateTimeSeconds;
        if (horizontalScrolling) {
            float newPosition1X = position1.x + (float) (speed * deltaTime);
            float newPosition2X = position2.x + (float) (speed * deltaTime);
            float screenWidth = (float) Engine.getNativeWidth();
            if (position1.x > screenWidth / 2 + size.x) {
                newPosition1X -= 2 * size.x;
            }
            if (position1.x < screenWidth / 2 - size.x) {
                newPosition1X += 2 * size.x;
            }
            if (position2.x > screenWidth / 2 + size.x) {
                newPosition2X -= 2 * size.x;
            }
            if (position2.x < screenWidth / 2 - size.x) {
                newPosition2X += 2 * size.x;
            }
            position1 = new Vec2D(newPosition1X, position1.y);
            position2 = new Vec2D(newPosition2X, position2.y);
        }
        else {
            float screenHeight = (float) Engine.getNativeHeight();
            float newPosition1Y = position1.y + (float) (speed * deltaTime);
            float newPosition2Y = position2.y + (float) (speed * deltaTime);
            if (position1.y > screenHeight / 2 + size.y) {
                newPosition1Y -= 2 * size.y;
            }
            if (position1.y < screenHeight / 2 - size.y) {
                newPosition1Y += 2 * size.y;
            }
            if (position2.y > screenHeight / 2 + size.y) {
                newPosition2Y -= 2 * size.y;
            }
            if (position2.y < screenHeight / 2 - size.y) {
                newPosition2Y += 2 * size.y;
            }
            position1 = new Vec2D(position1.x, newPosition1Y);
            position2 = new Vec2D(position2.x, newPosition2Y);
        }
        imageGraphic1.setPosition(position1);
        imageGraphic2.setPosition(position2);
        lastUpdateTimeSeconds = currentTimeSeconds;
    }

    @Override public void updateGraphicsColor() {
        imageGraphic1.setColorCoefs(colorCoefs);
        imageGraphic2.setColorCoefs(colorCoefs);
        imageGraphic1.setAddedColor(addedColor);
        imageGraphic2.setAddedColor(addedColor);
    }
}
