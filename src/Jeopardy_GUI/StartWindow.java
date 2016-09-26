package Jeopardy_GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class StartWindow extends JFrame {
	private static final long serialVersionUID = 1;
	private JLabel title, description1, description2, description3, fileName;
	private JSlider teamSlider;
	private JButton fileBtn, startBtn, clearBtn, exitBtn;
	private JPanel teamSpace, team1, team2, team3, team4;
	private JTextField f1, f2, f3, f4;
	private JCheckBox quickMode;
	private JFileChooser fileChooser;
	private File selectedFile;
	private boolean inQuickMode = false; // not selected by default
	
	private String teamName[];
	
	public StartWindow(){
		super("Jeopardy");
		initializeComponents();
		createGUI();
		addEvents();
	}
	
	public void gameStart(){
		this.dispose();
		GameWindow gw = new GameWindow(selectedFile.getAbsolutePath(), teamSlider.getValue(), teamName
									, inQuickMode);
		gw.setVisible(true);
	}
	
	
	
	public void initializeComponents(){
		title = new JLabel("Welcome to Jeopardy!");
		description1 = new JLabel("Choose the game file, number of teams, and"
				+ " team names before starting the game.");
		description2 = new JLabel("Please choose a game file");
		description3 = new JLabel("Please choose the number of teams that will be playing on" 
				+ "the slider below.");
		fileName = new JLabel("");
		fileName.setVisible(false);
		fileBtn = new JButton("Choose file");
		startBtn = new JButton("Start Jeopardy");
		clearBtn = new JButton("Clear Choices");
		exitBtn = new JButton("Exit");
		teamSlider = new JSlider(1,4);
		teamSlider.setMajorTickSpacing(1);
		teamSlider.setPaintTicks(true);
		teamSlider.setPaintLabels(true);
		teamSlider.setValue(1);
		//teamSlider.setBackground(Color.DARK_GRAY);
		//teamSlider.setForeground(Color.white);
		quickMode = new JCheckBox("Quick Play");
		quickMode.setAlignmentX(JCheckBox.CENTER_ALIGNMENT);
		setBtnFont();
		setLblFont();
		fileChooser = new JFileChooser();
		
		f1 = new JTextField(); //f1.setBackground(Color.gray);
		f2 = new JTextField(); //f2.setBackground(Color.gray);

		f3 = new JTextField(); //f3.setBackground(Color.gray);
	
		f4 = new JTextField(); //f4.setBackground(Color.gray);
		
		
		
		
		
	}
	
	public void chooseFile(){
		fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
		
		FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");
		fileChooser.setFileFilter(filter);
		int result = fileChooser.showOpenDialog(this);
		if (result == JFileChooser.APPROVE_OPTION) {
		    selectedFile = fileChooser.getSelectedFile();
		    fileName.setText(selectedFile.getName());
		    fileName.setVisible(true);
		}
	}
	
	public void setLblFont(){
		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		description1.setHorizontalAlignment(JLabel.CENTER);
		description2.setHorizontalAlignment(JLabel.CENTER);
		description3.setHorizontalAlignment(JLabel.CENTER);
		
		title.setFont(new Font("TimesRoman", Font.BOLD, 24));
		
	}
	public void setBtnFont(){
		fileBtn.setBackground(Color.gray);
		startBtn.setBackground(Color.gray);
		clearBtn.setBackground(Color.gray);
		exitBtn.setBackground(Color.gray);
		fileBtn.setForeground(Color.white);
		startBtn.setForeground(Color.white);
		clearBtn.setForeground(Color.white);
		exitBtn.setForeground(Color.white);
		
	}
	
	public void createGUI(){
		setSize(1000,600);
		setLocation(800, 50);
		setLayout(new GridLayout(5,1));
		JPanel top = new JPanel();
		GridBagConstraints titleGbc = new GridBagConstraints();
		titleGbc.insets = new Insets(3,3,3,3);
		quickMode.setAlignmentX(JCheckBox.CENTER_ALIGNMENT);
		title.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		top.add(title, titleGbc); top.add(quickMode, titleGbc);
		//top.add(quickMode);
		//top.setBackground(Color.gray);
		add(top);
		JPanel fileChoose = new JPanel();
		fileChoose.add(description2);
		fileChoose.add(fileBtn);
		fileChoose.add(fileName);
		add(fileChoose);
		JPanel slider = new JPanel(new GridLayout(2,1));
		slider.add(description3);
		slider.add(teamSlider);
		slider.setBorder(new EmptyBorder(15,15,15,15));
		add(slider);
		teamSpaceCreator();
		add(teamSpace);
		JPanel bottom = new JPanel();
		bottom.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10,10,10,10);
		startBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);
		clearBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);
		exitBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);
		
		bottom.add(startBtn, gbc); bottom.add(clearBtn, gbc); 
		bottom.add(exitBtn, gbc); 
		add(bottom);
		
		
		
	}
	

	public void teamSpaceCreator(){
		teamSpace = new JPanel(new GridLayout(2,2));
		//teamSpace.setBackground(Color.darkGray);
		team1 = new JPanel(new GridLayout(2,1));
		team2 = new JPanel(new GridLayout(2,1));
		team3 = new JPanel(new GridLayout(2,1));
		team4 = new JPanel(new GridLayout(2,1));
		
		JLabel t1 = new JLabel("Please name Team 1");
		JLabel t2 = new JLabel("Please name Team 2");
		JLabel t3 = new JLabel("Please name Team 3");
		JLabel t4 = new JLabel("Please name Team 4");
		t1.setHorizontalAlignment(JLabel.CENTER); t2.setHorizontalAlignment(JLabel.CENTER);
		t3.setHorizontalAlignment(JLabel.CENTER); t4.setHorizontalAlignment(JLabel.CENTER);
		//t1.setBackground(Color.darkGray); t1.setForeground(Color.white);
		//t1.setOpaque(true);
		//t2.setBackground(Color.darkGray); t2.setForeground(Color.white);
		//t2.setOpaque(true);
		//t3.setBackground(Color.darkGray); t3.setForeground(Color.white);
		//t3.setOpaque(true);
		//t4.setBackground(Color.darkGray); t4.setForeground(Color.white);
		//t4.setOpaque(true);
		team1.setBorder(new EmptyBorder(15,10,0,10));
		team2.setBorder(new EmptyBorder(15,10,0,10));
		team3.setBorder(new EmptyBorder(15,10,0,10));
		team4.setBorder(new EmptyBorder(15,10,0,10));
		team1.add(t1); team1.add(f1);
		team2.add(t2); team2.add(f2);
		team3.add(t3); team3.add(f3);
		team4.add(t4); team4.add(f4);
		team2.setVisible(false);team3.setVisible(false); team4.setVisible(false);
		teamSpace.add(team1); teamSpace.add(team2);
		teamSpace.add(team3); teamSpace.add(team4);
	}
	
	
	public void addEvents(){
		
		addWindowListener(new WindowAdapter(){
			 @Override
			 public void windowClosing(WindowEvent we)
			 { 
			     String ObjButtons[] = {"Yes","No"};
			     int PromptResult = JOptionPane.showOptionDialog(null,"Are you sure you want to exit?","Exit",
			    		 			JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE,null,ObjButtons,ObjButtons[1]);
			     if(PromptResult==JOptionPane.YES_OPTION)
			     {
			            System.exit(0);
			     }
			 }
		});
		teamSlider.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent e) {
		        teamNumChange(teamSlider.getValue());
		      }
		});
		startBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				//take in all the info and start the game
				boolean nameValid = checkTeamNameValid();
				boolean fileFormatCorrect = checkFileFormat();
				if(nameValid && fileFormatCorrect){
					processTeamName();
					gameStart();
				}
				else{
					if(!nameValid){
						int result = JOptionPane.showConfirmDialog(getParent(),
								"Name all your teams", "Error", JOptionPane.DEFAULT_OPTION);
					}
					
					if(!fileFormatCorrect){
						int result = JOptionPane.showConfirmDialog(getParent(),
								"Textfile format not correct. Choose another one", "Error", JOptionPane.DEFAULT_OPTION);
					}
					
				}
				
			}
		});
		fileBtn.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				chooseFile();
			}
		});
		quickMode.addItemListener(new ItemListener() {
		   public void itemStateChanged(ItemEvent e) {
		      inQuickMode = quickMode.isSelected();
		   }
		});
		clearBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				teamSlider.setValue(1);
				f1.setText("");
				fileChooser.setSelectedFile(new File(""));
				selectedFile = null;
				quickMode.setSelected(false);
				inQuickMode = false;
				fileName.setText("");
			}
		});
		exitBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
	}
	
	public void processTeamName(){
		teamName = new String[4];
		//safe to do so cuz if team doesn't exist will just be empty 
		//and we know what the team number is;
		teamName[0] = f1.getText();
		teamName[1] = f2.getText();
		teamName[2] = f3.getText();
		teamName[3] = f4.getText();
	}
	
	
	public boolean checkFileFormat(){
		if(selectedFile == null) return false;
		Play p = new Play(selectedFile.getAbsolutePath());
		return p.checkFormat();
	}
	
	public boolean checkTeamNameValid(){
		int numTeam = teamSlider.getValue();
		if(numTeam == 4){
			return !f4.getText().isEmpty() && !f3.getText().isEmpty()
					&& !f2.getText().isEmpty() && !f1.getText().isEmpty();
		}
		else if(numTeam == 3){
			return  !f3.getText().isEmpty()
					&& !f2.getText().isEmpty() && !f1.getText().isEmpty();
		}
		else if(numTeam == 2){
			return  !f2.getText().isEmpty() && !f1.getText().isEmpty();
		}
		else{
			return  !f1.getText().isEmpty();
		}
	}
	
	public void teamNumChange(int val){
		if(val==4){
			team4.setVisible(true);
			team3.setVisible(true);
			team2.setVisible(true);
		}
		else if(val==3){
			team4.setVisible(false);
			f4.setText("");
			team3.setVisible(true);
			team2.setVisible(true);
		}
		else if(val==2){
			team4.setVisible(false);
			team3.setVisible(false);
			f3.setText(""); f4.setText("");
			team2.setVisible(true);
		}
		else{
			team4.setVisible(false);
			team3.setVisible(false);
			team2.setVisible(false);
			f4.setText(""); f3.setText(""); 
			f2.setText("");
		}
	}
	
	
	
	
	
}
