package editor.fieldNode;

import edition.EditionData;
import edition.attribute.*;
import editor.AttributeLabels;
import editor.Style;
import engine.menu.Menu;
import engine.menu.MenuElementGroup;
import engine.menu.widget.BooleanField;
import engine.menu.widget.TextField;
import engine.menu.widget.Widget;
import engine.menu.widget.Widgets;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import lombok.Getter;
import lombok.Setter;
import types.IVec2D;
import types.Vec2D;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import static editor.Widgets.Checkbox;
import static editor.Widgets.EditorTextField;

final public class EditionDataFields implements EditionDataFieldNode {

    final private static DecimalFormat df = new DecimalFormat("0.000");
    @Setter @Getter
    private EditionData editionData;

    final private HashMap<Attribute, List<Widget>> attributeWidgetsMap;

    final private HashMap<Attribute, FieldNode> childrenNodeMap;

    final private List<ListFields> listFieldsSelectList;

    private Integer selectedListIndex;

    private Menu menu;

    final private MenuElementGroup elementGroup;

    private boolean isActive;

    public EditionDataFields(EditionData editionData, Vec2D startPosition) {
        this.editionData = editionData;
        this.attributeWidgetsMap = new HashMap<>();
        this.childrenNodeMap = new HashMap<>();
        this.listFieldsSelectList = new ArrayList<>();
        this.selectedListIndex = null;
        this.menu = null;
        this.elementGroup = new MenuElementGroup();
        this.isActive = false;
        this.buildFields(startPosition);
        this.loadFieldsFromAttributes();
    }

    private float getSpacing(Attribute attribute) {
        if (attribute instanceof EditionDataAttribute dataAttribute) {
            float fieldHeight = 45f;
            float editionDataFieldSize = switch (dataAttribute.getData().getCategory()) {
                case HITBOX -> 2 * fieldHeight;
                case NONE -> {
                    if (dataAttribute.getData().getType() == EditionData.Types.spritesheetInfo) {
                        yield 5 * fieldHeight;
                    }
                    else {
                        assert false : "incorrect edition data type";
                        yield 0f;
                    }
                }
                case CONFIG -> 4 * fieldHeight;
                default -> {
                    assert false : "incorrect edition data type";
                    yield 0f;
                }
            };
            if (dataAttribute.hasTypeSelect()) {
                return editionDataFieldSize + fieldHeight + EditionDataTypeSelect.selectorSpacing;
            }
            else {
                return editionDataFieldSize + fieldHeight;
            }
        }
        else {
            return 45f;
        }
    }

    private void buildFields(Vec2D startPosition) {
        List<Attribute> attributeList = editionData.getAttributesList();
        int fieldLayer = 1;
        float coupleFieldSpacing = 35f;
        Vec2D fieldMargin = new Vec2D(320f, 0f);


        Vec2D attributePosition = startPosition;
        for (Attribute attribute : attributeList) {
            TextDisplay attributeLabel = new TextDisplay(fieldLayer, false, attributePosition, AttributeLabels.get(attribute.getKey()) + ":", Style.Text.menuTextStyle, TextAlignment.LEFT);
            elementGroup.visuals().add(attributeLabel);
            Vec2D fieldPosition = attributePosition.add(fieldMargin);
            switch (attribute) {
                case BooleanAttribute booleanAttribute -> {
                    Vec2D fieldOffset = new Vec2D(16f, 0f);
                    BooleanField checkbox = Checkbox(fieldLayer, fieldPosition.add(fieldOffset), booleanAttribute.getValue());
                    elementGroup.widgets().add(checkbox);
                    attributeWidgetsMap.put(attribute, List.of(checkbox));
                }

                case DoubleAttribute doubleAttribute -> {
                    float fieldWidthPixels = 120f;
                    Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                    TextField textField = EditorTextField(fieldLayer, fieldPosition.add(fieldOffset), fieldWidthPixels, df.format(doubleAttribute.getValue()));
                    elementGroup.widgets().add(textField);
                    attributeWidgetsMap.put(attribute, List.of(textField));
                }

                case EditionDataAttribute editionDataAttribute -> {
                    Vec2D fieldOffset = new Vec2D(60f, -45f);
                    fieldPosition = attributePosition.add(fieldOffset);
                    EditionDataFieldNode node = EditionDataFieldNode.createFromEtitionData(editionDataAttribute.getData(), fieldPosition);
                    childrenNodeMap.put(editionDataAttribute, node);
                }

                case FloatAttribute floatAttribute -> {
                    float fieldWidthPixels = 120f;
                    Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                    TextField textField = EditorTextField(fieldLayer, fieldPosition.add(fieldOffset), fieldWidthPixels, df.format(floatAttribute.getValue()));
                    elementGroup.widgets().add(textField);
                    attributeWidgetsMap.put(attribute, List.of(textField));
                }

                case IntegerAttribute integerAttribute -> {
                    float fieldWidthPixels = 100f;
                    Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                    TextField textField = EditorTextField(fieldLayer, fieldPosition.add(fieldOffset), fieldWidthPixels, Integer.toString(integerAttribute.getValue()));
                    elementGroup.widgets().add(textField);
                    attributeWidgetsMap.put(attribute, List.of(textField));
                }

                case IVec2DAttribute iVec2DAttribute -> {
                    float fieldWidthPixels = 100f;
                    float fieldLabelOffset = 25f;
                    TextDisplay fieldLabel1 = new TextDisplay(fieldLayer, false, fieldPosition, "x:", Style.Text.menuTextStyle, TextAlignment.LEFT);
                    elementGroup.visuals().add(fieldLabel1);
                    Vec2D field1Offset = new Vec2D(fieldWidthPixels / 2 + fieldLabelOffset, 0f);
                    TextField textField1 = EditorTextField(fieldLayer, fieldPosition.add(field1Offset), fieldWidthPixels, Integer.toString(iVec2DAttribute.getValue().x));
                    elementGroup.widgets().add(textField1);
                    Vec2D fieldLabel2Position = new Vec2D(fieldPosition.x + fieldWidthPixels + coupleFieldSpacing, fieldPosition.y);
                    TextDisplay fieldDisplay2 = new TextDisplay(fieldLayer, false, fieldLabel2Position, "y:", Style.Text.menuTextStyle, TextAlignment.LEFT);
                    elementGroup.visuals().add(fieldDisplay2);
                    Vec2D field2Offset = new Vec2D(fieldLabelOffset + fieldWidthPixels + coupleFieldSpacing + fieldWidthPixels / 2, 0f);
                    TextField textField2 = EditorTextField(fieldLayer, fieldPosition.add(field2Offset), fieldWidthPixels, Integer.toString(iVec2DAttribute.getValue().y));
                    elementGroup.widgets().add(textField2);
                    attributeWidgetsMap.put(attribute, List.of(textField1, textField2));
                }

                case ListAttribute listAttribute -> {
                    Vec2D listStartPosition = startPosition.add(850f, 0f);
                    ArrayList<EditionData> listCopy = new ArrayList<>(listAttribute.getDataList());
                    ListFields listFields = new ListFields(listAttribute.getCategory(), listCopy, listStartPosition, listAttribute.getCategory() == EditionData.Category.NONE);
                    int fieldsListIndex = listFieldsSelectList.size();
                    listFieldsSelectList.add(listFields);
                    Vec2D openListButtonSize = new Vec2D(80f, 40f);
                    Vec2D fieldOffset = new Vec2D(openListButtonSize.x / 2, 0f);
                    Runnable onClick = () -> {
                        if (!Objects.equals(selectedListIndex, fieldsListIndex)) {
                            if (selectedListIndex != null) {
                                listFieldsSelectList.get(selectedListIndex).setActive(false);
                            }
                            listFieldsSelectList.get(fieldsListIndex).setActive(isActive);
                            selectedListIndex = fieldsListIndex;
                        }
                    };
                    Widget openListButton = Widgets.RoundedRectangleButton(fieldLayer, openListButtonSize, fieldPosition.add(fieldOffset), Style.editionSelectorUnselected, "Open", onClick);
                    elementGroup.widgets().add(openListButton);
                    childrenNodeMap.put(listAttribute, listFields);
                }

                case StringAttribute stringAttribute -> {
                    float fieldWidthPixels = 300f;
                    Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                    TextField textField = EditorTextField(fieldLayer, fieldPosition.add(fieldOffset), fieldWidthPixels, stringAttribute.getValue());
                    elementGroup.widgets().add(textField);
                    attributeWidgetsMap.put(attribute, List.of(textField));
                }

                case Vec2DAttribute vec2DAttribute -> {
                    float fieldWidthPixels = 120f;
                    float fieldLabelOffset = 25f;
                    TextDisplay fieldLabel1 = new TextDisplay(fieldLayer, false, fieldPosition, "x:", Style.Text.menuTextStyle, TextAlignment.LEFT);
                    elementGroup.visuals().add(fieldLabel1);
                    Vec2D field1Offset = new Vec2D(fieldWidthPixels / 2 + fieldLabelOffset, 0f);
                    TextField textField1 = EditorTextField(fieldLayer, fieldPosition.add(field1Offset), fieldWidthPixels, df.format(vec2DAttribute.getValue().x));
                    elementGroup.widgets().add(textField1);
                    Vec2D fieldLabel2Position = new Vec2D(fieldPosition.x + fieldWidthPixels + coupleFieldSpacing, fieldPosition.y);
                    TextDisplay fieldDisplay2 = new TextDisplay(fieldLayer, false, fieldLabel2Position, "y:", Style.Text.menuTextStyle, TextAlignment.LEFT);
                    elementGroup.visuals().add(fieldDisplay2);
                    Vec2D field2Offset = new Vec2D(fieldLabelOffset + fieldWidthPixels + coupleFieldSpacing + fieldWidthPixels / 2, 0f);
                    TextField textField2 = EditorTextField(fieldLayer, fieldPosition.add(field2Offset), fieldWidthPixels, df.format(vec2DAttribute.getValue().y));
                    elementGroup.widgets().add(textField2);
                    attributeWidgetsMap.put(attribute, List.of(textField1, textField2));
                }
            }
            attributePosition = attributePosition.add(0.0f, -getSpacing(attribute));
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

                case EditionDataAttribute ignored -> {
                    assert false : "attribute should not be in map";
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

                case ListAttribute ignored -> {
                    assert false : "attribute should not be in map";
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
        for (FieldNode node : childrenNodeMap.values()) {
            if (node instanceof EditionDataFields editionDataFields) {
                editionDataFields.resetToDefault();
            }
        }
    }
    @Override
    public void setMenu(Menu menu) {
        this.menu = menu;
        childrenNodeMap.values().forEach(fieldNode -> fieldNode.setMenu(menu));
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
            for (FieldNode node : childrenNodeMap.values()) {
                if (!(node instanceof ListFields)) {
                    node.setActive(active);
                }
            }
            if (selectedListIndex != null) {
                listFieldsSelectList.get(selectedListIndex).setActive(active);
            }
        }
    }
    @Override
    public void applyChanges() {

        for (var entry : childrenNodeMap.entrySet()) {
            Attribute attribute = entry.getKey();
            FieldNode node = entry.getValue();
            node.applyChanges();
            switch (node) {
                case EditionDataFieldNode editionDataNode -> {
                    EditionDataAttribute editionDataAttribute = (EditionDataAttribute) attribute;
                    editionDataAttribute.setData(editionDataNode.getEditionData());
                }
                case ListFields listFields -> {
                    ListAttribute listAttribute = (ListAttribute) attribute;
                    ArrayList<EditionData> dataList = listAttribute.getDataList();
                    dataList.clear();
                    dataList.addAll(listFields.getDataList());
                }
            }
        }

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

                case EditionDataAttribute ignored -> {
                    assert false : "attribute should not be in map";
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

                case ListAttribute ignored -> {
                    assert false : "attribute should not be in map";
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
