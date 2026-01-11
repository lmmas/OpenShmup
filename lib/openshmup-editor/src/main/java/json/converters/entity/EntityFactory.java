package json.converters.entity;

import engine.entity.Entity;
import engine.gameData.GameDataManager;
import json.GameLoader;
import json.SafeJsonNode;

public interface EntityFactory {

    Entity fromJson(SafeJsonNode node, GameLoader gameLoader, GameDataManager gameData);
}
