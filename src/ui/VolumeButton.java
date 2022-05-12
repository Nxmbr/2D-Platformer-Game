package ui;

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;


import static utils.Constants.UI.VolumeButtons.*;

public class VolumeButton extends PauseButton{

    private BufferedImage[] imgs;
    private BufferedImage slider;
    private int index = 0;
    private boolean mouseOver, mousePressed;
    private int buttonX, minX, maxX;

    public VolumeButton(int x, int y, int width, int height) {
        super(x + width / 2, y, VOLUME_WIDTH, VOLUME_HEIGHT);
        bounds.x -= VOLUME_WIDTH / 2;
        buttonX = x + width / 2;
        this.x = x;
        this.width = width;
        minX = x + VOLUME_WIDTH / 2;
        maxX = x + width - VOLUME_WIDTH / 2;
        loadImgs();
    }

    private void loadImgs() {
        BufferedImage temp = LoadSave.GetSprites(LoadSave.MENU)[5][0][0];
        imgs = new BufferedImage[3];
        for(int i = 0; i < imgs.length; i++){
            imgs[i] = temp.getSubimage(i*VOLUME_DEFAULT_WIDTH,0,VOLUME_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
        }
        slider = temp.getSubimage(3 * VOLUME_DEFAULT_WIDTH, 0, SLIDER_DEFAULT_WIDTH, VOLUME_DEFAULT_HEIGHT);
    }

    public void update() {
        index = 0;
        if(mouseOver)
            index = 1;
        if (mousePressed)
            index = 2;
    }
    public void draw (Graphics g){
        g.drawImage(slider, x, y,width, height,null);
        g.drawImage(imgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, VOLUME_HEIGHT, null);
    }

    public void changeX(int x){
        if(x < minX)
            buttonX = minX;
        else buttonX = Math.min(x , maxX);

        bounds.x = buttonX - VOLUME_WIDTH / 2;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }

    public void resetBools(){
        mouseOver = false;
        mousePressed = false;
    }
}
