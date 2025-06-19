import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Homepage extends JFrame {
    private static final int TIMEOUT = 1800; // 30 minutes
    private long lastActivity;
    private int userId; // Add userId field

    public Homepage(String userName, int userId) { // Modify constructor to accept userId
        this.userId = userId; // Initialize userId
        lastActivity = System.currentTimeMillis();

        setTitle("Homepage");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(new Color(79, 142, 220));

        // Top panel contains header and navigation stacked vertically
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));

        // Header: logo + title horizontally
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT));
        header.setBackground(new Color(67, 147, 227));

        JLabel logo = new JLabel(new ImageIcon("logoghapon.png"));
        header.add(logo);

        JLabel title = new JLabel("<html>University of Bohol<br>Guidance Center</html>");
        title.setFont(new Font("Poppins", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        header.add(title);

        // Navigation panel below header (horizontal buttons)
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        nav.setBackground(new Color(67, 147, 227));

        String[] navItems = {"Home", "Appointments", "Guidance Counselor", "Log Out"};
        for (String item : navItems) {
            JButton button = new JButton(item);
            // Make nav buttons flat and blend with header bg
            button.setBackground(new Color(67, 147, 227));
            button.setForeground(Color.WHITE);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            button.addActionListener(new NavButtonListener(item));
            nav.add(button);
        }

        topPanel.add(header);
        topPanel.add(nav);

        // Main content (2 columns)
        JPanel mainContent = new JPanel(new GridLayout(1, 2));
        mainContent.setBackground(Color.WHITE);

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        leftPanel.add(createLabel("Welcome", 24, true));
        leftPanel.add(createLabel("UBians!", 24, true));
        leftPanel.add(createLabel("<html>University of Bohol a premier university transforming lives for a great future.<br>Anchored on: SCHOLARSHIP, CHARACTER, SERVICE.</html>", 14, false));
        leftPanel.add(Box.createVerticalStrut(20));
        leftPanel.add(createLabel("<html>The University seal bears the symbol of the fervency of the<br>University of Bohol to be faithful to her mission of<br>providing holistic education to the students.<br><br>At the center is the burning flame of knowledge anchored on<br>three stripes of red, blue and yellow, the trinity of<br>virtues, for which the University of Bohol<br>stands for: Scholarship, Character and Service.</html>", 14, false));

        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(new JLabel(new ImageIcon("logonapod.jpg")), BorderLayout.CENTER);

        mainContent.add(leftPanel);
        mainContent.add(rightPanel);

        // Footer
        JPanel footer = new JPanel();
        footer.setBackground(new Color(67, 147, 227));
        footer.add(new JLabel("2024 Student Personnel Services. All rights reserved.", SwingConstants.CENTER));

        // Assemble overall layout
        container.add(topPanel, BorderLayout.NORTH);       // header + nav stacked vertically at top
        container.add(mainContent, BorderLayout.CENTER);   // main content center
        container.add(footer, BorderLayout.SOUTH);         // footer bottom

        setContentPane(container);
        setVisible(true);
    }

    private JLabel createLabel(String text, int fontSize, boolean bold) {
        JLabel label = new JLabel(text);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setForeground(Color.BLACK);
        label.setFont(new Font("Poppins", bold ? Font.BOLD : Font.PLAIN, fontSize));
        return label;
    }

    private class NavButtonListener implements ActionListener {
        private final String action;

        public NavButtonListener(String action) {
            this.action = action;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            lastActivity = System.currentTimeMillis();

            switch (action) {
                case "Home":
                    // TODO: Implement home logic
                    break;
                case "Appointments":
                    new AppointmentHistory(userId); // Pass userId to AppointmentHistory
                    break;
                case "Guidance Counselor":
                    new CounselorsList(); // Open the counselors list window
                    break;
                case "Log Out":
                    System.exit(0);
                    break;
            }
        }
    }

    public void checkSessionTimeout() {
        if (System.currentTimeMillis() - lastActivity > TIMEOUT * 1000) {
            JOptionPane.showMessageDialog(this, "Session timed out. Please log in again.", "Session Timeout", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Homepage("User ", 1)); // Pass a sample userId
    }
}
