package com.rectango;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import java.util.HashMap;
/**
 *
 * @author jonas
 */
public class LevelCollection {

    int num_width = 20;
    int num_height = 9;
    BlockType positionsUpper[][];
    BlockType positionsLower[][];
    int currentLevel = 0;
    
    int upper_unit_x = 0;
    int upper_unit_y = 0;
    
    int lower_unit_x = 0;
    int lower_unit_y = 0;
    
    Pixmap currentLevelMap;
    HashMap<Integer, BlockType> pixelToBlockMap = new HashMap<Integer,BlockType>();
    
    LevelCollection() {
        pixelToBlockMap.put(-16776961, BlockType.WALL);
        pixelToBlockMap.put(16711935 , BlockType.UNIT);
        pixelToBlockMap.put(65535    , BlockType.EXIT);
        pixelToBlockMap.put(-65281   , BlockType.STOP);
        pixelToBlockMap.put(16777215 , BlockType.PASS);
        pixelToBlockMap.put(-1       , BlockType.DEATH); 
        pixelToBlockMap.put(255      , BlockType.EMPTY);
    }
    
    void readPixmapBoard(boolean isLowerBoard) {
        int h_offset = isLowerBoard ? num_height : 0;
        BlockType positions[][] = new BlockType[num_height][num_width];
        int unit_x = 0;
        int unit_y = 0;
        
        for (int w = 0; w < num_width; w++) {
            for (int h = 0; h < num_height; h++) {
                int pixel = currentLevelMap.getPixel(w, h + h_offset);

                BlockType blockType = pixelToBlockMap.get(pixel);

                if (blockType == BlockType.UNIT) {
                    unit_x = w;
                    unit_y = num_height - h - 1;
                    blockType = BlockType.EMPTY;
                }
                positions[num_height - h - 1][w] = blockType;
            }
        }
        
        if (isLowerBoard) {
            lower_unit_x = unit_x;
            lower_unit_y = unit_y;
            positionsLower = positions;
        } else {
            upper_unit_x = unit_x;
            upper_unit_y = unit_y;
            positionsUpper = positions;
        }
    }
    
    void readPixmap() {
        readPixmapBoard(false);
        readPixmapBoard(true);
    }
    
    void setLevel(int levelId) {
        currentLevelMap = new Pixmap(Gdx.files.internal("level" + levelId + ".png"));
        readPixmap();
    }
}
