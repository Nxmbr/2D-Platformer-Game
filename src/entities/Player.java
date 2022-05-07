package entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import main.Game;
import utils.LoadSave;
import static utils.Constants.PlayerActions.*;
import static utils.HelpMethods.CanMoveHere;

public class Player extends Entity {

    private BufferedImage[][][] playerAnimations;
    private HashMap<String, Integer> animationMap;
    private int playerAction = IDLE;

    private boolean left,up,right,down;
    private boolean moving = false;

    private boolean quickAttacking = false, strongAttacking = false;

    private int aniTick;
    private int aniIndex;

    private int[][] lvlData;

    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;

    public Player(float x, float y, int width, int height ) {
        super(x, y, width, height);
        loadAnimation();
        initHitbox(x,y,20*Game.SCALE, 28*Game.SCALE);
    }

    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimation();
    }

    public void render(Graphics g) {
        System.out.println();
        g.drawImage(playerAnimations[animationMap.get("Captain Clown Nose with Sword")][playerAction][aniIndex],
                (int)(hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset), width, height, null);
        drawHitbox(g);
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
            playerAction = ATTACK_2;
        } else if (strongAttacking){
            playerAction = ATTACK_3;
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
        if (!left && !right && !up && !down)
            return;

        float xSpeed = 0, ySpeed = 0;
        float playerSpeed = 2.0f;

        if (left && !right)
            xSpeed = -playerSpeed;
        else if (!left && right)
            xSpeed = playerSpeed;

        if(up && !down)
            ySpeed = -playerSpeed;
        else if (down && !up)
            ySpeed = playerSpeed;

        if(CanMoveHere(hitbox.x+xSpeed, hitbox.y+ySpeed, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
            hitbox.y += ySpeed;
            moving = true;
        }

    }

    private void loadAnimation() {
        playerAnimations = LoadSave.GetSprites(LoadSave.PLAYER_SPRITES);
        animationMap = LoadSave.mapIndices(LoadSave.PLAYER_SPRITES);
    }

    public void loadLvlData(int[][] lvlData){
        this.lvlData = lvlData;
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
