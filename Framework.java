import java.awt.Graphics2D;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import java.awt.Color;
import java.awt.Font;

/**
 * Framework that controls the game (Game.java) that created it, update it and draw it on the screen.
 *
 * @frameworkauthor gametutorial.net
 */

public class Framework extends Canvas {
    
    // Width and height of the frame.
    public static int frameWidth, frameHeight;

    // Time of one second in nanoseconds.
    public static final long secInNanosec = 1000000000L;
    
    // Time of one millisecond in nanoseconds.
    public static final long milisecInNanosec = 1000000L;
    
    // FPS - Frames per second
    private final int GAME_FPS = 60;

    // Pause between updates. It is in nanoseconds.
    private final long GAME_UPDATE_PERIOD = secInNanosec / GAME_FPS;
    
    // Possible states of the game
    public static enum GameState{STARTING, VISUALIZING, GAME_CONTENT_LOADING, MAIN_MENU, OPTIONS, PLAYING, GAMEOVER, DESTROYED}
    
    // Current state of the game
    public static GameState gameState;
    
    // Elapsed game time in nanoseconds.
    private long gameTime;

    // It is used for calculating elapsed time.
    private long lastTime;
    private long waitTime;
    
    // The actual game
    private Game game;
    
    //Game background
    private BufferedImage background;

    private Graphics2D lastGameGraphics;
    
    //Main menu components
    private BufferedImage mainMenu;
    private boolean menuBool = true;
    
    private BufferedImage easyhover, easy, easypressed;
    private BufferedImage mediumhover, medium, mediumpressed;
    private BufferedImage hardhover, hard, hardpressed;
    
    private boolean E, M, H;
    private static String difficulty; 

    
    
    public Framework () {
        super();
        
        gameState = GameState.VISUALIZING;
        
        //We start game in new thread.
        Thread gameThread = new Thread() {
            @Override
            public void run(){
                GameLoop();
            }
        };
        gameThread.start();
    }
    
    
   /**
     * Set variables and objects.
     * This method is intended to set the variables and objects for this class, variables and objects for the actual game can be set in Game.java.
     */
    private void Initialize() {
    	waitTime = 0L; 
    }
    
    /**
     * Load files - images, sounds, ...
     * This method is intended to load files for this class, files for the actual game can be loaded in Game.java.
     */
    private void LoadContent() {
        try {
            background = ImageIO.read(new File("resources/images/gameboard.png"));
            mainMenu = ImageIO.read(new File("resources/images/menumain.png"));
            easy = ImageIO.read(new File("resources/images/easy.png"));
            easypressed = ImageIO.read(new File("resources/images/easypressed.png"));
            medium = ImageIO.read(new File("resources/images/medium.png"));
            mediumpressed = ImageIO.read(new File("resources/images/mediumpressed.png"));
            hard = ImageIO.read(new File("resources/images/hard.png"));
            hardpressed = ImageIO.read(new File("resources/images/hardpressed.png"));
        } catch (IOException ex) {
            System.out.println("Images not loaded");
            System.exit(1);
        }
        easyhover = easy;
        mediumhover = medium;
        hardhover = hard;
    }
    
    
    /**
     * In specific intervals of time (GAME_UPDATE_PERIOD) the game/logic is updated and then the game is drawn on the screen.
     */
    private void GameLoop() {
        // This two variables are used in VISUALIZING state of the game. We used them to wait some time so that we get correct frame/window resolution.
        long visualizingTime = 0, lastVisualizingTime = System.nanoTime();
        
        // This variables are used for calculating the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
        long beginTime, timeTaken, timeLeft;
        
        while (true) {
            try {
                beginTime = System.nanoTime();
                
                switch (gameState) {
                    case PLAYING:
                        gameTime += System.nanoTime() - lastTime;
                        game.UpdateGame(gameTime, mousePosition());
                        lastTime = System.nanoTime();
                        break;
                    case GAMEOVER:
                    	
                        break;
                    case DESTROYED:
                    	
                    	break;
                    case MAIN_MENU:
                        if (menuBool) {
                            Point point = mousePosition();
                            double x, y;
                            x = point.getX();
                            y = point.getY();
                            //Hover above EASY
                            if ((x > 260) && (x < (260 + easy.getWidth())) && (y > 350) && (y < (350 + easy.getHeight()))) {
                                easyhover = easypressed;
                                E = true;
                            } else {
                                easyhover = easy;
                                E = false;
                            }
                            //Hover above hard
                            if ((x > 614) && (x < (614 + hard.getWidth())) && (y > 350) && (y < (350 + hard.getHeight()))) {
                                hardhover = hardpressed;
                                H = true;
                            } else {
                                hardhover = hard;
                                H = false; 
                            }
                            //hover above medium
                            if ((x > 441) && (x < (441 + medium.getWidth())) && (y > 350) && (y < (350 + medium.getHeight()))) {
                                mediumhover = mediumpressed;
                                difficulty = "MEDIUM"; 
                                M = true;
                            } else {
                                mediumhover = medium;
                                M = false;
                            }

                        } else {
                            newGame(); 
                            gameState = GameState.PLAYING;
                        }
                        break;
                    case OPTIONS:
                        //...
                        break;
                    case GAME_CONTENT_LOADING:
                        //...
                        break;
                    
                    case STARTING:
                        // Sets variables and objects.
                        Initialize();
                        // Load files - images, sounds, ...
                        LoadContent();
                        // When all things that are called above finished, we change game status to main menu.
                 
                        gameState = GameState.MAIN_MENU;
                        break;
                    case VISUALIZING:
                        // On Ubuntu OS (when I tested on my old computer) this.getWidth() method doesn't return the correct value immediately (eg. for frame that should be 800px width, returns 0 than 790 and at last 798px). 
                        // So we wait one second for the window/frame to be set to its correct size. Just in case we
                        // also insert 'this.getWidth() > 1' condition in case when the window/frame size wasn't set in time,
                        // so that we although get approximately size.
                        if (this.getWidth() > 1 && visualizingTime > secInNanosec) {
                            frameWidth = this.getWidth();
                            frameHeight = this.getHeight();

                            // When we get size of frame we change status.
                            gameState = GameState.STARTING;
                        } else {
                            visualizingTime += System.nanoTime() - lastVisualizingTime;
                            lastVisualizingTime = System.nanoTime();
                        }
                        break;
                }
                
                // Repaint the screen.
                repaint();
                
                // Here we calculate the time that defines for how long we should put threat to sleep to meet the GAME_FPS.
                timeTaken = System.nanoTime() - beginTime;
                timeLeft = (GAME_UPDATE_PERIOD - timeTaken) / milisecInNanosec; // In milliseconds
                // If the time is less than 10 milliseconds, then we will put thread to sleep for 10 millisecond so that some other thread can do some work.
                if (timeLeft < 10) 
                    timeLeft = 10; //set a minimum
                try {
                     //Provides the necessary delay and also yields control so that other thread can do work.
                     Thread.sleep(timeLeft);
                } catch (InterruptedException ex) { }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Draw the game to the screen. It is called through repaint() method in GameLoop() method.
     */
    @Override
    public void Draw(Graphics2D g2d) {
        try {
            switch (gameState) {
                case PLAYING:
                    game.Draw(gameTime, g2d, mousePosition());
                    lastGameGraphics = g2d;
                    break;
                case GAMEOVER:
                	g2d.setFont(new Font("Arial", Font.PLAIN, 110));
                    g2d.setColor(Color.green);
                    g2d.drawString("YOU HAVE WON", 45, ((Window.GAME_HEIGHT_IN_UNITS * Window.UNIT_LEN) / 2));
                    g2d.setFont(new Font("Arial", Font.PLAIN, 50));
                    g2d.setColor(Color.green);
                    g2d.drawString("click to continue", 350, ((Window.GAME_HEIGHT_IN_UNITS * Window.UNIT_LEN)/2)+ 150);

                    break;
                case MAIN_MENU:
                    g2d.drawImage(background, 0, 0, null);
                    g2d.drawImage(mainMenu, 260, 100, null);
                    g2d.drawImage(easyhover, 260, 350, null);
                    g2d.drawImage(hardhover, 614, 350, null);
                    g2d.drawImage(mediumhover, 441, 350, null);
                    break;
                case OPTIONS:
                    //...
                    break;
                case GAME_CONTENT_LOADING:
                    //...
                    break;
                case DESTROYED:
                	g2d.setFont(new Font("Arial", Font.PLAIN, 120));
                    g2d.setColor(Color.red);
                    g2d.drawString("YOU HAVE DIED", 50, ((Window.GAME_HEIGHT_IN_UNITS * Window.UNIT_LEN) / 2));
                    g2d.setFont(new Font("Arial", Font.PLAIN, 50));
                    g2d.setColor(Color.red);
                    g2d.drawString("click to continue", 350 , ((Window.GAME_HEIGHT_IN_UNITS * Window.UNIT_LEN)/2) + 150);
                	break;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
    
    
    /**
     * Starts new game.
     */
    private void newGame() {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game = new Game();
        game.difficulty = difficulty; 
    }
    
    /**
     *  Restart game - reset game time and call RestartGame() method of game object so that reset some variables.
     */
    private void restartGame() {
        // We set gameTime to zero and lastTime to current time for later calculations.
        gameTime = 0;
        lastTime = System.nanoTime();
        
        game.RestartGame();
        
        // We change game status so that the game can start.
        gameState = GameState.PLAYING;
    }
    
    
    /**
     * Returns the position of the mouse pointer in game frame/window.
     * If mouse position is null than this method return 0,0 coordinate.
     * 
     * @return Point of mouse coordinates.
     */
    private Point mousePosition() {
        try {
            Point mp = this.getMousePosition();
            
            if(mp != null)
                return this.getMousePosition();
            else
                return new Point(0, 0);
        } catch (Exception e) {
            return new Point(0, 0);
        }
    }
    
    
    /**
     * This method is called when keyboard key is released.
     * 
     * @param e KeyEvent
     */
    @Override
    public void keyReleasedFramework(KeyEvent e) {
        
    }
    
    /**
     * This method is called when mouse button is clicked.
     * 
     * @param e MouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent e) {
    	
            switch (gameState) {
                case PLAYING:
                    break;
                case GAMEOVER:
                	menuBool = true;
            		gameState = GameState.STARTING;
                    break;
                case DESTROYED:
                	menuBool = true;
            		gameState = GameState.STARTING;
            		break;
                case MAIN_MENU:
                	if (E) {
                        difficulty = "EASY";
                        menuBool = false;
                    } else if (M) {
                        difficulty = "MEDIUM";
                        menuBool = false;
                    } else if (H) {
                        difficulty = "HARD";
                        menuBool = false;
                    }
                    break;
                case OPTIONS:
                    //...
                    break;
                case GAME_CONTENT_LOADING:
                    //...
                    break;
            }

    
    	
    }
}
