package editor;

import editor.attribute.*;
import editor.editionData.*;
import engine.Engine;
import engine.menu.MenuScreen;
import engine.menu.item.*;
import engine.scene.visual.BorderedRoundedRectangle;
import engine.scene.visual.SceneVisual;
import engine.scene.visual.TextDisplay;
import engine.scene.visual.style.TextAlignment;
import engine.types.Vec2D;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static editor.MenuItems.Checkbox;
import static editor.MenuItems.EditorTextField;
import static editor.Style.*;

final public class EditPanel {

    final private EditionData editionData;

    final private List<List<MenuItem>> attributeItems;
    @Getter
    private MenuScreen menuScreen;

    public EditPanel(EditionData editionData) {
        this.editionData = editionData;
        this.attributeItems = new ArrayList<>();
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

        SceneVisual backgroundRectangle = new BorderedRoundedRectangle(0, 1500f, 900f, (float) Engine.getNativeWidth() / 2, (float) Engine.getNativeHeight() / 2, menuButtonRoundingRadius, menuButtonBorderWidth, Color.menuButtonColor.r, Color.menuButtonColor.g, Color.menuButtonColor.b, Color.menuButtonColor.a, Color.menuButtonBorderColor.r, Color.menuButtonBorderColor.g, Color.menuButtonColor.b, Color.menuButtonBorderColor.a);
        menuScreen.addVisual(backgroundRectangle);

        Vec2D closeButtonSize = new Vec2D(150, 50);
        ActionButton closeButton = ActionButtons.RoundedRectangleButton(1, closeButtonSize, new Vec2D(1800, 1000), menuButtonRoundingRadius, menuButtonBorderWidth, Color.menuButtonColor, Color.menuButtonBorderColor, "Close", menuButtonLabelStyle, (() -> Engine.getCurrentMenu().removeMenuScreen(menuScreen)));
        menuScreen.addItem(closeButton);

        String panelTitleString = getPanelTitle();
        TextDisplay panelTitle = new TextDisplay(1, false, (float) Engine.getNativeWidth() / 2, 950f, panelTitleString, menuButtonLabelStyle, TextAlignment.CENTER);
        menuScreen.addVisual(panelTitle);

        List<Attribute> attributeList = editionData.getAttributes();
        Vec2D attributeListTextStartPosition = new Vec2D(300f, 700f);
        Vec2D attributeFieldStartPosition = new Vec2D(800f, 700f);
        float attributListStride = -45f;
        for (int i = 0; i < attributeList.size(); i++) {
            Vec2D attributePosition = attributeListTextStartPosition.add(new Vec2D(0.0f, attributListStride * i));
            TextDisplay attributeText = new TextDisplay(1, false, attributePosition.x, attributePosition.y, attributeList.get(i).getName() + ":", menuButtonLabelStyle, TextAlignment.LEFT);
            menuScreen.addVisual(attributeText);
            buildAttributeFields(attributeList.get(i), 1, attributeFieldStartPosition.add(new Vec2D(0.0f, attributListStride * i)));
        }
    }

    private void buildAttributeFields(Attribute attribute, int layer, Vec2D position) {
        float coupleFieldSpacing = 250f;
        switch (attribute) {
            case BooleanAttribute booleanAttribute -> {
                Vec2D fieldOffset = new Vec2D(100f, 0f);
                BooleanField checkbox = Checkbox(layer, position.add(fieldOffset), booleanAttribute.getValue());
                menuScreen.addItem(checkbox);
                attributeItems.add(List.of(checkbox));
            }

            case DoubleAttribute doubleAttribute -> {
                float fieldWidthPixels = 150f;
                Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                TextField textField = EditorTextField(layer, position.add(fieldOffset), fieldWidthPixels, Double.toString(doubleAttribute.getValue()));
                menuScreen.addItem(textField);
                attributeItems.add(List.of(textField));
            }

            case EditionDataAttribute<?> editionDataAttribute -> {
                List<Attribute> attributeList = editionDataAttribute.getData().getAttributes();
                Vec2D attributeFieldStartPosition = position.add(new Vec2D(800f, 0f));
                float attributListStride = -45f;
                for (int i = 0; i < attributeList.size(); i++) {
                    Vec2D attributePosition = position.add(new Vec2D(0.0f, attributListStride * i));
                    TextDisplay attributeText = new TextDisplay(1, false, attributePosition.x, attributePosition.y, attributeList.get(i).getName() + ":", menuButtonLabelStyle, TextAlignment.LEFT);
                    menuScreen.addVisual(attributeText);
                    buildAttributeFields(attributeList.get(i), 1, attributeFieldStartPosition.add(new Vec2D(0.0f, attributListStride * i)));
                }
            }

            case FloatAttribute floatAttribute -> {
                float fieldWidthPixels = 150f;
                Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                TextField textField = EditorTextField(layer, position.add(fieldOffset), fieldWidthPixels, Float.toString(floatAttribute.getValue()));
                menuScreen.addItem(textField);
                attributeItems.add(List.of(textField));
            }

            case IntegerAttribute integerAttribute -> {
                float fieldWidthPixels = 150f;
                Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                TextField textField = EditorTextField(layer, position.add(fieldOffset), fieldWidthPixels, Integer.toString(integerAttribute.getValue()));
                menuScreen.addItem(textField);
                attributeItems.add(List.of(textField));
            }

            case IVec2DAttribute iVec2DAttribute -> {
                float fieldWidthPixels = 150f;
                float fieldLabelOffset = 50f;
                TextDisplay fieldLabel1 = new TextDisplay(layer, false, position.x, position.y, "x:", menuButtonLabelStyle, TextAlignment.LEFT);
                menuScreen.addVisual(fieldLabel1);
                Vec2D field1Offset = new Vec2D(fieldWidthPixels / 2 + fieldLabelOffset, 0f);
                TextField textField1 = EditorTextField(layer, position.add(field1Offset), fieldWidthPixels, Integer.toString(iVec2DAttribute.getX()));
                menuScreen.addItem(textField1);
                Vec2D fieldLabel2Position = new Vec2D(position.x + coupleFieldSpacing, position.y);
                TextDisplay fieldDisplay2 = new TextDisplay(layer, false, fieldLabel2Position.x, fieldLabel2Position.y, "y:", menuButtonLabelStyle, TextAlignment.LEFT);
                menuScreen.addVisual(fieldDisplay2);
                Vec2D field2Offset = new Vec2D(fieldWidthPixels / 2 + fieldLabelOffset + coupleFieldSpacing, 0f);
                TextField textField2 = EditorTextField(layer, position.add(field2Offset), fieldWidthPixels, Integer.toString(iVec2DAttribute.getY()));
                menuScreen.addItem(textField2);
                attributeItems.add(List.of(textField1, textField2));
            }

            case ListAttribute<?> ignored -> {
            }

            case StringAttribute stringAttribute -> {
                float fieldWidthPixels = 300f;
                Vec2D fieldOffset = new Vec2D(fieldWidthPixels / 2, 0f);
                TextField textField = EditorTextField(layer, position.add(fieldOffset), fieldWidthPixels, stringAttribute.getValue());
                menuScreen.addItem(textField);
                attributeItems.add(List.of(textField));
            }

            case Vec2DAttribute vec2DAttribute -> {
                float fieldWidthPixels = 150f;
                float fieldLabelOffset = 50f;
                TextDisplay fieldLabel1 = new TextDisplay(layer, false, position.x, position.y, "x:", menuButtonLabelStyle, TextAlignment.LEFT);
                menuScreen.addVisual(fieldLabel1);
                Vec2D field1Offset = new Vec2D(fieldWidthPixels / 2 + fieldLabelOffset, 0f);
                TextField textField1 = EditorTextField(layer, position.add(field1Offset), fieldWidthPixels, Float.toString(vec2DAttribute.getX()));
                menuScreen.addItem(textField1);
                Vec2D fieldLabel2Position = new Vec2D(position.x + coupleFieldSpacing, position.y);
                TextDisplay fieldDisplay2 = new TextDisplay(layer, false, fieldLabel2Position.x, fieldLabel2Position.y, "y:", menuButtonLabelStyle, TextAlignment.LEFT);
                menuScreen.addVisual(fieldDisplay2);
                Vec2D field2Offset = new Vec2D(fieldWidthPixels / 2 + fieldLabelOffset + coupleFieldSpacing, 0f);
                TextField textField2 = EditorTextField(layer, position.add(field2Offset), fieldWidthPixels, Float.toString(vec2DAttribute.getY()));
                menuScreen.addItem(textField2);
                attributeItems.add(List.of(textField1, textField2));
            }
        }
    }
}
