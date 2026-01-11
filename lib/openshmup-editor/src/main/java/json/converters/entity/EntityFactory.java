package json.converters.entity;

import engine.entity.Entity;
import engine.gameData.GameDataManager;
import json.SafeJsonNode;
import json.factories.GameFactory;

public interface EntityFactory {

    Entity fromJson(SafeJsonNode node, GameFactory gameFactory, GameDataManager gameData);
}
