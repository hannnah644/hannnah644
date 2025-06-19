import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AppointmentHistory extends JFrame {
    private final int userId;
    private JTable table;
    private AppointmentTableModel tableModel;

    public AppointmentHistory(int userId) {
        this.userId = userId;

        setTitle("My Appointment History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);
        container.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton backButton = new JButton("\u2190 Back to Home");
        backButton.setBackground(new Color(0x12, 0x83, 0xe7));
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.setFont(new Font("Arial", Font.PLAIN, 14));
        backButton.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            dispose(); // Close this window
            // Optionally reopen Homepage if needed
            // new Homepage("UserName", userId).setVisible(true);
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(Color.WHITE);
        topPanel.add(backButton);

        container.add(topPanel, BorderLayout.NORTH);

        JLabel titleLabel = new JLabel("My Appointment History");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        container.add(titleLabel, BorderLayout.CENTER);

        List<Appointment> appointments = fetchAppointments();

        tableModel = new AppointmentTableModel(appointments);
        table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);

        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(250);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(100);
        columnModel.getColumn(3).setPreferredWidth(450);

        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(0, 123, 255));
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Arial", Font.BOLD, 14));

        table.setDefaultRenderer(Object.class, new AppointmentTableCellRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        container.add(scrollPane, BorderLayout.SOUTH);

        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(topPanel);
        mainPanel.add(titleLabel);
        mainPanel.add(scrollPane);

        setContentPane(mainPanel);

        setVisible(true);
    }

    AppointmentHistory() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private List<Appointment> fetchAppointments() {
        List<Appointment> list = new ArrayList<>();
        String sql = "SELECT ua.id, ua.appointment_date, ua.status, ua.response, " +
                "CONCAT(cl.first_name, ' ', cl.last_name) AS counselor_name " +
                "FROM users_appointments ua " +
                "JOIN counselorslist cl ON ua.counselor_id = cl.id " +
                "WHERE ua.user_id = ? " +
                "ORDER BY ua.appointment_date DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            if (conn == null) {
                JOptionPane.showMessageDialog(this,
                        "Database connection failed.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return list;
            }

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Appointment appt = new Appointment(
                        rs.getInt("id"),
                        rs.getString("counselor_name"),
                        rs.getString("appointment_date"),
                        rs.getString("status"),
                        rs.getString("response")
                );
                list.add(appt);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error fetching appointments:\n" + ex.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return list;
    }

    private static class Appointment {
        int id;
        String counselorName;
        String appointmentDate;
        String status;
        String response;

        public Appointment(int id, String counselorName, String appointmentDate, String status, String response) {
            this.id = id;
            this.counselorName = counselorName;
            this.appointmentDate = appointmentDate;
            this.status = status;
            this.response = (response == null || response.trim().isEmpty()) ? "No response yet." : response;
        }
    }

    private static class AppointmentTableModel extends AbstractTableModel {
        private final String[] columns = {"Counselor Name", "Appointment Date", "Status", "Message"};
        private final List<Appointment> data;

        public AppointmentTableModel(List<Appointment> data) {
            this.data = data;
        }

        @Override
        public int getRowCount() {
            return data.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int column) {
            return columns[column];
        }

        @Override
        public Object getValueAt(int rowIndex, int columnIndex) {
            Appointment appt = data.get(rowIndex);
            switch (columnIndex) {
                case 0:
                    return appt.counselorName;
                case 1:
                    return appt.appointmentDate;
                case 2:
                    return appt.status;
                case 3:
                    return appt.response;
                default:
                    return "";
            }
        }
    }

    private static class AppointmentTableCellRenderer extends DefaultTableCellRenderer {
        private static final Color EVEN_ROW_COLOR = new Color(0xF9, 0xF9, 0xF9);
        private static final Color ODD_ROW_COLOR = Color.WHITE;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            if (!isSelected) {
                c.setBackground(row % 2 == 0 ? EVEN_ROW_COLOR : ODD_ROW_COLOR);
            }

            if (column == 2) {
                String status = value.toString();
                c.setFont(c.getFont().deriveFont(Font.BOLD));
                switch (status.toLowerCase()) {
                    case "unread":
                        c.setForeground(Color.ORANGE);
                        break;
                    case "pending":
                        c.setForeground(new Color(0xFF, 0x99, 0x00));
                        break;
                    case "read":
                        c.setForeground(new Color(0, 128, 0));
                        break;
                    case "done":
                        c.setForeground(Color.GRAY);
                        break;
                    default:
                        c.setForeground(Color.BLACK);
                }
            } else {
                c.setForeground(Color.BLACK);
                c.setFont(c.getFont().deriveFont(Font.PLAIN));
            }

            if (column == 3 && c instanceof JLabel) {
                JLabel label = (JLabel) c;
                label.setToolTipText("<html>" + value.toString().replaceAll("\n", "<br>") + "</html>");
            }

            return c;
        }
    }
     public static void main(String[] args) {
        SwingUtilities.invokeLater(AppointmentHistory::new);
    }
}
