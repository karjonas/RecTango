/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ld30;

import com.badlogic.gdx.Gdx;

/**
 *
 * @author jonas
 */
public class LayoutData {
    int scaling = 1;
    int width = 160;
    int height = 144;
    
    int max_x = 160;
    int max_y = (144 * 5)/4;
    
    void calculateLayout() {
        int scaling_x = Gdx.graphics.getWidth() / max_x;
        int scaling_y = Gdx.graphics.getHeight() / max_y;
        scaling = Math.max(1,Math.min(scaling_x, scaling_y));

        width = Gdx.graphics.getWidth() / scaling;
        height = Gdx.graphics.getHeight() / scaling;
    }
            
            
}
