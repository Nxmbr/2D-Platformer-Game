package utils;

import main.Game;

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


public class LoadSave {

    public static final String PLAYER_SPRITES = "res/Player";
    public static final String LEVEL_ATLAS = "res/Level";


    public static BufferedImage[][][] GetSprites (String root){
        try {
            return Files.list(Path.of(root))
                    .map(path1 -> {
                        try {
                            return Files.list(path1)
                                    .map(path2 -> {
                                        try {
                                            return Files.list(path2)
                                                    .map(Path::toString)
                                                    .map(LoadSave::GetPlayerSprites).toArray(BufferedImage[]::new);
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

    private static BufferedImage GetPlayerSprites(String name) {
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
    public static HashMap mapIndices(String filePath) {
        HashMap<String, Integer> animationMap = new HashMap<>();
        String[] fileArray;
        try {
            fileArray = Files.list(Path.of(filePath))
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
            return animationMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static int[][] GetLevelData() {
        int[][] lvlData = new int[Game.TILES_IN_HEIGHT][Game.TILES_IN_WIDTH];
        BufferedImage img = GetSprites(LEVEL_ATLAS)[0][3][0];

        for (int j = 0; j < img.getHeight(); j++){
            for (int i = 0; i < img.getWidth(); i++){
                Color color = new Color((img.getRGB(i, j)));
                int value = color.getRed();
                if (value >= 48)
                    value = 0;
                lvlData[j][i] = value;
            }
        }
        return lvlData;
    }
}
