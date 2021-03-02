import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class is used to provide methods for drawing certain types of map from
 * either a BufferedImage object or, if the map type is special, from some kind
 * of semi-structured content document. Drawn maps take specific colours from
 * source BufferedImage objects to mean certain map elements such as walls and
 * doorways.
 */
public class MapLoader {

  private BufferedImageTranslator translator;
  private BufferedImage workingImage;
  private BufferedImage finishedMap;
  private JFrame workingFrame;
  private Game workingGame;

  /*An int that stores the stage painted used for game logic - checks are made
  against the current map for determining what behaviour needs to be implemented.
  All positions is a 2D array of values that relate to specific map information
  built using MapLoader*/
  private int currentMap;
  private int[][] allPositions;

  private int orangeColour = ((new Color(255,165,0)).getRGB());
  private int pinkColour = ((new Color(245,50,200)).getRGB());
  //RGB integers for map draws
  //private final int DETECTED_GREEN = -14834886;
  private final int DETECTED_GREEN = -16711936;
  private final int DETECTED_BLACK = -16777216;
  private final int DETECTED_LBLUE = -16776961;
  private final int DETECTED_BLUE = -16777036;
  private final int DETECTED_EXIT_COLOUR = -15792761;
  private final int DETECTED_BUTTON_RED = -65536;
  private final int DETECTED_BUTTON_ORANGE = -23296;//0 + ((new Color(255,165,0)).getRGB());
  private final int PINK = -707896;

  //Frame placements and sizes
  private final int FRAME_SIZE_X = 1506;
  private final int FRAME_SIZE_Y = 785;
  private final int FRAME_SIZE_XP = 10;
  private final int FRAME_SIZE_YP = 10;

  private final int INITIAL_PLAYER_POSITIONX = 50;
  private final int INITIAL_PLAYER_POSITIONY = 50;

  private ArrayList<Integer> maintainedBackgroundXs;
  private ArrayList<Integer> maintainedBackgroundYs;
  private ArrayList<Integer> maintainedBackgroundWs;
  private ArrayList<Integer> maintainedBackgroundHs;

  private BufferedImage playerPic;
  private BufferedImage bogusPacketPic;
  private BufferedImage rogueElectronPic;
  private BufferedImage[] virus1Pics;
  private BufferedImage[] virus2Pics;
  private BufferedImage[] virus3Pics;
  private int imageStage;
  private int virus1ImageStage;
  private BufferedImage trumpPic;
  private BufferedImage meexSneepPic;


  //The title of the frame
  private final String frameName = "Tales from Cyberspace";

  public MapLoader(Game game, JFrame frame) {
    System.out.println(pinkColour);
    maintainedBackgroundXs = new ArrayList<Integer>();
    maintainedBackgroundYs = new ArrayList<Integer>();
    maintainedBackgroundWs = new ArrayList<Integer>();
    maintainedBackgroundHs = new ArrayList<Integer>();
    workingFrame = frame;
    workingGame = game;
    imageStage = 0;
    virus1ImageStage = 0;
    translator = new BufferedImageTranslator();
    playerPic = translator.translatePNG("player.png");
    bogusPacketPic = translator.translatePNG("bogus_packet.png");
    rogueElectronPic = translator.translatePNG("rogue_electron.png");
    trumpPic = translator.translatePNG("trump1.png");
    meexSneepPic = translator.translatePNG("meex_sneep.png");
    virus1Pics = new BufferedImage[2];
    virus2Pics = new BufferedImage[3];
    virus3Pics = new BufferedImage[3];
    for (int i = 0; i < 2; i++) {
      virus1Pics[i] = translator.translatePNG("virus1-" + (i+1) + ".png");
    }
    for (int i = 0; i < 3; i++) {
      virus2Pics[i] = translator.translatePNG("virus2-" + (i+1) + ".png");
      virus3Pics[i] = translator.translatePNG("virus3-" + (i+1) + ".png");
    }
    setCurrentMap(0);
  }

  /**
   * From the Game class (from which the game is run primarily), the GUI is
   * passed appropriate parameters for which the GUI can be set up and run
   * (objects obtained from the Game class).
   */
  public void setUpFrame() {
    workingFrame.setBounds(FRAME_SIZE_XP, FRAME_SIZE_YP, FRAME_SIZE_X, FRAME_SIZE_Y);
    workingFrame.setTitle(frameName);
    workingFrame.setResizable(false);
    workingFrame.setVisible(true);
    workingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    workingFrame.add(workingGame);
  }

  /**
   * Once validated, the map specified by the file path parameter is added to
   * the frame, collision areas are sent to the GameController, the initial
   * positions of sprites are drawn, and the exit areas/special areas are
   * sent to the GameController.
   */
  public void updateBufferedMap(String filePath) {
    workingImage = translator.translatePNG(filePath);
    updateBufferedMap(workingImage);
  }

  public void updateBufferedMap(BufferedImage image) {
    maintainedBackgroundXs.clear();
    maintainedBackgroundYs.clear();
    maintainedBackgroundWs.clear();
    maintainedBackgroundHs.clear();
    workingImage = image;
    workingImage = validateMap(workingImage);
    if (workingImage == null) {
      System.out.println("The image was invalid.");
      System.exit(0);
    } else {
      System.out.println("\nTranslated and validated image by this point.");
      System.out.println("Selected image loaded in buffer.");
    }
  }

  /**
    * Using a copy of the graphics object, the map is physically drawn on-screen
    * using the drawImage method.
    */
  public void drawMapInBuffer(Graphics g) {
    System.out.println("Drawing map in buffer...\n");
    //null used for colors and ImageObserver
    g.drawImage(workingImage, 0, 0, null);
  }

  /**
    * Draws each sprite by gathering information from their object attributes
    * for as to where they are on the screen. This method also draws all enemies
    * currently in the enemies list on the screen as well.
    */
  public void drawSprites(Graphics g, int tickTimer) {
    Player tempPlayer = workingGame.getPlayer();
    g.drawImage(playerPic, tempPlayer.getspriteX(), tempPlayer.getspriteY(), null);
    tempPlayer.updateSpriteCollisionSpace();
    for (int i = 0; i < workingGame.getCurrentEnemies().size()-1; i++) { //Comment -1 for enemy 'charge-up'
      Enemy tempEnemy = null;
      if (workingGame.getCurrentEnemies().get(i) instanceof BogusPacket) {

        tempEnemy = (BogusPacket)workingGame.getCurrentEnemies().get(i);
        g.drawImage(bogusPacketPic, tempEnemy.getspriteX(), tempEnemy.getspriteY(), null);

      } else if (workingGame.getCurrentEnemies().get(i) instanceof RogueElectron) {

        tempEnemy = (RogueElectron)workingGame.getCurrentEnemies().get(i);
        g.drawImage(rogueElectronPic, tempEnemy.getspriteX(), tempEnemy.getspriteY(), null);

      } else if (workingGame.getCurrentEnemies().get(i) instanceof Virus) {

        Random rand = new Random();
        tempEnemy = (Virus)workingGame.getCurrentEnemies().get(i);
        if (tempEnemy.getType() == 1) {
          //System.out.println("Virus stage: " + virus1ImageStage);
          if (tickTimer % 50 == 0) {
            virus1ImageStage += 1;
          }
          if (virus1ImageStage == 2) {
            virus1ImageStage = 0;
          }
          g.drawImage(virus1Pics[virus1ImageStage], tempEnemy.getspriteX(), tempEnemy.getspriteY(), null);
        } else {
          if (tickTimer % 50 == 0) {
            imageStage += 1;
          }
          if (imageStage == 3) {
            imageStage = 0;
          }
          if (tempEnemy.getType() == 2) {
            g.drawImage(virus2Pics[imageStage], tempEnemy.getspriteX(), tempEnemy.getspriteY(), null);
          } else if (tempEnemy.getType() == 3) {
            g.drawImage(virus3Pics[imageStage], tempEnemy.getspriteX(), tempEnemy.getspriteY(), null);
          }
        }

      }
      tempEnemy.updateSpriteCollisionSpace();
    }
    g.dispose();
  }

  public void drawTrump(Graphics g) {
    Random rand = new Random();
    g.drawImage(trumpPic, 80 + rand.nextInt(850), 30 + rand.nextInt(400), null);
    g.dispose();
  }

  public void drawMeexSneep(Graphics g) {
    g.drawImage(meexSneepPic, 70, 115, null);
    g.dispose();
  }

  public void drawDyingPlayer(Graphics g, Player player) {
    g.setColor(new Color(255,0,0));
    g.drawRect(player.getspriteX(), player.getspriteY(), player.getSpriteWidth(), player.getSpriteHeight());
    g.dispose();
  }

  public void drawPlayerDefaultPosition(Graphics g) {
    Player tempPlayer = workingGame.getPlayer();
    tempPlayer.setspriteX(INITIAL_PLAYER_POSITIONX);
    tempPlayer.setspriteY(INITIAL_PLAYER_POSITIONY);
    //g.fillRect(tempPlayer.getspriteX(), tempPlayer.getspriteY(), tempPlayer.getPlayerWidth(), tempPlayer.getPlayerHeight());
    g.drawImage(playerPic, tempPlayer.getspriteX(), tempPlayer.getspriteY(), null);
    tempPlayer.updateSpriteCollisionSpace();
    g.dispose();
  }

  public void drawHealthBar(Graphics g, Player player) {
    g.setColor(new Color(0,255,0));
    g.fillRect(40, 40, player.getHealth() * 2, 20);
    g.dispose();
  }

  public void drawTrackLines(Graphics g, GameController controller, int flucSize, ArrayList<Enemy> enemies) {
    for (int j = 0; j < enemies.size(); j++) {
      if (enemies.get(j) instanceof RogueElectron) {
        RogueElectron temp = (RogueElectron)enemies.get(j);
        if (flucSize % 2 == 0) {
          g.setColor(new Color(255, 255, 153));
        } else {
          g.setColor(new Color(0, 255, 0));
        }
        if (temp.trackLineXs.size() != 0) {
          for (int i = 0; i < temp.trackLineXs.size(); i++) {
            g.fillRect(temp.trackLineXs.get(i), temp.trackLineYs.get(i), flucSize, flucSize);
          }
        }
      }
    }
    g.dispose();
  }

  /**
    * Using values collected when the map is rendered, the working background of
    * the frame is added
    */
  public void maintainBackground(Graphics g) {
    g.setColor(Color.black);
    GameController tempController = workingGame.getController();
    for (int i = 0; i < maintainedBackgroundXs.size(); i++) {
      g.drawRect(maintainedBackgroundXs.get(i), maintainedBackgroundYs.get(i), maintainedBackgroundWs.get(i), maintainedBackgroundHs.get(i));
    }
    g.dispose();
  }

  /**
   * Makes checks against the BufferedImage, i.e. if it is the right size, if
   * the right colours were used, etc.
   * @return the validated BufferedImage, null if invalid
   */
  public BufferedImage validateMap(BufferedImage passedImage) {
    //Validates that the passed BufferedImage is legit with the map convention
    //TODO Test dimensions for GUI including colours used and sizes etc!!!!
    if (gatherMapDetails(passedImage) == 0) {
      System.out.println("Image contained invalid colours for map creation");
      return null;
    }
    return passedImage;
  }

  /**
    * This method takes a validated image and finds the parts of the map that
    * the player collides with and where to place enemies etc. it also calculates
    * what parts of the map constitute as the background
    */
  public int gatherMapDetails(BufferedImage validatedImage) {
    allPositions = new int[validatedImage.getWidth()][validatedImage.getHeight()];
    boolean currentBackRunner = false; //For detecting a new background rectangle, false when no box is being gathered
    int counter = 0;
    boolean b = true;
    for (int x = 0; x < (validatedImage.getWidth() - 1); x++) {
      for (int y = 0; y < (validatedImage.getHeight() - 1); y++) {
        //System.out.println(validatedImage.getRGB(x,y));
        if ((validatedImage.getRGB(x,y) == DETECTED_BLACK) && (currentBackRunner == false)) {
          counter++;
          maintainedBackgroundXs.add(x);
          maintainedBackgroundYs.add(y);
          maintainedBackgroundWs.add(1);
          maintainedBackgroundHs.add(1);
          currentBackRunner = true;
        }
        if (currentBackRunner && validatedImage.getRGB(x,y) == DETECTED_BLACK) {
          maintainedBackgroundHs.set(counter-1, (maintainedBackgroundHs.get(counter-1) + 1));
        }
        if (currentBackRunner && validatedImage.getRGB(x,y) == DETECTED_GREEN) {
           currentBackRunner = false;
        }
        switch (validatedImage.getRGB(x, y)) {
          case DETECTED_BLACK : allPositions[x][y] = 0; break; //0 for back
          case DETECTED_GREEN : allPositions[x][y] = 1; break; //1 for walls
          case DETECTED_LBLUE : allPositions[x][y] = 2; break; //2 for menu option border
          case DETECTED_BLUE : allPositions[x][y] = 3; break; //3 for menu option body
          case DETECTED_EXIT_COLOUR : allPositions[x][y] = 4; break; //4 for menu exit option
          case DETECTED_BUTTON_RED : allPositions[x][y] = 5; break; //5 for buttons to the rowhammer stage
          case DETECTED_BUTTON_ORANGE : allPositions[x][y] = 6; break; //6 for buttons to the virus stage
          case PINK : allPositions[x][y] = 7; break; //7 for buttons to the state stage
        }
      }
    }
    workingGame.getController().deepSetCollisionAreas(allPositions, validatedImage.getWidth(), validatedImage.getHeight());
    return 1;
  }

  /**
    * Given a colour code, this method finds all the x and y co-ordinates on
    * the currently loaded map that hold that colour code.
    * @return an array of array lists of size 2 where the first element contains
    * the x co-ordinates of the colours found and the second element contains
    * the y co-ordinates
    */
  public ArrayList<Integer>[] getScreenCoordsByColour(int colourCode) {
    ArrayList<Integer>[] returnArray = new ArrayList[2];
    ArrayList<Integer> justXs = new ArrayList<Integer>();
    ArrayList<Integer> justYs = new ArrayList<Integer>();
    for (int i = 0; i < allPositions.length; i++) {
      for (int j = 0; j < allPositions[1].length; j++) {
        //This condition checks for the colour AND if the colour contains a text message (to ignore) - this is in the second condition
        if (allPositions[i][j] == colourCode) {
          justXs.add(i);
          justYs.add(j);
        }
      }
    }
    returnArray[0] = justXs;
    returnArray[1] = justYs;
    return returnArray;
  }

  public BufferedImage replaceColour(int code, Color color) {
    for (int i = 0; i < allPositions.length; i++) {
      for (int j = 0; j < allPositions[0].length; j++) {
        if (allPositions[i][j] == code) {
          workingImage.setRGB(i,j,color.getRGB());
        }
      }
    }
    return workingImage;
  }

  /**
    * For manipulating the current map buffer
    */
  public int getCurrentMap() {
    return currentMap;
  }
  public void setCurrentMap(int currentMap) {
    this.currentMap = currentMap;
  }
  public void incrementCurrentMap() {
    setCurrentMap(getCurrentMap()+1);
  }

  public BufferedImage getWorkingImage() {
    return workingImage;
  }

  public int[][] getAllPositions() {
    return allPositions;
  }

  public int getDETECTED_BLACK() {
    return DETECTED_BLACK;
  }

  public int getDETECTED_GREEN() {
    return DETECTED_GREEN;
  }
}
