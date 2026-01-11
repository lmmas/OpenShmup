package json.converters.extraComponent;

import engine.entity.extraComponent.ExtraComponent;
import engine.gameData.GameDataManager;
import json.GameLoader;
import json.SafeJsonNode;

public interface ExtraComponentFactory {

    ExtraComponent fromJson(SafeJsonNode node, GameDataManager gameData, GameLoader gameLoader, boolean isPlayer);
}
