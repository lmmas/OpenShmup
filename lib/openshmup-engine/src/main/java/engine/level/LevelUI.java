package engine.level;

import engine.gameData.GameConfig;
import engine.level.entity.Ship;
import engine.scene.Scene;
import engine.scene.visual.ImageDisplay;
import engine.types.Vec2D;

import java.util.ArrayList;

import static engine.Engine.assetManager;

public class LevelUI {

    final private Scene scene;

    final private ArrayList<ImageDisplay> playerLives;

    private Ship playerShip;

    final private GameConfig.LevelUI config;

    public LevelUI(GameConfig.LevelUI uiConfig, Scene scene) {
        this.scene = scene;
        this.playerLives = new ArrayList<>();
        this.playerShip = null;
        this.config = uiConfig;
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
                    Vec2D pointPosition = position.add(stride.scalar(playerLives.size()));
                    ImageDisplay hpPointDisplay = new ImageDisplay(config.contentsLayer, assetManager.getTexture(config.lives.textureFilepath), size.x, size.y, pointPosition.x, pointPosition.y);
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
