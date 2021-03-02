import javax.swing.JPanel;
import java.awt.event.KeyListener;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.Timer;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.JFrame;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * This class acts as the central 'element' around which the game
 * fully operates. This class is responsible for initialising the GUI
 * in accordance with the MapLoader class, updating the GUI in accordance
 * with the GameController class when game events such as movement and
 * attack occur, and that the stages follow each other in chronological
 * succession.
 */
public class Game extends JPanel implements KeyListener, ActionListener, MouseListener {

  /*Object variables that are updated around the central game state in order for
  other classes to view up to date information regarding the other classes*/
  private MapLoader loader;
  private JFrame frame;
  private Timer tickTimer;
  private GameController controller;
  private Player player;
  private int currentMap;
  private BufferedImage forChangeImage;
  private Random rand;

  /*The timer delay specifies the ms amount by which the paint function repaints*/
  private final int TICK_TIMER_DELAY = 1;

  /*The mouse listener records co-ordinates not in relation to the frame but the
  screen. These offset constants record where the mouse is in accordance with the
  default position of the frame to counteract this*/
  private final int X_MOUSE_POSITION_OFFSET = 14;
  private final int Y_MOUSE_PORTION_OFFSET = 43;

  /*Timers that are used for storing how things are spawned, when a map may be
  incremented, and how many ms have elapsed since the beginning of the game*/
  private int tickTimerCountdown;
  private int packetSpawnerTimer;
  private int genericTickTimer;
  private int electronSpawnerTimer;
  private final int INITIAL_COUNTDOWN_TIMER = 1000;

  /*String names for the maps used*/
  private final String INITIAL_MAP = "start_screen.png";
  private final String DETAILS1 = "inbetween1.png";
  private final String STAGE1 = "map1.png";
  private final String STAGE2 = "map2.png";
  private final String DETAILS2 = "crashed!.png";
  private final String DETAILS3 = "inbetween2.png";
  private final String SEGFAULT = "segfault.png";
  private final String DETAILS4 = "inbetween3.png";
  private final String DETAILS5 = "cpuoverheat.png";
  private final String DETAILS6 = "inbetween4.png";

  /*Boolean values used to control how the stages are incremented*/
  private boolean startOptionClicked;
  private boolean mapFlopper;
  private boolean ddosFinished;
  private boolean drumpfKill;

  /*ArrayLists that store the on-screen co-ordinates for various buttons*/
  private ArrayList<Integer> buttonXs;
  private ArrayList<Integer> buttonYs;
  private ArrayList<Integer> exitButtonXs;
  private ArrayList<Integer> exitButtonYs;
  private ArrayList<Integer> rowhammerButtonXs;
  private ArrayList<Integer> rowhammerButtonYs;
  private ArrayList<Integer> virusButtonXs;
  private ArrayList<Integer> virusButtonYs;
  private ArrayList<Integer> stateButtonXs;
  private ArrayList<Integer> stateButtonYs;
  private ArrayList<Integer> tempListXs;
  private ArrayList<Integer> tempListYs;

  /*Enemy management variables*/
  private ArrayList<Enemy> currentEnemies;
  private int numberOfEnemies;

  /*Used for DDoS spawns*/
  private int PACKET_SPAWN_RATE = 100; //higher = lower spawn rate
  private final int MAX_PACKET_SPAWNS = 10000;

  /*Used for electron spawns and the size of electron tractor beams*/
  private int MAX_ELECTRON_SPAWNS = 16;
  private int flucSize;
  private final int INITIAL_FLUC_SIZE = 3;

  private int MAX_VIRUS_SPAWNS = 16;

  public Game() {
    currentMap = 0;
    tickTimerCountdown = INITIAL_COUNTDOWN_TIMER;
    startOptionClicked = false;
    mapFlopper = false;
    ddosFinished = false;
    drumpfKill = false;
    electronSpawnerTimer = 0;
    flucSize = INITIAL_FLUC_SIZE;
    packetSpawnerTimer = 0;
    genericTickTimer = 0;
    numberOfEnemies = 0;
    buttonXs = new ArrayList<Integer>();
    buttonYs = new ArrayList<Integer>();
    exitButtonXs = new ArrayList<Integer>();
    exitButtonYs = new ArrayList<Integer>();
    rowhammerButtonXs = new ArrayList<Integer>();
    rowhammerButtonYs = new ArrayList<Integer>();
    virusButtonXs = new ArrayList<Integer>();
    virusButtonYs = new ArrayList<Integer>();
    stateButtonXs = new ArrayList<Integer>();
    stateButtonYs = new ArrayList<Integer>();
    tempListXs = new ArrayList<Integer>();
    tempListYs = new ArrayList<Integer>();
    currentEnemies = new ArrayList<Enemy>();
    addKeyListener(this); //Adds the key listener to the frame
    addMouseListener(this); //"" mouse listener
    setFocusable(true);
    setFocusTraversalKeysEnabled(true);
    frame = new JFrame();
    loader = new MapLoader(this, frame);
    loader.setUpFrame();
    player = new Player();
    controller = new GameController();
    tickTimer = new Timer(TICK_TIMER_DELAY, this);
    rand = new Random();
    tickTimer.start();
  }

  /**
    * A paint function must exist within the Game class because it is the class
    * where the tick timer is used. The tick timer in AWT is how the UI checks
    * for updates to the screen, and thus, calls upon the paint method.
    */
  public void paint(Graphics g) {
    tickTimerCountdown -= 1;
    genericTickTimer += 1;
    if (player.getHealth() <= 0) {
      //performAction(4);
      player.setHealth(100);
    }
    if (flucSize == 10) {
      flucSize = 3;
    }
    if (genericTickTimer % 5 == 0) {
      flucSize++;
    }

    //Code for initial map
    if (currentMap == 0 && mapFlopper == false) {
      loader.updateBufferedMap(INITIAL_MAP);
      loader.drawMapInBuffer(g.create());
      updateButtonLocations(3); //3 for button
      updateButtonLocations(4); //4 for exit button
      updateButtonLocations(5); //rowhammer button
      updateButtonLocations(6); //virus button
      updateButtonLocations(7); //State button
      mapFlopper = true;
    } else if (currentMap == 0 && startOptionClicked ) {
      tearDownInfoStage();
    }
    //Code for initial map

    //Code for first in-between map
    if (currentMap == 1 && mapFlopper == true) {
      loader.updateBufferedMap(DETAILS1);
      loader.drawMapInBuffer(g.create());
      updateButtonLocations(3);
      mapFlopper = false;
    } else if (currentMap == 1 && startOptionClicked) {
      tearDownInfoStage();
    }
    //Code for first in-between map

    //Code for first map
    if (currentMap == 2 && mapFlopper == false) {
      mapFlopper = true;
      loader.updateBufferedMap(STAGE1);
      loader.drawMapInBuffer(g.create());
      tickTimerCountdown = 100000;
    }
    if (currentMap == 2) {
      packetSpawnerTimer += 10;
      if (genericTickTimer % 25 == 0) {
        PACKET_SPAWN_RATE -= 1;
        if (PACKET_SPAWN_RATE <= 0) {
          ddosFinished = true;
        }
      }
      if (genericTickTimer % 50 == 0) {
        PACKET_SPAWN_RATE += 1;
      }
      if (PACKET_SPAWN_RATE != 0) {
        if (packetSpawnerTimer % PACKET_SPAWN_RATE == 0 && numberOfEnemies < MAX_PACKET_SPAWNS) {
          //Spawn new BogusPacket
          currentEnemies.add(new BogusPacket());
          numberOfEnemies += 1;
        } else if (ddosFinished) {
          currentMap += 1;
        }
      }
    }
    //Code for first map

    //Code for crashed screen!
    if (currentMap == 3 && mapFlopper == true) {
      //ddosFinished = false;
      //crashClicked = true;
      mapFlopper = false;
      loader.updateBufferedMap(DETAILS2);
      loader.drawMapInBuffer(g.create());
      updateButtonLocations(3);
    } else if (currentMap == 3 && startOptionClicked) {
      tearDownInfoStage();
    }
    //Code for crashed screen!

    //Code for rowhammer initialisation
    if (currentMap == 4 && mapFlopper == false) {
      mapFlopper = true;
      loader.updateBufferedMap(DETAILS3);
      loader.drawMapInBuffer(g.create());
      updateButtonLocations(3);
    } else if (currentMap == 4 && startOptionClicked) {
      tearDownInfoStage();
    }
    //Code for rowhammer initialisation

    //Code for rowhammer
    if (currentMap == 5 && mapFlopper == true) {
      loader.updateBufferedMap(STAGE2);
      loader.drawMapInBuffer(g.create());
      mapFlopper = false;
      clearAllEnemies();
      loader.drawPlayerDefaultPosition(g.create());
    } else if (currentMap == 5) {
      //code for rowhammer attack
      packetSpawnerTimer += 10;
      if (packetSpawnerTimer % 2000 == 0 && currentEnemies.size() < MAX_ELECTRON_SPAWNS) {
        RogueElectron e = new RogueElectron();
        currentEnemies.add(e);
      } else if (currentEnemies.size() == MAX_ELECTRON_SPAWNS) {
        clearAllEnemies();
        currentMap += 1;
      }
    }
    //Code for rowhammer

    //Code for segfault!
    if (currentMap == 6 && mapFlopper == false) {
      mapFlopper = true;
      loader.updateBufferedMap(SEGFAULT);
      loader.drawMapInBuffer(g.create());
      updateButtonLocations(3);
    } else if (currentMap == 6 && startOptionClicked) {
      tearDownInfoStage();
    }
    //Code for segfault!

    //Code for virus init
    if (currentMap == 7 && mapFlopper == true) {
      mapFlopper = false;
      loader.updateBufferedMap(DETAILS4);
      loader.drawMapInBuffer(g.create());
      updateButtonLocations(3);
    } else if (currentMap == 7 && startOptionClicked) {
      tearDownInfoStage();
    }
    //Code for virus init

    //Virus stage
    if (currentMap == 8 && mapFlopper == false) {
      mapFlopper = true;
      loader.updateBufferedMap(STAGE1);
      loader.drawMapInBuffer(g.create());
      loader.drawPlayerDefaultPosition(g.create());
      genericTickTimer = 0;
    } else if (currentMap == 8) {
      if (genericTickTimer % 100 == 0) {
        if (currentEnemies.size() == 0) {
          Virus v = new Virus(Virus.INITIAL_X, Virus.INITIAL_Y);
          currentEnemies.add(v);
        } else if (currentEnemies.size() < MAX_VIRUS_SPAWNS) {
          if (genericTickTimer % 100 == 0) {
            Virus v = new Virus(currentEnemies.get(currentEnemies.size()-1).getspriteX(), currentEnemies.get(currentEnemies.size()-1).getspriteY());
            currentEnemies.add(v);
          }
        } else {
          currentEnemies.clear();
          currentMap++;
        }
      }
    }
    //Virus stage

    //CPU Overheat!
    if (currentMap == 9 && mapFlopper) {
      mapFlopper = false;
      loader.updateBufferedMap(DETAILS5);
      loader.drawMapInBuffer(g.create());
      updateButtonLocations(3);
    } else if (currentMap == 9 && startOptionClicked) {
      tearDownInfoStage();
    }
    //CPU Overheat!

    //State actor init stage
    if (currentMap == 10 && mapFlopper == false) {
      mapFlopper = true;
      loader.updateBufferedMap(DETAILS6);
      loader.drawMapInBuffer(g.create());
      updateButtonLocations(3);
    } else if (currentMap == 10 && startOptionClicked) {
      tearDownInfoStage();
    }
    //State actor init stage

    if (currentMap == 11 && mapFlopper == true) {
      mapFlopper = false;
      loader.updateBufferedMap(STAGE1);
      loader.drawMapInBuffer(g.create());
      loader.drawPlayerDefaultPosition(g.create());
      genericTickTimer = 0;
    } else if (currentMap == 11) {
      if (currentEnemies.size() == 0) {
        for (int i = 0; i < 10; i++) {
          Virus v = new Virus(20 + rand.nextInt(1400), 20 + rand.nextInt(700));
          currentEnemies.add(v);
          RogueElectron e = new RogueElectron();
          currentEnemies.add(e);
        }
      }
      if (genericTickTimer % 10 == 0) {
        currentEnemies.add(new BogusPacket());
      }
      if (genericTickTimer % 1000 == 0) {
        drumpfKill = true;
      }
    }

    //Movement code
    if (currentMap == 2 || currentMap == 5 || currentMap == 8 || currentMap == 11) { //3 = ddos, 5 = rowhammer, 8 = virus, 11 = state actor
      loader.maintainBackground(g.create());
      loader.drawSprites(g.create(), genericTickTimer);
      loader.drawHealthBar(g.create(), player);
      if (currentMap == 5 || currentMap == 11) {
        loader.drawTrackLines(g.create(), controller, flucSize, currentEnemies);
      }
      if (currentMap == 11) {
        loader.drawTrump(g.create());
        if (drumpfKill) {
          currentEnemies.clear();
          loader.maintainBackground(g.create());
          currentMap++;
          loader.drawMeexSneep(g.create());
        }
      }
      player.setSpriteColour(new Color(0, 255, 0));
      g.setClip(player.getspriteX(), player.getspriteY(), player.getSpriteWidth(), player.getSpriteHeight());
      for (int i = 0; i < currentEnemies.size()-1; i++) {
        controller.enemyMovementActuator(currentEnemies.get(i), i, this);
        //Enemy collision detection
        if (g.hitClip(currentEnemies.get(i).getspriteX(), currentEnemies.get(i).getspriteY(), currentEnemies.get(i).getSpriteWidth(), currentEnemies.get(i).getSpriteHeight())) {
          player.setSpriteColour(new Color(255, 0, 0));
          //loader.drawDyingPlayer(g, player);
          player.decrementHealth();
          //loader.drawSprites(g.create(), genericTickTimer);
          loader.drawHealthBar(g.create(), player);
        }
      }
    }
  }

  private void loadInfoStage(Graphics g, String stageName) {
    //
  }

  private void tearDownInfoStage() {
    startOptionClicked = false;
    tearDownButtonLocations();
    currentMap += 1;
  }

  private void clearAllEnemies() {
    currentEnemies.clear();
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    //TICK UPDATES OCCUR IN HERE!!!!!!!!!!!!!!
    controller.playerMovementActuator(e, this);
    //Could involve enemy logic here...
    repaint();
  }

  @Override
  public void keyTyped(KeyEvent e) {
    //TODO
  }

  @Override
  public void keyPressed(KeyEvent e) {
    System.out.println(e.getKeyChar());
    controller.keyPressedDetects(e.getKeyCode());
  }

  @Override
  public void keyReleased(KeyEvent e) {
    System.out.println("Released: " + e.getKeyChar());
    controller.keyReleasedDetects(e.getKeyCode());
  }

  @Override
  public void mousePressed(MouseEvent e) {
    //int[][] tempCollision = loader.getCollisionAreas()
    System.out.println("Clicked " + e.getXOnScreen() + ", " + e.getYOnScreen() + " CC: " + controller.getCollisionAreas()[e.getXOnScreen()][e.getYOnScreen()]);
    //checkPressActivity(e, "Normal?");
    //checkPressActivity(e, "Exit?");
    if ((e.getXOnScreen() >= buttonXs.get(0) + X_MOUSE_POSITION_OFFSET) && (e.getXOnScreen() <= buttonXs.get(1) + X_MOUSE_POSITION_OFFSET)) {
      if ((e.getYOnScreen() >= (buttonYs.get(0) + Y_MOUSE_PORTION_OFFSET)) && (e.getYOnScreen() <= (buttonYs.get(1) + Y_MOUSE_PORTION_OFFSET))) {
        performAction(1);
      }
    }
    if ((e.getXOnScreen() >= exitButtonXs.get(0) + X_MOUSE_POSITION_OFFSET) && (e.getXOnScreen() <= exitButtonXs.get(1) + X_MOUSE_POSITION_OFFSET)) {
      if ((e.getYOnScreen() >= (exitButtonYs.get(0) + Y_MOUSE_PORTION_OFFSET)) && (e.getYOnScreen() <= (exitButtonYs.get(1) + Y_MOUSE_PORTION_OFFSET))) {
        performAction(2);
      }
    }
    if ((e.getXOnScreen() >= rowhammerButtonXs.get(0) + X_MOUSE_POSITION_OFFSET) && (e.getXOnScreen() <= rowhammerButtonXs.get(1) + X_MOUSE_POSITION_OFFSET)) {
      if ((e.getYOnScreen() >= (rowhammerButtonYs.get(0) + Y_MOUSE_PORTION_OFFSET)) && (e.getYOnScreen() <= (rowhammerButtonYs.get(1) + Y_MOUSE_PORTION_OFFSET))) {
        performAction(3);
      }
    }
    if ((e.getXOnScreen() >= virusButtonXs.get(0) + X_MOUSE_POSITION_OFFSET) && (e.getXOnScreen() <= virusButtonXs.get(1) + X_MOUSE_POSITION_OFFSET)) {
      if ((e.getYOnScreen() >= (virusButtonYs.get(0) + Y_MOUSE_PORTION_OFFSET)) && (e.getYOnScreen() <= (virusButtonYs.get(1) + Y_MOUSE_PORTION_OFFSET))) {
        performAction(5); //4 is used for death...
      }
    }
    if ((e.getXOnScreen() >= stateButtonXs.get(0) + X_MOUSE_POSITION_OFFSET) && (e.getXOnScreen() <= stateButtonXs.get(1) + X_MOUSE_POSITION_OFFSET)) {
      if ((e.getYOnScreen() >= (stateButtonYs.get(0) + Y_MOUSE_PORTION_OFFSET)) && (e.getYOnScreen() <= (stateButtonYs.get(1) + Y_MOUSE_PORTION_OFFSET))) {
        performAction(6); //4 is used for death...
      }
    }
  }

  @Override
  public void mouseReleased(MouseEvent e) {
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  private void updateButtonLocations(int colourCode) {
    ArrayList<Integer>[] takenCoords =  loader.getScreenCoordsByColour(colourCode);
    ArrayList<Integer> takenCoordsX = takenCoords[0];
    ArrayList<Integer> takenCoordsY = takenCoords[1];
    System.out.println(takenCoordsX.size() + " xxx");
    tempListXs.add(takenCoordsX.get(0));
    tempListXs.add(takenCoordsX.get(takenCoordsX.size() - 1));
    tempListYs.add(takenCoordsY.get(0));
    tempListYs.add(takenCoordsY.get(takenCoordsY.size() - 1));
    if (colourCode == 3) {
      buttonXs = (ArrayList<Integer>)tempListXs.clone();
      buttonYs = (ArrayList<Integer>)tempListYs.clone();
    } else if (colourCode == 5) {
      rowhammerButtonXs = (ArrayList<Integer>)tempListXs.clone();
      rowhammerButtonYs = (ArrayList<Integer>)tempListYs.clone();
    } else if (colourCode == 6) {
      virusButtonXs = (ArrayList<Integer>)tempListXs.clone();
      virusButtonYs = (ArrayList<Integer>)tempListYs.clone();
    } else if (colourCode == 7) {
      stateButtonXs = (ArrayList<Integer>)tempListXs.clone();
      stateButtonYs = (ArrayList<Integer>)tempListYs.clone();
    } else {
      exitButtonXs = (ArrayList<Integer>)tempListXs.clone();
      exitButtonYs = (ArrayList<Integer>)tempListYs.clone();
    }
    tempListXs.clear();
    tempListYs.clear();
  }

  private void tearDownButtonLocations() {
    buttonXs.clear();
    buttonYs.clear();
    exitButtonXs.clear();
    exitButtonYs.clear();
    rowhammerButtonXs.clear();
    rowhammerButtonYs.clear();
    virusButtonXs.clear();
    virusButtonYs.clear();
    stateButtonXs.clear();
    stateButtonYs.clear();
  }

  private void performAction(int buttonClickAction) {
    switch (buttonClickAction) {
      case 1 : System.out.println("Clicked next!"); startOptionClicked = true; break;
      case 2 : System.out.println("Clicked exit!"); System.exit(0); break;
      case 3 : System.out.println("Skipped to rowhammer..."); currentMap = 4; mapFlopper = false; break;
      case 4 : System.out.println("Died!"); currentMap = 1; mapFlopper = true; break;
      case 5 : System.out.println("Skipping to virus..."); currentMap = 7; mapFlopper = true; break;
      case 6 : System.out.println("Skipping to state..."); currentMap = 10; mapFlopper = false; break;
      default : System.out.println("Didn't press a button!");
    }
  }

  public GameController getController() {
    return controller;
  }

  public Player getPlayer() {
    return player;
  }

  public MapLoader getMapLoader() {
    return loader;
  }

  public ArrayList<Enemy> getCurrentEnemies() {
    return currentEnemies;
  }

  /*private void checkPressActivity(MouseEvent e, String buttonQuery) {
    int buttonClickAction = 0;
    boolean areaCheck = false;
    if (buttonQuery.equals("Normal?")) {
      tempListXs = (ArrayList<Integer>)buttonXs.clone();
      tempListYs = (ArrayList<Integer>)buttonYs.clone();
      buttonClickAction = 1; //startOptionClicked = true
      areaCheck = true;
    } else if (buttonQuery.equals("Exit?")) {
      tempListXs = (ArrayList<Integer>)exitButtonXs.clone();
      tempListYs = (ArrayList<Integer>)exitButtonYs.clone();
      buttonClickAction = 2; //System.exit(0)
      areaCheck = true;
    }
    System.out.println("Calculated " + tempListXs.size());
    if (areaCheck) {
      if (e.getXOnScreen() >= tempListXs.get(0) && e.getXOnScreen() <= tempListXs.get(1) && e.getYOnScreen() >= tempListYs.get(0) && e.getYOnScreen() <= tempListYs.get(1)) {
        performAction(buttonClickAction);
      }
    }
    tempListXs.clear();
    tempListYs.clear();
  }*/
}
