package engine.hitbox;

import engine.assets.Texture;
import engine.types.IVec2D;
import engine.types.Vec2D;
import lombok.Getter;

import java.nio.ByteBuffer;
import java.util.ArrayList;

final public class CompositeHitbox implements Hitbox {

    private final Vec2D position;

    private final Vec2D size;

    @Getter
    private ArrayList<Hitbox> rectangleList;

    private ArrayList<Vec2D> rectangleRelativePositions;

    private ArrayList<Vec2D> rectangleRelativeSizes;

    public CompositeHitbox(Texture texture, Vec2D size) {
        this.position = Vec2D.ZERO;
        this.size = new Vec2D(size);
        int imageWidth = texture.getResolution().x;
        int imageHeight = texture.getResolution().y;
        int textureChannelCount = texture.getChannelCount();
        ByteBuffer image = texture.getImageBuffer();
        byte[] bytes = new byte[image.capacity()];
        image.get(bytes);
        image.flip();
        boolean[][] rectanglePositions = new boolean[imageHeight][imageWidth];
        int detectionMargin = 3;
        for (int i = 0; i < imageHeight; i++) {
            for (int j = 0; j < imageWidth; j++) {
                if ((bytes[(i * imageWidth + j) * textureChannelCount] & 0xFF) + detectionMargin >= 255) {
                    rectanglePositions[i][j] = true;
                }
            }
        }
        generateSimplifiedRectangles(rectanglePositions);
    }

    public CompositeHitbox(Vec2D position, Vec2D size, ArrayList<Hitbox> rectangleList, ArrayList<Vec2D> rectangleRelativePositions, ArrayList<Vec2D> rectangleRelativeSizes) {
        //this constructor should only be used for copying
        this.position = position;
        this.size = size;
        this.rectangleList = rectangleList;
        this.rectangleRelativePositions = rectangleRelativePositions;
        this.rectangleRelativeSizes = rectangleRelativeSizes;
    }

    private void generateSimplifiedRectangles(boolean[][] rectanglePositions) {
        int pixelWidth = rectanglePositions[0].length;
        int pixelHeight = rectanglePositions.length;
        ArrayList<ArrayList<Vec2D>> tempPositionsList1 = new ArrayList<>(pixelHeight);
        ArrayList<ArrayList<Vec2D>> tempSizesList1 = new ArrayList<>(pixelHeight);
        for (int i = 0; i < pixelHeight; i++) {
            ArrayList<Vec2D> positionRowList = new ArrayList<>(pixelWidth);
            ArrayList<Vec2D> sizeRowList = new ArrayList<>(pixelWidth);
            boolean startingNewRectangle = false;
            int rectangleStartIndex = 0;
            for (int j = 0; j < pixelWidth; j++) {
                if (rectanglePositions[i][j]) {
                    if (!startingNewRectangle) {
                        startingNewRectangle = true;
                        rectangleStartIndex = j;
                    }
                }
                else {
                    if (startingNewRectangle) {
                        positionRowList.add(new Vec2D(
                            (float) (rectangleStartIndex + j) / 2 / pixelWidth,
                            ((float) i + 0.5f) / pixelHeight
                        ));
                        sizeRowList.add(new Vec2D(
                            (float) (j - rectangleStartIndex) / pixelWidth,
                            (float) 1 / pixelHeight
                        ));
                        startingNewRectangle = false;
                    }
                }
            }
            if (startingNewRectangle) {
                positionRowList.add(new Vec2D(
                    (float) (rectangleStartIndex + pixelWidth) / 2 / pixelWidth,
                    ((float) i + 0.5f) / pixelHeight
                ));
                sizeRowList.add(new Vec2D(
                    (float) (pixelWidth - rectangleStartIndex) / pixelWidth,
                    (float) 1 / pixelHeight
                ));
            }
            tempPositionsList1.add(positionRowList);
            tempSizesList1.add(sizeRowList);
        }
        this.rectangleRelativePositions = new ArrayList<>(pixelHeight);
        this.rectangleRelativeSizes = new ArrayList<>(pixelHeight);
        ArrayList<IVec2D> rectanglesToMerge = new ArrayList<>();
        while (!tempPositionsList1.stream().allMatch(ArrayList::isEmpty)) {
            Vec2D referencePosition = null;
            Vec2D referenceSize = null;
            for (int i = 0; i < tempPositionsList1.size(); i++) {
                if (!tempPositionsList1.get(i).isEmpty()) {
                    referencePosition = tempPositionsList1.get(i).get(0);
                    referenceSize = tempSizesList1.get(i).get(0);
                    rectanglesToMerge.add(new IVec2D(0, i));
                    break;
                }
            }
            for (int i = rectanglesToMerge.getFirst().y + 1; i < tempPositionsList1.size(); i++) {
                boolean rectangleFound = false;
                int j = 0;
                while (j < tempPositionsList1.get(i).size() && !rectangleFound) {
                    assert referenceSize != null;
                    if (tempSizesList1.get(i).get(j).x == referenceSize.x && tempPositionsList1.get(i).get(j).x == referencePosition.x) {
                        rectanglesToMerge.add(new IVec2D(j, i));
                        rectangleFound = true;
                    }
                    j++;
                }
                if (!rectangleFound) {
                    break;
                }
            }
            IVec2D lastMergedRectangleIndexes = rectanglesToMerge.getLast();
            assert referencePosition != null;
            Vec2D mergedRectanglePosition = new Vec2D(referencePosition.x, (referencePosition.y + tempPositionsList1.get(lastMergedRectangleIndexes.y).get(lastMergedRectangleIndexes.x).y) / 2);
            Vec2D mergedRectangleSize = new Vec2D(referenceSize.x, 1.0f / pixelHeight * rectanglesToMerge.size());
            this.rectangleRelativePositions.add(mergedRectanglePosition);
            this.rectangleRelativeSizes.add(mergedRectangleSize);
            for (IVec2D index : rectanglesToMerge) {
                ArrayList<Vec2D> rowList = tempPositionsList1.get(index.y);
                rowList.remove(index.x);
            }
            rectanglesToMerge.clear();
        }
        this.rectangleList = new ArrayList<>(rectangleRelativePositions.size());
        for (int i = 0; i < rectangleRelativePositions.size(); i++) {
            rectangleList.add(new SimpleRectangleHitbox(Vec2D.ZERO, Vec2D.ZERO));
        }
        setSize(size);
    }

    @Override
    public boolean containsPoint(Vec2D position) {
        return rectangleList.stream().anyMatch((Hitbox hitbox) -> hitbox.containsPoint(position));
    }

    @Override
    public void setPosition(Vec2D position) {
        Vec2D bottomLeftCornerPosition = position.add(size.scalar(-0.5f));
        for (int i = 0; i < rectangleRelativePositions.size(); i++) {
            rectangleList.get(i).setPosition(
                bottomLeftCornerPosition.add(size.multiply(rectangleRelativePositions.get(i))));
        }
    }

    @Override
    public void setSize(Vec2D size) {
        for (int i = 0; i < rectangleList.size(); i++) {
            rectangleList.get(i).setSize(size.multiply(rectangleRelativeSizes.get(i)));
        }
        setPosition(position);
    }

    @Override
    public void setOrientation(float orientationRadians) {

    }

    @Override
    public Hitbox copy() {
        ArrayList<Hitbox> rectanglesCopy = new ArrayList<>(rectangleList.size());
        for (var rectangle : rectangleList) {
            rectanglesCopy.add(rectangle.copy());
        }
        return new CompositeHitbox(position, size, rectanglesCopy, rectangleRelativePositions, rectangleRelativeSizes);
    }

}
