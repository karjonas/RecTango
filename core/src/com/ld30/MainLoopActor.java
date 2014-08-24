/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.ld30;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationAdapter;



import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.*;
import com.badlogic.gdx.utils.viewport.*;
import java.util.ArrayList;

import java.util.ArrayList;



/**
 *
 * @author jonas
 */
public class MainLoopActor extends Actor {
    
    Board upperBoard;
    Board lowerBoard;
    
    Stage stage;
    ScreenViewport stageViewport;

    LevelCollection levels;
    LayoutData layout;

    int lastPressedKey = 0;
    boolean flipBlocksUpper = false;
    boolean flipBlocksLower = false;

    int levelId = 4;

    public enum State {
        RUNNING, FADEINLEVEL, FADEOUTLEVEL
    };
    
    State state = State.FADEINLEVEL;
    
    float fadeInTime = 1.0f;
    float currentFadeTime = 0.0f;
    //float currentAlpha = 0.0f;

    public MainLoopActor() {
                setBounds(getX(), getY(), 8, 8);
        setOrigin(getWidth()/2, getHeight()/2);
    }
    
    public void create() {
        upperBoard = new Board(true, this);
        lowerBoard = new Board(false, this);
        stageViewport = new ScreenViewport();
        layout = new LayoutData();

        levels = new LevelCollection();
        stage = new Stage(stageViewport);
        stage.addActor(upperBoard);
        stage.addActor(lowerBoard);
        stage.addActor(upperBoard.unit);
        stage.addActor(lowerBoard.unit);
        stage.addActor(this);
    }

    @Override
    public void act(float delta) {
        if(state == State.FADEINLEVEL) {
            if(currentFadeTime == 0) {
                upperBoard.addAction(Actions.fadeIn(fadeInTime));
                lowerBoard.addAction(Actions.fadeIn(fadeInTime));

                
                    levels.setLevel(levelId);
                    upperBoard.setupBoard(levels.positionsUpper, levels.upper_unit_x, levels.upper_unit_y);
                    lowerBoard.setupBoard(levels.positionsLower, levels.lower_unit_x, levels.lower_unit_y);
            }
            currentFadeTime += delta;
            
            if (currentFadeTime >= fadeInTime) {
                state = State.RUNNING;
                //currentAlpha = 1.0f;
            }
            
            //currentAlpha = currentFadeTime/fadeInTime;
        } else if (state == State.FADEOUTLEVEL) {
            if(upperBoard.getActions().size == 0 && lowerBoard.getActions().size == 0) {
                state = State.FADEINLEVEL;
            }
        } else if (state == State.RUNNING) {
            if (lastPressedKey != 0) {
                keyDown(lastPressedKey);
            }

            { // Flip blocks
                if (flipBlocksUpper && !flipBlocksLower) {
                    upperBoard.flipBlocks();
                } else if (!flipBlocksUpper && flipBlocksLower) {
                    lowerBoard.flipBlocks();
                }

                flipBlocksUpper = false;
                flipBlocksLower = false;
            }

            { // Check if level completed
                if (upperBoard.isCompleted() && lowerBoard.isCompleted()) {
                    levelId++;
                    currentFadeTime = 0.0f;
                    state = State.FADEOUTLEVEL;
                    upperBoard.addAction(Actions.fadeOut(0.5f));
                    lowerBoard.addAction(Actions.fadeOut(0.5f));
                }
            }
        }
    }
    
    @Override
    public void draw(Batch batch, float alpha) {
        //upperBoard.draw(batch, alpha);
        //lowerBoard.draw(batch, alpha);
    }
    
    public void draw() {
        stage.draw();
    }
    
    public boolean keyDown(int keycode) {
        lastPressedKey = keycode;

        if (state == State.FADEINLEVEL
                || upperBoard.unit.getActions().size != 0
        || lowerBoard.unit.getActions().size != 0) {
            return false;
        }

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
        return true;
    }
    
    void keyUp(int keycode) {
        if (lastPressedKey == keycode) {
            lastPressedKey = 0;
        }
    };
    
    public void onFlipBlock(Board board) {
        if (board == upperBoard) {
            flipBlocksLower = true;
        } else {
            flipBlocksUpper = true;
        }
    }
    
    public void preRender() {
        upperBoard.drawBoard(1.0f);
        lowerBoard.drawBoard(1.0f);
    }
    
    public void setCamera(Matrix4 camera) {
        upperBoard.projectionMatrix = camera;
        lowerBoard.projectionMatrix = camera;
    }
    
    public void resize(int width, int height) {
        layout.calculateLayout();
        
        stage.getViewport().update(width, height, true);
        OrthographicCamera camera = ((OrthographicCamera) stageViewport.getCamera());
        camera.setToOrtho(false, layout.width, layout.height);
        camera.translate(-layout.width / 2, -layout.height / 2);
        
        setCamera(camera.combined);
    }
}