import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CounselorsList extends JFrame {

    public CounselorsList() {
        setTitle("Counselors List");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Container for the content
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Back button
        JButton backButton = new JButton("< Back");
        backButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        backButton.setBackground(new Color(12, 133, 239));
        backButton.setForeground(Color.WHITE);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            dispose(); // Close the current window
            new Homepage("User      "); // Open the homepage again
        });
        container.add(backButton);

        // Title
        JLabel titleLabel = new JLabel("Counselors List");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(titleLabel);

        // Fetch and display counselors
        List<Counselor> counselors = fetchCounselors();
        if (counselors.isEmpty()) {
            JLabel noCounselorsLabel = new JLabel("No counselors found.");
            noCounselorsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            container.add(noCounselorsLabel);
        } else {
            for (Counselor counselor : counselors) {
                JPanel counselorPanel = createCounselorPanel(counselor);
                container.add(counselorPanel);
            }
        }

        // Add a spacer to separate the list from the footer
        container.add(Box.createVerticalStrut(20));

        // Footer with appointment button
        JButton appointmentButton = new JButton("Appointment Form");
        appointmentButton.setBackground(new Color(0, 123, 255));
        appointmentButton.setForeground(Color.WHITE);
        appointmentButton.setBorderPainted(false);
        appointmentButton.setFocusPainted(false);
        appointmentButton.addActionListener(e -> {
            // Logic to open appointment form
            new AppointmentForm("1"); // Pass a sample counselor ID
        });
        appointmentButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        container.add(appointmentButton);

        // Add container to the frame
        add(container, BorderLayout.CENTER);
        setVisible(true);
    }

    private List<Counselor> fetchCounselors() {
        List<Counselor> counselors = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) { // Use DBUtil to get connection
            if (conn == null) {
                System.err.println("Failed to establish a database connection.");
                return counselors; // Return empty list if connection failed
            }
            Statement stmt = conn.createStatement();
            // Updated SQL query to use 'profile_pic' instead of 'profile_picture'
            String sql = "SELECT id, gmail, name, department, schedule, profile_pic FROM counselorslist"; 
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int id = rs.getInt("id");
                String gmail = rs.getString("gmail");
                String name = rs.getString("name");
                String department = rs.getString("department");
                String schedule = rs.getString("schedule"); // Use the correct column name
                String profilePic = rs.getString("profile_pic"); // Use the correct column name
                counselors.add(new Counselor(id, gmail, name, department, schedule, profilePic)); // Pass the correct profile picture
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return counselors;
    }

    private JPanel createCounselorPanel(Counselor counselor) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.setBackground(new Color(250, 250, 250));
        panel.setPreferredSize(new Dimension(0, 120));
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Profile picture
        JLabel profilePicLabel = new JLabel(new ImageIcon(counselor.getProfilePic()));
        profilePicLabel.setPreferredSize(new Dimension(100, 100));
        profilePicLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        panel.add(profilePicLabel);

        // Info panel
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        panel.add(infoPanel);

        // Name
        JLabel nameLabel = new JLabel(counselor.getName());
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(new Color(0, 123, 255));
        infoPanel.add(nameLabel);

        // Gmail
        JLabel gmailLabel = new JLabel(counselor.getGmail());
        gmailLabel.setForeground(new Color(119, 119, 119));
        infoPanel.add(gmailLabel);

        // Department
        JLabel departmentLabel = new JLabel("Department: " + counselor.getDepartment());
        infoPanel.add(departmentLabel);

        // Schedule
        JLabel scheduleLabel = new JLabel("Available Schedule for Counseling:");
        infoPanel.add(scheduleLabel);
        String[] schedules = counselor.getSchedule().split(","); // Use the correct method
        for (String schedule : schedules) {
            infoPanel.add(new JLabel("- " + schedule.trim()));
        }

        // Appointment button
        JButton appointmentButton = new JButton("Appointment Form");
        appointmentButton.setBackground(new Color(0, 123, 255));
        appointmentButton.setForeground(Color.WHITE);
        appointmentButton.setBorderPainted(false);
        appointmentButton.setFocusPainted(false);
        appointmentButton.addActionListener(e -> {
            new AppointmentForm(String.valueOf(counselor.getId())); // Pass the counselor ID
        });
        infoPanel.add(appointmentButton);

        return panel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CounselorsList::new);
    }
}
