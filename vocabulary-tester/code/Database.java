
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
	private Connection con;
	private Statement stmt;
	
	/**
	 * Inits the DB
	 */
	public Database() {
		System.out.println("DB initialization in progress");
		con = null;
		stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:library.db");
			stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS Regular " +
					"(QUESTION STRING NOT NULL, " + 
					" ANSWER STRING NOT NULL," +
					"LEVEL INT)";
			stmt.executeUpdate(sql);
			stmt.close();
			con.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("DB initialized successfully");
	}
	
	/**
	 * Adds a new Library
	 * @param TableName - Libraryname
	 */
	public void addTable(String TableName) {
		
		con = null;
		stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:library.db");
			stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS "+TableName+" " +
					"(QUESTION STRING NOT NULL, " + 
					" ANSWER STRING NOT NULL," +
					"LEVEL INT)";
			stmt.executeUpdate(sql);
			stmt.close();
			con.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			JOptionPane.showMessageDialog(new JFrame(), "Error, no special signs and/or spaces allowed!","ERROR", JOptionPane.ERROR_MESSAGE);
		}
		System.out.println("Created "+TableName+" successfully");
		
	}
	
	/**
	 * Inserts a voc into the db
	 * @param TableName
	 * @param Question
	 * @param Answer
	 * @param Level
	 */
	public void insertIntoTable(String TableName, String Question, String Answer, int Level) {
		con = null;
		stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:library.db");
			con.setAutoCommit(false);
			//System.out.println("Opened database successfully");

			stmt = con.createStatement();
			String sql = "INSERT INTO "+TableName+" (QUESTION,ANSWER,LEVEL) " +
					"VALUES ('"+Question+"', '"+Answer+"', "+Level+");"; 
			stmt.executeUpdate(sql);
			stmt.close();
			con.commit();
			con.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			JOptionPane.showMessageDialog(new JFrame(), "Error, no special signs allowed!","ERROR", JOptionPane.ERROR_MESSAGE);
		}
		System.out.println("Inserted successfully to db-Tablename:"+TableName+":Question:"+Question);
	}
	
	/**
	 * returns the QuestionList
	 * @param TableName
	 * @return
	 */
	public ArrayList<String> getQuestionList(String TableName) {
		con = null;
		stmt = null;
		
		ArrayList<String> QuestionList = new ArrayList<String>();
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:library.db");
			con.setAutoCommit(false);
			//System.out.println("Opened database successfully");

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT QUESTION FROM "+TableName+";" );
			
			
			while ( rs.next() ) {
				//System.out.println("ACCOUNT-SHOULD::"+rs.getString("ACCOUNTNUMBER")+"::ACCOUNT-IS::"+nb);
				QuestionList.add(rs.getString("QUESTION"));
			}
			rs.close();
			stmt.close();
			con.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		//System.out.println("Operation done successfully");
		
		return QuestionList;
	}
	
	/**
	 * Returns the AnswerList
	 * @param TableName
	 * @return
	 */
	public ArrayList<String> getAnswerList(String TableName) {
		con = null;
		stmt = null;
		
		ArrayList<String> AnswerList = new ArrayList<String>();
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:library.db");
			con.setAutoCommit(false);
			//System.out.println("Opened database successfully");

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT ANSWER FROM "+TableName+";" );
			
			
			while ( rs.next() ) {
				//System.out.println("ACCOUNT-SHOULD::"+rs.getString("ACCOUNTNUMBER")+"::ACCOUNT-IS::"+nb);
				AnswerList.add(rs.getString("ANSWER"));
			}
			rs.close();
			stmt.close();
			con.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		//System.out.println("Operation done successfully");
		
		return AnswerList;
	}
	
	/**
	 * returns the LeveList
	 * @param TableName
	 * @return
	 */
	public ArrayList<String> getLevelList(String TableName) {
		con = null;
		stmt = null;
		
		ArrayList<String> LevelList = new ArrayList<String>();
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:library.db");
			con.setAutoCommit(false);
			//System.out.println("Opened database successfully");

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT LEVEL FROM "+TableName+";" );
			
			
			while ( rs.next() ) {
				//System.out.println("ACCOUNT-SHOULD::"+rs.getString("ACCOUNTNUMBER")+"::ACCOUNT-IS::"+nb);
				LevelList.add(rs.getString("LEVEL"));
			}
			rs.close();
			stmt.close();
			con.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		//System.out.println("Operation done successfully");
		
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
			con = DriverManager.getConnection("jdbc:sqlite:library.db");
			con.setAutoCommit(false);
			//System.out.println("Opened database successfully");

			stmt = con.createStatement();
			stmt.execute("DROP TABLE IF EXISTS '"+TableName+"';");
			
			stmt.close();
			con.commit();
			con.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Deleted "+TableName+" successfully");
	}
	
	/**
	 * Returns all tablenames in the DB
	 * @return
	 */
	public ArrayList<String> TableNames(){
		con = null;
		stmt = null;
		
		ArrayList<String> TableNameList = new ArrayList<String>();
		
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:library.db");
			con.setAutoCommit(false);
			//System.out.println("Opened database successfully");

			stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery( "SELECT * FROM sqlite_master WHERE type='table'" );
			
			
			while ( rs.next() ) {
				//System.out.println("ACCOUNT-SHOULD::"+rs.getString("ACCOUNTNUMBER")+"::ACCOUNT-IS::"+nb);
				TableNameList.add(rs.getString(2));
			}
			rs.close();
			stmt.close();
			con.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		//System.out.println("Operation done successfully");
		
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
		con = null;
		stmt = null;

		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:library.db");
			con.setAutoCommit(false);
			//System.out.println("Opened database successfully");

			stmt = con.createStatement();
			String sql = "UPDATE "+TableName+" set LEVEL = "+Level+" where Question = '"+Question+"';";
			stmt.executeUpdate(sql);
			con.commit();

			stmt.close();
			con.close();
		} catch ( Exception e ) {
			System.err.println( e.getClass().getName() + ": " + e.getMessage() );
			System.exit(0);
		}
		System.out.println("Level of:"+Question+":written successfully to library.db");
	}
}
