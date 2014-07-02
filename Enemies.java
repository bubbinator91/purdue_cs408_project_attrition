import java.awt.Color;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.geom.*;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class Enemies {
	private static final float LEN = 10;

	//private final char[] DIRECTIONS = new char[] {'E','N','E','S','E','N','E','S','E','S','W','S','E','N','W','N','E','N','E','S','E','S','E','N','E'};

	// ArrayList containing all enemies currently in play.
	private ArrayList<Enemy> enemies;

	private String nextMove;
	private int lastGridX, lastGridY;

	// These arrays store the image information for the sprites. 0 = left; 1 = right, 2 = up, 3 = down;
	private BufferedImage[][] enemyImages;

	public Enemies() {
		Initialize();
		LoadContent();
	}

	private void Initialize() {
		enemies = new ArrayList<Enemy>();
		enemyImages = new BufferedImage[3][4];
	}

	private void LoadContent() {
		BufferedImage e1, e2, e3;
		try {
			e1 = ImageIO.read(new File("resources/images/enemies/enemy_1.png"));
			e2 = ImageIO.read(new File("resources/images/enemies/enemy_2.png"));
			e3 = ImageIO.read(new File("resources/images/enemies/enemy_3.png"));
			enemyImages[0][0] = e1.getSubimage(0, 0, 10, 10);
			enemyImages[0][1] = e1.getSubimage(10, 0, 10, 10);
			enemyImages[0][2] = e1.getSubimage(20, 0, 10, 10);
			enemyImages[0][3] = e1.getSubimage(30, 0, 10, 10);
			enemyImages[1][0] = e2.getSubimage(0, 0, 10, 10);
			enemyImages[1][1] = e2.getSubimage(10, 0, 10, 10);
			enemyImages[1][2] = e2.getSubimage(20, 0, 10, 10);
			enemyImages[1][3] = e2.getSubimage(30, 0, 10, 10);
			enemyImages[2][0] = e3.getSubimage(0, 0, 10, 10);
			enemyImages[2][1] = e3.getSubimage(10, 0, 10, 10);
			enemyImages[2][2] = e3.getSubimage(20, 0, 10, 10);
			enemyImages[2][3] = e3.getSubimage(30, 0, 10, 10);
		} catch (IOException e) {
			System.out.println("Data for sprites could not be loaded.");
			System.exit(1);
		}
	}

	public void Draw(Graphics2D g2d) {
		if (enemies.size() == 0)
			return;
		// Render enemies.
		for (Enemy e : enemies) {
			e.draw(g2d);
		}
	}

	public void removeEnemy(int i) {
		enemies.remove(i);
		Game.score = Game.score + 10;
	}

	public void dealDamage(int i, int j, int k) {
		if (enemies.size() == 0)
			return;
		Enemy e = null; 
		try{
		 e = enemies.get(i);
		} catch (IndexOutOfBoundsException r) {
			
		}
		System.out.print("Enemy health before: " + e.enemyHealth);
		e.enemyHealth = e.enemyHealth - j;
		System.out.println(" and after: " + e.enemyHealth);
		if (k == 2)
			e.speed = e.speed/2;
		if (e.enemyHealth <= 0) {
			Game.score = Game.score + e.value;
			enemies.remove(i);
		}
	}

	public int getSize() {
		return enemies.size();
	}

	public int getX(int i) {
		return (int)((float)enemies.get(i).x / (float)Window.UNIT_LEN);
	}

	public int getY(int i) {
		return (int)((float)enemies.get(i).y / (float)Window.UNIT_LEN);
	}

	public Enemy getEnemy(int i) {
		if (enemies.size() <= i)
			return null;
		else
			return enemies.get(i);
	}

	public String nextMove(int gridX, int gridY, Map map, Enemy e) {
		if (e.lastDirectionChangeGridX == gridX && e.lastDirectionChangeGridY == gridY) 
			return e.currentMove;

		//find the next move
		if ((map.GetTile(gridX + 1, gridY) == Map.TileType.PATH 
				|| map.GetTile(gridX + 1, gridY) == Map.TileType.SACRED_LAND) 
				&& !e.currentMove.equals("EAST")) {
			e.lastDirectionChangeGridX = gridX;
			e.lastDirectionChangeGridY = gridY;
			return "WEST";
		} else if ((map.GetTile(gridX, gridY - 1) == Map.TileType.PATH 
				|| map.GetTile(gridX, gridY - 1) == Map.TileType.SACRED_LAND) 
				&& !e.currentMove.equals("SOUTH")) {
			e.lastDirectionChangeGridX = gridX;
			e.lastDirectionChangeGridY = gridY;
			return "NORTH";
		} else if ((map.GetTile(gridX - 1, gridY) == Map.TileType.PATH 
				|| map.GetTile(gridX - 1, gridY) == Map.TileType.SACRED_LAND) 
				&& !e.currentMove.equals("WEST")) {
			e.lastDirectionChangeGridX = gridX;
			e.lastDirectionChangeGridY = gridY;
			return "EAST";
		} else if ((map.GetTile(gridX, gridY + 1) == Map.TileType.PATH 
				|| map.GetTile(gridX, gridY + 1) == Map.TileType.SACRED_LAND) 
				&& !e.currentMove.equals("NORTH")) {
			e.lastDirectionChangeGridX = gridX;
			e.lastDirectionChangeGridY = gridY;
			return "SOUTH";
		}
		return "ERROR";
	}

	public void Update(Map map) {
		// Check every enemy's next location. Done this way in order to modify ArrayList in place in a simple way.
		for (int i = 0;i < enemies.size();i++) {
			Enemy e = enemies.get(i);

			// Get the current grid the enemy is in.
			int gridX = getX(i);
			int gridY = getY(i);

			// Check if the enemy has reached the sacred land.
			if (map.GetTile(gridX, gridY) == Map.TileType.SACRED_LAND) {
				Game.health = Game.health - e.enemyHealth;
				enemies.remove(i);
				continue;
			}

			if (e.currentMove.equals("WEST")) e.x += e.speed;
			else if (e.currentMove.equals("NORTH")) e.y -= e.speed;
			else if (e.currentMove.equals("EAST")) e.x -= e.speed;
			else if (e.currentMove.equals("SOUTH")) e.y += e.speed;

			final float UP_LEFT_OFFSET = 10;
			final float DOWN_RIGHT_OFFSET = Window.UNIT_LEN - UP_LEFT_OFFSET;
			final float HALF_LEN = LEN / 2; 
			float xOffset = (e.x + HALF_LEN) % Window.UNIT_LEN;
			float yOffset = (e.y + HALF_LEN) % Window.UNIT_LEN;
			if (xOffset >= 20 && xOffset <= DOWN_RIGHT_OFFSET && yOffset >= 20 && yOffset <= DOWN_RIGHT_OFFSET)
				e.currentMove = nextMove(gridX, gridY, map, e);
		}
	}

	// Spawn an enemy at the start location.
	public boolean spawnEnemy(int i) {
		try {
			enemies.add(new Enemy(i, enemyImages[i]));
			System.out.println("Enemy spawned");
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public class Enemy {
		public float x = 0, y = 400;
		public String currentMove = "WEST";
		public float speed;
		public int enemyHealth, value;
		public int lastDirectionChangeGridX = 30, lastDirectionChangeGridY = 30;
		public BufferedImage[] image = new BufferedImage[4];

		public Enemy(int type, BufferedImage[] image) {
			switch (type) {
			// EASY 
			case 0:
				speed = 1;
				enemyHealth = 2;
				value = 2;
				this.image = image;
				break;
			case 1:
				speed = 1.5f;
				enemyHealth = 4;
				value = 4;
				this.image = image;
				break;
			case 2:
				speed = 1.2f;
				enemyHealth = 5;
				value = 5;
				this.image = image;
				break;
			default:
				break;
			}
		}

		public void draw(Graphics2D g2d) {
			float halfLen = Enemies.LEN / 2;
			if (currentMove.equals("WEST")) {
				g2d.drawImage(
					image[3],
					null,
					(int)(x - halfLen),
					(int)(y - halfLen)
				);
			} else if (currentMove.equals("EAST")) {
				g2d.drawImage(
					image[2],
					null,
					(int)(x - halfLen),
					(int)(y - halfLen)
				);
			} else if (currentMove.equals("NORTH")) {
				g2d.drawImage(
					image[0],
					null,
					(int)(x - halfLen),
					(int)(y - halfLen)
				);
			} else if (currentMove.equals("SOUTH")) {
				g2d.drawImage(
					image[1],
					null,
					(int)(x - halfLen),
					(int)(y - halfLen)
				);
			}
		}
	}
}
