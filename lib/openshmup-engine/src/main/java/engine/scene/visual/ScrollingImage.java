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

    final private Vec2D position1;

    private final ImageGraphic imageGraphic2;

    final private Vec2D position2;

    final private Vec2D size;

    boolean horizontalScrolling;

    private final float speed;

    private double lastUpdateTimeSeconds;

    final private TimeReference timeReference;

    public ScrollingImage(Texture texture, int layer, float sizeX, float sizeY, float speed, boolean horizontalScrolling, TimeReference timeReference) {
        super(layer, new ArrayList<>(2), List.of(0, 0));
        this.size = new Vec2D(sizeX, sizeY);
        this.position1 = new Vec2D((float) Engine.getNativeWidth() / 2, (float) Engine.getNativeHeight() / 2);
        this.position2 = new Vec2D(0.0f, 0.0f);
        this.imageGraphic1 = new ImageGraphic(texture, true,
            sizeX, sizeY,
            position1.x, position1.y,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 0.0f);
        graphicsList.add(imageGraphic1);
        this.imageGraphic2 = new ImageGraphic(texture, true,
            sizeX, sizeY,
            position2.x, position2.y,
            1.0f, 1.0f,
            0.0f, 0.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 0.0f);
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
        imageGraphic1.setPosition(position1.x, position1.y);
        imageGraphic2.setPosition(position2.x, position2.y);
    }

    @Override
    public void setScale(float scaleX, float scaleY) {
        this.size.x = scaleX;
        this.size.y = scaleY;
        imageGraphic1.setScale(scaleX, scaleY);
        imageGraphic2.setScale(scaleX, scaleY);
    }

    @Override
    public void initDisplay() {
        if (this.timeReference == TimeReference.LEVEL) {
            this.lastUpdateTimeSeconds = Game.getLevelTime();
        }
        else {
            this.lastUpdateTimeSeconds = Engine.getSceneTime();
        }
        this.setPosition((float) Engine.getNativeWidth() / 2, (float) Engine.getNativeHeight() / 2);
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
            position1.y += (float) (speed * deltaTime);
            position2.y += (float) (speed * deltaTime);
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
        imageGraphic1.setPosition(position1.x, position1.y);
        imageGraphic2.setPosition(position2.x, position2.y);
        lastUpdateTimeSeconds = currentTimeSeconds;
    }

    @Override public void updateGraphicColor(RGBAValue colorCoefs, RGBAValue addedColor) {
        imageGraphic1.setColorCoefs(colorCoefs.r, colorCoefs.g, colorCoefs.b, colorCoefs.a);
        imageGraphic2.setColorCoefs(colorCoefs.r, colorCoefs.g, colorCoefs.b, colorCoefs.a);
        imageGraphic1.setAddedColor(addedColor.r, addedColor.g, addedColor.b, addedColor.a);
        imageGraphic2.setAddedColor(addedColor.r, addedColor.g, addedColor.b, addedColor.a);
    }
}
