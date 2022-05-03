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
    private Game game;




    // Constructor
    public GamePanel(Game game) {
        //Object Initializations
        MouseInputs mouseInputs = new MouseInputs(this);
        this.game = game;
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setPreferredSize(size);
    }

    public void updateGame() {

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);


    }

    public Game getGame() {
        return game;
    }
}
