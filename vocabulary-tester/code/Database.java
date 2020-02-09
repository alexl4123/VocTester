
import java.sql.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * 
 * @author Alexander Beiser
 * @version 0.21
 * 
 * Here the database access happens
 *
 */
public class Database {
	
	
	//Attributes
	private Connection connection;
	private Statement statement;
	
	/**
	 * Inits the DB
	 */
	public Database() {
		connection = null;
		statement = null;
	}

	public void createTableIfNotExists() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:library.db");
			statement = connection.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS Regular " +
					"(QUESTION STRING NOT NULL, " +
					" ANSWER STRING NOT NULL," +
					"LEVEL INT)";
			statement.executeUpdate(sql);
			statement.close();
			connection.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}

	/**
	 * Adds a new Library
	 * @param TableName - Libraryname
	 */
	public void addTable(String TableName) {
		
		connection = null;
		statement = null;

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:library.db");
			statement = connection.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS "+TableName+" " +
					"(QUESTION STRING NOT NULL, " + 
					" ANSWER STRING NOT NULL," +
					"LEVEL INT)";
			statement.executeUpdate(sql);
			statement.close();
			connection.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			JOptionPane.showMessageDialog(new JFrame(), "Error, no special signs and/or spaces allowed!","ERROR", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	/**
	 * Inserts a voc into the db
	 * @param TableName
	 * @param Question
	 * @param Answer
	 * @param Level
	 */
	public void insertIntoTable(String TableName, String Question, String Answer, int Level) {
		connection = null;
		statement = null;

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:library.db");
			connection.setAutoCommit(false);

			statement = connection.createStatement();
			String sql = "INSERT INTO "+TableName+" (QUESTION,ANSWER,LEVEL) " +
					"VALUES ('"+Question+"', '"+Answer+"', "+Level+");"; 
			statement.executeUpdate(sql);
			statement.close();
			connection.commit();
			connection.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			JOptionPane.showMessageDialog(new JFrame(), "Error, no special signs allowed!","ERROR", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * returns the QuestionList
	 * @param TableName
	 * @return
	 */
	public ArrayList<String> getQuestionList(String TableName) {
		connection = null;
		statement = null;
		
		ArrayList<String> QuestionList = new ArrayList<String>();
		
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:library.db");
			connection.setAutoCommit(false);

			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery( "SELECT QUESTION FROM "+TableName+";" );
			
			
			while ( rs.next() ) {
				QuestionList.add(rs.getString("QUESTION"));
			}
			rs.close();
			statement.close();
			connection.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		
		return QuestionList;
	}
	
	/**
	 * Returns the AnswerList
	 * @param TableName
	 * @return
	 */
	public ArrayList<String> getAnswerList(String TableName) {
		connection = null;
		statement = null;
		
		ArrayList<String> AnswerList = new ArrayList<String>();
		
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:library.db");
			connection.setAutoCommit(false);

			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery( "SELECT ANSWER FROM "+TableName+";" );
			
			
			while ( rs.next() ) {
				AnswerList.add(rs.getString("ANSWER"));
			}
			rs.close();
			statement.close();
			connection.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		
		return AnswerList;
	}
	
	/**
	 * returns the LeveList
	 * @param TableName
	 * @return
	 */
	public ArrayList<String> getLevelList(String TableName) {
		connection = null;
		statement = null;
		
		ArrayList<String> LevelList = new ArrayList<String>();
		
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:library.db");
			connection.setAutoCommit(false);

			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery( "SELECT LEVEL FROM "+TableName+";" );
			
			
			while ( rs.next() ) {
				LevelList.add(rs.getString("LEVEL"));
			}
			rs.close();
			statement.close();
			connection.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		
		return LevelList;
	}
	
	/**
	 * Cur. nothing
	 * @param TableName
	 */
	public void deleteItemFromDB(String TableName) {
		
	}
	
	/**
	 * Deletes a table
	 * @param TableName
	 */
	public void deleteTable(String TableName) {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:library.db");
			connection.setAutoCommit(false);

			statement = connection.createStatement();
			statement.execute("DROP TABLE IF EXISTS '"+TableName+"';");
			
			statement.close();
			connection.commit();
			connection.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}
	
	/**
	 * Returns all tablenames in the DB
	 * @return
	 */
	public ArrayList<String> TableNames(){
		connection = null;
		statement = null;
		
		ArrayList<String> TableNameList = new ArrayList<String>();
		
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:library.db");
			connection.setAutoCommit(false);

			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery( "SELECT * FROM sqlite_master WHERE type='table'" );
			
			
			while ( rs.next() ) {
				TableNameList.add(rs.getString(2));
			}
			rs.close();
			statement.close();
			connection.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		
		return TableNameList;
	}
	
	/**
	 * Updates a voc
	 * @param TableName
	 * @param Question
	 * @param Answer
	 * @param Level
	 */
	public void updateVoc(String TableName, String Question, String Answer, int Level) {
		connection = null;
		statement = null;

		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:library.db");
			connection.setAutoCommit(false);

			statement = connection.createStatement();
			String sql = "UPDATE "+TableName+" set LEVEL = "+Level+" where Question = '"+Question+"';";
			statement.executeUpdate(sql);
			connection.commit();

			statement.close();
			connection.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
	}
}
