package entities;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

import static utils.Constants.Directions.*;
import static utils.Constants.PlayerActions.*;

public class Player extends Entity {

    private BufferedImage[][][] animations;
    private HashMap<String, Integer> animationMap;
    private int playerAction = IDLE;


    private boolean left,up,right,down;
    private boolean moving = false;



    private boolean quickAttacking = false, strongAttacking = false;

    private float playerSpeed = 2.0f;

    private int aniTick;
    private int aniIndex;

    public Player(float x, float y) {

        super(x, y);
        loadAnimation();
        mapIndices();

    }

    public void update() {
        updatePos();
        updateAnimationTick();
        setAnimation();

    }

    public void render(Graphics g) {
        System.out.println();
        g.drawImage(animations[animationMap.get("Captain Clown Nose with Sword")][playerAction][aniIndex],
                (int) x, (int) y, 256, 160, null);
    }



    private void updateAnimationTick() {
        aniTick++;
        int aniSpeed = 15;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= animations[animationMap.get("Captain Clown Nose with Sword")][playerAction].length) {
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

        if (left && !right){
            x-=playerSpeed;
            moving = true;
        }
        else if (!left && right){
            x+=playerSpeed;
            moving = true;
        }

        if(up && !down){
            y-=playerSpeed;
            moving = true;
        } else if (down && !up){
            y+=playerSpeed;
            moving = true;
        }

    }

    private void loadAnimation() {
        try {
            animations = Files.list(Path.of("res"))
                    .map(path1 -> {
                        try {
                            return Files.list(path1)
                                    .map(path2 -> {
                                        try {
                                            return Files.list(path2)
                                                    .map(Path::toString)
                                                    .map(this::importImg).toArray(BufferedImage[]::new);
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    }).toArray(BufferedImage[][]::new);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).toArray(BufferedImage[][][]::new);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private BufferedImage importImg(String name) {
        InputStream is;
        try {
            is = new FileInputStream(name);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            return ImageIO.read(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void mapIndices() {
        animationMap = new HashMap<>();
        String[] fileArray;
        try {
            fileArray = Files.list(Path.of("res"))
                    .map(path1 -> {
                        try {
                            String[] namesArray = Files.list(path1)
                                    .map(Path::getFileName)
                                    .map(Path::toString)
                                    .toArray(String[]::new);
                            for (int j = 0; j < namesArray.length; j++) {
                                animationMap.put(namesArray[j], j);
                            }
                            return path1;
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }).map(Path::getFileName)
                    .map(Path::toString)
                    .toArray(String[]::new);
            for (int j = 0; j < fileArray.length; j++) {
                animationMap.put(fileArray[j], j);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(fileArray[0]);
        System.out.println(animationMap);
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