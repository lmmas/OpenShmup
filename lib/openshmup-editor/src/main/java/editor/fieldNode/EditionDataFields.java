package editor.fieldNode;

import editor.AttributeLabels;
import editor.Style;
import editor.attribute.*;
import editor.editionData.*;
import engine.menu.Menu;
import engine.menu.MenuElementGroup;
import engine.menu.widget.BooleanField;
import engine.menu.widget.TextField;
import engine.menu.widget.Widget;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.types.IVec2D;
import engine.types.Vec2D;
import lombok.Getter;
import lombok.Setter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static editor.Widgets.Checkbox;
import static editor.Widgets.EditorTextField;

final public class EditionDataFields<D extends EditionData> implements FieldNode {

    final private static DecimalFormat df = new DecimalFormat("0.000");
    @Setter @Getter
    private D editionData;

    final private HashMap<Attribute, List<Widget>> attributeWidgetsMap;

    final private HashMap<EditionDataAttribute<?>, EditionDataTypeSelect<?>> editionDataTypeSelectMap;

    final private List<FieldNode> children;

    private Menu menu;

    final private MenuElementGroup elementGroup;

    private boolean isActive;

    public EditionDataFields(D editionData, Vec2D startPosition) {
        this.editionData = editionData;
        this.attributeWidgetsMap = new HashMap<>();
        this.editionDataTypeSelectMap = new HashMap<>();
        this.children = new ArrayList<>();
        this.menu = null;
        this.elementGroup = new MenuElementGroup();
        this.isActive = false;
        this.buildFields(startPosition);
        this.loadFieldsFromAttributes();
    }

    private float getSpacing(Attribute attribute) {
        if (attribute instanceof EditionDataAttribute<?> dataAttribute) {
            EditionData data = dataAttribute.getData();
            if (dataAttribute.hasTypeSelect()) {
                return (data.getAttributes().size() + 1) * 45f + EditionDataTypeSelect.selectorSpacing;
            }
            return (data.getAttributes().size() + 1) * 45f;
        }
        else {
            return 45f;
        }
    }

    private void buildFields(Vec2D startPosition) {
        List<Attribute> attributeList = editionData.getAttributes();
        int layer = 1;
        float coupleFieldSpacing = 250f;
        Vec2D fieldMargin = new Vec2D(500f, 0f);


        Vec2D attributePosition = startPosition;
        for (Attribute attribute : attributeList) {
            TextDisplay attributeLabel = new TextDisplay(layer, false, attributePosition, AttributeLabels.get(attribute.getKey()) + ":", Style.Text.menuButtonLabelStyle, TextAlignment.LEFT);
            elementGroup.visuals().add(attributeLabel);
            Vec2D fieldPosition = attributePosition.add(fieldMargin);
            switch (attribute) {
                case BooleanAttribute booleanAttribute -> {
                    Vec2D fieldOffset = new Vec2D(16f, 0f);
                    BooleanField checkbox = Checkbox(layer, fieldPosition.add(fieldOffset), booleanAttribute.getValue());
                    elementGroup.widgets().add(checkbox);
                    attributeWidgetsMap.put(attribute, List.of(checkbox));
                }

                case DoubleAttribute doubleAttribute -> {
                    float fieldWidthPixels = 150f;
                    Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                    TextField textField = EditorTextField(layer, fieldPosition.add(fieldOffset), fieldWidthPixels, df.format(doubleAttribute.getValue()));
                    elementGroup.widgets().add(textField);
                    attributeWidgetsMap.put(attribute, List.of(textField));
                }

                case EditionDataAttribute<?> editionDataAttribute -> {
                    Vec2D fieldOffset = new Vec2D(60f, -45f);
                    fieldPosition = attributePosition.add(fieldOffset);
                    if (editionDataAttribute.hasTypeSelect()) {
                        EditionDataTypeSelect<?> node = switch (editionDataAttribute.getData()) {
                            case VisualEditionData visualEditionData ->
                                new EditionDataTypeSelect<VisualEditionData>(visualEditionData, fieldPosition);
                            case TrajectoryEditionData trajectoryEditionData ->
                                new EditionDataTypeSelect<TrajectoryEditionData>(trajectoryEditionData, fieldPosition);
                            case SpawnEditionData spawnableEditionData ->
                                new EditionDataTypeSelect<SpawnEditionData>(spawnableEditionData, fieldPosition);
                            case HitboxEditionData hitboxEditionData ->
                                new EditionDataTypeSelect<HitboxEditionData>(hitboxEditionData, fieldPosition);
                            case EntityEditionData entityEditionData ->
                                new EditionDataTypeSelect<EntityEditionData>(entityEditionData, fieldPosition);
                            default ->
                                throw new IllegalStateException("Unexpected value: " + editionDataAttribute.getData());
                        };
                        children.add(node);
                        editionDataTypeSelectMap.put(editionDataAttribute, node);
                    }
                    else {
                        EditionDataFields<?> node = new EditionDataFields<>(editionDataAttribute.getData(), fieldPosition);
                        children.add(node);
                    }
                }

                case FloatAttribute floatAttribute -> {
                    float fieldWidthPixels = 150f;
                    Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                    TextField textField = EditorTextField(layer, fieldPosition.add(fieldOffset), fieldWidthPixels, df.format(floatAttribute.getValue()));
                    elementGroup.widgets().add(textField);
                    attributeWidgetsMap.put(attribute, List.of(textField));
                }

                case IntegerAttribute integerAttribute -> {
                    float fieldWidthPixels = 100f;
                    Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                    TextField textField = EditorTextField(layer, fieldPosition.add(fieldOffset), fieldWidthPixels, Integer.toString(integerAttribute.getValue()));
                    elementGroup.widgets().add(textField);
                    attributeWidgetsMap.put(attribute, List.of(textField));
                }

                case IVec2DAttribute iVec2DAttribute -> {
                    float fieldWidthPixels = 150f;
                    float fieldLabelOffset = 50f;
                    TextDisplay fieldLabel1 = new TextDisplay(layer, false, fieldPosition, "x:", Style.Text.menuButtonLabelStyle, TextAlignment.LEFT);
                    elementGroup.visuals().add(fieldLabel1);
                    Vec2D field1Offset = new Vec2D(fieldWidthPixels / 2 + fieldLabelOffset, 0f);
                    TextField textField1 = EditorTextField(layer, fieldPosition.add(field1Offset), fieldWidthPixels, Integer.toString(iVec2DAttribute.getValue().x));
                    elementGroup.widgets().add(textField1);
                    Vec2D fieldLabel2Position = new Vec2D(fieldPosition.x + coupleFieldSpacing, fieldPosition.y);
                    TextDisplay fieldDisplay2 = new TextDisplay(layer, false, fieldLabel2Position, "y:", Style.Text.menuButtonLabelStyle, TextAlignment.LEFT);
                    elementGroup.visuals().add(fieldDisplay2);
                    Vec2D field2Offset = new Vec2D(fieldWidthPixels / 2 + fieldLabelOffset + coupleFieldSpacing, 0f);
                    TextField textField2 = EditorTextField(layer, fieldPosition.add(field2Offset), fieldWidthPixels, Integer.toString(iVec2DAttribute.getValue().y));
                    elementGroup.widgets().add(textField2);
                    attributeWidgetsMap.put(attribute, List.of(textField1, textField2));
                }

                case ListAttribute<?> ignored -> {

                }

                case StringAttribute stringAttribute -> {
                    float fieldWidthPixels = 300f;
                    Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                    TextField textField = EditorTextField(layer, fieldPosition.add(fieldOffset), fieldWidthPixels, stringAttribute.getValue());
                    elementGroup.widgets().add(textField);
                    attributeWidgetsMap.put(attribute, List.of(textField));
                }

                case Vec2DAttribute vec2DAttribute -> {
                    float fieldWidthPixels = 150f;
                    float fieldLabelOffset = 50f;
                    TextDisplay fieldLabel1 = new TextDisplay(layer, false, fieldPosition, "x:", Style.Text.menuButtonLabelStyle, TextAlignment.LEFT);
                    elementGroup.visuals().add(fieldLabel1);
                    Vec2D field1Offset = new Vec2D(fieldWidthPixels / 2 + fieldLabelOffset, 0f);
                    TextField textField1 = EditorTextField(layer, fieldPosition.add(field1Offset), fieldWidthPixels, df.format(vec2DAttribute.getValue().x));
                    elementGroup.widgets().add(textField1);
                    Vec2D fieldLabel2Position = new Vec2D(fieldPosition.x + coupleFieldSpacing, fieldPosition.y);
                    TextDisplay fieldDisplay2 = new TextDisplay(layer, false, fieldLabel2Position, "y:", Style.Text.menuButtonLabelStyle, TextAlignment.LEFT);
                    elementGroup.visuals().add(fieldDisplay2);
                    Vec2D field2Offset = new Vec2D(fieldWidthPixels / 2 + fieldLabelOffset + coupleFieldSpacing, 0f);
                    TextField textField2 = EditorTextField(layer, fieldPosition.add(field2Offset), fieldWidthPixels, df.format(vec2DAttribute.getValue().y));
                    elementGroup.widgets().add(textField2);
                    attributeWidgetsMap.put(attribute, List.of(textField1, textField2));
                }
            }
            attributePosition = attributePosition.add(new Vec2D(0.0f, -getSpacing(attribute)));
        }
    }

    private void loadFieldsFromAttributes() {
        for (var entry : attributeWidgetsMap.entrySet()) {
            Attribute attribute = entry.getKey();
            List<Widget> items = entry.getValue();
            switch (attribute) {
                case BooleanAttribute booleanAttribute -> {
                    assert items.size() == 1 && items.getFirst() instanceof BooleanField : "incorrect fields assigned to attribute";
                    BooleanField booleanField = (BooleanField) items.getFirst();
                    booleanField.setValue(booleanAttribute.getValue());
                }

                case DoubleAttribute doubleAttribute -> {
                    assert items.size() == 1 && items.getFirst() instanceof TextField : "incorrect fields assigned to attribute";
                    TextField textField = (TextField) items.getFirst();
                    textField.setStringValue(df.format(doubleAttribute.getValue()));
                }

                case EditionDataAttribute<?> editionDataAttribute -> {
                    if (editionDataAttribute.hasTypeSelect()) {
                        EditionDataTypeSelect<?> editionDataTypeSelect = editionDataTypeSelectMap.get(editionDataAttribute);
                        editionDataTypeSelect.setData(editionDataAttribute.getData());
                    }
                }

                case FloatAttribute floatAttribute -> {
                    assert items.size() == 1 && items.getFirst() instanceof TextField : "incorrect fields assigned to attribute";
                    TextField textField = (TextField) items.getFirst();
                    textField.setStringValue(df.format(floatAttribute.getValue()));
                }

                case IntegerAttribute integerAttribute -> {
                    assert items.size() == 1 && items.getFirst() instanceof TextField : "incorrect fields assigned to attribute";
                    TextField textField = (TextField) items.getFirst();
                    textField.setStringValue(Integer.toString(integerAttribute.getValue()));
                }

                case IVec2DAttribute iVec2DAttribute -> {
                    assert items.size() == 2 && items.get(0) instanceof TextField && items.get(1) instanceof TextField : "incorrect fields assigned to attribute";
                    TextField textField1 = (TextField) items.get(0);
                    TextField textField2 = (TextField) items.get(1);
                    textField1.setStringValue(Integer.toString(iVec2DAttribute.getValue().x));
                    textField2.setStringValue(Integer.toString(iVec2DAttribute.getValue().y));
                }

                case ListAttribute<?> ignored -> {
                    //already taken care of
                }

                case StringAttribute stringAttribute -> {
                    assert items.size() == 1 && items.getFirst() instanceof TextField : "incorrect fields assigned to attribute";
                    TextField textField = (TextField) items.getFirst();
                    textField.setStringValue(stringAttribute.getValue());
                }

                case Vec2DAttribute vec2DAttribute -> {
                    assert items.size() == 2 && items.get(0) instanceof TextField && items.get(1) instanceof TextField : "incorrect fields assigned to attribute";
                    TextField textField1 = (TextField) items.get(0);
                    TextField textField2 = (TextField) items.get(1);
                    textField1.setStringValue(df.format(vec2DAttribute.getValue().x));
                    textField2.setStringValue(df.format(vec2DAttribute.getValue().y));
                }
            }
        }
    }

    public void resetToDefault() {
        this.editionData.setToDefault();
        this.loadFieldsFromAttributes();
        for (FieldNode node : children) {
            if (node instanceof EditionDataFields<?> editionDataFields) {
                editionDataFields.resetToDefault();
            }
        }
    }
    @Override
    public void setMenu(Menu menu) {
        this.menu = menu;
        children.forEach(fieldNode -> fieldNode.setMenu(menu));
    }
    @Override
    public MenuElementGroup getAllActiveElements() {
        MenuElementGroup allActiveElements = new MenuElementGroup();
        if (this.isActive) {
            allActiveElements.widgets().addAll(elementGroup.widgets());
            allActiveElements.visuals().addAll(elementGroup.visuals());
            for (FieldNode node : children) {
                MenuElementGroup editionDataFieldsElements = node.getAllActiveElements();
                allActiveElements.widgets().addAll(editionDataFieldsElements.widgets());
                allActiveElements.visuals().addAll(editionDataFieldsElements.visuals());
            }
        }
        return allActiveElements;
    }
    @Override
    public void setActive(boolean active) {
        if (active != this.isActive) {
            this.isActive = active;
            if (menu != null) {
                if (active) {
                    menu.addToCurrentScreen(this.elementGroup);
                }
                else {
                    menu.removeFromCurrentScreen(this.elementGroup);
                }
            }
            children.forEach(node -> node.setActive(active));
        }
    }
    @Override
    public void applyChanges() {
        children.forEach(FieldNode::applyChanges);
        for (var entry : attributeWidgetsMap.entrySet()) {
            Attribute attribute = entry.getKey();
            List<Widget> items = entry.getValue();

            switch (attribute) {
                case BooleanAttribute booleanAttribute -> {
                    assert items.size() == 1 && items.getFirst() instanceof BooleanField : "incorrect fields assigned to attribute";
                    BooleanField checkbox = (BooleanField) items.getFirst();
                    booleanAttribute.setValue(checkbox.getBooleanValue());
                }

                case DoubleAttribute doubleAttribute -> {
                    assert items.size() == 1 && items.getFirst() instanceof TextField : "incorrect fields assigned to attribute";
                    TextField textField = (TextField) items.getFirst();
                    double value = Double.parseDouble(textField.getStringValue());
                    doubleAttribute.setValue(value);
                }

                case EditionDataAttribute<?> editionDataAttribute -> {
                    if (editionDataAttribute.hasTypeSelect()) {
                        EditionDataTypeSelect<?> editionDataTypeSelect = editionDataTypeSelectMap.get(editionDataAttribute);
                        editionDataAttribute.setData(editionDataTypeSelect.getSelectedEditionData());
                    }
                }

                case FloatAttribute floatAttribute -> {
                    assert items.size() == 1 && items.getFirst() instanceof TextField : "incorrect fields assigned to attribute";
                    TextField textField = (TextField) items.getFirst();
                    float value = Float.parseFloat(textField.getStringValue());
                    floatAttribute.setValue(value);
                }

                case IntegerAttribute integerAttribute -> {
                    assert items.size() == 1 && items.getFirst() instanceof TextField : "incorrect fields assigned to attribute";
                    TextField textField = (TextField) items.getFirst();
                    int value = Integer.parseInt(textField.getStringValue());
                    integerAttribute.setValue(value);
                }

                case IVec2DAttribute iVec2DAttribute -> {
                    assert items.size() == 2 && items.get(0) instanceof TextField && items.get(1) instanceof TextField : "incorrect fields assigned to attribute";
                    TextField textField1 = (TextField) items.get(0);
                    TextField textField2 = (TextField) items.get(1);
                    int xValue = Integer.parseInt(textField1.getStringValue());
                    int yValue = Integer.parseInt(textField2.getStringValue());
                    iVec2DAttribute.setValue(new IVec2D(xValue, yValue));
                }

                case ListAttribute<?> ignored -> {
                    //already taken care of
                }

                case StringAttribute stringAttribute -> {
                    assert items.size() == 1 && items.getFirst() instanceof TextField : "incorrect fields assigned to attribute";
                    TextField textField = (TextField) items.getFirst();
                    stringAttribute.setValue(textField.getStringValue());
                }

                case Vec2DAttribute vec2DAttribute -> {
                    assert items.size() == 2 && items.get(0) instanceof TextField && items.get(1) instanceof TextField : "incorrect fields assigned to attribute";
                    TextField textField1 = (TextField) items.get(0);
                    TextField textField2 = (TextField) items.get(1);
                    float xValue = Float.parseFloat(textField1.getStringValue());
                    float yValue = Float.parseFloat(textField2.getStringValue());
                    vec2DAttribute.setValue(new Vec2D(xValue, yValue));
                }
            }
        }
    }
}
