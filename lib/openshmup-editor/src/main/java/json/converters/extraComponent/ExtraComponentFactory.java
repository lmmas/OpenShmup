package json.converters.extraComponent;

import engine.gameData.GameDataManager;
import engine.level.entity.extraComponent.ExtraComponent;
import json.GameLoader;
import json.SafeJsonNode;

public interface ExtraComponentFactory {

    ExtraComponent fromJson(SafeJsonNode node, GameDataManager gameData, GameLoader gameLoader, boolean isPlayer);
}
