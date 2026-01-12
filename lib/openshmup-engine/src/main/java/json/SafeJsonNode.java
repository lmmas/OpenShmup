package json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import engine.types.IVec2D;
import engine.types.Vec2D;
import lombok.Getter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

final public class SafeJsonNode {

    final private JsonNode node;
    @Getter
    final private Path filePath;

    final private String JSONPath;

    private SafeJsonNode(JsonNode node, Path filePath, String JSONpath) {
        this.node = node;
        this.filePath = filePath;
        this.JSONPath = JSONpath;
    }

    public static SafeJsonNode getObjectRootNode(Path filepath, ObjectMapper objectMapper) throws IllegalArgumentException {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(new File(filepath.toString()));
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid JSON file: " + filepath);
        }
        String rootPath = ":$";
        if (!rootNode.isObject()) {
            throw new IllegalArgumentException("Invalid JSON format: " + filepath + rootPath + "root node should be an object");
        }
        return new SafeJsonNode(rootNode, filepath, rootPath);
    }

    public static SafeJsonNode getArrayRootNode(Path filepath, ObjectMapper objectMapper) throws IllegalArgumentException {
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(new File(filepath.toString()));
        } catch (IOException e) {
            throw new IllegalArgumentException("Invalid JSON file: " + filepath);
        }
        String rootPath = ":$";
        if (!rootNode.isArray()) {
            throw new IllegalArgumentException("Invalid JSON format: " + rootPath + "root node should be an array");
        }
        return new SafeJsonNode(rootNode, filepath, rootPath);
    }

    public void checkForField(String field) throws IllegalArgumentException {
        if (!node.has(field)) {
            throw new IllegalArgumentException("Invalid JSON format: " + getFullPath() + ": field \"" + field + "\" not found");
        }
    }

    public boolean checkAndGetBoolean(String fieldName) throws IllegalArgumentException {
        checkForField(fieldName);
        JsonNode booleanNode = node.get(fieldName);
        if (!booleanNode.isBoolean()) {
            throw new IllegalArgumentException("Invalid JSON format: " + getFullPath() + "." + fieldName + " should be a boolean");
        }
        return booleanNode.booleanValue();
    }

    public int checkAndGetInt(String fieldName) throws IllegalArgumentException {
        checkForField(fieldName);
        JsonNode intNode = node.get(fieldName);
        if (!intNode.isInt()) {
            throw new IllegalArgumentException("Invalid JSON format: " + getFullPath() + "." + fieldName + " should be an int");
        }
        return intNode.intValue();
    }

    public float checkAndGetFloat(String fieldName) throws IllegalArgumentException {
        checkForField(fieldName);
        JsonNode floatNode = node.get(fieldName);
        if (!floatNode.isNumber()) {
            throw new IllegalArgumentException("Invalid JSON format: " + getFullPath() + "." + fieldName + " should be a float");
        }
        return floatNode.floatValue();
    }

    public String checkAndGetString(String fieldName) throws IllegalArgumentException {
        checkForField(fieldName);
        JsonNode stringNode = node.get(fieldName);
        if (!stringNode.isTextual()) {
            throw new IllegalArgumentException("Invalid JSON format: " + getFullPath() + "." + fieldName + " should be a string");
        }
        return stringNode.textValue();
    }

    public SafeJsonNode checkAndGetObject(String fieldName) throws IllegalArgumentException {
        checkForField(fieldName);
        JsonNode objectNode = node.get(fieldName);
        if (!objectNode.isObject()) {
            throw new IllegalArgumentException("Invalid JSON format: " + getFullPath() + "." + fieldName + " should be an object");
        }
        return new SafeJsonNode(objectNode, filePath, JSONPath + "." + fieldName);
    }

    public SafeJsonNode checkAndGetArray(String fieldName) throws IllegalArgumentException {
        checkForField(fieldName);
        JsonNode arrayNode = node.get(fieldName);
        if (!arrayNode.isArray()) {
            throw new IllegalArgumentException("Invalid JSON format: " + getFullPath() + "." + fieldName + " should be an array");
        }
        return new SafeJsonNode(arrayNode, filePath, JSONPath + "." + fieldName);
    }

    public void checkArraySize(int size) throws IllegalArgumentException {
        if (node.size() != size) {
            throw new IllegalArgumentException("Invalid JSON format: " + getFullPath() + " should be of size " + size);
        }
    }

    public List<SafeJsonNode> checkAndGetObjectListFromArray() throws IllegalArgumentException {
        if (!node.isArray()) {
            throw new IllegalArgumentException("Invalid JSON format: " + getFullPath() + " is not an array");
        }
        ArrayList<SafeJsonNode> objectList = new ArrayList<>(node.size());
        for (int i = 0; i < node.size(); i++) {
            if (!node.get(i).isObject()) {
                throw new IllegalArgumentException("Invalid JSON format: " + getFullPath() + " should only contain objects");
            }
            objectList.add(new SafeJsonNode(node.get(i), filePath, JSONPath + "[" + i + "]"));
        }
        return objectList;
    }

    public Vec2D checkAndGetVec2D(String fieldName) {
        checkForField(fieldName);
        JsonNode vecNode = node.get(fieldName);
        String vecPath = getFullPath() + "." + fieldName;
        if (!vecNode.isArray()) {
            throw new IllegalArgumentException("Invalid JSON format: " + vecPath + " is not an array");
        }
        if (vecNode.size() != 2) {
            throw new IllegalArgumentException("Invalid JSON format: " + vecPath + " should be of size 2");
        }
        JsonNode node1 = vecNode.get(0);
        JsonNode node2 = vecNode.get(1);
        if (!node1.isNumber() || !node2.isNumber()) {
            throw new IllegalArgumentException("Invalid JSON format: " + vecPath + " should only contain float values");
        }
        return new Vec2D(node1.floatValue(), node2.floatValue());
    }

    public IVec2D checkAndGetIVec2D(String fieldName) {
        checkForField(fieldName);
        JsonNode vecNode = node.get(fieldName);
        String vecPath = getFullPath() + "." + fieldName;
        if (!vecNode.isArray()) {
            throw new IllegalArgumentException("Invalid JSON format: " + vecPath + " is not an array");
        }
        if (vecNode.size() != 2) {
            throw new IllegalArgumentException("Invalid JSON format: " + vecPath + " should be of size 2");
        }
        JsonNode node1 = vecNode.get(0);
        JsonNode node2 = vecNode.get(1);
        if (!node1.isInt() || !node2.isInt()) {
            throw new IllegalArgumentException("Invalid JSON format: " + vecPath + " should only contain int values");
        }
        return new IVec2D(node1.intValue(), node2.intValue());
    }

    public boolean hasField(String fieldName) {
        return node.has(fieldName);
    }

    public boolean isArray() {
        return node.isArray();
    }

    public String getFullPath() {
        return this.filePath + this.JSONPath;
    }
}
