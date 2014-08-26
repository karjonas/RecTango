/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    char positionsUpper[][] = new char[num_height][num_width];
    char positionsLower[][] = new char[num_height][num_width];
    int currentLevel = 0;
    
    int upper_unit_x = 0;
    int upper_unit_y = 0;
    
    int lower_unit_x = 0;
    int lower_unit_y = 0;
    
    
    Pixmap currentLevelMap;
    HashMap<Integer, Character> pixelToCharMap = new HashMap<Integer,Character>();
    
    LevelCollection() {
        pixelToCharMap.put(-16776961, 'w'); // wall
        pixelToCharMap.put(16711935, 'u'); // unit
        pixelToCharMap.put(65535, 'e'); // exit
        pixelToCharMap.put(-65281, 'r'); // Stop block
        pixelToCharMap.put(16777215, 'g'); // Go block
        pixelToCharMap.put(-1, 'd'); // death block
        pixelToCharMap.put(255, '0'); // nothing
    }

    void readPixmap() {
        for (int w = 0; w < num_width; w++) {
            for (int h = 0; h < num_height; h++) {
                int pixel = currentLevelMap.getPixel(w, h);
                Character asChar = pixelToCharMap.get(pixel);
                
                if(asChar == null) {
                   asChar = '0';
                }
                
                if(asChar == 'u') {
                    upper_unit_x = w;
                    upper_unit_y = num_height - h - 1;
                    asChar = '0';
                }
                
                positionsUpper[num_height - h - 1][w] = asChar;
            }
        }

        for (int w = 0; w < num_width; w++) {
            for (int h = 0; h < num_height; h++) {
                int pixel = currentLevelMap.getPixel(w, h + num_height);

                Character asChar = pixelToCharMap.get(pixel);

                if (asChar == null) {
                    asChar = '0';
                }
                
                if (asChar == 'u') {
                    lower_unit_x = w;
                    lower_unit_y = num_height - h - 1;
                    asChar = '0';
                }
                
                positionsLower[num_height - h - 1][w] = asChar;
            }
        }
    }
    
    void setLevel(int levelId) {
        currentLevelMap = new Pixmap(Gdx.files.internal("level" + levelId + ".png"));
        readPixmap();
    }
}
