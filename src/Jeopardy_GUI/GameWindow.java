package Jeopardy_GUI;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GameWindow extends JFrame{
	private static final long serialVersionUID = 2;
	private JLabel title, questionLbl, teamNameLbl, catSelectedLbl, 
					pointSelectedLbl, warningLbl;
	private JTextField ansTextField;
	private JButton submitBtn;
	private JLabel [] teamsLbl = new JLabel[4];
	private JLabel [] teamMoneyLbl = new JLabel[4];
	private JLabel[] lblArray = new JLabel[5];
	private JButton[][] btnArray = new JButton[5][5];
	private JPanel mainPanel, leftPanel, rightPanel;
	private JPanel boardPanel, questionPanel, finalPanel;
	private JTextArea textArea;
	private JMenuBar mainMenuBar;
	private JMenuItem restart;
	private JMenuItem exit;
	private JMenuItem chooseNewFile;
	//final round stuff
	private JLabel finalRoundQ;
	private ArrayList<JLabel> finalTeamLbl ;
	private ArrayList<JSlider> finalTeamSlider ;
	private ArrayList<JButton> finalBetBtn ;
	private ArrayList<JLabel> finalBenMoneyLbl ;	
	private ArrayList<JTextField> finalAnswers;
	private ArrayList<JButton>finalSubmitBtn ;
	private ArrayList<Boolean> finalRoundCorrect;
	private final static String BOARDPAN = "BOARDPAN";
	private final static String QUESTIONPAN = "QUESTIONPAN";
	private final static String FINALPAN = "FINALPAN";
	
	//logic data member
	private Play game;
	private Team[]  teams;
	private Question[][] questions;
	private finalQuestion fjQ;
	private int numTeam;
	private String teamName[];
	private String category[];
	private int points[];
	private int selectedCatIdx;
	private int selectedPointIdx;
	private int selectedTeamIdx;
	private int questionAnswered;
	private final int FINALJEOPARDYCOUNT;
	
	
		
	public GameWindow(String filename, int numTeam, String[] teamName, boolean inQuickMode){
		super("Jeopardy");
		game = new Play(filename, numTeam, teamName);
		if(inQuickMode){
			FINALJEOPARDYCOUNT = 5;
		}else{
			FINALJEOPARDYCOUNT = 25;
		}
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		initializeGameDataMember(numTeam);
		initializeComponents();
		createGUI();
		addEvents();
		setVisible(true);
	}
	
	
	public void initializeComponents(){
		setTitle();
		createMenu();
		createCatLbls();
		createButtons();
			
	}
	
	public void initializeGameDataMember(int numTeam){
		teams = game.getTeams();
		questions = game.getQuestionsData();
		this.numTeam = numTeam;	
		points = game.getPoints();
		category = game.getCategories();
		Random randomGenerator = new Random();
		selectedTeamIdx = randomGenerator.nextInt(numTeam);
		fjQ = game.getFinalQ();
	}
	

	
	
	
	public void setTitle(){
		title = new JLabel("Jeopardy");
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setVerticalAlignment(SwingConstants.CENTER);
		title.setPreferredSize(new Dimension(200,125));
		title.setFont(new Font("Sans Serif", Font.BOLD +Font.ITALIC, 35));
		title.setBackground(Color.gray);
		title.setForeground(Color.white);
		title.setOpaque(true);
	}
	
	
	public void createMenu(){
		JMenu menu = new JMenu("Menu");
		mainMenuBar = new JMenuBar();
		mainMenuBar.add(menu);
		restart = new JMenuItem("Restart");
		chooseNewFile = new JMenuItem("Choose New File");
		exit = new JMenuItem("Exit");
		menu.add(restart);
		menu.add(chooseNewFile);
		menu.add(exit);
		mainMenuBar.add(menu);
		setJMenuBar(mainMenuBar);
	}
	public void createCatLbls(){
		for(int i = 0; i < 5; i++){
			lblArray[i] = new JLabel(category[i]);
			lblArray[i].setBackground(Color.gray);
			lblArray[i].setForeground(Color.white);
			lblArray[i].setOpaque(true);
			lblArray[i].setFont(new Font("Sans Serif", Font.BOLD +Font.ITALIC, 15));
			lblArray[i].setSize(200, 165);
			lblArray[i].setHorizontalAlignment(SwingConstants.CENTER);
			lblArray[i].setVerticalAlignment(SwingConstants.CENTER);
		}
	}
	
	public void createButtons(){
		int score[] = game.getPoints();
		for(int i =  0 ; i < 5; i++){
			for(int j = 0 ; j < 5; j++){
				btnArray[i][j] = new JButton("$" + Integer.toString(score[j]));
				btnArray[i][j].setPreferredSize(new Dimension(200, 165));
				btnArray[i][j].setBackground(Color.gray);
				btnArray[i][j].setForeground(Color.white);
				btnArray[i][j].setFont(new Font("Sans Serif", Font.BOLD, 15));
			}
		}
	}
	
	public void addLeftPanelElements(){
		leftPanel.setLayout(new CardLayout());
		leftPanel.setPreferredSize(new Dimension(1000,1000));
		createBoard();
		createQuestionPan();
	}
	
	
	public void initializeFinalComponenets(){
		finalPanel = new JPanel();
		finalTeamLbl = new ArrayList<JLabel>();
		finalTeamSlider = new ArrayList<JSlider>();
		finalBetBtn = new ArrayList<JButton>();
	    finalBenMoneyLbl = new ArrayList<JLabel>();	
		finalAnswers = new ArrayList<JTextField>();
		finalSubmitBtn = new ArrayList<JButton>();
		finalRoundCorrect = new ArrayList<Boolean>();
	}
	
	
	
	public void createFinalRoundEvents(ArrayList<Integer> finalTeamsIdx){
		for(int i = 0 ; i < finalTeamsIdx.size(); i++){
			JButton tBet  = finalBetBtn.get(i);
			JSlider tSlider = finalTeamSlider.get(i);
			JLabel moneyLbl = finalBenMoneyLbl.get(i);
			JButton submitB = finalSubmitBtn.get(i);
			JTextField finalAns = finalAnswers.get(i);
			int index = finalTeamsIdx.get(i);
			tBet.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					int bet = tSlider.getValue();
					textArea.setText(textArea.getText()+ "\n" + teams[index].getName() + " bets $ " + bet );
					tBet.setEnabled(false);
					tBet.setBackground(Color.LIGHT_GRAY);
					tSlider.setEnabled(false);
					
					if(checkIfAllBets()){
						//show question
						finalRoundQ.setText(fjQ.getQeustion());
						textArea.setText(textArea.getText()+ "\n" + "Here is the final question:" + "\n" + fjQ.getQeustion());
						for(int j = 0 ; j < finalSubmitBtn.size(); j++){
							finalSubmitBtn.get(j).setEnabled(true);
							finalSubmitBtn.get(j).setBackground(Color.WHITE);
						}
						
					}
				}
			});	
			finalAns.addMouseListener(new MouseAdapter(){
	            @Override
	            public void mouseClicked(MouseEvent e){
	               finalAns.setText("");
	            }
	        });
			tSlider.addChangeListener(new ChangeListener() {
		        public void stateChanged(ChangeEvent e) {
		        	String moneyString = new Integer(tSlider.getValue()).toString();
		        	moneyLbl.setText("$ " + moneyString);
		        	
		        }
	        });			
			submitB.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					submitB.setEnabled(false);
					submitB.setBackground(Color.GRAY);
					boolean getRight = fjQ.answerFJQuestion(finalAns.getText());				
					if(getRight){
						teams[index].addDollars(tSlider.getValue());
					}
					else{
						teams[index].minusDollars(tSlider.getValue());
					}
					
					if(checkIfAllAnswered()){
						finalUpdate();
						displayWinner();
					}
					
				}
			});
		}
		
		
	}
	public void createFinalRoundPan(){
		initializeFinalComponenets();
		finalPanel.setLayout(new BoxLayout(finalPanel, BoxLayout.Y_AXIS));
		finalPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		finalPanel.setMaximumSize(new Dimension(1000,1000));		
		finalPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		//finalPanel.setMaximumSize(new Dimension(1000,1000));
		JPanel grid = new JPanel();
		grid.setLayout(new GridLayout(5,1));
		grid.setPreferredSize(new Dimension(900, 600));
		grid.setMaximumSize(new Dimension(900, 600));
		grid.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		///grid.setBorder(new EmptyBorder(15, 15, 15, 15));
		JLabel title = new JLabel("Final Jeopardy Round", JLabel.CENTER);
		title.setFont(new Font("Sans-Serif", Font.PLAIN, 24));
		finalPanel.add(title);
		initializeFinalComponenets();
		ArrayList<Integer> finalTeamsIdx = game.getFinalCandidates();		
		for(int i = 0; i < finalTeamsIdx.size(); i++){
			JPanel t = new JPanel();
			t.setLayout(new BoxLayout(t, BoxLayout.X_AXIS));	
			t.setMaximumSize(new Dimension(475, 50));
			JLabel tName = new JLabel(teams[finalTeamsIdx.get(i)].getName());
			tName.setFont(new Font("Sans Serif", Font.BOLD , 15));
			JSlider tSlider = new JSlider(JSlider.HORIZONTAL, 1, teams[finalTeamsIdx.get(i)].getDollars(), 1);
			tSlider.setPaintTicks(true);
			tSlider.setPaintLabels(true);
			tSlider.setMajorTickSpacing(100);
			tSlider.setMinorTickSpacing(10);
			JButton tBet = new JButton("Set Bet");
			tBet.setBackground(Color.GRAY);
			tBet.setForeground(Color.WHITE);
			finalBetBtn.add(tBet);
			JLabel MoneyLbl = new JLabel("$ " + new Integer(tSlider.getValue()).toString());
			finalBenMoneyLbl.add(MoneyLbl);
			finalTeamSlider.add(tSlider);
			finalTeamLbl.add(tName);
			finalRoundCorrect.add(false); //set the team not answer correct by default
			t.add(tName); t.add(tSlider); t.add(MoneyLbl); t.add(tBet); 
			grid.add(t);
		}	
		finalRoundQ = new JLabel("And the question is...", JLabel.CENTER);
		finalRoundQ.setFont(new Font("Sans Serif", Font.BOLD +Font.ITALIC, 15));
		finalRoundQ.setPreferredSize(new Dimension(700, 50));
		finalRoundQ.setMaximumSize(new Dimension(700, 150));
		grid.add(finalRoundQ);
		finalPanel.add(grid);
		
		
		JPanel lowerGrid = new JPanel();
		lowerGrid.setLayout(new GridLayout(2,2));
		lowerGrid.setMinimumSize(new Dimension(900, 250));
		lowerGrid.setMaximumSize(new Dimension(900, 250));
		lowerGrid.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		for(int i = 0; i < finalTeamsIdx.size() ; i++){
			JPanel t = new JPanel();
			t.setMaximumSize(new Dimension(470,100));
			t.setLayout(new BoxLayout(t, BoxLayout.X_AXIS));	
			Font font = new Font("Courier", Font.ITALIC,12);
			JTextField answer = new JTextField(teams[finalTeamsIdx.get(i)].getName() + ", enter your answer.");
			answer.setFont(font);
			answer.setMinimumSize(new Dimension(300, 30));
			answer.setMinimumSize(new Dimension(300, 30));
			JButton submitB = new JButton("Submit Answer");
			submitB.setEnabled(false);
			submitB.setBackground(Color.LIGHT_GRAY);	
			submitB.setPreferredSize(new Dimension(200, 50));
			submitB.setMaximumSize(new Dimension(200, 50));
			t.add(answer); t.add(submitB);
			lowerGrid.add(t);
			//data member for later check
			finalAnswers.add(answer); finalSubmitBtn.add(submitB);		
		}
		finalPanel.add(lowerGrid);
		
		createFinalRoundEvents(finalTeamsIdx);
		leftPanel.add(finalPanel, FINALPAN);
		
		
	}
	public void createQuestionPan(){
		questionPanel = new JPanel();
		questionPanel.setLayout(new BoxLayout(questionPanel, BoxLayout.Y_AXIS));
		questionPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
		questionPanel.setMaximumSize(new Dimension(1000, 1000));		
		questionPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		questionLbl = new JLabel();
		questionLbl.setPreferredSize(new Dimension(1000,150));
		questionLbl.setMinimumSize(new Dimension(1000,150));
		questionLbl.setFont(new Font("Sans-Serif", Font.BOLD, 20));
		questionLbl.setOpaque(true);
		questionLbl.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		ansTextField = new JTextField();
		ansTextField.setPreferredSize(new Dimension(500, 50));
		ansTextField.setMaximumSize(new Dimension(500, 50));
		ansTextField.setFont(new Font("SansSerif", Font.BOLD, 20));
		ansTextField.setEditable(true);
		ansTextField.setAlignmentX(JTextField.CENTER_ALIGNMENT);
		submitBtn = new JButton("Submit");	
		submitBtn.setMinimumSize(new Dimension(200, 50));
		submitBtn.setBackground(Color.gray);
		submitBtn.setForeground(Color.white);
		submitBtn.setAlignmentX(JButton.CENTER_ALIGNMENT);
		JPanel profilePanel = new JPanel();
		profilePanel.setLayout(new GridLayout(1,3));
		profilePanel.setMaximumSize(new Dimension(1000, 100));
		profilePanel.setBackground(Color.gray);
		JPanel lowerPanel = new JPanel();
		lowerPanel.setMaximumSize(new Dimension(900, 280));
		lowerPanel.setLayout(new BoxLayout(lowerPanel, BoxLayout.Y_AXIS));		
		teamNameLbl = new JLabel();
		teamNameLbl.setHorizontalAlignment(SwingConstants.CENTER);
		teamNameLbl.setVerticalAlignment(SwingConstants.CENTER);
		teamNameLbl.setFont(new Font("Sans-Serif", Font.BOLD, 30));
		teamNameLbl.setPreferredSize(new Dimension(300, 100));
		teamNameLbl.setForeground(Color.WHITE);
		catSelectedLbl = new JLabel();
		catSelectedLbl.setHorizontalAlignment(SwingConstants.CENTER);
		catSelectedLbl.setVerticalAlignment(SwingConstants.CENTER);
		catSelectedLbl.setFont(new Font("Sans-Serif", Font.BOLD, 30));
		catSelectedLbl.setPreferredSize(new Dimension(300, 100));
		catSelectedLbl.setForeground(Color.WHITE);
		pointSelectedLbl = new JLabel();
		pointSelectedLbl.setHorizontalAlignment(SwingConstants.CENTER);
		pointSelectedLbl.setVerticalAlignment(SwingConstants.CENTER);
		pointSelectedLbl.setPreferredSize(new Dimension(300, 100));
		pointSelectedLbl.setFont(new Font("Sans-Serif", Font.BOLD, 30));
		pointSelectedLbl.setForeground(Color.WHITE);
		warningLbl = new JLabel("");	
		warningLbl.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		warningLbl.setOpaque(true);
		warningLbl.setPreferredSize(new Dimension(500, 50));
		warningLbl.setMaximumSize(new Dimension(500, 50));
		warningLbl.setFont(new Font("Sans-Serif", Font.PLAIN, 18)); //stub only shows when wrong format
		profilePanel.add(teamNameLbl);
		profilePanel.add(catSelectedLbl);
		profilePanel.add(pointSelectedLbl);
		JPanel ansPanel = new JPanel();
		ansPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(10,10,10,10);
        ansPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		ansPanel.setLayout(new GridBagLayout());	
		ansPanel.setPreferredSize(new Dimension(1000, 250));
		ansPanel.add(ansTextField,gbc);
		//ansPanel.add(Box.createHorizontalStrut(30));
		ansPanel.add(submitBtn,gbc);
		ansPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		lowerPanel.setMinimumSize(new Dimension(1000,650));
		lowerPanel.setMaximumSize(new Dimension(1000,650));
		lowerPanel.add(Box.createVerticalStrut(30));
		lowerPanel.add(warningLbl);
		lowerPanel.add(Box.createVerticalStrut(30));
		lowerPanel.add(questionLbl);
		lowerPanel.add(Box.createVerticalStrut(30));

		lowerPanel.add(ansPanel);
		lowerPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		questionPanel.add(profilePanel);
		//questionPanel.add(Box.createHorizontalStrut(20));
		questionPanel.add(lowerPanel);
		leftPanel.add(questionPanel, QUESTIONPAN);
		

	}
	public void addRightPanelElements(){
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		rightPanel.setBorder(new EmptyBorder(18, 15, 15, 15));
		rightPanel.setMaximumSize(new Dimension(400, 1000));
		rightPanel.setAlignmentX( JPanel.LEFT_ALIGNMENT );
		JPanel rightUpper = new JPanel();
		JPanel rightLower = new JPanel();
		JPanel rightMiddle = new JPanel();
	
		createMiddleLabel(rightMiddle);
		rightUpper.setLayout(new GridLayout(4,2));
		createTextArea(rightLower);
		createTeamLabels(rightUpper);
		rightPanel.add(rightUpper);
		rightPanel.add(rightMiddle);
		rightPanel.add(rightLower);
	}
	
	public void createMiddleLabel(JPanel rightMiddle){
		rightMiddle.setLayout(new GridLayout(1,1));
		rightMiddle.setAlignmentX( JPanel.LEFT_ALIGNMENT );
		rightMiddle.setPreferredSize(new Dimension(500, 100));
		rightMiddle.setMaximumSize(new Dimension(500, 100));
		JLabel middleLbl = new JLabel("Game Progress");
		middleLbl.setBackground(new Color(0,0,182,155) );
		middleLbl.setOpaque(true);
		middleLbl.setFont(new Font("Sans Serif", Font.BOLD +Font.ITALIC, 25));
		middleLbl.setHorizontalAlignment(SwingConstants.CENTER);
		middleLbl.setVerticalAlignment(SwingConstants.CENTER);
		rightMiddle.add(middleLbl);	
	}
	
	
	public void createTeamLabels(JPanel rightUpper){
		rightUpper.setAlignmentX( JPanel.LEFT_ALIGNMENT );	
		rightUpper.setPreferredSize(new Dimension(500, 400));
		rightUpper.setMaximumSize(new Dimension(500, 400));
		for(int i = 0 ; i < 4; i++){
			if(i >= numTeam){ //empty space
				teamsLbl[i] = new JLabel("");
				teamMoneyLbl[i] = new JLabel("");	
			}
			else{
				teamsLbl[i] = new JLabel(teams[i].getName());
				teamMoneyLbl[i] = new JLabel("$ " + String.valueOf(teams[i].getDollars()));	
			}	
			teamsLbl[i].setBackground(Color.gray);
			teamsLbl[i].setForeground(Color.white);
			teamsLbl[i].setOpaque(true);
			teamsLbl[i].setFont(new Font("Sans Serif", Font.BOLD +Font.ITALIC, 15));
			teamsLbl[i].setHorizontalAlignment(SwingConstants.CENTER);
			teamsLbl[i].setMaximumSize(new Dimension(150, 250));
			teamMoneyLbl[i].setBackground(Color.gray);
			teamMoneyLbl[i].setForeground(Color.white);
			teamMoneyLbl[i].setOpaque(true);
			teamMoneyLbl[i].setFont(new Font("Sans Serif", Font.BOLD , 15));
			teamMoneyLbl[i].setHorizontalAlignment(SwingConstants.CENTER);
			teamMoneyLbl[i].setVerticalAlignment(SwingConstants.CENTER);
			teamMoneyLbl[i].setMaximumSize(new Dimension(150, 250));
			rightUpper.add(teamsLbl[i]);
			rightUpper.add(teamMoneyLbl[i]);
		}
	
	}
	
	public void createTextArea(JPanel rightLower){
		rightLower.setAlignmentX( JPanel.LEFT_ALIGNMENT );
		rightLower.setPreferredSize(new Dimension(500, 400));
		rightLower.setMaximumSize(new Dimension(500, 400));
		textArea = new JTextArea(450,400);
		textArea.setFont(new Font("Helvetica Neue", Font.PLAIN + Font.BOLD, 18));
		textArea.setEditable(false);
		textArea.setMargin(new Insets(30, 30, 30, 30));
		textArea.setText("Welcome to Jeopardy! The First Team to go is" + "\n" 
							+ teams[selectedTeamIdx].getName());
		textArea.setBackground(Color.gray);
		JScrollPane sp = new JScrollPane (textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sp.setPreferredSize(new Dimension(450, 400));
		rightLower.add(sp);
	}
	
	public void createBoard(){
		boardPanel = new JPanel();
		boardPanel.setSize(900, 1000);
		boardPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		boardPanel.setLayout(new BoxLayout(boardPanel, BoxLayout.Y_AXIS));
		JPanel boardUpper = new JPanel();
		boardUpper.setLayout(new GridLayout(1,1));
		//boardUpper.setPreferredSize(new Dimension(500,300));
		JPanel boardMiddle = new JPanel();
		boardMiddle.setLayout(new GridLayout(1,5));
		boardMiddle.setPreferredSize(new Dimension(1000, 50));
		JPanel boardLower = new JPanel();
		boardLower.setLayout(new GridLayout(5,5));
		boardLower.setPreferredSize(new Dimension(1000, 750));
		boardUpper.add(title);
		for(int i =0; i <5; i ++){
			boardMiddle.add(lblArray[i]);
		}
		for(int i =  0 ; i < 5; i++){
			for(int j = 0 ; j < 5; j++){
				boardLower.add(btnArray[j][i]);
			}
		}
		boardPanel.add(boardUpper);
		boardPanel.add(boardMiddle);
		boardPanel.add(boardLower);
		leftPanel.add(boardPanel, BOARDPAN);
		
		
	}
	

	public void createGUI(){
		setSize(1500, 1000);
		mainPanel = new JPanel();
		leftPanel = new JPanel();
		rightPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
		leftPanel.setMaximumSize(new Dimension(850, 1000));		
		leftPanel.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		rightPanel.setMaximumSize(new Dimension(500,1000));
		addLeftPanelElements();
		addRightPanelElements();
		leftPanel.setVisible(true);
		mainPanel.add(leftPanel);
		mainPanel.add(rightPanel);
		add(mainPanel);
		mainPanel.setVisible(true);
	}
	
	public void addEvents(){
		addBtnEvents();
		addSubmitBtnEvents();
		addMenuEvents();
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
		
	}
	
	public void addMenuEvents(){
		restart.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				restartGame();
			}
		});
		exit.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				System.exit(0);
			}
		});
		chooseNewFile.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				chooseNewFile();
			}
		});
	}
	
	private void restartGame(){
		questionAnswered = 0;
		game.resetTheGame();
		for(int i = 0; i < 5; i++){
			for(int j = 0 ; j < 5; j++){
				btnArray[i][j].setBackground(Color.gray);
				btnArray[i][j].setEnabled(true);
			}
			
		}
		for(int i = 0 ; i < teams.length; i ++){	
			teamMoneyLbl[i].setText("$0");
		}
		ansTextField.setText("");
		CardLayout cl = (CardLayout)(leftPanel.getLayout());
		cl.show(leftPanel, BOARDPAN);
		textArea.setText("Welcome to Jeopardy!" + "\n" +"The team to go first is " 
							+ teams[selectedTeamIdx].getName() );
	}
	
	private void chooseNewFile(){
		StartWindow start = new StartWindow();
		start.setVisible(true);
		this.dispose();
	}
	
	public void addSubmitBtnEvents(){
		submitBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				Question selectedQ = questions[selectedCatIdx][selectedPointIdx];
				String answer = ansTextField.getText();
				selectedQ.answerQuestion(answer);
				if(!selectedQ.correctFormat() && !selectedQ.getWarned()){				
					warningLbl.setText("Remember to pose your question as a question form.");
					return;
				}
				else{
					if(selectedQ.getRight()){
						//System.out.println("You answered it right! " + point + " will be added to your total");
						questionAnswered ++;
						teams[selectedTeamIdx].addDollars(selectedQ.getPoints());
						teamMoneyLbl[selectedTeamIdx].setText("$" + String.valueOf(teams[selectedTeamIdx].getDollars()));
						displayMoneyMessage(true);
					}
					else{
						questionAnswered++; 
						teams[selectedTeamIdx].minusDollars(selectedQ.getPoints());
						teamMoneyLbl[selectedTeamIdx].setText("$" +String.valueOf(teams[selectedTeamIdx].getDollars()));
						displayMoneyMessage(false);
					}
				}
				warningLbl.setText("");
				selectedTeamIdx = (selectedTeamIdx + 1)%numTeam;
				ansTextField.setText("");
				CardLayout cl = (CardLayout)(leftPanel.getLayout());
				cl.show(leftPanel, BOARDPAN);
				if(questionAnswered == FINALJEOPARDYCOUNT){
					ArrayList<Integer> finalCanIdx = game.getFinalCandidates();
					if(finalCanIdx.size()==0){
						displayWinner();
					}
					else{
						finalJeopardyRound();
					}
					//
					return;
				}
				displayNextTeamMessage();
			}
		});
	}
	
	public void addBtnEvents(){
		for(int i = 0 ; i < 5; i ++){
			for(int j =0; j < 5; j++){
				JButton ref = btnArray[i][j];
				ref.putClientProperty( "cat", i );
				ref.putClientProperty("point", j);
				ref.addActionListener( new ActionListener() {
					public void actionPerformed(ActionEvent ae){
						ref.setEnabled(false);
						ref.setBackground(Color.lightGray);
						selectedCatIdx = (int)ref.getClientProperty("cat");
						selectedPointIdx = (int)ref.getClientProperty("point");	
						textArea.append("\n" + teams[selectedTeamIdx].getName()+
								" choose the question in " + category[selectedCatIdx] + "\nworth $"
								+ points[selectedPointIdx]);
						proceedQuestion();
					}
				});
			}
		}
	}
	
	
	public void proceedQuestion(){
		//System.out.println(questions[selectedCatIdx][selectedPointIdx].getQeustion());
		//stub teamname;
		teamNameLbl.setText(teams[0].getName());
		catSelectedLbl.setText(category[selectedCatIdx]);
		pointSelectedLbl.setText(String.valueOf(points[selectedPointIdx]));
		questionLbl.setText(questions[selectedCatIdx][selectedPointIdx].getQeustion());	
		CardLayout cl = (CardLayout)(leftPanel.getLayout());
		cl.show(leftPanel, QUESTIONPAN);
	}
	
	public void displayMoneyMessage(boolean win){
		if(win){
			textArea.append("\n" + teams[selectedTeamIdx].getName()+
					" you got it right the answer right! $ " + points[selectedPointIdx] +
					"\nwill be added to your score");
		}
		else{
			textArea.append("\nOops " + teams[selectedTeamIdx].getName()+
					" you got it right the answer wrong! $ " + points[selectedPointIdx] +
					"\nwill be deducted to your score");
		}
	}
	
	
	public void displayNextTeamMessage(){
		textArea.append("\nNow it's " + teams[selectedTeamIdx].getName()+
				"'s turn! Please choose a\n" +"question");
	}
	
	public void finalJeopardyRound(){
		textArea.append("\nWelcome to Final round Jeopardy.");
		createFinalRoundPan();
		CardLayout cl = (CardLayout)(leftPanel.getLayout());
		cl.show(leftPanel, FINALPAN);
	}
	
	
	private void displayWinner(){
			
			ArrayList<Integer> winnersIdxList = game.getWinners();
			if(winnersIdxList.size() == 0){
				int result = JOptionPane.showConfirmDialog(getParent(),
						"No winners in the game!", "Error", JOptionPane.DEFAULT_OPTION);
				if(result==0) chooseNewFile();
			}
			else{
				String winnerString ="";
				for(int i = 0 ; i < winnersIdxList.size(); i++){
					winnerString += teams[winnersIdxList.get(i)].getName() + "\n";
				}
				int result = JOptionPane.showConfirmDialog(getParent(),
						"And the winner(s) is ...\n" + winnerString , "Error", JOptionPane.DEFAULT_OPTION);
				if(result==0) chooseNewFile();
			}
			
			
		}
	
	private boolean checkIfAllBets(){
		int numBets = 0;
		for(int i = 0 ; i < finalBetBtn.size() ; i++){
			if(!finalBetBtn.get(i).isEnabled()){
				numBets++;
			}
		}
		
		
		return numBets == finalBetBtn.size();
	}
	
	private boolean checkIfAllAnswered(){
		int numAns = 0;
		for(int i = 0 ; i < finalSubmitBtn.size() ; i++){
			if(!finalSubmitBtn.get(i).isEnabled()){
				numAns++;
			}
		}
		
		return numAns == finalSubmitBtn.size();
	}
	
	private void finalUpdate(){
		for(int i = 0; i < teams.length; i++){
			String score = new Integer(teams[i].getDollars()).toString();
			teamMoneyLbl[i].setText(" $"+score);
		}
	}

	

}
