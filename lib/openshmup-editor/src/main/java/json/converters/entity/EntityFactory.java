package json.converters.entity;

import engine.gameData.GameDataManager;
import engine.level.entity.Entity;
import json.GameLoader;
import json.SafeJsonNode;

public interface EntityFactory {

    Entity fromJson(SafeJsonNode node, GameLoader gameLoader, GameDataManager gameData);
}
