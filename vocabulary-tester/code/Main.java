import java.util.ArrayList;

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
		System.out.println("Started");
		GameFlow GF = new GameFlow();
		GF.init();
		
		Graphics graphs = new Graphics(GF);
		
	}

}
