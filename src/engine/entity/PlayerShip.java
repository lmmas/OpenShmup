package engine.entity;

import engine.graphics.EntitySprite;
import engine.graphics.SimpleSprite;
import engine.scene.LevelScene;

import static org.lwjgl.glfw.GLFW.*;

final public class PlayerShip {
    private LevelScene scene;
    private EntitySprite sprite;
    private float positionX = 0.5f;
    private float positionY = 0.05f;
    private float sizeX = 0.05f;
    private float sizeY = 0.1f;
    private float speed = 0.6f;
    private float lastUpdateTime;

    public PlayerShip(LevelScene scene){
        this.scene = scene;
        this.sprite = new SimpleSprite("resources/textures/Spaceship Pack/ship_5.png", scene, 4);
        sprite.setPosition(positionX, positionY);
        sprite.setSize(sizeX, sizeY);
    }

    public EntitySprite getSprite() {
        return sprite;
    }

    public void actionsAndMoves(long window){
        float currentTime = scene.getLastUpdateTime();
        float deltaTime = currentTime - lastUpdateTime;
        if (glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS) {
            positionX-=speed * deltaTime;
        }
        if (glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS) {
            positionX+=speed * deltaTime;
        }
        if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
            positionY+=speed * deltaTime;
        }
        if (glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) {
            positionY-=speed * deltaTime;
        }
        if(positionX < 0.0f){
            positionX = 0.0f;
        }
        if(positionX > 1.0f){
            positionX = 1.0f;
        }
        if(positionY < 0.0f){
            positionY = 0.0f;
        }
        if(positionY > 1.0f){
            positionY = 1.0f;
        }
        sprite.setPosition(positionX, positionY);
        lastUpdateTime = currentTime;
    }
    public void handleCollisions(){

    }
}
