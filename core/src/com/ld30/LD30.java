package com.ld30;

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

public class LD30 implements ApplicationListener, InputProcessor {

    boolean paused = false;
    private Stage stage;
    ScreenViewport stageViewport;
    LayoutData layout;
    LevelCollection levels;
    
    Color bgColor;
    Board upperBoard;
    Board lowerBoard;
    
    int levelId = 4;
    
    public void checkLevelCompleted() {
        if(upperBoard.isCompleted() && lowerBoard.isCompleted()) {
            levelId++;
          levels.setLevel(levelId);
          upperBoard.setupBoard(levels.positionsUpper, levels.upper_unit_x, levels.upper_unit_y);
          lowerBoard.setupBoard(levels.positionsLower, levels.lower_unit_x, levels.lower_unit_y);        
        }
    }
    
    public void flipBlocks() {
        if(upperBoard.isOnFlipBlock()) {
            lowerBoard.flipBlocks();
        }
        if (lowerBoard.isOnFlipBlock()) {
            upperBoard.flipBlocks();
        }
    }

    
    public void levelComplete() {
       Gdx.app.exit();
    }
    
    @Override
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
            case Keys.SPACE:
                break;
            case Keys.X:
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int x, int y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int x, int y) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    @Override
    public void create() {
        layout = new LayoutData();
        bgColor = new Color(77/256f, 83/256f, 60/256f, 0.0f);
        upperBoard = new Board(true, this);
        lowerBoard = new Board(false, this);
        levels = new LevelCollection();
        levels.setLevel(levelId);
        upperBoard.setupBoard(levels.positionsUpper, levels.upper_unit_x, levels.upper_unit_y);
        lowerBoard.setupBoard(levels.positionsLower, levels.lower_unit_x, levels.lower_unit_y);
        
        
        Gdx.input.setInputProcessor(this);
        
        stageViewport = new ScreenViewport();
        stage = new Stage(stageViewport);
        stage.addActor(upperBoard);
        stage.addActor(lowerBoard);
         
        
        resize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        upperBoard.drawBoard();
        lowerBoard.drawBoard();
        
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
        
        flipBlocks();
        checkLevelCompleted();
    }

    @Override
    public void resize(int width, int height) {
        layout.calculateLayout();

        stage.getViewport().update(width, height, true);
        OrthographicCamera camera = ((OrthographicCamera) stageViewport.getCamera());
        camera.setToOrtho(false, layout.width, layout.height);
        camera.translate(-layout.width / 2, -layout.height / 2);
        
        upperBoard.projectionMatrix = camera.combined;
        lowerBoard.projectionMatrix = camera.combined;
    }

    @Override
    public void pause() {
        paused = true;
    }

    @Override
    public void resume() {
        paused = false;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}