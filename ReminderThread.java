import java.sql.*;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.awt.Toolkit;
import javax.swing.JOptionPane;

public class ReminderThread extends Thread {

    // Keeps track of reminders already shown
    private Set<Integer> startedTasks = new HashSet<>();
    private Set<Integer> endedTasks = new HashSet<>();

    public void run() {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");

        while (true) {

            try (
                Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(
                        "SELECT * FROM tasks WHERE status='Pending'")
            ) {

                LocalTime now = LocalTime.now().withSecond(0).withNano(0);

                while (rs.next()) {

                    int id = rs.getInt("id");
                    String task = rs.getString("task_name");

                    LocalTime start = rs.getTime("start_time")
                                        .toLocalTime()
                                        .withSecond(0)
                                        .withNano(0);

                    LocalTime end = rs.getTime("end_time")
                                      .toLocalTime()
                                      .withSecond(0)
                                      .withNano(0);

                    // Debug Output
                    System.out.println("--------------------------------");
                    System.out.println("Current Time : " + now.format(formatter));
                    System.out.println("Start Time   : " + start.format(formatter));
                    System.out.println("End Time     : " + end.format(formatter));

                    // START REMINDER (Only Once)
                    if (!startedTasks.contains(id)
                            && (now.equals(start) || now.isAfter(start))
                            && now.isBefore(end)) {

                        startedTasks.add(id);

                        Toolkit.getDefaultToolkit().beep();

                        JOptionPane.showMessageDialog(
                                null,
                                "🔔 Start Task\n\n"
                                + "Task : " + task
                                + "\nTime : " + start.format(formatter),
                                "Task Reminder",
                                JOptionPane.INFORMATION_MESSAGE
                        );
                    }

                    // END REMINDER (Only Once)
                    if (!endedTasks.contains(id)
                            && (now.equals(end) || now.isAfter(end))) {

                        endedTasks.add(id);

                        Toolkit.getDefaultToolkit().beep();

                        String[] options = {"Done", "Skipped"};

                        int choice = JOptionPane.showOptionDialog(
                                null,
                                "⏰ Task Finished\n\n"
                                + "Task : " + task
                                + "\nEnd Time : " + end.format(formatter),
                                "Reminder",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                options,
                                options[0]
                        );

                        if (choice == 0) {
                            updateStatus(id, "Done");
                        } else if (choice == 1) {
                            updateStatus(id, "Skipped");
                        }
                    }
                }

                Thread.sleep(1000);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateStatus(int id, String status) {

        try (
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE tasks SET status=? WHERE id=?")
        ) {

            ps.setString(1, status);
            ps.setInt(2, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}