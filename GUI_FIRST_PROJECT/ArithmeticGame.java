import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class ArithmeticGame extends JFrame {
    // Game panels
    private JPanel titlePanel;
    private JPanel gamePanel;
    
    // Game components
	private JLabel questionLabel;
	private QuestionPanel questionPanel;
    private JTextField answerField;
    private JLabel resultLabel;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JLabel playerNameLabel;
    private JLabel welcomeLabel;
    private JButton submitButton;
    private JButton newGameButton;
    private JButton leaderboardButton;
    private JButton backToTitleButton;
	private JButton settingsButton;
    private JComboBox<String> operationComboBox;
    private JComboBox<String> difficultyComboBox;
	private JComboBox<String> themeComboBox;
	private JComboBox<String> schemeComboBox;

	// Theme support
	private enum ThemeMode { LIGHT, DARK }
	private ThemeMode currentTheme = ThemeMode.LIGHT;

	private enum ColorScheme { BLUE, PURPLE, GREEN, SUNSET, NEON }
	private ColorScheme currentScheme = ColorScheme.BLUE;

	// Theme helpers
	private boolean isDark() { return currentTheme == ThemeMode.DARK; }

	private Color adjustForTheme(Color c) { return isDark() ? c.darker() : c; }

	private Color titleGradientStart() {
		switch (currentScheme) {
			case PURPLE: return adjustForTheme(new Color(186, 104, 200));
			case GREEN: return adjustForTheme(new Color(129, 199, 132));
			case SUNSET: return adjustForTheme(new Color(255, 138, 101));
			case NEON: return adjustForTheme(new Color(0, 230, 118));
			case BLUE:
			default: return adjustForTheme(new Color(100, 181, 246));
		}
	}

	private Color titleGradientEnd() {
		switch (currentScheme) {
			case PURPLE: return adjustForTheme(new Color(123, 31, 162));
			case GREEN: return adjustForTheme(new Color(56, 142, 60));
			case SUNSET: return adjustForTheme(new Color(255, 87, 34));
			case NEON: return adjustForTheme(new Color(0, 191, 165));
			case BLUE:
			default: return adjustForTheme(new Color(30, 136, 229));
		}
	}

	private Color gameGradientStart() {
		switch (currentScheme) {
			case PURPLE: return adjustForTheme(new Color(225, 190, 231));
			case GREEN: return adjustForTheme(new Color(200, 230, 201));
			case SUNSET: return adjustForTheme(new Color(255, 204, 188));
			case NEON: return adjustForTheme(new Color(178, 255, 219));
			case BLUE:
			default: return adjustForTheme(new Color(179, 229, 252));
		}
	}

	private Color gameGradientEnd() {
		switch (currentScheme) {
			case PURPLE: return adjustForTheme(new Color(171, 71, 188));
			case GREEN: return adjustForTheme(new Color(102, 187, 106));
			case SUNSET: return adjustForTheme(new Color(255, 112, 67));
			case NEON: return adjustForTheme(new Color(29, 233, 182));
			case BLUE:
			default: return adjustForTheme(new Color(100, 181, 246));
		}
	}
	private Color baseBg() {
		if (isDark()) return new Color(33, 33, 33);
		switch (currentScheme) {
			case PURPLE: return new Color(243, 232, 250);
			case GREEN: return new Color(232, 248, 235);
			case SUNSET: return new Color(255, 239, 231);
			case NEON: return new Color(232, 255, 246);
			case BLUE:
			default: return new Color(232, 245, 255);
		}
	}

private Color answerPanelBg() { return infoPanelBg(); }

	private Color infoPanelBg() {
		if (isDark()) return new Color(40, 40, 40);
		switch (currentScheme) {
			case PURPLE: return new Color(230, 210, 242);
			case GREEN: return new Color(212, 236, 218);
			case SUNSET: return new Color(255, 222, 205);
			case NEON: return new Color(196, 252, 230);
			case BLUE:
			default: return new Color(210, 235, 252);
		}
	}
	private Color primaryText() { return isDark() ? new Color(236, 239, 241) : new Color(25, 118, 210); }
	private Color secondaryText() { return isDark() ? new Color(207, 216, 220) : new Color(13, 71, 161); }
	private Color normalText() { return isDark() ? new Color(245, 245, 245) : Color.BLACK; }
	private Color scoreTextColor() { return isDark() ? new Color(255, 213, 79) : normalText(); }

	private void applyTextColors() {
		if (welcomeLabel != null) welcomeLabel.setForeground(primaryText());
		if (playerNameLabel != null) playerNameLabel.setForeground(secondaryText());
		if (questionLabel != null) questionLabel.setForeground(normalText());
		if (scoreLabel != null) scoreLabel.setForeground(scoreTextColor());
		if (timerLabel != null) timerLabel.setForeground(normalText());
	}

	// Large question panel that draws the equation inside a big rounded box
	private class QuestionPanel extends JPanel {
		private String questionText = "";

		public QuestionPanel() {
			setOpaque(false);
			setLayout(null); // allow absolute placement of answerField inside the panel
		}

		public void setQuestion(String text) {
			this.questionText = text == null ? "" : text;
			// Ensure the answer field is hosted here whenever the question expects an input
			if (this.questionText.contains("?")) {
				if (answerField != null && answerField.getParent() != this) {
					this.add(answerField);
					revalidate();
				}
			}
			// Optionally could remove when no placeholder, but not needed since game always expects input
			repaint();
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(900, 200);
		}

		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			int w = getWidth();
			int h = getHeight();
			int hPad = 24;
			int boxHPad = 18; // inner horizontal padding for number boxes
			int boxVPad = 10; // inner vertical padding for number boxes
			int arc = 20;

			// Prepare fonts
			Font numberFont = new Font("Arial", Font.BOLD, Math.max(56, h / 3));
			Font opFont = new Font("Arial", Font.BOLD, Math.max(48, h / 3 - 8));

			// Tokenize by spaces
			String[] tokens = questionText == null ? new String[0] : questionText.split(" ");

			// First pass: find widest number box
			int maxNumberBoxW = 0;
			for (int i = 0; i < tokens.length; i++) {
				String t = tokens[i];
				if (t.matches("\\d+")) {
					FontMetrics fmNum = g2d.getFontMetrics(numberFont);
					int wText = fmNum.stringWidth(t);
					maxNumberBoxW = Math.max(maxNumberBoxW, wText + boxHPad * 2);
				}
			}

			FontMetrics fmNumGlobal = g2d.getFontMetrics(numberFont);
			int defaultAnswerW = fmNumGlobal.stringWidth("00") + boxHPad * 2;
			int answerW = Math.max(maxNumberBoxW, defaultAnswerW);

            // Second pass (measurement): total width using unified width for numbers and answer
			int totalWidth = 0;
			for (int i = 0; i < tokens.length; i++) {
				String t = tokens[i];
                if ("?".equals(t) || t.matches("\\d+")) {
                    totalWidth += answerW;
				} else {
					FontMetrics fmOp = g2d.getFontMetrics(opFont);
					totalWidth += fmOp.stringWidth(t);
				}
				if (i < tokens.length - 1) totalWidth += hPad; // spacing between tokens
			}

			int startX = Math.max(hPad, (w - totalWidth) / 2);
			int centerY = h / 2;

			// Second pass: draw and position answer field if needed
			int x = startX;
			for (int i = 0; i < tokens.length; i++) {
				String t = tokens[i];
				boolean isNumber = t.matches("\\d+");
				Font font = isNumber ? numberFont : opFont;
				g2d.setFont(font);
				FontMetrics fm = g2d.getFontMetrics();
				int textW = fm.stringWidth(t);
				int textH = fm.getAscent();

                if (isNumber) {
                    int boxW = answerW;
					int boxH = textH + boxVPad * 2;
					int y = centerY - boxH / 2;
					// Box background and border (use same as info panel styling)
					g2d.setColor(infoPanelBg());
					g2d.fillRoundRect(x, y, boxW, boxH, arc, arc);
					g2d.setStroke(new BasicStroke(3f));
					g2d.setColor(isDark() ? new Color(120, 144, 156) : new Color(100, 181, 246));
					g2d.drawRoundRect(x, y, boxW, boxH, arc, arc);
					// Text centered in box
					g2d.setColor(normalText());
					int tx = x + (boxW - textW) / 2;
					int ty = y + (boxH + textH) / 2 - 4;
					g2d.drawString(t, tx, ty);
					x += boxW + hPad;
				} else if ("?".equals(t)) {
					// Draw answer box and place the real input field inside
					FontMetrics numFm = g2d.getFontMetrics(numberFont);
					int numTextH = numFm.getAscent();
					int boxH = numTextH + boxVPad * 2;
					int placeholderW = answerW;
					int y = centerY - boxH / 2;
					g2d.setColor(Color.WHITE);
					g2d.fillRoundRect(x, y, placeholderW, boxH, arc, arc);
					g2d.setStroke(new BasicStroke(3f));
					g2d.setColor(isDark() ? new Color(120, 144, 156) : new Color(100, 181, 246));
					g2d.drawRoundRect(x, y, placeholderW, boxH, arc, arc);

					answerField.setBackground(Color.WHITE);
					answerField.setForeground(Color.BLACK);
                    answerField.setBorder(BorderFactory.createEmptyBorder(4, 10, 4, 10));
                    answerField.setFont(new Font("Arial", Font.BOLD, numberFont.getSize()));
					answerField.setHorizontalAlignment(JTextField.CENTER);
					int innerPad = 6;
					answerField.setBounds(x + innerPad, y + innerPad, placeholderW - innerPad * 2, boxH - innerPad * 2);
					x += placeholderW + hPad;
				} else {
					// Draw operator/equals/question as plain large text
					g2d.setColor(normalText());
					int ty = centerY + textH / 2 - 4;
					g2d.drawString(t, x, ty);
					x += textW + hPad;
				}
			}

			g2d.dispose();
		}
	}
    
    private String playerName = "Player";
    private int score = 0;
    private int correctAnswer;
    private int timeLeft = 30;
    private Timer gameTimer;
    private Random random = new Random();
    private String currentOperation = "Addition";
    private String currentDifficulty = "Easy";
    
    // Leaderboard data structure
    private static class LeaderboardEntry implements Comparable<LeaderboardEntry>, Serializable {
        private static final long serialVersionUID = 1L;
        String name;
        int score;
        
        public LeaderboardEntry(String name, int score) {
            this.name = name;
            this.score = score;
        }
        
        @Override
        public int compareTo(LeaderboardEntry other) {
            return other.score - this.score; // Sort in descending order
        }
    }
    
    private ArrayList<LeaderboardEntry> leaderboard = new ArrayList<>();
    private String leaderboardFile = "leaderboard.dat";
    
    public ArithmeticGame() {
        // Load leaderboard
        loadLeaderboard();
        
        // Set up the frame
        setTitle("Arithmetic Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int confirm = JOptionPane.showConfirmDialog(
                    ArithmeticGame.this,
                    "Are you sure you want to exit?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                }
            }
        });
        
        // Enable full screen optimization
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        
        // Create card layout for switching between title and game screens
        setLayout(new CardLayout());
        
        // Create title panel
        createTitlePanel();
        
        // Create game panel
        createGamePanel();
        
        // Add panels to frame with CardLayout
        add(titlePanel, "title");
        add(gamePanel, "game");
        
        // Show title panel first
        ((CardLayout)getContentPane().getLayout()).show(getContentPane(), "title");
        
        // Center the frame on screen
        setLocationRelativeTo(null);
    }
    
    private void createTitlePanel() {
		titlePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
				// Create a gradient background
                int w = getWidth();
                int h = getHeight();
				GradientPaint gp = new GradientPaint(
					0, 0, titleGradientStart(),
					0, h, titleGradientEnd()
				);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                g2d.dispose();
            }
        };
        
        titlePanel.setLayout(new BorderLayout(20, 20));
        
        // Create a stylish title
		JLabel titleLabel = new JLabel("Arithmetic Game", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 70));
		titleLabel.setForeground(new Color(250, 250, 250));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        
        // Add a subtitle
		JLabel subtitleLabel = new JLabel("Test your math skills!", JLabel.CENTER);
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 24));
		subtitleLabel.setForeground(new Color(230, 230, 230));
        
        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.setOpaque(false);
        titleContainer.add(titleLabel, BorderLayout.CENTER);
        titleContainer.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Create stylish buttons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 0, 20));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 200, 100, 200));
        
        JButton startButton = createStylishButton("Start Game", new Color(76, 175, 80));
        startButton.addActionListener(e -> startGameFromTitle());
        
        JButton leaderboardButtonTitle = createStylishButton("Leaderboard", new Color(255, 193, 7));
        leaderboardButtonTitle.addActionListener(e -> showLeaderboard());

        JButton settingsButtonTitle = createStylishButton("Settings", new Color(96, 125, 139));
        settingsButtonTitle.addActionListener(e -> openSettingsDialog());
        
        JButton exitButton = createStylishButton("Exit", new Color(244, 67, 54));
        exitButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to exit?",
                "Confirm Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
            );
            if (confirm == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
        
        buttonPanel.add(startButton);
        buttonPanel.add(leaderboardButtonTitle);
        buttonPanel.add(settingsButtonTitle);
        buttonPanel.add(exitButton);
        
        titlePanel.add(titleContainer, BorderLayout.NORTH);
        titlePanel.add(buttonPanel, BorderLayout.CENTER);
    }
    
    private JButton createStylishButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient
                GradientPaint gp = new GradientPaint(
                    0, 0, color.brighter(),
                    0, getHeight(), color.darker()
                );
                
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                
                // Draw text
                FontMetrics fm = g2d.getFontMetrics();
                Rectangle2D r = fm.getStringBounds(text, g2d);
                
                int x = (getWidth() - (int) r.getWidth()) / 2;
                int y = (getHeight() - (int) r.getHeight()) / 2 + fm.getAscent();
                
                g2d.setColor(Color.WHITE);
                g2d.drawString(text, x, y);
                g2d.dispose();
            }
            
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 60);
            }
        };
        
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void startGameFromTitle() {
        // Ask for player name; if cancelled/empty, do nothing
        if (!askPlayerName()) {
            return;
        }
        
        // Update welcome label with player name
        welcomeLabel.setText("WELCOME " + playerName + "!");
        playerNameLabel.setText("Player: " + playerName);
        
        // Show game panel
        ((CardLayout)getContentPane().getLayout()).show(getContentPane(), "game");
        
        // Start a new game
        startNewGame();
    }
    
    private void createGamePanel() {
		gamePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                
				// Create a gradient background
                int w = getWidth();
                int h = getHeight();
				GradientPaint gp = new GradientPaint(
					0, 0, gameGradientStart(),
					0, h, gameGradientEnd()
				);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                g2d.dispose();
            }
        };
        
        gamePanel.setLayout(new BorderLayout(20, 20));
        
        // Create panels with better spacing
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        JPanel centerPanel = new JPanel(new GridLayout(6, 1, 20, 30));
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        
        topPanel.setOpaque(false);
        centerPanel.setOpaque(false);
        bottomPanel.setOpaque(false);
        
        // Initialize components
		welcomeLabel = new JLabel("WELCOME!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 42));
		welcomeLabel.setForeground(primaryText());
        
		playerNameLabel = new JLabel("Player: ", JLabel.CENTER);
        playerNameLabel.setFont(new Font("Arial", Font.BOLD, 24));
		playerNameLabel.setForeground(secondaryText());
        
        // Back to title button
        backToTitleButton = createStylishButton("Back to Title", new Color(63, 81, 181));
        backToTitleButton.addActionListener(e -> {
            // Stop timer if running
            if (gameTimer != null && gameTimer.isRunning()) {
                gameTimer.stop();
            }
            // Show title panel
            ((CardLayout)getContentPane().getLayout()).show(getContentPane(), "title");
        });
        
		questionPanel = new QuestionPanel();
        
        answerField = new JTextField(10);
        answerField.setFont(new Font("Arial", Font.PLAIN, 28));
        answerField.setPreferredSize(new Dimension(150, 50));
        
		resultLabel = new JLabel("", JLabel.CENTER);
        resultLabel.setFont(new Font("Arial", Font.ITALIC, 28));
        
		scoreLabel = new JLabel("Your Score: 0", JLabel.CENTER);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
		scoreLabel.setForeground(scoreTextColor());
        
		timerLabel = new JLabel("Time: 30", JLabel.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 24));
		timerLabel.setForeground(normalText());
        
        submitButton = new JButton("Submit Answer");
        submitButton.setFont(new Font("Arial", Font.BOLD, 20));
        submitButton.setPreferredSize(new Dimension(180, 50));
        
        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 20));
        newGameButton.setPreferredSize(new Dimension(150, 50));
        
        String[] operations = {"Addition", "Subtraction", "Multiplication", "Division", "Modulo", "Mixed"};
        operationComboBox = new JComboBox<>(operations);
        operationComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        operationComboBox.setPreferredSize(new Dimension(150, 40));
        
        String[] difficulties = {"Easy", "Medium", "Hard"};
        difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        difficultyComboBox.setPreferredSize(new Dimension(150, 40));
        
        // Add components to panels
		JLabel gameTitleLabel = new JLabel("Arithmetic Game", JLabel.CENTER);
        gameTitleLabel.setFont(new Font("Arial", Font.BOLD, 64));
		gameTitleLabel.setForeground(primaryText());
        topPanel.add(gameTitleLabel, BorderLayout.CENTER);

		JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        controlsPanel.setOpaque(false);
        controlsPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        JLabel opLabel = new JLabel("Operation: ");
        opLabel.setFont(new Font("Arial", Font.BOLD, 18));
		opLabel.setForeground(normalText());
        controlsPanel.add(opLabel);
        controlsPanel.add(operationComboBox);
        JLabel diffLabel = new JLabel("Difficulty: ");
        diffLabel.setFont(new Font("Arial", Font.BOLD, 18));
		diffLabel.setForeground(normalText());
        controlsPanel.add(diffLabel);
        controlsPanel.add(difficultyComboBox);

		settingsButton = createStylishButton("Settings", new Color(96, 125, 139));
		settingsButton.addActionListener(e -> openSettingsDialog());
		controlsPanel.add(settingsButton);
        controlsPanel.add(backToTitleButton);
        topPanel.add(controlsPanel, BorderLayout.SOUTH);
        
        // Add welcome message and player name to the top of center panel
        centerPanel.add(welcomeLabel);
        centerPanel.add(playerNameLabel);
        
		JPanel questionRowPanel = new JPanel(new BorderLayout(20, 0));
		questionRowPanel.setOpaque(false);
		questionRowPanel.setBackground(baseBg());
		questionRowPanel.add(questionPanel, BorderLayout.CENTER);
        
		// Move answer field inside the question panel (inline box replaces '?')
		centerPanel.add(questionRowPanel);
        
        centerPanel.add(resultLabel);
        
		JPanel infoPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 50, 10, 50));
        infoPanel.add(scoreLabel);
        infoPanel.add(timerLabel);
        centerPanel.add(infoPanel);
        
        // Initialize leaderboard button
        leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.setFont(new Font("Arial", Font.BOLD, 20));
        leaderboardButton.setPreferredSize(new Dimension(150, 50));
        leaderboardButton.addActionListener(e -> showLeaderboard());
        
        bottomPanel.add(submitButton);
        bottomPanel.add(newGameButton);
        bottomPanel.add(leaderboardButton);
        
        // Add panels to frame
        gamePanel.add(topPanel, BorderLayout.NORTH);
        gamePanel.add(centerPanel, BorderLayout.CENTER);
        gamePanel.add(bottomPanel, BorderLayout.SOUTH);
        
        // Set background colors
		getContentPane().setBackground(baseBg());
		topPanel.setBackground(baseBg());
		centerPanel.setBackground(baseBg());
		bottomPanel.setBackground(baseBg());
		infoPanel.setOpaque(true);
		infoPanel.setBackground(infoPanelBg());
        
        // Add action listeners
        submitButton.addActionListener(e -> checkAnswer());
        newGameButton.addActionListener(e -> startNewGame());
        answerField.addActionListener(e -> checkAnswer());
        
        // Add listeners to combo boxes that only generate a new question without resetting the game
        operationComboBox.addActionListener(e -> generateQuestion());
        difficultyComboBox.addActionListener(e -> generateQuestion());
        
		// Initialize game timer
        gameTimer = new Timer(1000, e -> updateTimer());
        
        // Center the frame on screen
        setLocationRelativeTo(null);

        // Ensure hidden settings combos are created before wiring listeners
        if (themeComboBox == null) {
            String[] themes = {"Light", "Dark"};
            themeComboBox = new JComboBox<>(themes);
            themeComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        }
        if (schemeComboBox == null) {
            String[] schemes = {"Blue", "Purple", "Green", "Sunset", "Neon"};
            schemeComboBox = new JComboBox<>(schemes);
            schemeComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        }

        // Start with combos reflecting current theme/scheme
        themeComboBox.setSelectedItem(currentTheme == ThemeMode.DARK ? "Dark" : "Light");
        switch (currentScheme) {
            case PURPLE: schemeComboBox.setSelectedItem("Purple"); break;
            case GREEN: schemeComboBox.setSelectedItem("Green"); break;
            case SUNSET: schemeComboBox.setSelectedItem("Sunset"); break;
            case NEON: schemeComboBox.setSelectedItem("Neon"); break;
            case BLUE:
            default: schemeComboBox.setSelectedItem("Blue");
        }

		// Theme switch handler
        themeComboBox.addActionListener(e -> {
			String sel = (String) themeComboBox.getSelectedItem();
			currentTheme = "Dark".equals(sel) ? ThemeMode.DARK : ThemeMode.LIGHT;
			getContentPane().setBackground(baseBg());
			topPanel.setBackground(baseBg());
			centerPanel.setBackground(baseBg());
			bottomPanel.setBackground(baseBg());
			// Inline answer field styling
			answerField.setBackground(Color.WHITE);
			answerField.setForeground(Color.BLACK);
			infoPanel.setBackground(infoPanelBg());
			scoreLabel.setForeground(scoreTextColor());
			applyTextColors();
			// Repaint panels to update gradients
			titlePanel.repaint();
			gamePanel.repaint();
			if (questionPanel != null) questionPanel.repaint();
		});

		// Color scheme handler
        schemeComboBox.addActionListener(e -> {
			String sel = (String) schemeComboBox.getSelectedItem();
			if ("Purple".equals(sel)) currentScheme = ColorScheme.PURPLE;
			else if ("Green".equals(sel)) currentScheme = ColorScheme.GREEN;
			else if ("Sunset".equals(sel)) currentScheme = ColorScheme.SUNSET;
			else if ("Neon".equals(sel)) currentScheme = ColorScheme.NEON;
			else currentScheme = ColorScheme.BLUE;
			// Update backgrounds and repaint gradients
			getContentPane().setBackground(baseBg());
			topPanel.setBackground(baseBg());
			centerPanel.setBackground(baseBg());
			bottomPanel.setBackground(baseBg());
			// Inline answer field styling
			answerField.setBackground(Color.WHITE);
			answerField.setForeground(Color.BLACK);
			infoPanel.setBackground(infoPanelBg());
			scoreLabel.setForeground(scoreTextColor());
			titlePanel.repaint();
			gamePanel.repaint();
			if (questionPanel != null) questionPanel.repaint();
		});

		// Start a new game
        startNewGame();
    }

    private void openSettingsDialog() {
        JDialog dialog = new JDialog(this, "Settings", true);
        JPanel content = new JPanel(new GridBagLayout());
        content.setBackground(baseBg());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel themeLbl = new JLabel("Theme:");
        themeLbl.setFont(new Font("Arial", Font.BOLD, 18));
        themeLbl.setForeground(normalText());
        gbc.gridx = 0; gbc.gridy = 0;
        content.add(themeLbl, gbc);

        // Ensure combo exists and style it
        if (themeComboBox == null) {
            themeComboBox = new JComboBox<>(new String[]{"Light", "Dark"});
            themeComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        }
        gbc.gridx = 1; gbc.gridy = 0;
        content.add(themeComboBox, gbc);

        JLabel schemeLbl = new JLabel("Color Scheme:");
        schemeLbl.setFont(new Font("Arial", Font.BOLD, 18));
        schemeLbl.setForeground(normalText());
        gbc.gridx = 0; gbc.gridy = 1;
        content.add(schemeLbl, gbc);

        if (schemeComboBox == null) {
            schemeComboBox = new JComboBox<>(new String[]{"Blue", "Purple", "Green", "Sunset", "Neon"});
            schemeComboBox.setFont(new Font("Arial", Font.PLAIN, 18));
        }
        gbc.gridx = 1; gbc.gridy = 1;
        content.add(schemeComboBox, gbc);

        // Leaderboard filename controls
        JLabel fileLbl = new JLabel("Leaderboard File:");
        fileLbl.setFont(new Font("Arial", Font.BOLD, 16));
        fileLbl.setForeground(normalText());
        JTextField fileField = new JTextField(leaderboardFile, 18);
        fileField.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.anchor = GridBagConstraints.WEST;
        content.add(fileLbl, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.anchor = GridBagConstraints.WEST;
        content.add(fileField, gbc);

        JButton saveFileBtn = new JButton("Save Filename");
        saveFileBtn.setFont(new Font("Arial", Font.BOLD, 14));
        saveFileBtn.addActionListener(e -> {
            String newName = fileField.getText().trim();
            if (!newName.isEmpty()) {
                leaderboardFile = newName;
                loadLeaderboard();
                JOptionPane.showMessageDialog(dialog, "Leaderboard file updated to '" + leaderboardFile + "'", "Saved", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.WEST;
        content.add(saveFileBtn, gbc);

        JButton resetBtn = new JButton("Reset Leaderboard");
        resetBtn.setFont(new Font("Arial", Font.BOLD, 14));
        resetBtn.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(dialog, "Clear all leaderboard scores?", "Confirm Reset", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
            if (confirm == JOptionPane.YES_OPTION) {
                clearLeaderboard();
                saveLeaderboard();
                JOptionPane.showMessageDialog(dialog, "Leaderboard cleared.", "Done", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        gbc.gridx = 1; gbc.gridy = 4; gbc.anchor = GridBagConstraints.WEST;
        content.add(resetBtn, gbc);

        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("Arial", Font.BOLD, 16));
        closeBtn.addActionListener(e -> dialog.dispose());
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        content.add(closeBtn, gbc);

        dialog.setContentPane(content);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
    
    private boolean askPlayerName() {
        // Keep prompting until a non-empty name is provided or the user confirms cancel
        while (true) {
            String name = JOptionPane.showInputDialog(
                    this,
                    "Please enter your name:",
                    "Welcome to Arithmetic Game",
                    JOptionPane.QUESTION_MESSAGE);

            if (name == null) {
                int confirm = JOptionPane.showConfirmDialog(
                        this,
                        "Name is required to continue. Do you want to cancel?",
                        "Name Required",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    return false; // user chose to cancel
                } else {
                    continue; // ask again
                }
            }

            name = name.trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(
                        this,
                        "Please enter your name.",
                        "Name Required",
                        JOptionPane.ERROR_MESSAGE);
                continue; // ask again
            }

            playerName = name;
            return true;
        }
    }
    
    private void startNewGame() {
        // Reset timer
        timeLeft = 30;
        timerLabel.setText("Time: " + timeLeft);
        if (gameTimer != null) {
            gameTimer.restart();
        }
        
		// Reset score
		score = 0;
		scoreLabel.setText("Your Score: " + score);
        
        // Generate new question based on selected operation and difficulty
        generateQuestion();
        
        // Clear previous answer and result
        answerField.setText("");
        resultLabel.setText("");
        answerField.requestFocus();
        
        // Enable answer field and submit button
        answerField.setEnabled(true);
        submitButton.setEnabled(true);
    }
    
    private void generateQuestion() {
        String operation = (String) operationComboBox.getSelectedItem();
        String difficulty = (String) difficultyComboBox.getSelectedItem();
        
        int num1, num2;
        
        // Set number range based on difficulty
        switch (difficulty) {
            case "Easy":
                num1 = random.nextInt(10) + 1;  // 1-10
                num2 = random.nextInt(10) + 1;
                break;
            case "Medium":
                num1 = random.nextInt(50) + 1;  // 1-50
                num2 = random.nextInt(25) + 1;
                break;
            case "Hard":
            default:
                num1 = random.nextInt(100) + 1;  // 1-100
                num2 = random.nextInt(50) + 1;
                break;
        }
        
        // Note: clean division adjustment moved into the Division case below
        
        // Generate question based on operation
        String question;
        if (operation.equals("Mixed")) {
            int opChoice = random.nextInt(5);
            switch (opChoice) {
                case 0:  // Addition
                    question = num1 + " + " + num2 + " = ?";
                    correctAnswer = num1 + num2;
                    break;
                case 1:  // Subtraction
                    // Ensure positive result
                    if (num1 < num2) {
                        int temp = num1;
                        num1 = num2;
                        num2 = temp;
                    }
                    question = num1 + " - " + num2 + " = ?";
                    correctAnswer = num1 - num2;
                    break;
                case 2:  // Multiplication
                    question = num1 + " × " + num2 + " = ?";
                    correctAnswer = num1 * num2;
                    break;
                case 3:  // Division
                    // Ensure clean division
                    num1 = num1 * num2;
                    question = num1 + " ÷ " + num2 + " = ?";
                    correctAnswer = num1 / num2;
                    break;
                case 4:  // Modulo
                    question = num1 + " % " + num2 + " = ?";
                    correctAnswer = num1 % num2;
                    break;
                default:
                    question = num1 + " + " + num2 + " = ?";
                    correctAnswer = num1 + num2;
            }
        } else {
            switch (operation) {
                case "Addition":
                    question = num1 + " + " + num2 + " = ?";
                    correctAnswer = num1 + num2;
                    break;
                case "Subtraction":
                    // Ensure positive result
                    if (num1 < num2) {
                        int temp = num1;
                        num1 = num2;
                        num2 = temp;
                    }
                    question = num1 + " - " + num2 + " = ?";
                    correctAnswer = num1 - num2;
                    break;
                case "Multiplication":
                    question = num1 + " × " + num2 + " = ?";
                    correctAnswer = num1 * num2;
                    break;
                case "Division":
                    // Ensure clean division
                    num1 = num1 * num2;
                    question = num1 + " ÷ " + num2 + " = ?";
                    correctAnswer = num1 / num2;
                    break;
                case "Modulo":
                    question = num1 + " % " + num2 + " = ?";
                    correctAnswer = num1 % num2;
                    break;
                default:
                    question = num1 + " + " + num2 + " = ?";
                    correctAnswer = num1 + num2;
            }
        }
        
        if (questionPanel != null) {
            questionPanel.setQuestion(question);
        }
    }
    
    private void checkAnswer() {
        try {
            int userAnswer = Integer.parseInt(answerField.getText().trim());
            
            if (userAnswer == correctAnswer) {
                resultLabel.setText("Correct! 👍");
				resultLabel.setForeground(new Color(0, 128, 0));  // Green
				score += 10;
				scoreLabel.setText("Your Score: " + score);
                
                // Generate a new question after a short delay
                Timer delayTimer = new Timer(1000, e -> {
                    // Just generate a new question without resetting the game
                    generateQuestion();
                    answerField.setText("");
                    resultLabel.setText("");
                    answerField.requestFocus();
                });
                delayTimer.setRepeats(false);
                delayTimer.start();
            } else {
                resultLabel.setText("Wrong! The correct answer is " + correctAnswer);
                resultLabel.setForeground(Color.RED);
                
				// Deduct points for wrong answer
				score = Math.max(0, score - 5);
				scoreLabel.setText("Your Score: " + score);

                // Generate a new question after a short delay
                Timer delayTimerWrong = new Timer(1000, e2 -> {
                    generateQuestion();
                    answerField.setText("");
                    resultLabel.setText("");
                    answerField.requestFocus();
                });
                delayTimerWrong.setRepeats(false);
                delayTimerWrong.start();
            }
        } catch (NumberFormatException e) {
            resultLabel.setText("Please enter a valid number!");
            resultLabel.setForeground(Color.RED);
        }
    }
    
    private void updateTimer() {
        timeLeft--;
        timerLabel.setText("Time: " + timeLeft);
        
        if (timeLeft <= 0) {
            gameTimer.stop();
            answerField.setEnabled(false);
            submitButton.setEnabled(false);
            resultLabel.setText("Time's up! The answer was " + correctAnswer);
            resultLabel.setForeground(Color.RED);

            // Add score to leaderboard when time's up
            addScoreToLeaderboard();

            // Show final score then present options
            JOptionPane.showMessageDialog(this,
                "Time's Up! Your final score is: " + score,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE);

            Object[] options = {"Restart", "Leaderboard", "Title", "Exit"};
            int sel = JOptionPane.showOptionDialog(
                this,
                "What would you like to do?",
                "Next Action",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]
            );

            if (sel == 0) { // Restart
                score = 0;
                timeLeft = 30;
                startNewGame();
            } else if (sel == 1) { // Leaderboard
                showLeaderboard();
                // After viewing, go back to title
                ((CardLayout)getContentPane().getLayout()).show(getContentPane(), "title");
            } else if (sel == 2) { // Title
                ((CardLayout)getContentPane().getLayout()).show(getContentPane(), "title");
            } else if (sel == 3) { // Exit
                int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Confirm Exit",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
                );
                if (confirm == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else {
                    // If cancel exit, return to title
                    ((CardLayout)getContentPane().getLayout()).show(getContentPane(), "title");
                }
            } else {
                // Closed dialog: go to title
                ((CardLayout)getContentPane().getLayout()).show(getContentPane(), "title");
            }
        }
    }
    
    private void addScoreToLeaderboard() {
        // Add the new score without deleting previous history
        leaderboard.add(new LeaderboardEntry(playerName, score));
        
        // Sort by score (highest first)
        Collections.sort(leaderboard);
        
        // Save all scores to file
        saveLeaderboard();
    }

    // Clear leaderboard: remove entries and delete file
    private void clearLeaderboard() {
        leaderboard.clear();
        File file = new File(leaderboardFile);
        if (file.exists()) {
            // best-effort delete
            try { file.delete(); } catch (Exception ignored) {}
        }
    }
    
    private void showLeaderboard() {
        // Create a string with all leaderboard entries
        StringBuilder sb = new StringBuilder();
        sb.append("LEADERBOARD\n\n");
        
        if (leaderboard.isEmpty()) {
            sb.append("No scores yet!");
        } else {
            for (int i = 0; i < leaderboard.size(); i++) {
                LeaderboardEntry entry = leaderboard.get(i);
                sb.append(String.format("%d. %s: %d points\n", i + 1, entry.name, entry.score));
            }
        }
        
        // Show leaderboard in a dialog
        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(300, 300));
        
        JOptionPane.showMessageDialog(this, scrollPane, "Leaderboard", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void loadLeaderboard() {
        File file = new File(leaderboardFile);
        if (!file.exists()) {
            return; // No leaderboard file yet
        }
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            leaderboard = (ArrayList<LeaderboardEntry>) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            // If there's an error, start with an empty leaderboard
            leaderboard = new ArrayList<>();
        }
    }
    
    private void saveLeaderboard() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(leaderboardFile))) {
            oos.writeObject(leaderboard);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        // Use the system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Create and show the game window
        SwingUtilities.invokeLater(() -> {
            ArithmeticGame game = new ArithmeticGame();
            game.setVisible(true);
        });
    }
}