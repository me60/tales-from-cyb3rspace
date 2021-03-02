import java.awt.Color;
import java.util.Random;

/**
  * This class is used to store details surrounding any instantiated bogus
  * packets used for the first stage of the game. This data includes the
  * on-screen sizes of the BogusPacket in question and its speed - also where
  * it spawns and dies. This class now stores the detects the game controller
  * uses for wall collision. 
  */
public class BogusPacket extends Enemy {

  private int chosenDirection;

  public BogusPacket() {
    Random rand = new Random();
    health = 1000000;
    spriteX = 20 + rand.nextInt(1460 - 21);
    spriteY = 20 + rand.nextInt(650 - 21);
    chosenDirection = 0 + rand.nextInt(8);
    spriteHeight = 24;
    spriteWidth = 30;
    //System.out.println("Spawn! X: " + spriteX + " Y: " + spriteY);
    spriteColour = new Color(255,0,0);
    spriteSpeed = 3;
    super.incrementOnScreenEnemies();
  }

  public int getChosenDirection() {
    return chosenDirection;
  }
  public void setChosenDirection(int chosenDirection) {
    this.chosenDirection = chosenDirection;
  }

  public boolean packetUpCheckDetect(int i, Game workingGame) {
    if ((midTopY - 20) < 0) {
      workingGame.getCurrentEnemies().remove(i);
      return false;
    }
    return true;
  }

  public boolean packetRightCheckDetect(int i, Game workingGame) {
    if ((midRightX + 25) > 1500) {
      workingGame.getCurrentEnemies().remove(i);
      return false;
    }
    return true;
  }

  public boolean packetLeftCheckDetect(int i, Game workingGame) {
    if ((midLeftX - 20) < 0) {
      workingGame.getCurrentEnemies().remove(i);
      return false;
    }
    return true;
  }

  public boolean packetBotCheckDetect(int i, Game workingGame) {
    if ((midBotY + 20) > 750) {
      workingGame.getCurrentEnemies().remove(i);
      return false;
    }
    return true;
  }
}
