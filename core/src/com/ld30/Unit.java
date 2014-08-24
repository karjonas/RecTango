/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.graphics.Color;

/**
 *
 * @author jonas
 */
public class Unit extends Actor {

    Texture texture;
    Board board;
    
    int pos_x = 0;
    int pos_y = 0;
    
    int pos_x_prev = 0;
    int pos_y_prev = 0;
    
    boolean isMoving;

    public Unit(Board _board) {
        texture = new Texture(Gdx.files.internal("unit.png"));
        setBounds(getX(), getY(), 8, 8);
        setOrigin(getWidth()/2, getHeight()/2);
        board = _board;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * alpha);

        batch.draw(texture, this.getX(), this.getY(), this.getOriginX(), this.getOriginY(), this.getWidth(),
                this.getHeight(), this.getScaleX(), this.getScaleY(), this.getRotation(), 0, 0,
                texture.getWidth(), texture.getHeight(), false, false);
    }
    
    
    void tryMoveTo(int x, int y) {
        if(getActions().size > 0 || x < 0 || x >= board.num_width || y < 0 || y >=board.num_height)
            return;
        
        if(board.positions[y][x] != 'w' && board.positions[y][x] != 'r') {
            int new_x = board.offset_x + board.block_width*x;
            int new_y = board.offset_y + board.block_height*y;
            
            addAction(Actions.moveTo(new_x, new_y, 0.2f));
            
            if((pos_x - x) != 0) {
                addAction(Actions.rotateBy(90.0f*(pos_x-x), 0.2f));
            }
            else {
                addAction(Actions.rotateBy(90.0f*(pos_y-y), 0.2f));
            }

            if(board.positions[pos_y][pos_x] == 'g' && (pos_x_prev != x || pos_y_prev != y)) {
                board.onFlipBlock();
            }

            pos_x_prev = pos_x;
            pos_y_prev = pos_y;
            pos_x = x;
            pos_y = y;
            
        }
        
    }
    
    void moveLeft() {
        tryMoveTo(pos_x - 1, pos_y);
    }

    void moveRight() {
        tryMoveTo(pos_x + 1, pos_y);
    }

    void moveDown() {
        tryMoveTo(pos_x, pos_y - 1);
    }

    void moveUp() {
        tryMoveTo(pos_x, pos_y + 1);
    }
    
}
