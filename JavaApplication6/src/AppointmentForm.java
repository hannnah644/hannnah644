import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppointmentForm extends JFrame {
    private JTextField nameField;
    private JTextField ageField;
    private JComboBox<String> departmentComboBox;
    private JTextField emailField;
    private JTextArea issueField;
    private JTextArea needsField;
    private JTextArea additionalField;
    private JComboBox<String> preferredContactComboBox;
    private JTextField messengerAccountField;
    private JComboBox<String> scheduleQuestionComboBox;
    private JTextField appointmentDateField;
    private int counselorId; // Store the counselor ID
    private JLabel counselorNameLabel; // Label to display counselor's name

    public AppointmentForm(int counselorId, String counselorName) {
        this.counselorId = counselorId; // Initialize counselor ID

        setTitle("Guidance Counseling Form");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 750);
        setLocationRelativeTo(null);

        JPanel wrapperPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        wrapperPanel.setBackground(new Color(245, 250, 255));

        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));
        formPanel.setPreferredSize(new Dimension(600, 740));

        // Back button
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        backPanel.setBackground(Color.WHITE);
        JButton backButton = new JButton("< Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> dispose());
        backPanel.add(backButton);
        formPanel.add(backPanel);

        formPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Title
        JLabel titleLabel = new JLabel("Guidance Counseling Form");
        titleLabel.setFont(new Font("Serif", Font.BOLD, 32));
        titleLabel.setForeground(new Color(139, 0, 0));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(titleLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 25)));

        // Counselor Name
        counselorNameLabel = new JLabel("Counselor: " + counselorName);
        counselorNameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        counselorNameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.add(counselorNameLabel);
        formPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Add fields
        nameField = new JTextField();
        formPanel.add(createFieldPanel("Name:", nameField));

        ageField = new JTextField();
        formPanel.add(createFieldPanel("Age:", ageField));

        departmentComboBox = new JComboBox<>(new String[]{
                "Select...",
                "College of Engineering Technology, Architecture and Fine Arts",
                "College of Hospitality Management, Tourism and Nutrition",
                "College of Arts and Sciences",
                "College of Criminal Justice",
                "Teachers College",
                "College of Nursing",
                "College of Midwifery",
                "College of Pharmacy",
                "College of Physical Therapy and Occupational Therapy",
                "College of Business and Accountancy",
                "Graduate School",
                "College of Law",
                "Senior High School"
        });
        formPanel.add(createFieldPanel("Department:", departmentComboBox));

        emailField = new JTextField();
        formPanel.add(createFieldPanel("Email:", emailField));

        issueField = new JTextArea(3, 30);
        formPanel.add(createTextAreaPanel("What issue are you facing?", issueField));

        needsField = new JTextArea(3, 30);
        formPanel.add(createTextAreaPanel("What do you need help with?", needsField));

        additionalField = new JTextArea(3, 30);
        formPanel.add(createTextAreaPanel("Any additional information:", additionalField));

        preferredContactComboBox = new JComboBox<>(new String[]{
                "Select...", "Email", "Messenger", "In-Person"
        });
        formPanel.add(createFieldPanel("Preferred Contact Method:", preferredContactComboBox));

        messengerAccountField = new JTextField();
        JPanel messengerPanel = createFieldPanel("Your Messenger Account:", messengerAccountField);
        messengerPanel.setVisible(false);
        formPanel.add(messengerPanel);

        preferredContactComboBox.addActionListener(e -> {
            messengerPanel.setVisible("Messenger".equals(preferredContactComboBox.getSelectedItem()));
            formPanel.revalidate();
            formPanel.repaint();
        });

        scheduleQuestionComboBox = new JComboBox<>(new String[]{"Select...", "Yes", "No"});
        formPanel.add(createFieldPanel("Do you want to schedule an appointment with your counselor?", scheduleQuestionComboBox));

        appointmentDateField = new JTextField();
        JPanel appointmentDatePanel = createFieldPanel("Select Appointment Date:", appointmentDateField);
        appointmentDatePanel.setVisible(false);
        formPanel.add(appointmentDatePanel);

        scheduleQuestionComboBox.addActionListener(e -> {
            appointmentDatePanel.setVisible("Yes".equals(scheduleQuestionComboBox.getSelectedItem()));
            formPanel.revalidate();
            formPanel.repaint();
        });

        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));
        submitButton.setBackground(new Color(0, 102, 204));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        submitButton.addActionListener(new SubmitButtonListener());
        formPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        formPanel.add(submitButton);

        wrapperPanel.add(formPanel);
        setContentPane(wrapperPanel);
        setVisible(true);
    }

    private JPanel createFieldPanel(String labelText, JComponent field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(field);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        return panel;
    }

    private JPanel createTextAreaPanel(String labelText, JTextArea textArea) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 15));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);

        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));

        panel.add(label);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(scrollPane);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        return panel;
    }

    private class SubmitButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Basic validation example
            if (nameField.getText().trim().isEmpty() ||
                ageField.getText().trim().isEmpty() ||
                "Select...".equals(departmentComboBox.getSelectedItem()) ||
                emailField.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(AppointmentForm.this,
                        "Please fill in all required fields (Name, Age, Department, Email).",
                        "Incomplete Form",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Here you can add logic to save the appointment with the counselorId
            JOptionPane.showMessageDialog(AppointmentForm.this,
                    "Form submitted successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);

            dispose();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppointmentForm(1, "Sample Counselor")); // Sample usage
    }
}
