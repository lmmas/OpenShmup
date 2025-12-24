package engine.scene;

import engine.entity.Ship;
import engine.gameData.GameConfig;
import engine.types.Vec2D;
import engine.visual.ImageDisplay;

import java.util.ArrayList;

import static engine.Engine.assetManager;

public class LevelUI {
    final private LevelScene scene;
    final private ArrayList<ImageDisplay> playerLives;
    private Ship playerShip;
    final private GameConfig.LevelUI config;

    public LevelUI(LevelScene scene) {
        this.scene = scene;
        this.playerLives = new ArrayList<>();
        this.playerShip = null;
        this.config = scene.getGameDataManager().config.levelUI;
    }

    public void update() {
        updatePlayerLives();
    }

    private void updatePlayerLives() {
        if (playerShip != null) {
            int playerHP = playerShip.getHP();
            if (playerLives.size() != playerHP) {
                while (playerLives.size() < playerHP) {
                    Vec2D size = config.lives.size;
                    Vec2D position = config.lives.position;
                    Vec2D stride = config.lives.stride;
                    float pointPositionX = position.x + stride.x * playerLives.size();
                    float pointPositionY = position.y + stride.y * playerLives.size();
                    ImageDisplay hpPointDisplay = new ImageDisplay(config.contentsLayer, assetManager.getTexture(config.lives.textureFilepath), size.x, size.y, pointPositionX, pointPositionY);
                    playerLives.add(hpPointDisplay);
                    scene.addVisual(hpPointDisplay);
                }
                while (playerLives.size() > playerHP) {
                    scene.removeVisual(playerLives.getLast());
                    playerLives.removeLast();
                }
            }
        }
    }

    public void setPlayerShip(Ship playerShip) {
        assert this.playerShip == null : "player ship is already set to entity " + this.playerShip;
        this.playerShip = playerShip;
    }
}
