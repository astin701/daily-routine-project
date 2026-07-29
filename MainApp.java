import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        TaskManager manager = new TaskManager();

        ReminderThread reminder = new ReminderThread();
        reminder.start();

        while(true) {

            System.out.println("\n--- DAILY ROUTINE REMINDER ---");
            System.out.println("1 Add Task");
            System.out.println("2 View Tasks");
            System.out.println("3 Edit Task");
            System.out.println("4 Delete Task");
            System.out.println("5 Exit");


            System.out.print("Enter Your Choice :");
            int choice = sc.nextInt();
            sc.nextLine();

            if(choice == 1) {

                manager.addTask();
            }

            else if(choice == 2) {

                manager.viewTasks();
            }

            else if(choice == 3) {

                manager.editTask();
            }

            else if(choice == 4) {

                manager.deleteTask();
            }

            else if(choice == 5) {

                System.exit(0);
            }
        }
    }
}