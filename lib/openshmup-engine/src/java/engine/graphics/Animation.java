package engine.graphics;

import engine.entity.hitbox.EntitySprite;
import engine.scene.Scene;
import engine.scene.visual.SceneVisual;

public class Animation implements EntitySprite, SceneVisual {
    private Scene scene;
    final private MovingImage image;
    final private AnimationInfo info;
    final private boolean looping;
    private float framePeriodSeconds;
    private int frameIndex;
    private float timeOfLastFrame;

    public Animation(Scene scene, int layer, AnimationInfo info, float framePeriodSeconds, boolean looping) {
        this.scene = scene;
        this.info = info;
        this.framePeriodSeconds = framePeriodSeconds;
        this.looping = looping;
        this.frameIndex = 0;
        this.image = new MovingImage(info.filepath(), scene, layer);
        image.setTextureSize(info.frameSizeX(), info.frameSizeY());
        updateTexturePosition();
        this.timeOfLastFrame = scene.getSceneTime();
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
    public Graphic<?, ?>[] getGraphics() {
        return new Graphic[]{image};
    }

    @Override
    public void update() {
        float currentTime = scene.getSceneTime();
        if(currentTime >= timeOfLastFrame + framePeriodSeconds){
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
    public void delete() {
        image.delete();
    }
}
