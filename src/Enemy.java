/**
 * This class acts as the overwhelming superclass for all enemy
 * classes such as the RogueElectron and the BogusPacket class. All
 * enemies will share behaviours specified within this class.
 */
public abstract class Enemy extends Sprite {

  private static int onScreenEnemies;

  public int getOnScreenEnemies() {
    return onScreenEnemies;
  }
  public void setOnScreenEnemies(int onScreenEnemies) {
    this.onScreenEnemies = onScreenEnemies;
  }
  public void decrementOnScreenEnemies() {
    onScreenEnemies = onScreenEnemies - 1;
  }
  public void incrementOnScreenEnemies() {
    onScreenEnemies = onScreenEnemies + 1;
  }
}
