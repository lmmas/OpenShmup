package engine.scene.visual;

import engine.Engine;
import engine.Game;
import engine.assets.Texture;
import engine.graphics.image.ImageGraphic;
import engine.scene.visual.style.TimeReference;
import engine.types.RGBAValue;

import java.util.ArrayList;
import java.util.List;

final public class Animation extends SceneVisual {

    final private ImageGraphic imageGraphic;

    final private SpritesheetInfo info;

    final private boolean looping;

    private final float framePeriodSeconds;

    private int frameIndex;

    private double timeOfLastFrame;

    private TimeReference timeReference;

    public Animation(int layer, Texture animationTexture, SpritesheetInfo info, float framePeriodSeconds, boolean looping, float sizeX, float sizeY, TimeReference timeReference) {
        super(layer, new ArrayList<>(1), List.of(0));
        this.info = info;
        this.framePeriodSeconds = framePeriodSeconds;
        this.looping = looping;
        this.frameIndex = 0;
        this.imageGraphic = new ImageGraphic(animationTexture, true,
            sizeX, sizeY,
            0.0f, 0.0f,
            info.frameSizeX(), info.frameSizeY(),
            0.0f, 0.0f,
            1.0f, 1.0f, 1.0f, 1.0f,
            0.0f, 0.0f, 0.0f, 0.0f);
        updateTexturePosition();
        graphicsList.add(imageGraphic);
        this.timeReference = timeReference;
    }

    public Animation(Animation animation) {
        super(animation.sceneLayerIndex, new ArrayList<>(1), List.of(0));
        this.imageGraphic = new ImageGraphic(animation.imageGraphic);
        this.graphicsList.add(imageGraphic);
        this.info = animation.info;
        this.looping = animation.looping;
        this.framePeriodSeconds = animation.framePeriodSeconds;
        this.frameIndex = animation.frameIndex;
        this.timeOfLastFrame = animation.timeOfLastFrame;
        this.timeReference = animation.timeReference;
    }

    private void updateTexturePosition() {
        float texturePositionX = frameIndex * info.strideX() + info.startPosX();
        float texturePositionY = frameIndex * info.strideY() + info.startPosY();
        imageGraphic.setTexturePosition(texturePositionX, texturePositionY);
    }

    @Override
    public Animation copy() {
        return new Animation(this);
    }

    @Override
    public void initDisplay() {
        if (this.timeReference == TimeReference.LEVEL) {
            this.timeOfLastFrame = Game.getLevelTime();
        }
        else {
            this.timeOfLastFrame = Engine.getSceneTime();
        }
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

    @Override
    public void updateGraphicColor(RGBAValue colorCoefs, RGBAValue addedColor) {
        imageGraphic.setColorCoefs(colorCoefs.r, colorCoefs.g, colorCoefs.g, colorCoefs.a);
        imageGraphic.setAddedColor(addedColor.r, addedColor.g, addedColor.b, addedColor.a);
    }
}
