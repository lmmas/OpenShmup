package json.converters.extraComponent;

import engine.entity.extraComponent.ExtraComponent;
import engine.gameData.GameDataManager;
import json.SafeJsonNode;
import json.factories.GameFactory;

public interface ExtraComponentFactory {

    ExtraComponent fromJson(SafeJsonNode node, GameDataManager gameData, GameFactory gameFactory, boolean isPlayer);
}
