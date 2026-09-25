import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class QuizApp extends JFrame implements ActionListener, ItemListener {

    String userName = "";
    String selectedDomain = "";

    String[] domains = {"Science", "Geography", "History"};

    String[] scienceQuestions = {
            "What is the chemical formula for water?",
            "Which gas is most abundant in Earth's atmosphere?",
            "What is the unit of electrical resistance?",
            "Which organ in the human body is responsible for pumping blood?",
            "What is the speed of light in vacuum (approximate)?",
            "What process plants use to make food using sunlight?",
            "What type of lens is used to correct nearsightedness?",
            "Which particle in an atom has a positive charge?",
            "What is the boiling point of water at sea level (°C)?",
            "Which force keeps planets in orbit around the Sun?"
    };

    String[][] scienceOptions = {
            {"H2O", "CO2", "O2", "NaCl"},
            {"Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen"},
            {"Ohm", "Volt", "Ampere", "Watt"},
            {"Brain", "Lungs", "Heart", "Liver"},
            {"300,000 km/s", "150,000 km/s", "450,000 km/s", "600,000 km/s"},
            {"Photosynthesis", "Respiration", "Transpiration", "Osmosis"},
            {"Convex lens", "Concave lens", "Cylindrical lens", "Plane lens"},
            {"Electron", "Neutron", "Proton", "Photon"},
            {"90°C", "100°C", "80°C", "120°C"},
            {"Magnetic force", "Electrostatic force", "Gravitational force", "Nuclear force"}
    };

    int[] scienceAnswers = {0, 1, 0, 2, 0, 0, 1, 2, 1, 2};

    String[] geographyQuestions = {
            "What is the longest river in the world?",
            "Which country has the largest population?",
            "What is the capital of Australia?",
            "Which continent is the Sahara Desert located in?",
            "Mount Everest is part of which mountain range?",
            "Which ocean is the largest by surface area?",
            "Which country is known as the Land of the Rising Sun?",
            "What is the largest island in the world?",
            "Which country has the most volcanoes?",
            "The Great Barrier Reef is located in which country?"
    };

    String[][] geographyOptions = {
            {"Amazon", "Nile", "Yangtze", "Mississippi"},
            {"India", "USA", "China", "Russia"},
            {"Sydney", "Melbourne", "Canberra", "Brisbane"},
            {"Asia", "Africa", "Europe", "South America"},
            {"Andes", "Rockies", "Himalayas", "Alps"},
            {"Atlantic", "Indian", "Pacific", "Arctic"},
            {"Japan", "China", "South Korea", "Thailand"},
            {"Greenland", "New Guinea", "Madagascar", "Borneo"},
            {"Indonesia", "Japan", "Philippines", "Italy"},
            {"USA", "Australia", "New Zealand", "Fiji"}
    };

    int[] geographyAnswers = {1, 2, 2, 1, 2, 2, 0, 0, 0, 1};

    String[] historyQuestions = {
            "Who was the first President of the United States?",
            "In which year did World War II end?",
            "What was the name of the ship on which the Pilgrims traveled to America?",
            "Who discovered America?",
            "Which empire was ruled by Genghis Khan?",
            "Where was the Renaissance movement centered?",
            "What wall divided Berlin from 1961 to 1989?",
            "Who was known as the Maid of Orleans?",
            "Which ancient civilization built the Machu Picchu?",
            "Who was the British monarch during the American Revolution?"
    };

    String[][] historyOptions = {
            {"George Washington", "Thomas Jefferson", "Abraham Lincoln", "John Adams"},
            {"1943", "1944", "1945", "1946"},
            {"Santa Maria", "Mayflower", "Endeavour", "Beagle"},
            {"Christopher Columbus", "Vasco da Gama", "Ferdinand Magellan", "James Cook"},
            {"Roman Empire", "Ottoman Empire", "Mongol Empire", "British Empire"},
            {"Florence", "Paris", "London", "Venice"},
            {"Great Wall of China", "Berlin Wall", "Hadrian's Wall", "Western Wall"},
            {"Joan of Arc", "Marie Curie", "Queen Elizabeth I", "Catherine the Great"},
            {"Aztec", "Inca", "Mayan", "Olmec"},
            {"George III", "Victoria", "Elizabeth I", "Henry VIII"}
    };

    int[] historyAnswers = {0, 2, 1, 0, 2, 0, 1, 0, 1, 0};

    String[] questions;
    String[][] options;
    int[] answers;

    int[] userAnswers;

    JLabel questionLabel;
    JLabel questionNumberLabel;
    JLabel timerLabel;
    ButtonGroup bg;
    JButton nextButton;
    JButton previousButton;
    JButton skipButton;
    JLabel scoreLabel;

    int currentQuestion = 0;
    Timer timer;
    int timeLeft = 60;

    JPanel[] optionPanels = new JPanel[4];
    JRadioButton[] optionButtons = new JRadioButton[4];

    // Splash window frame
    private JFrame splashFrame;

    // Background image for quiz window
    private Image backgroundImage;

    public QuizApp() {
        // Load background image from resource or file - adjust path as needed
        try {
            URL bgUrl = getClass().getResource("/background.jpg");
            if (bgUrl != null) {
                backgroundImage = ImageIO.read(bgUrl);
            } else {
                backgroundImage = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            backgroundImage = null;
        }

        showSplashWindow();
    }

    private void showSplashWindow() {
        splashFrame = new JFrame("Welcome");
        splashFrame.setSize(600, 350);
        splashFrame.setLocationRelativeTo(null);
        splashFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        splashFrame.setLayout(new BorderLayout(20, 20));

        // Get screen size for dynamic font scaling
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenHeight = screenSize.getHeight();

        // Define base height for reference (e.g., 1080p screen height)
        double baseHeight = 1080.0;

        // Calculate scaling factor capped between 0.7 and 1.3 for reasonable font sizes
        double scaleFactor = Math.min(Math.max(screenHeight / baseHeight, 0.7), 1.3);

        int titleFontSize = (int)(40 * scaleFactor);
        int descFontSize = (int)(20 * scaleFactor);
        int buttonFontSize = (int)(22 * scaleFactor);

        JLabel titleLabel = new JLabel("QUIZ APPLICATION", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, titleFontSize));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 10, 10, 10));
        splashFrame.add(titleLabel, BorderLayout.NORTH);

        JTextArea descriptionArea = new JTextArea(
                "Get ready to test your knowledge, challenge yourself, and have some fun! " +
                "You're here to learn something new.\n\nChoose a category, pick a difficulty level, " +
                "and let the quiz begin. Good luck — your brain is about to get a workout! 🧠✨");
        descriptionArea.setFont(new Font("Serif", Font.BOLD, descFontSize));
        descriptionArea.setBackground(splashFrame.getBackground());
        descriptionArea.setEditable(false);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(15, 40, 15, 40));
        splashFrame.add(descriptionArea, BorderLayout.CENTER);

        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, buttonFontSize));
        startButton.setBackground(new Color(0, 120, 215));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setPreferredSize(new Dimension((int)(140 * scaleFactor), (int)(60 * scaleFactor)));
        JPanel btnPanel = new JPanel();
        btnPanel.add(startButton);
        btnPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, (int)(30 * scaleFactor), 0));
        splashFrame.add(btnPanel, BorderLayout.SOUTH);

        startButton.addActionListener(e -> {
            splashFrame.dispose();
            proceedToUserInfoAndQuiz();
        });

        splashFrame.setVisible(true);
    }

    private void proceedToUserInfoAndQuiz() {
        if (!showUserInfoAndDomainSelection()) {
            System.exit(0);
        }

        loadDomainQuestions(selectedDomain);

        JOptionPane.showMessageDialog(null,
                "Welcome, " + userName + "! You have selected the " + selectedDomain + " quiz. Good luck!",
                "Welcome",
                JOptionPane.INFORMATION_MESSAGE);

        userAnswers = new int[questions.length];
        for (int i = 0; i < userAnswers.length; i++) userAnswers[i] = -1;

        initializeQuizUI();
    }

    private void initializeQuizUI() {
        setTitle(selectedDomain + " Quiz - Class 10");
        setSize(720, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout(0, 0));
        setContentPane(backgroundPanel);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        timerLabel = new JLabel("Time left: 01:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerLabel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        topPanel.add(timerLabel, BorderLayout.EAST);
        backgroundPanel.add(topPanel, BorderLayout.NORTH);

        questionNumberLabel = new JLabel();
        questionNumberLabel.setFont(new Font("Arial", Font.BOLD, 16));
        questionNumberLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        questionNumberLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 5, 20));
        questionNumberLabel.setForeground(Color.WHITE);

        questionLabel = new JLabel();
        questionLabel.setFont(new Font("Arial", Font.BOLD, 18));
        questionLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        questionLabel.setForeground(Color.WHITE);

        JPanel questionPanel = new JPanel(new BorderLayout());
        questionPanel.setOpaque(false);
        questionPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE, 2),
                "Question", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), Color.WHITE));
        questionPanel.add(questionLabel, BorderLayout.CENTER);

        JPanel questionContainer = new JPanel(new BorderLayout());
        questionContainer.setOpaque(false);
        questionContainer.setBorder(BorderFactory.createEmptyBorder(10, 20, 0, 20));
        questionContainer.add(questionNumberLabel, BorderLayout.NORTH);
        questionContainer.add(questionPanel, BorderLayout.CENTER);

        JPanel optionsPanel = new JPanel(new GridLayout(4, 1, 8, 8));
        optionsPanel.setOpaque(false);
        optionsPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.WHITE, 2),
                "Options", TitledBorder.LEFT, TitledBorder.TOP, new Font("Arial", Font.BOLD, 16), Color.WHITE));

        bg = new ButtonGroup();
        optionButtons = new JRadioButton[4];
        optionPanels = new JPanel[4];
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(new Font("Arial", Font.PLAIN, 16));
            optionButtons[i].addItemListener(this);
            optionButtons[i].setOpaque(false);
            optionButtons[i].setForeground(Color.WHITE);

            JPanel optionPanel = new JPanel(new BorderLayout());
            optionPanel.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 150), 2));
            optionPanel.setBackground(new Color(255, 255, 255, 40));
            optionPanel.add(optionButtons[i], BorderLayout.WEST);
            optionPanel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            optionPanel.setOpaque(true);

            final int idx = i;
            optionPanel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    optionPanel.setBackground(new Color(255, 255, 255, 100));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    if (optionButtons[idx].isSelected()) {
                        optionPanel.setBackground(new Color(255, 255, 255, 150));
                    } else {
                        optionPanel.setBackground(new Color(255, 255, 255, 40));
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    optionButtons[idx].setSelected(true);
                }
            });

            optionButtons[i].addActionListener(ev -> {
                for (int j = 0; j < 4; j++) {
                    if (optionButtons[j].isSelected()) {
                        optionPanels[j].setBackground(new Color(255, 255, 255, 150));
                    } else {
                        optionPanels[j].setBackground(new Color(255, 255, 255, 40));
                    }
                }
                nextButton.setEnabled(true);
            });

            optionPanels[i] = optionPanel;
            bg.add(optionButtons[i]);
            optionsPanel.add(optionPanel);
        }

        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        navPanel.setOpaque(false);

        previousButton = new JButton("← Previous");
        previousButton.setFont(new Font("Arial", Font.BOLD, 16));
        previousButton.addActionListener(this);
        previousButton.setEnabled(false);
        navPanel.add(previousButton);

        skipButton = new JButton("Skip");
        skipButton.setFont(new Font("Arial", Font.BOLD, 16));
        skipButton.addActionListener(this);
        navPanel.add(skipButton);

        nextButton = new JButton("Next →");
        nextButton.setFont(new Font("Arial", Font.BOLD, 16));
        nextButton.addActionListener(this);
        nextButton.setEnabled(false);
        navPanel.add(nextButton);

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 16));
        scoreLabel.setForeground(Color.WHITE);
        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        scorePanel.setOpaque(false);
        scorePanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        scorePanel.add(scoreLabel);

        JPanel centerPanel = new JPanel(new BorderLayout(0, 15));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        centerPanel.add(questionContainer, BorderLayout.NORTH);
        centerPanel.add(optionsPanel, BorderLayout.CENTER);
        centerPanel.add(navPanel, BorderLayout.SOUTH);

        backgroundPanel.add(centerPanel, BorderLayout.CENTER);
        backgroundPanel.add(scorePanel, BorderLayout.PAGE_END);

        timer = new Timer(1000, e -> {
            timeLeft--;
            updateTimerLabel();
            if (timeLeft <= 0) {
                timer.stop();
                JOptionPane.showMessageDialog(this,
                        "Time's up for this question! Moving to next.",
                        "Time Up", JOptionPane.INFORMATION_MESSAGE);
                autoMoveNext();
            }
        });

        loadQuestion(currentQuestion);
        timer.start();
        setVisible(true);
    }

    // Custom JPanel to paint a background image or fallback to gradient
    class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            } else {
                Graphics2D g2d = (Graphics2D) g;
                int width = getWidth();
                int height = getHeight();
                Color color1 = new Color(0, 102, 204);
                Color color2 = new Color(102, 178, 255);
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, height, color2);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, width, height);
            }
        }
    }

    private boolean showUserInfoAndDomainSelection() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double screenHeight = screenSize.getHeight();
        double baseHeight = 1080.0;
        double scaleFactor = Math.min(Math.max(screenHeight / baseHeight, 0.7), 1.3);

        JLabel nameLabel = new JLabel("Enter your name:");
        nameLabel.setFont(new Font("Arial", Font.BOLD, (int)(20 * scaleFactor)));
        JTextField nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, (int)(18 * scaleFactor)));

        JLabel domainLabel = new JLabel("Select quiz domain:");
        domainLabel.setFont(new Font("Arial", Font.BOLD, (int)(20 * scaleFactor)));
        JComboBox<String> domainComboBox = new JComboBox<>(domains);
        domainComboBox.setFont(new Font("Arial", Font.PLAIN, (int)(18 * scaleFactor)));

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder((int)(20 * scaleFactor), (int)(30 * scaleFactor),
                (int)(20 * scaleFactor), (int)(30 * scaleFactor)));

        panel.add(nameLabel);
        panel.add(Box.createRigidArea(new Dimension(0, (int)(10 * scaleFactor))));
        panel.add(nameField);
        panel.add(Box.createRigidArea(new Dimension(0, (int)(25 * scaleFactor))));
        panel.add(domainLabel);
        panel.add(Box.createRigidArea(new Dimension(0, (int)(10 * scaleFactor))));
        panel.add(domainComboBox);
        panel.add(Box.createRigidArea(new Dimension(0, (int)(15 * scaleFactor))));

        nameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, nameField.getPreferredSize().height));
        domainComboBox.setMaximumSize(new Dimension(Integer.MAX_VALUE, domainComboBox.getPreferredSize().height));

        while (true) {
            int result = JOptionPane.showConfirmDialog(null, panel, "User Information and Domain Selection", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result == JOptionPane.OK_OPTION) {
                String nameInput = nameField.getText().trim();
                if (nameInput.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Name cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                userName = nameInput;
                selectedDomain = (String) domainComboBox.getSelectedItem();
                if (selectedDomain == null) {
                    JOptionPane.showMessageDialog(null, "Please select a domain.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    continue;
                }
                return true;
            } else {
                return false;
            }
        }
    }

    private void loadDomainQuestions(String domain) {
        switch (domain) {
            case "Science":
                questions = scienceQuestions;
                options = scienceOptions;
                answers = scienceAnswers;
                break;
            case "Geography":
                questions = geographyQuestions;
                options = geographyOptions;
                answers = geographyAnswers;
                break;
            case "History":
                questions = historyQuestions;
                options = historyOptions;
                answers = historyAnswers;
                break;
            default:
                questions = scienceQuestions;
                options = scienceOptions;
                answers = scienceAnswers;
                break;
        }
    }

    private void updateTimerLabel() {
        int minutes = timeLeft / 60;
        int seconds = timeLeft % 60;
        timerLabel.setText(String.format("Time left: %02d:%02d", minutes, seconds));
    }

    private void resetTimer() {
        timeLeft = 60;
        updateTimerLabel();
        if (!timer.isRunning()) {
            timer.start();
        }
    }

    private void autoMoveNext() {
        if (userAnswers[currentQuestion] == -1) {
            userAnswers[currentQuestion] = -1;
        }
        if (currentQuestion < questions.length - 1) {
            currentQuestion++;
            loadQuestion(currentQuestion);
            resetTimer();
        } else {
            showResultAndExit();
        }
        updateScoreLabel();
    }

    private void loadQuestion(int index) {
        bg.clearSelection();
        for (int i = 0; i < 4; i++) {
            optionPanels[i].setBackground(new Color(255, 255, 255, 40));
        }
        nextButton.setEnabled(false);

        questionNumberLabel.setText("Question " + (index + 1) + " of " + questions.length);
        questionLabel.setText("<html><body style='width: 620px'>" + questions[index] + "</body></html>");

        for (int i = 0; i < 4; i++) {
            optionButtons[i].setText(options[index][i]);
        }

        int selected = userAnswers[index];
        if (selected >= 0 && selected < optionButtons.length) {
            optionButtons[selected].setSelected(true);
            optionPanels[selected].setBackground(new Color(255, 255, 255, 150));
            nextButton.setEnabled(true);
        } else {
            bg.clearSelection();
            nextButton.setEnabled(false);
        }

        previousButton.setEnabled(index > 0);
        skipButton.setEnabled(index < questions.length - 1);
        nextButton.setText(index == questions.length - 1 ? "Submit" : "Next →");
    }

    private void updateScoreLabel() {
        int score = 0;
        for (int i = 0; i < userAnswers.length; i++) {
            if (userAnswers[i] == answers[i]) score++;
        }
        scoreLabel.setText("Score: " + score);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            int selectedOption = getSelectedOption();
            if (selectedOption == -1) {
                JOptionPane.showMessageDialog(this, "Please select an answer or skip the question.", "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }

            userAnswers[currentQuestion] = selectedOption;

            if (currentQuestion < questions.length - 1) {
                currentQuestion++;
                loadQuestion(currentQuestion);
            } else {
                timer.stop();
                showResultAndExit();
            }
        } else if (e.getSource() == previousButton) {
            if (currentQuestion > 0) {
                currentQuestion--;
                loadQuestion(currentQuestion);
            }
        } else if (e.getSource() == skipButton) {
            userAnswers[currentQuestion] = -1;
            if (currentQuestion < questions.length - 1) {
                currentQuestion++;
                loadQuestion(currentQuestion);
            } else {
                timer.stop();
                showResultAndExit();
            }
        }
        updateScoreLabel();
    }

    private int getSelectedOption() {
        for (int i = 0; i < optionButtons.length; i++) {
            if (optionButtons[i].isSelected()) return i;
        }
        return -1;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            nextButton.setEnabled(true);
        }
    }

    private void showResultAndExit() {
        int score = 0;
        for (int i = 0; i < userAnswers.length; i++) {
            if (userAnswers[i] == answers[i]) score++;
        }
        JOptionPane.showMessageDialog(this,
                "Quiz Completed!\nYour Score: " + score + " / " + questions.length,
                "Result", JOptionPane.INFORMATION_MESSAGE);
        System.exit(0);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> new QuizApp());
    }
}