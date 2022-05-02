package main;

import javax.swing.*;

public class GameWindow extends JFrame {


    public GameWindow(GamePanel gamePanel) {

        JFrame jframe = new JFrame();


        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.add(gamePanel);
        jframe.setLocationRelativeTo(null);
        jframe.setResizable(false);
        jframe.pack();

        // Put everything that needs to happen before we open the window above setVisible
        jframe.setVisible(true);

    }

}
