package main;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.imageio.ImageIO;
import javax.swing.*;
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
import static utils.Constants.PlayerActions.IDLE;
import static utils.Constants.PlayerActions.RUNNING;


public class GamePanel extends JPanel {

    private float xDelta = 100, yDelta = 100;
    private BufferedImage[][][] animations;
    private HashMap<String, Integer> animationMap;
    private int playerAction = IDLE;
    private int playerDir = -1;

    private boolean moving = false;

    private int aniTick;
    private int aniIndex;


    // Constructor
    public GamePanel() {
        //Object Initializations
        MouseInputs mouseInputs = new MouseInputs(this);
        loadAnimation();
        mapIndices();
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
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

    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setPreferredSize(size);
    }

    public void setDirection(int direction) {
        this.playerDir = direction;
        moving = true;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    private void updateAnimationTick() {
        aniTick++;
        int aniSpeed = 15;
        if (aniTick >= aniSpeed) {
            aniTick = 0;
            aniIndex++;
            if (aniIndex >= animations[animationMap.get("Captain Clown Nose with Sword")][playerAction].length) {
                aniIndex = 0;
            }
        }
    }

    private void setAnimation() {
        if (moving)
            playerAction = RUNNING;
        else
            playerAction = IDLE;
    }

    private void updatePos() {
        if (moving) {
            switch (playerDir) {
                case LEFT -> xDelta -= 5;
                case UP -> yDelta -= 5;
                case RIGHT -> xDelta += 5;
                case DOWN -> yDelta += 5;

            }
        }
    }

    public void updateGame() {
        updateAnimationTick();
        setAnimation();
        updatePos();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(animations[animationMap.get("Captain Clown Nose with Sword")][playerAction][aniIndex],
                (int) xDelta, (int) yDelta, 256, 160, null);
    }


}
