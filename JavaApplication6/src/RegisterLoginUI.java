import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class RegisterLoginUI extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JTextField loginEmailField;
    private JPasswordField loginPasswordField;

    public RegisterLoginUI() {
        setTitle("Register & Login");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel bgPanel = new JPanel();
        bgPanel.setBackground(new Color(79, 142, 220));
        bgPanel.setLayout(new GridBagLayout());

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setOpaque(false);

        JPanel registerPanel = createRegisterPanel();
        JPanel loginPanel = createLoginPanel();

        mainPanel.add(registerPanel, "register");
        mainPanel.add(loginPanel, "login");

        bgPanel.add(mainPanel, new GridBagConstraints());
        setContentPane(bgPanel);

        cardLayout.show(mainPanel, "login");
    }

    private JPanel createRegisterPanel() {
        JPanel panel = containerPanel(550);

        JLabel title = new JLabel("Register");
        title.setFont(new Font("Poppins", Font.BOLD, 32));
        title.setForeground(new Color(36, 98, 204));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(title);

        panel.add(Box.createRigidArea(new Dimension(0, 18)));

        JTextField fNameField = input(panel, "First Name");
        JTextField lNameField = input(panel, "Last Name");
        JTextField emailField = input(panel, "Email");
        JPasswordField passwordField = passwordInput(panel, "Password");
        JPasswordField confirmPasswordField = passwordInput(panel, "Confirm Password");

        panel.add(Box.createRigidArea(new Dimension(0, 14)));

        JButton signUpBtn = new JButton("Sign Up");
        stylizeButton(signUpBtn);
        panel.add(signUpBtn);

        panel.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel haveAccount = new JLabel("Already have an account?");
        haveAccount.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(haveAccount);

        JButton signInSwitch = linkButton("Sign In");
        panel.add(signInSwitch);

        signInSwitch.addActionListener(e -> cardLayout.show(mainPanel, "login"));

        signUpBtn.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            String firstName = fNameField.getText().trim();
            String lastName = lNameField.getText().trim();

            if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please fill in all required fields!", "Registration Error", JOptionPane.ERROR_MESSAGE);
            } else if (!email.matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {
                JOptionPane.showMessageDialog(panel, "Only valid Gmail addresses are accepted!", "Registration Error", JOptionPane.ERROR_MESSAGE);
            } else if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(panel, "Passwords do not match!", "Registration Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try (Connection conn = DBUtil.getConnection()) {
                    // Check if email already exists
                    PreparedStatement ps = conn.prepareStatement("SELECT id FROM users WHERE email = ?");
                    ps.setString(1, email);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(panel, "Email is already registered!", "Registration Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    // Hash the password
                    String hashedPassword = hashPassword(password);
                    // Insert new user
                    ps = conn.prepareStatement("INSERT INTO users (email, password, firstName, lastName) VALUES (?, ?, ?, ?)");
                    ps.setString(1, email);
                    ps.setString(2, hashedPassword); // Store the hashed password
                    ps.setString(3, firstName);
                    ps.setString(4, lastName);
                    ps.executeUpdate();
                    JOptionPane.showMessageDialog(panel, "Registration successful! Please sign in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    cardLayout.show(mainPanel, "login");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Database error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = containerPanel(320);

        JLabel title = new JLabel("Sign In");
        title.setFont(new Font("Poppins", Font.BOLD, 32));
        title.setForeground(new Color(36, 98, 204));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(title);

        panel.add(Box.createRigidArea(new Dimension(0, 18)));

        loginEmailField = input(panel, "Email");
        loginPasswordField = passwordInput(panel, "Password");

        panel.add(Box.createRigidArea(new Dimension(0, 14)));

        JButton signInBtn = new JButton("Sign In");
        stylizeButton(signInBtn);
        panel.add(signInBtn);

        signInBtn.addActionListener(e -> {
            String email = loginEmailField.getText().trim();
            String password = new String(loginPasswordField.getPassword());
            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(panel, "Please enter Email and Password!", "Login Error", JOptionPane.ERROR_MESSAGE);
            } else {
                try (Connection conn = DBUtil.getConnection()) {
                    PreparedStatement ps = conn.prepareStatement("SELECT firstName, password FROM users WHERE email = ?");
                    ps.setString(1, email);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        String dbPassword = rs.getString("password");
                        String firstName = rs.getString("firstName");
                        // Hash the entered password and compare
                        if (hashPassword(password).equals(dbPassword)) {
                            // Open the homepage and pass the first name
                            new Homepage(firstName).setVisible(true);
                            this.dispose(); // Close the login window
                        } else {
                            JOptionPane.showMessageDialog(panel, "Incorrect password!", "Login Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(panel, "Invalid email!", "Login Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(panel, "Database error: " + ex.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        panel.add(Box.createRigidArea(new Dimension(0, 12)));

        JLabel noAccount = new JLabel("Don't have an account yet?");
        noAccount.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(noAccount);

        JButton signUpSwitch = linkButton("Sign Up");
        panel.add(signUpSwitch);

        signUpSwitch.addActionListener(e -> cardLayout.show(mainPanel, "register"));

        return panel;
    }

    private JPanel containerPanel(int boxHeight) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(30, 30, 30, 30),
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true)
        ));
        panel.setBackground(new Color(255, 255, 255, 245));
        int boxWidth = 380;
        panel.setPreferredSize(new Dimension(boxWidth, boxHeight));
        panel.setMaximumSize(new Dimension(boxWidth, boxHeight));
        panel.setMinimumSize(new Dimension(boxWidth, boxHeight));
        return panel;
    }

    private JTextField input(JPanel panel, String placeholder) {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setOpaque(false);
        JTextField textField = new JTextField();
        textField.setFont(new Font("Poppins", Font.PLAIN, 18));
        textField.setBorder(BorderFactory.createTitledBorder(placeholder));
        inputPanel.add(textField, BorderLayout.CENTER);
        inputPanel.setMaximumSize(new Dimension(320, 52));
        panel.add(inputPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        return textField;
    }

    private JPasswordField passwordInput(JPanel panel, String placeholder) {
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.setOpaque(false);
        JPasswordField passwordField = new JPasswordField();
        passwordField.setFont(new Font("Poppins", Font.PLAIN, 18));
        passwordField.setBorder(BorderFactory.createTitledBorder(placeholder));
        inputPanel.add(passwordField, BorderLayout.CENTER);
        inputPanel.setMaximumSize(new Dimension(320, 52));
        panel.add(inputPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 8)));
        return passwordField;
    }

    private void stylizeButton(JButton btn) {
        btn.setBackground(new Color(36, 98, 204));
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Poppins", Font.BOLD, 20));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(200, 40));
    }

    private JButton linkButton(String text) {
        JButton btn = new JButton(text);
        btn.setForeground(new Color(36, 98, 204));
        btn.setFont(new Font("Poppins", Font.BOLD, 16));
        btn.setBackground(null);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(120, 30));
        return btn;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new RegisterLoginUI().setVisible(true));
    }
}


