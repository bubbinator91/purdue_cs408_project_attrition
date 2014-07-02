import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

/**
 * Actual game.
  *
 * @frameworkauthor gametutorial.net
 */

public class Game {
    // The Map system for the game.
    private Map map;
    
    public static int score, boost;
    public static int health;
    public static boolean alive , win;
    
    //The controllable character.
    private Player player;

    // The enemies trying to get by the towers
    public Enemies enemies;
    
    //The towers that will attack enemies
    private Towers towers;

    // Stores the actual time when the game was last updated.
    private long lastRealTime;

    public String difficulty; 
    
    //Background image
    private BufferedImage background;
    
    //private BufferedImage waveimg, wave1;
    private BufferedImage sendWave; 
    
    private Wave wave;
    private int currentWave; 
    private int nextWave;
    private boolean[] lastMouseButtonState;
   
    
    public Game() {
        Framework.gameState = Framework.GameState.GAME_CONTENT_LOADING;
        Thread threadForInitGame = new Thread() {
            @Override
            public void run() {
             // Sets variables and objects for the game.
                Initialize();
                // Load game files (images, sounds, ...)
                LoadContent();
                
                Framework.gameState = Framework.GameState.PLAYING;
            }
        };
        threadForInitGame.start();
    }
    
    
   /**
     * Set variables and objects for the game.
     */
    private void Initialize() {
        currentWave = 1;
        nextWave = 1;
        alive = true;
        win = false;
        wave = new Wave(); 
        map = new Map();
        enemies = new Enemies();
        towers = new Towers();
        player = new Player();
        
        lastMouseButtonState = new boolean[3];
        lastMouseButtonState[0] = Canvas.mouseButtonState(MouseEvent.BUTTON1);
		lastMouseButtonState[1] = Canvas.mouseButtonState(MouseEvent.BUTTON2);
		lastMouseButtonState[2] = Canvas.mouseButtonState(MouseEvent.BUTTON3);
		
        if (difficulty.equals("EASY")) {
            health = 100;
            score = 300;
            boost = 50;
        } else if (difficulty.equals("MEDIUM")) {
            health = 75;
            score = 250;
            boost = 35; 
        } else if (difficulty.equals("HARD")) {
            health = 50; 
            score = 200;
            boost = 25;
        }
        lastRealTime = 0;
    }
    
    /**
     * Load game files - images, sounds, ...
     */
    private void LoadContent() {
        try {
            background = ImageIO.read(new File("resources/images/gameboard.png"));
            //wave1 = ImageIO.read(new File("resources/images/wave1.png"));
            sendWave = ImageIO.read(new File("resources/images/sendwave.png"));
        } catch (IOException ex) {
            System.out.println("Image not loaded");
        }
        //waveimg = wave1;
    }    
    
    private void gameOver(){
    	if(health <= 0){
    		alive = false;
    		Framework.gameState = Framework.GameState.DESTROYED;
    	} 
    	if ( wave.isLastWave() && enemies.getSize() == 0 ) {
    		win = true; 
    		Framework.gameState = Framework.GameState.GAMEOVER;
    	}
    }
    
    /**
     * Restart game - reset some variables.
     */
    public void RestartGame() {

    }
    
    
    /**
     * Update game logic.
     * 
     * @param gameTime gameTime of the game.
     * @param mousePosition current mouse position.
     */
    public void UpdateGame(long gameTime, Point mousePosition) {
        
        double x = mousePosition.getX();
        double y = mousePosition.getY();
        //Hover above EASY
        if ((Canvas.mouseButtonState(MouseEvent.BUTTON1)) && !lastMouseButtonState[0] && ((x > 800) && (y > 690)) ) {
            if (currentWave == nextWave)
            	if ( currentWave != 1 ) {score += boost;}
                nextWave++;
        }

        double timeBetweenEnemySpawns = Framework.secInNanosec;
        double gameDuration = (double)gameTime / Framework.secInNanosec;
        int enemyType; 


        if (gameTime - lastRealTime > timeBetweenEnemySpawns && currentWave != nextWave) {
            lastRealTime = gameTime;
            enemyType = wave.popEnemy(); 
            if (enemyType != -1)
                enemies.spawnEnemy(enemyType);
            else{
            	timeBetweenEnemySpawns *= 0.8;
                currentWave = nextWave; 
            }
        }

        if (currentWave == nextWave)
            wave.Update(currentWave);
        
        gameOver();
        enemies.Update(map);
        towers.Update(gameTime, map, enemies, mousePosition);
        player.Update(map, enemies);
    
    
        lastMouseButtonState[0] = Canvas.mouseButtonState(MouseEvent.BUTTON1);
        lastMouseButtonState[1] = Canvas.mouseButtonState(MouseEvent.BUTTON2);
        lastMouseButtonState[2] = Canvas.mouseButtonState(MouseEvent.BUTTON3);
    
    }
    
    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
    public void Draw(long gameTime, Graphics2D g2d, Point mousePosition) {
        g2d.drawImage(background, 0, 0, null);
        if (currentWave == nextWave && enemies.getSize() == 0)
            g2d.drawImage(sendWave, 800, 690, null);
        g2d.setColor(Color.white);
        g2d.drawString("Resources: " + score, 10, 21);
        g2d.drawString("Health: " + health, 950, 21);
        g2d.drawString("Wave: " + currentWave, 10, 35);
        enemies.Draw(g2d);
        towers.Draw(g2d, enemies);
        player.Draw(g2d);
    }

    /**
     * Draw the game to the screen.
     * 
     * @param g2d Graphics2D
     * @param mousePosition current mouse position.
     */
}