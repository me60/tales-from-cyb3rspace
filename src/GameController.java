import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.Random;
import java.lang.Math;
import java.util.ArrayList;

/**
 * This class is used to provide methods for the Game class that define
 * how the game responds to normal game events such as movement among sprites
 * and attacks among the player and any enemies.
 */
public class GameController {

  private boolean dIsPressed;
  private boolean aIsPressed;
  private boolean wIsPressed;
  private boolean sIsPressed;

  private Player tempPlayer;
  private Game workingGame;

  private Random rand;

  private static int[][] collisionAreas;

  private boolean bounced;

  public GameController() {
    bounced = false;
  }

  public void keyReleasedDetects(int code) {
    switch (code) {
      case KeyEvent.VK_D: dIsPressed = false; break;
      case KeyEvent.VK_A: aIsPressed = false; break;
      case KeyEvent.VK_W: wIsPressed = false; break;
      case KeyEvent.VK_S: sIsPressed = false; break;
    }
  }

  public void keyPressedDetects(int code) {
    switch (code) {
      case KeyEvent.VK_D: dIsPressed = true; break;
      case KeyEvent.VK_A: aIsPressed = true; break;
      case KeyEvent.VK_W: wIsPressed = true; break;
      case KeyEvent.VK_S: sIsPressed = true; break;
    }
  }

  public void deepSetCollisionAreas(int[][] formalCollisionAreas, int x, int y) {
    System.out.println(x + " " + y);
    collisionAreas = new int[x][y];
    for (int i = 0; i < x; i++) {
      for (int j = 0; j < y; j++) {
        collisionAreas[i][j] = formalCollisionAreas[i][j];
      }
    }
  }

  public int[][] getCollisionAreas() {
    return collisionAreas;
  }

  public void enemyMovementActuator(Enemy enemy, int enemyIndex, Game game) {
    workingGame = game;
    //System.out.println("eyy");
    if (enemy == null) {
      return;
    } else {
      if (enemy instanceof BogusPacket) {
        BogusPacket tempEnemy = (BogusPacket)enemy;
        packetMovementActuator(tempEnemy, enemyIndex, game);
      } else if (enemy instanceof RogueElectron) {
        RogueElectron tempEnemy = (RogueElectron)enemy;
        electronMovementActuator(tempEnemy, enemyIndex, game);
      } else if (enemy instanceof Virus) {
        Virus tempEnemy = (Virus)enemy;
        virusMovementActuator(tempEnemy, enemyIndex, game);
      }
    }
  }

  public void virusMovementActuator(Virus virus, int enemyIndex, Game game) {
    //System.out.println(enemyIndex + ": :" + virus.getDirection());
    if (virus.getDirection() == 1) {
      if (virus.virusUpCheckDetect()) {
        virus.setspriteY(virus.getspriteY() - virus.getSpriteSpeed());
      } else {
        virus.setDirection(5);
        return;
      }
    }
    if (virus.getDirection() == 2) {
      if (virus.virusUpCheckDetect() == true && virus.virusRightCheckDetect() == true) {
        virus.setspriteY(virus.getspriteY() - virus.getSpriteSpeed());
        virus.setspriteX(virus.getspriteX() + virus.getSpriteSpeed());
      } else {
        if (virus.getspriteY()-25 < 0) {
          virus.setDirection(4);
          return;
        } else {
          virus.setDirection(8);
        }
      }
    }
    if (virus.getDirection() == 3) {
      if (virus.virusRightCheckDetect() == true) {
        virus.setspriteX(virus.getspriteX() + virus.getSpriteSpeed());
      } else {
        virus.setDirection(7);
        return;
      }
    }
    if (virus.getDirection() == 4) {
      if (virus.virusRightCheckDetect() == true && virus.virusBotCheckDetect() == true) {
        virus.setspriteX(virus.getspriteX() + virus.getSpriteSpeed());
        virus.setspriteY(virus.getspriteY() + virus.getSpriteSpeed());
      } else if ((virus.getspriteY() + 80) > 750) {
        virus.setDirection(2);
        return;
      } else {
        virus.setDirection(6);
        return;
      }
    }
    if (virus.getDirection() == 5) {
      if (virus.virusBotCheckDetect() == true) {
        virus.setspriteY(virus.getspriteY() + virus.getSpriteSpeed());
      } else {
        virus.setDirection(1);
        return;
      }
    }
    if (virus.getDirection() == 6) {
      if (virus.virusLeftCheckDetect() == true && virus.virusBotCheckDetect() == true) {
        virus.setspriteX(virus.getspriteX() - virus.getSpriteSpeed());
        virus.setspriteY(virus.getspriteY() + virus.getSpriteSpeed());
      } else if ((virus.getspriteY() + 80) > 750) {
        virus.setDirection(8);
        return;
      } else {
        virus.setDirection(4);
        return;
      }
    }
    if (virus.getDirection() == 7) {
      if (virus.virusLeftCheckDetect() == true) {
        virus.setspriteX(virus.getspriteX() - virus.getSpriteSpeed());
      } else {
        virus.setDirection(3);
        return;
      }
    }
    if (virus.getDirection() == 8) {
      if (virus.virusLeftCheckDetect() == true && virus.virusUpCheckDetect() == true) {
        virus.setspriteX(virus.getspriteX() - virus.getSpriteSpeed());
        virus.setspriteY(virus.getspriteY() - virus.getSpriteSpeed());
      } else if (virus.getspriteY()-25 < 0) {
        virus.setDirection(6);
        return;
      } else {
        virus.setDirection(2);
        return;
      }
    }
  }

  public void electronMovementActuator(RogueElectron tempEnemy, int enemyIndex, Game game) {
    Player player = workingGame.getPlayer();
    int playerXPos = player.getspriteX();
    int playerYPos = player.getspriteY();
    int electronXPos = tempEnemy.getspriteX();
    int electronYPos = tempEnemy.getspriteY();
    String directionFlag = "";
    if (playerYPos < electronYPos) {
      directionFlag += "N";
      if (playerYPos < electronYPos - 150) {
        directionFlag += "N";
      } else {
        directionFlag += "";
      }
    } else if (playerYPos > electronYPos) {
      directionFlag += "S";
      if (playerYPos > electronYPos + 150) {
        directionFlag += "S";
      } else {
        directionFlag += "";
      }
    } else if (playerYPos == electronYPos) {
      directionFlag += "";
    }
    if (playerXPos > electronXPos) {
      directionFlag += "E";
    } else if (playerXPos < electronXPos) {
      directionFlag += "W";
    } else if (playerXPos == electronXPos) {
      directionFlag += "";
    }
    boolean wall = false;
    tempEnemy.trackLineXs.clear();
    tempEnemy.trackLineYs.clear();
    calcTrackLines(tempEnemy, player.centralX, tempEnemy.centralX, player.centralY, tempEnemy.centralY);
    if (directionFlag.equals("NE") || directionFlag.equals("NNE")) {
        tempEnemy.setspriteX(tempEnemy.getspriteX() + tempEnemy.getSpriteSpeed());
        tempEnemy.setspriteY(tempEnemy.getspriteY() - tempEnemy.getSpriteSpeed());
      }
      if (directionFlag.equals("SE") || directionFlag.equals("SSE")) {
        tempEnemy.setspriteX(tempEnemy.getspriteX() + tempEnemy.getSpriteSpeed());
        tempEnemy.setspriteY(tempEnemy.getspriteY() + tempEnemy.getSpriteSpeed());
      }
      if (directionFlag.equals("NW") || directionFlag.equals("NNW")) {
        tempEnemy.setspriteX(tempEnemy.getspriteX() - tempEnemy.getSpriteSpeed());
        tempEnemy.setspriteY(tempEnemy.getspriteY() - tempEnemy.getSpriteSpeed());
      }
      if (directionFlag.equals("SW") || directionFlag.equals("SSW")) {
        tempEnemy.setspriteX(tempEnemy.getspriteX() - tempEnemy.getSpriteSpeed());
        tempEnemy.setspriteY(tempEnemy.getspriteY() + tempEnemy.getSpriteSpeed());
      }
      if (directionFlag.equals("N")) {
        tempEnemy.setspriteX(tempEnemy.getspriteY() + tempEnemy.getSpriteSpeed());
      }
      if (directionFlag.equals("S")) {
        tempEnemy.setspriteX(tempEnemy.getspriteY() - tempEnemy.getSpriteSpeed());
      }
      if (directionFlag.equals("E")) {
        tempEnemy.setspriteX(tempEnemy.getspriteX() + tempEnemy.getSpriteSpeed());
      }
      if (directionFlag.equals("W")) {
        tempEnemy.setspriteX(tempEnemy.getspriteX() - tempEnemy.getSpriteSpeed());
      }
    //}
    System.out.println("Direction: " + directionFlag + " Wall? " + wall);
  }

  private boolean detectWall(Game game, RogueElectron enemy) {
    for (int i = 0; i < enemy.trackLineXs.size(); i++) {
      for (int j = 0; j < enemy.trackLineYs.size(); j++) {
        if (game.getMapLoader().getAllPositions()[enemy.trackLineXs.get(i)][enemy.trackLineYs.get(j)] == 1) {
          return true;
        }
      }
    }
    return false;
  }

  //http://rosettacode.org/wiki/Bitmap/Bresenham%27s_line_algorithm#Java
  /**
    * This is a borrowed implementation of Bresenham's algorithm used to
    * calculate the line approximation between the player and any on-screen
    * rogue electrons. This can be used for wall detection to aid in how the
    * electrons travel to the player.
    */
  public void calcTrackLines(RogueElectron enemy, int x1, int x2, int y1, int y2) {
    int d = 0;
    int dx = Math.abs(x2 - x1);
    int dy = Math.abs(y2 - y1);
    int dx2 = 2 * dx; // slope scaling factors to
    int dy2 = 2 * dy; // avoid floating point
    int ix = x1 < x2 ? 1 : -1; // increment direction
    int iy = y1 < y2 ? 1 : -1;
    int x = x1;
    int y = y1;
    if (dx >= dy) {
        while (true) {
            enemy.trackLineXs.add(x);
            enemy.trackLineYs.add(y);
            if (x == x2)
                break;
            x += ix;
            d += dy2;
            if (d > dx) {
                y += iy;
                d -= dx2;
            }
        }
    } else {
        while (true) {
            enemy.trackLineXs.add(x);
            enemy.trackLineYs.add(y);
            if (y == y2)
                break;
            y += iy;
            d += dx2;
            if (d > dy) {
                x += ix;
                d -= dy2;
            }
        }
    }
  }

  /**
    * Used to dictate how packets move. Only checks for wall collision in the
    * direction that the packet was pre-destined to travel in
    */
  public void packetMovementActuator(BogusPacket tempEnemy, int enemyIndex, Game game) {
    switch (tempEnemy.getChosenDirection()) {
      case 0 :
      if (tempEnemy.packetUpCheckDetect(enemyIndex, workingGame) && tempEnemy.packetRightCheckDetect(enemyIndex, workingGame)) {
          tempEnemy.setspriteX(tempEnemy.getspriteX() + tempEnemy.getSpriteSpeed());
          tempEnemy.setspriteY(tempEnemy.getspriteY() - tempEnemy.getSpriteSpeed());
      }
      break;
      case 1 :
      if (tempEnemy.packetUpCheckDetect(enemyIndex, workingGame) && tempEnemy.packetLeftCheckDetect(enemyIndex, workingGame)) {
          tempEnemy.setspriteX(tempEnemy.getspriteX() - tempEnemy.getSpriteSpeed());
          tempEnemy.setspriteY(tempEnemy.getspriteY() - tempEnemy.getSpriteSpeed());
      }
      break;
      case 2 :
      if (tempEnemy.packetBotCheckDetect(enemyIndex, workingGame) && tempEnemy.packetRightCheckDetect(enemyIndex, workingGame)) {
          tempEnemy.setspriteX(tempEnemy.getspriteX() + tempEnemy.getSpriteSpeed());
          tempEnemy.setspriteY(tempEnemy.getspriteY() + tempEnemy.getSpriteSpeed());
      }
      break;
      case 3 :
      if (tempEnemy.packetBotCheckDetect(enemyIndex, workingGame) && tempEnemy.packetLeftCheckDetect(enemyIndex, workingGame)) {
          tempEnemy.setspriteX(tempEnemy.getspriteX() - tempEnemy.getSpriteSpeed());
          tempEnemy.setspriteY(tempEnemy.getspriteY() + tempEnemy.getSpriteSpeed());
      }
      break;
      case 4 :
      if (tempEnemy.packetUpCheckDetect(enemyIndex, workingGame)) {
        tempEnemy.setspriteY(tempEnemy.getspriteY() - tempEnemy.getSpriteSpeed() * 2);
      }
      break;
      case 5 :
      if (tempEnemy.packetBotCheckDetect(enemyIndex, workingGame)) {
        tempEnemy.setspriteY(tempEnemy.getspriteY() + tempEnemy.getSpriteSpeed() * 2);
      }
      break;
      case 6 :
      if (tempEnemy.packetLeftCheckDetect(enemyIndex, workingGame)) {
        tempEnemy.setspriteX(tempEnemy.getspriteX() - tempEnemy.getSpriteSpeed() * 2);
      }
      break;
      case 7 :
      if (tempEnemy.packetRightCheckDetect(enemyIndex, workingGame)) {
        tempEnemy.setspriteX(tempEnemy.getspriteX() + tempEnemy.getSpriteSpeed() * 2);
      }
    }
  }

  /**
    * When invoked, a cascading if statement determines how to move on the
    * screen based upon button check detects and if the player will collide with
    * a wall.
    */
  public void playerMovementActuator(ActionEvent code, Game game) {
    tempPlayer = game.getPlayer();
    tempPlayer.setCollisionAreas(collisionAreas);
    if (wIsPressed && dIsPressed) {
      if (tempPlayer.upCheckDetect()) {
        if (tempPlayer.rightCheckDetect()) {
          tempPlayer.setspriteX(tempPlayer.getspriteX() + tempPlayer.getSpriteSpeed());
          tempPlayer.setspriteY(tempPlayer.getspriteY() - tempPlayer.getSpriteSpeed());
        }
      }
    } else if (wIsPressed && aIsPressed) {
      if (tempPlayer.upCheckDetect()) {
        if (tempPlayer.leftCheckDetect()) {
          tempPlayer.setspriteX(tempPlayer.getspriteX() - tempPlayer.getSpriteSpeed());
          tempPlayer.setspriteY(tempPlayer.getspriteY() - tempPlayer.getSpriteSpeed());
        }
      }
    } else if (sIsPressed && dIsPressed) {
      if (tempPlayer.botCheckDetect()) {
        if (tempPlayer.rightCheckDetect()) {
          tempPlayer.setspriteX(tempPlayer.getspriteX() + tempPlayer.getSpriteSpeed());
          tempPlayer.setspriteY(tempPlayer.getspriteY() + tempPlayer.getSpriteSpeed());
        }
      }
    } else if (sIsPressed && aIsPressed) {
      if (tempPlayer.botCheckDetect()) {
        if (tempPlayer.leftCheckDetect()) {
          tempPlayer.setspriteX(tempPlayer.getspriteX() - tempPlayer.getSpriteSpeed());
          tempPlayer.setspriteY(tempPlayer.getspriteY() + tempPlayer.getSpriteSpeed());
        }
      }
    }
    if (wIsPressed) {
      if (tempPlayer.upCheckDetect()) {
        tempPlayer.setspriteY(tempPlayer.getspriteY() - tempPlayer.getSpriteSpeed() * 2);
      }
    } else if (sIsPressed) {
      if (tempPlayer.botCheckDetect()) {
        tempPlayer.setspriteY(tempPlayer.getspriteY() + tempPlayer.getSpriteSpeed() * 2);
      }
    }
    if (aIsPressed) {
      if (tempPlayer.leftCheckDetect()) {
        tempPlayer.setspriteX(tempPlayer.getspriteX() - tempPlayer.getSpriteSpeed() * 2);
      }
    } else if (dIsPressed) {
      if (tempPlayer.rightCheckDetect()) {
        tempPlayer.setspriteX(tempPlayer.getspriteX() + tempPlayer.getSpriteSpeed() * 2);
      }
    }
  }
}
