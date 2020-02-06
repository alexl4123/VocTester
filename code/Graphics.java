package code;

import java.awt.Adjustable;
import java.awt.BorderLayout;
import java.awt.Desktop.Action;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.TextField;
import java.awt.Toolkit;

import javax.management.Query;
import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.awt.color.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter.DEFAULT;

import javafx.scene.paint.Color;

/**
 * 
 * @author Alexander Beiser
 * @version 0.21
 * 
 * Here is the asked question window
 *
 */
public class Graphics {

	private double screenWidth, screenHeight; 
	private JTextPane Question;
	private JTextField TableName,Answer;
	private JButton Library, BQuery, Final, NewTable;
	private JLabel selectLabel;
	private JPanel mainPanel;
	private JFrame mainFrame;
	private GameFlow GF;
	private Database DB;
	private JComboBox JCB;
	private SimpleAttributeSet attribs;
	private ArrayList<String> QuestionList, AnswerList, LeveList, DoneQuestionList, DoneAnswerList, DoneLeveList;
	private int RandomNumber, TotalVocs, VocsDone;
	private float windowWidth, windowHeight;
	private boolean AnswerCheck, CheckDone;
	private vocs vc;

	/**
	 * Constructor - calls all the inits
	 * @param GFOld
	 */
	public Graphics(GameFlow GFOld) {

		//----Main Panel-----
		initGeneral(GFOld);
		initButtonLabel();
		initTextFields();
		initComboBox();
		initPositions();
		actionListeners();
		vc = new vocs(this);


		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
		Answer.requestFocusInWindow();
	}

	/**
	 * General INIT
	 * For Database, GameFlow and mainFrame
	 * @param GFOld
	 */
	private void initGeneral(GameFlow GFOld) {
		DB = new Database();
		GF = GFOld;
		CheckDone = false;
		AnswerCheck = false;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.screenWidth = screenSize.getWidth();
		this.screenHeight = screenSize.getHeight();

		mainFrame = new JFrame("Voctester");
		mainFrame.setBounds((int)screenWidth/4,(int)screenHeight/4 , (int)screenWidth/4, (int)screenHeight/2);
		Dimension minSize = new Dimension((int)screenWidth/5, (int)screenHeight/3);
		mainFrame.setMinimumSize(minSize);

		mainPanel = new JPanel();
		mainPanel.setBackground(java.awt.Color.white);
		mainFrame.add(mainPanel);
		mainPanel.setLayout(null);

		

		QuestionList = DB.getQuestionList("Regular");
		AnswerList = DB.getAnswerList("Regular");
		LeveList = DB.getLevelList("Regular");
		
		int sizeOfArray = 0; 
		while( sizeOfArray < LeveList.size()) {
			System.out.println(sizeOfArray);
			if(Integer.parseInt(LeveList.get(sizeOfArray))>=5) {
				System.out.println(QuestionList.get(sizeOfArray));
				QuestionList.remove(sizeOfArray);
				AnswerList.remove(sizeOfArray);
				LeveList.remove(sizeOfArray);
				
			}else {
				sizeOfArray++;
			}
		}
		
		if(QuestionList.size()>0) {
			RandomNumber = GF.randNumSelect(QuestionList.size()-1);
		}
		DoneAnswerList = new ArrayList<String>();
		DoneLeveList = new ArrayList<String>();
		DoneQuestionList = new ArrayList<String>();

	}

	/**
	 *  Init for all the buttons and labels
	 */
	private void initButtonLabel() {
		Library = new JButton("Library");
		
		BQuery = new JButton("Testing");
		
		selectLabel = new JLabel("Select list");

		Final = new JButton("Check");
		NewTable = new JButton("New Table");
		if(LeveList.size()>0) {
			NewTable.setText("Cur. Level:"+LeveList.get(RandomNumber));
		}else {
			NewTable.setText("No Card selected");
		}
		TotalVocs = DoneAnswerList.size() + AnswerList.size();
		VocsDone = DoneAnswerList.size();

		NewTable.setEnabled(false);
	}

	/**
	 * Inits all the JTextFields
	 */
	private void initTextFields() {


		int TextWidth = mainFrame.getWidth()/2;
		int TextHeight = (mainFrame.getHeight()*4)/5;
		Dimension textSize = new Dimension(TextWidth, TextHeight);


		Question = new JTextPane();
		Question.setPreferredSize(textSize);
		Question.setEnabled(false);
		Question.setDisabledTextColor(java.awt.Color.BLACK);
		//Question.setHorizontalAlignment(JTextField.CENTER);
		Question.setBackground(java.awt.Color.lightGray);
		attribs = new SimpleAttributeSet();
		StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_CENTER);
		StyleConstants.setFontSize(attribs, 30);
		StyleConstants.setFontFamily(attribs, "Serif");
		Question.setParagraphAttributes(attribs,true);
		
	
		if(QuestionList.size()>0) {
			displayQuestion();
		}
		
		
	   
		Answer = new JTextField();
		Answer.setPreferredSize(textSize);
		Answer.setHorizontalAlignment(JTextField.CENTER);
		Answer.setFont(new Font("Arial",Font.ITALIC,25));
		clearAnswer();

		TableName = new JTextField("Table Name");
		TableName.setHorizontalAlignment(JTextField.LEFT);
		TableName.setEditable(false);
		TableName.setText(VocsDone+"/"+TotalVocs+"-Done");
	}

	/**
	 * Inits the JComboBox
	 */
	private void initComboBox() {
		JCB = new JComboBox();

		for(String TableName:DB.TableNames()) {
			JCB.addItem(TableName);
		}
		for(int i = 0; i < JCB.getItemCount(); i++) {
			if(JCB.getItemAt(i).equals("Regular")) {
				System.out.println("Found at:"+i);
				JCB.setSelectedIndex(i);
			}
		}


	}
	
	/**
	 * Inits all the positions for the Objects
	 * Eg.: The button position
	 * This happens with the relative/absolut coord. pos 
	 * The Button is positioned absolute on the mainPanel,
	 * but the size and coord is relatively altered (threw a correcture Factor),
	 * so the impression is, that the Button keeps it place
	 */
	private void initPositions() {

		windowHeight = mainFrame.getHeight();
		windowWidth = mainFrame.getWidth();
		
		BQuery.setBounds(corFactorWidth(50), corFactorHeight(25), corFactorWidth(150), corFactorHeight(50));
		Library.setBounds(corFactorWidth(250), corFactorHeight(25), corFactorWidth(150), corFactorHeight(50));
		
		JCB.setBounds(corFactorWidth(800), corFactorHeight(25), corFactorWidth(150), corFactorHeight(50));
		selectLabel.setBounds(corFactorWidth(650), corFactorHeight(25), corFactorWidth(150), corFactorHeight(50));
		
		 Question.setBounds(corFactorWidth(50), corFactorHeight(100), corFactorWidth(900), corFactorHeight(350));
		Answer.setBounds(corFactorWidth(50), corFactorHeight(500), corFactorWidth(900), corFactorHeight(350));
		
		Final.setBounds(corFactorWidth(800), corFactorHeight(875), corFactorWidth(150), corFactorHeight(50));
		
		NewTable.setBounds(corFactorWidth(250), corFactorHeight(875), corFactorWidth(200), corFactorHeight(50));
		TableName.setBounds(corFactorWidth(50), corFactorHeight(875), corFactorWidth(200), corFactorHeight(50));
		
		//Specify font size
		float x = windowHeight*windowWidth;
		float fffFontField = (float) (1.0/140000.0);
		float ffFontField = fffFontField*x;
		float fFontField = (float) (ffFontField+23.45);
		int fontSizeField = (int) fFontField;
		
		Answer.setFont(new Font("Arial", Font.ITALIC, fontSizeField));
		
		
		
		float fffFontButton = (float) (5.14*Math.pow(10, -6));
		float ffFontButton = fffFontButton*x;
		float fFontButton = (float) (ffFontButton+11.73);
		int fontSizeButton = (int) fFontButton;
		Font ButtonFont = new Font("Arial", Font.BOLD, fontSizeButton);
		Library.setFont(ButtonFont);
		Final.setFont(ButtonFont);
		selectLabel.setFont(ButtonFont);
		BQuery.setFont(ButtonFont);
		JCB.setFont(ButtonFont);
		TableName.setFont(ButtonFont);
		NewTable.setFont(ButtonFont);
		
		System.out.println(fontSizeButton);
		
		mainPanel.add(BQuery);
		mainPanel.add(Library);
		
		mainPanel.add(JCB);
		mainPanel.add(selectLabel);
		
		mainPanel.add(Question);
		mainPanel.add(Answer);
		
		mainPanel.add(Final);
		
		mainPanel.add(NewTable);
		mainPanel.add(TableName);
		
	}

	/**
	 * here are all the action listeners
	 */
	private void actionListeners() {

		mainFrame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				
				initPositions();
			}
		});
		
		ActionListener AL_BQuery = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Query");

				Question.setEnabled(false);
				Final.setText("Check");
				GF.setInput(false);
				TableName.setEditable(false);
				NewTable.setEnabled(false);
				clearAnswer();
				NewQuestionWithNewTable();
				BQuery.setBackground(java.awt.Color.LIGHT_GRAY);
				CheckDone = false;
				AnswerCheck = false;
				Answer.setEditable(true);
			}
		};
		BQuery.addActionListener(AL_BQuery);

		InputMap inputMap = Answer.getInputMap();
		inputMap.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), "foo");
		Answer.getActionMap().put("foo", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonFinal();

			}
		});

		Library.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Library");
				vc.setVisible(true);
				vc.selectList(JCB.getSelectedItem().toString());

			}
		});

		NewTable.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("New List");
				String Tablename = TableName.getText();
				try {
					DB.addTable(Tablename);

					JCB.removeAllItems();
					for(String TableName:DB.TableNames()) {
						JCB.addItem(TableName);
					}
				}catch(Exception ex) {
					JOptionPane.showConfirmDialog(new JFrame(), "Error, no special signs and/or spaces allowed!");
				}

			}
		});

		Final.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonFinal();
			}
		});

		JCB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Triggered");

				if(!GF.getInput()) {
					NewQuestionWithNewTable();
				}
				Object OJ = (Object) JCB.getSelectedItem();
				JCB.removeAllItems();
				for(String TableName:DB.TableNames()) {
					JCB.addItem(TableName);
				}
				JCB.setSelectedItem(OJ);
				vc.selectList(JCB.getSelectedItem().toString());
				
				CheckDone = false;
				AnswerCheck = false;
				Answer.setEditable(true);
				Answer.setText("");

			}
		});

	}

	/**
	 * here is the new Question displayed
	 */
	public void displayQuestion() {

		Question.setText("Question: " + QuestionList.get(RandomNumber));

	}

	/**
	 * The answer is clearde
	 */
	public void clearAnswer() {
		Answer.setText("");
		Answer.requestFocusInWindow();
	}

	/**
	 * A new question is displayed
	 */
	public void NewQuestionWithNewTable() {
		DoneAnswerList.clear();
		DoneLeveList.clear();
		DoneQuestionList.clear();

		QuestionList = DB.getQuestionList(JCB.getSelectedItem().toString());
		AnswerList = DB.getAnswerList(JCB.getSelectedItem().toString());
		LeveList = DB.getLevelList(JCB.getSelectedItem().toString());
		
		int sizeOfArray = 0; 
		while( sizeOfArray < LeveList.size()) {
			System.out.println(sizeOfArray);
			if(Integer.parseInt(LeveList.get(sizeOfArray))>=5) {
				System.out.println(QuestionList.get(sizeOfArray));
				QuestionList.remove(sizeOfArray);
				AnswerList.remove(sizeOfArray);
				LeveList.remove(sizeOfArray);
				
			}else {
				sizeOfArray++;
			}
		}

		VocsDone = DoneAnswerList.size();
		TotalVocs = AnswerList.size()+VocsDone;



		TableName.setText(VocsDone+"/"+TotalVocs+"-Done");
		if(LeveList.size() > 0) {
			RandomNumber = GF.randNumSelect(QuestionList.size()-1);
			displayQuestion();
			NewTable.setText("Cur. Level:"+LeveList.get(RandomNumber));
		}else {
			NewTable.setText("Cur. Level: ?");
			Question.setText("This list is empty, or all vocs are higher than level five!");
		}
	}

	/**
	 * The action, when the ,,final'' button is pressed
	 */
	public void ButtonFinal() {
		System.out.println("Final");

		System.out.println(Answer.getText());
		System.out.println("::");
		if(!AnswerCheck){
			Integer Level;
			if(AnswerList.size() > 0) {
				if(AnswerList.get(RandomNumber).equals(Answer.getText())) {
					System.out.println("You got it right");

					Level = Integer.parseInt(LeveList.get(RandomNumber))+1;
					DoneAnswerList.add(AnswerList.get(RandomNumber));
					DoneLeveList.add(Level.toString());
					DoneQuestionList.add(QuestionList.get(RandomNumber));

					DB.updateVoc(JCB.getSelectedItem().toString(), QuestionList.get(RandomNumber), AnswerList.get(RandomNumber), Level);

					System.out.println("New Level of:"+QuestionList.get(RandomNumber)+"::"+Level);
					
					AnswerList.remove(RandomNumber);
					LeveList.remove(RandomNumber);
					QuestionList.remove(RandomNumber);

					Answer.setText("Right! \n\r Press next");
					NewTable.setText("Cur. Level:"+Level.toString());

				}else {
					System.out.println("You got it wrong");
					Level = 0;
					LeveList.set(RandomNumber, Level.toString());
					DB.updateVoc(JCB.getSelectedItem().toString(), QuestionList.get(RandomNumber), AnswerList.get(RandomNumber), Level);
					NewTable.setText("Cur. Level:"+Level.toString());
					Answer.setText("Wrong! Correct answer:'"+AnswerList.get(RandomNumber).toString()+"' - Press next:");
				}



				TotalVocs = DoneAnswerList.size() + AnswerList.size();
				VocsDone = DoneAnswerList.size();
				TableName.setText(VocsDone+"/"+TotalVocs+"-Done");

				Final.setText("Next");
				AnswerCheck = true;
				Answer.setEditable(false);

				if(AnswerList.size()==0) {
					Answer.setText("You finished all, continue with sth. else!");
					System.out.println("You have got all right!");
					System.out.println("Continue with another library or stop!");
					CheckDone = true;
				}
			}
			
			vc.updateTable();

		}else if(AnswerCheck && !CheckDone) {
			System.out.println("Next");
			AnswerCheck = false;
			Answer.setEditable(true);
			Final.setText("Check");
			if(QuestionList.size()>0) {
				RandomNumber = GF.randNumSelect(QuestionList.size()-1);
				displayQuestion();
				NewTable.setText("Cur. Level:"+LeveList.get(RandomNumber));
			}
			clearAnswer();
		}
	}
	
	/**
	 * Calculates the correctur factor for the x coord
	 * @param percent
	 * @return
	 */
	private int corFactorWidth(float percent) {
		return (int) (percent*(windowWidth/1000));
	}

	/**
	 * calculates the correture factor for the y coord
	 * @param percent
	 * @return
	 */
	private int corFactorHeight(float percent) {
		int Factor = (int) (percent*((windowHeight-39)/(1000-39)));
		return Factor;
	}
	
	public void setSelectedList(String ListName) {
		for(int i = 0; i < JCB.getItemCount(); i++) {
			if(JCB.getItemAt(i).equals(ListName)) {
				System.out.println("Hit");
				JCB.setSelectedIndex(i);
				break;
			}
		}
	}

}
