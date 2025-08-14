package engine.graphics;

import engine.render.RenderInfo;
import engine.assets.Texture;
import engine.scene.Scene;
import engine.scene.display.SceneDisplay;

import java.util.*;

public class Animation implements SceneDisplay {
    final private Image2D image;
    final private AnimationInfo info;
    final private boolean looping;
    private final float framePeriodSeconds;
    private int frameIndex;
    private float timeOfLastFrame;

    public Animation(int layer, Texture animationTexture, AnimationInfo info, float framePeriodSeconds, boolean looping, float sizeX, float sizeY) {
        this.info = info;
        this.framePeriodSeconds = framePeriodSeconds;
        this.looping = looping;
        this.frameIndex = 0;
        this.image = new Image2D(animationTexture, layer, true, sizeX, sizeY);
        this.image.setTextureSize(info.frameSizeX(), info.frameSizeY());
        updateTexturePosition();
        this.timeOfLastFrame = 0.0f;
    }

    public Animation(Animation animation){
        this.image = animation.image.copy();
        this.info = animation.info;
        this.looping = animation.looping;
        this.framePeriodSeconds = animation.framePeriodSeconds;
        this.frameIndex = animation.frameIndex;
        this.timeOfLastFrame = animation.timeOfLastFrame;
    }
    private void updateTexturePosition(){
        float texturePositionX = frameIndex * info.strideX() + info.startPosX();
        float texturePositionY = frameIndex * info.strideY() + info.startPosY();
        image.setTexturePosition(texturePositionX, texturePositionY);
    }

    @Override
    public Animation copy() {
        return new Animation(this);
    }

    @Override
    public Optional<RenderInfo> getRenderInfo() {
        return Optional.of(image.getRenderInfo());
    }

    @Override
    public void setPosition(float positionX, float positionY) {
        image.setPosition(positionX, positionY);
    }

    public void setSize(float sizeX, float sizeY) {
        image.setSize(sizeX, sizeY);
    }


    public Image2D getImage() {
        return image;
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of(image);
    }

    @Override
    public Optional<Texture> getTexture() {
        return Optional.of(image.getTexture());
    }

    @Override
    public void initDisplay(float startingTimeSeconds) {
        this.timeOfLastFrame = startingTimeSeconds;
    }

    @Override
    public void update(float currentTimeSeconds) {
        if(currentTimeSeconds >= timeOfLastFrame + framePeriodSeconds){
            frameIndex++;
            if(frameIndex >= info.frameCount()){
                if(looping){
                    frameIndex = 0;
                }
                else{
                    return;
                }
            }
            updateTexturePosition();
            timeOfLastFrame += framePeriodSeconds;
        }
    }

    @Override
    public boolean shouldBeRemoved() {
        return !looping && frameIndex >= info.frameCount();
    }
}
