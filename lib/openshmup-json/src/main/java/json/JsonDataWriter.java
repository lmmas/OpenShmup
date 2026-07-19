package json;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edition.EditionData;
import edition.GameEditionData;
import edition.attribute.*;

import static edition.EditionData.*;

import java.io.IOException;
import java.util.List;

final public class JsonDataWriter {

    final private ObjectMapper mapper;

    final private ObjectWriter writer;

    public JsonDataWriter() {
        this.mapper = new ObjectMapper();
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter()
            .withArrayIndenter(new DefaultPrettyPrinter.NopIndenter())
            .withSeparators(
                Separators.createDefaultInstance()
                    .withObjectFieldValueSpacing(Separators.Spacing.AFTER)
                    .withArrayValueSpacing(Separators.Spacing.AFTER)
                    .withArrayEmptySeparator("")
            );

        ObjectMapper mapper = new ObjectMapper();
        this.writer = mapper.writer(pp);
    }

    private void addToNode(Attribute attribute, ObjectNode node) {
        switch (attribute) {
            case BooleanAttribute booleanAttribute ->
                node.put(booleanAttribute.getKey().name(), booleanAttribute.getValue());
            case DoubleAttribute doubleAttribute ->
                node.put(doubleAttribute.getKey().name(), doubleAttribute.getValue());
            case EditionDataAttribute editionDataAttribute -> {
                EditionData editionData = editionDataAttribute.getData();
                var dataNode = node.putObject(editionDataAttribute.getKey().name());
                if (editionData.hasTypeSelect()) {
                    dataNode.put(EditionData.Keys.type.name(), editionData.getType().name());
                }
                editionData.getAttributesList().forEach(attr -> addToNode(attr, dataNode));
            }
            case FloatAttribute floatAttribute -> node.put(floatAttribute.getKey().name(), floatAttribute.getValue());
            case IntegerAttribute integerAttribute ->
                node.put(integerAttribute.getKey().name(), integerAttribute.getValue());
            case IVec2DAttribute iVec2DAttribute -> {
                var arrayNode = node.putArray(iVec2DAttribute.getKey().name());
                arrayNode.add(iVec2DAttribute.getValue().x);
                arrayNode.add(iVec2DAttribute.getValue().y);
            }
            case ListAttribute listAttribute -> {
                ArrayNode arrayNode = node.putArray(listAttribute.getKey().name());
                for (EditionData data : listAttribute.getDataList()) {
                    ObjectNode dataNode = arrayNode.addObject();
                    if (data.hasTypeSelect()) {
                        dataNode.put(EditionData.Keys.type.name(), data.getType().name());
                    }
                    for (Attribute attr : data.getAttributesList()) {
                        addToNode(attr, dataNode);
                    }
                }
            }
            case StringAttribute stringAttribute ->
                node.put(stringAttribute.getKey().name(), stringAttribute.getValue());
            case Vec2DAttribute vec2DAttribute -> {
                var arrayNode = node.putArray(vec2DAttribute.getKey().name());
                arrayNode.add(vec2DAttribute.getValue().x);
                arrayNode.add(vec2DAttribute.getValue().y);
            }
        }
    }

    private ArrayNode buildJsonNodeOfList(List<EditionData> dataList) {
        ArrayNode listNode = mapper.createArrayNode();
        for (EditionData data : dataList) {
            ObjectNode node = listNode.addObject();
            if (data.hasTypeSelect()) {
                node.put(EditionData.Keys.type.name(), data.getType().name());
            }
            data.getAttributesList().forEach(attribute -> addToNode(attribute, node));
        }
        return listNode;
    }

    public void saveToJson(GameEditionData gameEditionData) {
        gameEditionData.paths.gameFolder.toFile().mkdir();
        gameEditionData.paths.gameVisualsFile.getParent().toFile().mkdir();
        gameEditionData.paths.gameTextureFolder.toFile().mkdir();
        ArrayNode visualsNode = buildJsonNodeOfList(gameEditionData.getVisualEditionDataList());
        ArrayNode trajectoriesNode = buildJsonNodeOfList(gameEditionData.getTrajectoryEditionDataList());
        ArrayNode entitiesNode = buildJsonNodeOfList(gameEditionData.getEntityEditionDataList());
        ArrayNode timelineSpawnInfosNode = buildJsonNodeOfList(gameEditionData.getTimelineDataList());
        ObjectNode timelineNode = mapper.createObjectNode();
        timelineNode.put("duration", 3000.0d);
        timelineNode.set("spawns", timelineSpawnInfosNode);
        ObjectNode configNode = mapper.createObjectNode();
        ObjectNode generalConfigNode = configNode.putObject(Types.Config.general.name());
        gameEditionData.configs.get(Types.Config.general).getAttributesList().forEach(attribute -> addToNode(attribute, generalConfigNode));
        ObjectNode levelUIConfigNode = configNode.putObject(Types.Config.levelUI.name());
        gameEditionData.configs.get(Types.Config.levelUI).getAttributesList().forEach(attribute -> addToNode(attribute, levelUIConfigNode));
        try {
            writer.writeValue(gameEditionData.paths.gameVisualsFile.toFile(), visualsNode);
            writer.writeValue(gameEditionData.paths.gameTrajectoriesFile.toFile(), trajectoriesNode);
            writer.writeValue(gameEditionData.paths.gameEntitiesFile.toFile(), entitiesNode);
            writer.writeValue(gameEditionData.paths.gameTimelineFile.toFile(), timelineNode);
            writer.writeValue(gameEditionData.paths.gameConfigFile.toFile(), configNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
