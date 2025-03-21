package engine.graphics;

import engine.entity.EntitySprite;
import engine.scene.Scene;
import engine.scene.visual.SceneVisual;

public class Animation implements EntitySprite, SceneVisual {
    final private MovingImage image;
    final private AnimationInfo info;
    final private boolean looping;
    private float framePeriodSeconds;
    private int frameIndex;
    private float timeOfLastFrame;

    public Animation(MovingImage image, AnimationInfo info, boolean looping, float framePeriodSeconds) {
        this.image = image;
        this.info = info;
        this.looping = looping;
        this.framePeriodSeconds = framePeriodSeconds;
        this.frameIndex = 0;
        this.timeOfLastFrame = 0.0f;
    }

    public Animation(int layer, AnimationInfo info, float framePeriodSeconds, boolean looping) {
        this.info = info;
        this.framePeriodSeconds = framePeriodSeconds;
        this.looping = looping;
        this.frameIndex = 0;
        this.image = new MovingImage(info.filepath(), layer);
        image.setTextureSize(info.frameSizeX(), info.frameSizeY());
        updateTexturePosition();
        this.timeOfLastFrame = 0.0f;
    }

    private void updateTexturePosition(){
        float texturePositionX = frameIndex * info.strideX() + info.startPosX();
        float texturePositionY = frameIndex * info.strideY() + info.startPosY();
        image.setTexturePosition(texturePositionX, texturePositionY);
    }

    @Override
    public void setPosition(float positionX, float positionY) {
        image.setPosition(positionX, positionY);
    }

    @Override
    public void setSize(float sizeX, float sizeY) {
        image.setSize(sizeX, sizeY);
    }

    @Override
    public void setOrientation(float orientation) {
        image.setOrientation(orientation);
    }

    @Override
    public Graphic<?, ?> getGraphic() {
        return image;
    }

    @Override
    public SceneVisual copy() {
        return new Animation(image.copy(), info, looping, framePeriodSeconds);
    }

    @Override
    public Graphic<?, ?>[] getGraphics() {
        return new Graphic[]{image};
    }

    @Override
    public void setScene(Scene scene) {
        this.timeOfLastFrame = scene.getSceneTimeSeconds();
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
}
