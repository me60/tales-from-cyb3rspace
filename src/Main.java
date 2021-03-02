import java.awt.image.BufferedImage;

/**
 * This class simply acts as the runner class for the rest of the
 * program - game critical classes run alongside the main run of
 * this class. This class is also responsible for performing command-line
 * argument validation.
 */
public class Main {
  public static void main(String[] args) {
    Game game = new Game();
  }
}
