package editor;

import editor.attribute.*;
import editor.editionData.*;
import engine.Engine;
import engine.menu.MenuScreen;
import engine.menu.item.ActionButton;
import engine.menu.item.BooleanField;
import engine.menu.item.MenuItem;
import engine.menu.item.TextField;
import engine.scene.visual.BorderedRoundedRectangle;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.types.Vec2D;
import lombok.Getter;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import static editor.MenuItems.Checkbox;
import static editor.MenuItems.EditorTextField;
import static editor.Style.*;

final public class EditPanel {

    final private EditionData editionData;

    final private HashMap<Attribute, List<MenuItem>> attributeItemMap;
    @Getter
    private MenuScreen menuScreen;

    public EditPanel(EditionData editionData) {
        this.editionData = editionData;
        this.attributeItemMap = new HashMap<>();
        this.menuScreen = null;
        buildEditPanel();
    }

    private String getPanelTitle() {
        return switch (editionData) {
            case AnimationEditionData ignored -> "Edit Animation";
            case AnimationEditionData.SpritesheetInfoData ignored -> throw new RuntimeException();
            case ScrollingImageEditionData ignored -> "Edit Scrolling Image";
            case HitboxEditionData ignored -> "Edit Hitbox";
            case TrajectoryEditionData ignored -> "Edit Trajectory";
            case ShotEditionData ignored -> "Edit Shot";
            case ShipEditionData ignored -> "Edit Ship";
            case ProjectileEditionData ignored -> "Edit Projectile";
            case SpawnableEditionData ignored -> "Edit Spawn Info";
        };
    }

    private void buildEditPanel() {
        this.menuScreen = new MenuScreen(4);

        SceneVisual backgroundRectangle = new BorderedRoundedRectangle(0, new Vec2D(1500f, 900f), Engine.getNativeResolution().scalar(0.5f), menuButtonRoundingRadius, menuButtonBorderWidth, Color.menuButtonColor, Color.menuButtonBorderColor);
        menuScreen.addVisual(backgroundRectangle);

        Vec2D closeButtonSize = new Vec2D(150, 50);
        ActionButton closeButton = engine.menu.item.MenuItems.RoundedRectangleButton(1, closeButtonSize, new Vec2D(1800, 1000), menuButtonStyle, "Close", (() -> Engine.getCurrentMenu().removeMenuScreen(menuScreen)));
        menuScreen.addItem(closeButton);

        String panelTitleString = getPanelTitle();
        TextDisplay panelTitle = new TextDisplay(1, false, new Vec2D((float) Engine.getNativeWidth() / 2, 950f), panelTitleString, Text.menuButtonLabelStyle, TextAlignment.CENTER);
        menuScreen.addVisual(panelTitle);

        Vec2D attributeListStartPosition = new Vec2D(300f, 700f);
        buildAttributeFields(editionData, attributeListStartPosition);
    }

    private void buildAttributeFields(EditionData editionData, Vec2D startPosition) {
        List<Attribute> attributeList = editionData.getAttributes();
        Vec2D attributeStride = new Vec2D(0f, -45f);
        int layer = 1;
        float coupleFieldSpacing = 250f;
        Vec2D fieldSpacing = new Vec2D(500f, 0f);
        DecimalFormat df = new DecimalFormat("0.000");


        Vec2D attributePosition = new Vec2D(startPosition);
        for (Attribute attribute : attributeList) {
            TextDisplay attributeText = new TextDisplay(layer, false, attributePosition, attribute.getName() + ":", Text.menuButtonLabelStyle, TextAlignment.LEFT);
            menuScreen.addVisual(attributeText);
            Vec2D fieldPosition = attributePosition.add(fieldSpacing);
            switch (attribute) {
                case BooleanAttribute booleanAttribute -> {
                    Vec2D fieldOffset = new Vec2D(16f, 0f);
                    BooleanField checkbox = Checkbox(layer, fieldPosition.add(fieldOffset), booleanAttribute.getValue());
                    menuScreen.addItem(checkbox);
                    attributeItemMap.put(attribute, List.of(checkbox));
                    attributePosition = attributePosition.add(attributeStride);
                }

                case DoubleAttribute doubleAttribute -> {
                    float fieldWidthPixels = 150f;
                    Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                    TextField textField = EditorTextField(layer, fieldPosition.add(fieldOffset), fieldWidthPixels, df.format(doubleAttribute.getValue()));
                    menuScreen.addItem(textField);
                    attributeItemMap.put(attribute, List.of(textField));
                    attributePosition = attributePosition.add(attributeStride);
                }

                case EditionDataAttribute<?> editionDataAttribute -> {
                    List<Attribute> editionDataAttributeList = editionDataAttribute.getData().getAttributes();
                    Vec2D subEditionDataOffset = new Vec2D(60f, attributeStride.y);
                    buildAttributeFields(editionDataAttribute.getData(), attributePosition.add(subEditionDataOffset));
                    attributePosition = attributePosition.add(attributeStride.scalar(editionDataAttributeList.size() + 1));
                }

                case FloatAttribute floatAttribute -> {
                    float fieldWidthPixels = 150f;
                    Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                    TextField textField = EditorTextField(layer, fieldPosition.add(fieldOffset), fieldWidthPixels, df.format(floatAttribute.getValue()));
                    menuScreen.addItem(textField);
                    attributeItemMap.put(attribute, List.of(textField));
                    attributePosition = attributePosition.add(attributeStride);
                }

                case IntegerAttribute integerAttribute -> {
                    float fieldWidthPixels = 100f;
                    Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                    TextField textField = EditorTextField(layer, fieldPosition.add(fieldOffset), fieldWidthPixels, Integer.toString(integerAttribute.getValue()));
                    menuScreen.addItem(textField);
                    attributeItemMap.put(attribute, List.of(textField));
                    attributePosition = attributePosition.add(attributeStride);
                }

                case IVec2DAttribute iVec2DAttribute -> {
                    float fieldWidthPixels = 150f;
                    float fieldLabelOffset = 50f;
                    TextDisplay fieldLabel1 = new TextDisplay(layer, false, fieldPosition, "x:", Text.menuButtonLabelStyle, TextAlignment.LEFT);
                    menuScreen.addVisual(fieldLabel1);
                    Vec2D field1Offset = new Vec2D(fieldWidthPixels / 2 + fieldLabelOffset, 0f);
                    TextField textField1 = EditorTextField(layer, fieldPosition.add(field1Offset), fieldWidthPixels, Integer.toString(iVec2DAttribute.getX()));
                    menuScreen.addItem(textField1);
                    Vec2D fieldLabel2Position = new Vec2D(fieldPosition.x + coupleFieldSpacing, fieldPosition.y);
                    TextDisplay fieldDisplay2 = new TextDisplay(layer, false, fieldLabel2Position, "y:", Text.menuButtonLabelStyle, TextAlignment.LEFT);
                    menuScreen.addVisual(fieldDisplay2);
                    Vec2D field2Offset = new Vec2D(fieldWidthPixels / 2 + fieldLabelOffset + coupleFieldSpacing, 0f);
                    TextField textField2 = EditorTextField(layer, fieldPosition.add(field2Offset), fieldWidthPixels, Integer.toString(iVec2DAttribute.getY()));
                    menuScreen.addItem(textField2);
                    attributeItemMap.put(attribute, List.of(textField1, textField2));
                    attributePosition = attributePosition.add(attributeStride);
                }

                case ListAttribute<?> ignored -> {
                }

                case StringAttribute stringAttribute -> {
                    float fieldWidthPixels = 300f;
                    Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                    TextField textField = EditorTextField(layer, fieldPosition.add(fieldOffset), fieldWidthPixels, stringAttribute.getValue());
                    menuScreen.addItem(textField);
                    attributeItemMap.put(attribute, List.of(textField));
                    attributePosition = attributePosition.add(attributeStride);
                }

                case Vec2DAttribute vec2DAttribute -> {
                    float fieldWidthPixels = 150f;
                    float fieldLabelOffset = 50f;
                    TextDisplay fieldLabel1 = new TextDisplay(layer, false, fieldPosition, "x:", Text.menuButtonLabelStyle, TextAlignment.LEFT);
                    menuScreen.addVisual(fieldLabel1);
                    Vec2D field1Offset = new Vec2D(fieldWidthPixels / 2 + fieldLabelOffset, 0f);
                    TextField textField1 = EditorTextField(layer, fieldPosition.add(field1Offset), fieldWidthPixels, df.format(vec2DAttribute.getX()));
                    menuScreen.addItem(textField1);
                    Vec2D fieldLabel2Position = new Vec2D(fieldPosition.x + coupleFieldSpacing, fieldPosition.y);
                    TextDisplay fieldDisplay2 = new TextDisplay(layer, false, fieldLabel2Position, "y:", Text.menuButtonLabelStyle, TextAlignment.LEFT);
                    menuScreen.addVisual(fieldDisplay2);
                    Vec2D field2Offset = new Vec2D(fieldWidthPixels / 2 + fieldLabelOffset + coupleFieldSpacing, 0f);
                    TextField textField2 = EditorTextField(layer, fieldPosition.add(field2Offset), fieldWidthPixels, df.format(vec2DAttribute.getY()));
                    menuScreen.addItem(textField2);
                    attributeItemMap.put(attribute, List.of(textField1, textField2));
                    attributePosition = attributePosition.add(attributeStride);
                }
            }
        }
    }

    private void updateAttributes() throws NumberFormatException {
        for (var entry : attributeItemMap.entrySet()) {
            Attribute attribute = entry.getKey();
            List<MenuItem> items = entry.getValue();

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

                case EditionDataAttribute editionDataAttribute -> {
                    assert false : "invalid attribute type";
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
                    iVec2DAttribute.setX(xValue);
                    iVec2DAttribute.setY(yValue);
                }

                case ListAttribute listAttribute -> {

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
                    vec2DAttribute.setX(xValue);
                    vec2DAttribute.setY(yValue);
                }
            }
        }
    }
}
