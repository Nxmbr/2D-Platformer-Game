package utils;

import main.Game;

import java.awt.geom.Rectangle2D;

public class HelpMethods {

    public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
        if (!IsSolid(x,y,lvlData))
            if(!IsSolid(x+width, y+height, lvlData))
                if(!IsSolid(x+width,y,lvlData))
                    return !IsSolid(x, y + height, lvlData);
        return false;


    }

    private static boolean IsSolid(float x, float y, int[][] lvlData){
        if( x < 0 || x >= Game.GAME_WIDTH )
            return true;
        if( y < 0 || y >= Game.GAME_HEIGHT )
            return true;

        float xIndex = x / Game.TILES_SIZE;
        float yIndex = y / Game.TILES_SIZE;

        int value = lvlData[(int)yIndex][(int)xIndex];

        return value >= 48 || value < 0 || value != 11;


    }

    public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed){
        int currentTile = (int)(hitbox.x / Game.TILES_SIZE);
        if(xSpeed > 0){
            // right collision
            int tileXPos = currentTile*Game.TILES_SIZE;
            int xOffset = (int)(Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else{
            // left collision
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed){
        int currentTile = (int)(hitbox.y / Game.TILES_SIZE);
        if(airSpeed > 0){
            //falling - touching floor
            int tileYPos = currentTile*Game.TILES_SIZE;
            int yOffset = (int)(Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        }else {
            //jumping - touching ceiling
            return currentTile * Game.TILES_SIZE;

        }
    }

    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] lvlData){
        // check the pixel below bottom left and bottom right
        if (!IsSolid(hitbox.x, hitbox.y + hitbox.height + 1,lvlData)){
            return IsSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData);
        }
        return true;
    }
}
