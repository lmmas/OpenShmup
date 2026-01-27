package json.converters.extraComponent;

import com.fasterxml.jackson.databind.node.ObjectNode;
import editor.editionData.ExtraComponentEditionData;
import json.SafeJsonNode;
import json.converters.JsonDataConverter;

import java.nio.file.Path;

public interface ExtraComponentConverter {

    ExtraComponentEditionData fromJson(SafeJsonNode node, JsonDataConverter jsonDataConverter, Path textureFolderPath);

    ObjectNode toJson(ObjectNode node, ExtraComponentEditionData extraComponentData, JsonDataConverter jsonDataConverter);
}
