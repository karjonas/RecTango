package com.ld30;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Color;

public class LD30 implements ApplicationListener, InputProcessor {

    MainLoopActor mainLoopActor;
    boolean paused = false;
    private Stage stage;
    Color bgColor;
    
    public void levelComplete() {
       Gdx.app.exit();
    }
    
    @Override
    public boolean keyDown(int keycode) {
        mainLoopActor.keyDown(keycode);
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        mainLoopActor.keyUp(keycode);
        return true;
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
        mainLoopActor = new MainLoopActor();
        mainLoopActor.create();
        bgColor = new Color(77/256f, 83/256f, 60/256f, 0.0f);
 
        Gdx.input.setInputProcessor(this);
        
        stage = mainLoopActor.stage;
        
        resize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight()); 
    }

    @Override
    public void render() {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, bgColor.a);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        stage.act(Gdx.graphics.getDeltaTime());
        //stage.draw();
        mainLoopActor.preRender();
        mainLoopActor.draw();
    }

    @Override
    public void resize(int width, int height) {
        mainLoopActor.resize(width, height);
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