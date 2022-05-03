package utils;

import javax.imageio.ImageIO;
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

}
