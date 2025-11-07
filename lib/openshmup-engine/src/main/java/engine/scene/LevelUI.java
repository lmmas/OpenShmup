package engine.scene;

import engine.gameData.GameConfig;
import engine.entity.Ship;
import engine.graphics.image.Image;
import engine.visual.ImageDisplay;
import engine.types.Vec2D;

import java.util.ArrayList;

import static engine.Application.assetManager;

public class LevelUI {
    final private LevelScene scene;
    final private ArrayList<ImageDisplay> playerLives;
    private Ship playerShip;
    final private GameConfig.LevelUI config;
    public LevelUI(LevelScene scene){
        this.scene = scene;
        this.playerLives = new ArrayList<>();
        this.playerShip = null;
        this.config = scene.getGameDataManager().config.levelUI;
    }

    public void update(){
        updatePlayerLives();
    }

    private void updatePlayerLives(){
        if(playerShip != null){
            int playerHP = playerShip.getHP();
            if(playerLives.size() != playerHP){
                while(playerLives.size() < playerHP){
                    Vec2D size = config.lives.size;
                    Image hpPointImage = new Image(assetManager.getTexture(config.lives.textureFilepath), config.contentsLayer, false, size.x, size.y);
                    Vec2D position = config.lives.position;
                    Vec2D stride = config.lives.stride;
                    float pointPositionX = position.x + stride.x * playerLives.size();
                    float pointPositionY = position.y + stride.y * playerLives.size();
                    hpPointImage.setPosition(pointPositionX, pointPositionY);
                    ImageDisplay hpPointDisplay = new ImageDisplay(hpPointImage);
                    scene.addVisual(new ImageDisplay(hpPointImage));
                    playerLives.add(hpPointDisplay);
                }
                while(playerLives.size() > playerHP){
                    scene.removeVisual(playerLives.getLast());
                    playerLives.removeLast();
                }
            }
        }
    }

    public void setPlayerShip(Ship playerShip){
        this.playerShip = playerShip;
    }
}
