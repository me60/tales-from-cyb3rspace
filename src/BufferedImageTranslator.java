import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/**
 * This class is used for converting images used to draw each stage
 * of the game into a BufferedImage object so the MapLoader class can
 * draw an appropriate map within the frame for the player to interact
 * with.
 */
public class BufferedImageTranslator {

  private BufferedImage translatedImage;
  private File currentFile;

  /**
   * Translates a PNG into a BufferedImage.
   */
  public BufferedImage translatePNG(String filePath) {
    try {
      currentFile = new File("../resources/" + filePath);
      translatedImage = ImageIO.read(currentFile);
    } catch(IOException e) {
      System.out.println("IOException detected.");
      System.exit(0);
    }
    return translatedImage;
  }
}
