import java.awt.*;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

/**
 * 
 * @author Alexander Beiser
 * @version 0.21
 * 
 * Here the question window is asked
 *
 */
public class MainGraphicsFrame {

	private double screenWidth, screenHeight; 
	private JTextPane questionTextPane;
	private JTextField tableNameField, answerField;
	private JButton libraryButton, bQueryButton, finalButton, newTableButton;
	private JLabel selectLabel;
	private JPanel mainPanel;
	private JFrame mainFrame;
	private GameFlow gameFlow;
	private Database database;
	private JComboBox jComboBox;
	private SimpleAttributeSet simpleAttributeSet;
	private ArrayList<String> questionList, qnswerList, levelList, doneQuestionList, doneAnswerList, doneLeveList;
	private int randomNumber, totalVocs, vocsDone;
	private float windowWidth, windowHeight;
	private boolean answerCheck, checkDone;
	private VocsMenuFrame vocsMenuFrame;

	/**
	 * Constructor
	 * @param gameFlow
	 */
	public MainGraphicsFrame(GameFlow gameFlow, Database database) {

		this.database = database;
		this.gameFlow = gameFlow;
		checkDone = false;
		answerCheck = false;
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.screenWidth = screenSize.getWidth();
		this.screenHeight = screenSize.getHeight();
	}

	/**
	 * Inits the graphical user interface
	 * For Database, GameFlow and mainFrame
	 */
	public void initGraphicalUserInterface() {
		initMainFrame();
		initDatabaseUserInterface();

        vocsMenuFrame = new VocsMenuFrame(this);
        vocsMenuFrame.startVocsMenuFrame();

        initButtonLabel();
        initTextFields();
        initComboBox();
        actionListeners();

        initPositions();
		mainFrame.setVisible(true);
    }

	private void initDatabaseUserInterface() {
		questionList = database.getQuestionList("Regular");
		qnswerList = database.getAnswerList("Regular");
		levelList = database.getLevelList("Regular");

		int sizeOfArray = 0;
		while( sizeOfArray < levelList.size()) {
			if(Integer.parseInt(levelList.get(sizeOfArray))>=5) {
				questionList.remove(sizeOfArray);
				qnswerList.remove(sizeOfArray);
				levelList.remove(sizeOfArray);

			}else {
				sizeOfArray++;
			}
		}

		if(questionList.size()>0) {
			randomNumber = gameFlow.getRandomNumber(questionList.size()-1);
		}
		doneAnswerList = new ArrayList<String>();
		doneLeveList = new ArrayList<String>();
		doneQuestionList = new ArrayList<String>();
	}

	private void initMainFrame() {
		mainFrame = new JFrame("Voctester");

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setBounds((int)screenWidth/3,(int)screenHeight/4 , (int)screenWidth/3, (int)screenHeight/2);
		Dimension minSize = new Dimension((int)screenWidth/5, (int)screenHeight/3);
		mainFrame.setMinimumSize(minSize);

		mainPanel = new JPanel();
		mainPanel.setBackground(Color.white);
		mainFrame.add(mainPanel);
		mainPanel.setLayout(null);
	}

	/**
	 *  Init for all the buttons and labels
	 */
	private void initButtonLabel() {
		libraryButton = new JButton("Library");
		
		bQueryButton = new JButton("Testing");
		
		selectLabel = new JLabel("Select list");

		finalButton = new JButton("Check");
		newTableButton = new JButton("New Table");
		if(levelList.size()>0) {
			newTableButton.setText("Cur. Level:"+ levelList.get(randomNumber));
		}else {
			newTableButton.setText("No Card selected");
		}
		totalVocs = doneAnswerList.size() + qnswerList.size();
		vocsDone = doneAnswerList.size();

		newTableButton.setEnabled(false);
	}

	/**
	 * Inits all the JTextFields
	 */
	private void initTextFields() {


		int TextWidth = mainFrame.getWidth()/2;
		int TextHeight = (mainFrame.getHeight()*4)/5;
		Dimension textSize = new Dimension(TextWidth, TextHeight);


		questionTextPane = new JTextPane();
		questionTextPane.setPreferredSize(textSize);
		questionTextPane.setEnabled(false);
		questionTextPane.setDisabledTextColor(java.awt.Color.BLACK);

		questionTextPane.setBackground(java.awt.Color.lightGray);
		simpleAttributeSet = new SimpleAttributeSet();
		StyleConstants.setAlignment(simpleAttributeSet, StyleConstants.ALIGN_CENTER);
		StyleConstants.setFontSize(simpleAttributeSet, 30);
		StyleConstants.setFontFamily(simpleAttributeSet, "Serif");
		questionTextPane.setParagraphAttributes(simpleAttributeSet,true);

		if(questionList.size()>0) {
			displayQuestion();
		}else {
            questionTextPane.setText("This list is empty, or all vocs are higher than level five!");
        }

		answerField = new JTextField();
		answerField.setPreferredSize(textSize);
		answerField.setHorizontalAlignment(JTextField.CENTER);
		answerField.setFont(new Font("Arial",Font.ITALIC,25));
		clearAnswer();

		tableNameField = new JTextField("Table Name");
		tableNameField.setHorizontalAlignment(JTextField.LEFT);
		tableNameField.setEditable(false);
		tableNameField.setText(vocsDone +"/"+ totalVocs +"-Done");
	}

	/**
	 * Inits the JComboBox
	 */
	private void initComboBox() {
		jComboBox = new JComboBox();

		for(String TableName: database.TableNames()) {
			jComboBox.addItem(TableName);
		}
		for(int i = 0; i < jComboBox.getItemCount(); i++) {
			if(jComboBox.getItemAt(i).equals("Regular")) {
				jComboBox.setSelectedIndex(i);
			}
		}


	}
	
	/**
	 * Inits all the positions for the Objects
	 * Eg.: The button position
	 * This happens with the relative/absolut coord. pos 
	 * The Button is positioned absolute on the mainPanel,
	 * but the size and coord is relatively altered (through a correcture Factor),
	 * so it seems like the button keeps it's place
	 */
	private void initPositions() {

		windowHeight = mainFrame.getHeight();
		windowWidth = mainFrame.getWidth();
		
		bQueryButton.setBounds(corFactorWidth(50), corFactorHeight(25), corFactorWidth(150), corFactorHeight(50));
		libraryButton.setBounds(corFactorWidth(250), corFactorHeight(25), corFactorWidth(150), corFactorHeight(50));
		
		jComboBox.setBounds(corFactorWidth(800), corFactorHeight(25), corFactorWidth(150), corFactorHeight(50));
		selectLabel.setBounds(corFactorWidth(650), corFactorHeight(25), corFactorWidth(150), corFactorHeight(50));
		
		questionTextPane.setBounds(corFactorWidth(50), corFactorHeight(100), corFactorWidth(900), corFactorHeight(350));
		answerField.setBounds(corFactorWidth(50), corFactorHeight(500), corFactorWidth(900), corFactorHeight(350));
		
		finalButton.setBounds(corFactorWidth(800), corFactorHeight(875), corFactorWidth(150), corFactorHeight(50));
		
		newTableButton.setBounds(corFactorWidth(250), corFactorHeight(875), corFactorWidth(200), corFactorHeight(50));
		tableNameField.setBounds(corFactorWidth(50), corFactorHeight(875), corFactorWidth(200), corFactorHeight(50));
		
		//Specify font size
		float x = windowHeight*windowWidth;
		float fffFontField = (float) (1.0/140000.0);
		float ffFontField = fffFontField*x;
		float fFontField = (float) (ffFontField+23.45);
		int fontSizeField = (int) fFontField;
		
		answerField.setFont(new Font("Arial", Font.ITALIC, fontSizeField));

		float fffFontButton = (float) (5.14*Math.pow(10, -6));
		float ffFontButton = fffFontButton*x;
		float fFontButton = (float) (ffFontButton+11.73);
		int fontSizeButton = (int) fFontButton;
		Font ButtonFont = new Font("Arial", Font.BOLD, fontSizeButton);
		libraryButton.setFont(ButtonFont);
		finalButton.setFont(ButtonFont);
		selectLabel.setFont(ButtonFont);
		bQueryButton.setFont(ButtonFont);
		jComboBox.setFont(ButtonFont);
		tableNameField.setFont(ButtonFont);
		newTableButton.setFont(ButtonFont);

		mainPanel.add(bQueryButton);
		mainPanel.add(libraryButton);
		
		mainPanel.add(jComboBox);
		mainPanel.add(selectLabel);
		
		mainPanel.add(questionTextPane);
		mainPanel.add(answerField);
		
		mainPanel.add(finalButton);
		
		mainPanel.add(newTableButton);
		mainPanel.add(tableNameField);
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

				questionTextPane.setEnabled(false);
				finalButton.setText("Check");
				gameFlow.setInput(false);
				tableNameField.setEditable(false);
				newTableButton.setEnabled(false);
				clearAnswer();
				NewQuestionWithNewTable();
				bQueryButton.setBackground(java.awt.Color.LIGHT_GRAY);
				checkDone = false;
				answerCheck = false;
				answerField.setEditable(true);
			}
		};
		bQueryButton.addActionListener(AL_BQuery);

		InputMap inputMap = answerField.getInputMap();
		inputMap.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), "foo");
		answerField.getActionMap().put("foo", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonFinal();

			}
		});

		libraryButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				vocsMenuFrame.setVisible(true);
				vocsMenuFrame.selectList(jComboBox.getSelectedItem().toString());

			}
		});

		newTableButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String Tablename = tableNameField.getText();
				try {
					database.addTable(Tablename);

					jComboBox.removeAllItems();
					for(String TableName: database.TableNames()) {
						jComboBox.addItem(TableName);
					}
				}catch(Exception ex) {
					JOptionPane.showConfirmDialog(new JFrame(), "Error, no special signs and/or spaces allowed!");
				}

			}
		});

		finalButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ButtonFinal();
			}
		});

		jComboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if(!gameFlow.getInput()) {
					NewQuestionWithNewTable();
				}
				Object OJ = (Object) jComboBox.getSelectedItem();
				jComboBox.removeAllItems();
				for(String TableName: database.TableNames()) {
					jComboBox.addItem(TableName);
				}
				jComboBox.setSelectedItem(OJ);
				vocsMenuFrame.selectList(jComboBox.getSelectedItem().toString());
				
				checkDone = false;
				answerCheck = false;
				answerField.setEditable(true);
				answerField.setText("");

			}
		});

	}

	/**
	 * here is the new Question displayed
	 */
	public void displayQuestion() {

		questionTextPane.setText("Question: " + questionList.get(randomNumber));

	}

	/**
	 * The answer is clearde
	 */
	public void clearAnswer() {
		answerField.setText("");
		answerField.requestFocusInWindow();
	}

	/**
	 * A new question is displayed
	 */
	public void NewQuestionWithNewTable() {
		doneAnswerList.clear();
		doneLeveList.clear();
		doneQuestionList.clear();

		questionList = database.getQuestionList(jComboBox.getSelectedItem().toString());
		qnswerList = database.getAnswerList(jComboBox.getSelectedItem().toString());
		levelList = database.getLevelList(jComboBox.getSelectedItem().toString());
		
		int sizeOfArray = 0; 
		while( sizeOfArray < levelList.size()) {
			if(Integer.parseInt(levelList.get(sizeOfArray))>=5) {
				questionList.remove(sizeOfArray);
				qnswerList.remove(sizeOfArray);
				levelList.remove(sizeOfArray);
				
			}else {
				sizeOfArray++;
			}
		}

		vocsDone = doneAnswerList.size();
		totalVocs = qnswerList.size()+ vocsDone;



		tableNameField.setText(vocsDone +"/"+ totalVocs +"-Done");
		if(levelList.size() > 0) {
			randomNumber = gameFlow.getRandomNumber(questionList.size()-1);
			displayQuestion();
			newTableButton.setText("Cur. Level:"+ levelList.get(randomNumber));
		}else {
			newTableButton.setText("Cur. Level: ?");
			questionTextPane.setText("This list is empty, or all vocs are higher than level five!");
		}
	}

	/**
	 * The action, when the ,,final'' button is pressed
	 */
	public void ButtonFinal() {
		if(!answerCheck){
			Integer Level;
			if(qnswerList.size() > 0) {
				if(qnswerList.get(randomNumber).equals(answerField.getText())) {

					Level = Integer.parseInt(levelList.get(randomNumber))+1;
					doneAnswerList.add(qnswerList.get(randomNumber));
					doneLeveList.add(Level.toString());
					doneQuestionList.add(questionList.get(randomNumber));

					database.updateVoc(jComboBox.getSelectedItem().toString(), questionList.get(randomNumber), qnswerList.get(randomNumber), Level);
					
					qnswerList.remove(randomNumber);
					levelList.remove(randomNumber);
					questionList.remove(randomNumber);

					answerField.setText("Right! \n\r Press next");
					newTableButton.setText("Cur. Level:"+Level.toString());

				}else {
					Level = 0;
					levelList.set(randomNumber, Level.toString());
					database.updateVoc(jComboBox.getSelectedItem().toString(), questionList.get(randomNumber), qnswerList.get(randomNumber), Level);
					newTableButton.setText("Cur. Level:"+Level.toString());
					answerField.setText("Wrong! Correct answer:'"+ qnswerList.get(randomNumber).toString()+"' - Press next:");
				}



				totalVocs = doneAnswerList.size() + qnswerList.size();
				vocsDone = doneAnswerList.size();
				tableNameField.setText(vocsDone +"/"+ totalVocs +"-Done");

				finalButton.setText("Next");
				answerCheck = true;
				answerField.setEditable(false);

				if(qnswerList.size()==0) {
					answerField.setText("You finished all, continue with sth. else!");
					checkDone = true;
				}
			}
			
			vocsMenuFrame.updateTable();

		}else if(answerCheck && !checkDone) {
			answerCheck = false;
			answerField.setEditable(true);
			finalButton.setText("Check");
			if(questionList.size()>0) {
				randomNumber = gameFlow.getRandomNumber(questionList.size()-1);
				displayQuestion();
				newTableButton.setText("Cur. Level:"+ levelList.get(randomNumber));
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
		for(int i = 0; i < jComboBox.getItemCount(); i++) {
			if(jComboBox.getItemAt(i).equals(ListName)) {
				jComboBox.setSelectedIndex(i);
				break;
			}
		}
	}

}
