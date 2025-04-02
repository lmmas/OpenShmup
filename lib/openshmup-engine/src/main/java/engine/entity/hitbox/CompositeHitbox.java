package engine.entity.hitbox;

import engine.Vec2D;
import engine.render.Texture;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

import static org.lwjgl.stb.STBImage.*;

public class CompositeHitbox implements Hitbox{
    private Vec2D position;
    private Vec2D size;
    private ArrayList<HitboxRectangle> rectangleList;
    private ArrayList<Vec2D> rectangleRelativePositions;
    private ArrayList<Vec2D> rectangleRelativeSizes;
    public CompositeHitbox(String textureFilepath, float sizeX, float sizeY){
        this.position = new Vec2D(0.0f, 0.0f);
        this.size = new Vec2D(sizeX, sizeY);
        Texture texture = Texture.getTexture(textureFilepath);
        int imageWidth = texture.getWidth();
        int imageHeight = texture.getHeight();

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);

        ByteBuffer image = stbi_load(textureFilepath, width, height, channels, 1);
        byte[] bytes = new byte[image.capacity()];
        image.get(bytes);
        int[] colorValues = new int[bytes.length];
        for(int i = 0; i < bytes.length; i++){
            colorValues[i] = bytes[i] & 0xFF;
        }
        boolean[][] rectanglePositions = new boolean[imageHeight][imageWidth];
        int detectionMargin = 3;
        for(int i = 0; i < imageHeight; i++){
            for(int j = 0; j < imageWidth; j++){
                if((bytes[i * imageWidth + j] & 0xFF) + detectionMargin >= 255){
                    rectanglePositions[i][j] = true;
                }
            }
        }
        generateSimplifiedRectangles(rectanglePositions);
    }

    private void generateSimplifiedRectangles(boolean[][] rectanglePositions){
        int pixelWidth = rectanglePositions[0].length;
        int pixelHeight = rectanglePositions.length;
        ArrayList<ArrayList<Vec2D>> tempPositionsList = new ArrayList<>(pixelHeight);
        ArrayList<ArrayList<Vec2D>> tempSizesList = new ArrayList<>(pixelHeight);
        for(int i = 0; i < pixelHeight; i++){
            ArrayList<Vec2D> positionRowList = new ArrayList<>(pixelWidth);
            ArrayList<Vec2D> sizeRowList = new ArrayList<>(pixelWidth);
            boolean startingNewRectangle = false;
            int rectangleStartIndex = 0;
            for(int j = 0; j < pixelWidth; j++){
                if(rectanglePositions[i][j] == true){
                    if(!startingNewRectangle){
                        startingNewRectangle = true;
                        rectangleStartIndex = j;
                    }
                }
                else{
                    if(startingNewRectangle == true){
                        positionRowList.add(new Vec2D(
                                (float)(rectangleStartIndex + j) / 2 / pixelWidth,
                                ((float) i + 0.5f) /pixelHeight
                        ));
                        sizeRowList.add(new Vec2D(
                                (float)(j - rectangleStartIndex) / pixelWidth,
                                (float) 1 / pixelHeight
                        ));
                        startingNewRectangle = false;
                    }
                }
            }
            if(startingNewRectangle == true){
                positionRowList.add(new Vec2D(
                        (float)(rectangleStartIndex + pixelWidth) / 2 / pixelWidth,
                        ((float) i + 0.5f) /pixelHeight
                ));
                sizeRowList.add(new Vec2D(
                        (float)(pixelWidth - rectangleStartIndex) / pixelWidth,
                        (float) 1 / pixelHeight
                ));
            }
            tempPositionsList.add(positionRowList);
            tempSizesList.add(sizeRowList);
        }
        this.rectangleRelativePositions = new ArrayList<>(pixelHeight);
        for(var positionRow: tempPositionsList){
            this.rectangleRelativePositions.addAll(positionRow);
        }
        this.rectangleRelativeSizes = new ArrayList<>(pixelHeight);
        for(var sizeRow: tempSizesList){
            this.rectangleRelativeSizes.addAll(sizeRow);
        }
        this.rectangleList = new ArrayList<>(rectangleRelativePositions.size());
        for(int i = 0; i < rectangleRelativePositions.size(); i++){
            rectangleList.add(new HitboxRectangle(0.0f,0.0f,0.0f,0.0f));
        }
        setSize(size.x, size.y);
    }

    @Override
    public void setPosition(float positionX, float positionY) {
        Vec2D bottomLeftCornerPosition = new Vec2D(positionX - size.x / 2, positionY - size.y / 2);
        for(int i = 0; i < rectangleRelativePositions.size(); i++){
            rectangleList.get(i).setPosition(
                    bottomLeftCornerPosition.x + size.x * rectangleRelativePositions.get(i).x,
                    bottomLeftCornerPosition.y + size.y * rectangleRelativePositions.get(i).y
            );
        }
    }

    @Override
    public void setSize(float sizeX, float sizeY) {
        for(int i = 0; i < rectangleList.size(); i++){
            rectangleList.get(i).setSize(sizeX * rectangleRelativeSizes.get(i).x, sizeY * rectangleRelativeSizes.get(i).y);
        }
        setPosition(position.x, position.y);
    }

    @Override
    public void setOrientation(float orientationRadians) {

    }

    @Override
    public boolean intersects(Hitbox otherHitbox) {
        return false;
    }

    @Override
    public Hitbox copy() {
        return null;
    }
}
