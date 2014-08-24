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
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Camera;

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

    BitmapFont font;
    Sound flipSound;
    Sound winSound;

    int lastPressedKey = 0;
    boolean flipBlocksUpper = false;
    boolean flipBlocksLower = false;

    int levelId = 7;
    int maxLevelId = 8;

    public enum State {
        SPLASHSCREEN, RUNNING, FADEINLEVEL, FADEOUTLEVEL, FINSIHED
    };
    
    State state = State.SPLASHSCREEN;
    
    float fadeInTime = 1.0f;

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
        
        upperBoard.addAction(Actions.fadeOut(0));
        lowerBoard.addAction(Actions.fadeOut(0));
        
        font = new BitmapFont(Gdx.files.internal("PressStart2P.fnt"),
                Gdx.files.internal("PressStart2P.png"), false);
        font.setColor(196 / 255f, 207 / 255f, 161 / 255f, 1.0f);
        flipSound = Gdx.audio.newSound(Gdx.files.internal("blip.wav"));
        winSound = Gdx.audio.newSound(Gdx.files.internal("level_win.wav"));

    }

    @Override
    public void act(float delta) {
        if (state == State.SPLASHSCREEN) {

        } else if (state == State.FADEINLEVEL) {
            upperBoard.addAction(Actions.fadeIn(fadeInTime));
            lowerBoard.addAction(Actions.fadeIn(fadeInTime));

            levels.setLevel(levelId);
            upperBoard.setupBoard(levels.positionsUpper, levels.upper_unit_x, levels.upper_unit_y);
            lowerBoard.setupBoard(levels.positionsLower, levels.lower_unit_x, levels.lower_unit_y);

            state = State.RUNNING;
        } else if (state == State.FADEOUTLEVEL) {
            if (upperBoard.getActions().size == 0 && lowerBoard.getActions().size == 0) {
                if(levelId == maxLevelId) {
                    state = State.FINSIHED;
                } else {
                    state = State.FADEINLEVEL;
                }
            }
        } else if (state == State.RUNNING) {
            { // Flip blocks
                if (flipBlocksUpper && !flipBlocksLower) {
                    flipSound.play();
                    upperBoard.flipBlocks();
                } else if (!flipBlocksUpper && flipBlocksLower) {
                    lowerBoard.flipBlocks();
                    flipSound.play();
                }

                flipBlocksUpper = false;
                flipBlocksLower = false;
            }

            { // Check if level completed
                if (upperBoard.isCompleted() && lowerBoard.isCompleted()) {
                    levelId++;
                    state = State.FADEOUTLEVEL;
                    upperBoard.addAction(Actions.fadeOut(0.5f));
                    lowerBoard.addAction(Actions.fadeOut(0.5f));
                    winSound.play();
                }
            }
            
            if (lastPressedKey != 0) {
                keyDown(lastPressedKey);
            }
        }
    }
    
    @Override
    public void draw(Batch batch, float alpha) {
        if (state == State.SPLASHSCREEN) {
            BitmapFont.TextBounds textBounds = font.getWrappedBounds("PRESS ANY KEY", 1000);
            float x = -textBounds.width / 2;
            float y = textBounds.height / 2;
            font.draw(batch, "PRESS ANY KEY", x, y);
        } else if (state == State.FINSIHED) {
            BitmapFont.TextBounds textBounds = font.getWrappedBounds("THANKS FOR PLAYING!", 1000);
            float x = -textBounds.width / 2;
            float y = textBounds.height / 2;
            font.draw(batch, "THANKS FOR PLAYING!", x, y);
        }
    }
    
    public void draw() {
        stage.draw();
    }
    
    public boolean keyDown(int keycode) {
        lastPressedKey = keycode;

        if (state == State.FADEINLEVEL
                || upperBoard.unit.getActions().size != 0
                || lowerBoard.unit.getActions().size != 0
                || upperBoard.getActions().size != 0
                || lowerBoard.getActions().size != 0) {
            return false;
        } else if (state == State.SPLASHSCREEN) {
            state = State.FADEINLEVEL;
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
            case Keys.ESCAPE:
                Gdx.app.exit();
                break;
            case Keys.ENTER:
                if (state == State.FINSIHED) {
                    Gdx.app.exit();
                }
                break;
            case Keys.SPACE:
                if (state == State.FINSIHED) {
                    Gdx.app.exit();
                }
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
