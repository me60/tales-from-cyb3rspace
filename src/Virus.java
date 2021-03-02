import java.awt.Color;
import java.util.Random;
import java.util.ArrayList;

public class Virus extends Enemy {

  public static int INITIAL_X = 681;
  public static int INITIAL_Y = 527;

  public Virus(int x, int y) {
    System.out.println("Spawning virus...");
    Random rand = new Random();
    health = 1000000;
    spriteX = x;
    spriteY = y;
    spriteHeight = 32;
    spriteWidth = 43;
    direction = 1 + rand.nextInt(8);
    System.out.println("Chosen direction: " + direction);

    //1 N
    //2 NE
    //3 E
    //4 SE
    //5 S
    //6 SW
    //7 W
    //8 NW

    spriteSpeed = 1 + rand.nextInt(3);
    type = 1 + rand.nextInt(3);
    super.incrementOnScreenEnemies();
  }

  public int getDirection() {
    return this.direction;
  }
  public void setDirection(int direction) {
    this.direction = direction;
  }

  public boolean virusUpCheckDetect() {
    if ((midTopY - 20) < 0) {
      //workingGame.getCurrentEnemies().remove(i);
      return false;
    }
    return true;
  }

  public boolean virusRightCheckDetect() {
    if ((midRightX + 25) > 1500) {
      //workingGame.getCurrentEnemies().remove(i);
      return false;
    }
    return true;
  }

  public boolean virusLeftCheckDetect() {
    if ((midLeftX - 20) < 0) {
      //workingGame.getCurrentEnemies().remove(i);
      return false;
    }
    return true;
  }

  public boolean virusBotCheckDetect() {
    if ((midBotY + 40) > 750) {
      //workingGame.getCurrentEnemies().remove(i);
      return false;
    }
    return true;
  }
}
