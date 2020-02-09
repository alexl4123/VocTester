/**
 * 
 * @author Alexander Beiser
 * @version 0.21
 * 
 * Here is the main methode
 *
 */
public class Main {
	
	/**
	 * Main Methode
	 * @param args
	 */
	public static void main(String[] args) {
		GameFlow gameflow = new GameFlow();
		gameflow.init();
		Database database = new Database();
		database.createTableIfNotExists();
		
		MainGraphicsFrame mainGraphicsFrame = new MainGraphicsFrame(gameflow, database);
		mainGraphicsFrame.initGraphicalUserInterface();
	}

}
