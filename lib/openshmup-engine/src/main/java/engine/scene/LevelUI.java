package engine.scene;

import engine.AssetManager;
import engine.GameConfig;
import engine.entity.Ship;
import engine.graphics.Image2D;
import engine.scene.display.StaticImageDisplay;
import engine.types.Vec2D;

import java.util.ArrayList;

import static engine.Engine.assetManager;

public class LevelUI {
    final private LevelScene scene;
    final private ArrayList<StaticImageDisplay> playerLives;
    private Ship playerShip;
    public LevelUI(LevelScene scene){
        this.scene = scene;
        this.playerLives = new ArrayList<>();
        this.playerShip = null;
    }

    public void update(){
        updatePlayerLives();
    }

    private void updatePlayerLives(){
        if(playerShip != null){
            int playerHP = playerShip.getHP();
            if(playerLives.size() != playerHP){
                while(playerLives.size() < playerHP){
                    Vec2D size = GameConfig.LevelUI.Lives.size;
                    Image2D hpPointImage = new Image2D(assetManager.getTexture(GameConfig.LevelUI.Lives.textureFilepath), GameConfig.LevelUI.contentsLayer, false, size.x, size.y);
                    Vec2D position = GameConfig.LevelUI.Lives.position;
                    Vec2D stride = GameConfig.LevelUI.Lives.stride;
                    float pointPositionX = position.x + stride.x * playerLives.size();
                    float pointPositionY = position.y + stride.y * playerLives.size();
                    hpPointImage.setPosition(pointPositionX, pointPositionY);
                    StaticImageDisplay hpPointDisplay = new StaticImageDisplay(hpPointImage);
                    scene.addDisplay(new StaticImageDisplay(hpPointImage));
                    playerLives.add(hpPointDisplay);
                }
                while(playerLives.size() > playerHP){
                    scene.deleteDisplay(playerLives.getLast());
                    playerLives.removeLast();
                }
            }
        }
    }

    public void setPlayerShip(Ship playerShip){
        this.playerShip = playerShip;
    }
}
