package engine.visual;

import engine.Engine;
import engine.Game;
import engine.assets.Texture;
import engine.graphics.Graphic;
import engine.graphics.image.Image;
import engine.visual.style.TimeReference;

import java.util.List;

final public class Animation extends SceneVisual {

    final private Image image;

    final private AnimationInfo info;

    final private boolean looping;

    private final float framePeriodSeconds;

    private int frameIndex;

    private double timeOfLastFrame;

    private TimeReference timeReference;

    public Animation(int layer, Texture animationTexture, AnimationInfo info, float framePeriodSeconds, boolean looping, float sizeX, float sizeY, TimeReference timeReference) {
        super(layer, List.of(0));
        this.info = info;
        this.framePeriodSeconds = framePeriodSeconds;
        this.looping = looping;
        this.frameIndex = 0;
        this.image = new Image(animationTexture, true,
            sizeX, sizeY,
            0.0f, 0.0f,
            info.frameSizeX(), info.frameSizeY(),
            0.0f, 0.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 0.0f);
        updateTexturePosition();
        this.timeOfLastFrame = 0.0d;
        this.timeReference = timeReference;
    }

    public Animation(Animation animation) {
        super(animation.sceneLayerIndex, List.of(0));
        this.image = new Image(animation.image);
        this.info = animation.info;
        this.looping = animation.looping;
        this.framePeriodSeconds = animation.framePeriodSeconds;
        this.frameIndex = animation.frameIndex;
        this.timeOfLastFrame = animation.timeOfLastFrame;
    }

    private void updateTexturePosition() {
        float texturePositionX = frameIndex * info.strideX() + info.startPosX();
        float texturePositionY = frameIndex * info.strideY() + info.startPosY();
        image.setTexturePosition(texturePositionX, texturePositionY);
    }

    @Override
    public Animation copy() {
        return new Animation(this);
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of(image);
    }

    @Override
    public void initDisplay(double startingTimeSeconds) {
        this.timeOfLastFrame = startingTimeSeconds;
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
        if (currentTimeSeconds >= timeOfLastFrame + framePeriodSeconds) {
            frameIndex++;
            if (frameIndex >= info.frameCount()) {
                if (looping) {
                    frameIndex = 0;
                }
                else {
                    this.setShouldBeRemoved();
                    return;
                }
            }
            updateTexturePosition();
            timeOfLastFrame += framePeriodSeconds;
        }
    }
}
