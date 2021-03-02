import java.awt.Color;

/**
 * This abstract class is responsible for defining the behaviours that all
 * on-screen, moving 'elements' have. This class should contain constructors for
 * how each on-screen sprite is created and destroyed.
 */
public abstract class Sprite {

  protected int health;
  protected int spriteX;
  protected int spriteY;
  protected int spriteSpeed;
  protected int spriteHeight;
  protected int spriteWidth;
  protected Color spriteColour;

  public int topLeftColX;
  public int topRightColX;
  public int botLeftColX;
  public int botRightColX;

  public int topLeftColY;
  public int topRightColY;
  public int botLeftColY;
  public int botRightColY;

  public int midLeftX;
  public int midRightX;
  public int midTopX;
  public int midBotX;

  public int midLeftY;
  public int midRightY;
  public int midTopY;
  public int midBotY;

  public int centralX;
  public int centralY;

  protected int[][] collisionAreas;

  //For viruses...
  public int direction;
  protected int type;

  public void updateSpriteCollisionSpace() {
    topLeftColX = spriteX;
    topLeftColY = spriteY;

    topRightColX = spriteX + spriteWidth;
    topRightColY = spriteY;

    botLeftColX = spriteX;
    botLeftColY = spriteY + spriteHeight;

    botRightColX = spriteX + spriteWidth;
    botRightColY = spriteY + spriteHeight;

    midLeftX = topLeftColX;
    midLeftY = (topLeftColY + botLeftColY) / 2;

    midRightX = topRightColX;
    midRightY = (topRightColY + botRightColY) / 2;

    midTopX = (topLeftColX + topRightColX) / 2;
    midTopY = topRightColY;

    midBotX = (botLeftColX + botRightColX) / 2;
    midBotY = botLeftColY;

    centralX = midLeftX + ((midRightX - midLeftX) / 2);
    centralY = midTopY + ((midBotY - midTopY) / 2);
  }

  public int getSpriteHeight() {
    return spriteHeight;
  }

  public int getSpriteWidth() {
    return spriteWidth;
  }

  public int getspriteX() {
    return spriteX;
  }
  public void setspriteX(int spriteX) {
    this.spriteX = spriteX;
  }

  public int getspriteY() {
    return spriteY;
  }
  public void setspriteY(int spriteY) {
    this.spriteY = spriteY;
  }

  public Color getSpriteColour() {
    return spriteColour;
  }
  public void setSpriteColour(Color spriteColour) {
    this.spriteColour = spriteColour;
  }

  public int getSpriteSpeed() {
    return spriteSpeed;
  }
  public void setSpriteSpeed(int spriteSpeed) {
    this.spriteSpeed = spriteSpeed;
  }

  public int[][] getCollisionAreas() {
    return collisionAreas;
  }
  public void setCollisionAreas(int[][] collisionAreas) {
    this.collisionAreas = collisionAreas;
  }

  public boolean upCheckDetect() {
    if (collisionAreas[topRightColX][topRightColY - 2] == 1 || collisionAreas[topLeftColX][topLeftColY - 2] == 1 || collisionAreas[midTopX][midTopY - 2] == 1) {
      System.out.println("Hit my top on something...");
      return false;
    }
    return true;
  }

  public boolean rightCheckDetect() {
    if (collisionAreas[topRightColX + 2][topRightColY] == 1 || collisionAreas[botRightColX + 2][botRightColY] == 1 || collisionAreas[midRightX + 2][midRightY] == 1) {
    //if ((tempSprite.midRightX + 15) > 1500) {
      System.out.println("Hit my right on something...");
      return false;
    }
    return true;
  }

  public boolean leftCheckDetect() {
    if (collisionAreas[topLeftColX - 2][topLeftColY] == 1 || collisionAreas[botLeftColX - 2][botLeftColY] == 1 || collisionAreas[midLeftX - 2][midLeftY] == 1) {
      System.out.println("Hit my left on something...");
      return false;
    }
    return true;
  }

  public boolean botCheckDetect() {
    if (collisionAreas[botRightColX][botRightColY] == 1 || collisionAreas[botLeftColX][botLeftColY] == 1 || collisionAreas[midBotX][midBotY] == 1) {
      System.out.println("Hit my bottom on something...");
      return false;
    }
    return true;
  }

  public int getType() {
    return type;
  }
}
