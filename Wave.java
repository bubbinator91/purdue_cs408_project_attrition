public class Wave {
	private int currentWave; 
	private int enemyCount[];
	private int highestEnemy;
	private boolean lastWave;
	
	public Wave() {
		Initialize();
		LoadContent();
	}
	
	private void Initialize() {
		lastWave = false; 
		currentWave = 0; 
		highestEnemy = 0;
		enemyCount = new int[]{0, 0, 0};
	}
	
	public boolean isLastWave() {
		return lastWave; 
	}
	
	private void inWave() {
		if (currentWave == 1){
			// 14 easy enemies, thats it
			enemyCount[0]= 14;
			highestEnemy = 0;
		} else if (currentWave == 2) {
			enemyCount = new int[] {15, 5, 0};
			highestEnemy = 1;
		} else if (currentWave == 3) {
			enemyCount = new int[] {10, 15, 0};
			highestEnemy = 1;
		} else if (currentWave == 4){
			enemyCount = new int[] {5, 10, 5};
			highestEnemy = 2;
		} else {
			lastWave = true;
		}
	}
	
	public int popEnemy() {
		if (highestEnemy < 0)
			return -1;

		System.out.println("{" +enemyCount[0]+ "," +enemyCount[1]+ "," +enemyCount[2]+ "}");
		if (enemyCount[highestEnemy] > 0) {
			enemyCount[highestEnemy]--;
			return highestEnemy; 
		} else if (enemyCount[highestEnemy] == 0) {
			highestEnemy--;
			if (highestEnemy != -1) {
				enemyCount[highestEnemy]--;
				return highestEnemy;
			}
		}
		return -1;
	}
		
	public void Update(int wavenum) {
		currentWave = wavenum; 
		inWave();
	}
	
	private void LoadContent( ) {}
}
