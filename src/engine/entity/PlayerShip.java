package engine.entity;

import engine.visual.EntitySprite;
import engine.visual.SimpleSprite;

import static org.lwjgl.glfw.GLFW.*;

final public class PlayerShip {
    private EntitySprite sprite = new SimpleSprite("resources/textures/gunther-2.png",4);
    private float positionX = 0.5f;
    private float positionY = 0.05f;
    private float sizeX = 0.05f;
    private float sizeY = 0.1f;
    private float speed = 0.6f;

    public PlayerShip(){
        sprite.setPosition(positionX, positionY);
        sprite.setSize(sizeX, sizeY);
    }

    public EntitySprite getSprite() {
        return sprite;
    }

    public void actionsAndMoves(long window, float deltaTime){
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
    }
    public void handleCollisions(){

    }
}
