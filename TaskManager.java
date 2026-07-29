import java.sql.*;
import java.util.Scanner;

public class TaskManager {

    Scanner sc = new Scanner(System.in);

    // ADD TASK
    public void addTask() {

        try(
            Connection con = DBConnection.getConnection();
            PreparedStatement ps =
            con.prepareStatement(
            "INSERT INTO tasks(task_name,start_time,end_time,status) VALUES(?,?,?,?)")
        ){

            System.out.print("Enter Task Name: ");
            String name = sc.nextLine();

            System.out.print("Start Time (HH:MM): ");
            String start = sc.nextLine() + ":00";

            System.out.print("End Time (HH:MM): ");
            String end = sc.nextLine() + ":00";

            ps.setString(1,name);
            ps.setString(2,start);
            ps.setString(3,end);
            ps.setString(4,"Pending");

            ps.executeUpdate();

            System.out.println("Task Added");

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    // VIEW TASKS
    public void viewTasks() {

        try(
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM tasks")
        ){

            System.out.println("\n---- TASK LIST ----");

            while(rs.next()) {

                System.out.println(
                rs.getInt("id")+" | "+
                rs.getString("task_name")+" | "+
                rs.getString("start_time")+" | "+
                rs.getString("end_time")+" | "+
                rs.getString("status"));
            }

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    // EDIT TASK
    public void editTask() {

        try(
            Connection con = DBConnection.getConnection();
            PreparedStatement ps =
            con.prepareStatement(
            "UPDATE tasks SET task_name=?, start_time=?, end_time=? WHERE id=?")
        ){

            System.out.print("Enter Task ID to Edit: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("New Task Name: ");
            String name = sc.nextLine();

            System.out.print("New Start Time (HH:MM): ");
            String start = sc.nextLine() + ":00";

            System.out.print("New End Time (HH:MM): ");
            String end = sc.nextLine() + ":00";

            ps.setString(1,name);
            ps.setString(2,start);
            ps.setString(3,end);
            ps.setInt(4,id);

            ps.executeUpdate();

            System.out.println("Task Updated");

        } catch(Exception e) {

            e.printStackTrace();
        }
    }

    // DELETE TASK
    public void deleteTask() {

        try(
            Connection con = DBConnection.getConnection();
            PreparedStatement ps =
            con.prepareStatement(
            "DELETE FROM tasks WHERE id=?")
        ){

            System.out.print("Enter Task ID to Delete: ");
            int id = sc.nextInt();

            ps.setInt(1,id);

            ps.executeUpdate();

            System.out.println("Task Deleted");

        } catch(Exception e) {

            e.printStackTrace();
        }
    }
}