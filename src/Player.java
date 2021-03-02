import java.awt.Color;

/**
 * This class is used to define player specific methods such as attacking
 * and providing current position. This class should also store player-specific
 * values such as where the player is and the player health such that these
 * values can be changed.
 */
public class Player extends Sprite {

  private final int PLAYER_WIDTH = 30;
  private final int PLAYER_HEIGHT = 60;

  public Player() {
    health = 100;
    spriteX = 50;
    spriteY = 50;
    spriteHeight = PLAYER_HEIGHT;
    spriteWidth = PLAYER_WIDTH;
    spriteColour = new Color(0,255,0);
    spriteSpeed = 2;
    collisionAreas = null;
  }

  public int getHealth() {
    return health;
  }
  public void setHealth(int health) {
    this.health = health;
  }
  public void decrementHealth() {
    health -= 1;
  }

  public int getPlayerHeight() {
    return spriteHeight;
  }

  public int getPlayerWidth() {
    return spriteWidth;
  }
}
