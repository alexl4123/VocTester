import java.util.concurrent.ThreadLocalRandom;

/**
 * 
 * @author Alexander Beiser
 * @version 0.21
 * 
 * Here, the random number is calculated
 *
 */
public class GameFlow {
	private boolean Input;
	
	/**
	 * Inits the game flow
	 */
	public void init() {
		
		Input = false;
	}
	
	/**
	 * returns if the Input field has been clicked (old vers.)
	 * @return
	 */
	public boolean getInput() {
		return Input;
	}
	
	/**
	 * Sets the input field clicked
	 * @param newInput
	 */
	public void setInput(boolean newInput) {
		Input = newInput;
	}
	
	/**
	 * returns a Random number between 0 and maxNum
	 * @param maxNum
	 * @return
	 */
	public int randNumSelect(int maxNum) {
		
		int randomNum = ThreadLocalRandom.current().nextInt(0, maxNum+1);
		
		return randomNum;
	}
}
