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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;



import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import java.util.ArrayList;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.graphics.Color;

/**
 *
 * @author jonas
 */
public class MainLoopActor extends Actor {
    
    Board upperBoard;
    Board lowerBoard;
    
    int lastPressedKey = 0;
    
    public MainLoopActor() {
        
    }

    @Override
    public void act(float delta) {
        if(lastPressedKey != 0)
            keyDown(lastPressedKey);
        upperBoard.act(delta);
        lowerBoard.act(delta);
    }
    
    @Override
    public void draw(Batch batch, float alpha) {
        upperBoard.draw(batch, alpha);
        lowerBoard.draw(batch, alpha);
    }
    
    public boolean keyDown(int keycode) {
        switch (keycode) {
            case Keys.LEFT:
                upperBoard.moveLeft();
                lowerBoard.moveLeft();
                break;
            case Keys.RIGHT:
                upperBoard.moveRight();
                lowerBoard.moveRight();
                break;
            case Keys.UP:
                upperBoard.moveUp();
                lowerBoard.moveUp();
                break;
            case Keys.DOWN:
                upperBoard.moveDown();
                lowerBoard.moveDown();
                break;
        }
        lastPressedKey = keycode;
        return true;
    }
    
    void keyUp(int keycode) {
        if (lastPressedKey == keycode) {
            lastPressedKey = 0;
        }
    };
    
    
}
