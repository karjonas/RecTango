/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ld30;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.removeActor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.scaleTo;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.math.Matrix4;

import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

/**
 *
 * @author jonas
 */
public class Board extends Actor {

    int num_width = 20;
    int num_height = 9;
    int block_width = 8;
    int block_height = 8;

    char positions[][] = new char[num_height][num_width];

    int total_width = num_width * block_width;
    int total_height = num_height * block_height;

    int offset_x = -(num_width * block_width) / 2;
    int offset_y = -(num_height * block_height) / 2;

    ShapeRenderer shapeRenderer;
    Matrix4 projectionMatrix;

    Color wallColor;
    Color floorColor;
    Color passColor;
    Color stopColor;
    
    Unit unit;
    LD30 mainLoop;
    
    Texture textureHappy;
    Texture textureSad;
    
    boolean onFlipBlock = false;
    
    public Board(boolean is_upper, LD30 _mainLoop) {
        if (is_upper) {
            offset_y += total_height / 2;
            wallColor = new Color(139 / 255f, 149 / 255f, 109 / 255f, 1.0f);
            floorColor = new Color(31 / 255f, 31 / 255f, 31 / 255f, 1.0f);

            textureHappy = new Texture(Gdx.files.internal("block_upper_happy.png"));
            textureSad = new Texture(Gdx.files.internal("block_upper_sad.png"));
        } else {
            offset_y -= total_height / 2;
            wallColor = new Color(31 / 255f, 31 / 255f, 31 / 255f, 1.0f);
            floorColor = new Color(139 / 255f, 149 / 255f, 109 / 255f, 1.0f);
            
            textureHappy = new Texture(Gdx.files.internal("block_lower_happy.png"));
            textureSad = new Texture(Gdx.files.internal("block_lower_sad.png"));
        }
        
        passColor = new Color(0.0f,1.0f,0.0f,1.0f);
        stopColor = new Color(1.0f,0.0f,0.0f,1.0f);

        
        setWidth(num_width * block_width);
        setHeight(num_height * block_height);

        shapeRenderer = new ShapeRenderer();
        unit = new Unit(this, _mainLoop);
    }
    
    public void setupBoard(char _positions[][], int unit_x, int unit_y) {
        positions = _positions;
        unit.pos_x = unit_x;
        unit.pos_y = unit_y;
        unit.clearActions();
        unit.setX(offset_x + block_width * unit.pos_x);
        unit.setY(offset_y + block_height * unit.pos_y);
        unit.setRotation(0);
    }
    
    public boolean isCompleted() {
        return (positions[unit.pos_y][unit.pos_x] == 'e' && unit.getActions().size == 0);
    }
    
    public void flipBlocks() {
        for (int w = 0; w < num_width; w++) {
            for (int h = 0; h < num_height; h++) {
                if (positions[h][w] == 'r') {
                    positions[h][w] = 'g';
                }
                else if(positions[h][w] == 'g') {
                    positions[h][w] = 'r';
                }
            }
        }
    }
    
    public boolean isOnFlipBlock() {
        if(onFlipBlock) {
            onFlipBlock = false;
            return true;
        }
        return false;
    }
    
    void moveLeft() {
        unit.moveLeft();
    }

    void moveRight() {
        unit.moveRight();
    }

    void moveDown() {
        unit.moveDown();
    }

    void moveUp() {
        unit.moveUp();
    }

    void draw_block_at(int h, int w, Color color) {
        int x = offset_x + block_width * w;
        int y = offset_y + block_height * h;
        
        shapeRenderer.setProjectionMatrix(projectionMatrix);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(color.r, color.g, color.b, color.a);
        shapeRenderer.rect(x, y, block_width, block_height);
        shapeRenderer.end();
        
    }    
    
    public void drawBoard() {
        for (int w = 0; w < num_width; w++) {
            for (int h = 0; h < num_height; h++) {
                if (positions[h][w] == 'w') {
                    draw_block_at(h, w, wallColor);
                }
                else if(positions[h][w] == '0') {
                    draw_block_at(h,w, floorColor);
                } else if(positions[h][w] == 'r') {
                    draw_block_at(h,w, stopColor);
                } else if(positions[h][w] == 'g') {
                    draw_block_at(h,w, passColor);
                }
            }
        }
    }
    
    public void drawHappySadBlocks(Batch batch, float alpha) {
        for (int w = 0; w < num_width; w++) {
            for (int h = 0; h < num_height; h++) {
                int x = offset_x + w*block_width;
                int y = offset_y + h*block_height;
                if (positions[h][w] == 'r') {
                    batch.draw(textureSad, x, y);
                } else if (positions[h][w] == 'g') {
                    batch.draw(textureHappy, x, y);
                }
            }
        }
    }
    
    @Override
    public void draw(Batch batch, float alpha) {
        drawHappySadBlocks(batch,alpha);
        unit.draw(batch, alpha);
    }

    @Override
    public void act(float delta) {
        unit.act(delta);
    }
}