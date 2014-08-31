package com.rectango;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import java.util.HashMap;

/**
 *
 * @author jonas
 */

enum BlockType { EMPTY, WALL, UNIT, EXIT, STOP, PASS, DEATH }

public class Board extends Actor {

    int num_width = 20;
    int num_height = 9;
    int block_width = 8;
    int block_height = 8;

    BlockType positions[][] = new BlockType[num_height][num_width];

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
    RecTango mainLoop;
    
    Texture textureHappy;
    Texture textureSad;
    Texture textureDead;
    
    MainLoopActor mainLoopActor;
    HashMap<Integer,CustomBlock> customBlocks;
    
    float currentAlpha = 1.0f;
    
    public Board(boolean is_upper, MainLoopActor _mainLoopActor) {
        if (is_upper) {
            offset_y += total_height / 2;
            wallColor = new Color(139 / 255f, 149 / 255f, 109 / 255f, 1.0f);
            floorColor = new Color(31 / 255f, 31 / 255f, 31 / 255f, 1.0f);

            textureHappy = new Texture(Gdx.files.internal("block_upper_happy.png"));
            textureSad = new Texture(Gdx.files.internal("block_upper_sad.png"));
            textureDead = new Texture(Gdx.files.internal("block_upper_death.png"));

        } else {
            offset_y -= total_height / 2;
            wallColor = new Color(31 / 255f, 31 / 255f, 31 / 255f, 1.0f);
            floorColor = new Color(139 / 255f, 149 / 255f, 109 / 255f, 1.0f);
            
            textureHappy = new Texture(Gdx.files.internal("block_lower_happy.png"));
            textureSad = new Texture(Gdx.files.internal("block_lower_sad.png"));
            textureDead = new Texture(Gdx.files.internal("block_lower_death.png"));
        }
        
        passColor = new Color(0.0f,1.0f,0.0f,1.0f);
        stopColor = new Color(1.0f,0.0f,0.0f,1.0f);

        setWidth(num_width * block_width);
        setHeight(num_height * block_height);

        shapeRenderer = new ShapeRenderer();
        unit = new Unit(this);
        mainLoopActor = _mainLoopActor;
        customBlocks = new HashMap<Integer, CustomBlock>();
    }
    
    public int getHashKey(int h, int w) {
        return (num_width*h) + w;
    }
    
    public void setupBoard(BlockType _positions[][], int unit_x, int unit_y) {
        positions = _positions;
        unit.pos_x = unit_x;
        unit.pos_y = unit_y;
        unit.clearActions();
        unit.setX(offset_x + block_width * unit.pos_x);
        unit.setY(offset_y + block_height * unit.pos_y);
        unit.setRotation(0);
        
        customBlocks.clear();
        
        // Add custom actors
        for (int w = 0; w < num_width; w++) {
            for (int h = 0; h < num_height; h++) {
                
                int hashKey = getHashKey(h,w);
                int x = offset_x + w * block_width;
                int y = offset_y + h * block_height;
                
                if (positions[h][w] == BlockType.STOP) {
                    CustomBlock block = new CustomBlock(textureSad);
                    block.setBounds(x, y, block_width, block_height);
                    customBlocks.put(hashKey, block);
                } else if(positions[h][w] == BlockType.PASS) {
                    CustomBlock block = new CustomBlock(textureHappy);
                    block.setBounds(x, y, block_width, block_height);
                    customBlocks.put(hashKey, block);      
                } else if(positions[h][w] == BlockType.DEATH) {
                    CustomBlock block = new CustomBlock(textureDead);
                    block.setBounds(x, y, block_width, block_height);
                    customBlocks.put(hashKey, block);      
                }
            }
        }
        
    }
    
    public boolean isCompleted() {
        return (positions[unit.pos_y][unit.pos_x] == BlockType.EXIT
             && unit.getActions().size == 0);
    }

    public boolean isDead() {
        return (positions[unit.pos_y][unit.pos_x] == BlockType.DEATH);
    }
    
    public void flipBlocks() {
        for (int w = 0; w < num_width; w++) {
            for (int h = 0; h < num_height; h++) {
                int hashKey = getHashKey(h, w);
                if (positions[h][w] == BlockType.STOP) {
                    customBlocks.get(hashKey).texture = textureHappy;
                    positions[h][w] = BlockType.PASS;
                } else if(positions[h][w] == BlockType.PASS) {
                    customBlocks.get(hashKey).texture = textureSad;
                    positions[h][w] = BlockType.STOP;
                }
            }
        }
    }
    
    public void onFlipBlock() {
        mainLoopActor.onFlipBlock(this);
    }
    
    public boolean isFlipBlock(int h, int w) {
        return (positions[h][w] == BlockType.PASS);
    }
    
    public boolean isWalkable(int h, int w) {
        return (positions[h][w] != BlockType.WALL
             && positions[h][w] != BlockType.STOP);
    }
    
    public boolean isCustomBlock(int h, int w) {
        return (positions[h][w] == BlockType.PASS
             || positions[h][w] == BlockType.STOP
             || positions[h][w] == BlockType.DEATH);
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

    void draw_block_at(int h, int w, float alpha, Color color) {

        int x = offset_x + block_width * w;
        int y = offset_y + block_height * h;
        
        shapeRenderer.setProjectionMatrix(projectionMatrix);
        shapeRenderer.begin(ShapeType.Filled);
        shapeRenderer.setColor(color.r, color.g, color.b, color.a * alpha);
        
        shapeRenderer.rect(x, y, block_width, block_height);
        shapeRenderer.end();
    }    
    
    public void drawBoard(float alpha) {
        alpha = currentAlpha * alpha;
        for (int w = 0; w < num_width; w++) {
            for (int h = 0; h < num_height; h++) {
                if (positions[h][w] == BlockType.WALL) {
                    draw_block_at(h, w, alpha, wallColor);
                } else if(isCustomBlock(h, w)|| positions[h][w] == BlockType.EMPTY) {
                    draw_block_at(h,w, alpha, floorColor);
                }
            }
        }
    }
    
    public void drawHappySadBlocks(Batch batch, float alpha) {
        for (int w = 0; w < num_width; w++) {
            for (int h = 0; h < num_height; h++) {
                if (isCustomBlock(h, w)) {
                    int hashKey = getHashKey(h,w);
                    customBlocks.get(hashKey).draw(batch, alpha);
                }
            }
        }
    }
    
    @Override
    public void draw(Batch batch, float alpha) {
        Color color = getColor();
        batch.setColor(color.r, color.g, color.b, color.a * alpha);
        currentAlpha = color.a * alpha;

        drawHappySadBlocks(batch, color.a * alpha);
    }
}
