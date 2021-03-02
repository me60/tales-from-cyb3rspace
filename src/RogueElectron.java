import java.awt.Color;
import java.util.Random;
import java.util.ArrayList;

public class RogueElectron extends Enemy {

  public ArrayList<Integer> trackLineXs;
  public ArrayList<Integer> trackLineYs;

  public RogueElectron() {
    Random rand = new Random();
    health = 1000000;
    int place = (rand.nextInt(4)); //Between 0 and 3 (4 options)
    switch (place) {
      case 0 : spriteX = 140; spriteY = 684; break;
      case 1 : spriteX = 530; spriteY = 34; break;
      case 2 : spriteX = 1434; spriteY = 130; break;
      case 3 : spriteX = 1350; spriteY = 680;
    }
    spriteHeight = 27;
    spriteWidth = 28;
    spriteSpeed = 1 + rand.nextInt(4);
    trackLineXs = new ArrayList<Integer>();
    trackLineYs = new ArrayList<Integer>();
    super.incrementOnScreenEnemies();
  }
}
