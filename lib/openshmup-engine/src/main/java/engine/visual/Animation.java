package engine.visual;

import engine.graphics.Graphic;
import engine.graphics.image.Image;
import engine.graphics.RenderInfo;
import engine.assets.Texture;

import java.util.*;

final public class Animation extends SceneVisual {
    final private Image image;
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
        this.image = new Image(animationTexture, layer, true, sizeX, sizeY);
        this.image.setTextureSize(info.frameSizeX(), info.frameSizeY());
        updateTexturePosition();
        this.timeOfLastFrame = 0.0f;
    }

    public Animation(Animation animation){
        this.image = new Image(animation.image);
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
    public List<RenderInfo> getRenderInfos() {
        return List.of(image.getRenderInfo());
    }

    @Override
    public List<Graphic<?, ?>> getGraphics() {
        return List.of(image);
    }

    @Override
    public List<Texture> getTextures() {
        return List.of(image.getTexture());
    }

    @Override
    public void setPosition(float positionX, float positionY) {
        image.setPosition(positionX, positionY);
    }

    @Override
    public void setScale(float sizeX, float sizeY) {
        image.setScale(sizeX, sizeY);
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
                    this.setShouldBeRemoved();
                    return;
                }
            }
            updateTexturePosition();
            timeOfLastFrame += framePeriodSeconds;
        }
    }
}
