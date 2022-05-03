package entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import utils.LoadSave;
import static utils.Constants.PlayerActions.*;

public class Player extends Entity {

    private BufferedImage[][][] playerAnimations;
    private HashMap<String, Integer> animationMap;
    private int playerAction = IDLE;

    private boolean left,up,right,down;
    private boolean moving = false;

    private boolean quickAttacking = false, strongAttacking = false;

    private int aniTick;
    private int aniIndex;

    public Player(float x, float y, int width, int height ) {
        super(x, y, width, height);
        loadAnimation();
    }

    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g) {
        System.out.println();
        g.drawImage(playerAnimations[animationMap.get("Captain Clown Nose with Sword")][playerAction][aniIndex],
                (int) x, (int) y, width, height, null);
    }

    private void updateAnimationTick() {
        aniTick++;
        int aniSpeed = 15;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= playerAnimations[animationMap.get("Captain Clown Nose with Sword")][playerAction].length) {
                aniIndex = 0;
                quickAttacking = false;
                strongAttacking = false;
            }
        }
    }

    private void setAnimation() {
        int startAni = playerAction;
        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;
        if (quickAttacking) {
            playerAction = ATTACK_1;
        } else if (strongAttacking){
            playerAction = ATTACK_2;
        }

        if(startAni != playerAction){
            resetAniTick();
        }
    }

    private void resetAniTick() {
        aniTick = 0;
        aniIndex = 0;
    }

    private void updatePos() {
        moving = false;

        float playerSpeed = 2.0f;
        if (left && !right){
            x-= playerSpeed;
            moving = true;
        }
        else if (!left && right){
            x+= playerSpeed;
            moving = true;
        }

        if(up && !down){
            y-= playerSpeed;
            moving = true;
        } else if (down && !up){
            y+= playerSpeed;
            moving = true;
        }

    }

    private void loadAnimation() {
        playerAnimations = LoadSave.GetSprites(LoadSave.PLAYER_SPRITES);
        animationMap = LoadSave.mapIndices(LoadSave.PLAYER_SPRITES);
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public void setQuickAttacking(boolean quickAttacking) {
        this.quickAttacking = quickAttacking;
    }

    public void setStrongAttacking(boolean strongAttacking) {
        this.strongAttacking = strongAttacking;
    }
    public void resetDirBooleans() {
        left = false;
        right = false;
        down = false;
        up = false;
    }

}
