package code;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.lang.invoke.VolatileCallSite;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

/**
 * 
 * @author Alexander Beiser
 * @version 0.21
 * 
 * For the ,,library'' window
 * Here the user can add new vocs, delete vocs and edit vocs.
 *
 */
public class vocs{

	private int screenWidth, screenHeight;
	private float windowWidth, windowHeight;
	private JTable vocTable;
	private JFrame vocsFrame;
	private JLabel edit_string, select_string;
	private JButton Delete_Voc, New_Table, Finish, AddVoc, Delete_Table;
	private JComboBox Select_Table;
	private JScrollPane JSP;
	private JPanel VocPanel;
	private DefaultTableModel model;
	private ArrayList<String> QuestionList,AnswerList,LeveList;
	private Database DB;
	private boolean bTableUpdate;
	private Graphics GC;

	/**
	 * Default constructor
	 */
	public vocs(Graphics GCs) {
		GC = GCs;
		
		initGeneral();
		initLabelButton();
		initComboBox();
		initTable();

		actionListeners();

		vocsFrame.add(select_string);
		vocsFrame.add(Delete_Table);
		vocsFrame.add(edit_string);
		vocsFrame.add(VocPanel);
		vocsFrame.add(Delete_Voc);
		vocsFrame.add(Finish);
		vocsFrame.add(New_Table);
		vocsFrame.add(AddVoc);
		vocsFrame.add(Select_Table);
		vocsFrame.setLayout(null);
		vocsFrame.setVisible(false);
		vocsFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	}

	/**
	 * Inits general stuff like DataBase access
	 */
	private void initGeneral() {
		vocsFrame = new JFrame("Vocs");

		bTableUpdate = false;
		DB = new Database();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		this.screenWidth = (int) screenSize.getWidth();
		this.screenHeight = (int) screenSize.getHeight();

		vocsFrame = new JFrame("Voctester");
		vocsFrame.setBounds((int)screenWidth/4,(int)screenHeight/4 , 1000, 500);

		Dimension WindowWidth = vocsFrame.getSize();
		this.windowWidth = (float) WindowWidth.getWidth();
		this.windowHeight = (float) WindowWidth.getHeight();
		System.out.println(windowHeight);


		Dimension minSize = new Dimension((int)screenWidth/5, (int)screenHeight/3);
		vocsFrame.setMinimumSize(minSize);
	}

	/**
	 * inits the labels and buttons
	 */
	private void initLabelButton() {
		edit_string = new JLabel("Edit Vocs.");

		select_string = new JLabel("Select list:");

		Delete_Voc = new JButton("Delete Voc");

		New_Table = new JButton("New List");

		Delete_Table = new JButton("Delete List");

		Finish = new JButton("Finish");

		AddVoc = new JButton("Add Voc");
		//

	}

	/**
	 * inits the table
	 */
	private void initTable() {

		model = new DefaultTableModel();
		vocTable = new JTable(model);
		model.addColumn("Question");
		model.addColumn("Answer");
		model.addColumn("Level");
		//vocTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		vocTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		VocPanel = new JPanel();

		JSP = new JScrollPane(vocTable);


		VocPanel.setLayout(new BorderLayout());
		//VocPanel.add(vocTable.getTableHeader(), BorderLayout.NORTH);

		VocPanel.add(JSP);
		//VocPanel.add(vocTable, BorderLayout.CENTER);
		vocTable.setFillsViewportHeight(true);
		updateTable();
	}

	/**
	 * updates the table
	 */
	public void updateTable() {

		bTableUpdate = true;
		int rows = model.getRowCount();
		System.out.println(rows);
		for(int ii = 0; ii<rows; ii++) {
			System.out.println(model.getRowCount());
			model.removeRow(0);

		}
		try {
			QuestionList = DB.getQuestionList(Select_Table.getSelectedItem().toString());
			AnswerList = DB.getAnswerList(Select_Table.getSelectedItem().toString());
			LeveList = DB.getLevelList(Select_Table.getSelectedItem().toString());
		}catch(Exception ex) {
			System.out.println("No table present:"+ex.getMessage());
		}

		for(int i = 0; i < QuestionList.size(); i++) {

			model.addRow(new Object[] {QuestionList.get(i),AnswerList.get(i),LeveList.get(i)});
		}
		bTableUpdate = false;


	}

	/**
	 * inits the Combo
	 */
	private void initComboBox() {

		Select_Table = new JComboBox();

		for(String TableName:DB.TableNames()) {
			Select_Table.addItem(TableName);
		}

	}

	/**
	 * all the actionListeners
	 */
	private void actionListeners() {

		vocsFrame.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent componentEvent) {
				SpecifySize();
			}
		});

		AddVoc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				model.addRow(new Object[] {"","","0"});
				QuestionList.add("");
				AnswerList.add("");
				LeveList.add("0");

				
				FinalTableUpadate();
			}
		});

		vocTable.getModel().addTableModelListener(new TableModelListener() {

			@Override
			public void tableChanged(TableModelEvent e) {
				System.out.println("Changed:"+String.valueOf(e.getFirstRow())+"::"+String.valueOf(e.getColumn()));
				if(!bTableUpdate){
					if(e.getColumn() == 0) {
						QuestionList.set(e.getFirstRow(), model.getValueAt(e.getFirstRow(), e.getColumn()).toString());
						FinalTableUpadate();
					}else if(e.getColumn() == 1) {
						AnswerList.set(e.getFirstRow(), model.getValueAt(e.getFirstRow(), e.getColumn()).toString());
						FinalTableUpadate();
					}else if(e.getColumn() == 2) {
						LeveList.set(e.getFirstRow(), model.getValueAt(e.getFirstRow(), e.getColumn()).toString());
						FinalTableUpadate();
					}
				}



			}
		});

		Delete_Voc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					int row = vocTable.getSelectedRow();
					vocTable.selectAll();
					model.removeRow(row);
					QuestionList.remove(row);
					AnswerList.remove(row);
					LeveList.remove(row);
					vocTable.clearSelection();

					FinalTableUpadate();
				}catch(Exception ex) {
					System.out.println(ex.getMessage());
				}
			}
		});

		Select_Table.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateTable();
				GC.setSelectedList(Select_Table.getSelectedItem().toString());
			}
		});

		Finish.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				vocsFrame.setVisible(false);

			}
		});

		New_Table.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				String Tablename;

				Tablename = JOptionPane.showInputDialog(vocsFrame, "What should the new list be called?", null);
				try {
					if(!Tablename.equals(null)) {
						DB.addTable(Tablename);

						Select_Table.removeAllItems();
						for(String TableName:DB.TableNames()) {
							Select_Table.addItem(TableName);
						}

					}
				}catch(Exception ex) {
					System.out.println(ex.getMessage());
				}


			}
		});

		Delete_Table.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				int UserInput = JOptionPane.showOptionDialog(vocsFrame, "Do you really want to delete this list?", "Delete list", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
				if(UserInput == 0) {
					try {
						DB.deleteTable(Select_Table.getSelectedItem().toString());
						Select_Table.removeAllItems();
						for(String TableName:DB.TableNames()) {
							Select_Table.addItem(TableName);
						}
					}catch(Exception ex) {
						System.out.println("No Item found: "+ex.getMessage());
					}
				}
			}
		});

		InputMap inputMap = vocTable.getInputMap();
		inputMap.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_ENTER), "enter");
		inputMap.put(KeyStroke.getKeyStroke((char) KeyEvent.VK_TAB), "tab");

		vocTable.getActionMap().put("tab", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {


				if((vocTable.getSelectedColumn()==2) && (vocTable.getSelectedRow()==(vocTable.getRowCount()-1))){
					model.addRow(new Object[] {"","","0"});
					QuestionList.add("");
					AnswerList.add("");
					LeveList.add("0");

					updateDB();
				}

			}
		});

		vocTable.getActionMap().put("enter", new AbstractAction() {

			@Override
			public void actionPerformed(ActionEvent e) {


				if((vocTable.getSelectedRow()==0)){
					model.addRow(new Object[] {"","","0"});
					QuestionList.add("");
					AnswerList.add("");
					LeveList.add("0");

					updateDB();
					vocTable.setRowSelectionInterval(vocTable.getRowCount()-1, vocTable.getRowCount()-1);


				}

			}
		});

	}
	
	public void FinalTableUpadate() {
		updateDB();
		updateTable();
		GC.setSelectedList(Select_Table.getSelectedItem().toString());
	}

	/**
	 * specifies the size of the components and position.
	 * Eg.: The button position
	 * This happens with the relative/absolut coord. pos 
	 * The Button is positioned absolute on the mainPanel,
	 * but the size and coord is relatively altered (threw a correcture Factor),
	 * so the impression is, that the Button keeps it place
	 */
	public void SpecifySize() {
		this.windowHeight = vocsFrame.getHeight();
		this.windowWidth = vocsFrame.getWidth();

		edit_string.setBounds(corFactorWidth(250), corFactorHeight(25), corFactorWidth(400), corFactorHeight(50));
		select_string.setBounds(corFactorWidth(700), corFactorHeight(10), corFactorWidth(300), corFactorHeight(50));

		Delete_Voc.setBounds(corFactorWidth(50), corFactorHeight(900), corFactorWidth(150), corFactorHeight(50));
		AddVoc.setBounds(corFactorWidth(280), corFactorHeight(900), corFactorWidth(150), corFactorHeight(50));
		Finish.setBounds(corFactorWidth(800), corFactorHeight(900) , corFactorWidth(150), corFactorHeight(50));

		New_Table.setBounds(corFactorWidth(500), corFactorHeight(0), corFactorWidth(200), corFactorHeight(50));
		Delete_Table.setBounds(corFactorWidth(500), corFactorHeight(50), corFactorWidth(200), corFactorHeight(50));

		JSP.setBounds(corFactorWidth(50),corFactorHeight(0), corFactorWidth(900), corFactorHeight(800));	
		VocPanel.setBounds(corFactorWidth(0),corFactorHeight(100), corFactorWidth(1000), corFactorHeight(800));

		Select_Table.setBounds(corFactorWidth(700),corFactorHeight(50), corFactorWidth(250), corFactorHeight(50));

		float x = windowHeight*windowWidth;
		float fffFontButton = (float) (5.14*Math.pow(10, -6));
		float ffFontButton = fffFontButton*x;
		float fFontButton = (float) (ffFontButton+11.73);
		int fontSizeButton = (int) fFontButton;
		Font ButtonFont = new Font("Arial", Font.BOLD, fontSizeButton);

		select_string.setFont(ButtonFont);

		Delete_Voc.setFont(ButtonFont);
		AddVoc.setFont(ButtonFont);
		Finish.setFont(ButtonFont);

		New_Table.setFont(ButtonFont);
		Delete_Table.setFont(ButtonFont);

		VocPanel.setFont(ButtonFont);
		JSP.setFont(ButtonFont);

		Select_Table.setFont(ButtonFont);

		float fffFontField = (float) (1.0/140000.0);
		float ffFontField = fffFontField*x;
		float fFontField = (float) (ffFontField+23.45);
		int fontSizeField = (int) fFontField;
		Font fontField = new Font("Arial", Font.BOLD, fontSizeField);

		edit_string.setFont(fontField);

		//Otherwise it won't resize properly
		System.out.println("New Row");
		model.addRow(new Object[] {"","",""});
		System.out.println("Row Count:"+model.getRowCount());
		model.removeRow(model.getRowCount()-1);

	}

	/**
	 * updates the DataBase
	 */
	private void updateDB() {

		for(int i = 0; i < AnswerList.size(); i++) {
			try {
				int x = Integer.parseInt(LeveList.get(i));
				if(x > 9) {
					LeveList.set(i, "0");
					JOptionPane.showMessageDialog(new JFrame(), "Error, only single digit numbers allowed!","ERROR", JOptionPane.ERROR_MESSAGE);
				}
			}catch(Exception ex)
			{
				JOptionPane.showMessageDialog(new JFrame(), "Error, only single digit numbers allowed!","ERROR", JOptionPane.ERROR_MESSAGE);
				LeveList.set(i, "0");
			}
		}


		DB.deleteTable(Select_Table.getSelectedItem().toString());
		DB.addTable(Select_Table.getSelectedItem().toString());

		for(int i = 0; i < AnswerList.size(); i++) {
			try {
				DB.insertIntoTable(Select_Table.getSelectedItem().toString(), QuestionList.get(i), AnswerList.get(i) , Integer.parseInt(LeveList.get(i)) );
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * calculates the correcture factor
	 * @param percent
	 * @return
	 */
	private int corFactorWidth(float percent) {
		return (int) (percent*(windowWidth/1000));
	}

	/**
	 * calculates the correcture factor
	 * @param percent
	 * @return
	 */
	private int corFactorHeight(float percent) {
		int Factor = (int) (percent*((windowHeight-39)/(1000-39)));
		return Factor;
	}

	/**
	 * sets the visibility of the frame
	 * @param visibility
	 */
	public void setVisible(boolean visibility) {
		vocsFrame.setVisible(visibility);
	}

	/**
	 * Selects the list selected in the other window for better user experience
	 * @param ListName
	 */
	public void selectList(String ListName) {

		for(int i = 0; i < Select_Table.getItemCount(); i++) {
			if(Select_Table.getItemAt(i).equals(ListName)) {
				System.out.println("Hit");
				Select_Table.setSelectedIndex(i);
				break;
			}
		}
	}


}
