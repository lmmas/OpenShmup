package json;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.Separators;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import json.editionData.EditionData;

import java.io.IOException;
import java.util.List;

public class JsonDataWriter {

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

    private ArrayNode buildJsonNodeOfList(List<EditionData> dataList) {
        ArrayNode listNode = mapper.createArrayNode();
        for (EditionData data : dataList) {
            ObjectNode visualNode = listNode.addObject();
            if (data.hasTypeSelect()) {
                visualNode.put(EditionData.Keys.type.name(), data.getType().name());
            }
            data.getAttributesList().forEach(attribute -> attribute.addToNode(visualNode));
        }
        return listNode;
    }

    public void saveToJson(GameEditionData gameEditionData) {
        ArrayNode visualsNode = buildJsonNodeOfList(gameEditionData.getVisualEditionDataList());
        ArrayNode trajectoriesNode = buildJsonNodeOfList(gameEditionData.getTrajectoryEditionDataList());
        ArrayNode entitiesNode = buildJsonNodeOfList(gameEditionData.getEntityEditionDataList());
        ArrayNode timelineSpawnInfosNode = buildJsonNodeOfList(gameEditionData.getTimelineDataList());
        ObjectNode timelineNode = mapper.createObjectNode();
        timelineNode.put("duration", 3000.0d);
        timelineNode.set("spawns", timelineSpawnInfosNode);
        try {
            writer.writeValue(gameEditionData.paths.gameVisualsFile.toFile(), visualsNode);
            writer.writeValue(gameEditionData.paths.gameTrajectoriesFile.toFile(), trajectoriesNode);
            writer.writeValue(gameEditionData.paths.gameEntitiesFile.toFile(), entitiesNode);
            writer.writeValue(gameEditionData.paths.gameTimelineFile.toFile(), timelineNode);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
