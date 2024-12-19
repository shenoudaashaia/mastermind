package mastermind;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Mastermind extends JFrame {

    private final String[] colors = {"Red", "Green", "Blue", "Yellow", "Orange", "Purple"};
    private final String[] secretCode = new String[4];
    private int attemptsLeft = 10;


    private ArrayList<JComboBox<String>> guessComboBoxes = new ArrayList<>();
    private JButton submitButton;
    private JTextArea feedbackArea;
    private JPanel feedbackPanel;
    private JPanel colorDisplayPanel;
    private JLabel attemptsLabel;
    private JButton resetButton;

    public Mastermind() {

        setTitle("Mastermind Game");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 240, 240));


        generateSecretCode();

        JPanel guessPanel = new JPanel();
        guessPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        guessPanel.setBackground(new Color(240, 240, 240));
        for (int i = 0; i < 4; i++) {
            JComboBox<String> comboBox = new JComboBox<>(colors);
            comboBox.setPreferredSize(new Dimension(100, 40));
            guessComboBoxes.add(comboBox);
            guessPanel.add(comboBox);
        }
        add(guessPanel);


        submitButton = new JButton("Submit Guess");
        submitButton.setBackground(new Color(50, 150, 255));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.addActionListener(new SubmitButtonListener());
        submitButton.setPreferredSize(new Dimension(150, 40));
        add(submitButton);

        feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);
        feedbackArea.setFont(new Font("Arial", Font.PLAIN, 14));
        feedbackArea.setBackground(Color.LIGHT_GRAY);
        feedbackArea.setLineWrap(true);
        feedbackArea.setWrapStyleWord(true);
        feedbackArea.setPreferredSize(new Dimension(500, 100));
        add(new JScrollPane(feedbackArea));


        colorDisplayPanel = new JPanel();
        colorDisplayPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        colorDisplayPanel.setBackground(new Color(240, 240, 240));
        for (int i = 0; i < 4; i++) {
            JLabel label = new JLabel();
            label.setPreferredSize(new Dimension(50, 50));
            label.setHorizontalAlignment(SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 30));
            label.setOpaque(true);
            label.setBackground(Color.GRAY);
            colorDisplayPanel.add(label);
        }
        add(colorDisplayPanel);


        attemptsLabel = new JLabel("Attempts Left: " + attemptsLeft);
        attemptsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        attemptsLabel.setForeground(Color.RED);
        add(attemptsLabel);


        resetButton = new JButton("Reset Game");
        resetButton.setBackground(new Color(255, 100, 100));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFocusPainted(false);
        resetButton.setFont(new Font("Arial", Font.BOLD, 16));
        resetButton.addActionListener(new ResetButtonListener());
        resetButton.setPreferredSize(new Dimension(150, 40));
        add(resetButton);

        setVisible(true);
    }



    private void generateSecretCode() {
        Random rand = new Random();
        for (int i = 0; i < 4; i++) {
            secretCode[i] = colors[rand.nextInt(colors.length)];
        }
        System.out.println("Secret Code: " + Arrays.toString(secretCode)); 
    }


    private String evaluateGuess() {
        StringBuilder feedback = new StringBuilder();
        String[] guess = new String[4];
        for (int i = 0; i < 4; i++) {
            guess[i] = (String) guessComboBoxes.get(i).getSelectedItem();
        }

        int correctPosition = 0, correctColor = 0;
        boolean[] secretUsed = new boolean[4];  
        boolean[] guessUsed = new boolean[4];  

        // Check for correct positions
        for (int i = 0; i < 4; i++) {
            if (secretCode[i].equals(guess[i])) {
                correctPosition++;
                secretUsed[i] = true;
                guessUsed[i] = true;  
            }
        }

        for (int i = 0; i < 4; i++) {
            if (!guessUsed[i]) {
                for (int j = 0; j < 4; j++) {
                    if (!secretUsed[j] && secretCode[j].equals(guess[i])) {
                        correctColor++;
                        secretUsed[j] = true;  
                        break;
                    }
                }
            }
        }


        feedback.append("Correct positions: ").append(correctPosition).append("\n");
        feedback.append("Correct colors but wrong positions: ").append(correctColor).append("\n");

        if (correctPosition == 4) {
            feedback.append("Congratulations! You guessed the correct code!");
        } else if (attemptsLeft == 0) {
            feedback.append("Game Over! You've used all your attempts.\nThe secret code was: ");
            feedback.append(Arrays.toString(secretCode));
            revealSecretCode();  
        }

]
        updateFeedbackPanel(correctPosition, correctColor);

        return feedback.toString();
    }

 
    private void updateFeedbackPanel(int correctPosition, int correctColor) {
   
        Component[] labels = colorDisplayPanel.getComponents();
        for (Component label : labels) {
            JLabel jLabel = (JLabel) label;
            jLabel.setBackground(Color.GRAY);
        }

        for (int i = 0; i < 4; i++) {
            JLabel jLabel = (JLabel) labels[i];
            if (correctPosition > 0) {
                jLabel.setBackground(Color.BLACK);
                correctPosition--;
            } else if (correctColor > 0) {
                jLabel.setBackground(Color.WHITE); 
                correctColor--;
            }
        }
    }


    private void revealSecretCode() {
        Component[] labels = colorDisplayPanel.getComponents();
        for (int i = 0; i < 4; i++) {
            JLabel jLabel = (JLabel) labels[i];
            String color = secretCode[i];
            switch (color) {
                case "Red":
                    jLabel.setBackground(Color.RED);

break;
                case "Green":
                    jLabel.setBackground(Color.GREEN);
                    break;
                case "Blue":
                    jLabel.setBackground(Color.BLUE);
                    break;
                case "Yellow":
                    jLabel.setBackground(Color.YELLOW);
                    break;
                case "Orange":
                    jLabel.setBackground(Color.ORANGE);
                    break;
                case "Purple":
                    jLabel.setBackground(Color.MAGENTA);
                    break;
            }
        }
    }

    private class SubmitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (attemptsLeft > 0) {
                String feedback = evaluateGuess();
                feedbackArea.setText(feedback);
                attemptsLeft--;
                attemptsLabel.setText("Attempts Left: " + attemptsLeft);
            }
            if (attemptsLeft == 0) {
                submitButton.setEnabled(false);
            }
        }
    }


    private class ResetButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            attemptsLeft = 10;
            generateSecretCode();
            guessComboBoxes.forEach(comboBox -> comboBox.setSelectedIndex(0));
            feedbackArea.setText("");
            submitButton.setEnabled(true);
            attemptsLabel.setText("Attempts Left: " + attemptsLeft);

            // Reset the color display
            Component[] labels = colorDisplayPanel.getComponents();
            for (Component label : labels) {
                JLabel jLabel = (JLabel) label;
                jLabel.setBackground(Color.GRAY);
            }
        }
    }

    public static void main(String[] args) {
        new Mastermind();
    }
}
