import java.awt.Color;
import java.util.ArrayList;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;

import javax.imageio.ImageIO;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class Towers {
	// ArrayList that holds the currect towers on the map
	private ArrayList<Tower> towers;

	// Stores the last state of Buttons on the mouse
	private boolean[] lastMouseButtonState;

	// Stores the last state of up, down, right, and left buttons on the keyboard
	private boolean[] lastKeyButtonState;

	// Variable to determine whether a tower is in movement mode
	private boolean towerIsSelected;

	// Variable to hold tower images. Only one tower now.
	private BufferedImage tower1;
	private BufferedImage tower2;
	private BufferedImage tower1up;
	private BufferedImage tower2up;
	private BufferedImage selectedTower1;

	public Towers() {
		Initialize();
		LoadContent();
	}

	private void Initialize() {
		towers = new ArrayList<Tower>();
		lastMouseButtonState = new boolean[3];
		lastKeyButtonState = new boolean[4];
		towerIsSelected = false;
	}

	private void LoadContent() {
		try {
			tower1 = ImageIO.read(new File("resources/images/towers/tower_1.png"));
			tower2 = ImageIO.read(new File("resources/images/towers/icetower_1.png"));
			tower1up = ImageIO.read(new File("resources/images/towers/tower_1up.png"));
			tower2up = ImageIO.read(new File("resources/images/towers/icetower_1up.png"));
			selectedTower1 = ImageIO.read(new File("resources/images/towers/tower_1.png"));
			for (int i = 0;i < Window.UNIT_LEN;i++) {
				for (int j = 0;j < Window.UNIT_LEN;j++) {
					Color a = new Color(selectedTower1.getRGB(i, j));
					Color b = new Color (a.getRed(), a.getBlue(), a.getRed());
					selectedTower1.setRGB(i, j, b.getRGB());
				}
			}
		} catch (IOException e) {
			System.out.println("Data for towers could not be loaded.");
			System.exit(1);
		}
	}

	public void Draw(Graphics2D g2d, Enemies enemies) {
		for (Tower tower : towers) {
			tower.Draw(g2d, enemies);
		}
	}

	public void Update(long gameTime, Map map, Enemies enemies, Point mousePosition) {
        double x = mousePosition.getX();
        double y = mousePosition.getY();

		/*
		* This block of code determines what to do with the mouse clicks. If the main mouse button
		* is clicked, the game either tries to add a tower, select a tower for movement, or deselect
		* a tower. If the right button is clicked, and the game is not in tower movement mode, it
		* will try to remove the tower at the click.
		*/
		if (Canvas.mouseButtonState(MouseEvent.BUTTON1) && !lastMouseButtonState[0] && ((x <= 800) && (y <= 690))) {
			int gridX = (int)((float)mousePosition.getX() / (float)Window.UNIT_LEN);
			int gridY = (int)((float)mousePosition.getY() / (float)Window.UNIT_LEN);
			System.out.println("Mouse clicked in grid X=" + gridX + " x Y=" + gridY);
			if ((map.GetTile(gridX, gridY) == Map.TileType.OFF_PATH) && (towerIsSelected == false) && (Game.score >= 100)) {
				map.SetTile(gridX, gridY, Map.TileType.TOWER);
				addTower(gridX, gridY, 0);
				Game.score = Game.score - 100;
			} else if (map.GetTile(gridX, gridY) == Map.TileType.TOWER) {
				for (int i = 0;i < towers.size();i++) {
					Tower t = towers.get(i);
					if ((t.gridX == gridX) && (t.gridY == gridY)) {
						t.isSelected = !t.isSelected;
						towers.set(i, t);
						towerIsSelected = !towerIsSelected;
						if (t.isSelected)
							System.out.println("Tower is selected");
						else
							System.out.println("Tower is deselected");
						break;
					}
				}
			}
		} else if (Canvas.mouseButtonState(MouseEvent.BUTTON2) && !lastMouseButtonState[1] && (towerIsSelected == false) && ((x <= 800) && (y <= 690))) {
			int gridX = (int)((float)mousePosition.getX() / (float)Window.UNIT_LEN);
			int gridY = (int)((float)mousePosition.getY() / (float)Window.UNIT_LEN);
			System.out.println("Mouse middle clicked in grid X=" + gridX + " x Y=" + gridY);
			if (map.GetTile(gridX, gridY) == Map.TileType.TOWER)
				upgradeTower(gridX, gridY);
		} else if (Canvas.mouseButtonState(MouseEvent.BUTTON3) && !lastMouseButtonState[2] && (towerIsSelected == false) && ((x <= 800) && (y <= 690))) {
			int gridX = (int)((float)mousePosition.getX() / (float)Window.UNIT_LEN);
			int gridY = (int)((float)mousePosition.getY() / (float)Window.UNIT_LEN);
			System.out.println("Mouse clicked in grid X=" + gridX + " x Y=" + gridY);
			if (map.GetTile(gridX, gridY) == Map.TileType.TOWER) {
				map.SetTile(gridX, gridY, Map.TileType.OFF_PATH);
				removeTower(gridX, gridY);
				Game.score = Game.score + 25;
			}
		}
		lastMouseButtonState[0] = Canvas.mouseButtonState(MouseEvent.BUTTON1);
		lastMouseButtonState[1] = Canvas.mouseButtonState(MouseEvent.BUTTON2);
		lastMouseButtonState[2] = Canvas.mouseButtonState(MouseEvent.BUTTON3);

		/*
		* This block of code determines what to do with the presses on the arrow keys. If no tower is
		* selected for movement, the keys do nothing. Otherwise, the select tower will move in the
		* direction of the pressed key.
		*/
		
		// MOVE NORTH
		if (Canvas.keyboardKeyState(KeyEvent.VK_UP) && towerIsSelected && !lastKeyButtonState[0]) {
			for (int i = 0;i < towers.size();i++) {
				Tower t = towers.get(i);
				if ( t.gridY != 0){
					if (t.isSelected) {
						if (map.GetTile(t.gridX, t.gridY - 1) == Map.TileType.OFF_PATH && t.gridY != 0) {
							map.SetTile(t.gridX, t.gridY, Map.TileType.OFF_PATH);
							t.gridY--;
							towers.set(i, t);
							map.SetTile(t.gridX, t.gridY, Map.TileType.TOWER);
						} else {
							for (int j = t.gridY;j >= 0;j--) {
								if (map.GetTile(t.gridX, j) == Map.TileType.OFF_PATH && t.gridY != 0 ) {
									map.SetTile(t.gridX, t.gridY, Map.TileType.OFF_PATH);
									t.gridY = j;
									towers.set(i, t);
									map.SetTile(t.gridX, t.gridY, Map.TileType.TOWER);
									break;
								}
							}
						}
					}
				}
			}
		
		} else if (Canvas.keyboardKeyState(KeyEvent.VK_DOWN) && towerIsSelected && !lastKeyButtonState[1]) {
			// MOVE SOUTH
			for (int i = 0;i < towers.size();i++) {
				Tower t = towers.get(i);
				if (t.isSelected) {
					System.out.println( "x = " + t.gridX + " y = " + t.gridY);
					if( t.gridY != 20){
						if (map.GetTile(t.gridX, t.gridY + 1) == Map.TileType.OFF_PATH) {
							map.SetTile(t.gridX, t.gridY, Map.TileType.OFF_PATH);
							t.gridY++;
							towers.set(i, t);
							map.SetTile(t.gridX, t.gridY, Map.TileType.TOWER);
						} else {
							for (int j = t.gridY;j < Window.GAME_HEIGHT_IN_UNITS;j++) {
								if (map.GetTile(t.gridX, j) == Map.TileType.OFF_PATH) {
									map.SetTile(t.gridX, t.gridY, Map.TileType.OFF_PATH);
									t.gridY = j;
									towers.set(i, t);
									map.SetTile(t.gridX, t.gridY, Map.TileType.TOWER);
									break;
								}
							}
						}
					}
				}
			}
		} else if (Canvas.keyboardKeyState(KeyEvent.VK_RIGHT) && towerIsSelected && !lastKeyButtonState[2]) {
			// MOVE EAST
			for (int i = 0;i < towers.size();i++) {
				Tower t = towers.get(i);
				if( t.gridX != 31){
					if (t.isSelected) {
						if (map.GetTile(t.gridX + 1, t.gridY) == Map.TileType.OFF_PATH) {
							map.SetTile(t.gridX, t.gridY, Map.TileType.OFF_PATH);
							t.gridX++;
							towers.set(i, t);
							map.SetTile(t.gridX, t.gridY, Map.TileType.TOWER);
						} else {
							for (int j = t.gridX;j < Window.GAME_WIDTH_IN_UNITS;j++) {
								if (map.GetTile(j, t.gridY) == Map.TileType.OFF_PATH) {
									map.SetTile(t.gridX, t.gridY, Map.TileType.OFF_PATH);
									t.gridX = j;
									towers.set(i, t);
									map.SetTile(t.gridX, t.gridY, Map.TileType.TOWER);
									break;
								}
							}
						}
					}
				}
			}
		} else if (Canvas.keyboardKeyState(KeyEvent.VK_LEFT) && towerIsSelected && !lastKeyButtonState[3]) {
			// MOVE WEST
			for (int i = 0;i < towers.size();i++) {
				Tower t = towers.get(i);
				if(t.gridX != 0){
					if (t.isSelected) {
						if (map.GetTile(t.gridX - 1, t.gridY) == Map.TileType.OFF_PATH) {
							map.SetTile(t.gridX, t.gridY, Map.TileType.OFF_PATH);
							t.gridX--;
							towers.set(i, t);
							map.SetTile(t.gridX, t.gridY, Map.TileType.TOWER);
						} else {
							for (int j = t.gridX;j >= 0;j--) {
								if (map.GetTile(j, t.gridY) == Map.TileType.OFF_PATH) {
									map.SetTile(t.gridX, t.gridY, Map.TileType.OFF_PATH);
									t.gridX = j;
									towers.set(i, t);
									map.SetTile(t.gridX, t.gridY, Map.TileType.TOWER);
									break;
								}
							}
						}
					}
				}
			}
		}
		lastKeyButtonState[0] = Canvas.keyboardKeyState(KeyEvent.VK_UP);
		lastKeyButtonState[1] = Canvas.keyboardKeyState(KeyEvent.VK_DOWN);
		lastKeyButtonState[2] = Canvas.keyboardKeyState(KeyEvent.VK_RIGHT);
		lastKeyButtonState[3] = Canvas.keyboardKeyState(KeyEvent.VK_LEFT);

		// This nested for loop checks for distance between towers and enemies and destroys enemies within range.
		for (Tower tower : towers) {
			if (tower.enemyIndex >= 0) {
				if ((double)(gameTime - tower.enemyHitGameTime) / Framework.secInNanosec >= .5) {
					System.out.println("Tower is dealing " + tower.towerDamage + " damage to enemy.");
					enemies.dealDamage(tower.enemyIndex, tower.towerDamage, tower.type);
					tower.enemyIndex = -1;
				} else continue;
			}
			for (int j = 0;j < enemies.getSize();j++) {
				if (Math.sqrt(Math.pow(enemies.getX(j) - tower.gridX, 2) + Math.pow(enemies.getY(j) - tower.gridY, 2)) < 2) {
					tower.enemyIndex = j;
					tower.enemyHitGameTime = gameTime;
					break;
				}
			}
		}
	}

	public int getSize() {
		return towers.size();
	}

	public int getX(int i) {
		return towers.get(i).gridX;
	}

	public int getY(int i) {
		return towers.get(i).gridY;
	}

	public void addTower(int gridX, int gridY, int type) {
		System.out.println("Adding tower in grid X=" + gridX + " x Y=" + gridY);
		if (type == 0)
			towers.add(new Tower(gridX, gridY, tower1, selectedTower1, 0));
		else if(type == 1)
			towers.add(new Tower(gridX, gridY, tower2, selectedTower1, 1));
	}

	public void removeTower(int gridX, int gridY) {
		for (int i = 0;i < towers.size();i++) {
			Tower t = towers.get(i);

			if ((t.gridX == gridX) && (t.gridY == gridY)) {
				System.out.println("Removing tower in grid X=" + gridX + " x Y=" + gridY);
				towers.remove(i);
				break;
			}
		}
	}

	public void upgradeTower(int gridX, int gridY) {
		for (int i = 0;i < towers.size();i++) {
			Tower t = towers.get(i);

			if ((t.gridX == gridX) && (t.gridY == gridY) && (Game.score >= 25) && (t.isUpgraded == false)) {
				System.out.println("Upgrading tower in grid X=" + gridX + " x Y=" + gridY);
				Game.score = Game.score - 25;
				t.isUpgraded = true;
				t.image = tower1up;
				t.towerDamage++;
				break;
			}
		}
	}

	private class Tower {
		public int gridX, gridY;
		public int type;
		public int enemyIndex = -1;
		public int towerDamage = 1;
		public boolean isUpgraded;
		public long enemyHitGameTime;
		public boolean isSelected;
		public BufferedImage image;
		public BufferedImage selectedImage;

		public Tower(int gridX, int gridY, BufferedImage image, BufferedImage selectedImage, int type) {
			switch (type) {
			case 0:
				this.gridX = gridX;
				this.gridY = gridY;
				this.isSelected = false;
				this.isUpgraded = false;
				this.towerDamage = 1;
				this.image = image;
				this.selectedImage = selectedImage;
				break;
			case 1:
				this.gridX = gridX;
				this.gridY = gridY;
				this.isSelected = false;
				this.isUpgraded = false;
				this.towerDamage = 0;
				this.image = image;
				this.selectedImage = selectedImage;
				break;
			default:
				break;
			};
		}

		public void Draw(Graphics2D g2d, Enemies enemies) {
			// If the tower is selected, gray. Otherwise, black.
			if (isSelected) {
				g2d.drawImage(
					selectedImage,
					null,
					gridX * Window.UNIT_LEN,
					gridY * Window.UNIT_LEN
				);
			} else {
				g2d.drawImage(
					image,
					null,
					gridX * Window.UNIT_LEN,
					gridY * Window.UNIT_LEN
				);
			}

			if (Framework.gameState == Framework.GameState.PLAYING) {
				// Render tower lasers.
				g2d.setColor(Color.blue);
				if (enemyIndex >=0) {
					Enemies.Enemy enemy = enemies.getEnemy(enemyIndex);
					if (enemy != null) {
						g2d.drawLine(
							(int)((gridX + 0.5) * Window.UNIT_LEN),
							(int)((gridY + 0.13) * Window.UNIT_LEN),
							(int)enemy.x,
							(int)enemy.y
						);
					}
				}
			}
		}
	}
}
