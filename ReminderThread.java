import java.sql.*;
import java.time.LocalTime;
import java.awt.Toolkit;
import javax.swing.JOptionPane;

public class ReminderThread extends Thread {

    public void run() {

        while(true) {

            try(
                Connection con = DBConnection.getConnection();
                Statement st = con.createStatement();
                ResultSet rs = st.executeQuery(
                "SELECT * FROM tasks WHERE status='Pending'")
            ){

                LocalTime now = LocalTime.now().withSecond(0).withNano(0);

                while(rs.next()) {

                    int id = rs.getInt("id");
                    String task = rs.getString("task_name");

                    Time startTime = rs.getTime("start_time");
                    Time endTime = rs.getTime("end_time");

                    LocalTime start = startTime.toLocalTime().withSecond(0);
                    LocalTime end = endTime.toLocalTime().withSecond(0);

                    // START REMINDER
                    if((now.equals(start) || now.isAfter(start)) && now.isBefore(end)) {

                        System.out.println("\nSTART TASK: " + task);

                        JOptionPane.showMessageDialog(
                                null,
                                "Start Task: " + task
                        );

                        Toolkit.getDefaultToolkit().beep();
                    }

                    if(now.equals(end)) {

                        System.out.println("\nEND TIME: " + task);

                        Toolkit.getDefaultToolkit().beep();

                        String[] options = {"Done","Skipped"};

                        int choice = JOptionPane.showOptionDialog(
                                null,
                                "Task Finished: " + task,
                                "Reminder",
                                JOptionPane.DEFAULT_OPTION,
                                JOptionPane.INFORMATION_MESSAGE,
                                null,
                                options,
                                options[0]
                        );

                        if(choice == 0) {
                            updateStatus(id,"Done");
                        }

                        if(choice == 1) {
                            updateStatus(id,"Skipped");
                        }
                    }
                }

                Thread.sleep(1000);

            } catch(Exception e) {

                e.printStackTrace();
            }
        }
    }

    void updateStatus(int id,String status) {

        try(
            Connection con = DBConnection.getConnection();
            PreparedStatement ps =
            con.prepareStatement(
            "UPDATE tasks SET status=? WHERE id=?")
        ){

            ps.setString(1,status);
            ps.setInt(2,id);

            ps.executeUpdate();

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}