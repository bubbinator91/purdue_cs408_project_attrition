import java.awt.Color;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.geom.*;
import java.awt.Rectangle;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class Player {
	// ArrayList that holds the currect projectiles the player has shot
	private ArrayList<Projectile> projectiles;

	// Array of size 2 that holds the x and y position of the player
	private int[] playerPos = new int[2];

	// Holds the last time a projectile was shot
	private long lastTimeShot;

	// Holds the direction the player is facing
	private String playerDirection;

    // Variable to hold player's image
    private BufferedImage image;
  
	public Player() {
		Initialize();
		LoadContent();
	}

	private void Initialize() {
		projectiles = new ArrayList<Projectile>();
		playerPos[0] = 1000;
		playerPos[1] = 400;
		lastTimeShot = System.nanoTime();
		playerDirection = "WEST";
	}

	private void LoadContent() {
        try {
            image = ImageIO.read(new File("resources/images/player_character.png"));
        } catch (IOException e) {
            System.out.println("Data for player could not be loaded.");
            System.exit(1);
        }
	}

	public void Draw(Graphics2D g2d) {
        //draw player
        if (playerDirection.equals("WEST")) {
            g2d.drawImage(
                image.getSubimage(20, 0, 10, 10),
                playerPos[0],
                playerPos[1],
                null
            );
        } else if (playerDirection.equals("EAST")) {
            g2d.drawImage(
                image.getSubimage(30, 0, 10, 10),
                playerPos[0],
                playerPos[1],
                null
            );
        } else if (playerDirection.equals("NORTH")) {
            g2d.drawImage(
                image.getSubimage(0, 0, 10, 10),
                playerPos[0],
                playerPos[1],
                null
            );
        } else if (playerDirection.equals("SOUTH")) {
            g2d.drawImage(
                image.getSubimage(10, 0, 10, 10),
                playerPos[0],
                playerPos[1],
                null
            );
        }

        // draw any projectiles
		g2d.setColor(Color.orange);
		for (Projectile p : projectiles) {
			g2d.fill(new Ellipse2D.Float(
				(float)(p.x - 5),
				(float)(p.y - 5),
				(float)10.0,
				(float)10.0
			));
		}
	}

	public void Update(Map map, Enemies enemies) {
		int gridX = (int)((float)playerPos[0] / (float)Window.UNIT_LEN);
		int gridY = (int)((float)playerPos[1] / (float)Window.UNIT_LEN);

        /*
         * This block of code determines what to do with the W, S, A, D, and SPACE keyboard clicks.
         * W = up, S = down, D = right, and A = left. When a key is pressed, the character will move
         * in the direction the key represents. If the SPACE bar is pressed, a projectile if fired
         * in the direction the character is facing.
         */
		if (Canvas.keyboardKeyState(KeyEvent.VK_W)) {
			playerDirection = "NORTH";
			if (gridY != 0) {
				if ((map.GetTile(gridX, gridY - 1) == Map.TileType.PATH) || (map.GetTile(gridX, gridY - 1) == Map.TileType.SACRED_LAND))
            		playerPos[1]--;
            	else {
            		if ((playerPos[1] % (float)Window.UNIT_LEN) > 0.0)
            			playerPos[1]--;
            	}
            } else {
            	if ((playerPos[1] % (float)Window.UNIT_LEN) > 0.0)
            			playerPos[1]--;
            }
        } else if (Canvas.keyboardKeyState(KeyEvent.VK_S)) {
        	playerDirection = "SOUTH";
        	if (gridY < (Window.GAME_HEIGHT_IN_UNITS - 1)) {
        		if ((map.GetTile(gridX, gridY + 1) == Map.TileType.PATH) || (map.GetTile(gridX, gridY + 1) == Map.TileType.SACRED_LAND))
            		playerPos[1]++;
            	else {
            		if ((playerPos[1] % (float)Window.UNIT_LEN) < (float)(Window.UNIT_LEN - 1))
            			playerPos[1]++;
            	}
        	} else {
        		if ((playerPos[1] % (float)Window.UNIT_LEN) < (float)(Window.UNIT_LEN - 1))
            			playerPos[1]++;
        	}
        } else if (Canvas.keyboardKeyState(KeyEvent.VK_D)) {
        	playerDirection = "EAST";
        	if (gridX < (Window.GAME_WIDTH_IN_UNITS - 1)) {
        		if ((map.GetTile(gridX + 1, gridY) == Map.TileType.PATH) || (map.GetTile(gridX + 1, gridY) == Map.TileType.SACRED_LAND))
            		playerPos[0]++;
            	else {
            		if ((playerPos[0] % (float)Window.UNIT_LEN) < (float)(Window.UNIT_LEN - 1))
            			playerPos[0]++;
            	}
        	} else {
        		if ((playerPos[0] % (float)Window.UNIT_LEN) < (float)(Window.UNIT_LEN - 1))
            			playerPos[0]++;
        	}
        } else if (Canvas.keyboardKeyState(KeyEvent.VK_A)) {
        	playerDirection = "WEST";
        	if (gridX != 0) {
        		if ((map.GetTile(gridX - 1, gridY) == Map.TileType.PATH) || (map.GetTile(gridX - 1, gridY) == Map.TileType.SACRED_LAND))
            		playerPos[0]--;
            	else {
            		if ((playerPos[0] % (float)Window.UNIT_LEN) > 0.0)
            			playerPos[0]--;
            	}
        	} else {
        		if ((playerPos[0] % (float)Window.UNIT_LEN) > 0.0)
            			playerPos[0]--;
        	}
        } else if (Canvas.keyboardKeyState(KeyEvent.VK_SPACE)) {
        	if ((System.nanoTime() - lastTimeShot) >= (2 * Framework.secInNanosec)) {
        		Projectile p = new Projectile(playerPos[0], playerPos[1], playerDirection);
        		projectiles.add(p);
                Game.score -= 20;
        		lastTimeShot = System.nanoTime();
        	}
        }

        // this updates the positions of all projectiles
        for (int i = 0;i < projectiles.size();i++) {
        	Projectile p = projectiles.get(i);
        	if (p.directionTraveling.equals("NORTH")) {
        		p.y--;
        		if (p.y <= 0)
        			projectiles.remove(i);
        		else
        			projectiles.set(i, p);
        	} else if (p.directionTraveling.equals("SOUTH")) {
        		p.y++;
        		if (p.y >= (Window.GAME_HEIGHT_IN_UNITS * Window.UNIT_LEN))
        			projectiles.remove(i);
        		else
        			projectiles.set(i, p);
        	} else if (p.directionTraveling.equals("EAST")) {
        		p.x++;
        		if (p.x >= (Window.GAME_WIDTH_IN_UNITS * Window.UNIT_LEN))
        			projectiles.remove(i);
        		else
        			projectiles.set(i, p);
        	} else if (p.directionTraveling.equals("WEST")) {
        		p.x--;
        		if (p.x <= 0)
        			projectiles.remove(i);
        		else
        			projectiles.set(i, p);
        	}
        }

        /*
         * This block of code attempts to determine when a projectile comes into contact with an
         * enemy. In order to keep things kosher, if a projectile is found to come into contact
         * with an enemy, the projectile and enemy are removed from their lists. The loops need
         * to be restarted since the size() methods used in the creation of the loops have changed
         * their return value.
         */
    	boolean breakCompletely = false;
    	for (int i = 0;i < projectiles.size();i++) {
            Projectile p = projectiles.get(i);
    		for (int j = 0;j < enemies.getSize();j++) {
                Enemies.Enemy e = enemies.getEnemy(j);
                Rectangle proj = new Rectangle((p.x - 3), (p.y - 3), 7, 7);
                Rectangle enem = new Rectangle((int)(e.x - 1), (int)(e.y - 1), 3, 3);
                if (proj.intersects(enem)) {
                    enemies.removeEnemy(j);
                    projectiles.remove(i);
                    System.out.println("boom");
                    breakCompletely = true;
                    break;
                }
    		}
    		if (breakCompletely)
    			break;
		}
	}

	private class Projectile {
		public int x, y;
		public String directionTraveling;

		/*public Projectile() {
			this.x = 0;
			this.y = 0;
			directionTraveling = "EAST";
		}*/

		public Projectile(int x, int y, String directionTraveling) {
			this.x = x;
			this.y = y;
			this.directionTraveling = directionTraveling;
		}
	}
}
