package entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import main.Game;
import utils.LoadSave;
import static utils.Constants.PlayerActions.*;
import static utils.HelpMethods.*;


public class Player extends Entity {

    private int playerAction = IDLE;
    private boolean quickAttacking = false, strongAttacking = false;

    // Player movement
    private boolean left,right, jump;
    private boolean moving = false;
    private float xSpeed = 0;
    private float playerSpeed = 1.0f * Game.SCALE;
    //   Jumping/Gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollision = 0.5f*Game.SCALE;
    private boolean inAir = false;

    // Player animations
    private BufferedImage[][][] playerAnimations;
    private HashMap<String, Integer> animationMap;
    private int aniSpeed = 15;
    private int aniTick;
    private int aniIndex;
    private int[][] lvlData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;




    public Player(float x, float y, int width, int height ) {
        super(x, y, width, height);
        loadAnimation();
        initHitbox(x, y, (int)(20*Game.SCALE), (int)(27*Game.SCALE));
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
        //drawHitbox(g);
    }

    private void updateAnimationTick() {
        aniTick++;
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

        if(inAir){
            if(airSpeed < 0)
                playerAction = JUMP;
            else
                playerAction = FALLING;
        }
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
        if(jump)
            jump();
        if (!left && !right && !inAir)
            return;

        xSpeed = 0;

        if (left)
            xSpeed -= playerSpeed;
        if (right)
            xSpeed += playerSpeed;
        if (!inAir)
            if(!isEntityOnFloor(hitbox, lvlData))
                inAir = true;


        if(inAir){
            if(CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width,hitbox.height,lvlData)){
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            }else {
                hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0){
                    resetInAir();
                }else
                    airSpeed = fallSpeedAfterCollision;
                updateXPos(xSpeed);
            }
        }else
            updateXPos(xSpeed);
        moving = true;
    }

    private void jump() {
        if(inAir)
            return;
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if(CanMoveHere(hitbox.x+xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
            hitbox.x += xSpeed;
        } else{
          hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    private void loadAnimation() {
        playerAnimations = LoadSave.GetSprites(LoadSave.PLAYER_SPRITES);
        animationMap = LoadSave.mapIndices(LoadSave.PLAYER_SPRITES);
    }

    public void loadLvlData(int[][] lvlData){
        this.lvlData = lvlData;
        if(!isEntityOnFloor(hitbox, lvlData))
            inAir = true;
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
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
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
}
